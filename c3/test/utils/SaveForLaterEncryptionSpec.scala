package utils

import models.view.CachedClaim
import models.view.cache.EncryptedCacheHandling
import models.yesNo._
import models.{SortCode, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import org.specs2.mutable.Specification
import play.api.i18n.Lang

/**
 * Created by peterwhitehead on 30/11/2015.
 */
class SaveForLaterEncryptionSpec extends Specification {
  def yourDetails = YourDetails("Mr", None, "H", None, "Dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1, 1, 1986))
  def contactDetails = ContactDetails(MultiLineAddress(Some("123"), Some("Fake street"), None),
    Some("PL18 1AA"), Some("by post"), None, Some("Yes"), Some("blah@blah.com"), Some("blah@blah.com"))
  def theirPersonalDetails = TheirPersonalDetails("Wifey", "Mrs", None, "H", None, "Dawg",
    Some(NationalInsuranceNumber(Some("AA123456A"))), DayMonthYear(1,1,1988),
    YesNoMandWithAddress("No", Some(MultiLineAddress(Some("122"), Some("Fake street"),None)), None))
  def circumstancesReportChange = CircumstancesReportChange("H-dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1,1,1986),
    "blah", "blah", Some("blah"), Some("blah"), Some("blah@blah.com"), Some("blah@blah.com"))
  def howWePayYou = HowWePayYou("Cold, hard cash", "Daily", Some(BankBuildingSocietyDetails(
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "")))
  def yourPartnerPersonalDetails = YourPartnerPersonalDetails(Some("Mrs"), None, Some("H"),
    None, Some("Dawg"), None, Some(NationalInsuranceNumber(Some("AA123456A"))),
    Some(DayMonthYear(1,1,1988)), Some("Cornish"), Some("yes"), Some("yes"), "yes")
  def circumstancesPaymentChange = CircumstancesPaymentChange(YesNoWith2Text("blah", Some("blah"), None),
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "", "Weekly", Some("blah"))
  def circumstancesAddressChange = CircumstancesAddressChange(MultiLineAddress(
    Some("123"), Some("Fake street"), None), Some("PL18 1AA"), YesNoWithDateAndQs("yes",
    Some(DayMonthYear(1,1,1988)), None), MultiLineAddress(Some("124"), Some("Fake street"), None),
    Some("PL18 1AB"), OptYesNoWithText(None, None), YesNoWithAddress(Some("No"),
      Some(MultiLineAddress(Some("121"), Some("Fake Street"), None)), None), None)

  def claim = Claim(CachedClaim.key, List(
    Section(AboutYou, List(yourDetails, contactDetails)),
    Section(CareYouProvide, List(theirPersonalDetails)),
    Section(CircumstancesIdentification, List(circumstancesReportChange)),
    Section(PayDetails, List(howWePayYou)),
    Section(YourPartner, List(yourPartnerPersonalDetails)),
    Section(CircumstancesReportChanges, List(circumstancesPaymentChange, circumstancesAddressChange))
  ), System.currentTimeMillis(), Some(Lang("en")), CachedClaim.key)

  "SaveForLaterEncryption Integration Spec" should {
    "Claim must be encrypted during the save for later process" in new WithApplication {
      val encryptedClaim = SaveForLaterEncryption.encryptClaim(claim, "1234567890123456")
      val newClaim = SaveForLaterEncryption.decryptClaim("1234567890123456", encryptedClaim)

      claim.questionGroup[YourDetails] mustEqual newClaim.questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustEqual newClaim.questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustEqual newClaim.questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesReportChange] mustEqual newClaim.questionGroup[CircumstancesReportChange]
      claim.questionGroup[HowWePayYou] mustEqual newClaim.questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual newClaim.questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustEqual newClaim.questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustEqual newClaim.questionGroup[CircumstancesPaymentChange]
    }

    "Claim must be decrypt claim check status ok" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      val saveForLaterStatus = encryptedCacheHandling.checkSaveForLaterInCache(claimUuid)

      saveForLaterStatus mustEqual "OK"
    }

    "Claim must be decrypt claim check retry count" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      encryptedCacheHandling.resumeSaveForLaterFromCache(createResumeSaveForLaterInvalid(claim), claimUuid) match {
        case Some(saveForLater) =>
          saveForLater.remainingAuthenticationAttempts mustEqual 2
          saveForLater.status mustEqual "FAILED-RETRY"
        case _ => failure("no cache found")
      }
    }

    "Claim must be removed after three failed attempts" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      val resumeKey = createResumeSaveForLaterInvalid(claim)
      for (i <- 1 to 2) {
        encryptedCacheHandling.resumeSaveForLaterFromCache(resumeKey, claimUuid)
      }
      encryptedCacheHandling.resumeSaveForLaterFromCache(resumeKey, claimUuid) match {
        case Some(saveForLater) =>
          saveForLater.remainingAuthenticationAttempts mustEqual 0
          saveForLater.status mustEqual "FAILED-FINAL"
          saveForLater.claim mustEqual null
        case _ => failure("no cache found")
      }
    }

    "Claim must be decrypt claim check invalid key no claim" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid+"1"
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      encryptedCacheHandling.resumeSaveForLaterFromCache(createResumeSaveForLater(claim), claimUuid) match {
        case Some(saveForLater) => failure("should not find cache")
        case _ => success
      }
    }

    "Claim must be decrypt claim check status no claim when invalid claim uuid" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid + "1"
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      val saveForLaterStatus = encryptedCacheHandling.checkSaveForLaterInCache(claimUuid)

      saveForLaterStatus mustEqual "NO-CLAIM"
    }

    "Claim must be decrypt claim check status no claim when no claim in cache" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid
      removeFromCache(encryptedCacheHandling, claimUuid)
      val saveForLaterStatus = encryptedCacheHandling.checkSaveForLaterInCache(claimUuid)

      saveForLaterStatus mustEqual "NO-CLAIM"
    }

    "Check claim encrypted and decryption create same claim during the save for later process" in new WithApplication {
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      val claimUuid = claim.uuid
      removeFromCache(encryptedCacheHandling, claimUuid)
      encryptedCacheHandling.saveForLaterInCache(claim, "/nationality")
      encryptedCacheHandling.resumeSaveForLaterFromCache(createResumeSaveForLater(claim), claimUuid)
      encryptedCacheHandling.cache.get[Claim](claimUuid) match {
        case Some(encryptedClaim) =>
          val newClaim = ClaimEncryption.decrypt(encryptedClaim)
          claim.questionGroup[YourDetails] mustEqual newClaim.questionGroup[YourDetails]
          claim.questionGroup[ContactDetails] mustEqual newClaim.questionGroup[ContactDetails]
          claim.questionGroup[TheirPersonalDetails] mustEqual newClaim.questionGroup[TheirPersonalDetails]
          claim.questionGroup[CircumstancesReportChange] mustEqual newClaim.questionGroup[CircumstancesReportChange]
          claim.questionGroup[HowWePayYou] mustEqual newClaim.questionGroup[HowWePayYou]
          claim.questionGroup[YourPartnerPersonalDetails] mustEqual newClaim.questionGroup[YourPartnerPersonalDetails]
          claim.questionGroup[CircumstancesAddressChange] mustEqual newClaim.questionGroup[CircumstancesAddressChange]
          claim.questionGroup[CircumstancesPaymentChange] mustEqual newClaim.questionGroup[CircumstancesPaymentChange]
        case _ => failure("Couldn't get claim from cache")
      }
    }
  }
  section ("unit")

  def createResumeSaveForLater(claim: Claim) = {
    val yourDetails = claim.section(AboutYou).questionGroup(YourDetails).get.asInstanceOf[YourDetails]
    ResumeSaveForLater(yourDetails.firstName, yourDetails.surname, yourDetails.nationalInsuranceNumber, yourDetails.dateOfBirth)
  }

  def createResumeSaveForLaterInvalid(claim: Claim) = {
    val yourDetails = claim.section(AboutYou).questionGroup(YourDetails).get.asInstanceOf[YourDetails]
    ResumeSaveForLater(yourDetails.firstName + "none", yourDetails.surname, yourDetails.nationalInsuranceNumber, yourDetails.dateOfBirth)
  }

  def removeFromCache(encryptedCacheHandling: EncryptedCacheHandling, claimUuid: String): Unit = {
    encryptedCacheHandling.removeFromCache(claimUuid)
    encryptedCacheHandling.removeFromCache(s"SFL-$claimUuid")
  }
}

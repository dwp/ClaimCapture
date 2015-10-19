package utils

import models.view.{EncryptedCacheHandling, CachedClaim}
import models.yesNo._
import models.{SortCode, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import org.specs2.mutable.Specification
import play.api.Logger
import play.api.cache.Cache
import play.api.i18n.Lang
import play.api.test.FakeRequest
import play.api.mvc.{AnyContent, Request}

class ClaimEncryptionIntegrationSpec extends Specification {

  val yourDetails = YourDetails("Mr", None, "H", None, "Dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1, 1, 1986))
  val contactDetails = ContactDetails(MultiLineAddress(Some("123"), Some("Fake street"), None),
    Some("PL18 1AA"), Some("by post"), None, Some("Yes"), Some("blah@blah.com"), Some("blah@blah.com"))
  val theirPersonalDetails = TheirPersonalDetails("Wifey", "Mrs", None, "H", None, "Dawg",
    Some(NationalInsuranceNumber(Some("AA123456A"))), DayMonthYear(1,1,1988),
    YesNoMandWithAddress("No", Some(MultiLineAddress(Some("122"), Some("Fake street"),None)), None))
  val circumstancesReportChange = CircumstancesReportChange("H-dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1,1,1986),
    "blah", "blah", Some("blah"), Some("blah"), Some("blah@blah.com"), Some("blah@blah.com"))
  val howWePayYou = HowWePayYou("Cold, hard cash", "Daily", Some(BankBuildingSocietyDetails(
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "")))
  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(Some("Mrs"), None, Some("H"),
    None, Some("Dawg"), None, Some(NationalInsuranceNumber(Some("AA123456A"))),
    Some(DayMonthYear(1,1,1988)), Some("Cornish"), Some("yes"), Some("yes"), "yes")
  val circumstancesPaymentChange = CircumstancesPaymentChange(YesNoWith2Text("blah", Some("blah"), None),
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "", "Weekly", Some("blah"))
  val circumstancesAddressChange = CircumstancesAddressChange(MultiLineAddress(
    Some("123"), Some("Fake street"), None), Some("PL18 1AA"), YesNoWithDateAndQs("yes",
    Some(DayMonthYear(1,1,1988)), None), MultiLineAddress(Some("124"), Some("Fake street"), None),
    Some("PL18 1AB"), OptYesNoWithText(None, None), YesNoWithAddress(Some("No"),
      Some(MultiLineAddress(Some("121"), Some("Fake Street"), None)), None), None)

  "ClaimEncryption Integration Spec" should {

    "Claim must be encrypted before entering the cache" in new WithApplication with MockForm with EncryptedCacheHandling with CachedClaim {

      // Override fromCache so that it does not decrypt the Claim object when getting from the cache
      // This demonstrates Claim object was indeed encrypted when in the cache.
      override def fromCache(request: Request[AnyContent], required: Boolean = true): Option[Claim] = {
        val key = keyFrom(request)
        if (key.isEmpty) {
          if (required) {
            // Log an error if session empty or with no cacheKey entry so we know it is not a cache but a cookie issue.
            Logger.error(s"Did not receive Session information for a $cacheKey for ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}. Probably a cookie issue: ${request.cookies.filterNot(_.name.startsWith("_"))}.")
          }
          None
        } else {
          val claim = Cache.getAs[Claim](key) match {
            case Some(c) => Some(c)
            case _ => None
          }
          claim
        }
      }

      val request = FakeRequest().withSession(cacheKey -> claimKey)
      val claim = Claim(cacheKey, List(
        Section(AboutYou, List(yourDetails, contactDetails)),
        Section(CareYouProvide, List(theirPersonalDetails)),
        Section(CircumstancesIdentification, List(circumstancesReportChange)),
        Section(PayDetails, List(howWePayYou)),
        Section(YourPartner, List(yourPartnerPersonalDetails)),
        Section(CircumstancesReportChanges, List(circumstancesPaymentChange, circumstancesAddressChange))
      ), System.currentTimeMillis(), Some(Lang("en")), claimKey)

      saveInCache(claim)
      val claimFromCache = fromCache(request).get // Bypasses decryption

      // Claim object is not ordered so you cannot compare original claim with decrypted claim
      // Individual question groups must be asserted
      claim.questionGroup[YourDetails] mustNotEqual claimFromCache.questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustNotEqual claimFromCache.questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustNotEqual claimFromCache.questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesReportChange] mustNotEqual claimFromCache.questionGroup[CircumstancesReportChange]
      claim.questionGroup[HowWePayYou] mustNotEqual claimFromCache.questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustNotEqual claimFromCache.questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustNotEqual claimFromCache.questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustNotEqual claimFromCache.questionGroup[CircumstancesPaymentChange]

      claim.questionGroup[YourDetails] mustEqual ClaimEncryption.decryptYourDetails(claimFromCache).questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustEqual ClaimEncryption.decryptContactDetails(claimFromCache).questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustEqual ClaimEncryption.decryptTheirPersonalDetails(claimFromCache).questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesReportChange] mustEqual ClaimEncryption.decryptCircumstancesReportChange(claimFromCache).questionGroup[CircumstancesReportChange]
      claim.questionGroup[HowWePayYou] mustEqual ClaimEncryption.decryptHowWePayYou(claimFromCache).questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual ClaimEncryption.decryptYourPartnerPersonalDetails(claimFromCache).questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustEqual ClaimEncryption.decryptCircumstancesAddressChange(claimFromCache).questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustEqual ClaimEncryption.decryptCircumstancesPaymentChange(claimFromCache).questionGroup[CircumstancesPaymentChange]
    }

    "Claim must be decrypted when getting it from the cache" in new WithApplication with MockForm with EncryptedCacheHandling with CachedClaim {
      val request = FakeRequest().withSession(cacheKey -> claimKey)
      val claim = Claim(cacheKey, List(
        Section(AboutYou, List(yourDetails, contactDetails)),
        Section(CareYouProvide, List(theirPersonalDetails)),
        Section(CircumstancesIdentification, List(circumstancesReportChange)),
        Section(PayDetails, List(howWePayYou)),
        Section(YourPartner, List(yourPartnerPersonalDetails)),
        Section(CircumstancesReportChanges, List(circumstancesPaymentChange, circumstancesAddressChange))
      ), System.currentTimeMillis(), Some(Lang("en")), claimKey)

      saveInCache(claim)
      val claimFromCache = fromCache(request).get

      // Claim object is not ordered so you cannot compare original claim with decrypted claim
      // Individual question groups must be asserted
      claim.questionGroup[YourDetails] mustEqual claimFromCache.questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustEqual claimFromCache.questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustEqual claimFromCache.questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesReportChange] mustEqual claimFromCache.questionGroup[CircumstancesReportChange]
      claim.questionGroup[HowWePayYou] mustEqual claimFromCache.questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual claimFromCache.questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustEqual claimFromCache.questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustEqual claimFromCache.questionGroup[CircumstancesPaymentChange]

    }

  }

}

package utils

import models.view.CachedClaim
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
  }
}

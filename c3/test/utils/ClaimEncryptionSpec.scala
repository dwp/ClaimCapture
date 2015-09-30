package utils

import models.yesNo._
import models.{SortCode, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable.Specification
import utils.ClaimEncryption

class ClaimEncryptionSpec extends Specification {

  val yourDetails = YourDetails("Mr", None, "H", None, "Dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1, 1, 1986))
  val contactDetails = ContactDetails(MultiLineAddress(Some("123"), Some("Fake street"), None),
    Some("PL18 1AA"), "by post", None, Some("Yes"), Some("blah@blah.com"), Some("blah@blah.com"))
  val theirPersonalDetails = TheirPersonalDetails("Wifey", "Mrs", None, "H", None, "Dawg",
    Some(NationalInsuranceNumber(Some("AA123456A"))), DayMonthYear(1,1,1988),
    YesNoMandWithAddress("No", Some(MultiLineAddress(Some("122"), Some("Fake street"),None)), None))
  val circumstancesReportChange = CircumstancesReportChange("H-dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1,1,1986),
    "blah", "blah", "blah", Some("blah"), Some("blah@blah.com"), Some("blah@blah.com"))
  val howWePayYou = HowWePayYou("Cold, hard cash", "Daily", Some(BankBuildingSocietyDetails(
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "")))
  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(Some("Mrs"), None, Some("H"),
    None, Some("Dawg"), None, Some(NationalInsuranceNumber(Some("AA123456A"))),
    Some(DayMonthYear(1,1,1988)), Some("Cornish"), Some("yes"), Some("yes"), "yes")
  val circumstancesAddressChange = CircumstancesAddressChange(MultiLineAddress(
    Some("123"), Some("Fake street"), None), Some("PL18 1AA"), YesNoWithDateAndQs("yes",
    Some(DayMonthYear(1,1,1988)), None), MultiLineAddress(Some("124"), Some("Fake street"), None),
    Some("PL18 1AB"), OptYesNoWithText(None, None), YesNoWithAddress(Some("No"),
      Some(MultiLineAddress(Some("121"), Some("Fake Street"), None)), None), None)
  val circumstancesPaymentChange = CircumstancesPaymentChange(YesNoWith2Text("blah", Some("blah"), None),
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "", "Weekly", Some("blah"))

  val claim = Claim(CachedClaim.key, List(
    Section(AboutYou, List(yourDetails, contactDetails)),
    Section(CareYouProvide, List(theirPersonalDetails)),
    Section(CircumstancesIdentification, List(circumstancesReportChange)),
    Section(PayDetails, List(howWePayYou)),
    Section(YourPartner, List(yourPartnerPersonalDetails)),
    Section(CircumstancesReportChanges, List(circumstancesAddressChange, circumstancesPaymentChange))
  ))

  "ClaimEncryption" should {

    "Encrypt the Claim object" in {
      val encryptedClaim = ClaimEncryption.encrypt(claim)
      claim mustNotEqual encryptedClaim
    }
/*
    "Decrypt the Claim object" in {
      val encryptedClaim = ClaimEncryption.encrypt(claim)
      val decryptedClaim = ClaimEncryption.decrypt(encryptedClaim)
      claim mustNotEqual encryptedClaim
      claim mustEqual decryptedClaim
    }
*/
    "Encrypt YourDetails question group" in {
      val encryptedYourDetails = ClaimEncryption.encryptYourDetails(yourDetails)
      yourDetails mustNotEqual encryptedYourDetails
    }

    "Decrypt YourDetails question group" in {
      val encryptedYourDetails = ClaimEncryption.encryptYourDetails(yourDetails)
      val decryptedYourDetails = ClaimEncryption.decryptYourDetails(encryptedYourDetails)
      yourDetails mustEqual decryptedYourDetails
    }

    "Encrypt ContactDetails question group" in {
      val encryptedContactDetails = ClaimEncryption.encryptContactDetails(contactDetails)
      contactDetails mustNotEqual encryptedContactDetails
    }

    "Decrypt ContactDetails question group" in {
      val encryptedContactDetails = ClaimEncryption.encryptContactDetails(contactDetails)
      val decryptedContactDetails = ClaimEncryption.decryptContactDetails(encryptedContactDetails)
      contactDetails mustEqual decryptedContactDetails
    }

    "Encrypt TheirPersonalDetails question group" in {
      val encryptedTheirPersonalDetails = ClaimEncryption.encryptTheirPersonalDetails(theirPersonalDetails)
      theirPersonalDetails mustNotEqual encryptedTheirPersonalDetails
    }

    "Decrypt TheirPersonalDetails question group" in {
      val encryptedTheirPersonalDetails = ClaimEncryption.encryptTheirPersonalDetails(theirPersonalDetails)
      val decryptedTheirPersonalDetails = ClaimEncryption.decryptTheirPersonalDetails(encryptedTheirPersonalDetails)
      theirPersonalDetails mustEqual decryptedTheirPersonalDetails
    }

    "Encrypt CircumstancesReportChange question group" in {
      val encryptedData = ClaimEncryption.encryptCircumstancesReportChange(circumstancesReportChange)
      circumstancesReportChange mustNotEqual encryptedData
    }

    "Decrypt CircumstancesReportChange question group" in {
      val encryptedData = ClaimEncryption.encryptCircumstancesReportChange(circumstancesReportChange)
      val decryptedData = ClaimEncryption.decryptCircumstancesReportChange(encryptedData)
      circumstancesReportChange mustEqual decryptedData
    }

    "Encrypt HowWePayYou question group" in {
      val encryptedHowWePayYou = ClaimEncryption.encryptHowWePayYou(howWePayYou)
      howWePayYou mustNotEqual encryptedHowWePayYou
    }

    "Decrypt HowWePayYou question group" in {
      val encryptedHowWePayYou = ClaimEncryption.encryptHowWePayYou(howWePayYou)
      val decryptedHowWePayYou = ClaimEncryption.decryptHowWePayYou(encryptedHowWePayYou)
      howWePayYou mustEqual decryptedHowWePayYou
    }

    "Encrypt YourPartnerPersonalDetails question group" in {
      val encryptedYourPartnerPersonalDetails = ClaimEncryption.encryptYourPartnerPersonalDetails(yourPartnerPersonalDetails)
      yourPartnerPersonalDetails mustNotEqual encryptedYourPartnerPersonalDetails
    }

    "Decrypt YourPartnerPersonalDetails question group" in {
      val encryptedYourPartnerPersonalDetails = ClaimEncryption.encryptYourPartnerPersonalDetails(yourPartnerPersonalDetails)
      val decryptedYourPartnerPersonalDetails = ClaimEncryption.decryptYourPartnerPersonalDetails(encryptedYourPartnerPersonalDetails)
      yourPartnerPersonalDetails mustEqual decryptedYourPartnerPersonalDetails
    }

    "Encrypt CircumstancesAddressChange question group" in {
      val encryptedCircumstancesAddressChange = ClaimEncryption.encryptCircumstancesAddressChange(circumstancesAddressChange)
      circumstancesAddressChange mustNotEqual encryptedCircumstancesAddressChange
    }

    "Decrypt CircumstancesAddressChange question group" in {
      val encryptedCircumstancesAddressChange = ClaimEncryption.encryptCircumstancesAddressChange(circumstancesAddressChange)
      val decryptedCircumstancesAddressChange = ClaimEncryption.decryptCircumstancesAddressChange(encryptedCircumstancesAddressChange)
      circumstancesAddressChange mustEqual decryptedCircumstancesAddressChange
    }

    "Encrypt CircumstancesPaymentChange question group" in {
      val encryptedCircumstancesPaymentChange = ClaimEncryption.encryptCircumstancesPaymentChange(circumstancesPaymentChange)
      circumstancesPaymentChange mustNotEqual encryptedCircumstancesPaymentChange
    }

    "Decrypt CircumstancesPaymentChange guestion group" in {
      val encryptedCircumstancesPaymentChange = ClaimEncryption.encryptCircumstancesPaymentChange(circumstancesPaymentChange)
      val decryptedCircumstancesPaymentChange = ClaimEncryption.decryptCircumstancesPaymentChange(encryptedCircumstancesPaymentChange)
      circumstancesPaymentChange mustEqual decryptedCircumstancesPaymentChange
    }

  }

}

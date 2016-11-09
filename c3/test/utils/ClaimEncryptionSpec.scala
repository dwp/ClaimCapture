package utils

import java.util.UUID._

import models.yesNo._
import models.{SortCode, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._

class ClaimEncryptionSpec extends Specification {

  def yourDetails = YourDetails("Mr", "H", None, "Dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1, 1, 1986))
  def contactDetails = ContactDetails(MultiLineAddress(Some("123"), Some("Fake street"), None),
    Some("PL18 1AA"), Some("by post"), None, "Yes", Some("blah@blah.com"), Some("blah@blah.com"))
  def theirPersonalDetails = TheirPersonalDetails("Mrs", "H", None, "Dawg",
    Some(NationalInsuranceNumber(Some("AA123456A"))), DayMonthYear(1,1,1988),"Wifey",
    YesNoMandWithAddress("No", Some(MultiLineAddress(Some("122"), Some("Fake street"),None)), None))
  def circumstancesReportChange = CircumstancesYourDetails("H","Dawg",
    NationalInsuranceNumber(Some("AA123456A")), DayMonthYear(1,1,1986),
    "blah", "blah", "blah", Some("blah"), "blah", Some("blah@blah.com"), Some("blah@blah.com"))
  def howWePayYou = HowWePayYou("Daily", Some(BankBuildingSocietyDetails(
    "H-dawg", "Barclays", SortCode("00", "00", "00"), "00000000", "")),"Cold, hard cash")
  def yourPartnerPersonalDetails = YourPartnerPersonalDetails(Some("Mrs"), Some("H"),
    None, Some("Dawg"), None, Some(NationalInsuranceNumber(Some("AA123456A"))),
    Some(DayMonthYear(1,1,1988)), Some("Cornish"), Some("yes"), Some("yes"), "yes")
  def circumstancesPaymentChange = CircumstancesPaymentChange("blah", Some("blah"), None,
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
  ))

  section("unit")
  "ClaimEncryption" should {
    "Encrypt the Claim object" in new WithApplication {
      val encryptedClaim = ClaimEncryption.encrypt(claim)
      claim mustNotEqual encryptedClaim

      // Claim object is not ordered so you cannot compare original claim with decrypted claim
      // Individual question groups must be asserted
      claim.questionGroup[YourDetails] mustNotEqual encryptedClaim.questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustNotEqual encryptedClaim.questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustNotEqual encryptedClaim.questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesYourDetails] mustNotEqual encryptedClaim.questionGroup[CircumstancesYourDetails]
      claim.questionGroup[HowWePayYou] mustNotEqual encryptedClaim.questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustNotEqual encryptedClaim.questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustNotEqual encryptedClaim.questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustNotEqual encryptedClaim.questionGroup[CircumstancesPaymentChange]

      claim.questionGroup[YourDetails] mustEqual ClaimEncryption.decryptYourDetails(encryptedClaim).questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustEqual ClaimEncryption.decryptContactDetails(encryptedClaim).questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustEqual ClaimEncryption.decryptTheirPersonalDetails(encryptedClaim).questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesYourDetails] mustEqual ClaimEncryption.decryptCircumstancesReportChange(encryptedClaim).questionGroup[CircumstancesYourDetails]
      claim.questionGroup[HowWePayYou] mustEqual ClaimEncryption.decryptHowWePayYou(encryptedClaim).questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual ClaimEncryption.decryptYourPartnerPersonalDetails(encryptedClaim).questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustEqual ClaimEncryption.decryptCircumstancesAddressChange(encryptedClaim).questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustEqual ClaimEncryption.decryptCircumstancesPaymentChange(encryptedClaim).questionGroup[CircumstancesPaymentChange]
    }

    "Decrypt the Claim object" in new WithApplication {
      val encryptedClaim = ClaimEncryption.encrypt(claim)
      val decryptedClaim = ClaimEncryption.decrypt(encryptedClaim)
      claim mustNotEqual encryptedClaim

      // Claim object is not ordered so you cannot compare original claim with decrypted claim
      // Individual question groups must be asserted
      claim.questionGroup[YourDetails] mustEqual decryptedClaim.questionGroup[YourDetails]
      claim.questionGroup[ContactDetails] mustEqual decryptedClaim.questionGroup[ContactDetails]
      claim.questionGroup[TheirPersonalDetails] mustEqual decryptedClaim.questionGroup[TheirPersonalDetails]
      claim.questionGroup[CircumstancesYourDetails] mustEqual decryptedClaim.questionGroup[CircumstancesYourDetails]
      claim.questionGroup[HowWePayYou] mustEqual decryptedClaim.questionGroup[HowWePayYou]
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual decryptedClaim.questionGroup[YourPartnerPersonalDetails]
      claim.questionGroup[CircumstancesAddressChange] mustEqual decryptedClaim.questionGroup[CircumstancesAddressChange]
      claim.questionGroup[CircumstancesPaymentChange] mustEqual decryptedClaim.questionGroup[CircumstancesPaymentChange]
    }

    "Encrypt YourDetails question group" in new WithApplication {
      val encryptedYourDetails = ClaimEncryption.encryptYourDetails(claim)
      claim.questionGroup[YourDetails] mustNotEqual encryptedYourDetails.questionGroup[YourDetails]
    }

    "Decrypt YourDetails question group" in new WithApplication {
      val encryptedYourDetails = ClaimEncryption.encryptYourDetails(claim)
      val decryptedYourDetails = ClaimEncryption.decryptYourDetails(encryptedYourDetails)
      claim.questionGroup[YourDetails] mustEqual decryptedYourDetails.questionGroup[YourDetails]
    }

    "Encrypt ContactDetails question group" in new WithApplication {
      val encryptedContactDetails = ClaimEncryption.encryptContactDetails(claim)
      claim.questionGroup[ContactDetails] mustNotEqual encryptedContactDetails.questionGroup[ContactDetails]
    }

    "Decrypt ContactDetails question group" in new WithApplication {
      val encryptedContactDetails = ClaimEncryption.encryptContactDetails(claim)
      val decryptedContactDetails = ClaimEncryption.decryptContactDetails(encryptedContactDetails)
      claim.questionGroup[ContactDetails] mustEqual decryptedContactDetails.questionGroup[ContactDetails]
    }

    "Encrypt TheirPersonalDetails question group" in new WithApplication {
      val encryptedTheirPersonalDetails = ClaimEncryption.encryptTheirPersonalDetails(claim)
      claim.questionGroup[TheirPersonalDetails] mustNotEqual encryptedTheirPersonalDetails.questionGroup[TheirPersonalDetails]
    }

    "Decrypt TheirPersonalDetails question group" in new WithApplication {
      val encryptedTheirPersonalDetails = ClaimEncryption.encryptTheirPersonalDetails(claim)
      val decryptedTheirPersonalDetails = ClaimEncryption.decryptTheirPersonalDetails(encryptedTheirPersonalDetails)
      claim.questionGroup[TheirPersonalDetails] mustEqual decryptedTheirPersonalDetails.questionGroup[TheirPersonalDetails]
    }

    "Encrypt CircumstancesReportChange question group" in new WithApplication {
      val encryptedData = ClaimEncryption.encryptCircumstancesReportChange(claim)
      claim.questionGroup[CircumstancesYourDetails] mustNotEqual encryptedData.questionGroup[CircumstancesYourDetails]
    }

    "Decrypt CircumstancesReportChange question group" in new WithApplication {
      val encryptedData = ClaimEncryption.encryptCircumstancesReportChange(claim)
      val decryptedData = ClaimEncryption.decryptCircumstancesReportChange(encryptedData)
      claim.questionGroup[CircumstancesYourDetails] mustEqual decryptedData.questionGroup[CircumstancesYourDetails]
    }

    "Encrypt HowWePayYou question group" in new WithApplication {
      val encryptedHowWePayYou = ClaimEncryption.encryptHowWePayYou(claim)
      claim.questionGroup[HowWePayYou] mustNotEqual encryptedHowWePayYou.questionGroup[HowWePayYou]
    }

    "Decrypt HowWePayYou question group" in new WithApplication {
      val encryptedHowWePayYou = ClaimEncryption.encryptHowWePayYou(claim)
      val decryptedHowWePayYou = ClaimEncryption.decryptHowWePayYou(encryptedHowWePayYou)
      claim.questionGroup[HowWePayYou] mustEqual decryptedHowWePayYou.questionGroup[HowWePayYou]
    }

    "Encrypt YourPartnerPersonalDetails question group" in new WithApplication {
      val encryptedYourPartnerPersonalDetails = ClaimEncryption.encryptYourPartnerPersonalDetails(claim)
      claim.questionGroup[YourPartnerPersonalDetails] mustNotEqual encryptedYourPartnerPersonalDetails.questionGroup[YourPartnerPersonalDetails]
    }

    "Decrypt YourPartnerPersonalDetails question group" in new WithApplication {
      val encryptedYourPartnerPersonalDetails = ClaimEncryption.encryptYourPartnerPersonalDetails(claim)
      val decryptedYourPartnerPersonalDetails = ClaimEncryption.decryptYourPartnerPersonalDetails(encryptedYourPartnerPersonalDetails)
      claim.questionGroup[YourPartnerPersonalDetails] mustEqual decryptedYourPartnerPersonalDetails.questionGroup[YourPartnerPersonalDetails]
    }

    "Encrypt CircumstancesAddressChange question group" in new WithApplication {
      val encryptedCircumstancesAddressChange = ClaimEncryption.encryptCircumstancesAddressChange(claim)
      claim.questionGroup[CircumstancesAddressChange] mustNotEqual encryptedCircumstancesAddressChange.questionGroup[CircumstancesAddressChange]
    }

    "Decrypt CircumstancesAddressChange question group" in new WithApplication {
      val encryptedCircumstancesAddressChange = ClaimEncryption.encryptCircumstancesAddressChange(claim)
      val decryptedCircumstancesAddressChange = ClaimEncryption.decryptCircumstancesAddressChange(encryptedCircumstancesAddressChange)
      claim.questionGroup[CircumstancesAddressChange] mustEqual decryptedCircumstancesAddressChange.questionGroup[CircumstancesAddressChange]
    }

    "Encrypt CircumstancesPaymentChange question group" in new WithApplication {
      val encryptedCircumstancesPaymentChange = ClaimEncryption.encryptCircumstancesPaymentChange(claim)
      claim.questionGroup[CircumstancesPaymentChange] mustNotEqual encryptedCircumstancesPaymentChange.questionGroup[CircumstancesPaymentChange]
    }

    "Decrypt CircumstancesPaymentChange guestion group" in new WithApplication {
      val encryptedCircumstancesPaymentChange = ClaimEncryption.encryptCircumstancesPaymentChange(claim)
      val decryptedCircumstancesPaymentChange = ClaimEncryption.decryptCircumstancesPaymentChange(encryptedCircumstancesPaymentChange)
      claim.questionGroup[CircumstancesPaymentChange] mustEqual decryptedCircumstancesPaymentChange.questionGroup[CircumstancesPaymentChange]
    }

    "Encrypt and Decrypt uuid as string" in new WithApplication {
      val uuid = randomUUID.toString
      val encryptedUuid = ClaimEncryption.encryptUuid(uuid)
      encryptedUuid mustNotEqual uuid
      val decryptedUuid = ClaimEncryption.decryptUuid(encryptedUuid)
      decryptedUuid mustEqual uuid
    }
  }
  section("unit")
}

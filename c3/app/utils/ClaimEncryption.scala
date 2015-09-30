package utils

import models.domain._
import utils.C3Encryption._

object ClaimEncryption {

  def encrypt(claim: Claim): Claim = {
    val claimWithYourDetails = if (claim.questionGroup[YourDetails].isDefined) claim.update(encryptYourDetails(claim.questionGroup[YourDetails].get))
      else claim
    println(claimWithYourDetails)
    claimWithYourDetails
  }

  def encryptYourDetails(yourDetails: YourDetails) = {
    yourDetails.copy(
      encryptString(yourDetails.title),
      encryptOptionalString(yourDetails.titleOther),
      encryptString(yourDetails.firstName),
      encryptOptionalString(yourDetails.middleName),
      encryptString(yourDetails.surname),
      encryptNationalInsuranceNumber(yourDetails.nationalInsuranceNumber),
      yourDetails.dateOfBirth
    )
  }

  def encryptContactDetails(contactDetails: ContactDetails) = {
    contactDetails.copy(
      encryptMultiLineAddress(contactDetails.address),
      encryptOptionalString(contactDetails.postcode),
      encryptString(contactDetails.howWeContactYou),
      contactDetails.contactYouByTextphone,
      contactDetails.wantsContactEmail,
      encryptOptionalString(contactDetails.email),
      encryptOptionalString(contactDetails.emailConfirmation)
    )
  }

  def encryptTheirPersonalDetails(theirPersonalDetails: TheirPersonalDetails) = {
    theirPersonalDetails.copy(
      encryptString(theirPersonalDetails.relationship),
      encryptString(theirPersonalDetails.title),
      encryptOptionalString(theirPersonalDetails.titleOther),
      encryptString(theirPersonalDetails.firstName),
      encryptOptionalString(theirPersonalDetails.middleName),
      encryptString(theirPersonalDetails.surname),
      encryptOptionalNationalInsuranceNumber(theirPersonalDetails.nationalInsuranceNumber),
      theirPersonalDetails.dateOfBirth,
      encryptYesNoMandWithAddress(theirPersonalDetails.theirAddress)
    )
  }

  def encryptCircumstancesReportChange(circumstancesReportChange: CircumstancesReportChange) = {
    circumstancesReportChange.copy(
      encryptString(circumstancesReportChange.fullName),
      encryptNationalInsuranceNumber(circumstancesReportChange.nationalInsuranceNumber),
      circumstancesReportChange.dateOfBirth,
      encryptString(circumstancesReportChange.theirFullName),
      encryptString(circumstancesReportChange.theirRelationshipToYou),
      encryptString(circumstancesReportChange.furtherInfoContact),
      encryptOptionalString(circumstancesReportChange.wantsContactEmail),
      encryptOptionalString(circumstancesReportChange.email),
      encryptOptionalString(circumstancesReportChange.emailConfirmation)
    )
  }

  def encryptHowWePayYou(howWePayYou: HowWePayYou) = {
    howWePayYou.copy(
      encryptString(howWePayYou.likeToBePaid),
      encryptString(howWePayYou.paymentFrequency),
      encryptOptionalBankBuildingSoceityDetails(howWePayYou.bankDetails)
    )
  }

  def encryptYourPartnerPersonalDetails(yourPartnerPersonalDetails: YourPartnerPersonalDetails) = {
    yourPartnerPersonalDetails.copy(
      encryptOptionalString(yourPartnerPersonalDetails.title),
      encryptOptionalString(yourPartnerPersonalDetails.titleOther),
      encryptOptionalString(yourPartnerPersonalDetails.firstName),
      encryptOptionalString(yourPartnerPersonalDetails.middleName),
      encryptOptionalString(yourPartnerPersonalDetails.surname),
      encryptOptionalString(yourPartnerPersonalDetails.otherSurnames),
      encryptOptionalNationalInsuranceNumber(yourPartnerPersonalDetails.nationalInsuranceNumber),
      yourPartnerPersonalDetails.dateOfBirth,
      encryptOptionalString(yourPartnerPersonalDetails.nationality),
      encryptOptionalString(yourPartnerPersonalDetails.separatedFromPartner),
      encryptOptionalString(yourPartnerPersonalDetails.isPartnerPersonYouCareFor),
      encryptString(yourPartnerPersonalDetails.hadPartnerSinceClaimDate)
    )
  }

  def encryptCircumstancesAddressChange(circumstancesAddressChange: CircumstancesAddressChange) = {
    circumstancesAddressChange.copy(
      encryptMultiLineAddress(circumstancesAddressChange.previousAddress),
      encryptOptionalString(circumstancesAddressChange.previousPostcode),
      circumstancesAddressChange.stillCaring,
      encryptMultiLineAddress(circumstancesAddressChange.newAddress),
      encryptOptionalString(circumstancesAddressChange.newPostcode),
      circumstancesAddressChange.caredForChangedAddress,
      encryptYesNoWithAddress(circumstancesAddressChange.sameAddress),
      encryptOptionalString(circumstancesAddressChange.moreAboutChanges)
    )
  }

  def encryptCircumstancesPaymentChange(circumstancesPaymentChange: CircumstancesPaymentChange) = {
    circumstancesPaymentChange.copy(
      encryptYesNoWith2Text(circumstancesPaymentChange.currentlyPaidIntoBank),
      encryptString(circumstancesPaymentChange.accountHolderName),
      encryptString(circumstancesPaymentChange.bankFullName),
      encryptSortCode(circumstancesPaymentChange.sortCode),
      encryptString(circumstancesPaymentChange.accountNumber),
      encryptString(circumstancesPaymentChange.rollOrReferenceNumber),
      encryptString(circumstancesPaymentChange.paymentFrequency),
      encryptOptionalString(circumstancesPaymentChange.moreAboutChanges)
    )
  }

  def decryptYourDetails(yourDetails: YourDetails) = {
    yourDetails.copy(
      decryptString(yourDetails.title),
      decryptOptionalString(yourDetails.titleOther),
      decryptString(yourDetails.firstName),
      decryptOptionalString(yourDetails.middleName),
      decryptString(yourDetails.surname),
      decryptNationalInsuranceNumber(yourDetails.nationalInsuranceNumber),
      yourDetails.dateOfBirth
    )
  }

  def decryptContactDetails(contactDetails: ContactDetails) = {
    contactDetails.copy(
      decryptMultiLineAddress(contactDetails.address),
      decryptOptionalString(contactDetails.postcode),
      decryptString(contactDetails.howWeContactYou),
      contactDetails.contactYouByTextphone,
      contactDetails.wantsContactEmail,
      decryptOptionalString(contactDetails.email),
      decryptOptionalString(contactDetails.emailConfirmation)
    )
  }

  def decryptTheirPersonalDetails(theirPersonalDetails: TheirPersonalDetails) = {
    theirPersonalDetails.copy(
      decryptString(theirPersonalDetails.relationship),
      decryptString(theirPersonalDetails.title),
      decryptOptionalString(theirPersonalDetails.titleOther),
      decryptString(theirPersonalDetails.firstName),
      decryptOptionalString(theirPersonalDetails.middleName),
      decryptString(theirPersonalDetails.surname),
      decryptOptionalNationalInsuranceNumber(theirPersonalDetails.nationalInsuranceNumber),
      theirPersonalDetails.dateOfBirth,
      decryptYesNoMandWithAddress(theirPersonalDetails.theirAddress)
    )
  }

  def decryptCircumstancesReportChange(circumstancesReportChange: CircumstancesReportChange) = {
    circumstancesReportChange.copy(
      decryptString(circumstancesReportChange.fullName),
      decryptNationalInsuranceNumber(circumstancesReportChange.nationalInsuranceNumber),
      circumstancesReportChange.dateOfBirth,
      decryptString(circumstancesReportChange.theirFullName),
      decryptString(circumstancesReportChange.theirRelationshipToYou),
      decryptString(circumstancesReportChange.furtherInfoContact),
      decryptOptionalString(circumstancesReportChange.wantsContactEmail),
      decryptOptionalString(circumstancesReportChange.email),
      decryptOptionalString(circumstancesReportChange.emailConfirmation)
    )
  }

  def decryptHowWePayYou(howWePayYou: HowWePayYou) = {
    howWePayYou.copy(
      decryptString(howWePayYou.likeToBePaid),
      decryptString(howWePayYou.paymentFrequency),
      decryptOptionalBankBuildingSoceityDetails(howWePayYou.bankDetails)
    )
  }

  def decryptYourPartnerPersonalDetails(yourPartnerPersonalDetails: YourPartnerPersonalDetails) = {
    yourPartnerPersonalDetails.copy(
      decryptOptionalString(yourPartnerPersonalDetails.title),
      decryptOptionalString(yourPartnerPersonalDetails.titleOther),
      decryptOptionalString(yourPartnerPersonalDetails.firstName),
      decryptOptionalString(yourPartnerPersonalDetails.middleName),
      decryptOptionalString(yourPartnerPersonalDetails.surname),
      decryptOptionalString(yourPartnerPersonalDetails.otherSurnames),
      decryptOptionalNationalInsuranceNumber(yourPartnerPersonalDetails.nationalInsuranceNumber),
      yourPartnerPersonalDetails.dateOfBirth,
      decryptOptionalString(yourPartnerPersonalDetails.nationality),
      decryptOptionalString(yourPartnerPersonalDetails.separatedFromPartner),
      decryptOptionalString(yourPartnerPersonalDetails.isPartnerPersonYouCareFor),
      decryptString(yourPartnerPersonalDetails.hadPartnerSinceClaimDate)
    )
  }

  def decryptCircumstancesAddressChange(circumstancesAddressChange: CircumstancesAddressChange) = {
    circumstancesAddressChange.copy(
      decryptMultiLineAddress(circumstancesAddressChange.previousAddress),
      decryptOptionalString(circumstancesAddressChange.previousPostcode),
      circumstancesAddressChange.stillCaring,
      decryptMultiLineAddress(circumstancesAddressChange.newAddress),
      decryptOptionalString(circumstancesAddressChange.newPostcode),
      circumstancesAddressChange.caredForChangedAddress,
      decryptYesNoWithAddress(circumstancesAddressChange.sameAddress),
      decryptOptionalString(circumstancesAddressChange.moreAboutChanges)
    )
  }

  def decryptCircumstancesPaymentChange(circumstancesPaymentChange: CircumstancesPaymentChange) = {
    circumstancesPaymentChange.copy(
      decryptYesNoWith2Text(circumstancesPaymentChange.currentlyPaidIntoBank),
      decryptString(circumstancesPaymentChange.accountHolderName),
      decryptString(circumstancesPaymentChange.bankFullName),
      decryptSortCode(circumstancesPaymentChange.sortCode),
      decryptString(circumstancesPaymentChange.accountNumber),
      decryptString(circumstancesPaymentChange.rollOrReferenceNumber),
      decryptString(circumstancesPaymentChange.paymentFrequency),
      decryptOptionalString(circumstancesPaymentChange.moreAboutChanges)
    )
  }

}

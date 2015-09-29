package utils

import models.yesNo.{YesNoWithAddress, YesNoMandWithAddress}
import org.specs2.mutable.Specification
import models.{SortCode, MultiLineAddress, NationalInsuranceNumber}
import models.domain.BankBuildingSocietyDetails
import utils.Encryption
import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES

class EncryptionSpec extends Specification {

  "Encryption" should {

    "Encrypt Option[String] values" in {
      val optionalString = Some("Barney")
      val encryptedOptionalString = Encryption.encryptOptionalString(optionalString)
      val decryptedOptionalString = Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedOptionalString.get)))
      optionalString mustNotEqual encryptedOptionalString
      optionalString mustEqual decryptedOptionalString
      Encryption.encryptOptionalString(None) mustEqual None
    }

    "Decrypt Option[String] values" in {
      val optionalString = Some("Barney")
      val encryptedOptionalString = Encryption.encryptOptionalString(optionalString)
      val decryptedOptionalString = Encryption.decryptOptionalString(encryptedOptionalString)
      optionalString mustNotEqual encryptedOptionalString
      optionalString mustEqual decryptedOptionalString
      Encryption.decryptOptionalString(None) mustEqual None
    }

    "Encrypt MultiLineAddress values" in {
      val multiLineAddress = MultiLineAddress(Some("123"), Some("Fake Street"), None)
      val encryptedMultiLineAddress = Encryption.encryptMultiLineAddress(multiLineAddress)
      val decryptedLineOne = Encryption.decryptOptionalString(encryptedMultiLineAddress.lineOne)
      val decryptedLineTwo = Encryption.decryptOptionalString(encryptedMultiLineAddress.lineTwo)
      val decryptedLineThree = Encryption.decryptOptionalString(encryptedMultiLineAddress.lineThree)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress.lineOne mustEqual decryptedLineOne
      multiLineAddress.lineTwo mustEqual decryptedLineTwo
      multiLineAddress.lineThree mustEqual decryptedLineThree
    }

    "Decrypt MultiLineAddress values" in {
      val multiLineAddress = MultiLineAddress(Some("123"), Some("Fake Street"), None)
      val encryptedMultiLineAddress = Encryption.encryptMultiLineAddress(multiLineAddress)
      val decryptedMultiLineAddress = Encryption.decryptMultiLineAddress(encryptedMultiLineAddress)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress mustEqual decryptedMultiLineAddress
    }

    "Encrypt NationalInsuranceNumber values" in {
      val nationalInsuranceNumber = NationalInsuranceNumber(Some("AA123456A"))
      val encryptedNationalInsuranceNumber = Encryption.encryptNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.nino.get))))
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Decrypt NationalInsuranceNumber values" in {
      val nationalInsuranceNumber = NationalInsuranceNumber(Some("AA123456A"))
      val encryptedNationalInsuranceNumber = Encryption.encryptNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = Encryption.decryptNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Encrypt Option[NationalInsuranceNumber] values" in {
      val nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA123456A")))
      val encryptedNationalInsuranceNumber = Encryption.encryptOptionalNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = Some(NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.get.nino.get)))))
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      Encryption.encryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Decrypt Option[NationalInsuranceNumber] values" in {
      val nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA123456A")))
      val encryptedNationalInsuranceNumber = Encryption.encryptOptionalNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = Encryption.decryptOptionalNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      Encryption.decryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Encrypt Option[MultiLineAddress] values" in {
      val multiLineAddress = Some(MultiLineAddress(Some("123"), Some("Fake Street"), None))
      val encryptedMultiLineAddress = Encryption.encryptOptionalMultiLineAddress(multiLineAddress)
      val decryptedLineOne = Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineOne)
      val decryptedLineTwo = Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineTwo)
      val decryptedLineThree = Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineThree)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress.get.lineOne mustEqual decryptedLineOne
      multiLineAddress.get.lineTwo mustEqual decryptedLineTwo
      multiLineAddress.get.lineThree mustEqual decryptedLineThree
      Encryption.encryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Decrypt Option[MultiLineAddress] values" in {
      val multiLineAddress = Some(MultiLineAddress(Some("123"), Some("Fake Street"), None))
      val encryptedMultiLineAddress = Encryption.encryptOptionalMultiLineAddress(multiLineAddress)
      val decryptedMultiLineAddress = Encryption.decryptOptionalMultiLineAddress(encryptedMultiLineAddress)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress mustEqual decryptedMultiLineAddress
      Encryption.decryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Encrypt YesNoMandWithAddress values" in {
      val yesNoMandWithAddress = YesNoMandWithAddress("No",
        Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
      val encryptedAddress = Encryption.encryptYesNoMandWithAddress(yesNoMandWithAddress)
      val decryptedAnswer = (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedAddress.answer))
      val decryptedLineOne = Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineOne
      val decryptedLineTwo = Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineTwo
      val decryptedLineThree = Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineThree
      val decryptedPostCode = Encryption.decryptOptionalString(yesNoMandWithAddress.postCode)
      yesNoMandWithAddress mustNotEqual encryptedAddress
      yesNoMandWithAddress.answer mustEqual decryptedAnswer
      yesNoMandWithAddress.address.get.lineOne mustEqual decryptedLineOne
      yesNoMandWithAddress.address.get.lineTwo mustEqual decryptedLineTwo
      yesNoMandWithAddress.address.get.lineThree mustEqual decryptedLineThree
      yesNoMandWithAddress.postCode mustEqual decryptedPostCode
    }

    "Decrypt YesNoMandWithAddress values" in {
      val yesNoMandWithAddress = YesNoMandWithAddress("No",
        Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
      val encryptedAddress = Encryption.encryptYesNoMandWithAddress(yesNoMandWithAddress)
      val decryptedAddress = Encryption.decryptYesNoMandWithAddress(encryptedAddress)
      yesNoMandWithAddress mustNotEqual encryptedAddress
      yesNoMandWithAddress mustEqual decryptedAddress
    }

    "Encrypt YesNoWithAddress values" in {
      val yesNoWithAddress = YesNoWithAddress(Some("No"),
        Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
      val encryptedAddress = Encryption.encryptYesNoWithAddress(yesNoWithAddress)
      val decryptedAnswer = Encryption.decryptOptionalString(encryptedAddress.answer)
      val decryptedLineOne = Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineOne
      val decryptedLineTwo = Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineTwo
      val decryptedLineThree = Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineThree
      val decryptedPostCode = Encryption.decryptOptionalString(yesNoWithAddress.postCode)
      yesNoWithAddress mustNotEqual encryptedAddress
      yesNoWithAddress.answer mustEqual decryptedAnswer
      yesNoWithAddress.address.get.lineOne mustEqual decryptedLineOne
      yesNoWithAddress.address.get.lineTwo mustEqual decryptedLineTwo
      yesNoWithAddress.address.get.lineThree mustEqual decryptedLineThree
      yesNoWithAddress.postCode mustEqual decryptedPostCode
    }

    "Decrypt YesNoWithAddress values" in {
      val yesNoWithAddress = YesNoWithAddress(Some("No"),
        Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
      val encryptedAddress = Encryption.encryptYesNoWithAddress(yesNoWithAddress)
      val decryptedAddress = Encryption.decryptYesNoWithAddress(encryptedAddress)
      yesNoWithAddress mustNotEqual encryptedAddress
      yesNoWithAddress mustEqual decryptedAddress
    }

    "Encrypt SortCode values" in {
      val sortCode = SortCode("00", "00", "00")
      val encryptedSortCode = Encryption.encryptSortCode(sortCode)
      val decryptedSort1 = Encryption.decryptString(encryptedSortCode.sort1)
      val decryptedSort2 = Encryption.decryptString(encryptedSortCode.sort2)
      val decryptedSort3 = Encryption.decryptString(encryptedSortCode.sort3)
      sortCode mustNotEqual encryptedSortCode
      sortCode.sort1 mustEqual decryptedSort1
      sortCode.sort2 mustEqual decryptedSort2
      sortCode.sort3 mustEqual decryptedSort3
    }

    "Decrypt SortCode values" in {
      val sortCode = SortCode("00", "00", "00")
      val encryptedSortCode = Encryption.encryptSortCode(sortCode)
      val decryptedSortCode = Encryption.decryptSortCode(encryptedSortCode)
      sortCode mustNotEqual encryptedSortCode
      sortCode mustEqual decryptedSortCode
    }

    "Encrypt Option[BankBuildingSoceityDetails]" in {
      val bankBuildingSoceityDetails = Some(BankBuildingSocietyDetails("blah", "blah", SortCode("00", "00", "00"),
        "blah", "blah"))
      val encryptedBankBuildingSoceityDetails = Encryption.encryptOptionalBankBuildingSoceityDetails(bankBuildingSoceityDetails)
      val decryptedAccountHolderName = Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountHolderName)
      val decryptedBankFullName = Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.bankFullName)
      val decryptedSortCode = Encryption.decryptSortCode(encryptedBankBuildingSoceityDetails.get.sortCode)
      val decryptedAccountNumber = Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountNumber)
      val decryptedRollOrReference = Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.rollOrReferenceNumber)
      bankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      bankBuildingSoceityDetails.get.accountHolderName mustEqual decryptedAccountHolderName
      bankBuildingSoceityDetails.get.bankFullName mustEqual decryptedBankFullName
      bankBuildingSoceityDetails.get.sortCode mustEqual decryptedSortCode
      bankBuildingSoceityDetails.get.accountNumber mustEqual decryptedAccountNumber
      bankBuildingSoceityDetails.get.rollOrReferenceNumber mustEqual decryptedRollOrReference
      Encryption.encryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

    "Decrypt Option[BankBuildingSoceityDetails] values" in {
      val bankBuildingSoceityDetails = Some(BankBuildingSocietyDetails("blah", "blah", SortCode("00", "00", "00"),
        "blah", "blah"))
      val encryptedBankBuildingSoceityDetails = Encryption.encryptOptionalBankBuildingSoceityDetails(bankBuildingSoceityDetails)
      val decryptedBankBuildingSoceityDetails = Encryption.decryptOptionalBankBuildingSoceityDetails(encryptedBankBuildingSoceityDetails)
      bankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      bankBuildingSoceityDetails mustEqual decryptedBankBuildingSoceityDetails
      Encryption.decryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

  }

}

package utils

import models.yesNo.{YesNoWith2Text, YesNoWithAddress, YesNoMandWithAddress}
import org.specs2.mutable.Specification
import models.{DayMonthYear, SortCode, MultiLineAddress, NationalInsuranceNumber}
import models.domain.BankBuildingSocietyDetails
import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES

class C3EncryptionSpec extends Specification {

  val string = "Barney"
  val optionalString = Some("Barney")
  val multiLineAddress = MultiLineAddress(Some("123"), Some("Fake Street"), None)
  val nationalInsuranceNumber = NationalInsuranceNumber(Some("AA123456A"))
  val optionalNationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA123456A")))
  val optionalMultiLineAddress = Some(MultiLineAddress(Some("123"), Some("Fake Street"), None))
  val yesNoMandWithAddress = YesNoMandWithAddress("No",
    Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
  val yesNoWithAddress = YesNoWithAddress(Some("No"),
    Some(MultiLineAddress(Some("123"), Some("Fake street"), None)), Some("PL18 1AA"))
  val sortCode = SortCode("00", "00", "00")
  val optionalBankBuildingSoceityDetails = Some(BankBuildingSocietyDetails("blah", "blah", SortCode("00", "00", "00"),
    "blah", "blah"))
  val yesNoWith2Text = YesNoWith2Text("blah", Some("blah"), Some("blah"))
  val optionalDayMonthYear = Some(DayMonthYear(1,2,2000))

  "C3Encryption" should {

    "Not decrypt an already decrypted string" in {
      val encryptedString = C3Encryption.encryptString(string)
      val decryptedString = C3Encryption.decryptString(encryptedString)
      val reDecryptedString = C3Encryption.decryptString(decryptedString)
      decryptedString mustEqual reDecryptedString
    }

    "Encrypt Option[String] values" in {
      val encryptedOptionalString = C3Encryption.encryptOptionalString(optionalString)
      val decryptedOptionalString = Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedOptionalString.get)))
      optionalString mustNotEqual encryptedOptionalString
      optionalString mustEqual decryptedOptionalString
      C3Encryption.encryptOptionalString(None) mustEqual None
    }

    "Decrypt Option[String] values" in {
      val encryptedOptionalString = C3Encryption.encryptOptionalString(optionalString)
      val decryptedOptionalString = C3Encryption.decryptOptionalString(encryptedOptionalString)
      optionalString mustNotEqual encryptedOptionalString
      optionalString mustEqual decryptedOptionalString
      C3Encryption.decryptOptionalString(None) mustEqual None
    }

    "Encrypt MultiLineAddress values" in {
      val encryptedMultiLineAddress = C3Encryption.encryptMultiLineAddress(multiLineAddress)
      val decryptedLineOne = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineOne)
      val decryptedLineTwo = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineTwo)
      val decryptedLineThree = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineThree)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress.lineOne mustEqual decryptedLineOne
      multiLineAddress.lineTwo mustEqual decryptedLineTwo
      multiLineAddress.lineThree mustEqual decryptedLineThree
    }

    "Decrypt MultiLineAddress values" in {
      val encryptedMultiLineAddress = C3Encryption.encryptMultiLineAddress(multiLineAddress)
      val decryptedMultiLineAddress = C3Encryption.decryptMultiLineAddress(encryptedMultiLineAddress)
      multiLineAddress mustNotEqual encryptedMultiLineAddress
      multiLineAddress mustEqual decryptedMultiLineAddress
    }

    "Encrypt NationalInsuranceNumber values" in {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.nino.get))))
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Decrypt NationalInsuranceNumber values" in {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptNationalInsuranceNumber(nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = C3Encryption.decryptNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Encrypt Option[NationalInsuranceNumber] values" in {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptOptionalNationalInsuranceNumber(optionalNationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = Some(NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.get.nino.get)))))
      optionalNationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      optionalNationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      C3Encryption.encryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Decrypt Option[NationalInsuranceNumber] values" in {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptOptionalNationalInsuranceNumber(optionalNationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = C3Encryption.decryptOptionalNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      optionalNationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      optionalNationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      C3Encryption.decryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Encrypt Option[MultiLineAddress] values" in {
      val encryptedMultiLineAddress = C3Encryption.encryptOptionalMultiLineAddress(optionalMultiLineAddress)
      val decryptedLineOne = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineOne)
      val decryptedLineTwo = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineTwo)
      val decryptedLineThree = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineThree)
      optionalMultiLineAddress mustNotEqual encryptedMultiLineAddress
      optionalMultiLineAddress.get.lineOne mustEqual decryptedLineOne
      optionalMultiLineAddress.get.lineTwo mustEqual decryptedLineTwo
      optionalMultiLineAddress.get.lineThree mustEqual decryptedLineThree
      C3Encryption.encryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Decrypt Option[MultiLineAddress] values" in {
      val encryptedMultiLineAddress = C3Encryption.encryptOptionalMultiLineAddress(optionalMultiLineAddress)
      val decryptedMultiLineAddress = C3Encryption.decryptOptionalMultiLineAddress(encryptedMultiLineAddress)
      optionalMultiLineAddress mustNotEqual encryptedMultiLineAddress
      optionalMultiLineAddress mustEqual decryptedMultiLineAddress
      C3Encryption.decryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Encrypt YesNoMandWithAddress values" in {
      val encryptedAddress = C3Encryption.encryptYesNoMandWithAddress(yesNoMandWithAddress)
      val decryptedAnswer = (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedAddress.answer))
      val decryptedLineOne = C3Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineOne
      val decryptedLineTwo = C3Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineTwo
      val decryptedLineThree = C3Encryption.decryptOptionalMultiLineAddress(yesNoMandWithAddress.address).get.lineThree
      val decryptedPostCode = C3Encryption.decryptOptionalString(yesNoMandWithAddress.postCode)
      yesNoMandWithAddress mustNotEqual encryptedAddress
      yesNoMandWithAddress.answer mustEqual decryptedAnswer
      yesNoMandWithAddress.address.get.lineOne mustEqual decryptedLineOne
      yesNoMandWithAddress.address.get.lineTwo mustEqual decryptedLineTwo
      yesNoMandWithAddress.address.get.lineThree mustEqual decryptedLineThree
      yesNoMandWithAddress.postCode mustEqual decryptedPostCode
    }

    "Decrypt YesNoMandWithAddress values" in {
      val encryptedAddress = C3Encryption.encryptYesNoMandWithAddress(yesNoMandWithAddress)
      val decryptedAddress = C3Encryption.decryptYesNoMandWithAddress(encryptedAddress)
      yesNoMandWithAddress mustNotEqual encryptedAddress
      yesNoMandWithAddress mustEqual decryptedAddress
    }

    "Encrypt YesNoWithAddress values" in {
      val encryptedAddress = C3Encryption.encryptYesNoWithAddress(yesNoWithAddress)
      val decryptedAnswer = C3Encryption.decryptOptionalString(encryptedAddress.answer)
      val decryptedLineOne = C3Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineOne
      val decryptedLineTwo = C3Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineTwo
      val decryptedLineThree = C3Encryption.decryptOptionalMultiLineAddress(yesNoWithAddress.address).get.lineThree
      val decryptedPostCode = C3Encryption.decryptOptionalString(yesNoWithAddress.postCode)
      yesNoWithAddress mustNotEqual encryptedAddress
      yesNoWithAddress.answer mustEqual decryptedAnswer
      yesNoWithAddress.address.get.lineOne mustEqual decryptedLineOne
      yesNoWithAddress.address.get.lineTwo mustEqual decryptedLineTwo
      yesNoWithAddress.address.get.lineThree mustEqual decryptedLineThree
      yesNoWithAddress.postCode mustEqual decryptedPostCode
    }

    "Encrypt Option[DayMonthYear] values" in {
      // Tests for DayMonthYear are in models.DayMonthYearSpec
      val encryptedOptionalDayMonthYear = C3Encryption.encryptOptionalDayMonthYear(optionalDayMonthYear)
      val decryptedOptionalDayMonthYear = Some(encryptedOptionalDayMonthYear.get.decrypt)
      optionalDayMonthYear mustNotEqual encryptedOptionalDayMonthYear
      optionalDayMonthYear mustEqual decryptedOptionalDayMonthYear
    }

    "Decrypt Option[DayMonthYear] values" in {
      // Tests for DayMonthYear are in models.DayMonthYearSpec
      val encryptedOptionalDayMonthYear = C3Encryption.encryptOptionalDayMonthYear(optionalDayMonthYear)
      val decryptedOptionalDayMonthYear = C3Encryption.decryptOptionalDayMonthYear(encryptedOptionalDayMonthYear)
      optionalDayMonthYear mustEqual decryptedOptionalDayMonthYear
    }

    "Decrypt YesNoWithAddress values" in {
      val encryptedAddress = C3Encryption.encryptYesNoWithAddress(yesNoWithAddress)
      val decryptedAddress = C3Encryption.decryptYesNoWithAddress(encryptedAddress)
      yesNoWithAddress mustNotEqual encryptedAddress
      yesNoWithAddress mustEqual decryptedAddress
    }

    "Encrypt SortCode values" in {
      val encryptedSortCode = C3Encryption.encryptSortCode(sortCode)
      val decryptedSort1 = C3Encryption.decryptString(encryptedSortCode.sort1)
      val decryptedSort2 = C3Encryption.decryptString(encryptedSortCode.sort2)
      val decryptedSort3 = C3Encryption.decryptString(encryptedSortCode.sort3)
      sortCode mustNotEqual encryptedSortCode
      sortCode.sort1 mustEqual decryptedSort1
      sortCode.sort2 mustEqual decryptedSort2
      sortCode.sort3 mustEqual decryptedSort3
    }

    "Decrypt SortCode values" in {
      val encryptedSortCode = C3Encryption.encryptSortCode(sortCode)
      val decryptedSortCode = C3Encryption.decryptSortCode(encryptedSortCode)
      sortCode mustNotEqual encryptedSortCode
      sortCode mustEqual decryptedSortCode
    }

    "Encrypt Option[BankBuildingSoceityDetails]" in {
      val encryptedBankBuildingSoceityDetails = C3Encryption.encryptOptionalBankBuildingSoceityDetails(optionalBankBuildingSoceityDetails)
      val decryptedAccountHolderName = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountHolderName)
      val decryptedBankFullName = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.bankFullName)
      val decryptedSortCode = C3Encryption.decryptSortCode(encryptedBankBuildingSoceityDetails.get.sortCode)
      val decryptedAccountNumber = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountNumber)
      val decryptedRollOrReference = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.rollOrReferenceNumber)
      optionalBankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      optionalBankBuildingSoceityDetails.get.accountHolderName mustEqual decryptedAccountHolderName
      optionalBankBuildingSoceityDetails.get.bankFullName mustEqual decryptedBankFullName
      optionalBankBuildingSoceityDetails.get.sortCode mustEqual decryptedSortCode
      optionalBankBuildingSoceityDetails.get.accountNumber mustEqual decryptedAccountNumber
      optionalBankBuildingSoceityDetails.get.rollOrReferenceNumber mustEqual decryptedRollOrReference
      C3Encryption.encryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

    "Decrypt Option[BankBuildingSoceityDetails] values" in {
      val encryptedBankBuildingSoceityDetails = C3Encryption.encryptOptionalBankBuildingSoceityDetails(optionalBankBuildingSoceityDetails)
      val decryptedBankBuildingSoceityDetails = C3Encryption.decryptOptionalBankBuildingSoceityDetails(encryptedBankBuildingSoceityDetails)
      optionalBankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      optionalBankBuildingSoceityDetails mustEqual decryptedBankBuildingSoceityDetails
      C3Encryption.decryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

    "Encrypt YesNoWith2Text values" in {
      val encryptedYesNoWith2Text = C3Encryption.encryptYesNoWith2Text(yesNoWith2Text)
      val decryptedYesNo = C3Encryption.decryptString(encryptedYesNoWith2Text.answer)
      val decryptedText1 = C3Encryption.decryptOptionalString(encryptedYesNoWith2Text.text1)
      val decryptedText2 = C3Encryption.decryptOptionalString(encryptedYesNoWith2Text.text2)
      yesNoWith2Text mustNotEqual encryptedYesNoWith2Text
      yesNoWith2Text.answer mustEqual decryptedYesNo
      yesNoWith2Text.text1 mustEqual decryptedText1
      yesNoWith2Text.text2 mustEqual decryptedText2
    }

    "Decrypt YesNoWith2Text values" in {
      val encryptedYesNoWith2Text = C3Encryption.encryptYesNoWith2Text(yesNoWith2Text)
      val decryptedYesNoWith2Text = C3Encryption.decryptYesNoWith2Text(encryptedYesNoWith2Text)
      yesNoWith2Text mustNotEqual encryptedYesNoWith2Text
      yesNoWith2Text mustEqual decryptedYesNoWith2Text
    }

  }

}

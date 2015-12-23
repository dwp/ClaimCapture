package utils

import java.util.UUID._

import models.yesNo.{YesNoWith2Text, YesNoWithAddress, YesNoMandWithAddress}
import org.specs2.mutable._
import models.{DayMonthYear, SortCode, MultiLineAddress, NationalInsuranceNumber}
import models.domain.BankBuildingSocietyDetails
import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES
import app.ConfigProperties._

class C3EncryptionSpec extends Specification {

  object ValuesToBeUsed {
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
    val optionalDayMonthYear = Some(DayMonthYear(1, 2, 2000))
  }

  "C3Encryption" should {
    "Not decrypt an already decrypted string" in new WithApplication {
      val encryptedString = C3Encryption.encryptString(ValuesToBeUsed.string)
      val decryptedString = C3Encryption.decryptString(encryptedString)
      val reDecryptedString = C3Encryption.decryptString(decryptedString)
      decryptedString mustEqual reDecryptedString
    }

    "Encrypt Option[String] values" in new WithApplication {
      val encryptedOptionalString = C3Encryption.encryptOptionalString(ValuesToBeUsed.optionalString)
      val decryptedOptionalString = Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedOptionalString.get)))
      ValuesToBeUsed.optionalString mustNotEqual encryptedOptionalString
      ValuesToBeUsed.optionalString mustEqual decryptedOptionalString
      C3Encryption.encryptOptionalString(None) mustEqual None
    }

    "Decrypt Option[String] values" in new WithApplication {
      val encryptedOptionalString = C3Encryption.encryptOptionalString(ValuesToBeUsed.optionalString)
      val decryptedOptionalString = C3Encryption.decryptOptionalString(encryptedOptionalString)
      ValuesToBeUsed.optionalString mustNotEqual encryptedOptionalString
      ValuesToBeUsed.optionalString mustEqual decryptedOptionalString
      C3Encryption.decryptOptionalString(None) mustEqual None
    }

    "Encrypt MultiLineAddress values" in new WithApplication {
      val encryptedMultiLineAddress = C3Encryption.encryptMultiLineAddress(ValuesToBeUsed.multiLineAddress)
      val decryptedLineOne = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineOne)
      val decryptedLineTwo = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineTwo)
      val decryptedLineThree = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.lineThree)
      ValuesToBeUsed.multiLineAddress mustNotEqual encryptedMultiLineAddress
      ValuesToBeUsed.multiLineAddress.lineOne mustEqual decryptedLineOne
      ValuesToBeUsed.multiLineAddress.lineTwo mustEqual decryptedLineTwo
      ValuesToBeUsed.multiLineAddress.lineThree mustEqual decryptedLineThree
    }

    "Decrypt MultiLineAddress values" in new WithApplication {
      val encryptedMultiLineAddress = C3Encryption.encryptMultiLineAddress(ValuesToBeUsed.multiLineAddress)
      val decryptedMultiLineAddress = C3Encryption.decryptMultiLineAddress(encryptedMultiLineAddress)
      ValuesToBeUsed.multiLineAddress mustNotEqual encryptedMultiLineAddress
      ValuesToBeUsed.multiLineAddress mustEqual decryptedMultiLineAddress
    }

    "Encrypt NationalInsuranceNumber values" in new WithApplication {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptNationalInsuranceNumber(ValuesToBeUsed.nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.nino.get))))
      ValuesToBeUsed.nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      ValuesToBeUsed.nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Decrypt NationalInsuranceNumber values" in new WithApplication {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptNationalInsuranceNumber(ValuesToBeUsed.nationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = C3Encryption.decryptNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      ValuesToBeUsed.nationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      ValuesToBeUsed.nationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
    }

    "Encrypt Option[NationalInsuranceNumber] values" in new WithApplication {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptOptionalNationalInsuranceNumber(ValuesToBeUsed.optionalNationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = Some(NationalInsuranceNumber(Some((new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedNationalInsuranceNumber.get.nino.get)))))
      ValuesToBeUsed.optionalNationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      ValuesToBeUsed.optionalNationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      C3Encryption.encryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Decrypt Option[NationalInsuranceNumber] values" in new WithApplication {
      val encryptedNationalInsuranceNumber = C3Encryption.encryptOptionalNationalInsuranceNumber(ValuesToBeUsed.optionalNationalInsuranceNumber)
      val decryptedNationalInsuranceNumber = C3Encryption.decryptOptionalNationalInsuranceNumber(encryptedNationalInsuranceNumber)
      ValuesToBeUsed.optionalNationalInsuranceNumber mustNotEqual encryptedNationalInsuranceNumber
      ValuesToBeUsed.optionalNationalInsuranceNumber mustEqual decryptedNationalInsuranceNumber
      C3Encryption.decryptOptionalNationalInsuranceNumber(None) mustEqual None
    }

    "Encrypt Option[MultiLineAddress] values" in new WithApplication {
      val encryptedMultiLineAddress = C3Encryption.encryptOptionalMultiLineAddress(ValuesToBeUsed.optionalMultiLineAddress)
      val decryptedLineOne = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineOne)
      val decryptedLineTwo = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineTwo)
      val decryptedLineThree = C3Encryption.decryptOptionalString(encryptedMultiLineAddress.get.lineThree)
      ValuesToBeUsed.optionalMultiLineAddress mustNotEqual encryptedMultiLineAddress
      ValuesToBeUsed.optionalMultiLineAddress.get.lineOne mustEqual decryptedLineOne
      ValuesToBeUsed.optionalMultiLineAddress.get.lineTwo mustEqual decryptedLineTwo
      ValuesToBeUsed.optionalMultiLineAddress.get.lineThree mustEqual decryptedLineThree
      C3Encryption.encryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Decrypt Option[MultiLineAddress] values" in new WithApplication {
      val encryptedMultiLineAddress = C3Encryption.encryptOptionalMultiLineAddress(ValuesToBeUsed.optionalMultiLineAddress)
      val decryptedMultiLineAddress = C3Encryption.decryptOptionalMultiLineAddress(encryptedMultiLineAddress)
      ValuesToBeUsed.optionalMultiLineAddress mustNotEqual encryptedMultiLineAddress
      ValuesToBeUsed.optionalMultiLineAddress mustEqual decryptedMultiLineAddress
      C3Encryption.decryptOptionalMultiLineAddress(None) mustEqual None
    }

    "Encrypt YesNoMandWithAddress values" in new WithApplication {
      val encryptedAddress = C3Encryption.encryptYesNoMandWithAddress(ValuesToBeUsed.yesNoMandWithAddress)
      val decryptedAnswer = (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encryptedAddress.answer))
      val decryptedLineOne = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoMandWithAddress.address).get.lineOne
      val decryptedLineTwo = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoMandWithAddress.address).get.lineTwo
      val decryptedLineThree = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoMandWithAddress.address).get.lineThree
      val decryptedPostCode = C3Encryption.decryptOptionalString(ValuesToBeUsed.yesNoMandWithAddress.postCode)
      ValuesToBeUsed.yesNoMandWithAddress mustNotEqual encryptedAddress
      ValuesToBeUsed.yesNoMandWithAddress.answer mustEqual decryptedAnswer
      ValuesToBeUsed.yesNoMandWithAddress.address.get.lineOne mustEqual decryptedLineOne
      ValuesToBeUsed.yesNoMandWithAddress.address.get.lineTwo mustEqual decryptedLineTwo
      ValuesToBeUsed.yesNoMandWithAddress.address.get.lineThree mustEqual decryptedLineThree
      ValuesToBeUsed.yesNoMandWithAddress.postCode mustEqual decryptedPostCode
    }

    "Decrypt YesNoMandWithAddress values" in new WithApplication {
      val encryptedAddress = C3Encryption.encryptYesNoMandWithAddress(ValuesToBeUsed.yesNoMandWithAddress)
      val decryptedAddress = C3Encryption.decryptYesNoMandWithAddress(encryptedAddress)
      ValuesToBeUsed.yesNoMandWithAddress mustNotEqual encryptedAddress
      ValuesToBeUsed.yesNoMandWithAddress mustEqual decryptedAddress
    }

    "Encrypt YesNoWithAddress values" in new WithApplication {
      val encryptedAddress = C3Encryption.encryptYesNoWithAddress(ValuesToBeUsed.yesNoWithAddress)
      val decryptedAnswer = C3Encryption.decryptOptionalString(encryptedAddress.answer)
      val decryptedLineOne = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoWithAddress.address).get.lineOne
      val decryptedLineTwo = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoWithAddress.address).get.lineTwo
      val decryptedLineThree = C3Encryption.decryptOptionalMultiLineAddress(ValuesToBeUsed.yesNoWithAddress.address).get.lineThree
      val decryptedPostCode = C3Encryption.decryptOptionalString(ValuesToBeUsed.yesNoWithAddress.postCode)
      ValuesToBeUsed.yesNoWithAddress mustNotEqual encryptedAddress
      ValuesToBeUsed.yesNoWithAddress.answer mustEqual decryptedAnswer
      ValuesToBeUsed.yesNoWithAddress.address.get.lineOne mustEqual decryptedLineOne
      ValuesToBeUsed.yesNoWithAddress.address.get.lineThree mustEqual decryptedLineThree
      ValuesToBeUsed.yesNoWithAddress.postCode mustEqual decryptedPostCode
    }

    "Encrypt Option[DayMonthYear] values" in new WithApplication {
      // Tests for DayMonthYear are in models.DayMonthYearSpec
      val encryptedOptionalDayMonthYear = C3Encryption.encryptOptionalDayMonthYear(ValuesToBeUsed.optionalDayMonthYear)
      val decryptedOptionalDayMonthYear = Some(encryptedOptionalDayMonthYear.get.decrypt)
      ValuesToBeUsed.optionalDayMonthYear mustNotEqual encryptedOptionalDayMonthYear
      ValuesToBeUsed.optionalDayMonthYear mustEqual decryptedOptionalDayMonthYear
    }

    "Decrypt Option[DayMonthYear] values" in new WithApplication {
      // Tests for DayMonthYear are in models.DayMonthYearSpec
      val encryptedOptionalDayMonthYear = C3Encryption.encryptOptionalDayMonthYear(ValuesToBeUsed.optionalDayMonthYear)
      val decryptedOptionalDayMonthYear = C3Encryption.decryptOptionalDayMonthYear(encryptedOptionalDayMonthYear)
      ValuesToBeUsed.optionalDayMonthYear mustEqual decryptedOptionalDayMonthYear
    }

    "Decrypt YesNoWithAddress values" in new WithApplication {
      val encryptedAddress = C3Encryption.encryptYesNoWithAddress(ValuesToBeUsed.yesNoWithAddress)
      val decryptedAddress = C3Encryption.decryptYesNoWithAddress(encryptedAddress)
      ValuesToBeUsed.yesNoWithAddress mustNotEqual encryptedAddress
      ValuesToBeUsed.yesNoWithAddress mustEqual decryptedAddress
    }

    "Encrypt SortCode values" in new WithApplication {
      val encryptedSortCode = C3Encryption.encryptSortCode(ValuesToBeUsed.sortCode)
      val decryptedSort1 = C3Encryption.decryptString(encryptedSortCode.sort1)
      val decryptedSort2 = C3Encryption.decryptString(encryptedSortCode.sort2)
      val decryptedSort3 = C3Encryption.decryptString(encryptedSortCode.sort3)
      ValuesToBeUsed.sortCode mustNotEqual encryptedSortCode
      ValuesToBeUsed.sortCode.sort1 mustEqual decryptedSort1
      ValuesToBeUsed.sortCode.sort2 mustEqual decryptedSort2
      ValuesToBeUsed.sortCode.sort3 mustEqual decryptedSort3
    }

    "Decrypt SortCode values" in new WithApplication {
      val encryptedSortCode = C3Encryption.encryptSortCode(ValuesToBeUsed.sortCode)
      val decryptedSortCode = C3Encryption.decryptSortCode(encryptedSortCode)
      ValuesToBeUsed.sortCode mustNotEqual encryptedSortCode
      ValuesToBeUsed.sortCode mustEqual decryptedSortCode
    }

    "Encrypt Option[BankBuildingSoceityDetails]" in new WithApplication {
      val encryptedBankBuildingSoceityDetails = C3Encryption.encryptOptionalBankBuildingSoceityDetails(ValuesToBeUsed.optionalBankBuildingSoceityDetails)
      val decryptedAccountHolderName = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountHolderName)
      val decryptedBankFullName = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.bankFullName)
      val decryptedSortCode = C3Encryption.decryptSortCode(encryptedBankBuildingSoceityDetails.get.sortCode)
      val decryptedAccountNumber = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.accountNumber)
      val decryptedRollOrReference = C3Encryption.decryptString(encryptedBankBuildingSoceityDetails.get.rollOrReferenceNumber)
      ValuesToBeUsed.optionalBankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      ValuesToBeUsed.optionalBankBuildingSoceityDetails.get.accountHolderName mustEqual decryptedAccountHolderName
      ValuesToBeUsed.optionalBankBuildingSoceityDetails.get.bankFullName mustEqual decryptedBankFullName
      ValuesToBeUsed.optionalBankBuildingSoceityDetails.get.sortCode mustEqual decryptedSortCode
      ValuesToBeUsed.optionalBankBuildingSoceityDetails.get.accountNumber mustEqual decryptedAccountNumber
      ValuesToBeUsed.optionalBankBuildingSoceityDetails.get.rollOrReferenceNumber mustEqual decryptedRollOrReference
      C3Encryption.encryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

    "Decrypt Option[BankBuildingSoceityDetails] values" in new WithApplication {
      val encryptedBankBuildingSoceityDetails = C3Encryption.encryptOptionalBankBuildingSoceityDetails(ValuesToBeUsed.optionalBankBuildingSoceityDetails)
      val decryptedBankBuildingSoceityDetails = C3Encryption.decryptOptionalBankBuildingSoceityDetails(encryptedBankBuildingSoceityDetails)
      ValuesToBeUsed.optionalBankBuildingSoceityDetails mustNotEqual encryptedBankBuildingSoceityDetails
      ValuesToBeUsed.optionalBankBuildingSoceityDetails mustEqual decryptedBankBuildingSoceityDetails
      C3Encryption.decryptOptionalBankBuildingSoceityDetails(None) mustEqual None
    }

    "Encrypt YesNoWith2Text values" in new WithApplication {
      val encryptedYesNoWith2Text = C3Encryption.encryptYesNoWith2Text(ValuesToBeUsed.yesNoWith2Text)
      val decryptedYesNo = C3Encryption.decryptString(encryptedYesNoWith2Text.answer)
      val decryptedText1 = C3Encryption.decryptOptionalString(encryptedYesNoWith2Text.text1)
      val decryptedText2 = C3Encryption.decryptOptionalString(encryptedYesNoWith2Text.text2)
      ValuesToBeUsed.yesNoWith2Text mustNotEqual encryptedYesNoWith2Text
      ValuesToBeUsed.yesNoWith2Text.answer mustEqual decryptedYesNo
      ValuesToBeUsed.yesNoWith2Text.text1 mustEqual decryptedText1
      ValuesToBeUsed.yesNoWith2Text.text2 mustEqual decryptedText2
    }

    "Decrypt YesNoWith2Text values" in new WithApplication {
      val encryptedYesNoWith2Text = C3Encryption.encryptYesNoWith2Text(ValuesToBeUsed.yesNoWith2Text)
      val decryptedYesNoWith2Text = C3Encryption.decryptYesNoWith2Text(encryptedYesNoWith2Text)
      ValuesToBeUsed.yesNoWith2Text mustNotEqual encryptedYesNoWith2Text
      ValuesToBeUsed.yesNoWith2Text mustEqual decryptedYesNoWith2Text
    }

    "Encrypt uppercase uuid returns empty string" in new WithApplication {
      val uuid = "ABCD1234-0000-0000-0000-ABCD1234CDEF"
      val encrypted = XorEncryption.encryptUuid(uuid)
      encrypted mustEqual ""
    }

    "Encrypt short uuid returns empty string" in new WithApplication {
      val uuid = "ABCD1234-0000-0000-0000-ABCD1234CDE"
      val encrypted = XorEncryption.encryptUuid(uuid)
      encrypted mustEqual ""
    }

    "Encrypt and decrypt a typical lowercase uuid back to original" in new WithApplication {
      val uuid = "abcd1234-0000-0000-0000-abcd1234cdef"
      val encrypted = XorEncryption.encryptUuid(uuid)
      val decrypted = XorEncryption.decryptUuid(encrypted)
      encrypted mustNotEqual uuid
      decrypted mustEqual uuid
    }

    "Encrypt and decrypt a typical 0 leading uuid back to original" in new WithApplication {
      val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
      val encrypted = XorEncryption.encryptUuid(uuid)
      val decrypted = XorEncryption.decryptUuid(encrypted)
      encrypted mustNotEqual uuid
      decrypted mustEqual uuid
    }

    "Encrypt and decrypt a random claim uuid back to original" in new WithApplication {
      val uuid = randomUUID.toString
      val encrypted = XorEncryption.encryptUuid(uuid)
      println( "encrypted uuid:"+uuid+" to "+encrypted)
      val decrypted = XorEncryption.decryptUuid(encrypted)
      encrypted mustNotEqual uuid
      decrypted mustEqual uuid
    }

    "Encrypt and decrypt a zero uuid back to original" in new WithApplication {
      val uuid = "00000000-0000-0000-0000-000000000000"
      val encrypted = XorEncryption.encryptUuid(uuid)
      val decrypted = XorEncryption.decryptUuid(encrypted)
      encrypted mustNotEqual uuid
      decrypted mustEqual uuid
    }

    "Encrypt when no key in properties should return empty string" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLater.uuid.secret.key" -> ""))) {
      val uuid = randomUUID.toString
      val encrypted = XorEncryption.encryptUuid(uuid)
      encrypted mustEqual ""
    }

    "Encrypt when uppercase key in properties should return empty string" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLater.uuid.secret.key" -> "8F7A0412-DF0E-485E-92C6-5B0F17E339C8"))) {
      val uuid = randomUUID.toString
      val encrypted = XorEncryption.encryptUuid(uuid)
      encrypted mustEqual ""
    }

    "Generate a decryption pair-value for use in other tests" in new WithApplication {
      val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
      val encrypted = XorEncryption.encryptUuid(uuid)
      encrypted mustNotEqual uuid
      println("With secretkey of:"+getProperty("saveForLater.uuid.secret.key","")+" created xor pair of:"+uuid+" and:"+encrypted)
    }
  }
}

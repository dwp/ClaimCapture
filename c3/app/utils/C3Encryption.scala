package utils

import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES
import models.yesNo.{YesNoWith2Text, YesNoWithAddress, YesNoMandWithAddress}
import models.{DayMonthYear, SortCode, NationalInsuranceNumber, MultiLineAddress}
import models.domain._
import play.api.Logger

object C3Encryption {

  def encryptString(text: String): String = {
    try {
      DatatypeConverter.printBase64Binary((new  EncryptorAES).encrypt(text))
    } catch {
      case e: Exception =>
        Logger.debug("Could not encrypt string: " + e.getMessage)
        text
    }
  }

  def encryptOptionalString(text: Option[String]): Option[String] ={
    text match {
      case Some(t) => Some(encryptString(t))
      case None => None
    }
  }

  def encryptMultiLineAddress(multiLineAddress: MultiLineAddress): MultiLineAddress = {
    MultiLineAddress(
      encryptOptionalString(multiLineAddress.lineOne),
      encryptOptionalString(multiLineAddress.lineTwo),
      encryptOptionalString(multiLineAddress.lineThree)
    )
  }

  def encryptNationalInsuranceNumber(nationalInsuranceNumber: NationalInsuranceNumber): NationalInsuranceNumber = {
    NationalInsuranceNumber(encryptOptionalString(nationalInsuranceNumber.nino))
  }

  def encryptOptionalNationalInsuranceNumber(nationalInsuranceNumber: Option[NationalInsuranceNumber]) = {
    nationalInsuranceNumber match {
      case Some(nino) => Some(encryptNationalInsuranceNumber(nino))
      case None => None
    }
  }

  def encryptOptionalMultiLineAddress(multiLineAddress: Option[MultiLineAddress]) = {
    multiLineAddress match {
      case Some(address) => Some(encryptMultiLineAddress(address))
      case None => None
    }
  }

  def encryptYesNoMandWithAddress(yesNoMandWithAddress: YesNoMandWithAddress) = {
    YesNoMandWithAddress(
      encryptString(yesNoMandWithAddress.answer),
      encryptOptionalMultiLineAddress(yesNoMandWithAddress.address),
      encryptOptionalString(yesNoMandWithAddress.postCode)
    )
  }
  def encryptYesNoWithAddress(yesNoWithAddress: YesNoWithAddress) = {
    YesNoWithAddress(
      encryptOptionalString(yesNoWithAddress.answer),
      encryptOptionalMultiLineAddress(yesNoWithAddress.address),
      encryptOptionalString(yesNoWithAddress.postCode)
    )
  }

  def encryptSortCode(sortCode: SortCode): SortCode = {
    SortCode(
      encryptString(sortCode.sort1),
      encryptString(sortCode.sort2),
      encryptString(sortCode.sort3)
    )
  }

  def encryptOptionalBankBuildingSoceityDetails(bankBuildingSocietyDetails: Option[BankBuildingSocietyDetails]) = {
    bankBuildingSocietyDetails match {
      case Some(bankDetails) => Some(BankBuildingSocietyDetails(
        encryptString(bankDetails.accountHolderName),
        encryptString(bankDetails.bankFullName),
        encryptSortCode(bankDetails.sortCode),
        encryptString(bankDetails.accountNumber),
        encryptString(bankDetails.rollOrReferenceNumber))
      )
      case None => None
    }
  }

  def encryptYesNoWith2Text(yesNoWith2Text: YesNoWith2Text) = {
    YesNoWith2Text(
      encryptString(yesNoWith2Text.answer),
      encryptOptionalString(yesNoWith2Text.text1),
      encryptOptionalString(yesNoWith2Text.text2)
    )
  }

  def encryptDayMonthYear(dayMonthYear: DayMonthYear) = {
    dayMonthYear.encrypt
  }

  def encryptOptionalDayMonthYear(dayMonthYear: Option[DayMonthYear]) = {
    dayMonthYear match {
      case Some(dmy) => Some(dmy.encrypt)
      case None => None
    }
  }

  def decryptString(text: String): String = {
    try {
      (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(text))
    } catch {
      case e: Exception =>
        Logger.debug("Could not decrypt string: " + e.getMessage)
        text
    }
  }

  def decryptOptionalString(text: Option[String]): Option[String] = {
    text match {
      case Some(t) => Some(decryptString(t))
      case None => None
    }
  }

  def decryptMultiLineAddress(multiLineAddress: MultiLineAddress): MultiLineAddress = {
    MultiLineAddress(
      decryptOptionalString(multiLineAddress.lineOne),
      decryptOptionalString(multiLineAddress.lineTwo),
      decryptOptionalString(multiLineAddress.lineThree)
    )
  }

  def decryptNationalInsuranceNumber(nationalInsuranceNumber: NationalInsuranceNumber): NationalInsuranceNumber = {
    NationalInsuranceNumber(decryptOptionalString(nationalInsuranceNumber.nino))
  }

  def decryptOptionalNationalInsuranceNumber(nationalInsuranceNumber: Option[NationalInsuranceNumber]) = {
    nationalInsuranceNumber match {
      case Some(nino) => Some(decryptNationalInsuranceNumber(nino))
      case None => None
    }
  }

  def decryptOptionalMultiLineAddress(multiLineAddress: Option[MultiLineAddress]) = {
    multiLineAddress match {
      case Some(address) => Some(decryptMultiLineAddress(address))
      case None => None
    }
  }

  def decryptYesNoMandWithAddress(yesNoMandWithAddress: YesNoMandWithAddress) = {
    YesNoMandWithAddress(
      decryptString(yesNoMandWithAddress.answer),
      decryptOptionalMultiLineAddress(yesNoMandWithAddress.address),
      decryptOptionalString(yesNoMandWithAddress.postCode)
    )
  }

  def decryptYesNoWithAddress(yesNoWithAddress: YesNoWithAddress) = {
    YesNoWithAddress(
      decryptOptionalString(yesNoWithAddress.answer),
      decryptOptionalMultiLineAddress(yesNoWithAddress.address),
      decryptOptionalString(yesNoWithAddress.postCode)
    )
  }

  def decryptSortCode(sortCode: SortCode): SortCode = {
    SortCode(
      decryptString(sortCode.sort1),
      decryptString(sortCode.sort2),
      decryptString(sortCode.sort3)
    )
  }

  def decryptOptionalBankBuildingSoceityDetails(bankBuildingSocietyDetails: Option[BankBuildingSocietyDetails]) = {
    bankBuildingSocietyDetails match {
      case Some(bankDetails) => Some(BankBuildingSocietyDetails(
        decryptString(bankDetails.accountHolderName),
        decryptString(bankDetails.bankFullName),
        decryptSortCode(bankDetails.sortCode),
        decryptString(bankDetails.accountNumber),
        decryptString(bankDetails.rollOrReferenceNumber))
      )
      case None => None
    }
  }

  def decryptYesNoWith2Text(yesNoWith2Text: YesNoWith2Text) = {
    YesNoWith2Text(
      decryptString(yesNoWith2Text.answer),
      decryptOptionalString(yesNoWith2Text.text1),
      decryptOptionalString(yesNoWith2Text.text2)
    )
  }

  def decryptDayMonthYear(dayMonthYear: DayMonthYear) = {
    dayMonthYear.decrypt
  }

  def decryptOptionalDayMonthYear(dayMonthYear: Option[DayMonthYear]) = {
    dayMonthYear match {
      case Some(dmy) => Some(dmy.decrypt)
      case None => None
    }
  }

}

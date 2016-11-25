package controllers

import models.NationalInsuranceNumber
import models.domain.{YourDetails, YourPartnerPersonalDetails, TheirPersonalDetails}
import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation._
import mappings.Mappings._
import play.api.Logger
import play.api.data.format.Formats._
import play.api.mvc.{AnyContent, Request}
import gov.dwp.carers.xml.validation.CommonValidation._
import utils.helpers.CarersCrypto

import scala.util.{Failure, Try, Success}

object CarersForms  {

  val carersText: Mapping[String] =
    text verifying restrictedStringText

  def carersText(minLength: Int = 0, maxLength: Int = Int.MaxValue): Mapping[String] =
    text(minLength, maxLength) verifying restrictedStringText

  val carersNonEmptyText: Mapping[String] =
    text verifying Constraints.nonEmpty verifying restrictedStringText

  def carersNonEmptyText(minLength: Int = 0, maxLength: Int = Int.MaxValue): Mapping[String] =
    nonEmptyText(minLength, maxLength) verifying restrictedStringText

  def carersEmailValidation: Mapping[String] =
    of[String] verifying emailAddress

  private val emailRegex = EMAIL_REGEX.r
  def emailAddress: Constraint[String] = Constraint[String]("constraint.email") { e =>
    if (e == null) Invalid(ValidationError("error.email"))
    else if (e.trim.isEmpty) Invalid(ValidationError("error.email"))
    else emailRegex.findFirstMatchIn(e.trim).map(_ => Valid) match {
      case Some(Valid) => Valid
      case _ => { Logger.info(s"Email: invalid email - $e"); Invalid(ValidationError("error.email")) }
    }
  }

  def formatPostCode(postCode : String) : String = {
    val newPostcode=postCode.trim.split("\\s+").map(_.trim).mkString(" ").toUpperCase()
    if( postCode != newPostcode )
      Logger.info("Masked postcode whitespace stripped from:\""+postCode.replaceAll("[a-zA-Z]", "*")+"\" to:\""+newPostcode.replaceAll("[a-zA-Z]", "*")+"\"")
    newPostcode
  }

  def dpName(theirPersonalDetails: TheirPersonalDetails) = {
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }

  def partnerName(partnerDetails: YourPartnerPersonalDetails) = {
    partnerDetails.firstName.getOrElse("") + " " + partnerDetails.surname.getOrElse("")
  }

  def dpNINO(theirPersonalDetails: TheirPersonalDetails) = {
    theirPersonalDetails.nationalInsuranceNumber.getOrElse(NationalInsuranceNumber(None)).nino.getOrElse("").toUpperCase.replace(" ", "")
  }

  def partnerNINO(partnerDetails: YourPartnerPersonalDetails) = {
    partnerDetails.nationalInsuranceNumber.getOrElse(NationalInsuranceNumber(None)).nino.getOrElse("").toUpperCase.replace(" ", "")
  }

  def yourNINO(yourDetails: YourDetails) = {
    yourDetails.nationalInsuranceNumber.nino.getOrElse("").toUpperCase.replace(" ", "")
  }

  def yourName(yourDetails: YourDetails) = {
    yourDetails.firstName + " " + yourDetails.surname
  }

  def pageName(request: Request[AnyContent]): String = {
    Try(request.body.asFormUrlEncoded.get.get(CarersCrypto.encryptAES("firstName")).get.head + " " + request.body.asFormUrlEncoded.get.get(CarersCrypto.encryptAES("surname")).get.head) match {
      case Success(name: String) => name
      case Failure(_) => ""
    }
  }

  def getValueFromRequest(request: Request[AnyContent], value: String): String = {
    Try(request.body.asFormUrlEncoded.get.get(CarersCrypto.encryptAES(value)).get.head) match {
      case Success(name: String) => name
      case Failure(_) => ""
    }
  }
}

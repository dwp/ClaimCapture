package models.domain

import models.DayMonthYear
import controllers.mappings.Mappings._
import models.NationalInsuranceNumber

object YourPartner extends Section.Identifier {
  val id = "s4"
}

case class YourPartnerPersonalDetails(title: Option[String] = None,
                                      firstName: Option[String] = None,
                                      middleName: Option[String] = None,
                                      surname: Option[String] = None,
                                      otherSurnames: Option[String] = None,
                                      nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                      dateOfBirth: Option[DayMonthYear] = None,
                                      nationality: Option[String] = None,
                                      separatedFromPartner: Option[String] = None,
                                      isPartnerPersonYouCareFor:Option[String] = None,
                                      hadPartnerSinceClaimDate: String = "") extends QuestionGroup(YourPartnerPersonalDetails)

object YourPartnerPersonalDetails extends QuestionGroup.Identifier  {
  val id = s"${YourPartner.id}.g1"

  def validateTitle(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.title.isDefined
    case `no` => true
  }

  def validateFirstName(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.firstName.isDefined
    case `no` => true
  }

  def validateSurName(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.surname.isDefined
    case `no` => true
  }

  def validateDateOfBirth(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.dateOfBirth.isDefined
    case `no` => true
  }

  def validateSeperatedFromPartner(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.separatedFromPartner.isDefined
    case `no` => true
  }

  def validatePartnerPersonYoucareFor(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate match {
    case `yes` => input.isPartnerPersonYouCareFor.isDefined
    case `no` => true
  }

  def validateNationalityIfPresent(input: YourPartnerPersonalDetails): Boolean = input.hadPartnerSinceClaimDate == yes match {
    case true => input.nationality.isDefined
    case false => true
  }

}
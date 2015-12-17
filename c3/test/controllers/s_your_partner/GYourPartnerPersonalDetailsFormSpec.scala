package controllers.s_your_partner

import utils.WithApplication
import controllers.mappings.Mappings
import models.view.CachedClaim
import org.specs2.mutable._
import models.{ NationalInsuranceNumber, DayMonthYear }

class GYourPartnerPersonalDetailsFormSpec extends Specification {

  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val nino = "AB123456C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val nationality = "British"
  val separatedFromPartner = "yes"

  "Your Partner Personal Details Form" should {
    "map data into case class when partner answer is yes" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.title must equalTo(Some(title))
            f.firstName must equalTo(Some(firstName))
            f.middleName must equalTo(Some(middleName))
            f.surname must equalTo(Some(surname))
            f.otherSurnames must equalTo(Some(otherNames))
            f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(nino))))
            f.dateOfBirth must equalTo(Some(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None)))
            f.nationality must equalTo(Some(nationality))
            f.separatedFromPartner must equalTo(Some(separatedFromPartner))
            f.isPartnerPersonYouCareFor must equalTo(Some("yes"))
            f.hadPartnerSinceClaimDate must equalTo("yes")
          })
    }

    "reject too many characters in text fields" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "otherNames" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(3).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(4).message must equalTo("error.nationality")
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 7 mandatory fields" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("hadPartnerSinceClaimDate" -> "yes","middleName" -> "middle optional")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(7)
            formWithErrors.errors(0).message must equalTo("title.required")
            formWithErrors.errors(1).message must equalTo("firstName.required")
            formWithErrors.errors(2).message must equalTo("surname.required")
            formWithErrors.errors(3).message must equalTo("dateOfBirth.required")
            formWithErrors.errors(4).message must equalTo("separated.fromPartner.required")
            formWithErrors.errors(5).message must equalTo("isPartnerPersonYouCareFor.required")
            formWithErrors.errors(6).message must equalTo("partner.nationality.required")
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject form when partner question not answered" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("hadPartnerSinceClaimDate" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2) // error.required and yesNo.invalid
        },
        theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> "INVALID",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "partner.nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject form without partnerispersonyoucarefor" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "United States",
          "separated.fromPartner" -> separatedFromPartner,
          "hadPartnerSinceClaimDate" -> "yes")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("isPartnerPersonYouCareFor.required")
        },f => "This mapping should not happen." must equalTo("Valid"))
    }

    "accept nationality with space character, uppercase and lowercase" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "United States",
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.nationality must equalTo(Some("United States"))
          })
    }

    "reject invalid nationality with numbers" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "a123456",
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.nationality")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid nationality with special characters" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "a!@£$%^&*(){}",
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.nationality")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters" in new WithApplication {
      GYourPartnerPersonalDetails.form(models.domain.Claim(CachedClaim.key)).bind(
        Map("title" -> title,
          "firstName" -> "MyNa>me",
          "middleName" -> "middleNam©e",
          "surname" -> ";My Surn˙h∫ame;",
          "otherNames" -> "other@Names",
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "partner.nationality" -> "United States",
          "separated.fromPartner" -> separatedFromPartner,
          "isPartnerPersonYouCareFor"->"yes",
          "hadPartnerSinceClaimDate" -> "yes")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(4)
          formWithErrors.errors.head.message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section ("unit", models.domain.YourPartner.id)
}

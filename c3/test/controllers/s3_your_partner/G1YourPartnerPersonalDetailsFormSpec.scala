package controllers.s3_your_partner

import org.specs2.mutable.Specification
import models.{NationalInsuranceNumber, DayMonthYear}
import scala.Some

class G1YourPartnerPersonalDetailsFormSpec extends Specification {


  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val ni1 = "AB"
  val ni2 = 12
  val ni3 = 34
  val ni4 = 56
  val ni5 = "C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val nationality = "British"
  val liveAtSameAddress = "yes"

  "Your Partner Personal Details Form" should {
    "map data into case class" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> nationality,
          "liveAtSameAddress" -> liveAtSameAddress
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.title must equalTo(title)
          f.firstName must equalTo(firstName)
          f.middleName must equalTo(Some(middleName))
          f.surname must equalTo(surname)
          f.otherNames must equalTo(Some(otherNames))
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
          f.dateOfBirth must equalTo(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None))
          f.nationality must equalTo(Some(nationality))
          f.liveAtSameAddress must equalTo(liveAtSameAddress)
        }
      )
    }
  }

}

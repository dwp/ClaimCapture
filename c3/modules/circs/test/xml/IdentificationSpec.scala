package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.DayMonthYear
import models.domain.Circs
import models.MultiLineAddress
import models.NationalInsuranceNumber
import scala.Some

class IdentificationSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))


  val yourDetails = CircumstancesAboutYou(title = "mr",
    firstName = "Phil",
    middleName = Some("Joe"),
    lastName = "Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  val careeDetails = DetailsOfThePersonYouCareFor(firstName = "Phil",
    middleName = Some("Joe"),
    lastName = "Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  val contactDetails = CircumstancesYourContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"),
    mobileNumber = Some("01818 118181"))

  "Identification" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Circs()().update(yourDetails).update(contactDetails)
      val xml = Identification.xml(claim.asInstanceOf[Circs])

      (xml \\ "Surname").text shouldEqual yourDetails.lastName
      (xml \\ "OtherNames").text shouldEqual yourDetails.otherNames
      (xml \\ "OtherSurnames").text shouldEqual NotAsked
      (xml \\ "Title").text shouldEqual yourDetails.title
      (xml \\ "MaritalStatus").text shouldEqual NotAsked
      (xml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "Address" \\ "PostCode").text shouldEqual contactDetails.postcode.get
      (xml \\ "ConfirmAddress").text shouldEqual yes
      (xml \\ "HomePhoneNumber").text shouldEqual contactDetails.phoneNumber.get
      (xml \\ "DaytimePhoneNumber" \\ "Number").text shouldEqual contactDetails.mobileNumber.get
      (xml \\ "DaytimePhoneNumber" \\ "Qualifier").text shouldEqual "mobile"
    }

    "generate Caree xml from a given circumstances" in {
      val claim = Circs()().update(careeDetails)
      val xml = Identification.xml(claim.asInstanceOf[Circs])

      (xml \\ "Surname").text shouldEqual careeDetails.lastName
      (xml \\ "OtherNames").text shouldEqual careeDetails.otherNames
      (xml \\ "OtherSurnames").text shouldEqual NotAsked
      (xml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify

    }
  } section "unit"
}
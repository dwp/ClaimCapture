package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}

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

  val contactDetails = CircumstancesYourContactDetails(address = MultiLineAddress(Some("Line1")),
    postcode = Some("PR2 8AE"),
    phoneNumber = "01772 700806",
    mobileNumber = Some("01818 118181"))

  "Identification" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails).update(contactDetails)
      val xml = Identification.xml(claim)

      (xml \\ "Surname").text shouldEqual yourDetails.lastName
      (xml \\ "OtherNames").text shouldEqual yourDetails.otherNames
      (xml \\ "Title").text shouldEqual yourDetails.title
      (xml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "Address" \\ "PostCode").text shouldEqual contactDetails.postcode.get
      (xml \\ "ConfirmAddress").text shouldEqual yes
      (xml \\ "HomePhone").text shouldEqual contactDetails.phoneNumber
      (xml \\ "DaytimePhone" \\ "Number").text shouldEqual contactDetails.mobileNumber.get
      (xml \\ "DaytimePhone" \\ "Qualifier").text shouldEqual "mobile"
    }

    "generate Caree xml from a given circumstances" in {
      val claim = Claim().update(careeDetails)
      val xml = Identification.xml(claim)

      (xml \\ "Surname").text shouldEqual careeDetails.lastName
      (xml \\ "OtherNames").text shouldEqual careeDetails.otherNames
      (xml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify

    }
  } section "unit"
}
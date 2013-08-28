package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.DayMonthYear
import controllers.Mappings.{yes, no}
import models.yesNo.{YesNoWithDropDownAndText, YesNoWithDropDown, YesNoWithDate}
import models.NationalInsuranceNumber
import models.Whereabouts
import models.MultiLineAddress
import models.domain.Break
import app.ActingType._

class CareeSpec extends Specification with Tags {

  "Caree" should {

    "generate <Caree> xml" in {

      val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))
      val dateOfBirth = DayMonthYear(Some(3), Some(4), Some(1950))
      val claimDate = DayMonthYear(Some(10), Some(10), Some(2013))
      val theirPersonalDetails = TheirPersonalDetails(title = "title", firstName = "firstName", middleName = Some("middleName"), surname = "surname", nationalInsuranceNumber = Some(nationalInsuranceNr), dateOfBirth = dateOfBirth)
      val theirContactDetails = TheirContactDetails(address = MultiLineAddress(Some("line1")), postcode = Some("postcode"), phoneNumber = Some("020-12302312"))
      val moreAboutThePerson = MoreAboutThePerson(relationship = "son")
      val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaring = yes, spent35HoursCaringBeforeClaim = YesNoWithDate(no, Some(claimDate)))

      val claim = Claim().update(theirPersonalDetails).update(theirContactDetails).update(moreAboutThePerson).update(moreAboutTheCare)

      val xml = Caree.xml(claim)

      (xml \\ "Surname").text shouldEqual theirPersonalDetails.surname
      (xml \\ "OtherNames").text must contain(theirPersonalDetails.firstName)
      (xml \\ "OtherNames").text must contain(theirPersonalDetails.middleName.get)
      (xml \\ "Title").text shouldEqual theirPersonalDetails.title
      (xml \\ "DateOfBirth").text shouldEqual dateOfBirth.`yyyy-MM-dd`
      (xml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "Address" \\ "Line").theSeq(0).text shouldEqual theirContactDetails.address.lineOne.get
      (xml \\ "Address" \\ "PostCode").text shouldEqual theirContactDetails.postcode.get.toUpperCase
      (xml \\ "DaytimePhoneNumber" \\ "Number").text shouldEqual theirContactDetails.phoneNumber.get
      (xml \\ "RelationToClaimant").text shouldEqual moreAboutThePerson.relationship
      (xml \\ "Cared35hours").text shouldEqual yes
      (xml \\ "BreaksSinceClaim").text shouldEqual no
      (xml \\ "Cared35hoursBefore").text shouldEqual moreAboutTheCare.spent35HoursCaringBeforeClaim.answer
    }

    "generate <BreaksSinceClaim> xml with" in {
      "yes when claimer has breaks and NOT spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None))
        val breaks = BreaksInCare().update(Break())

        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare).update(breaks))
        xml.text shouldEqual yes
      }

      "no when claimer has breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }

      "no when claimer has NO breaks and NOT spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None))
        val xml = Caree.breaksSinceClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }
    }

    "generate <BreaksBeforeClaim> xml with" in {
      "yes when claimer has breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val breaks = BreaksInCare().update(Break())

        val xml = Caree.breaksBeforeClaim(Claim().update(moreAboutTheCare).update(breaks))
        xml.text shouldEqual yes
      }

      "no when claimer has no breaks and spent 35 hours caring BEFORE claim date" in {
        val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, None))
        val xml = Caree.breaksBeforeClaim(Claim().update(moreAboutTheCare))
        xml.text shouldEqual no
      }
    }

    "skip <BreaksBeforeClaim> xml if claimer did NOT spent 35 hours caring BEFORE claim date" in {
      val xml = Caree.breaksBeforeClaim(Claim())
      xml.text must beEmpty
    }

    "generate <CareBreak> xml when claimer has breaks" in {
      val startDate = DayMonthYear(Some(1), Some(2), Some(2012), Some(15), Some(5))
      val endDate = DayMonthYear(Some(1), Some(2), Some(2012))
      val breakOne = Break(id = "1", start = startDate, end = Some(endDate), whereYou = Whereabouts(location = "Netherlands"), medicalDuringBreak = yes)
      val breakTwo = Break(id = "2", whereYou = Whereabouts(location = "Spain"), medicalDuringBreak = no)
      val claim = Claim().update(BreaksInCare().update(breakOne).update(breakTwo))
      val xml = Caree.careBreak(claim)

      (xml \\ "CareBreak").length shouldEqual 2
      val careBreakOneXml = (xml \\ "CareBreak").theSeq(0)
      (careBreakOneXml \\ "StartDateTime").text shouldEqual startDate.`yyyy-MM-dd'T'HH:mm:00`
      (careBreakOneXml \\ "EndDateTime").text shouldEqual endDate.`yyyy-MM-dd'T'HH:mm:00`
      (careBreakOneXml \\ "Reason").text shouldEqual breakOne.whereYou.location
      (careBreakOneXml \\ "MedicalCare").text shouldEqual breakOne.medicalDuringBreak
    }

    "skip <CareBreak> xml when claimer has NO breaks" in {
      val xml = Caree.careBreak(Claim())
      xml.text must beEmpty
    }

    "skip <DateStartedCaring> xml if claimer did NOT spent 35 hours caring BEFORE claim date" in {
      val xml = Caree.dateStartedCaring(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(no, None)))
      xml.text must beEmpty
    }

    "<DateStartedCaring> should contain date if claimer spent 35 hours caring BEFORE claim date" in {
      val startedCaringDate = DayMonthYear(Some(5), Some(5), Some(2000))
      val xml = Caree.dateStartedCaring(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(yes, Some(startedCaringDate))))
      xml.text shouldEqual startedCaringDate.`yyyy-MM-dd`
    }
  } section "unit"
}
package xml.circumstances

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.{CachedChangeOfCircs}
import models.yesNo.YesNoWithDate
import org.specs2.mutable._
import utils.WithApplication

class BreaksSpec extends Specification {
  section("unit")
  "Circs Breaks" should {
    val breakTrailerXml = "<CareBreak><BreaksSinceClaim><QuestionLabel>Have there been any other times you or @dpname have been in hospital, respite or care home?</QuestionLabel><Answer>None</Answer></BreaksSinceClaim><BreaksOtherSinceClaim><QuestionLabel>Have there been any other times you've not provided care for @dpname for 35 hours a week?</QuestionLabel><Answer>No</Answer></BreaksOtherSinceClaim></CareBreak>"
    val hospitalQuestion = "Have there been any times you or @dpname have been in hospital, respite or care home?"
    val hospitalOtherQuestion = "Have there been any other times you or @dpname have been in hospital, respite or care home?"
    val otherQuestion = "Have there been any other times you've not provided care for @dpname for 35 hours a week?"
    lazy val yourDetails = CircumstancesYourDetails(theirFirstName = "Phil", theirSurname = "Smith", theirRelationshipToYou = "Husband")

    "generate empty Breaks xml with data and dp even when no breaks" in new WithApplication {
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails)
      val xml = DWPCoCircs.xml(claim)
      (xml \\ "CareBreak").size shouldEqual(1)
      (xml \\ "CareBreak" \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual hospitalQuestion
      (xml \\ "CareBreak" \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "None"
      (xml \\ "CareBreak" \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual otherQuestion
      (xml \\ "CareBreak" \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"
    }

    "generate Breaks xml for single hospital break" in new WithApplication {
      val break = CircsBreak(iterationID = "1", typeOfCare = CircsBreaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)))
      val breaksInCare = CircsBreaksInCare().update(break)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val breakTypes = (circsXml \\ "CareBreak") (0)
      val firstBreak = (circsXml \\ "CareBreak") (1)
      val trailer = (circsXml \\ "CareBreak") (2)

      (breakTypes \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual hospitalQuestion
      (breakTypes \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "Hospital"
      (breakTypes \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual otherQuestion
      (breakTypes \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"

      (firstBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual "Type of Break"
      (firstBreak \\ "BreaksType" \\ "Answer").text shouldEqual "YouHospital"
      (firstBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual "Who was in hospital?"
      (firstBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual "@yourname"
      (firstBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual "When were you admitted?"
      (firstBreak \\ "StartDate" \\ "Answer").text shouldEqual "01-02-2003"

      trailer.toString().split('\n').map(_.trim.filter(_ >= ' ')).mkString shouldEqual breakTrailerXml
    }

    "generate Breaks xml for two breaks hospital and carehome/respite break" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)))
      val break2 = CircsBreak(iterationID = "2", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(2, 3, 2004)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), yourMedicalProfessional = Some(Mappings.no))
      val breaksInCare = CircsBreaksInCare().update(break1).update(break2)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (4)
      val breakType = (circsXml \\ "CareBreak")(0)
      val hospBreak = (circsXml \\ "CareBreak")(1)
      val respiteBreak = (circsXml \\ "CareBreak")(2)
      val trailer = (circsXml \\ "CareBreak")(3)

      (breakType \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual hospitalQuestion
      (breakType \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "Hospital, Respite or care home"
      (breakType \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual otherQuestion
      (breakType \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"

      (hospBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual "Type of Break"
      (hospBreak \\ "BreaksType" \\ "Answer").text shouldEqual "YouHospital"
      (hospBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual "Who was in hospital?"
      (hospBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual "@yourname"
      (hospBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual "When were you admitted?"
      (hospBreak \\ "StartDate" \\ "Answer").text shouldEqual "01-02-2003"

      (respiteBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual "Type of Break"
      (respiteBreak \\ "BreaksType" \\ "Answer").text shouldEqual "YouRespite"
      (respiteBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual "Who was in respite or a care home?"
      (respiteBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual "@yourname"
      (respiteBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual "When were you admitted?"
      (respiteBreak \\ "StartDate" \\ "Answer").text shouldEqual "02-03-2004"

      trailer.toString().split('\n').map(_.trim.filter(_ >= ' ')).mkString shouldEqual breakTrailerXml
    }
  }
  section("unit")
}

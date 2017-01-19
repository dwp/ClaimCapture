package xml.claim

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.CachedClaim
import models.yesNo.YesNoWithDate
import org.specs2.mutable._
import utils.WithApplication

class BreaksSpec extends Specification {
  section("unit")
  "Claim Breaks" should {
    lazy val claimDate = ClaimDate(DayMonthYear(11, 4, 2016))
    val breakTrailerXml = "<CareBreak><BreaksSinceClaim><QuestionLabel>Since 11 April 2016 have there been any other times you or @dpname have been in hospital, respite or care home for at least a week?</QuestionLabel><Answer>None</Answer></BreaksSinceClaim><BreaksOtherSinceClaim><QuestionLabel>Have there been any other times you've not provided care for @dpname for 35 hours a week?</QuestionLabel><Answer>No</Answer></BreaksOtherSinceClaim></CareBreak>"
    val hospitalQuestion = "Since 11 April 2016 have there been any times you or @dpname have been in hospital, respite or care home for at least a week?"
    val hospitalOtherQuestion = "Since 11 April 2016 have there been any other times you or @dpname have been in hospital, respite or care home for at least a week?"
    val otherQuestion = "Have there been any other times you've not provided care for @dpname for 35 hours a week?"

    "generate empty Breaks xml with data and dp even when no breaks" in new WithApplication {
      val claim = Claim(CachedClaim.key).update(claimDate)
      (Caree.xml(claim) \\ "Caree" \\ "CareBreak").size shouldEqual(1)
      val trailer = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(0)
      (trailer \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual hospitalQuestion
      (trailer \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "None"
      (trailer \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual otherQuestion
      (trailer \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"
    }

    "generate Breaks xml for single hospital break" in new WithApplication {
      val break = Break(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)))
      val breaksInCare = BreaksInCare().update(break)
      val claim = Claim(CachedClaim.key).update(claimDate).update(breaksInCare)

      (Caree.xml(claim) \\ "Caree" \\ "CareBreak").size shouldEqual(3)
      val breakTypes = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(0)
      val firstBreak = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(1)
      val trailer = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(2)

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
      val break1 = Break(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)))
      val break2 = Break(iterationID = "2", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(2, 3, 2004)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), yourMedicalProfessional = Some(Mappings.no))
      val breaksInCare = BreaksInCare().update(break1).update(break2)
      val claim = Claim(CachedClaim.key).update(claimDate).update(breaksInCare)
      val xml = Caree.xml(claim)

      (Caree.xml(claim) \\ "Caree" \\ "CareBreak").size shouldEqual(4)
      val breakTypes = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(0)
      val hospBreak = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(1)
      val respiteBreak = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(2)
      val trailer = (Caree.xml(claim) \\ "Caree" \\ "CareBreak")(3)

      (breakTypes \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual hospitalQuestion
      (breakTypes \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "Hospital, Respite or care home"
      (breakTypes \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual otherQuestion
      (breakTypes \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"

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

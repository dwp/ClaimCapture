package xml.circumstances

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.{CachedChangeOfCircs}
import models.yesNo.{YesNoDontKnowWithDates, YesNoWithDate}
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

    "not generate any Breaks xml when no breaks" in new WithApplication {
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails)
      val xml = DWPCoCircs.xml(claim)
      (xml \\ "CareBreak").size shouldEqual (0)
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

    "generate xml for Other page when Not restarted caring and Yes - Caring Will Restart" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(2, 3, 2004)), None)))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val otherBreak = (circsXml \\ "CareBreak")(1)

      (otherBreak \\ "BreakStarted" \\ "QuestionLabel").text shouldEqual ("Have you started providing care again?")
      (otherBreak \\ "BreakStarted" \\ "Answer").text shouldEqual ("No")
      (otherBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (otherBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("Yes")
      (otherBreak \\ "ExpectToCareAgainDate" \\ "QuestionLabel").text shouldEqual ("What date do you expect to start providing care again?")
      (otherBreak \\ "ExpectToCareAgainDate" \\ "Answer").text shouldEqual ("02-03-2004")
      (otherBreak \\ "StopCaringDecisionDate").size shouldEqual (0)
    }

    "generate xml for Other page when Not restarted caring and No - So Caring Ended" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.no), None, Some(DayMonthYear(3, 3, 2004)))))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val otherBreak = (circsXml \\ "CareBreak")(1)

      (otherBreak \\ "BreakStarted" \\ "QuestionLabel").text shouldEqual ("Have you started providing care again?")
      (otherBreak \\ "BreakStarted" \\ "Answer").text shouldEqual ("No")
      (otherBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (otherBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("No")
      (otherBreak \\ "StopCaringDecisionDate" \\ "QuestionLabel").text shouldEqual ("What date did you decide that you permanently stopped providing care?")
      (otherBreak \\ "StopCaringDecisionDate" \\ "Answer").text shouldEqual ("03-03-2004")
      (otherBreak \\ "ExpectToCareAgainDate").size shouldEqual (0)
    }

    "generate xml for Other page when Not restarted caring and Dont Know If Will" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val otherBreak = (circsXml \\ "CareBreak")(1)

      (otherBreak \\ "BreakStarted" \\ "QuestionLabel").text shouldEqual ("Have you started providing care again?")
      (otherBreak \\ "BreakStarted" \\ "Answer").text shouldEqual ("No")
      (otherBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (otherBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("Don't know")
      (otherBreak \\ "ExpectToCareAgainDate").size shouldEqual (0)
      (otherBreak \\ "StopCaringDecisionDate").size shouldEqual (0)
    }

    "generate xml for Hospital page when You Hospital Not restarted caring and Yes - Caring Will Restart" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(2, 3, 2004)), None)))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalBreak = (circsXml \\ "CareBreak")(1)
      (hospitalBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual ("Type of Break")
      (hospitalBreak \\ "BreaksType" \\ "Answer").text shouldEqual ("YouHospital")
      (hospitalBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual ("Who was in hospital?")
      (hospitalBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual ("@yourname")
      (hospitalBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual ("When were you admitted?")
      (hospitalBreak \\ "StartDate" \\ "Answer").text shouldEqual ("01-01-2003")
      (hospitalBreak \\ "BreakEnded" \\ "QuestionLabel").text shouldEqual ("Has the hospital stay ended?")
      (hospitalBreak \\ "BreakEnded" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("Yes")
      (hospitalBreak \\ "ExpectToCareAgainDate" \\ "QuestionLabel").text shouldEqual ("What date do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgainDate" \\ "Answer").text shouldEqual ("02-03-2004")
    }

    "generate xml for Hospital page when Dp Hospital Not restarted caring and No - Caring Will Not Restart" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.no), None, Some(DayMonthYear(2, 3, 2004)))))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalBreak = (circsXml \\ "CareBreak")(1)
      (hospitalBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual ("Type of Break")
      (hospitalBreak \\ "BreaksType" \\ "Answer").text shouldEqual ("DPHospital")
      (hospitalBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual ("Who was in hospital?")
      (hospitalBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual ("@dpname")
      (hospitalBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual ("When was @dpname admitted?")
      (hospitalBreak \\ "StartDate" \\ "Answer").text shouldEqual ("01-01-2003")
      (hospitalBreak \\ "BreakEnded" \\ "QuestionLabel").text shouldEqual ("Has the hospital stay ended?")
      (hospitalBreak \\ "BreakEnded" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "BreaksInCareRespiteStillCaring" \\ "QuestionLabel").text shouldEqual ("During this time in hospital, were you still providing care for @dpname for 35 hours a week?")
      (hospitalBreak \\ "BreaksInCareRespiteStillCaring" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "StopCaringDecisionDate" \\ "QuestionLabel").text shouldEqual ("What date did you decide that you permanently stopped providing care?")
      (hospitalBreak \\ "StopCaringDecisionDate" \\ "Answer").text shouldEqual ("02-03-2004")
    }

    "generate xml for Respite page when You in Respite Not restarted caring and Yes - Caring Will Restart" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(2, 3, 2004)), None)),
        yourMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalBreak = (circsXml \\ "CareBreak")(1)
      (hospitalBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual ("Type of Break")
      (hospitalBreak \\ "BreaksType" \\ "Answer").text shouldEqual ("YouRespite")
      (hospitalBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual ("Who was in respite or a care home?")
      (hospitalBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual ("@yourname")
      (hospitalBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual ("When were you admitted?")
      (hospitalBreak \\ "StartDate" \\ "Answer").text shouldEqual ("01-01-2003")
      (hospitalBreak \\ "BreakEnded" \\ "QuestionLabel").text shouldEqual ("Has the respite or care home stay ended?")
      (hospitalBreak \\ "BreakEnded" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("Yes")
      (hospitalBreak \\ "ExpectToCareAgainDate" \\ "QuestionLabel").text shouldEqual ("What date do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgainDate" \\ "Answer").text shouldEqual ("02-03-2004")
    }

    "generate xml for Respite page when Dp in Respite Not restarted caring and No - Caring Will Not Restart" in new WithApplication {
      val break1 = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.no), None, Some(DayMonthYear(2, 3, 2004)))),
        dpMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(break1)
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalBreak = (circsXml \\ "CareBreak")(1)
      (hospitalBreak \\ "BreaksType" \\ "QuestionLabel").text shouldEqual ("Type of Break")
      (hospitalBreak \\ "BreaksType" \\ "Answer").text shouldEqual ("DPRespite")
      (hospitalBreak \\ "WhoWasAway" \\ "QuestionLabel").text shouldEqual ("Who was in respite or a care home?")
      (hospitalBreak \\ "WhoWasAway" \\ "Answer").text shouldEqual ("@dpname")
      (hospitalBreak \\ "StartDate" \\ "QuestionLabel").text shouldEqual ("When was @dpname admitted?")
      (hospitalBreak \\ "StartDate" \\ "Answer").text shouldEqual ("01-01-2003")
      (hospitalBreak \\ "BreakEnded" \\ "QuestionLabel").text shouldEqual ("Has the respite or care home stay ended?")
      (hospitalBreak \\ "BreakEnded" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "BreaksInCareRespiteStillCaring" \\ "QuestionLabel").text shouldEqual ("During this time in hospital, were you still providing care for @dpname for 35 hours a week?")
      (hospitalBreak \\ "BreaksInCareRespiteStillCaring" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "QuestionLabel").text shouldEqual ("Do you expect to start providing care again?")
      (hospitalBreak \\ "ExpectToCareAgain" \\ "Answer").text shouldEqual ("No")
      (hospitalBreak \\ "StopCaringDecisionDate" \\ "QuestionLabel").text shouldEqual ("What date did you decide that you permanently stopped providing care?")
      (hospitalBreak \\ "StopCaringDecisionDate" \\ "Answer").text shouldEqual ("02-03-2004")
    }

    "generate Breaks xml including summary page MoreAbout" in new WithApplication {
      val hospitalbreak = CircsBreak(iterationID = "1", typeOfCare = CircsBreaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.no, None)))
      val breaksInCare = CircsBreaksInCare().update(hospitalbreak)
      val breakType = CircsBreaksInCareType(moreAboutChanges = Some("Some text in moreinfo"))
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare).update(yourDetails).update(breakType)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val trailer = (circsXml \\ "CareBreak")(2)
      (trailer \\ "BreaksMoreAbout" \\ "QuestionLabel").text shouldEqual "Tell us more about your changes"
      (trailer \\ "BreaksMoreAbout" \\ "Answer").text shouldEqual "Some text in moreinfo"
    }

    "generate Breaks xml for You break with no ExpectToCareAgain block if restarted Yes" in new WithApplication {
      val hospitalbreak = CircsBreak(iterationID = "1", typeOfCare = CircsBreaks.hospital, whoWasAway = BreaksInCareGatherOptions.You, whenWereYouAdmitted = Some(DayMonthYear(1, 2, 2003)),
        yourStayEnded = Some(YesNoWithDate(Mappings.yes, Some(DayMonthYear(1, 2, 2003)))))
      val breaksInCare = CircsBreaksInCare().update(hospitalbreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare).update(yourDetails)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalXml = (circsXml \\ "CareBreak")(1)
      (hospitalXml \\ "BreakEnded" \\ "Answer").text shouldEqual "Yes"
      (hospitalXml \\ "ExpectToCareAgain").size shouldEqual 0
    }

    "generate Breaks xml for Dp break with no ExpectToCareAgain block if restarted Yes" in new WithApplication {
      val hospitalbreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.yes, Some(DayMonthYear(1, 2, 2003)))), breaksInCareStillCaring = Some(Mappings.no))
      val breaksInCare = CircsBreaksInCare().update(hospitalbreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare).update(yourDetails)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "CareBreak").size shouldEqual (3)
      val hospitalXml = (circsXml \\ "CareBreak")(1)
      (hospitalXml \\ "BreakEnded" \\ "Answer").text shouldEqual "Yes"
      (hospitalXml \\ "ExpectToCareAgain").size shouldEqual 0
    }
  }
  section("unit")
}

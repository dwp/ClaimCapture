package xml.circumstances

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.CachedChangeOfCircs
import models.yesNo.{YesNoDontKnowWithDates, YesNoWithDate}
import org.specs2.mutable._
import utils.WithApplication

class EvidenceListSpec extends Specification {
  section("unit")
  "Circs Breaks" should {
    "generate evidence message for You Hospital break that need to inform about change when restart caring = Dont Know" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)),
        yourMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (5)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
      (evidenceWhatHappensNext \\ "Title").text mustEqual ("What happens next")
      val evidenceBreaksInfo = (circsXml \\ "Evidence")(1)
      (evidenceBreaksInfo \\ "Title").text mustEqual ("You must tell us as soon as you start providing care again, or if a decision is made that you have permanently stopped providing care, as your entitlement may be affected.")
    }

    "NOT generate evidence message for You Hospital break that need to inform about change when restart caring = Yes" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(1, 1, 2003)), None)),
        yourMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "Evidence").size shouldEqual (4)
    }

    "generate evidence message for DP Hospital break that need to inform about change when restart caring = Dont Know" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain2 = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)),
        dpMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (5)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
      (evidenceWhatHappensNext \\ "Title").text mustEqual ("What happens next")
      val evidenceBreaksInfo = (circsXml \\ "Evidence")(1)
      (evidenceBreaksInfo \\ "Title").text mustEqual ("You must tell us as soon as you start providing care again, or if a decision is made that you have permanently stopped providing care, as your entitlement may be affected.")
    }

    "NOT generate evidence message for DP Hospital break that need to inform about change when restart caring = Yes" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.hospital, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain2 = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(1, 1, 2003)), None)),
        dpMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (4)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
    }

    "generate evidence message for You respite break that need to inform about change when restart caring = Dont Know" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)),
        yourMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (5)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
      (evidenceWhatHappensNext \\ "Title").text mustEqual ("What happens next")
      val evidenceBreaksInfo = (circsXml \\ "Evidence")(1)
      (evidenceBreaksInfo \\ "Title").text mustEqual ("You must tell us as soon as you start providing care again, or if a decision is made that you have permanently stopped providing care, as your entitlement may be affected.")
    }

    "NOT generate evidence message for You respite break that need to inform about change when restart caring = Yes" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.You,
        whenWereYouAdmitted = Some((DayMonthYear(1, 1, 2003))), yourStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(1, 1, 2003)), None)),
        yourMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)
      (circsXml \\ "Evidence").size shouldEqual (4)
    }

    "generate evidence message for DP respite break that need to inform about change when restart caring = Dont Know" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain2 = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)),
        dpMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (5)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
      (evidenceWhatHappensNext \\ "Title").text mustEqual ("What happens next")
      val evidenceBreaksInfo = (circsXml \\ "Evidence")(1)
      (evidenceBreaksInfo \\ "Title").text mustEqual ("You must tell us as soon as you start providing care again, or if a decision is made that you have permanently stopped providing care, as your entitlement may be affected.")
    }

    "NOT generate evidence message for DP respite break that need to inform about change when restart caring = Yes" in new WithApplication {
      val respiteBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.carehome, whoWasAway = BreaksInCareGatherOptions.DP,
        whenWasDpAdmitted = Some((DayMonthYear(1, 1, 2003))), dpStayEnded = Some(YesNoWithDate(Mappings.no, None)), breaksInCareStillCaring = Some(Mappings.no),
        expectToCareAgain2 = Some(YesNoDontKnowWithDates(Some(Mappings.yes), Some(DayMonthYear(1, 1, 2003)), None)),
        dpMedicalProfessional = Some(Mappings.yes))
      val breaksInCare = CircsBreaksInCare().update(respiteBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      val circsXml = DWPCoCircs.xml(claim)

      (circsXml \\ "Evidence").size shouldEqual (4)
      val evidenceWhatHappensNext = (circsXml \\ "Evidence")(0)
    }
  }
  section("unit")
}

package xml.claim

import models.DayMonthYear
import models.domain.{Claim, _}
import models.view.CachedClaim
import org.specs2.mutable._
import utils.WithApplication

class NationalityAndResidencySpec extends Specification {
  section("unit")
  "Nationality and residency section xml generation" should {
    "Generate correct xml items from simple case" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))
      val residency = new NationalityAndResidency("British", None, "Yes", None, None, None, "No", None)
      val xml = Residency.xml(claim + claimDate + residency)

      (xml \\ "Residency" \\ "Nationality" \\ "QuestionLabel").text must contain("your nationality")
      (xml \\ "Residency" \\ "Nationality" \\ "Answer").text mustEqual ("British")

      (xml \\ "Residency" \\ "AlwaysLivedInUK" \\ "QuestionLabel").text must contain("always lived in England")
      (xml \\ "Residency" \\ "AlwaysLivedInUK" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "Residency" \\ "TimeOutsideGBLast3Years" \\ "QuestionLabel").text must contain("been away from England")
      (xml \\ "Residency" \\ "TimeOutsideGBLast3Years" \\ "Answer").text mustEqual ("No")
    }

    "Generate correct xml items from complex case" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))
      val residency = new NationalityAndResidency("Another nationality", Some("French"), "No", Some("Yes"), Some("less"), Some(DayMonthYear(10, 3, 2016)), "Yes", Some("Worked in spain"))
      val xml = Residency.xml(claim + claimDate + residency)

      (xml \\ "Residency" \\ "ActualNationality" \\ "QuestionLabel").text must contain("Your nationality")
      (xml \\ "Residency" \\ "ActualNationality" \\ "Answer").text mustEqual ("French")

      (xml \\ "Residency" \\ "AlwaysLivedInUK" \\ "QuestionLabel").text must contain("always lived in England")
      (xml \\ "Residency" \\ "AlwaysLivedInUK" \\ "Answer").text mustEqual ("No")

      (xml \\ "Residency" \\ "LiveInUKNow" \\ "QuestionLabel").text must contain("live in England")
      (xml \\ "Residency" \\ "LiveInUKNow" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "Residency" \\ "ArrivedInUK" \\ "QuestionLabel").text must contain("arrive in England")
      (xml \\ "Residency" \\ "ArrivedInUK" \\ "Answer").text mustEqual ("less")

      (xml \\ "Residency" \\ "ArrivedInUKDate" \\ "QuestionLabel").text must contain("arrived")
      (xml \\ "Residency" \\ "ArrivedInUKDate" \\ "Answer").text mustEqual ("10-03-2016")

      (xml \\ "Residency" \\ "TimeOutsideGBLast3Years" \\ "QuestionLabel").text must contain("been away from England")
      (xml \\ "Residency" \\ "TimeOutsideGBLast3Years" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "Residency" \\ "TripDetails" \\ "QuestionLabel").text must contain("Tell us about")
      (xml \\ "Residency" \\ "TripDetails" \\ "Answer").text mustEqual ("Worked in spain")
    }

    "Reverse engineer claim using fromxml method" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))
      val nr = new NationalityAndResidency("Another nationality", Some("French"), "No", Some("Yes"), Some("less"), Some(DayMonthYear(10, 3, 2016)), "Yes", Some("Worked in spain"))
      val xml = Residency.xml(claim + claimDate + nr)
      val claimFromXml = Residency.fromXml(xml, claim)
      val nrFromXml: NationalityAndResidency=claimFromXml.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())

      nrFromXml.nationality mustEqual("Another nationality")
      nrFromXml.actualnationality.get mustEqual("French")
      nrFromXml.alwaysLivedInUK mustEqual("No")
      nrFromXml.liveInUKNow.get mustEqual("Yes")
      nrFromXml.arrivedInUK.get mustEqual("less")
      nrFromXml.arrivedInUKDate.get.`dd-MM-yyyy` mustEqual("10-03-2016")
    }
  }
  section("unit")
}

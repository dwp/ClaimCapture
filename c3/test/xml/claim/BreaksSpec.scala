package xml.claim

import models.DayMonthYear
import models.domain.{TheirPersonalDetails, Claim, ClaimDate}
import models.view.{CachedClaim}
import org.specs2.mutable._
import utils.WithApplication

class BreaksSpec extends Specification {
  section("unit")
  "Circs Breaks" should {
    "generate empty Breaks xml with data and dp even when no breaks" in new WithApplication {
      val claimDate=ClaimDate(DayMonthYear(10,3,2016))
      // dont really need these details as we dont see the dp name in xml anyway we see it as placeholder @dpname!
      val theirDetails = TheirPersonalDetails(
        firstName = "Arthur",
        surname = "Peterson"
      )

      val claim = Claim(CachedClaim.key).update(claimDate).update(theirDetails)
      val xml = Caree.xml(claim)

      val expectedQuestion="Since 10 March 2016 have there been any times you or @dpname have been in hospital, respite or care home for at least a week?"
      (xml \\ "Caree" \\ "CareBreak" \\ "BreaksSinceClaim" \\ "QuestionLabel").text shouldEqual expectedQuestion
      (xml \\ "Caree" \\ "CareBreak" \\ "BreaksSinceClaim" \\ "Answer").text shouldEqual "None"

      val expectedOtherQuestion="Have there been any other times you've not provided care for @dpname for 35 hours a week?"
      (xml \\ "Caree" \\ "CareBreak" \\ "BreaksOtherSinceClaim" \\ "QuestionLabel").text shouldEqual expectedOtherQuestion
      (xml \\ "Caree" \\ "CareBreak" \\ "BreaksOtherSinceClaim" \\ "Answer").text shouldEqual "No"
    }
  }
  section("unit")
}

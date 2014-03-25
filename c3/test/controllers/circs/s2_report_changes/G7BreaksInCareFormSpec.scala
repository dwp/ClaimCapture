package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import app.CircsBreaksWhereabouts
import controllers.Mappings._

class G7BreaksInCareFormSpec extends Specification with Tags {

  "Report a change in your circumstances - Breaks from caring Form" should {

    val recentBreakFromCaringDateDay = 10
    val recentBreakFromCaringDateMonth = 11
    val recentBreakFromCaringDateYear = 2012
    val startTime = "10 am"
    val wherePersonYouCareFor = CircsBreaksWhereabouts.Holiday
    val whereWereYou = CircsBreaksWhereabouts.SomewhereElse
    val hasBreaksFromCaringEnded = yes

  }
}

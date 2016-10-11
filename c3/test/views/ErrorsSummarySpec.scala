package views

import org.specs2.mutable._
import utils.WithApplication
import utils.helpers.GAHelper._

class ErrorsSummarySpec extends Specification {
  section("unit")

  // Previous regex was ... val result = ".*\\b(before \\d+|after \\d+|since \\d+|ers \\d+|cyn \\d+)\\b".r.findFirstMatchIn(key)
  "Errors summary" should {
    "Replace date on dd/mm/yyyy patterns with @date" in new WithApplication {
      val error1 = "Do you spend 35 hours or more each week providing care for Jane Bloggs? - You must complete this section"
      replaceDate(error1) mustEqual ("Do you spend 35 hours or more each week providing care for Jane Bloggs? - You must complete this section")

      val error2 = "Have you been self-employed since 03/10/2016? - You must complete this section"
      replaceDate(error2) mustEqual ("Have you been self-employed since @date? - You must complete this section")

      val error3 = "Have you been an employee since 3/4/2016? - You must complete this section"
      replaceDate(error3) mustEqual ("Have you been an employee since @date? - You must complete this section")

      val error4 = "What other income have you had since 10/10/2016? - You must complete this section"
      replaceDate(error4) mustEqual ("What other income have you had since @date? - You must complete this section")

      val error5 = "Did you and your partner/spouse start living together, after your claim date: 1/1/2000 ?"
      replaceDate(error5) mustEqual ("Did you and your partner/spouse start living together, after your claim date: @date ?")
    }

    "Replace date on dd Month yyyy patterns with @date" in new WithApplication {
      val error5 = "Since 10 July 2016 have there been any other times you or Jane Bloggs have been in hospital, respite or care home for at least a week? - You must complete this section"
      replaceDate(error5) mustEqual ("Since @date have there been any other times you or Jane Bloggs have been in hospital, respite or care home for at least a week? - You must complete this section")

      val error6 = "Since 1 Jan 1990 have there been any other times you or Jane Bloggs have been in hospital, respite or care home for at least a week? - You must complete this section"
      replaceDate(error6) mustEqual ("Since @date have there been any other times you or Jane Bloggs have been in hospital, respite or care home for at least a week? - You must complete this section")

      val error7 = "Ers 10 Gorffennaf 2016 a fu unrhyw adegau rydych chi neu Jane Bloggs wedi bod yn yr ysbyty, mewn gofal seibiant neu gartref gofal am o leiaf wythnos?"
      replaceDate(error7) mustEqual ("Ers @date a fu unrhyw adegau rydych chi neu Jane Bloggs wedi bod yn yr ysbyty, mewn gofal seibiant neu gartref gofal am o leiaf wythnos?")
    }

    // Replace any markup with blanks
    "Remove html markup from string" in new WithApplication {
      val error = "You must select:<ol class='validation-message list-bullet'><li>Hospital</li>"
      removeHtml(error) mustEqual ("You must select:Hospital")
    }
  }
  section("unit")
}
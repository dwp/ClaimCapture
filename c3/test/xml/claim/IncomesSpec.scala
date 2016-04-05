package xml.claim

import java.util.Date

import models.{DayMonthYear}
import models.domain.{Claim, _}
import models.view.{CachedClaim}
import org.joda.time.DateTime
import org.specs2.mutable._
import utils.WithApplication

class IncomesSpec extends Specification {
  val otherInfo = "Some other info"

  section("unit")
  "Incomes section xml generation" should {
    /*
    "Generate correct header xml items for Emp, SelfEmp, SickPay, PatMat, Fost, DP, Other" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      // SE, EMP, SICK, PATMATADOP, FOST, DP, OTHER
      val incomeHeader = new YourIncomes("Yes", "Yes", Some("true"), Some("true"), Some("true"), Some("true"), Some("true"))
      claim = claim + incomeHeader
      val xml = Incomes.xml(claim)
      println("xml:" + xml)
      (xml \\ "Incomes" \\ "Employed" \\ "QuestionLabel").text must contain("been employed")
      (xml \\ "Incomes" \\ "Employed" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "SelfEmployed" \\ "QuestionLabel").text must contain("been self-employed")
      (xml \\ "Incomes" \\ "SelfEmployed" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "SickPayment" \\ "QuestionLabel").text must contain("sick pay")
      (xml \\ "Incomes" \\ "SickPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("paternity")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("maternity")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("adoption")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "FosteringPayment" \\ "QuestionLabel").text must contain("Fostering")
      (xml \\ "Incomes" \\ "FosteringPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "DirectPayment" \\ "QuestionLabel").text must contain("Direct payment")
      (xml \\ "Incomes" \\ "DirectPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "QuestionLabel").text must contain("other income")
      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "Answer").text shouldEqual "Yes"
    }


    "Generate correct xml for Sick Pay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("true"), Some("false"), Some("false"), Some("false"), Some("false"))

      // Still, WhenLast, WhoPaid, Amount, HowOften, HowOftenOther
      val sickPay = new StatutorySickPay("No",Some(DayMonthYear(31,1,2016)), "Asda", "10.00", "Weekly", None)
      claim = claim + incomeHeader + sickPay
      val xml = Incomes.xml(claim)
      (xml \\ "Incomes" \\ "SickPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "SickPay" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid statutory sick pay")
      (xml \\ "Incomes" \\ "SickPay" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "SickPay" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "SickPay" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "31-01-2016"
      (xml \\ "Incomes" \\ "SickPay" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "SickPay" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "10.00"
      (xml \\ "Incomes" \\ "SickPay" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you statutory sick pay")
      (xml \\ "Incomes" \\ "SickPay" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "Asda"
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPayOther" ).length shouldEqual 0
    }

    "Generate correct xml for PatMatAdopt Pay Section" in new WithApplication {
      pending("To do")
    }

    "Generate correct xml for Foster Pay Section" in new WithApplication {
      pending("To do")
    }
*/
    /*
                             override val stillBeingPaidThisPay: String = "",
                         override val whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                         override val whoPaidYouThisPay: String = "",
                         override val amountOfThisPay: String = "",
                         override val howOftenPaidThisPay: String = "",
                         override val howOftenPaidThisPayOther: Option[String] = None
                        ) extends QuestionGroup(DirectPayment) with OtherIncomes
     */
    "Generate correct xml for DirectPay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(DateTime.now().plusMonths(3).plusDays(2)))
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("false"), Some("true"), Some("false"))
      val directPayment = new DirectPayment("No",Some(DayMonthYear(29,2,2016)), "Disabled person", "25.00", "Weekly", None)
      claim = claim + claimDate + incomeHeader + directPayment
      val xml = Incomes.xml(claim)
      println(xml)
      (xml \\ "Incomes" \\ "DirectPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "DirectPay" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid statutory sick pay")
      (xml \\ "Incomes" \\ "DirectPay" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "29-02-2016"
      (xml \\ "Incomes" \\ "DirectPay" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "DirectPay" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "25.00"
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you statutory sick pay")
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "Disabled person"
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPayOther" ).length shouldEqual 0
    }

    "Generate correct xml for OtherPay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(DateTime.now().plusMonths(3).plusDays(2)))
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("false"), Some("false"), Some("true"))

      // otherPaymentsInfo
      val otherPayment = new OtherPayments("Was paid some money by carees brother")
      claim = claim + claimDate + incomeHeader + otherPayment
      val xml = Incomes.xml(claim)
      println(xml)
      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "OtherPaymentsInfo" \\ "Question").text should contain("What other income have you had since {0}?")
      (xml \\ "Incomes" \\ "OtherPaymentsInfo" \\ "Answer").text shouldEqual "Was paid some money by carees brother"
    }

  }
  section("unit")
}

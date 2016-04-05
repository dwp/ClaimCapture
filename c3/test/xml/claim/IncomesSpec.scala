package xml.claim

import models.{DayMonthYear}
import models.domain.{Claim, _}
import models.view.{CachedClaim}
import org.specs2.mutable._
import utils.WithApplication

class IncomesSpec extends Specification {
  val otherInfo = "Some other info"

  section("unit")
  "Incomes section xml generation" should {
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
      val incomeHeader = new YourIncomes("Yes", "Yes", Some("true"), Some("true"), Some("true"), Some("true"), Some("true"))

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

    "Generate correct xml for DirectPay Section" in new WithApplication {
      pending("To do")
    }

    "Generate correct xml for OtherPay Section" in new WithApplication {
      pending("To do")
    }

  }
  section("unit")
}

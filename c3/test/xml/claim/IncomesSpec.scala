package xml.claim

import app.PaymentTypes
import models.{DayMonthYear}
import models.domain.{Claim, _}
import models.view.{CachedClaim}
import org.specs2.mutable._
import utils.WithApplication

class IncomesSpec extends Specification {
  section("unit")
  "Incomes section xml generation" should {
    "Generate correct header xml items for Emp, SelfEmp, SickPay, PatMat, Fost, DP, Other" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))

      // SE, EMP, SICK, PATMATADOP, FOST, DP, OTHER
      val incomeHeader = new YourIncomes("Yes", "Yes", Some("true"), Some("true"), Some("true"), Some("true"), Some("true"), Some("true"))
      val xml = Incomes.xml(claim + claimDate + incomeHeader)
      (xml \\ "Incomes" \\ "Employed" \\ "QuestionLabel").text must contain("been employed")
      (xml \\ "Incomes" \\ "Employed" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "SelfEmployed" \\ "QuestionLabel").text must contain("been self-employed")
      (xml \\ "Incomes" \\ "SelfEmployed" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "OtherPaymentQuestion" \\ "QuestionLabel").text shouldEqual "What other income have you had since 20/03/2016?"
      (xml \\ "Incomes" \\ "OtherPaymentQuestion" \\ "Answer").text shouldEqual "Some"

      (xml \\ "Incomes" \\ "SickPayment" \\ "QuestionLabel").text must contain("Sick Pay")
      (xml \\ "Incomes" \\ "SickPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("Paternity")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("Maternity")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "QuestionLabel").text must contain("Adoption")
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "FosteringPayment" \\ "QuestionLabel").text must contain("Fostering")
      (xml \\ "Incomes" \\ "FosteringPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "DirectPayment" \\ "QuestionLabel").text must contain("Direct payment")
      (xml \\ "Incomes" \\ "DirectPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "RentalIncome" \\ "QuestionLabel").text must contain("Rental income")
      (xml \\ "Incomes" \\ "RentalIncome" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "QuestionLabel").text must contain("other income")
      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "Answer").text shouldEqual "Yes"
    }

    "Generate correct header xml items for None Selected" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))

      // SE, EMP, SICK, PATMATADOP, FOST, DP, OTHER, NONE
      val incomeHeader = new YourIncomes("No", "No", None, None, None, None, None, None, Some("true"))
      val xml = Incomes.xml(claim + claimDate + incomeHeader)

      (xml \\ "Incomes" \\ "Employed" \\ "QuestionLabel").text must contain("been employed")
      (xml \\ "Incomes" \\ "Employed" \\ "Answer").text shouldEqual "No"

      (xml \\ "Incomes" \\ "SelfEmployed" \\ "QuestionLabel").text must contain("been self-employed")
      (xml \\ "Incomes" \\ "SelfEmployed" \\ "Answer").text shouldEqual "No"

      (xml \\ "Incomes" \\ "OtherPaymentQuestion" \\ "QuestionLabel").text shouldEqual "What other income have you had since 20/03/2016?"
      (xml \\ "Incomes" \\ "OtherPaymentQuestion" \\ "Answer").text shouldEqual "None"

      (xml \\ "Incomes" \\ "NoOtherPayment" \\ "QuestionLabel").text shouldEqual "None"
      (xml \\ "Incomes" \\ "NoOtherPayment" \\ "Answer").text shouldEqual "Yes"
    }

    "Generate correct xml for Sick Pay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("true"), Some("false"), Some("false"), Some("false"), Some("false"), Some("false"))

      val sickPay = new StatutorySickPay("No", Some(DayMonthYear(31, 1, 2016)), "Asda", "10.00", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + sickPay)
      (xml \\ "Incomes" \\ "SickPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "SickPay" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid Statutory Sick Pay")
      (xml \\ "Incomes" \\ "SickPay" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "SickPay" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "SickPay" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "31-01-2016"
      (xml \\ "Incomes" \\ "SickPay" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "SickPay" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "10.00"
      (xml \\ "Incomes" \\ "SickPay" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you Statutory Sick Pay")
      (xml \\ "Incomes" \\ "SickPay" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "Asda"
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "SickPay" \\ "HowOftenPaidThisPayOther").length shouldEqual 0
    }

    "Generate correct xml for PatMatAdopt Pay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("true"), Some("false"), Some("false"), Some("false"), Some("false"))
      val patMatAdoptPay = new StatutoryMaternityPaternityAdoptionPay(PaymentTypes.MaternityPaternity, "No", Some(DayMonthYear(31, 3, 2016)), "Tesco", "50.01", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + patMatAdoptPay)
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "Answer").text shouldEqual "Yes"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "PaymentTypesForThisPay" \\ "QuestionLabel").text should contain("Which are you paid")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "PaymentTypesForThisPay" \\ "Answer").text should contain("Maternity or Paternity Pay")

      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid this")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "31-03-2016"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "50.01"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you this")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "Tesco"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "HowOftenPaidThisPayOther").length shouldEqual 0
    }

    "Generate correct xml for PatMatAdopt Pay Section with Adoption Selected" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("true"), Some("false"), Some("false"), Some("false"), Some("false"))
      val patMatAdoptPay = new StatutoryMaternityPaternityAdoptionPay(PaymentTypes.Adoption, "No", Some(DayMonthYear(31, 3, 2016)), "Tesco", "50.01", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + patMatAdoptPay)
      (xml \\ "Incomes" \\ "PatMatAdopPayment" \\ "Answer").text shouldEqual "Yes"
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "PaymentTypesForThisPay" \\ "QuestionLabel").text should contain("Which are you paid")
      (xml \\ "Incomes" \\ "StatutoryMaternityPaternityAdopt" \\ "PaymentTypesForThisPay" \\ "Answer").text should contain("Adoption Pay")
    }

    "Generate correct xml for Foster Pay Section for Paid By Local Authority" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("true"), Some("false"), Some("false"), Some("false"))
      val foster = new FosteringAllowance(PaymentTypes.LocalAuthority, None, "No", Some(DayMonthYear(31, 3, 2016)), "LCC", "50.01", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + foster)
      (xml \\ "Incomes" \\ "FosteringPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "QuestionLabel").text should contain("What type of organisation pays you for Fostering")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "Answer").text should contain("Local Authority")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPayOther").length shouldEqual 0

      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid this")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "31-03-2016"
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "50.01"
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you this")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "LCC"
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "HowOftenPaidThisPayOther").length shouldEqual 0
    }

    "Generate correct xml for Foster Pay Section for Paid By Foster Agency" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("true"), Some("false"), Some("false"), Some("false"))
      val foster = new FosteringAllowance(PaymentTypes.FosteringAllowance, None, "No", Some(DayMonthYear(31, 3, 2016)), "LCC", "50.01", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + foster)
      (xml \\ "Incomes" \\ "FosteringPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "QuestionLabel").text should contain("What type of organisation pays you for Fostering")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "Answer").text should contain("Fostering Agency")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPayOther").length shouldEqual 0
    }

    "Generate correct xml for Foster Pay Section for Paid By Other" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("true"), Some("false"), Some("false"), Some("false"))
      val foster = new FosteringAllowance(PaymentTypes.Other, Some("Foster charity paid"), "No", Some(DayMonthYear(31, 3, 2016)), "LCC", "50.01", "Weekly", None)
      val xml = Incomes.xml(claim + incomeHeader + foster)
      (xml \\ "Incomes" \\ "FosteringPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "QuestionLabel").text should contain("What type of organisation pays you for Fostering")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPay" \\ "Answer").text should contain("Other")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPayOther" \\ "QuestionLabel").text should contain("Who paid you Fostering Allowance")
      (xml \\ "Incomes" \\ "FosteringAllowance" \\ "PaymentTypesForThisPayOther" \\ "Answer").text should contain("Foster charity paid")
    }

    "Generate correct xml for DirectPay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(10, 2, 2016))
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("false"), Some("true"), Some("false"), Some("false"))
      val directPayment = new DirectPayment("No", Some(DayMonthYear(29, 2, 2016)), "Disabled person", "25.00", "Weekly", None)
      val xml = Incomes.xml(claim + claimDate + incomeHeader + directPayment)
      (xml \\ "Incomes" \\ "DirectPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "DirectPay" \\ "StillBeingPaidThisPay" \\ "QuestionLabel").text should contain("still being paid this")
      (xml \\ "Incomes" \\ "DirectPay" \\ "StillBeingPaidThisPay" \\ "Answer").text shouldEqual "No"
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhenDidYouLastGetPaid" \\ "QuestionLabel").text should contain("last paid")
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhenDidYouLastGetPaid" \\ "Answer").text shouldEqual "29-02-2016"
      (xml \\ "Incomes" \\ "DirectPay" \\ "AmountOfThisPay" \\ "QuestionLabel").text should contain("Amount")
      (xml \\ "Incomes" \\ "DirectPay" \\ "AmountOfThisPay" \\ "Answer").text shouldEqual "25.00"
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhoPaidYouThisPay" \\ "QuestionLabel").text should contain("Who paid you this")
      (xml \\ "Incomes" \\ "DirectPay" \\ "WhoPaidYouThisPay" \\ "Answer").text shouldEqual "Disabled person"
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPay" \\ "QuestionLabel").text should contain("How often")
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPay" \\ "Answer").text shouldEqual "Weekly"
      (xml \\ "Incomes" \\ "DirectPay" \\ "HowOftenPaidThisPayOther").length shouldEqual 0
    }

    "Generate correct xml for RentalIncome Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(10, 2, 2016))
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("false"), Some("false"), Some("true"), Some("false"))

      val rentalIncome = new RentalIncome("Some rent money paid by tenant")
      val xml = Incomes.xml(claim + claimDate + incomeHeader + rentalIncome)
      (xml \\ "Incomes" \\ "RentalIncome" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "RentalIncomeInfo" \\ "QuestionLabel").text should contain("What rental income have you had since 10/02/2016?")
      (xml \\ "Incomes" \\ "RentalIncomeInfo" \\ "Answer").text shouldEqual "Some rent money paid by tenant"
    }

    "Generate correct xml for OtherPay Section" in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(10, 2, 2016))
      val incomeHeader = new YourIncomes("No", "No", Some("false"), Some("false"), Some("false"), Some("false"), Some("false"), Some("true"))

      val otherPayment = new OtherPayments("Was paid some money by carees brother")
      val xml = Incomes.xml(claim + claimDate + incomeHeader + otherPayment)
      (xml \\ "Incomes" \\ "AnyOtherPayment" \\ "Answer").text shouldEqual "Yes"

      (xml \\ "Incomes" \\ "OtherPaymentsInfo" \\ "QuestionLabel").text should contain("What other income have you had since 10/02/2016?")
      (xml \\ "Incomes" \\ "OtherPaymentsInfo" \\ "Answer").text shouldEqual "Was paid some money by carees brother"
    }
  }
  section("unit")
}

package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{BankBuildingSocietyDetails, Claim, HowWePayYou}
import app.AccountStatus._
import app.PaymentFrequency._
import models.SortCode


class PaymentSpec extends Specification with Tags {

  "Payment" should {

    "generate xml" in {
      val howWePayYou = HowWePayYou(likeToBePaid=BankBuildingAccount.name, paymentFrequency=EveryWeek.name)
      val xml = Payment.xml(Claim()().update(howWePayYou).asInstanceOf[Claim])

      (xml \\ "PaymentFrequency").text shouldEqual EveryWeek.name
      (xml \\ "InitialAccountQuestion").text shouldEqual BankBuildingAccount.name

      (xml \\ "Account").text  must not(beEmpty)

    }

    "generate <Account> xml if how to be paid is: " + BankBuildingAccount.name  in {
      val howWePayYou = HowWePayYou(likeToBePaid=BankBuildingAccount.name, paymentFrequency=EveryWeek.name)
      val sortCode =  SortCode("AB", "123456", "D")
      val bankBuildingSocietyDetails = BankBuildingSocietyDetails(accountHolderName="accountHolder", bankFullName="bankFullName", sortCode=sortCode, accountNumber="accountNumber", rollOrReferenceNumber="rollNumber" )
      val xml = Payment.account(Claim()().update(howWePayYou).update(bankBuildingSocietyDetails).asInstanceOf[Claim])

      (xml \\ "HolderName").text shouldEqual bankBuildingSocietyDetails.accountHolderName
      (xml \\ "BuildingSocietyDetails" \\ "AccountNumber").text shouldEqual bankBuildingSocietyDetails.accountNumber
      (xml \\ "BuildingSocietyDetails" \\ "RollNumber").text shouldEqual bankBuildingSocietyDetails.rollOrReferenceNumber
      (xml \\ "BuildingSocietyDetails" \\ "SortCode").text shouldEqual sortCode.stringify
      (xml \\ "BuildingSocietyDetails" \\ "Name").text shouldEqual bankBuildingSocietyDetails.bankFullName

    }

    "skip <Account> xml if how to be paid is NOT: " + BankBuildingAccount.name  in {
      val howWePayYou = HowWePayYou(likeToBePaid=AppliedForAccount.name, paymentFrequency=EveryWeek.name)
      val xml = Payment.xml(Claim()().update(howWePayYou).asInstanceOf[Claim])

      (xml \\ "Account").text  must beEmpty
    }

  }

}

package models.domain

import controllers.mappings.Mappings
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo}
import org.specs2.mutable.Specification

class OtherMoneySpec extends Specification {

  val withoutStatutoryPayments = AboutOtherMoney(YesNo(""), None, None, None,
    YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None),
    YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None))
  val withStatutoryPayments = AboutOtherMoney(YesNo(""), None, None, None,
    YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None),
    YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None))
  val claimWithNoOtherMoney = Claim(CachedClaim.key)
  val claimWithoutStatutoryPayments = claimWithNoOtherMoney.update(withoutStatutoryPayments)
  val claimWithStatutoryPayments = claimWithNoOtherMoney.update(withStatutoryPayments)

  "OtherMoney section" should {

    "tell whether the individual receives statutory sick pay" in {
      OtherMoney.receivesStatutorySickPay(claimWithNoOtherMoney) mustEqual false
      OtherMoney.receivesStatutorySickPay(claimWithoutStatutoryPayments) mustEqual false
      OtherMoney.receivesStatutorySickPay(claimWithStatutoryPayments) mustEqual true
    }

    "tell whether the individual receives other statutory payments" in {
      OtherMoney.receivesOtherStatutoryPay(claimWithNoOtherMoney) mustEqual false
      OtherMoney.receivesOtherStatutoryPay(claimWithoutStatutoryPayments) mustEqual false
      OtherMoney.receivesOtherStatutoryPay(claimWithStatutoryPayments) mustEqual true
    }

  }

}

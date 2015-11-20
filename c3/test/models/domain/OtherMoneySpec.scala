package models.domain

import controllers.mappings.Mappings
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo}
import org.specs2.mutable._
import utils.WithApplication

class OtherMoneySpec extends Specification {

  def withoutStatutoryPayments = AboutOtherMoney(YesNo(""), None, None, None,
    YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None),
    YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None))
  def withStatutoryPayments = AboutOtherMoney(YesNo(""), None, None, None,
    YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None),
    YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None))
  def claimWithNoOtherMoney = Claim(CachedClaim.key)
  def claimWithoutStatutoryPayments = claimWithNoOtherMoney.update(withoutStatutoryPayments)
  def claimWithStatutoryPayments = claimWithNoOtherMoney.update(withStatutoryPayments)

  "OtherMoney section" should {

    "tell whether the individual receives statutory sick pay" in new WithApplication {
      OtherMoney.receivesStatutorySickPay(claimWithNoOtherMoney) mustEqual false
      OtherMoney.receivesStatutorySickPay(claimWithoutStatutoryPayments) mustEqual false
      OtherMoney.receivesStatutorySickPay(claimWithStatutoryPayments) mustEqual true
    }

    "tell whether the individual receives other statutory payments" in new WithApplication {
      OtherMoney.receivesOtherStatutoryPay(claimWithNoOtherMoney) mustEqual false
      OtherMoney.receivesOtherStatutoryPay(claimWithoutStatutoryPayments) mustEqual false
      OtherMoney.receivesOtherStatutoryPay(claimWithStatutoryPayments) mustEqual true
    }

  }

}

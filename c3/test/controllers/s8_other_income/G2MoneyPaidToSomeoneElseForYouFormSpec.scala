package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G2MoneyPaidToSomeoneElseForYouFormSpec extends Specification with Tags {
  "Money Paid To Someone Else For You" should {
    val moneyAddedToBenefitSinceClaimDate = "yes"
  
      
    "map data into case class" in {
      G2MoneyPaidToSomeoneElseForYou.form.bind(
        Map("moneyAddedToBenefitSinceClaimDate" -> moneyAddedToBenefitSinceClaimDate
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.moneyAddedToBenefitSinceClaimDate must equalTo(moneyAddedToBenefitSinceClaimDate)
        }
      )
    }
    
  }
}
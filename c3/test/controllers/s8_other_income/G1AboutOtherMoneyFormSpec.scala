package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val partnerBenefits = "bar"
      
    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits" -> yourBenefits,
          "partnerBenefits" -> partnerBenefits
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits must equalTo(yourBenefits)
          f.partnerBenefits must equalTo(Some(partnerBenefits))
        }
      )
    }
    
    "allow optional fields to be left blank" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits" -> yourBenefits)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => "Ok" must equalTo("Ok")
      )
    }
    
    "reject invalid yesNo" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits" -> "INVALID")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("yesNo.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}
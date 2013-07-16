package controllers.s8_other_income

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "foo"
    val partnerBenefits = "bar"
      
    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits" -> yourBenefits,
          "partnerBenefits" -> partnerBenefits
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits must equalTo(Some(yourBenefits))
          f.partnerBenefits must equalTo(Some(partnerBenefits))
        }
      )
    }
    
    "allow optional fields to be left blank" in {
      G1AboutOtherMoney.form.bind(
        Map("" -> "")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => "Ok" must equalTo("Ok")
      )
    }
  }
}
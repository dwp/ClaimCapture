package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.domain.{NoRouting, MoreAboutYou}

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val yourBenefitsText1 = "bar"
    val yourBenefitsText2 = "fizz"
      
    "map data into case class" in {
      G1AboutOtherMoney.form(models.domain.Claim()).bind(
        Map("yourBenefits.answer" -> yourBenefits,
          "yourBenefits.text1" -> yourBenefitsText1,
          "yourBenefits.text2" -> yourBenefitsText2
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.yourBenefits.text1 must equalTo(Some(yourBenefitsText1))
          f.yourBenefits.text2 must equalTo(Some(yourBenefitsText2))
        }
      )
    }
    
    "reject invalid yesNo answer" in {
      G1AboutOtherMoney.form(models.domain.Claim()).bind(
        Map("yourBenefits.answer" -> "INVALID")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("yesNo.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
    
    "reject text1 enabled but text1 not filled in" in {
      val moreAboutYou: MoreAboutYou = MoreAboutYou(NoRouting,
                                                    hadPartnerSinceClaimDate = "yes",
                                                    eitherClaimedBenefitSinceClaimDate = "no",
                                                    beenInEducationSinceClaimDate = "yes",
                                                    receiveStatePension = "yes")
      val claim = models.domain.Claim().update(moreAboutYou)
      G1AboutOtherMoney.form(claim).bind(
        Map("yourBenefits.answer" -> yourBenefits)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("text1.required")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
    
        
    "reject text2 enabled but text2 not filled in" in {
      val moreAboutYou: MoreAboutYou = MoreAboutYou(NoRouting,
                                                    hadPartnerSinceClaimDate = "yes",
                                                    eitherClaimedBenefitSinceClaimDate = "yes",
                                                    beenInEducationSinceClaimDate = "yes",
                                                    receiveStatePension = "yes")
      val claim = models.domain.Claim().update(moreAboutYou)
      G1AboutOtherMoney.form(claim).bind(
        Map("yourBenefits.answer" -> yourBenefits, "yourBenefits.text1" -> yourBenefitsText1)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("text2.required")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
    
    "reject text1 and text2 enabled but neither not filled in" in { // Can't test text field2 as it requires
      val moreAboutYou: MoreAboutYou = MoreAboutYou(NoRouting,
                                                    hadPartnerSinceClaimDate = "yes",
                                                    eitherClaimedBenefitSinceClaimDate = "yes",
                                                    beenInEducationSinceClaimDate = "yes",
                                                    receiveStatePension = "yes")
      val claim = models.domain.Claim().update(moreAboutYou)
      G1AboutOtherMoney.form(claim).bind(
        Map("yourBenefits.answer" -> yourBenefits)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}
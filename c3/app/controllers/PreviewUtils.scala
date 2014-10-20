package controllers

import play.api.templates.Html
import play.api.i18n.{MMessages => Messages}
import scala.concurrent.ExecutionContext
import play.api.i18n.Lang

class PreviewUtils {

}

object PreviewUtils {

  var routesMap = yourDetailsRoute ++ otherMoneyRoute

  def yourDetailsRoute = {
    val g1YourDetailsRoute = controllers.s2_about_you.routes.G1YourDetails.present.toString
    val g2ContactDetailsRoute = controllers.s2_about_you.routes.G2ContactDetails.present.toString
    val g4NationalityRoute = controllers.s2_about_you.routes.G4NationalityAndResidency.present.toString
    val g7OtherEEARoute = controllers.s2_about_you.routes.G7OtherEEAStateOrSwitzerland.present.toString

    val routesMap = Map("about_you_full_name" -> g1YourDetailsRoute,
                        "about_you_nino" -> g1YourDetailsRoute,
                        "about_you_dob" -> g1YourDetailsRoute,
                        "about_you_address" -> g2ContactDetailsRoute,
                        "about_you_contact" -> g2ContactDetailsRoute,
                        "about_you_marital_status" -> g4NationalityRoute,
                        "about_you_claimDate" -> controllers.s1_2_claim_date.routes.G1ClaimDate.present.toString,
                        "about_you_nationality" -> g4NationalityRoute,
                        "about_you_abroad" -> controllers.s2_about_you.routes.G5AbroadForMoreThan52Weeks.present.toString,
                        "about_you_benefitsFromEEA" -> g7OtherEEARoute,
                        "about_you_workingForEEA" -> g7OtherEEARoute)
    routesMap
  }

  def otherMoneyRoute = {
    val g1AboutOtherMoneyRoute = controllers.s9_other_money.routes.G1AboutOtherMoney.present.toString

    val routesMap = Map("other_money_anyPaymentsSinceClaimDate" -> g1AboutOtherMoneyRoute,
                        "other_money_statutoryPay" -> g1AboutOtherMoneyRoute,
                        "other_money_otherStatutoryPay" -> g1AboutOtherMoneyRoute)
    routesMap
  }

  def fieldWithLink(id:String, name:String, value:String)(implicit lang:Lang) = {
    val label = Html(Messages(name))
    val row = s"<dt><a id=$id href=${routesMap(id)}>$label</a></dt><dd>$value</dd>"
    Html(row)
  }

  def apply() = new PreviewUtils()

}

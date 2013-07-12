package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.data.Forms._
import utils.helpers.CarersForm._
import play.api.i18n.Messages
import controllers.Mappings._
import models.domain.Claim
import models.yesNo.YesNo
import controllers.s4_care_you_provide.CareYouProvide.breaksInCare

object G10BreaksInCare extends Controller with Routing with CachedClaim {
  override val route = BreaksInCare.id -> routes.G10BreaksInCare.present

  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
  )(YesNo.apply)(YesNo.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(BreaksInCare)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g10_breaksInCare(form, breaksInCare, completedQuestionGroups, dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrors, breaksInCare, completedQuestionGroups, dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim))),
      hasBreaks => hasBreaks.answer match {
        case "yes" if breaksInCare.breaks.size < 10 => claim.update(breaksInCare) -> Redirect(routes.G11Break.present())
        case "yes" => claim.update(breaksInCare) -> Redirect(routes.G10BreaksInCare.present())
        case _ => claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed())
      })
  }

  def delete(id: String) = claiming { implicit claim => implicit request =>
    import play.api.libs.json.Json

    claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => {
        val updatedBreaksInCare = b.delete(id)
        val dateOfClaim = dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim)

        if (updatedBreaksInCare.breaks.isEmpty) claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.label", dateOfClaim)))
        else claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.more.label", dateOfClaim)))
      }

      case _ => BadRequest(s"""Failed to delete break with ID "$id" as claim currently has no breaks""")
    }
  }

  private def dateOfClaimCheckIfSpent35HoursCaringBeforeClaim(claim: Claim): String = {
    import language.postfixOps

    val spent35HoursCaringBeforeClaim = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => m.spent35HoursCaringBeforeClaim.answer == "yes"
      case _ => false
    }

    claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
      if (spent35HoursCaringBeforeClaim) (dmy - 6 months).`dd/MM/yyyy`
      else dmy.`dd/MM/yyyy`)
  }
}
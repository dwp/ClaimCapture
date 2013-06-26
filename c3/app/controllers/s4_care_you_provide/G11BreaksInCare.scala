package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G11BreaksInCare extends Controller with Routing with CachedClaim {
  override val route = BreaksInCare.id -> controllers.s4_care_you_provide.routes.G11BreaksInCare.present

  val breakInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText,
      "break" -> optional(mapping(
        "start" -> (dayMonthYear verifying validDate),
        "end" -> optional(dayMonthYear verifying validDateOnly),
        "whereYou" -> whereabouts.verifying(requiredWhereabouts),
        "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
        "medicalDuringBreak" -> optional(text)
      )((start, end, whereYou, wherePerson, medicalDuringBreak) => Break(java.util.UUID.randomUUID.toString, start, end, whereYou, wherePerson, medicalDuringBreak))
        ((b: Break) => Some(b.start, b.end, b.whereYou, b.wherePerson, b.medicalDuringBreak)))
    )(BreakInCare.apply)(BreakInCare.unapply))

  val breakForm = Form(
    mapping(
      "breakID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDate),
      "end" -> optional(dayMonthYear verifying validDateOnly),
      "whereYou" -> whereabouts.verifying(requiredWhereabouts),
      "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
      "medicalDuringBreak" -> optional(text)
    )(Break.apply)(Break.unapply))

  def present = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(q => q.id != BreaksInCare.id)

    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    Ok(views.html.s4_careYouProvide.g11_breaksInCare(breakInCareForm, breaksInCare, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(q => q.id != BreaksInCare.id)

    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    breakInCareForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_breaksInCare(formWithErrors, breaksInCare, completedQuestionGroups)),
      breakInCare => {
        val updatedBreaksInCare = breakInCare.break.fold(breaksInCare)(break => if (breaksInCare.breaks.size == 10) breaksInCare else breaksInCare.update(break))

        breakInCare.moreBreaks match {
          case "no" => claim.update(updatedBreaksInCare) -> Redirect(controllers.routes.CareYouProvide.completed())
          case "yes" if updatedBreaksInCare.breaks.size == 10 => claim.update(updatedBreaksInCare) -> Redirect(controllers.routes.CareYouProvide.completed)
          case _ => claim.update(updatedBreaksInCare) -> Redirect(routes.G11BreaksInCare.present)
        }
      })
  }

  def break(id: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b.breaks.find(_.id == id) match {
        case Some(b: Break) => Ok(views.html.s4_careYouProvide.g11_break(breakForm.fill(b)))
        case _ => Redirect(routes.G11BreaksInCare.present)
      }

      case _ => Redirect(routes.G11BreaksInCare.present)
    }
  }

  def breakSubmit = claiming { implicit claim => implicit request =>
    breakForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_break(formWithErrors)),
      break => {
        val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
          case Some(b: BreaksInCare) => b
          case _ => BreaksInCare()
        }

        claim.update(breaksInCare.update(break)) -> Redirect(routes.G11BreaksInCare.present)
      })
  }

  def deleteBreak(id: String) = claiming { implicit claim => implicit request =>
    import play.api.libs.json.Json

    claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => claim.update(b.delete(id)) -> Ok(Json.obj("id" -> id))
      case _ => BadRequest(s"""Failed to delete break with ID "$id" as claim currently has no breaks""")
    }
  }
}
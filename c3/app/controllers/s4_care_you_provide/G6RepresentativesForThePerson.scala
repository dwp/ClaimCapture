package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.RepresentativesForPerson
import play.api.data.{Form}
import play.api.data.Forms._
import controllers.Mappings._
import models.{YesNoWithDropDownAndText, YesNoWithDropDown}
import models.domain.Claim
import scala.Some
import utils.helpers.CarersForm._
import play.api.data.FormError

object G6RepresentativesForThePerson extends Controller with Routing with CachedClaim {

  override val route = RepresentativesForPerson.id -> routes.G6RepresentativesForThePerson.present

  def validateYouAct(input: YesNoWithDropDown): Boolean = {
    input.answer match {
      case `yes` => input.dropDownValue.isDefined
      case `no` => true
    }
  }

  def validateSomeoneElseAct(input: YesNoWithDropDownAndText): Boolean = {
    input.answer match {
      case `yes` => input.dropDownValue.isDefined
      case `no` => true
    }
  }

  val youActMapping = (
    "you" -> mapping(
      "actForPerson" -> nonEmptyText.verifying(validYesNo),
      "actAs" -> optional(nonEmptyText))(YesNoWithDropDown.apply)(YesNoWithDropDown.unapply)
      .verifying("required", validateYouAct _)
    )

  val someoneElseMapping = {
    "someoneElse" -> mapping(
      "actForPerson" -> nonEmptyText.verifying(validYesNo),
      "actAs" -> optional(nonEmptyText),
      "fullName" -> optional(text)
    )(YesNoWithDropDownAndText.apply)(YesNoWithDropDownAndText.unapply)
      .verifying("required", validateSomeoneElseAct _)
  }

  val form = Form(
    mapping(
      youActMapping,
      someoneElseMapping
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(RepresentativesForPerson)

  def present = claiming {
    implicit claim => implicit request =>
      val currentForm = claim.questionGroup(RepresentativesForPerson) match {
        case Some(r: RepresentativesForPerson) => form.fill(r)
        case _ => form
      }

      Ok(views.html.s4_care_you_provide.g6_representativesForThePerson(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>

      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("you", FormError("you.actAs", "error.required"))
            .replaceError("someoneElse", FormError("someoneElse.actAs", "error.required"))
          BadRequest(views.html.s4_care_you_provide.g6_representativesForThePerson(formWithErrorsUpdate, completedQuestionGroups))
        },
        representativesForPerson => claim.update(representativesForPerson) -> Redirect(routes.G7MoreAboutTheCare.present())
      )
  }

}
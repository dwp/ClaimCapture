package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.RepresentativesForPerson
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import play.api.data.FormError
import models.yesNo.{YesNoWithDropDownAndText, YesNoWithDropDown}
import CareYouProvide._

object G6RepresentativesForThePerson extends Controller with CachedClaim {
  val youActMapping =
    "you" -> mapping(
      "actForPerson" -> nonEmptyText(maxLength = 20).verifying(validYesNo),
      "actAs" -> optional(nonEmptyText(maxLength = 20))
    )(YesNoWithDropDown.apply)(YesNoWithDropDown.unapply)
      .verifying("required", YesNoWithDropDown.validate _)

  val someoneElseMapping =
    "someoneElse" -> mapping(
      "actForPerson" -> optional(nonEmptyText(maxLength = 20).verifying(validYesNo)),
      "actAs" -> optional(nonEmptyText(maxLength = 20)),
      "fullName" -> optional(text(maxLength = 120))
    )(YesNoWithDropDownAndText.apply)(YesNoWithDropDownAndText.unapply)
      .verifying("required", YesNoWithDropDownAndText.validate _)

  val form = Form(
     mapping(
      youActMapping,
      someoneElseMapping
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply)
      .verifying("required", RepresentativesForPerson.validate _))
      

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g6_representativesForThePerson(form.fill(RepresentativesForPerson), completedQuestionGroups(RepresentativesForPerson)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("you", FormError("you.actAs", "error.required"))
          .replaceError("someoneElse", FormError("someoneElse.actAs", "error.required"))
          .replaceError("", FormError("someoneElse.actForPerson", "error.required"))
        BadRequest(views.html.s4_care_you_provide.g6_representativesForThePerson(formWithErrorsUpdate, completedQuestionGroups(RepresentativesForPerson)))
      },
      representativesForPerson => claim.update(representativesForPerson) -> Redirect(routes.G7MoreAboutTheCare.present())
    )
  }
}
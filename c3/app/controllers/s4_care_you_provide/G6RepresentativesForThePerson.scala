package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.RepresentativesForPerson
import play.api.data.Form
import utils.helpers.CarersForm._
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.Claim

object G6RepresentativesForThePerson extends Controller with Routing with CachedClaim {

  override val route = RepresentativesForPerson.id -> routes.G6RepresentativesForThePerson.present

  val form = Form(
    mapping(
      "actForPerson" -> nonEmptyText.verifying(validYesNo),
      "actAs" -> optional(text),
      "someoneElseActForPerson" -> nonEmptyText.verifying(validYesNo),
      "someoneElseActAs" -> optional(text),
      "someoneElseFullName" -> optional(text)
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(RepresentativesForPerson)

  def present = claiming { implicit claim => implicit request =>
    val currentForm = claim.questionGroup(RepresentativesForPerson) match {
      case Some(r: RepresentativesForPerson) => form.fill(r)
      case _ => form
    }

    Ok(views.html.s4_care_you_provide.g6_representativesForThePerson(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    def actAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
      if (rfp.actForPerson == "yes" && rfp.actAs == None) form.fill(rfp).withError("actAs", "error.required")
      else form
    }

    def someoneElseActAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
      if (rfp.someoneElseActForPerson == "yes" && rfp.someoneElseActAs == None) form.fill(rfp).withError("someoneElseActAs", "error.required")
      else form
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g6_representativesForThePerson(formWithErrors, completedQuestionGroups)),
      implicit representativesForPerson => {
        val formValidations = actAs _ andThen someoneElseActAs _
        val timeOutsideUKFormValidated = formValidations(form)

        if (timeOutsideUKFormValidated.hasErrors) BadRequest(views.html.s4_care_you_provide.g6_representativesForThePerson(timeOutsideUKFormValidated, completedQuestionGroups))
        else claim.update(representativesForPerson) -> Redirect(routes.G7MoreAboutTheCare.present())
      })
  }
}
package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.RepresentativesForPerson
import play.api.data.Form
import utils.helpers.CarersForm._
import play.api.data.Forms._
import scala.Some

object G6RepresentativesForThePerson extends Controller with Routing with CachedClaim {

  override val route = RepresentativesForPerson.id -> controllers.s4_care_you_provide.routes.G6RepresentativesForThePerson.present

  val form = Form(
    mapping(
      "actForPerson" -> nonEmptyText,
      "actAs" -> optional(text),
      "someoneElseActForPerson" -> nonEmptyText,
      "someoneElseActAs" -> optional(text),
      "someoneElseFullName" -> optional(text)
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).filter(q => q.id != RepresentativesForPerson.id)

      val currentForm = claim.questionGroup(RepresentativesForPerson.id) match {
        case Some(h: RepresentativesForPerson) => form.fill(h)
        case _ => form
      }

      Ok(views.html.s4_careYouProvide.g6_representativesForThePerson(currentForm, completedQuestionGroups))

  }

  def submit = claiming {
    implicit claim => implicit request =>

      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != RepresentativesForPerson.id)

      def actAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
        if (rfp.actForPerson == "yes" && rfp.actAs == None) form.fill(rfp).withError("actAs", "error.required")
        else form
      }

      def someoneElseActAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
        if (rfp.someoneElseActForPerson == "yes" && rfp.someoneElseActAs == None) form.fill(rfp).withError("someoneElseActAs", "error.required")
        else form
      }

      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g6_representativesForThePerson(formWithErrors, completedQuestionGroups)),
        implicit representativesForPerson => {
          val formValidations = actAs _ andThen someoneElseActAs _
          val timeOutsideUKFormValidated = formValidations(form)

          if (timeOutsideUKFormValidated.hasErrors) BadRequest(views.html.s4_careYouProvide.g6_representativesForThePerson(timeOutsideUKFormValidated, completedQuestionGroups))
          else claim.update(representativesForPerson) -> Redirect(controllers.s4_care_you_provide.routes.G7MoreAboutTheCare.present)
        })

  }

}

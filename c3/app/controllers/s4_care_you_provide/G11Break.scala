package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.{BreaksInCareSummary, BreaksInCare, Break}
import models.view.CachedClaim
import play.api.data.FormError
import G10BreaksInCare.breaksInCare
import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.{YesNoWithDate, RadioWithText}


object G11Break extends Controller with CachedClaim {

  val whereWasPersonMapping =
    "wherePerson" -> mapping(
      "answer" -> carersNonEmptyText,
      "text" -> optional(carersText(maxLength = sixty))
    )(RadioWithText.apply)(RadioWithText.unapply)
      .verifying("wherePerson.text.required", RadioWithText.validateOnOther _)

  val whereWereYouMapping =
    "whereYou" -> mapping(
      "answer" -> carersNonEmptyText,
      "text" -> optional(carersText(maxLength = sixty))
    )(RadioWithText.apply)(RadioWithText.unapply)
      .verifying("whereYou.text.required", RadioWithText.validateOnOther _)

  val hasBreakEndedMapping =
    "hasBreakEnded" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "start" -> (dayMonthYear verifying validDate),
    "startTime" -> optional(carersText),
    hasBreakEndedMapping,
    "endTime" -> optional(carersText),
    whereWereYouMapping,
    whereWasPersonMapping,
    "medicalDuringBreak" -> carersNonEmptyText
  )(Break.apply)(Break.unapply))

  val backCall = routes.G10BreaksInCare.present()

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val fwe = formWithErrors
        .replaceError("whereYou.answer", errorRequired, FormError("whereYou",errorRequired,Seq("This field is required")))
        .replaceError("whereYou","whereYou.text.required",FormError("whereYou.text",errorRequired))
        .replaceError("whereYou.text",errorRestrictedCharacters,FormError("whereYou",errorRestrictedCharacters))
        .replaceError("wherePerson.answer", errorRequired, FormError("wherePerson",errorRequired,Seq("This field is required")))
        .replaceError("wherePerson","wherePerson.text.required",FormError("wherePerson.text",errorRequired))
        .replaceError("wherePerson.text",errorRestrictedCharacters,FormError("wherePerson",errorRestrictedCharacters))
        .replaceError("start.date",errorRequired, FormError("start",errorRequired, Seq("This field is required")))
        .replaceError("hasBreakEnded.answer",errorRequired, FormError("hasBreakEnded.answer",errorRequired, Seq("This field is required")))
        .replaceError("hasBreakEnded.date",errorRequired, FormError("hasBreakEnded.date",errorRequired, Seq("This field is required")))
        BadRequest(views.html.s4_care_you_provide.g11_break(fwe,backCall)(lang))
      },
      break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)

        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had any more breaks in care since...'
        claim.update(updatedBreaksInCare).delete(BreaksInCareSummary) -> Redirect(routes.G10BreaksInCare.present())
      })
  }


 def present(iterationID: String) = claimingWithCheck{ implicit claim =>  implicit request =>  lang =>
   val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())

    Ok(views.html.s4_care_you_provide.g11_break(form.fill(break),backCall)(lang))
  }
}
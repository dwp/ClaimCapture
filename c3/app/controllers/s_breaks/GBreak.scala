package controllers.s_breaks

import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.s_breaks.GBreaksInCare.breaksInCare
import models.domain.{Break, BreaksInCare, BreaksInCareSummary}
import models.view.CachedClaim
import models.yesNo.{RadioWithText, YesNoWithDate}
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._


object GBreak extends Controller with CachedClaim {

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
      .verifying("hasBreakEnded.date.required", YesNoWithDate.validate _)

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

  val backCall = routes.GBreaksInCare.present()

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
        .replaceError("hasBreakEnded","hasBreakEnded.date.required", FormError("hasBreakEnded.date",errorRequired, Seq("This field is required")))
        BadRequest(views.html.s_breaks.g_break(fwe,backCall)(lang))
      },
      break => {
        val updatedBreaksInCare = if (breaksInCare.breaks.size >= 10) breaksInCare else breaksInCare.update(break)

        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had any more breaks in care since...'
        claim.update(updatedBreaksInCare).delete(BreaksInCareSummary) -> Redirect(routes.GBreaksInCare.present())
      })
  }


 def present(iterationID: String) = claimingWithCheck{ implicit claim =>  implicit request =>  lang =>
   val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())

    Ok(views.html.s_breaks.g_break(form.fill(break),backCall)(lang))
  }
}
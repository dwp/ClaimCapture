package controllers.s_employment

import controllers.IterationID
import controllers.mappings.Mappings
import models.view.{Navigable, CachedClaim}
import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.{YourIncomes, Jobs, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import controllers.s_employment.Employment.jobs
import models.domain.Claim
import scala.reflect.ClassTag
import scala.language.postfixOps
import models.view.ClaimHandling.ClaimResult
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import play.api.i18n._
import play.api.Play.current

object GBeenEmployed extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "beenEmployed" -> (nonEmptyText verifying validYesNo)
  )(BeenEmployed.apply)(BeenEmployed.unapply))

  private def presentConditionally(c: => Either[Result,ClaimResult])(implicit claim: Claim, lang:Lang, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    val previousYourIncome = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    claim.questionGroup[YourIncomes].collect {
      case e: YourIncomes if (previousYourIncome.beenEmployedSince6MonthsBeforeClaim != e.beenEmployedSince6MonthsBeforeClaim && e.beenEmployedSince6MonthsBeforeClaim == yes ) => c
    }.getOrElse(redirect(lang))
  }

  /**
   * Redirect to about other money when self-employment and employment is answered no else
   * redirect to employment additional info page.
   *
   * @param lang
   * @param claim
   * @param request
   * @return
   */
  private def redirect(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    claim.questionGroup[YourIncomes].collect {
      case e: YourIncomes if e.beenEmployedSince6MonthsBeforeClaim == no && e.beenSelfEmployedSince1WeekBeforeClaim == no => Left(Redirect(controllers.your_income.routes.GStatutorySickPay.present()))
    }.getOrElse(Left(Redirect(controllers.s_self_employment.routes.GSelfEmploymentDates.present())))
  }

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
      presentConditionally(beenEmployed(lang))
  }

  private def beenEmployed(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    if(getCompletedJobs) {
      val f:Claim => Result = { implicit claim => Ok(views.html.s_employment.g_beenEmployed(form.fill(BeenEmployed)))}
      Right(trackBackToBeginningOfEmploymentSection(BeenEmployed)(f)(claim, request,ClassTag[BeenEmployed.type](BeenEmployed.getClass)) )
    } else if (getUncompletedJobs) {
      Left(Redirect(routes.GJobDetails.present(jobs.head.iterationID)))
    } else Left(Redirect(routes.GJobDetails.present(IterationID(form))))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    import controllers.mappings.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < app.ConfigProperties.getProperty("maximumJobs", 5) => Redirect(routes.GJobDetails.present(IterationID(form)))
      case _ => Redirect(controllers.s_self_employment.routes.GSelfEmploymentDates.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("beenEmployed", Mappings.errorRequired, FormError("beenEmployed", Mappings.errorRequired,Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
          displayPlaybackDatesFormat(request2lang, dmy - 6 months)))))
        BadRequest(views.html.s_employment.g_beenEmployed(formWithErrorsUpdate))
      },
      beenEmployed => clearUnfinishedJobs.update(beenEmployed) -> next(beenEmployed))
  }

  private def clearUnfinishedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    claim.update(Jobs(jobs.jobs.filter(_.completed == true)))
  }

  private def getCompletedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    Jobs(jobs.jobs.filter(_.completed == true)).size > 0
  }

  private def getUncompletedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    Jobs(jobs.jobs.filter(_.completed == false)).size > 0
  }
}


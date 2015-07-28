package controllers.s8_employment

import controllers.IterationID
import controllers.mappings.Mappings
import models.view.{Navigable, CachedClaim}
import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.{Employment => Emp, Jobs, BeenEmployed}
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import controllers.s8_employment.Employment.jobs
import models.domain.Claim
import scala.reflect.ClassTag
import play.api.i18n.Lang
import scala.language.postfixOps
import models.view.ClaimHandling.ClaimResult
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat

object G2BeenEmployed extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenEmployed" -> (nonEmptyText verifying validYesNo)
  )(BeenEmployed.apply)(BeenEmployed.unapply))

  private def presentConditionally(c: => Either[Result,ClaimResult], lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    claim.questionGroup[Emp].collect {
      case e: Emp if e.beenEmployedSince6MonthsBeforeClaim == yes => c
    }.getOrElse(redirect(lang))
  }

  /**
   * Redirect to about other money when self employment and employment is answered no else
   * redirect to employment additional info page.
   *
   * @param lang
   * @param claim
   * @param request
   * @return
   */
  private def redirect(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    claim.questionGroup[Emp].collect {
      case e: Emp if e.beenEmployedSince6MonthsBeforeClaim == no && e.beenSelfEmployedSince1WeekBeforeClaim == no => Left(Redirect(controllers.s9_other_money.routes.G1AboutOtherMoney.present()))
    }.getOrElse(Left(Redirect(controllers.s8_employment.routes.G9EmploymentAdditionalInfo.present())))
  }

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
      presentConditionally(beenEmployed(lang),lang)
  }

  private def beenEmployed(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): Either[Result,ClaimResult] = {
    if(getCompletedJobs) {
      val f:Claim => Result = { implicit claim => Ok(views.html.s8_employment.g2_beenEmployed(form.fill(BeenEmployed))(lang))}
      Right(trackBackToBeginningOfEmploymentSection(BeenEmployed)(f)(claim, request,ClassTag[BeenEmployed.type](BeenEmployed.getClass)) )
    }
    else Left(Redirect(routes.G3JobDetails.present(IterationID(form))))
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    import controllers.mappings.Mappings.yes

    def next(beenEmployed: BeenEmployed) = beenEmployed.beenEmployed match {
      case `yes` if jobs.size < Mappings.five => Redirect(routes.G3JobDetails.present(IterationID(form)))
      case _ => Redirect(controllers.s8_employment.routes.G9EmploymentAdditionalInfo.present())
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("beenEmployed", Mappings.errorRequired, FormError("beenEmployed", Mappings.errorRequired,Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
          displayPlaybackDatesFormat(lang, dmy - 6 months)))))
        BadRequest(views.html.s8_employment.g2_beenEmployed(formWithErrorsUpdate)(lang))
      },
      beenEmployed => clearUnfinishedJobs.update(beenEmployed) -> next(beenEmployed))
  }.withPreviewConditionally[BeenEmployed](beenEmp => beenEmp._2.beenEmployed == Mappings.no)

  private def clearUnfinishedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    claim.update(Jobs(jobs.jobs.filter(_.completed == true)))
  }

  private def getCompletedJobs(implicit claim: Claim) = {
    val jobs = claim.questionGroup[Jobs].getOrElse(Jobs())
    Jobs(jobs.jobs.filter(_.completed == true)).size > 0
  }
}
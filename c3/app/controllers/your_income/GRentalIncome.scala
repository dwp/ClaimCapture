package controllers.your_income

import controllers.CarersForms._
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain.{RentalIncome, Claim, YourIncomes}
import models.view.ClaimHandling._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.{AnyContent, Controller, Request}
import utils.helpers.CarersForm._
import utils.helpers.HtmlLabelHelper._

object GRentalIncome extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "rentalIncomeInfo" -> carersNonEmptyText(maxLength = Mappings.threeThousand)
  )(RentalIncome.apply)(RentalIncome.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(rentalIncome)
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("rentalIncomeInfo", Mappings.errorRequired, FormError("rentalIncomeInfo", Mappings.errorRequired,Seq(displayPlaybackDatesFormat(request2lang, claim.dateOfClaim.getOrElse(DayMonthYear.today)))))
        BadRequest(views.html.your_income.rentalIncome(formWithErrorsUpdate))
      },
      rentalIncome => claim.update(rentalIncome) -> Redirect(controllers.your_income.routes.GOtherPayments.present()))
  } withPreview()

  def rentalIncome(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(RentalIncome) { implicit claim => Ok(views.html.your_income.rentalIncome(form.fill(RentalIncome))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val previousYourIncome = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    if (((previousYourIncome.yourIncome_rentalincome != yourIncomes.yourIncome_rentalincome && yourIncomes.yourIncome_rentalincome.isDefined || claim.questionGroup[RentalIncome].getOrElse(RentalIncome()).rentalIncomeInfo.isEmpty) || !request.flash.isEmpty) && models.domain.YourIncomeRentalIncome.visible) c
    else if (claim.navigation.beenInPreview)claim -> Redirect(controllers.preview.routes.Preview.present().url + getReturnToSummaryValue(claim))
    else claim -> Redirect(controllers.s_pay_details.routes.GHowWePayYou.present())
  }
}
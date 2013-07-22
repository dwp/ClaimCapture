package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{ Claim, StatutorySickPay }
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.yesNo.YesNoWith2Text

object G5StatutorySickPay extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(StatutorySickPay)

  val form = Form(
    mapping(
      "haveYouHadAnyStatutorySickPay" -> nonEmptyText(maxLength = sixty),
      "howMuch" -> optional(text(maxLength = sixty)),
      "howOften" -> optional(text(maxLength = sixty)),
      "employersName" -> optional(nonEmptyText(maxLength = sixty)),
      "employersAddress" -> optional(address),
      "employersPostcode" -> optional(text verifying validPostcode),
      call(routes.G5StatutorySickPay.present())
    )(StatutorySickPay.apply)(StatutorySickPay.unapply)
    .verifying("employersName.required", c => validateText(c.haveYouHadAnyStatutorySickPay, c.employersName)))

  def validateText(answer: String, text:Option[String], required:Boolean = true) = {
    answer match {
      case `yes` => if(required) text.isDefined else true
      case `no` => true
    }
  }
  
  def present = claiming { implicit claim =>
    implicit request =>
      OtherMoney.whenVisible(claim)(() => {
        val currentForm = claim.questionGroup(StatutorySickPay) match {
          case Some(t: StatutorySickPay) => form.fill(t)
          case _ => form
        }
        Ok(views.html.s8_other_money.g5_statutorySickPay(currentForm, completedQuestionGroups))
      })
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s8_other_money.g5_statutorySickPay(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G4PersonContactDetails.present()) // TODO replace with next page to go to
      )
  }
}
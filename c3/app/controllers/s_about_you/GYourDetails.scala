package controllers.s_about_you

import controllers.mappings.Mappings
import models.NationalInsuranceNumber
import play.api.Play._
import play.api.data.validation._
import utils.CommonValidation

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AnyContent, Request, Controller}
import controllers.mappings.Mappings._
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.Logger
import scala.language.postfixOps
import controllers.mappings.NINOMappings._
import app.ConfigProperties._
import play.api.i18n._

object GYourDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def form(implicit request: Request[AnyContent]) = Form(mapping(
    "title" -> carersNonEmptyText(maxLength = Mappings.twenty),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = CommonValidation.NAME_MAX_LENGTH),
    "nationalInsuranceNumber" -> nino.verifying(stopOnFirstFail (filledInNino, validNino, isSameNinoAsDPOrPartner)),
    "dateOfBirth" -> dayMonthYear.verifying(validDateOfBirth)
  )(YourDetails.apply)(YourDetails.unapply)
  )

  def present = claiming {implicit claim => implicit request => implicit request2lang =>
    Logger.debug(s"Start your details ${claim.key} ${claim.uuid}")
    track(YourDetails) { implicit claim => Ok(views.html.s_about_you.g_yourDetails(form.fill(YourDetails))) }
  }

  def submit = claiming {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s_about_you.g_yourDetails(formWithErrors))
      },
      yourDetails => { // Show pay details if the person is below age.hide.paydetails years of age on the day of the claim (claim date)
        val updatedClaim:Claim = previewClaim(claim, yourDetails)
        updatedClaim.update(yourDetails) -> Redirect(routes.GMaritalStatus.present())
      })
  } withPreview()

  def showPayDetails(claim:Claim, yourDetails:YourDetails):Boolean = {
    claim.dateOfClaim match {
      case Some(dmy) => yourDetails.dateOfBirth.yearsDiffWith(dmy) < getIntProperty("age.hide.paydetails")
      case _ => false
    }
  }

  def previewClaim(claim:Claim, yourDetails:YourDetails): Claim = {
    if (!showPayDetails(claim, yourDetails)) {
      claim.delete(HowWePayYou).hideSection(PayDetails)
    } else {
      val updatedClaim = claim.showSection(PayDetails)
      val howWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
      if (claim.navigation.beenInPreview && howWePayYou.likeToBePaid == "") updatedClaim.update(howWePayYou.copy(paymentFrequency = "Every week", likeToBePaid = "no", bankDetails = None))
      else updatedClaim
    }
  }

  private def isSameNinoAsDPOrPartner(implicit request: Request[AnyContent]): Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.nino") {
    case nino@NationalInsuranceNumber(Some(_)) => checkSameValues(nino.nino.get.toUpperCase, request)
    case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
  }

  private def checkSameValues(nino: String, request: Request[AnyContent]) = {
    val claim = fromCache(request).getOrElse(new Claim("xxxx"))
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val partnerDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    if (partnerNINO(partnerDetails) == nino) Invalid(ValidationError("error.partner.and.you.nationalInsuranceNumber", partnerName(partnerDetails), pageName(request)))
    else if (dpNINO(theirPersonalDetails) == nino) Invalid(ValidationError("error.dp.and.you.nationalInsuranceNumber", dpName(theirPersonalDetails), pageName(request)))
    else Valid
  }
}


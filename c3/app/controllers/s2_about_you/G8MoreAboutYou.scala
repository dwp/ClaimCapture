package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import controllers.Mappings._
import models.domain._

object G8MoreAboutYou extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "maritalStatus" -> nonEmptyText,
    "hadPartnerSinceClaimDate" -> optional(nonEmptyText.verifying(validYesNo)),
    "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo)
  )(MoreAboutYou.apply)(MoreAboutYou.unapply)
    .verifying("hadPartnerSinceClaimDate.required",MoreAboutYou.validateHadPartner _)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    claim.questionGroup(OtherEEAStateOrSwitzerland) match {
      case Some(n) => track(MoreAboutYou) { implicit claim => Ok(views.html.s2_about_you.g8_moreAboutYou(form.fill(MoreAboutYou))) }
      case _ => Redirect(startPage)
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("hadPartnerSinceClaimDate", "error.required", FormError("hadPartnerSinceClaimDate", "error.required",Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          .replaceError("", "hadPartnerSinceClaimDate.required", FormError("hadPartnerSinceClaimDate", "error.required",Seq(claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))))
        BadRequest(views.html.s2_about_you.g8_moreAboutYou(formWithErrorsUpdate))}
      ,
      moreAboutYou => {
        val updatedClaim = claim.showHideSection(moreAboutYou.hadPartnerSinceClaimDate == Some(yes) || moreAboutYou.maritalStatus == "p", YourPartner)
                                .showHideSection(moreAboutYou.beenInEducationSinceClaimDate == yes, Education)

        updatedClaim.update(moreAboutYou) -> Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
      })
  }
}
package controllers.s_your_partner

import models.DayMonthYear
import play.api.Logger

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.text
import play.api.data.Forms.optional
import play.api.mvc.{Request, AnyContent, Controller, Action}
import controllers.mappings.Mappings.errorRequired
import controllers.mappings.Mappings.Name
import controllers.mappings.NINOMappings._
import controllers.mappings.Mappings.validDate
import controllers.mappings.Mappings.validYesNo
import controllers.mappings.Mappings.dayMonthYear
import controllers.mappings.Mappings.validNationality
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm.formBinding
import YourPartner.presentConditionally
import controllers.CarersForms.carersNonEmptyText
import controllers.CarersForms.carersText
import play.api.i18n.Lang
import models.view.ClaimHandling.ClaimResult
import controllers.mappings.Mappings

object GYourPartnerPersonalDetails extends Controller with CachedClaim with Navigable {

  def form(implicit claim: Claim):Form[YourPartnerPersonalDetails] = Form(mapping(
    "title" -> optional(carersNonEmptyText(maxLength = Mappings.five)),
    "titleOther" -> optional(carersText(maxLength = Mappings.twenty)),
    "firstName" -> optional(carersNonEmptyText(maxLength = Mappings.seventeen)),
    "middleName" -> optional(carersText(maxLength = Mappings.seventeen)),
    "surname" -> optional(carersNonEmptyText(maxLength = Name.maxLength)),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
    "dateOfBirth" -> optional(dayMonthYear.verifying(validDate)),
    "nationality" -> optional(text.verifying(validNationality)),
    "separated.fromPartner" -> optional(nonEmptyText.verifying(validYesNo)),
    "isPartnerPersonYouCareFor" -> optional(nonEmptyText.verifying(validYesNo)),
    "hadPartnerSinceClaimDate" -> nonEmptyText.verifying(validYesNo)
  )(YourPartnerPersonalDetails.apply)(YourPartnerPersonalDetails.unapply)
    .verifying("titleOther.required",YourPartnerPersonalDetails.verifyTitleOther _)
    .verifying("title.required", YourPartnerPersonalDetails.validateTitle _)
    .verifying("firstName.required", YourPartnerPersonalDetails.validateFirstName _)
    .verifying("surname.required", YourPartnerPersonalDetails.validateSurName _)
    .verifying("dateOfBirth.required", YourPartnerPersonalDetails.validateDateOfBirth _)
    .verifying("separated.fromPartner.required", YourPartnerPersonalDetails.validateSeperatedFromPartner _)
    .verifying("isPartnerPersonYouCareFor.required", YourPartnerPersonalDetails.validatePartnerPersonYoucareFor _)
    .verifying("nationality.required", YourPartnerPersonalDetails.validateNationalityIfPresent _)
  )

  def present:Action[AnyContent] = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    presentConditionally(yourPartnerPersonalDetails(lang),lang)
  }

  private def yourPartnerPersonalDetails(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(YourPartnerPersonalDetails) { implicit claim => Ok(views.html.s_your_partner.g_yourPartnerPersonalDetails(form.fill(YourPartnerPersonalDetails))(lang)) }
  }

  def submit:Action[AnyContent] = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("","titleOther.required",FormError("titleOther",errorRequired))
          .replaceError("", "title.required", FormError("title", errorRequired))
          .replaceError("", "firstName.required", FormError("firstName", errorRequired))
          .replaceError("", "surname.required", FormError("surname", errorRequired))
          .replaceError("", "dateOfBirth.required", FormError("dateOfBirth", errorRequired))
          .replaceError("", "separated.fromPartner.required", FormError("separated.fromPartner", errorRequired))
          .replaceError("", "isPartnerPersonYouCareFor.required", FormError("isPartnerPersonYouCareFor", errorRequired))
          .replaceError("", "nationality.required", FormError("nationality", errorRequired))
        BadRequest(views.html.s_your_partner.g_yourPartnerPersonalDetails(formWithErrorsUpdate)(lang))
      },
      f => {
        val preUpdatedClaim = clearTheirPersonalDetailsIfPartnerQuestionChanged(claim,f)

        preUpdatedClaim.update(f) -> Redirect(controllers.s_care_you_provide.routes.GTheirPersonalDetails.present())
      }
    )
  }.withPreviewConditionally(goToPreviewCondition)

  private def goToPreviewCondition(details: (Option[YourPartnerPersonalDetails],YourPartnerPersonalDetails)) = {
    import Mappings._

    val previewData = details._1
    val formData = details._2
    val matchingValue = previewData -> formData.isPartnerPersonYouCareFor
    Logger.info(s"matching value $matchingValue")
    matchingValue match {
      case (Some(data),Some(isPartnerPerson))
        if data.isPartnerPersonYouCareFor.nonEmpty && data.isPartnerPersonYouCareFor.get != isPartnerPerson => false
      case (Some(data),None)
        if data.isPartnerPersonYouCareFor.nonEmpty => false
      case (Some(YourPartnerPersonalDetails(_,_,_,_,_,_,_,_,_,_,None,_)),Some(_)) => false

      case _ => true
    }
  }

  def clearTheirPersonalDetailsIfPartnerQuestionChanged(claim:Claim,formData:YourPartnerPersonalDetails) = {
    claim.questionGroup[YourPartnerPersonalDetails] -> claim.questionGroup[TheirPersonalDetails] match {
      case (Some(oldData),Some(theirPersonalDetails)) =>

        val tupleData = (oldData.hadPartnerSinceClaimDate -> formData.hadPartnerSinceClaimDate) ->
          (oldData.isPartnerPersonYouCareFor -> formData.isPartnerPersonYouCareFor)

         tupleData match {
           case (("no","yes"),(None,Some("no"))) =>
             //This case is when we change the partner question from no -> yes and we specify the DP is not our partner
             //In this case we don't want to wipe the data
             claim
          case ((oldQ,newQ),_) if oldQ != newQ =>
            wipeTheirPersonalDetailsData(claim,theirPersonalDetails)

           case (_,(Some(oldQ), Some(newQ))) if oldQ != newQ =>
             wipeTheirPersonalDetailsData(claim,theirPersonalDetails)

          case _ => claim
        }
      case _ => claim
    }

  }

  def wipeTheirPersonalDetailsData(claim:Claim,theirPersonalDetails:TheirPersonalDetails) = {
    claim.delete(TheirPersonalDetails)
  }
}


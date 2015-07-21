package controllers.s4_care_you_provide

import controllers.mappings.Mappings

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._
import controllers.CarersForms._
import models.DayMonthYear
import controllers.mappings.NINOMappings._

object G1TheirPersonalDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "relationship" -> carersNonEmptyText(maxLength = 35),
    "title" -> carersNonEmptyText(maxLength = Mappings.five),
    "titleOther" -> optional(carersText(maxLength = Mappings.twenty)),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "liveAtSameAddressCareYouProvide" -> nonEmptyText.verifying(validYesNo)
  )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply)
    .verifying("titleOther.required",TheirPersonalDetails.verifyTitleOther _)
  )

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    val isPartnerPersonYouCareFor = YourPartner.visible &&
                                    claim.questionGroup[YourPartnerPersonalDetails].exists(_.isPartnerPersonYouCareFor.getOrElse("") == "yes")

    val currentForm = if (isPartnerPersonYouCareFor) {
      claim.questionGroup(YourPartnerPersonalDetails) match {
        case Some(t: YourPartnerPersonalDetails) =>
          val theirPersonalDetails =  claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
          form.fill(TheirPersonalDetails( relationship = theirPersonalDetails.relationship,
                                          title = t.title.getOrElse(""),
                                         firstName = t.firstName.getOrElse(""),
                                         middleName = t.middleName,
                                         surname = t.surname.getOrElse(""),
                                         nationalInsuranceNumber = t.nationalInsuranceNumber,
                                         dateOfBirth = t.dateOfBirth.getOrElse(DayMonthYear(None,None,None)),
                                         liveAtSameAddressCareYouProvide = theirPersonalDetails.liveAtSameAddressCareYouProvide)) // Pre-populate form with values from YourPartnerPersonalDetails
        case _ => form // Blank form (user can only get here if they skip sections by manually typing URL).
      }
    } else {
      form.fill(TheirPersonalDetails)
    }

    track(TheirPersonalDetails) { implicit claim => Ok(views.html.s4_care_you_provide.g1_theirPersonalDetails(currentForm)(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("","titleOther.required",FormError("titleOther","constraint.required"))
        BadRequest(views.html.s4_care_you_provide.g1_theirPersonalDetails(updatedFormWithErrors)(lang))
      },
      theirPersonalDetails => {
        val liveAtSameAddress = theirPersonalDetails.liveAtSameAddressCareYouProvide == yes

        val updatedClaim = if (liveAtSameAddress) {
          val theirContactDetailsForm = claim.questionGroup[ContactDetails].map { cd =>
            G2TheirContactDetails.form.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
          }.getOrElse(form)

          claim.update(theirContactDetailsForm.fold(p => TheirContactDetails(),p => p))
        }else{
          //If we are changing to "do they live same addres? No" when it was yes before, we will remove the personal contact details.
          if (claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails()).liveAtSameAddressCareYouProvide == yes)
            claim.delete(TheirContactDetails)
          else
            claim
        }

        updatedClaim.update(theirPersonalDetails) -> Redirect(routes.G2TheirContactDetails.present())
      })
  } withPreview()
}
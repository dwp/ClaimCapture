package controllers.circs.s2_report_changes

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._
import models.view.{CachedChangeOfCircs, Navigable}
import utils.helpers.CarersForm._
import models.domain._
import models.yesNo.{YesNoWithAddress, YesNoWithDateAndQs, OptYesNoWithText}
import controllers.CarersForms._

object G6AddressChange extends Controller with CachedChangeOfCircs with Navigable  {
  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate)),
      "caredForChangedAddress" -> optional(text verifying validYesNo)
    )(YesNoWithDateAndQs.apply)(YesNoWithDateAndQs.unapply)
      .verifying("dateRequired", YesNoWithDateAndQs.validateDateOnNo _)

  val changedAddressMapping =
    "caredForChangedAddress" -> mapping(
      "answer" -> optional(carersText()),
      "sameAddress" -> optional(text verifying validYesNo)
    )(OptYesNoWithText.apply)(OptYesNoWithText.unapply)

  val sameAddressMapping =
    "sameAddress" -> mapping(
      "answer" -> optional(carersText()),
      "theirNewAddress" -> optional(address.verifying(requiredAddress)),
      "theirNewPostcode" -> optional(text verifying validPostcode)
    )(YesNoWithAddress.apply)(YesNoWithAddress.unapply)

  val form = Form(mapping(
    "previousAddress" -> address.verifying(requiredAddressWithTwoLines),
    "previousPostcode" -> optional(text verifying validPostcode),
    stillCaringMapping,
    "newAddress" -> address.verifying(requiredAddress),
    "newPostcode" -> optional(text verifying validPostcode),
    changedAddressMapping,
    sameAddressMapping,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesAddressChange.apply)(CircumstancesAddressChange.unapply)
    .verifying("caredForChangedAddress.answer", validateCaredForChangedAddress _)
    .verifying("sameAddress.answer", validateSameAddress _)
    .verifying("sameAddress.theirNewAddress", validateTheirNewAddress _)
  )

  private def validateCaredForChangedAddress(form: CircumstancesAddressChange) = {
    form.stillCaring.answer match {
      case `yes` => form.caredForChangedAddress.answer.isDefined
      case _ => true
    }
  }

  private def validateSameAddress(form: CircumstancesAddressChange) = {
    form.caredForChangedAddress.answer match {
      case Some(`yes`) => form.sameAddress.answer.isDefined
      case _ => true
    }
  }

  private def validateTheirNewAddress(form: CircumstancesAddressChange) = {
    form.stillCaring.answer match {
      case `yes` =>
        if(form.sameAddress.answer == Some("no")) form.sameAddress.address.isDefined
        else true
      case _ => true
    }
  }

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesAddressChange) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g6_addressChange(form.fill(CircumstancesAddressChange))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("stillCaring","dateRequired", FormError("stillCaring.date", "error.required"))
          .replaceError("","sameAddress.answer", FormError("sameAddress.answer", "error.required"))
          .replaceError("","sameAddress.theirNewAddress", FormError("sameAddress.theirNewAddress", "error.required"))
          .replaceError("","caredForChangedAddress.answer", FormError("caredForChangedAddress.answer", "error.required"))
        BadRequest(views.html.circs.s2_report_changes.g6_addressChange(updatedFormWithErrors)(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
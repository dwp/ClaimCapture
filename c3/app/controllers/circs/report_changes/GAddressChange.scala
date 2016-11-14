package controllers.circs.report_changes

import controllers.mappings.AddressMappings
import play.api.Play._

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
import play.api.i18n._

object GAddressChange extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
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
      "theirNewAddress" -> optional(address(AddressMappings.CAREENEW)),
      "theirNewPostcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode))
    )(YesNoWithAddress.apply)(YesNoWithAddress.unapply)

  val form = Form(mapping(
    "previousAddress" -> address(AddressMappings.CARERPREVIOUS),
    "previousPostcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode)),
    stillCaringMapping,
    "newAddress" -> address(AddressMappings.CARERNEW),
    "newPostcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode)),
    changedAddressMapping,
    sameAddressMapping,
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesAddressChange.textMaxLength))
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
        if (form.sameAddress.answer == Some("no")) form.sameAddress.address.isDefined
        else true
      case _ => true
    }
  }

  def present = claiming { implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesAddressChange) {
      implicit circs => Ok(views.html.circs.report_changes.addressChange(form.fill(CircumstancesAddressChange)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("stillCaring", "dateRequired", FormError("stillCaring.date", errorRequired))
          .replaceError("", "sameAddress.answer", FormError("sameAddress.answer", errorRequired))
          .replaceError("", "sameAddress.theirNewAddress", FormError("sameAddress.theirNewAddress", "error.careenewaddress.lines.required"))
          .replaceError("", "caredForChangedAddress.answer", FormError("caredForChangedAddress.answer", errorRequired))
        BadRequest(views.html.circs.report_changes.addressChange(updatedFormWithErrors))
      },
      addressChange => circs.update(formatPostCodes(addressChange)) -> Redirect(controllers.circs.your_details.routes.GYourDetails.present())
    )
  }

  private def formatPostCodes(addressChange: CircumstancesAddressChange): CircumstancesAddressChange = {
    addressChange.copy(
      previousPostcode = Some(formatPostCode(addressChange.previousPostcode.getOrElse(""))),
      newPostcode = Some(formatPostCode(addressChange.newPostcode.getOrElse(""))),
      sameAddress = addressChange.sameAddress.copy(postCode = Some(formatPostCode(addressChange.sameAddress.postCode.getOrElse(""))))
    )
  }
}

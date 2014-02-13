package controllers.circs.s2_report_changes

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.view.{CachedChangeOfCircs, Navigable}
import utils.helpers.CarersForm._
import models.domain._
import models.yesNo.{YesNoWithAddress, YesNoWithDateAndQs, YesNoWithText}

object G6AddressChange extends Controller with CachedChangeOfCircs with Navigable  {
  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate)),
      "caredForChangedAddress" -> optional(text verifying validYesNo)
    )(YesNoWithDateAndQs.apply)(YesNoWithDateAndQs.unapply)
      .verifying("required", YesNoWithDateAndQs.validateNo _)

  val changedAddressMapping =
    "caredForChangedAddress" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "sameAddress" -> optional(text verifying validYesNo)
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val sameAddressMapping =
    "sameAddress" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "theirNewAddress" -> optional(address.verifying(requiredAddress)),
      "theirNewPostcode" -> optional(text verifying validPostcode)
    )(YesNoWithAddress.apply)(YesNoWithAddress.unapply)
      .verifying("required", YesNoWithAddress.validateOnNo _)

  val form = Form(mapping(
    stillCaringMapping,
    "newAddress" -> address.verifying(requiredAddress),
    "newPostcode" -> optional(text verifying validPostcode),
    changedAddressMapping,
    sameAddressMapping
  )(CircumstancesAddressChange.apply)(CircumstancesAddressChange.unapply))

  def present = claiming { implicit circs => implicit request =>
    track(CircumstancesAddressChange) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g6_addressChange(form.fill(CircumstancesAddressChange)))
    }
  }

  def submit = claiming { implicit circs => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g6_addressChange(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
package controllers.s_care_you_provide

import controllers.mappings.AddressMappings._
import controllers.mappings.{AddressMappings, Mappings}
import controllers.s_your_partner.GYourPartnerPersonalDetails._
import models.yesNo.YesNoMandWithAddress
import play.api.Play._
import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
import gov.dwp.carers.xml.validation.CommonValidation
import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.{AnyContent, Request, Controller}
import controllers.mappings.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._
import controllers.CarersForms._
import models.{NationalInsuranceNumber, DayMonthYear}
import controllers.mappings.NINOMappings._
import play.api.i18n._

object GTheirPersonalDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val addressMapping = "theirAddress"->mapping(
    "answer" -> nonEmptyText.verifying(validYesNo),
    "address" -> optional(address(AddressMappings.CAREE)),
    "postCode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode))
      )(YesNoMandWithAddress.apply)(YesNoMandWithAddress.unapply)

  def form(implicit request: Request[AnyContent]) = Form(mapping(
    "title" -> carersNonEmptyText(maxLength = Mappings.twenty),
    "firstName" -> nonEmptyText(maxLength = CommonValidation.FIRSTNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "middleName" -> optional(text(maxLength = CommonValidation.MIDDLENAME_MAX_LENGTH).verifying(YourDetails.validName)),
    "surname" -> nonEmptyText(maxLength = CommonValidation.SURNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "nationalInsuranceNumber" -> optional(nino.verifying(stopOnFirstFail (validNino, isSameNinoAsDPOrPartner))),
    "dateOfBirth" -> dayMonthYear.verifying(validDateOfBirth),
    "relationship" -> carersNonEmptyText(maxLength = 35),
    addressMapping
  )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply)
    .verifying("theirAddress.address", validateSameAddressAnswer _)
  )

  private def validateSameAddressAnswer(form: TheirPersonalDetails) = form.theirAddress.answer match {
      case `no` => form.theirAddress.address.isDefined
      case _ => true
    }


  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    val isPartnerPersonYouCareFor = YourPartner.visible &&
      claim.questionGroup[YourPartnerPersonalDetails].exists(_.isPartnerPersonYouCareFor.getOrElse("") == "yes")

    val currentForm = if (isPartnerPersonYouCareFor) {
      claim.questionGroup(YourPartnerPersonalDetails) match {
        case Some(t: YourPartnerPersonalDetails) =>
          val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
          form.fill(TheirPersonalDetails(relationship = theirPersonalDetails.relationship,
            title = t.title.getOrElse(""),
            firstName = t.firstName.getOrElse(""),
            middleName = t.middleName,
            surname = t.surname.getOrElse(""),
            nationalInsuranceNumber = t.nationalInsuranceNumber,
            dateOfBirth = t.dateOfBirth.getOrElse(DayMonthYear(None, None, None)),
            theirAddress = theirPersonalDetails.theirAddress
          )) // Pre-populate form with values from YourPartnerPersonalDetails - this is for the case that the Caree is your partner
        case _ => form // Blank form (user can only get here if they skip sections by manually typing URL).
      }
    } else {
      form.fill(TheirPersonalDetails)
    }

    track(TheirPersonalDetails) { implicit claim => Ok(views.html.s_care_you_provide.g_theirPersonalDetails(currentForm)) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("","theirAddress.address", FormError("theirAddress.address", "error.careeaddress.lines.required"))
        BadRequest(views.html.s_care_you_provide.g_theirPersonalDetails(updatedFormWithErrors))
      },
      theirPersonalDetails => {
        val liveAtSameAddress = theirPersonalDetails.theirAddress.answer == yes

        //copy the address from the carer
        val updatedTheirPersonalDetails = if(liveAtSameAddress){
          claim.questionGroup[ContactDetails].map{ cd =>
            theirPersonalDetails.copy(theirAddress = YesNoMandWithAddress(answer = yes, address= Some(cd.address), postCode = cd.postcode))
          }.getOrElse(theirPersonalDetails)
        }else{
          theirPersonalDetails
        }

        claim.update(formatPostCodes(updatedTheirPersonalDetails)) -> Redirect(routes.GMoreAboutTheCare.present())
      })
  } withPreview()

  private def formatPostCodes(theirPersonalDetails : TheirPersonalDetails): TheirPersonalDetails = {
    theirPersonalDetails.copy(
      theirAddress = theirPersonalDetails.theirAddress.copy(
        postCode = Some(formatPostCode(theirPersonalDetails.theirAddress.postCode.getOrElse("")))))
  }

  private def isSameNinoAsDPOrPartner(implicit request: Request[AnyContent]): Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.nino") {
    case nino@NationalInsuranceNumber(Some(_)) => checkSameValues(nino.nino.get.toUpperCase.replace(" ", ""), request)
    case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
  }

  private def checkSameValues(nino: String, request: Request[AnyContent]) = {
    val claim = fromCache(request).getOrElse(new Claim("xxxx"))
    val partnerDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    if (yourNINO(yourDetails) == nino) Invalid(ValidationError("error.you.and.dp.nationalInsuranceNumber", yourName(yourDetails), pageName(request)))
    else if (partnerNINO(partnerDetails) == nino && partnerDetails.isPartnerPersonYouCareFor.getOrElse("no") == Mappings.no) Invalid(ValidationError("error.partner.and.dp.nationalInsuranceNumber", partnerName(partnerDetails), pageName(request)))
    else Valid
  }
}

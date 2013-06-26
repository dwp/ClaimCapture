package controllers

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import Mappings._
import models.domain._
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import forms.CareYouProvide._
import utils.helpers.CarersForm._
import controllers.s4_care_you_provide.{G11BreaksInCare, G10HasBreaks, G9ContactDetailsOfPayingPerson}

object CareYouProvide extends Controller with CachedClaim {
  import Routing._

  val route: ListMap[String, Call] = ListMap(TheirPersonalDetails.id -> routes.CareYouProvide.theirPersonalDetails,
                                             TheirContactDetails.id -> routes.CareYouProvide.theirContactDetails,
                                             RepresentativesForPerson.id -> routes.CareYouProvide.representativesForPerson,
                                             MoreAboutThePerson.id -> routes.CareYouProvide.moreAboutThePerson,
                                             PreviousCarerPersonalDetails.id -> routes.CareYouProvide.previousCarerPersonalDetails,
                                             PreviousCarerContactDetails.id -> routes.CareYouProvide.previousCarerContactDetails,
                                             MoreAboutTheCare.id -> routes.CareYouProvide.moreAboutTheCare,
                                             G9ContactDetailsOfPayingPerson,
                                             G10HasBreaks,
                                             G11BreaksInCare)


  def theirPersonalDetails = claiming { implicit claim => implicit request =>
    val theirPersonalDetailsQGForm: Form[TheirPersonalDetails] = claim.questionGroup(TheirPersonalDetails.id) match {
      case Some(t: TheirPersonalDetails) => theirPersonalDetailsForm.fill(t)
      case _ => theirPersonalDetailsForm
    }

    Ok(views.html.s4_careYouProvide.g1_theirPersonalDetails(theirPersonalDetailsQGForm))
  }

  def theirPersonalDetailsSubmit = claiming { implicit claim => implicit request =>
    theirPersonalDetailsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g1_theirPersonalDetails(formWithErrors)),
      theirPersonalDetails => claim.update(theirPersonalDetails) -> Redirect(routes.CareYouProvide.theirContactDetails()))
  }

  def theirContactDetails = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails.id) match {
      case Some(t: TheirPersonalDetails) => t.liveAtSameAddress == yes
      case _ => false
    }

    val theirContactDetailsPrePopulatedForm = if (liveAtSameAddress) {
      claim.questionGroup(ContactDetails.id) match {
        case Some(cd: ContactDetails) => theirContactDetailsForm.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
        case _ => theirContactDetailsForm
      }
    } else {
      claim.questionGroup(TheirContactDetails.id) match {
        case Some(t: TheirContactDetails) => theirContactDetailsForm.fill(t)
        case _ => theirContactDetailsForm
      }
    }

    Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm, completedQuestionGroups))
  }

  def theirContactDetailsSubmit = claiming { implicit claim => implicit request =>
    theirContactDetailsForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g2_theirContactDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.CareYouProvide.moreAboutThePerson()))
  }

  def moreAboutThePerson = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    val currentForm: Form[MoreAboutThePerson] = claim.questionGroup(MoreAboutThePerson.id) match {
      case Some(t: MoreAboutThePerson) => moreAboutThePersonForm.fill(t)
      case _ => moreAboutThePersonForm
    }

    Ok(views.html.s4_careYouProvide.g3_moreAboutThePerson(currentForm, completedQuestionGroups))
  }

  def moreAboutThePersonSubmit = claiming { implicit claim => implicit request =>
    moreAboutThePersonForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g3_moreAboutThePerson(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
      moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(routes.CareYouProvide.previousCarerPersonalDetails))
  }

  def previousCarerPersonalDetails = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson.id) match {
      case Some(t: MoreAboutThePerson) => if (t.claimedAllowanceBefore == Mappings.yes) true else false
      case _ => false
    }

    if (claimedAllowanceBefore) {
      val currentForm = claim.questionGroup(PreviousCarerPersonalDetails.id) match {
        case Some(h: PreviousCarerPersonalDetails) => previousCarerPersonalDetailsForm.fill(h)
        case _ => previousCarerPersonalDetailsForm
      }

      Ok(views.html.s4_careYouProvide.g4_previousCarerPersonalDetails(currentForm, completedQuestionGroups))
    } else Redirect(routes.CareYouProvide.representativesForPerson)
  }
  
  def previousCarerPersonalDetailsSubmit = claiming {
    implicit claim =>
      implicit request =>
        previousCarerPersonalDetailsForm.bindEncrypted.fold(
          formWithErrors => BadRequest(views.html.s4_careYouProvide.g4_previousCarerPersonalDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
          currentForm => claim.update(currentForm) -> Redirect(routes.CareYouProvide.previousCarerContactDetails))
  }

  def previousCarerContactDetails = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)
    
    val currentForm: Form[PreviousCarerContactDetails] = claim.questionGroup(PreviousCarerContactDetails.id) match {
      case Some(t: PreviousCarerContactDetails) => previousCarerContactDetailsForm.fill(t)
      case _ => previousCarerContactDetailsForm
    }
    
    Ok(views.html.s4_careYouProvide.g5_previousCarerContactDetails(currentForm, completedQuestionGroups))
  }

  def previousCarerContactDetailsSubmit = claiming { implicit claim => implicit request =>
    previousCarerContactDetailsForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g5_previousCarerContactDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
      previousCarerContactDetails => claim.update(previousCarerContactDetails) -> Redirect(routes.CareYouProvide.representativesForPerson))
  }
  
  def representativesForPerson = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != RepresentativesForPerson.id)

    val currentForm = claim.questionGroup(RepresentativesForPerson.id) match {
      case Some(h: RepresentativesForPerson) => representativesForPersonForm.fill(h)
      case _ => representativesForPersonForm
    }

    Ok(views.html.s4_careYouProvide.g6_representativesForThePerson(currentForm, completedQuestionGroups))
  }

  def representativesForPersonSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != RepresentativesForPerson.id)

    def actAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
      if (rfp.actForPerson == "yes" && rfp.actAs == None) form.fill(rfp).withError("actAs", "error.required")
      else form
    }

    def someoneElseActAs(form: Form[RepresentativesForPerson])(implicit rfp: RepresentativesForPerson): Form[RepresentativesForPerson] = {
      if (rfp.someoneElseActForPerson == "yes" && rfp.someoneElseActAs == None) form.fill(rfp).withError("someoneElseActAs", "error.required")
      else form
    }

    representativesForPersonForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g6_representativesForThePerson(formWithErrors, completedQuestionGroups)),
      implicit representativesForPerson => {
        val formValidations = actAs _ andThen someoneElseActAs _
        val timeOutsideUKFormValidated = formValidations(representativesForPersonForm)

        if (timeOutsideUKFormValidated.hasErrors) BadRequest(views.html.s4_careYouProvide.g6_representativesForThePerson(timeOutsideUKFormValidated, completedQuestionGroups))
        else claim.update(representativesForPerson) -> Redirect(routes.CareYouProvide.moreAboutTheCare())
      })
  }

  def moreAboutTheCare = claiming {implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != MoreAboutTheCare.id)

    val currentForm = claim.questionGroup(MoreAboutTheCare.id) match {
      case Some(h: MoreAboutTheCare) => moreAboutTheCareForm.fill(h)
      case _ => moreAboutTheCareForm
    }

    Ok(views.html.s4_careYouProvide.g7_moreAboutTheCare(currentForm, completedQuestionGroups))
  }

  def moreAboutTheCareSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != MoreAboutTheCare.id)

    def actAs(form: Form[MoreAboutTheCare])(implicit matc: MoreAboutTheCare): Form[MoreAboutTheCare] = {
      if (matc.spent35HoursCaringBeforeClaim == "yes" && matc.careStartDate == None) form.fill(matc).withError("careStartDate", "error.required")
      else form
    }

    moreAboutTheCareForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g7_moreAboutTheCare(formWithErrors, completedQuestionGroups)),
      implicit moreAboutTheCare => {
        val formValidations: (Form[MoreAboutTheCare]) => Form[MoreAboutTheCare] = actAs
        val moreAboutTheCareFormValidated = formValidations(moreAboutTheCareForm)

        if (moreAboutTheCareFormValidated.hasErrors) BadRequest(views.html.s4_careYouProvide.g7_moreAboutTheCare(moreAboutTheCareFormValidated, completedQuestionGroups))
        else claim.update(moreAboutTheCare) -> Redirect(controllers.s4_care_you_provide.routes.G9ContactDetailsOfPayingPerson.present())
      })
  }

  def oneWhoPaysPersonalDetails = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    Ok(views.html.s4_careYouProvide.g8_oneWhoPaysPersonalDetails(oneWhoPaysPersonalDetailsFrom, completedQuestionGroups))
  }

  def oneWhoPaysPersonalDetailsSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

    Ok(views.html.s4_careYouProvide.g8_oneWhoPaysPersonalDetails(oneWhoPaysPersonalDetailsFrom, completedQuestionGroups))
  }

  def completed = claiming { implicit claim => implicit request =>
    val outcome =
      <html>
        <head>
          <title>Completed - Care You Provide</title>
        </head>
        <body>
          <h1>End of Sprint 2</h1>
          <ul>
            { claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>{ f }</li>) }
          </ul>
        </body>
      </html>

    Ok(outcome)
  }
}
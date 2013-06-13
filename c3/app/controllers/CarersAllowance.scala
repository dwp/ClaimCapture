package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.claim._
import models.claim.Hours
import models.claim.Benefits
import models.claim.LivesInGB
import models.claim.Over16

object CarersAllowance extends Controller with CachedClaim {

  val benefitsForm = Form(
    mapping(
      "answer" -> boolean
    )(Benefits.apply)(Benefits.unapply)
  )

  val hoursForm = Form(
    mapping(
      "answer" -> boolean
    )(Hours.apply)(Hours.unapply)
  )

  val livesInGBForm = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGB.apply)(LivesInGB.unapply)
  )

  val over16Form = Form(
    mapping(
      "answer" -> boolean
    )(Over16.apply)(Over16.unapply)
  )

  def goto(formID: String) = Action {
    formID match {
      case Benefits.id => Redirect(routes.CarersAllowance.benefits())
      case Hours.id => Redirect(routes.CarersAllowance.hours())
      case LivesInGB.id => Redirect(routes.CarersAllowance.livesInGB())
      case Over16.id => Redirect(routes.CarersAllowance.over16())
    }
  }

  def benefits = newClaim {
    implicit claim => implicit request =>
      if (claiming(Benefits.id, claim)) Ok(views.html.s1_carersallowance.g1_benefits(confirmed = true))
      else Ok(views.html.s1_carersallowance.g1_benefits(confirmed = false))
  }

  def benefitsSubmit = claiming {
    implicit claim => implicit request =>
      benefitsForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.benefits()),
        model => claim.update(model) -> Redirect(routes.CarersAllowance.hours()))
  }

  def hours = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.CarersAllowance.id)

      if (claiming(Hours.id, claim)) Ok(views.html.s1_carersallowance.g2_hours(confirmed = true, completedForms.takeWhile(_.id != Hours.id)))
      else Ok(views.html.s1_carersallowance.g2_hours(confirmed = false, completedForms.takeWhile(_.id != Hours.id)))
  }

  def hoursSubmit = claiming {
    implicit claim => implicit request =>
      hoursForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.hours()),
        model => claim.update(model) -> Redirect(routes.CarersAllowance.livesInGB()))
  }

  def livesInGB = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.CarersAllowance.id)

      if (claiming(LivesInGB.id, claim)) Ok(views.html.s1_carersallowance.g3_livesInGB(confirmed = true, completedForms.takeWhile(_.id != LivesInGB.id)))
      else Ok(views.html.s1_carersallowance.g3_livesInGB(confirmed = false, completedForms.takeWhile(_.id != LivesInGB.id)))
  }

  def livesInGBSubmit = claiming {
    implicit claim => implicit request =>
      livesInGBForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.livesInGB()),
        model => claim.update(model) -> Redirect(routes.CarersAllowance.over16()))
  }

  def over16 = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.CarersAllowance.id)

      if (claiming(Over16.id, claim)) Ok(views.html.s1_carersallowance.g4_over16(confirmed = true, completedForms.takeWhile(_.id != Over16.id)))
      else Ok(views.html.s1_carersallowance.g4_over16(confirmed = false, completedForms.takeWhile(_.id != Over16.id)))
  }

  def over16Submit = claiming {
    implicit claim => implicit request =>
      over16Form.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.over16()),
        model => claim.update(model) -> Redirect(routes.CarersAllowance.approve()))
  }

  def approve = claiming {
    implicit claim => implicit request =>
      val sectionId = models.claim.CarersAllowance.id
      val completedForms = claim.completedFormsForSection(sectionId)
      val approved = claim.completedFormsForSection(sectionId).forall(_.asInstanceOf[BooleanConfirmation].answer) && completedForms.length == 4

      Ok(views.html.s1_carersallowance.g5_approve(approved, completedForms))
  }

  def approveSubmit = Action {
    Redirect(routes.AboutYou.yourDetails())
  }

  private def claiming(formID: String, claim: Claim) = claim.form(formID) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }
}
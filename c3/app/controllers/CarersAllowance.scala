package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.view._
import models.domain
import models.domain._
import scala.Some
import scala.collection.immutable.ListMap

object CarersAllowance extends Controller with CachedClaim {
  val route: ListMap[String, Call] = ListMap(Benefits.id -> routes.CarersAllowance.benefits,
                                             Hours.id -> routes.CarersAllowance.hours,
                                             Over16.id -> routes.CarersAllowance.over16,
                                             LivesInGB.id -> routes.CarersAllowance.livesInGB)

  val benefitsForm = Form(
    mapping(
      "answer" -> boolean
    )(Benefits.apply)(Benefits.unapply))

  val hoursForm = Form(
    mapping(
      "answer" -> boolean
    )(Hours.apply)(Hours.unapply))

  val over16Form = Form(
    mapping(
      "answer" -> boolean
    )(Over16.apply)(Over16.unapply))

  val livesInGBForm = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGB.apply)(LivesInGB.unapply))

  def benefits = newClaim { implicit claim => implicit request =>
    if (claiming(Benefits.id, claim)) Ok(views.html.s1_carersallowance.g1_benefits(confirmed = true))
    else Ok(views.html.s1_carersallowance.g1_benefits(confirmed = false))
  }

  def benefitsSubmit = claiming { implicit claim => implicit request =>
    benefitsForm.bindFromRequest.fold(
      formWithErrors => Redirect(routes.CarersAllowance.benefits()),
      benefits => claim.update(benefits) -> Redirect(routes.CarersAllowance.hours()))
  }

  def hours = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(domain.CarersAllowance.id)

    if (claiming(Hours.id, claim)) Ok(views.html.s1_carersallowance.g2_hours(confirmed = true, completedQuestionGroups.takeWhile(_.id != Hours.id)))
    else Ok(views.html.s1_carersallowance.g2_hours(confirmed = false, completedQuestionGroups.takeWhile(_.id != Hours.id)))
  }

  def hoursSubmit = claiming { implicit claim => implicit request =>
    hoursForm.bindFromRequest.fold(
      formWithErrors => Redirect(routes.CarersAllowance.hours()),
      hours => claim.update(hours) -> Redirect(routes.CarersAllowance.over16()))
  }

  def over16 = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(domain.CarersAllowance.id)

    if (claiming(Over16.id, claim)) Ok(views.html.s1_carersallowance.g3_over16(confirmed = true, completedQuestionGroups.takeWhile(_.id != Over16.id)))
    else Ok(views.html.s1_carersallowance.g3_over16(confirmed = false, completedQuestionGroups.takeWhile(_.id != Over16.id)))
  }

  def over16Submit = claiming { implicit claim => implicit request =>
    over16Form.bindFromRequest.fold(
      formWithErrors => Redirect(routes.CarersAllowance.over16()),
      over16 => claim.update(over16) -> Redirect(routes.CarersAllowance.livesInGB()))
  }

  def livesInGB = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(domain.CarersAllowance.id)

    if (claiming(LivesInGB.id, claim)) Ok(views.html.s1_carersallowance.g4_livesInGB(confirmed = true, completedQuestionGroups.takeWhile(_.id != LivesInGB.id)))
    else Ok(views.html.s1_carersallowance.g4_livesInGB(confirmed = false, completedQuestionGroups.takeWhile(_.id != LivesInGB.id)))
  }

  def livesInGBSubmit = claiming { implicit claim => implicit request =>
    livesInGBForm.bindFromRequest.fold(
      formWithErrors => Redirect(routes.CarersAllowance.livesInGB()),
      livesInGB => claim.update(livesInGB) -> Redirect(routes.CarersAllowance.approve()))
  }

  def approve = claiming { implicit claim => implicit request =>
    val sectionId = domain.CarersAllowance.id
    val completedQuestionGroups = claim.completedQuestionGroups(sectionId)
    val approved = claim.completedQuestionGroups(sectionId).forall(_.asInstanceOf[BooleanConfirmation].answer) && completedQuestionGroups.length == 4

    Ok(views.html.s1_carersallowance.g5_approve(approved, completedQuestionGroups))
  }

  def approveSubmit = Action {
    Redirect(routes.AboutYou.yourDetails())
  }

  private def claiming(formID: String, claim: Claim) = claim.questionGroup(formID) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }
}
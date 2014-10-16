package models.view

import play.api.Logger
import play.api.mvc.{Request, Result, AnyContent}
import models.domain.{PreviewModel, Claim}
import scala.reflect.ClassTag
import models.view.CachedClaim.ClaimResult

trait Navigable {
  this: CachedClaim =>

  def resetPreviewState(f: => Claim => Result)(implicit claim: Claim):ClaimResult = {

    val updatedNavigation = claim.navigation.resetPreviewState()
    val updatedClaim = claim.copy(claim.key, claim.sections)(updatedNavigation)
    updatedClaim -> f(updatedClaim)
  }

  def track[T](t: T,beenInPreview:Boolean=false)(f: => Claim => Result)(implicit claim: Claim, request: Request[AnyContent], classTag: ClassTag[T]): ClaimResult = {

    val updatedNavigation = claim.navigation.track(t,beenInPreview )(request.uri)
    val updatedClaim = claim.copy(claim.key, claim.sections)(updatedNavigation)

    updatedClaim -> f(updatedClaim)
  }

  def trackBackToBeginningOfEmploymentSection[T](t: T)(f: => Claim => Result)(implicit claim: Claim, request: Request[AnyContent], classTag: ClassTag[T]): ClaimResult = {
    // Similar to track but this removes all routes navigation entries up to /employment/employment.
    // Done so that when at the jobs summary page of employment, back can take you back to the initial guard questions page
    val updatedNavigationSec = claim.navigation.copy(claim.navigation.routes.takeWhile(r => r.uri == "/employment/employment" || !r.uri.contains("employment") )).track(t)(request.uri)
    val updatedClaim = claim.copy(claim.key, claim.sections)(updatedNavigationSec)

    updatedClaim -> f(updatedClaim)
  }
}

case class Navigation(routes: List[Route[_]] = List(), beenInPreview:Boolean = false) {


  def resetPreviewState():Navigation = copy(beenInPreview = false)
                                                                                                                                                                       //As all controllers are calling false, once we set it to true, we want to remain like that till we call resetPreviewState
  def track[T](t: T,beenInPreviewParam:Boolean = false)(route: String)(implicit classTag: ClassTag[T]): Navigation = copy(routes.takeWhile(_.uri != route) :+ Route[T](route), if(!beenInPreview)beenInPreviewParam else beenInPreview)

  def trackBackToBeginningOfSection[T](t: T)(route: String)(implicit classTag: ClassTag[T]): Navigation = copy(routes.takeWhile(_.uri != route) :+ Route[T](route))

  def current: Route[_] = if (routes.isEmpty) Route("") else routes.last

  def previousIgnorePreview: Route[_] = {
    if (routes.size > 1) routes.dropRight(1).last
    else if (routes.size == 1) current
    else Route("")
  }

  def previous: Route[_] = {
    Logger.error(this.toString)
    if (beenInPreview) Route(controllers.preview.routes.Preview.present.url)
    else previousIgnorePreview

  }

  def apply[T](t: T)(implicit classTag: ClassTag[T]): Option[Route[_]] = routes.find(_.classTag.runtimeClass == t.getClass)
}

case class Route[T](uri: String)(implicit val classTag: ClassTag[T]) {
  override def toString = uri
}

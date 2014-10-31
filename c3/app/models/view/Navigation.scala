package models.view

import play.api.mvc.{Request, Result, AnyContent}
import models.domain.Claim
import scala.reflect.ClassTag
import models.view.CachedClaim.ClaimResult

trait Navigable {
  this: CachedClaim =>

  def track[T](t: T)(f: => Claim => Result)( implicit claim: Claim, request: Request[AnyContent], classTag: ClassTag[T]): ClaimResult = {
    val updatedNavigation = claim.navigation.track(t)(request.uri)
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

case class Navigation(routes: List[Route[_]] = List()) {
  def track[T](t: T)(route: String)(implicit classTag: ClassTag[T]): Navigation = copy(routes.takeWhile(_.uri != route) :+ Route[T](route))

  def trackBackToBeginningOfSection[T](t: T)(route: String)(implicit classTag: ClassTag[T]): Navigation = copy(routes.takeWhile(_.uri != route) :+ Route[T](route))

  def current: Route[_] = if (routes.isEmpty) Route("") else routes.last

  def previous: Route[_] = if (routes.size > 1) routes.dropRight(1).last else if (routes.size == 1) current else Route("")

  def apply[T](t: T)(implicit classTag: ClassTag[T]): Option[Route[_]] = routes.find(_.classTag.runtimeClass == t.getClass)
}

case class Route[T](uri: String)(implicit val classTag: ClassTag[T]) {
  override def toString = uri
}

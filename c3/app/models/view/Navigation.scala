package models.view

import play.api.mvc.{Request, Result, AnyContent}
import models.domain.Claim
import scala.reflect.ClassTag
import models.view.ClaimHandling.ClaimResult

trait Navigable {
  this: ClaimHandling =>

  def resetPreviewState(f: => Claim => Result)(implicit claim: Claim): ClaimResult = {

    val updatedNavigation = claim.navigation.resetPreviewState()
    val updatedClaim = claim.copy(claim.key, claim.sections)(updatedNavigation)
    updatedClaim -> f(updatedClaim)
  }

  def track[T](t: T, beenInPreview: Boolean = false)(f: => Claim => Result)(implicit claim: Claim, request: Request[AnyContent], classTag: ClassTag[T]): ClaimResult = {

    val updatedNavigation = claim.navigation.track(t, beenInPreview)(request.uri)
    val checkYAnswers = if (beenInPreview) claim.checkYAnswers.copy(previouslySavedClaim = Some(claim)) else claim.checkYAnswers
    val updatedClaim = claim.copy(claim.key, claim.sections, checkYAnswers = checkYAnswers)(updatedNavigation)

    updatedClaim -> f(updatedClaim)
  }

  def trackBackToBeginningOfEmploymentSection[T](t: T)(f: => Claim => Result)(implicit claim: Claim, request: Request[AnyContent], classTag: ClassTag[T]): ClaimResult = {
    val updatedNavigationSec = if (claim.navigation.beenInPreview) {
      claim.navigation.copy(routesAfterPreview = claim.navigation.routesAfterPreview.takeWhile(r => r.uri == "/employment/employment" || !r.uri.contains("employment"))).track(t)(request.uri)
    } else {
      claim.navigation.copy(routes = claim.navigation.routes.takeWhile(r => r.uri == "/employment/employment" || !r.uri.contains("employment"))).track(t)(request.uri)
    }
    // Similar to track but this removes all routes navigation entries up to /employment/employment.
    // Done so that when at the jobs summary page of employment, back can take you back to the initial guard questions page
    val updatedClaim = claim.copy(claim.key, claim.sections)(updatedNavigationSec)

    updatedClaim -> f(updatedClaim)
  }
}

case class Navigation(routes: List[Route[_]] = List(), beenInPreview: Boolean = false, routesAfterPreview: List[Route[_]] = List.empty[Route[_]], showSaveButton: Boolean = true) {


  def resetPreviewState(): Navigation = copy(beenInPreview = false)

  def track[T](t: T, beenInPreviewParam: Boolean = false)(route: String)(implicit classTag: ClassTag[T]): Navigation = {
    val routeObj = Route[T](route)

    //Tracking after CYA is a special case, and since it's takeWhile(_.uri != route), using the normal tracking will delete any later routes.
    //So we want to use an alternative tracking in case we have been in preview, to still be able to use the back button link
    if (beenInPreviewParam) {
      copy(routes.takeWhile(_.uri != controllers.preview.routes.Preview.present().url) :+ routeObj, beenInPreviewParam, routesAfterPreview = List.empty[Route[_]], showSaveButton = showSaveButton(route))
    } else if (beenInPreview) {
      copy(routesAfterPreview = routesAfterPreview.takeWhile(_.uri != route) :+ Route[T](route), showSaveButton = showSaveButton(route))
    } else {
      copy(routes.takeWhile(_.uri != route) :+ Route[T](route), showSaveButton = showSaveButton(route))
    }
  }

  def showSaveButton(route: String) = {
    // a match case statement would be the scala way here but getting error "stable identifier required during pattern matching". ColinG 03/12/2015.
    if (route == controllers.s_eligibility.routes.GBenefits.present.url) false
    else if (route == controllers.s_eligibility.routes.GEligibility.present.url) false
    else if (route == controllers.s_eligibility.routes.CarersAllowance.approve.url) false
    else if (route == controllers.s_disclaimer.routes.GDisclaimer.present.url) false
    else if (route == controllers.s_claim_date.routes.GClaimDate.present.url) false
    else if (route == controllers.s_about_you.routes.GYourDetails.present.url) false
    else if (route == controllers.s_about_you.routes.GMaritalStatus.present.url) false
    else if (route == controllers.s_about_you.routes.GContactDetails.present.url) false
    else true
  }

  def current: Route[_] = if (routes.isEmpty) Route("") else routes.last

  def previous: Route[_] = {
    val routesList = if (beenInPreview) routesAfterPreview else routes
    if (routesList.size > 1) routesList.dropRight(1).last
    else if (routesList.size == 1) current
    else Route("")
  }

  def apply[T](t: T)(implicit classTag: ClassTag[T]): Option[Route[_]] = routes.find(_.classTag.runtimeClass == t.getClass)
}

case class Route[T](uri: String)(implicit val classTag: ClassTag[T]) {
  override def toString = uri
}

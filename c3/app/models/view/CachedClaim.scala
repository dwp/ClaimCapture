package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.{ReplicaData, Replica, routes}
import models.domain.{YourDetails, ClaimDate, Claim}
import models.view.ClaimHandling.ClaimResult
import play.api.mvc._
import play.api.i18n.Lang

import scala.language.implicitConversions

object CachedClaim {
  val key = "claim"
}

/**
 * The Unique ID use to identified a claim/circs in the cache is a UUID, stored in the claim/circs and set in the request session (cookie).
 */
trait CachedClaim extends ClaimHandling {

  type IterationID = String

  override lazy val cacheKey = CachedClaim.key

  override lazy val startPage: String = getProperty("claim.start.page", "/allowance/benefits")
  override lazy val timeoutPage = routes.ClaimEnding.timeout()
  override lazy val errorPageCookie = routes.ClaimEnding.errorCookie()
  override lazy val errorPage = routes.ClaimEnding.error()
  override lazy val errorPageBrowserBackButton = routes.ClaimEnding.errorBrowserBackbutton()
  override lazy val backButtonPage = controllers.routes.Application.backButtonPage()

  override protected def newInstance(newuuid: String = randomUUID.toString): Claim = {
    getProperty("replica.prepopulatedData",false) match {
      case true => ReplicaData.newInstance(cacheKey,newuuid)
      case _ => new Claim(cacheKey, uuid = newuuid)
    }
  }

  override def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid, claim.transactionId, claim.checkYAnswers, claim.saveForLaterCurrentPageData)(claim.navigation)

  override protected def claimNotValid(claim: Claim): Boolean = {
    (claim.questionGroup[ClaimDate] match {
      case Some(null) => true
      case None => true
      case _ => false

    }) ||
      (claim.questionGroup[YourDetails] match {
        case None => true
        case Some(YourDetails(_, _, firstName, _, surname, nino, _)) if firstName.isEmpty || surname.isEmpty || nino.nino.isEmpty => true
        case _ => false
      })
  }

  def claimingWithCheckInIteration(f: (IterationID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action.async {
    request =>
      claimingWithCheck(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).getOrElse("iterationID", Seq("Missing IterationID at request"))(0)))(request)
  }

}

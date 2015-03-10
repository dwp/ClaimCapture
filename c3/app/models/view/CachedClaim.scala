package models.view

import java.util.UUID._

import app.ConfigProperties._
import controllers.routes
import models.domain.{Claim, _}
import models.view.ClaimHandling.ClaimResult
import play.api.i18n.Lang
import play.api.mvc._

import scala.language.implicitConversions

object CachedClaim {
  val missingRefererConfig = "Referer not set in config"
  val key = "claim"
  // Versioning
  val C3VERSION = "C3Version"
  val C3VERSION_VALUE = "2.13"
}

/**
 * The Unique ID use to identified a claim/circs in the cache is a UUID, stored in the claim/circs and set in the request session (cookie).
 */
trait CachedClaim extends ClaimHandling {

  type IterationID = String

  lazy val cacheKey = CachedClaim.key

  // Common pages
  lazy val startPage: String = getProperty("claim.start.page", "/allowance/benefits")
  lazy val timeoutPage = routes.ClaimEnding.timeout()
  lazy val errorPageCookie = routes.ClaimEnding.errorCookie()
  lazy val errorPage = routes.ClaimEnding.error()
  lazy val errorPageBrowserBackButton = routes.ClaimEnding.errorBrowserBackbutton()

  protected def newInstance(newuuid: String = randomUUID.toString): Claim = new Claim(cacheKey, uuid = newuuid) with FullClaim

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid, claim.transactionId, claim.previouslySavedClaim)(claim.navigation) with FullClaim

  override protected def fieldsCheck(claim: Claim): Boolean = {
    (claim.questionGroup[ClaimDate] match {
      case Some(null) => true
      case None => true
      case _ => false

    }) ||
      (claim.questionGroup[YourDetails] match {
        case None => true
        case Some(YourDetails(_, firstName, _, surname, nino, _)) if firstName.isEmpty || surname.isEmpty || nino.nino.isEmpty => true
        case _ => false
      })
  }


  def claimingWithCheckInIteration(f: (IterationID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action.async {
    request =>
      claimingWithCheck(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).getOrElse("iterationID", Seq("Missing IterationID at request"))(0)))(request)
  }


}
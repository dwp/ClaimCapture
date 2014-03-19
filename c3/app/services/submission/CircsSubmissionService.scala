package services.submission

import play.api.mvc.{AnyContent, Request}
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger
import models.view.CachedChangeOfCircs
import models.domain.{ReportChanges, CircumstancesDeclaration, Claim}
import play.api.mvc.SimpleResult
import services.ClaimTransactionComponent
import ExecutionContext.Implicits.global
import app.ReportChange._

trait CircsSubmissionService extends ClaimSubmissionService{

  this : ClaimTransactionComponent with WebServiceClientComponent =>

  private val changesMap = Map(StoppedCaring.name -> 0,AddressChange.name -> 1, SelfEmployment.name -> 2, PaymentChange.name -> 3, AdditionalInfo.name -> 4)

  override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = {
    val txnID = claimTransaction.generateId
    val declaration = claim.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())
    val thirdParty = declaration.circsSomeOneElse.isDefined
    val circsChange = changesMap(claim.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges)

    Logger.info(s"Retrieved Id : $txnID")

    webServiceClient.submitClaim(claim, txnID).map(
      response => {
        registerId(claim, txnID, SUBMITTED, thirdParty, Some(circsChange))
        processResponse(claim, txnID, response, request)
      }
    )
  }


}

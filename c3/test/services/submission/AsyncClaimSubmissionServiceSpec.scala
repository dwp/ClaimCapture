package services.submission

import java.util.UUID._

import models.domain.{Claim, _}
import models.view.CachedClaim
import models.{DayMonthYear, NationalInsuranceNumber}
import org.specs2.mock.Mockito
import org.specs2.mutable.{Specification, Tags}
import play.api.http
import play.api.libs.ws.WSResponse
import play.api.test.FakeApplication
import services.{TransactionStatus, _}

import scala.concurrent.Future


class AsyncClaimSubmissionServiceSpec extends Specification with Mockito with Tags with CachedClaim {

  val ni = "AB123456D"

  def resultXml(result: String, correlationID: String, messageClass:String, errorCode: String, pollEndpoint: String) = {
    <response>
      <result>{result}</result>
      <correlationID>{correlationID}</correlationID>
      <messageClass>{messageClass}</messageClass>
      <pollEndpoint>{pollEndpoint}</pollEndpoint>
      <errorCode>{errorCode}</errorCode>
    </response>
  }

  def asyncService( status:Int=200, transactionId:String, result: String= "",
                    correlationID: String="", messageClass:String="",
                    errorCode: String="", pollEndpoint: String ="") = new AsyncClaimSubmissionService with ClaimTransactionComponent
                                                                                                      with WebServiceClientComponent {
      import scala.concurrent.ExecutionContext.Implicits.global
      val webServiceClient = mock[WebServiceClient]
      val response = mock[WSResponse]
      response.status returns status
      response.body returns resultXml(result,correlationID,messageClass,errorCode,pollEndpoint).buildString(stripComments = false)

      webServiceClient.submitClaim(any[Claim],any[String]) returns Future(response)
      val claimTransaction = new ClaimTransaction
  }

  def getClaim(surname: String): Claim = {
    val claim = new Claim(transactionId = Some(transactionId), uuid=randomUUID.toString)

    // need to set the qs groups used to create the fingerprint of the claim, otherwise a dup cache error will be thrown
    val det = new YourDetails("", "",None, surname,NationalInsuranceNumber(Some(ni)), DayMonthYear(Some(1), Some(1), Some(1969)))

    val claimDate = new ClaimDate(DayMonthYear(Some(1), Some(1), Some(2014)))

    claim + det + claimDate match {
      case c:Claim => new Claim(c.key, c.sections, c.created, c.lang, c.uuid, c.transactionId)(c.navigation) with FullClaim
    }

  }

  def getCofc(fullname: String): Claim = {
    val claim = new Claim("change-of-circs",transactionId = Some(transactionId), uuid=randomUUID.toString)

    // need to set the qs groups used to create the fingerprint of the claim, otherwise a dup cache error will be thrown
    val det = new CircumstancesReportChange(true, fullname, NationalInsuranceNumber(Some(ni)), DayMonthYear(Some(1), Some(1), Some(1967)), "", "")

    val claimDate = new ClaimDate(DayMonthYear(Some(1), Some(1), Some(2014)))

    claim + det + claimDate match {
      case c:Claim => new Claim(c.key, c.sections, c.created, c.lang, c.uuid, c.transactionId)(c.navigation) with FullClaim
    }
  }

  val transactionId = "1234567"

  "claim submission" should {
    "record BAD_REQUEST" in new WithApplicationAndDB {

      val service = asyncService(http.Status.BAD_REQUEST,transactionId)

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.BAD_REQUEST_ERROR,1,Some(0),None,Some("en")))

    }

    "record SUCCESS" in new WithApplicationAndDB {

      val service = asyncService(http.Status.OK,transactionId,result = "response")

      serviceSubmission(service, getClaim("test"))

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SUCCESS,1,Some(0),None,Some("en")))

    }

    "record change of circs submission SUCCESS" in new WithApplicationAndDB {
      val service = asyncService(http.Status.OK,transactionId,result = "response")

      serviceSubmission(service, getCofc("test"))

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SUCCESS,1,Some(0),None,Some("en")))

    }

    "do not submit a duplicate claim" in new WithApplicationAndDB {
      val service = asyncService(http.Status.OK,transactionId)
      val claim = getClaim("test")

      serviceSubmission(service, claim)
      Thread.sleep(100)
      service.submission(claim) must throwA(DuplicateClaimException(s"Duplicate claim submission. transactionId [${claim.transactionId.get}]"))

      Thread.sleep(1500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.INTERNAL_ERROR,1,Some(0),None,Some("en")))

    }

    "do not submit a duplicate change of circs" in new WithApplicationAndDB {

      val service = asyncService(http.Status.OK,transactionId)
      val claim = getCofc("test")

      serviceSubmission(service, claim)
      Thread.sleep(100)
      service.submission(claim) must throwA(DuplicateClaimException(s"Duplicate claim submission. transactionId [${claim.transactionId.get}]"))

      Thread.sleep(1500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.INTERNAL_ERROR,1,Some(0),None,Some("en")))

    }

    "record SERVICE_UNAVAILABLE" in new WithApplicationAndDB {

      val service = asyncService(http.Status.SERVICE_UNAVAILABLE,transactionId)

      val claim = getClaim("test1")

      serviceSubmission(service, claim)

      Thread.sleep(1000)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SERVICE_UNAVAILABLE,1,Some(0),None,Some("en")))

    }

    "record REQUEST_TIMEOUT_ERROR" in new WithApplicationAndDB {

      val service = asyncService(http.Status.REQUEST_TIMEOUT,transactionId)

      val claim = getClaim("test2")

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.REQUEST_TIMEOUT_ERROR,1,Some(0),None,Some("en")))

    }

    "record SERVER_ERROR" in new WithApplicationAndDB {

      val service = asyncService(http.Status.INTERNAL_SERVER_ERROR,transactionId)

      val claim = getClaim("test3")

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SERVER_ERROR,1,Some(0),None,Some("en")))

    }
  } section "unit"


  def serviceSubmission(service: AsyncClaimSubmissionService with ClaimTransactionComponent, claim: Claim)(implicit app: FakeApplication) {
    DBTests.createId(transactionId)
    service.claimTransaction.registerId(transactionId, ClaimSubmissionService.SUBMITTED, controllers.submission.claimType(claim), 1)
    println("calling service.submission")
    service.submission(claim)
  }
}

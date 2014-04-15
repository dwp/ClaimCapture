import app.ConfigProperties._
import controllers.circs.s3_consent_and_declaration.{G1Declaration, G1SyncDeclaration, G1AsyncDeclaration}
import controllers.circs.submission.ChangeOfCircsSubmissionController
import controllers.s11_consent_and_declaration.{G5Submit, G5SyncSubmit, G5AsyncSubmit}
import controllers.submission.{XmlSubmitter, ClaimSubmissionController}
import models.domain.Claim
import play.api.{Play, Logger}
import play.api.mvc.{SimpleResult, AnyContent, Request}
import scala.concurrent.Future
import scala.reflect._
import scala.language.existentials

trait Injector {

  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private lazy val instances: Map[Class[_ <: Any], Any] = {
    import Play.current

    def controller[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance

    def xmlPrintControllers: Map[Class[_], Any] = {
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        }),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        })
      )
    }

    def stubDBControllers: Map[Class[_], Any] = {
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController {
          override val claimTransaction = new StubClaimTransaction
        }),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController {
          override val claimTransaction = new StubClaimTransaction
        })
      )
    }

    val controllers = if (!Play.isTest && getProperty("async.submission",false)){
      Logger.info("async controllers being used")
      // ASYNC SUBMIT POINT HERE
      Map(controller[G5Submit](new G5AsyncSubmit), controller[G1Declaration](new G1AsyncDeclaration))
    }else{
      Logger.info("sync controllers being used")
      // SYNC SUBMIT POINT
      Map(controller[G5Submit](new G5SyncSubmit), controller[G1Declaration](new G1SyncDeclaration))
    }

    controllers ++ (if (getProperty("submit.prints.xml", default = false)) {
      Logger.warn("submit.prints.xml = true")
      xmlPrintControllers
    } else if (getProperty("stub.db", default = false)) {
        Logger.warn("stub.db = true")
        stubDBControllers
    }
    else {
      Map(controller[ClaimSubmissionController](new ClaimSubmissionController),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController))
    })
  }
}

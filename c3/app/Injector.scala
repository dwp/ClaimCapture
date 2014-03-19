import app.ConfigProperties._
import controllers.circs.submission.ChangeOfCircsSubmissionController
import controllers.submission.{XmlSubmitter, ClaimSubmissionController}
import models.domain.Claim
import play.api.Logger
import play.api.mvc.{SimpleResult, AnyContent, Request}
import scala.concurrent.Future
import scala.reflect._
import scala.language.existentials

trait Injector {

  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private lazy val instances: Map[Class[_ <: Any], Any] = {

    def controller[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance

    if (getProperty("submit.prints.xml", default = false)) {
      Logger.warn("submit.prints.xml = true")
    Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        }),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        })
      )
    } else if (getProperty("stub.db", default = false)) {
      Logger.warn("stub.db = true")
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController { override val claimTransaction = new StubClaimTransaction }),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController { override val claimTransaction = new StubClaimTransaction })
      )
    }
    else
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController)
      )
  }
}

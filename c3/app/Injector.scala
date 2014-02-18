import controllers.circs.submission.ChangeOfCircsSubmissionController
import controllers.submission.{XmlSubmitter, ClaimSubmissionController}
import models.domain.Claim
import play.api.mvc.{SimpleResult, AnyContent, Request}
import play.api.Play
import scala.concurrent.Future
import scala.reflect._
import scala.language.existentials
import play.api.Play.current

trait Injector {

  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private lazy val instances: Map[Class[_ <: Any], Any] = {

    def controller[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance

    if (Play.isProd)
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController)
      )
    else
      Map(
        controller[ClaimSubmissionController](new ClaimSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        }),
        controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController {
          override def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = XmlSubmitter.submission(claim, request)
        })
      )
  }
}

import com.google.inject.Guice
import controllers.submission.{WebServiceSubmitter, XmlSubmitter, Submitter}
import modules.ProdModule
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeExample
import play.api.GlobalSettings
import play.api.test.WithApplication

class GlobalSpec extends Specification with BeforeExample {
  "Application" should {
    "use 'production' module in 'prod' mode <- This is actual a badly implemented example as we use hard coding" in new WithApplication {
      ProdGlobal.injector.getInstance(classOf[Submitter]) should beAnInstanceOf[WebServiceSubmitter]
    }

    "use 'default' module when not in 'prod' mode" in new WithApplication {
      Global.injector.getInstance(classOf[Submitter]) should beAnInstanceOf[XmlSubmitter]
    }
  }

  protected def before: Any = System.setProperty("application.global", "ProdGlobal")
}

object ProdGlobal extends GlobalSettings {
  val injector = Guice.createInjector(ProdModule)
}
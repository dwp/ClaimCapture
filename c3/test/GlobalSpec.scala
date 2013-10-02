import com.google.inject.Guice
import controllers.submission.{WebServiceSubmitter, XmlSubmitter, Submitter}
import modules.ProdModule
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeExample
import play.api.GlobalSettings
import play.api.mvc.EssentialAction
import play.api.test.WithApplication

class GlobalSpec extends Specification with BeforeExample with Mockito {
  "Application" should {
    "use 'production' module in 'prod' mode <- This is actual a badly implemented example as we use hard coding" in new WithApplication {
      ProdGlobal.injector.getInstance(classOf[Submitter]) should beAnInstanceOf[WebServiceSubmitter]
    }

    "use 'default' module when not in 'prod' mode" in new WithApplication {
      Global.injector.getInstance(classOf[Submitter]) should beAnInstanceOf[XmlSubmitter]
    }
  }

  "Global" should {
    "apply filters 'referer', 'requests' " in {
      val actions = Global.doFilter(mock[EssentialAction])
    }
  }

  protected def before: Any = System.setProperty("application.global", "ProdGlobal")
}

object ProdGlobal extends GlobalSettings {
  val injector = Guice.createInjector(ProdModule)
}
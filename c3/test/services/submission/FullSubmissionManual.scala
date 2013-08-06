package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario}
import play.api.{Mode, Configuration, GlobalSettings}
import com.google.inject.Guice
import java.io.File
import com.typesafe.config.ConfigFactory
import controllers.submission.{XmlSubmitter, Submitter}
import com.tzavellas.sse.guice.ScalaModule
import services.TransactionIdService

class FullSubmissionManual extends Specification with Tags {
  private lazy val injector = Guice.createInjector(new ScalaModule {
    def configure() {
      bind[Submitter].to[XmlSubmitter]
      bind[ClaimSubmission].to[MockClaimSubmission]
      bind[TransactionIdService].to[MockTransactionIdService]
    }
  })

  val global = new GlobalSettings {
    override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
      val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
      val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
      super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
    }

    override def getControllerInstance[A](controllerClass: Class[A]): A = {
      injector.getInstance(controllerClass)
    }
  }

  "The application " should {
    "Successfully run submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
      println(lastPage.source())
    }
  }
}

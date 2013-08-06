package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario}
import play.api.{Mode, Configuration, GlobalSettings}
import com.google.inject.Guice
import play.api.Play.current
import java.io.File
import com.typesafe.config.ConfigFactory
import controllers.submission.{XmlSubmitter, WebServiceSubmitter, Submitter}
import services.{PostgresTransactionIdService, TransactionIdService}
import com.tzavellas.sse.guice.ScalaModule

class FullSubmissionManual extends Specification with Tags {

   val global = new GlobalSettings {
     private lazy val injector =  Guice.createInjector(new ScalaModule {
       def configure() {
         bind[Submitter].to[XmlSubmitter]
       }
     })

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

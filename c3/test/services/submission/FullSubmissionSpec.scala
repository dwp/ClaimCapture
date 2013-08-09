package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.ClaimScenario
import play.api.{Mode, Configuration, GlobalSettings}
import java.io.File
import com.typesafe.config.ConfigFactory
import controllers.submission.{WebServiceSubmitter, Submitter}
import com.tzavellas.sse.guice.ScalaModule
import services.TransactionIdService
import com.google.inject._
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.S11_consent_and_declaration.G5SubmitPage

class FullSubmissionSpec extends Specification with Tags {
  sequential

  val thankYouPageTitle = "GOV.UK - The best place to find government services and information"

  val cAndDError = "Error"

  private lazy val injector = Guice.createInjector(new ScalaModule {
    def configure() {
      bind[Submitter].to[WebServiceSubmitter]
      bind[ClaimSubmission].to[MockClaimSubmission].in[Singleton]
      bind[TransactionIdService].to[MockTransactionIdService].in[Singleton]
    }
  })

  val global = new GlobalSettings {
    override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
      val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
      val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
      super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
    }

    override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)
  }

  "The application" should {
    "Successfully run submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {

      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST223"
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, thankYouPageTitle, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Recoverable Error submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {

      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST224"
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Recoverable acknowledgement submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {

      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST225"
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Retry submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {

      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST225"
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      // first time through stores val in session
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
      val submissionPage = lastPage goToPage new G5SubmitPage(browser,Some(lastPage))
      val finalPage = submissionPage submitPage ()
    }
  } section "functional"
}
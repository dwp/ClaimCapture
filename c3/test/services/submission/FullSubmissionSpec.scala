package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.TestData
import play.api.{Mode, Configuration, GlobalSettings}
import java.io.File
import com.typesafe.config.ConfigFactory
import controllers.submission.{WebServiceSubmitter, Submitter}
import com.tzavellas.sse.guice.ScalaModule
import services.TransactionIdService
import com.google.inject._
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import submission._
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPageContext

class FullSubmissionSpec extends Specification with Tags {
  sequential

  val claimThankYouPageTitle = "Application complete"
  val changeThankYouPageTitle = "Change complete"

  val cAndDError = "An unrecoverable error has occurred"

  private lazy val injector = Guice.createInjector(new ScalaModule {
    def configure() {
      bind[Submitter].to[WebServiceSubmitter]
      bind[FormSubmission].to[MockFormSubmission].in[Singleton]
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

    "Successfully run claim submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST223"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, claimThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Successfully run circs submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1ReportAChangeInYourCircumstancesPageContext {
      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST223"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, changeThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = true)
    }

    "Recoverable Error submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST224"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Recoverable acknowledgement submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      val idService = injector.getInstance(classOf[TransactionIdService])
      idService.id = "TEST225"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, claimThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = false)
    }

  } section "functional"
}
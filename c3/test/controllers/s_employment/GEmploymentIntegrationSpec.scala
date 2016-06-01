package controllers.s_employment

import org.specs2.mutable._
import utils.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class GEmploymentIntegrationSpec extends Specification

trait EmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)

    Formulate.employment(browser)
  }
}

trait EducatedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)

    Formulate.nationalityAndResidency(browser)

    Formulate.paymentsFromAbroad(browser)

    Formulate.yourCourseDetails(browser)
  }
}

trait EducatedAndEmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)

    Formulate.nationalityAndResidency(browser)

    Formulate.paymentsFromAbroad(browser)

    Formulate.employment(browser)
  }
}

trait NotEmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)

    Formulate.notInEmployment(browser)
  }
}

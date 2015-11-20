package models.domain

import controllers.mappings.Mappings
import models.view.CachedClaim
import org.specs2.mutable._
import utils.WithApplication

class SelfEmploymentSpec extends Specification {

  def claimSelfEmployed = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List(Employment(Mappings.yes, Mappings.no)))
  ))
  def claimNotSelfEmployed = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List(Employment(Mappings.no, Mappings.no)))
  ))
  def claimWithNoEmploymentDetails = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List())
  ))

  "Self Employment" should {
    "tell whether the Claim object contains self employment" in new WithApplication {
      SelfEmployment.isSelfEmployed(claimSelfEmployed) mustEqual true
      SelfEmployment.isSelfEmployed(claimNotSelfEmployed) mustEqual false
      SelfEmployment.isSelfEmployed(claimWithNoEmploymentDetails) mustEqual false
    }

  }

}

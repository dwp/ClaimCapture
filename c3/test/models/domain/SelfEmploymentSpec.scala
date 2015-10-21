package models.domain

import controllers.mappings.Mappings
import models.view.CachedClaim
import org.specs2.mutable.Specification

class SelfEmploymentSpec extends Specification {

  val claimSelfEmployed = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List(Employment(Mappings.yes, Mappings.no)))
  ))
  val claimNotSelfEmployed = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List(Employment(Mappings.no, Mappings.no)))
  ))
  val claimWithNoEmploymentDetails = Claim(CachedClaim.key, List(
    Section(SelfEmployment, List())
  ))

  "Self Employment" should {

    "tell whether the Claim object contains self employment" in {
      SelfEmployment.isSelfEmployed(claimSelfEmployed) mustEqual true
      SelfEmployment.isSelfEmployed(claimNotSelfEmployed) mustEqual false
      SelfEmployment.isSelfEmployed(claimWithNoEmploymentDetails) mustEqual false
    }

  }

}

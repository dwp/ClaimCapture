package monitoring

import org.specs2.mutable._
import models.domain.{CircumstancesDeclaration, Claim}
import models.view.{CachedClaim, CachedChangeOfCircs}
import org.specs2.mock.Mockito
import utils.WithApplication

class ChangeBotCheckingSpec extends Specification with Mockito {
  "Claim submission" should {
    "returns false given CircumstancesDeclaration answered yes and honeyPot not filled" in new WithApplication {
      val controller = new ChangeBotChecking {}
      val circs = Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = None))
      controller.honeyPot(circs) should beFalse
    }

    "returns false given CircumstancesDeclaration answered no and honeyPot filled" in new WithApplication {
      val controller = new ChangeBotChecking {}
      val circs = Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff")))
      controller.honeyPot(circs) should beFalse
    }

    "return true given CircumstancesDeclaration answered yes and honeypot filled in" in new WithApplication {
      val controller = new ChangeBotChecking {}
      val circs = Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = Some("stuff")))
      controller.honeyPot(circs) should beTrue
    }

    "be flagged for completing sections too quickly e.g. a bot" in new WithApplication {
      val controller = new ChangeBotChecking {}
      val circs = new CachedChangeOfCircs(){}.copyInstance(Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff"))))
      controller.checkTimeToCompleteAllSections(circs, currentTime = 0) should beTrue
    }

    "be completed slow enough to be human" in new WithApplication {
      val controller = new ChangeBotChecking {}
      val circs = new CachedChangeOfCircs(){}.copyInstance(Claim(CachedClaim.key).update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff"))))
      controller.checkTimeToCompleteAllSections(circs, currentTime = Long.MaxValue) should beFalse
    }
  }


}

package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim

class G9ContactDetailsOfPayingPersonSpec extends Specification with Tags {
  "Contact details of paying person" should {
    """bypass to "breaks" when "no one has paid you to look after this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val moreAboutTheCare = mockQuestionGroup[MoreAboutTheCare](MoreAboutTheCare.id)
      moreAboutTheCare.hasSomeonePaidYou returns "no"

      val claim = Claim().update(moreAboutTheCare)
      Cache.set(claimKey, claim)

      val result = G9ContactDetailsOfPayingPerson.present(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
    }

    """present when "someone has paid you to look after this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val moreAboutTheCare = mockQuestionGroup[MoreAboutTheCare](MoreAboutTheCare.id)
      moreAboutTheCare.hasSomeonePaidYou returns "yes"

      val claim = Claim().update(moreAboutTheCare)
      Cache.set(claimKey, claim)

      val result = G9ContactDetailsOfPayingPerson.present(request)
      status(result) mustEqual OK
    }

    "be added to claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G9ContactDetailsOfPayingPerson.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(ContactDetailsOfPayingPerson) must beSome(ContactDetailsOfPayingPerson(None, None))
    }
  } section "unit"
}
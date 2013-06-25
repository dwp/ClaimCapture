package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache

class G9ContactDetailsOfPayingPersonSpec extends Specification {
  "Contact details of paying person" should {
    """bypass to "breaks" when "no one has paid you to look after this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val moreAboutTheCare = mockQuestionGroup[MoreAboutTheCare](MoreAboutTheCare.id)
      moreAboutTheCare.hasSomeonePaidYou returns "no"

      val claim = Claim().update(moreAboutTheCare)
      Cache.set(claimKey, claim)

      val result = controllers.CareYouProvide.contactDetailsOfPayingPerson(request)
      redirectLocation(result) must beSome("/careYouProvide/hasBreaks")
    }

    """present when "someone has paid you to look after this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val moreAboutTheCare = mockQuestionGroup[MoreAboutTheCare](MoreAboutTheCare.id)
      moreAboutTheCare.hasSomeonePaidYou returns "yes"

      val claim = Claim().update(moreAboutTheCare)
      Cache.set(claimKey, claim)

      val result = controllers.CareYouProvide.contactDetailsOfPayingPerson(request)
      status(result) mustEqual OK
    }

    """be submitted and proceed to "breaks" """ in new WithApplication with Claiming {

    }
  }
}
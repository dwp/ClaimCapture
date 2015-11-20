package xml.circumstances

import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import models.domain.{Claim, CircumstancesDeclaration}
import play.api.Play._
import play.api.i18n.{MMessages, MessagesApi}
import utils.WithApplication

class ConsentsSpec  extends Specification {

  "Consents" should {
    "Generate a valid consent with no Why element if not Why was provided" in new WithApplication {
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      val declaration = new CircumstancesDeclaration(obtainInfoAgreement= "Yes",obtainInfoWhy = None)
      val circs = Claim(CachedChangeOfCircs.key).update(declaration)
      val xml = Consents.xml(circs)
      (xml \\ "Consents" \ "Consent").length mustEqual 1
      (xml \\ "Consents"  \ "Consent" \ "Answer").text mustEqual declaration.obtainInfoAgreement
      (xml \\ "Consents" \ "Consent" \ "QuestionLabel").text mustEqual messagesApi("obtainInfoAgreement")
      (xml \\ "Consents"  \ "Consent" \ "Why").length mustEqual 0
    }

    "Generate a valid consent with  Why element if  Why was provided" in new WithApplication {
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      val declaration = new CircumstancesDeclaration(obtainInfoAgreement= "No",obtainInfoWhy = Some("Because"))
      val circs = Claim(CachedChangeOfCircs.key).update(declaration)
      val xml = Consents.xml(circs)
      (xml \\ "Consents" \ "Consent").length mustEqual 1
      (xml \\ "Consents"  \ "Consent" \ "Answer").text mustEqual declaration.obtainInfoAgreement
      (xml \\ "Consents" \ "Consent" \ "QuestionLabel").text mustEqual messagesApi("obtainInfoAgreement")
      (xml \\ "Consents"  \ "Consent" \ "Why" \ "Answer").text mustEqual "Because"
    }

  }
  section("unit")
}

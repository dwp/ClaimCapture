package xml.circumstances

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesDeclaration}


class ConsentsSpec  extends Specification with Tags {


  "Consents" should {

    "Generate a valid consent with no Why element if not Why was provided" in {
      val declaration = new CircumstancesDeclaration(obtainInfoAgreement= "Yes",obtainInfoWhy = None)
      val circs = Claim().update(declaration)
      val xml = Consents.xml(circs)
      (xml \\ "Consents" \ "Consent").length mustEqual 1
      (xml \\ "Consents"  \ "Consent" \ "Answer").text mustEqual declaration.obtainInfoAgreement
      (xml \\ "Consents" \ "Consent" \ "QuestionLabel").text mustEqual "obtainInfoAgreement"
      (xml \\ "Consents"  \ "Consent" \ "Why").length mustEqual 0
    }

    "Generate a valid consent with  Why element if  Why was provided" in {
      val declaration = new CircumstancesDeclaration(obtainInfoAgreement= "No",obtainInfoWhy = Some("Because"))
      val circs = Claim().update(declaration)
      val xml = Consents.xml(circs)
      (xml \\ "Consents" \ "Consent").length mustEqual 1
      (xml \\ "Consents"  \ "Consent" \ "Answer").text mustEqual declaration.obtainInfoAgreement
      (xml \\ "Consents" \ "Consent" \ "QuestionLabel").text mustEqual "obtainInfoAgreement"
      (xml \\ "Consents"  \ "Consent" \ "Why" \ "Answer").text mustEqual "Because"
    }

  } section "unit"

}

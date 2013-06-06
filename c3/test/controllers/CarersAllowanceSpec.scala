package controllers

import org.specs2.mutable.Specification
import play.api.test.{WithApplication, FakeRequest}

import play.api.cache.Cache
import models.view.example.{BenefitsForm, Section, Claim}


class CarersAllowanceSpec extends Specification {
 """Can you get Carer's Allowance""" should {
   "start with a new Claim" in new WithApplication {
     val request = FakeRequest().withSession("connected" -> "claim")

     val claim = Claim()
     Cache.set("claim", claim)

     CarersAllowance.questionGroup1(request)

     Cache.get("claim") match {
       case Some(c: Claim) => c.created mustNotEqual claim.created
       case _ => failure
     }
   }

   "acknowledge that the person looks after get one of the required benefits " in new WithApplication {
     val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
     CarersAllowance.questionGroup1Submit(request)

     val claim = Cache.getAs[Claim]("claim").get
     val section: Section = claim.getSectionWithId("s1").get
     section.form("s1.q1") match {
       case Some(f: BenefitsForm) => f.answer mustEqual true
       case _ => failure
     }
   }
 }
}


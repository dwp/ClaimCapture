package models.domain

import org.specs2.mutable._
import utils.WithApplication

class QuestionGroupSpec extends Specification {
  section("unit")
  "Question group" should {
    "state that the index of s1g9 is less than s1g10" in new WithApplication {
      val s1g9 = new QuestionGroup(new QGIdentifier(id = "s1g9")) {}
      val s1g10 = new QuestionGroup(new QGIdentifier(id = "s1g10")) {}

      s1g9.identifier.id < s1g10.identifier.id must beFalse
      s1g9.identifier.index < s1g10.identifier.index must beTrue
    }
  }
  section("unit")
}

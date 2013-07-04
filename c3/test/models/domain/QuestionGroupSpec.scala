package models.domain

import org.specs2.mutable.Specification

class QuestionGroupSpec extends Specification {
  "Question group" should {
    "state that the index of s1g9 is less than s1g10" in {
      val s1g9 = new QuestionGroup("s1g9") {}
      val s1g10 = new QuestionGroup("s1g10") {}

      s1g9.id < s1g10.id must beFalse
      s1g9.index < s1g10.index must beTrue
    }
  }
}
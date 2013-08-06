package xml

import models.domain.{MoreAboutYou, Claim}
import controllers.Mappings.{yes, no}
import org.specs2.mutable.{Tags, Specification}

class CourseOfEducationSpec extends Specification with Tags {

  "CourseOfEducation" should {

    "generate CourseOfEducation xml with answer 'yes' when claimer has been in education" in {
      val claim = Claim().update(MoreAboutYou(beenInEducationSinceClaimDate = yes))
      val courseOfEducationXml = CourseOfEducation.xml(claim)
      (courseOfEducationXml \\ "CourseOfEducation").text shouldEqual yes
    }

    "generate CourseOfEducation xml with answer 'no' when claimer has NOT been in education" in {
      val claim = Claim().update(MoreAboutYou(beenInEducationSinceClaimDate = no))
      val courseOfEducationXml = CourseOfEducation.xml(claim)
      (courseOfEducationXml \\ "CourseOfEducation").text shouldEqual no
    }
  } section "unit"
}

package xml

import models.domain.{MoreAboutYou, Claim}
import controllers.Mappings._

object CourseOfEducation {

  def xml(claim: Claim) = {
    val moreAboutYouOption = claim.questionGroup[MoreAboutYou]
    val moreAboutYou = moreAboutYouOption.getOrElse(MoreAboutYou(beenInEducationSinceClaimDate = no))

    <CourseOfEducation>{moreAboutYou.beenInEducationSinceClaimDate}</CourseOfEducation>
  }
}
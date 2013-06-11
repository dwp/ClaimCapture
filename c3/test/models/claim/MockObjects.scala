package models.claim

object MockObjects {

  val sectionOne: Section = {
    val forms = List[Form](BenefitsForm(), HoursForm(), LivesInGBForm(), Over16Form())
    Section(Section.allowanceId, forms)
  }

  val claim: Claim = {
    Claim(Map(Section.allowanceId -> sectionOne))
  }

}

package models.claim

object MockObjects {

  val sectionOne: Section = {
    val forms = List[Form](Benefits(), Hours(), LivesInGB(), Over16())
    Section(Section.allowanceId, forms)
  }

  val claim: Claim = {
    Claim(Map(Section.allowanceId -> sectionOne))
  }
}
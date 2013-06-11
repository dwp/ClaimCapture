package models.claim

object MockObjects {

  val sectionOne: Section = {
    val forms = List[Form](Benefits(), Hours(), LivesInGB(), Over16())
    Section(CarersAllowance.id, forms)
  }

  val claim: Claim = {
    Claim(Map(CarersAllowance.id -> sectionOne))
  }
}
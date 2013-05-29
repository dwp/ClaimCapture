package models.view

class Claim {
  def sections : Seq[Section] = {
    Seq(
      new Eligibility,
      new AboutYou
    )
  }
}

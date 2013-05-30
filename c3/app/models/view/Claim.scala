package models.view

case class Claim(sections : Seq[Section])

object Claim {
  def apply() = {
    new Claim(Seq(
      Eligibility(),
      AboutYou()
    ))
  }
}

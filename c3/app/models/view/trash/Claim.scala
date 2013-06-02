package models.view.trash

case class Claim(sections : Seq[Section])

object Claim {
  def apply() = {
    new Claim(Seq(
      Eligibility(),
      AboutYou()
    ))
  }
}

package models.domain

object CareYouProvide {
  val id = "s4"
}

case class TheirPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                                otherNames: Option[String], nationalInsuranceNumber: Option[String],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends QuestionGroup(TheirPersonalDetails.id)

object TheirPersonalDetails {
  val id = s"${CareYouProvide.id}.g1"
}

case class HasBreaks(breaks: String) extends QuestionGroup(HasBreaks.id)

object HasBreaks {
  val id = s"${CareYouProvide.id}.g10"
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare.id) {
  def update(break: Break) = BreaksInCare(breaks :+ break)
}

object BreaksInCare {
  val id = s"${CareYouProvide.id}.g11"

  def apply() = new BreaksInCare()
}

case class BreakInCare(moreBreaks: String, break: Option[Break])

case class Break(start: DayMonthYear, end: DayMonthYear)
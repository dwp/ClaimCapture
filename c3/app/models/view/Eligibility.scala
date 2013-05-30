package models.view

class Eligibility() extends Section {
  def name: String = "Eligibility"
  val questionGroups: Seq[QuestionGroup] = Seq(new Benefits, new HoursCaring)
}

class Benefits() extends QuestionGroup {
  def name: String = "Benefits"
  val benefits = BenefitsForm()
}
case class BenefitsForm(hasBenefits: Option[Boolean] = None)

class HoursCaring() extends QuestionGroup {
  def name: String = "HoursCaring"
  val enoughHours = HoursCaringForm()
}
case class HoursCaringForm(enoughHours: Option[Boolean] = None)



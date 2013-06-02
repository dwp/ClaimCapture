package models.view.trash

class Eligibility(val questionGroups: Seq[QuestionGroup]) extends Section {
  def name: String = "Eligibility"
}

object Eligibility {
  def apply() = {
     new Eligibility(Seq(Benefits(), HoursCaring()))
  }
}


class Benefits(val form : BenefitsForm) extends QuestionGroup {
  def name: String = "Benefits"
}
object Benefits {
  def apply() = {
    new Benefits(BenefitsForm())
  }
}
case class BenefitsForm(hasBenefits: Option[Boolean] = None) extends CarersForm



class HoursCaring(val form : HoursCaringForm) extends QuestionGroup {
  def name: String = "HoursCaring"
}
object HoursCaring {
  def apply() = {
    new HoursCaring(HoursCaringForm())
  }
}
case class HoursCaringForm(enoughHours: Option[Boolean] = None) extends CarersForm



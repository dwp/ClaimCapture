package models.view

class Eligibility() extends Section {
  val questionGroups: Seq[QuestionGroup] = Seq(new Benefits, new HoursCaring)
  def name: String = "Eligibility"
}

class Benefits() extends QuestionGroup {
  val questions: Seq[Question] = Seq(new HasBenefits)
}

class HoursCaring() extends QuestionGroup {
  val questions: Seq[Question] = Seq(new EnoughHours)
}

class HasBenefits extends Question {
  val label = "Has benefits"
}

class EnoughHours extends Question {
  val label = "Enough hours"
}



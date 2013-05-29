package models.view

import models.view.common.{FirstName, Postcode, ClaimDate}

class AboutYou extends Section {
  def name: String = "AboutYou"
  val questionGroups: Seq[QuestionGroup] = Seq(new Details, new ContactDetails, new ClaimStartDate)
}

class Details extends QuestionGroup {
  val questions: Seq[Question] = Seq(new FirstName)
}

class ContactDetails extends QuestionGroup {
  val questions: Seq[Question] = Seq(new Postcode)
}

class ClaimStartDate extends QuestionGroup {
  val questions: Seq[Question] = Seq(new ClaimDate)
}

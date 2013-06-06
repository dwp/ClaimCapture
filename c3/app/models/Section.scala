package models

case class Section(id: String, forms: List[CarersAllowanceForm]) {
  def isComplete() = false

  def form(id: String): Option[CarersAllowanceForm] = {
    forms.find(form => form.id.equals(id))
  }
}
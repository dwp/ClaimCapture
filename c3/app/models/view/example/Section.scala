package models.view.example

case class Section(id: String, forms: Seq[CarersAllowanceForm]) {

  def isComplete() = false

  def form(id: String): Option[CarersAllowanceForm] = {
    forms.find(form => form.id.equals(id))
  }
}

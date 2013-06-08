package models.claim

case class Section(id: String, forms: List[Form]) {
  def isComplete() = false // TODO Is this still needed?

  def form(id: String): Option[Form] = {
    forms.find(form => form.id.equals(id))
  }
}
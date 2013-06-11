package models.claim

case class Section(id: String, forms: List[Form]) {
  def form(formId: String): Option[Form] = {
    forms.find(form => form.id == formId)
  }
}
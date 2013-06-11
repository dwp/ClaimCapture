package utils

object ClaimUtils {

  def sectionId(formId: String) = {
    formId.split('.')(0)
  }

}

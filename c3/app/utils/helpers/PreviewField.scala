package utils.helpers

/**
 * Used to build HTML ID attributes for elements on the
 * preview (check your answers) page.
 *
 * Also used in tests to reference these elements using
 * CSS selectors.
 */
object PreviewField {

  def getLabelId(id: String, useCssSelector: Boolean = true): String = {
    if (useCssSelector) "#" + id + "_label" else id + "_label"
  }

  def getValueId(id: String, useCssSelector: Boolean = true): String = {
    if (useCssSelector) "#" + id + "_value" else id + "_value"
  }

  def getLinkContainerId(id: String, useCssSelector: Boolean = true): String = {
    if (useCssSelector) "#" + id + "_linkContainer" else id + "_linkContainer"
  }

  def getLinkId(id: String, useCssSelector: Boolean = true): String = {
    if (useCssSelector) "#" + id + "_link" else id + "_link"
  }

}

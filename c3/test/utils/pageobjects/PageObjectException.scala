package utils.pageobjects


/**
 * Exception thrown by the Page Object test framework                  .
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
case class PageObjectException(message: String, errors: List[String] = null, exception:Exception = null) extends RuntimeException(PageObjectException.buildDetailMessage(message,errors),exception) {


}

object PageObjectException {
  def buildDetailMessage(message: String, errors: List[String]) = {
    val fullMessage = new StringBuilder(message)
    if (null != errors) errors foreach { s => fullMessage ++= ". "  ++= s}
    fullMessage.toString()
  }
}

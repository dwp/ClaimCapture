package utils.pageobjects

/**
 * Exception thrown by the Page Object test framework                  .
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
case class PageObjectException(message: String) extends RuntimeException(message) {

}

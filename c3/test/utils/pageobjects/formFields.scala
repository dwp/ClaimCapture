package utils.pageobjects

import scala.collection.mutable

/**
 * To be used to declare a form field. It is injected into Page class, so the fields of an html form can be declared
 * in a page object.
 * @author Jorge Migueis
 *         Date: 03/08/2013
 */
trait FormFields {

  protected val fields = mutable.ArrayBuffer.empty[(String,Symbol,String)]

  val ADDRESS = 'Address
  val CHECK = 'Check
  val DATE = 'Date
  val DATE_FROM = 'DateFrom
  val DATE_TO = 'DateTo
  val INPUT = 'Input
  val JSINPUT = 'JSInput
  val NINO = 'Nino
  val RADIO_LIST = 'RadioList
  val SELECT = 'Select
  val SORTCODE = 'SortCode
  val TIME = 'Time
  val YESNO = 'YesNo
  val YESNODONTKNOW = 'YesNoDontknow

  def declareAddress(elementCssSelector: String, claimAttribute: String) = declareField(ADDRESS, elementCssSelector, claimAttribute)

  def declareCheck(elementCssSelector: String, claimAttribute: String) = declareField(CHECK, elementCssSelector, claimAttribute)

  def declareDate(elementCssSelector: String, claimAttribute: String) = declareField(DATE, elementCssSelector, claimAttribute)

  def declareDateFromTo(elementCssSelector: String, claimAttributeFrom: String,claimAttributeTo: String) = {
      declareDateFrom(elementCssSelector, claimAttributeFrom)
      declareDateTo(elementCssSelector, claimAttributeTo)
  }

  private def declareDateFrom(elementCssSelector: String, claimAttribute: String) = declareField(DATE_FROM, elementCssSelector, claimAttribute)

  private def declareDateTo(elementCssSelector: String, claimAttribute: String) = declareField(DATE_TO, elementCssSelector, claimAttribute)

  def declareInput(elementCssSelector: String, claimAttribute: String) = declareField(INPUT, elementCssSelector, claimAttribute)
  def declareJSInput(elementCssSelector: String, claimAttribute: String) = declareField(JSINPUT, elementCssSelector, claimAttribute)

  def declareNino(elementCssSelector: String, claimAttribute: String) = declareField(NINO, elementCssSelector, claimAttribute)

  def declareRadioList(elementCssSelector: String, claimAttribute: String) = declareField(RADIO_LIST, elementCssSelector, claimAttribute)

  def declareSelect(elementCssSelector: String, claimAttribute: String) = declareField(SELECT, elementCssSelector, claimAttribute)

  def declareSortCode(elementCssSelector: String, claimAttribute: String) = declareField(SORTCODE, elementCssSelector, claimAttribute)

  def declareTime(elementCssSelector: String, claimAttribute: String) = declareField(TIME, elementCssSelector, claimAttribute)

  def declareYesNo(elementCssSelector: String, claimAttribute: String) = declareField(YESNO, elementCssSelector, claimAttribute)

  def declareYesNoDontKnow(elementCssSelector: String, claimAttribute: String) = declareField(YESNODONTKNOW, elementCssSelector, claimAttribute)

  private def declareField( fieldType:Symbol , elementCssSelector: String, claimAttribute: String) = fields += Tuple3(elementCssSelector, fieldType , claimAttribute )
}
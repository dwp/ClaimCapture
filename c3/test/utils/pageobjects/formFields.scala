package utils.pageobjects

import scala.collection.mutable

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 03/08/2013
 */
trait FormFields {

  protected val fields = mutable.ArrayBuffer.empty[Tuple3[String,Symbol,String]]

  val ADDRESS = 'Address
  val DATE = 'Date
  val DATE_FROM = 'DateFrom
  val DATE_TO = 'DateTo
  val INPUT = 'Input
  val RADIO_LIST = 'RadioList
  val SELECT = 'Select
  val PAYMENT_FREQUENCY = 'PaymentFrequency
  val WHEREABOUTS = 'Whereabouts
  val TIME = 'Time
  val YESNO = 'YesNo

  def declareAddress(elementCssSelector: String, claimAttribute: String) = declareField(ADDRESS, elementCssSelector, claimAttribute)

  def declareDate(elementCssSelector: String, claimAttribute: String) = declareField(DATE, elementCssSelector, claimAttribute)


  def declareDateFromTo(elementCssSelector: String, claimAttributeFrom: String,claimAttributeTo: String) = {
      declareDateFrom(elementCssSelector, claimAttributeFrom)
      declareDateTo(elementCssSelector, claimAttributeTo)
  }

  private def declareDateFrom(elementCssSelector: String, claimAttribute: String) = declareField(DATE_FROM, elementCssSelector, claimAttribute)

  private def declareDateTo(elementCssSelector: String, claimAttribute: String) = declareField(DATE_TO, elementCssSelector, claimAttribute)

  def declareInput(elementCssSelector: String, claimAttribute: String) = declareField(INPUT, elementCssSelector, claimAttribute)

  def declareNino(elementCssSelector: String, claimAttribute: String) = declareField(INPUT, elementCssSelector, claimAttribute)

  def declareRadioList(elementCssSelector: String, claimAttribute: String) = declareField(RADIO_LIST, elementCssSelector, claimAttribute)

  def declareSelect(elementCssSelector: String, claimAttribute: String) = declareField(SELECT, elementCssSelector, claimAttribute)

//  def declareSelectWithOther(elementCssSelector: String, claimAttribute: String) = declareField(SELECT, elementCssSelector, claimAttribute)

  def declarePaymentFrequency(elementCssSelector: String, claimAttribute: String) = declareField(PAYMENT_FREQUENCY, elementCssSelector, claimAttribute)

  def declareWhereabouts(elementCssSelector: String, claimAttribute: String) = declareField(WHEREABOUTS, elementCssSelector, claimAttribute)

  def declareTime(elementCssSelector: String, claimAttribute: String) = declareField(TIME, elementCssSelector, claimAttribute)

  def declareYesNo(elementCssSelector: String, claimAttribute: String) = declareField(YESNO, elementCssSelector, claimAttribute)

  private def declareField( fieldType:Symbol , elementCssSelector: String, claimAttribute: String) = fields += Tuple3(elementCssSelector, fieldType , claimAttribute )


}

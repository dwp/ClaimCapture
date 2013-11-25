package xml

import scala.xml._
import scala.language.implicitConversions
import scala.reflect.ClassTag
import app.XMLValues._
import models._
import models.PaymentFrequency
import models.MultiLineAddress
import models.PeriodFromTo
import models.NationalInsuranceNumber
import play.api.i18n.Messages
import scala.deprecated
import play.api.Logger

object XMLHelper {


  private def stringifyOption(value: Option[_], default: String = ""):String = value match {
    case Some(s) => stringify(s,default)
    case None => default
  }

  def stringify[T](value: T, default: String = ""): String = value match {
    case s: String => formatValue(s)
    case b: Boolean => booleanToYesNo(b)
    case dmy: DayMonthYear => dmy.`dd-MM-yyyy`
    case nr: NationalInsuranceNumber => nr.stringify
    case sc: SortCode => sc.stringify
    case pf: PaymentFrequency => pf.stringify
    case opt: Option[_] => stringifyOption(opt, default)
    case _ => default
  }

  // CJR : Note that I'm changing the text to format it
  private def nodifyOption(value: Option[_]): NodeSeq = value match {
    case Some(s) => nodify(s)
    case _ => Text("")
  }

  // CJR : Note that I'm changing the text to format it
  private def nodify[T](value: T): NodeSeq = value match {
    case dmy: DayMonthYear => Text(dmy.`dd-MM-yyyy`)
    case nr: NationalInsuranceNumber => Text(nr.stringify)
    case pf: PaymentFrequency => paymentFrequency(pf)
    case pft: PeriodFromTo => fromToStructure(pft)
    case sc: SortCode => Text(sc.sort1 + sc.sort2 + sc.sort2)
    case opt: Option[_] => nodifyOption(opt)
    case nd: NodeSeq => nd
    case _ => Text(stringify(value))
  }

  def postalAddressStructure(addressOption: Option[MultiLineAddress], postcodeOption: Option[String]): NodeSeq = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(address, postcodeOption.orNull)
    case _ => postalAddressStructure(new MultiLineAddress(), postcodeOption.orNull)
  }

  def postalAddressStructure(addressOption: MultiLineAddress, postcodeOption: Option[String]): NodeSeq = postalAddressStructure(addressOption, postcodeOption.getOrElse(""))

  def postalAddressStructure(address: MultiLineAddress, postcode: String): NodeSeq = {
    <Address>
      {if(address.lineOne.isEmpty){NodeSeq.Empty}else{<Line>{address.lineOne.orNull}</Line>}}
      {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.orNull}</Line>}}
      {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.orNull}</Line>}}
      {if(postcode == null || postcode == "") NodeSeq.Empty else <PostCode>{postcode.toUpperCase}</PostCode>}
    </Address>
  }

  def postalAddressStructureRecipientAddress(address: MultiLineAddress, postcode: String): NodeSeq = {
    <RecipientAddress>
      <Line>{address.lineOne.orNull}</Line>
      {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.orNull}</Line>}}
      {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.orNull}</Line>}}
      {if(postcode == null || postcode == "") NodeSeq.Empty else <PostCode>{postcode.toUpperCase}</PostCode>}
    </RecipientAddress>
  }

  def moneyStructure(amount: String):NodeSeq = {
    <Currency>{GBP}</Currency>
    <Amount>{amount}</Amount>
  }


  def question[T](wrappingNode:Node,questionLabelCode: String, answerText: T,labelParameters: Option[String] = None): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptional(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],labelParameters)
    else {
      val answer:NodeSeq = nodify(answerText)
      if (answer.length > 0) addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters) ++ <Answer>{answer}</Answer>)
      else NodeSeq.Empty
    }

  }

  private def questionOptional[T](wrappingNode:Node,questionLabelCode: String, answer:Option[T],labelParameters: Option[String] = None ): NodeSeq = {
    if (answer.isDefined) question(wrappingNode,questionLabelCode,answer.get,labelParameters)
    else NodeSeq.Empty
  }

  private def questionLabel(questionLabelCode: String,labelParameters: Option[String]) = {
    <QuestionLabel>{labelParameters match {
      case Some(p) => Messages(questionLabelCode, p)
      case None => Messages(questionLabelCode)
    }}</QuestionLabel>
  }

  def questionOther[T](wrappingNode:Node,questionLabelCode: String, answerText: T, otherText: Option[String],labelParameters: Option[String] = None): NodeSeq = {
    val other = <Other/>
    addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters) ++ optionalEmpty(otherText,other) ++ <Answer>{stringify(answerText)}</Answer>)
  }

  // We should only see a why text supplied if the answer is no, but add the why text regardless if supplied.
  // The business logic should be in the UI not in XML generation.
  def questionWhy[T](wrappingNode:Node,questionLabelCode: String, answerText: T, whyText: Option[String],labelParameters: Option[String] = None): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptionalWhy(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],whyText,labelParameters)
     else {
    val why = <Why/>
    addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters) ++ <Answer>{stringify(answerText)}</Answer> ++ optionalEmpty(whyText,why))
    }
  }

  private def questionOptionalWhy[T](wrappingNode:Node,questionLabelCode: String, answerOption: Option[T], whyText: Option[String],labelParameters: Option[String] = None): NodeSeq = {
    if (answerOption.isDefined) {
      questionWhy(wrappingNode,questionLabelCode,answerOption.get,whyText,labelParameters )
//      val why = <Why/>
//      addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters) ++ <Answer>{formatValue(answerText.get)}</Answer> ++ optionalEmpty(whyText,why))
    } else NodeSeq.Empty
  }

  def questionCurrency(wrappingNode:Node,questionLabelCode: String, amount:Option[String],labelParameters: Option[String] = None): NodeSeq = {
    if (amount.isDefined) {
//      Logger.debug("amount: " + amount.get )
      question(wrappingNode,questionLabelCode,moneyStructure(amount.get),labelParameters)
    }
    else NodeSeq.Empty
  }


  def fromToStructure(period: Option[PeriodFromTo]): NodeSeq = {
    period.fold(
      (<DateFrom/><DateTo/>).asInstanceOf[NodeSeq]
    )(fromToStructure)
  }

  def fromToStructure(period: PeriodFromTo): NodeSeq = {
    <DateFrom>{period.from.`yyyy-MM-dd`}</DateFrom>
    <DateTo>{period.to.`yyyy-MM-dd`}</DateTo>
  }

  def paymentFrequency(freq: Option[PaymentFrequency]): NodeSeq = freq match {
    case Some(p) => paymentFrequency(p)
    case _ => NodeSeq.Empty
  }

  def paymentFrequency(freq: PaymentFrequency): NodeSeq =
    <PayFrequency>
      <QuestionLabel>{Messages("paymentFrequency")}</QuestionLabel>
      {
      freq.other match{
        case Some(s) => <Other>{s}</Other>
        case _ => NodeSeq.Empty
      }
      }<Answer>{freq.frequency}</Answer>
    </PayFrequency>

  def optional[T](option: Option[T], elem: Elem)(implicit classTag: ClassTag[T]): Elem = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => elem
  }

  def optional[T](option: Option[T], elem: Elem, default: String)(implicit classTag: ClassTag[T]): Elem = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => addChild(elem, Text(default))
  }

  def optionalEmpty[T](option: Option[T], elem: Elem)(implicit classTag: ClassTag[T]): NodeSeq = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => NodeSeq.Empty
  }

  def addChild(n: Node, children: NodeSeq) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) => Elem(prefix, label, attribs, scope, true, child ++ children : _*)
    case _ => <error>failed adding children</error>
  }

  implicit def attachToNode(elem: Elem) = new {
    def +++[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem)

    def +-[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, NotAsked)

    def +?[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, yes)

    def +!?[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, no)

    def ?+[T](option: Option[T])(implicit classTag: ClassTag[T]): NodeSeq = optionalEmpty(option, elem)
  }

  @deprecated("Should be replaced by call to formatValue", "25.11.2013")
  def booleanStringToYesNo(booleanString: String) = booleanString match {
    case "true" => Yes
    case "false" => No
    case null => ""
    case _ => booleanString
  }

  def booleanToYesNo(value:Boolean) = if (value) Yes else No

  def titleCase(s: String) = if(s != null && s.length() > 0) s.head.toUpper + s.tail.toLowerCase else ""

  def formatValue(value:String):String = value match {
       case "yes" => Yes
       case "no" => No
       case "other" => Other
       case "true" => Yes
       case "false" => No
       case _ => value
     }

  def extractIdFrom(xml:Elem):String = {(xml \\ "TransactionId").text}

  // relies on the question function being passed
//  def optionalQuestions (conditionField:String, parentNode:Node, questionNode:NodeSeq) = {
//    conditionField.isEmpty match {
//      case false => addChild(parentNode, questionNode)
//      case true => NodeSeq.Empty
//    }
//  }

}
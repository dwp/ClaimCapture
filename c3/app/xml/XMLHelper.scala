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
    case ppf: PensionPaymentFrequency => ppf.stringify
    case opt: Option[_] => stringifyOption(opt, default)
    case _ => default
  }

  // CJR : Note that I'm changing the text to format it
  private def nodifyOption(value: Option[_], default: String = ""): NodeSeq = value match {
    case Some(s) => nodify(s)
    case _ => Text(default)
  }

  // CJR : Note that I'm changing the text to format it
  private def nodify[T](value: T, default: String = ""): NodeSeq = value match {
    case dmy: DayMonthYear => Text(dmy.`dd-MM-yyyy`)
    case nr: NationalInsuranceNumber => Text(nr.stringify)
    case pf: PaymentFrequency => paymentFrequency(pf)
    case pft: PeriodFromTo => fromToStructure(pft)
    case sc: SortCode => Text(sc.sort1 + sc.sort2 + sc.sort2)
    case opt: Option[_] => nodifyOption(opt)
    case nd: NodeSeq => nd
    case _ => Text(stringify(value,default))
  }

  def postalAddressStructure(addressOption: Option[MultiLineAddress], postcodeOption: Option[String]): NodeSeq = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(address, postcodeOption)
    case _ => postalAddressStructure(new MultiLineAddress(), postcodeOption)
  }

  def postalAddressStructure(addressOption: MultiLineAddress, postcodeOption: Option[String]): NodeSeq = postalAddressStructure(addressOption, postcodeOption.getOrElse(""))

  private def postalAddressStructure(address: MultiLineAddress, postcode: String): NodeSeq = {
    <Address>
      {if(address.lineOne.isEmpty){NodeSeq.Empty}else{<Line>{address.lineOne.get}</Line>}}
      {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.get}</Line>}}
      {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.get}</Line>}}
      {if(postcode == null || postcode.isEmpty) NodeSeq.Empty else <PostCode>{postcode.toUpperCase}</PostCode>}
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

  private def moneyStructure(amount: String):NodeSeq = {
    <Currency>{GBP}</Currency>
    <Amount>{amount}</Amount>
  }


  def question[T](wrappingNode:Node,questionLabelCode: String, answerText: T,labelParameters: String*): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptional(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],labelParameters:_*)
    else {
      val answer:NodeSeq = nodify(answerText)
      if (answer.text.length > 0) addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ <Answer>{answer}</Answer>)
      else NodeSeq.Empty
    }

  }

  private def questionOptional[T](wrappingNode:Node,questionLabelCode: String, answer:Option[T],labelParameters: String* ): NodeSeq = {
    if (answer.isDefined) question(wrappingNode,questionLabelCode,answer.get,labelParameters:_*)
    else NodeSeq.Empty
  }

  private def questionLabel(questionLabelCode: String,labelParameters: String*) = {
    <QuestionLabel>{Messages(questionLabelCode, labelParameters: _*)}</QuestionLabel>
  }

  def questionOther[T](wrappingNode:Node,questionLabelCode: String, answerText: T, otherText: Option[String],labelParameters: String *): NodeSeq = {
    val other = <Other/>
    addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ optionalEmpty(otherText,other) ++ <Answer>{stringify(answerText)}</Answer>)
  }

  // We should only see a why text supplied if the answer is no, but add the why text regardless if supplied.
  // The business logic should be in the UI not in XML generation.
  def questionWhy[T](wrappingNode:Node,questionLabelCode: String, answerText: T, whyText: Option[String],labelParameters: String*): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptionalWhy(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],whyText,labelParameters:_*)
     else {
    val why = <Why/>
    addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ <Answer>{stringify(answerText)}</Answer> ++ optionalEmpty(whyText,why))
    }
  }

  private def questionOptionalWhy[T](wrappingNode:Node,questionLabelCode: String, answerOption: Option[T], whyText: Option[String],labelParameters: String*): NodeSeq = {
    if (answerOption.isDefined) {
      questionWhy(wrappingNode,questionLabelCode,answerOption.get,whyText,labelParameters:_* )
    } else NodeSeq.Empty
  }

  def questionCurrency(wrappingNode:Node,questionLabelCode: String, amount:Option[String],labelParameters: String*): NodeSeq = {
    if (amount.isDefined) {
      question(wrappingNode,questionLabelCode,moneyStructure(amount.get),labelParameters:_*)
    }
    else NodeSeq.Empty
  }

  private def fromToStructure(period: PeriodFromTo): NodeSeq = {
    <DateFrom>{period.from.`yyyy-MM-dd`}</DateFrom>
    <DateTo>{period.to.`yyyy-MM-dd`}</DateTo>
  }

  @deprecated("Should use new question function", "25.11.2013")
  def paymentFrequency(freq: Option[PaymentFrequency]): NodeSeq = freq match {
    case Some(p) => paymentFrequency(p)
    case _ => NodeSeq.Empty
  }

  private def paymentFrequency(freq: PaymentFrequency): NodeSeq =
      questionOther(<PayFrequency/>,"paymentFrequency",freq.frequency,freq.other)


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

  private def addChild(n: Node, children: NodeSeq) = n match {
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


  @deprecated("Should use stringify or formatValue or nothing since question function already take care of formatting.", "25.11.2013")
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

}
package xml

import javax.xml.bind.DatatypeConverter

import app.XMLValues._
import com.dwp.carers.security.encryption.EncryptorAES
import models._
import models.domain.Claim
import play.api.i18n.{Lang, MMessages => Messages}
import utils.helpers.PastPresentLabelHelper._

import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.xml._

object XMLHelper {

  def statement[T](wrappingNode:Node,answer: T): NodeSeq = {
    if (answer.isInstanceOf[Option[_]]) statementOptional(wrappingNode,answer.asInstanceOf[Option[_]])
    else {
      val answerNode:NodeSeq = nodify(answer)
      if (answerNode.text.length > 0) addChild(wrappingNode,answerNode)
      else NodeSeq.Empty
    }
  }

  def question[T](wrappingNode:Node,questionLabelCode: String, answerText: T,labelParameters: String*): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptional(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],labelParameters:_*)
    else {
      val answer:NodeSeq = nodify(answerText)
      if (answer.text.length > 0) addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ <Answer>{answer}</Answer>)
      else NodeSeq.Empty
    }
  }

  def questionOther[T](wrappingNode:Node,questionLabelCode: String, answerText: T, otherText: Option[String],labelParameters: String *): NodeSeq = {
    val other = <Other/>
    addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ optionalEmpty(otherText,other) ++ <Answer>{stringify(answerText)}</Answer>)
  }

  // We should only see a why text supplied if the answer is no, but add the why text regardless if supplied.
  // The business logic should be in the UI not in XML generation.
  def questionWhy[T](wrappingNode:Node,questionLabelCode: String, answerText: T, whyText: Option[String], questionWhyLabelCode: String,labelParameters: String*): NodeSeq = {
    if (answerText.isInstanceOf[Option[_]]) questionOptionalWhy(wrappingNode,questionLabelCode,answerText.asInstanceOf[Option[_]],whyText,questionWhyLabelCode,labelParameters:_*)
    else {
      val whyTextNode = if (whyText.isDefined) addChild(<Why/>,questionLabel(questionWhyLabelCode,labelParameters:_*) ++ <Answer>{nodify(whyText)}</Answer>) else NodeSeq.Empty
      addChild(wrappingNode,questionLabel(questionLabelCode,labelParameters:_*) ++ <Answer>{stringify(answerText)}</Answer> ++ whyTextNode)
    }
  }

  def questionCurrency(wrappingNode:Node,questionLabelCode: String, amount:Option[String],labelParameters: String*): NodeSeq = {
    if (amount.isDefined) {
      question(wrappingNode,questionLabelCode,moneyStructure(amount.get),labelParameters:_*)
    }
    else NodeSeq.Empty
  }

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

  private def nodifyOption(value: Option[_], default: String = ""): NodeSeq = value match {
    case Some(s) => nodify(s)
    case _ => Text(default)
  }

  private def nodify[T](value: T, default: String = ""): NodeSeq = value match {
    case dmy: DayMonthYear => Text(dmy.`dd-MM-yyyy`)
    case nr: NationalInsuranceNumber => Text(nr.stringify)
    case pf: PaymentFrequency => paymentFrequency(pf)
    case pft: PeriodFromTo => fromToStructure(pft)
    case sc: SortCode => Text(sc.sort1 + sc.sort2 + sc.sort3)
    case opt: Option[_] => nodifyOption(opt)
    case nd: NodeSeq => nd
    case _ => Text(stringify(value,default))
  }

  def postalAddressStructureOpt(questionLabelCode: String, addressOption: Option[MultiLineAddress], postcodeOption: String): NodeSeq = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(questionLabelCode,address, postcodeOption)
    case _ => NodeSeq.Empty
  }
  def postalAddressStructure(questionLabelCode: String, addressOption: Option[MultiLineAddress], postcodeOption:String): NodeSeq = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(questionLabelCode,address, postcodeOption)
    case _ => postalAddressStructure(questionLabelCode, new MultiLineAddress(), postcodeOption)
  }

//def postalAddressStructure(questionLabelCode: String, addressOption: MultiLineAddress, postcodeOption: Option[String]): NodeSeq = postalAddressStructure(questionLabelCode, addressOption, postcodeOption.getOrElse(""))

   def postalAddressStructure(questionLabelCode: String, address: MultiLineAddress, postcode: String): NodeSeq = {
    <Address>
      {questionLabel(questionLabelCode)}
      <Answer>
        {postalAddress(address, postcode)}
      </Answer>
    </Address>
  }

  def postalAddressStructureRecipientAddress(questionLabelCode: String, address: MultiLineAddress, postcode: Option[String]): NodeSeq = {
    <RecipientAddress>
      {questionLabel(questionLabelCode)}
      <Answer>
        {postalAddress(address, postcode.orNull)}
      </Answer>
    </RecipientAddress>
  }

  def postalAddressStructurePreviousAddress(questionLabelCode: String, address: MultiLineAddress, postcode: String): NodeSeq = {
    <PreviousAddress>
      {questionLabel(questionLabelCode)}
      <Answer>
        {postalAddress(address, postcode)}
      </Answer>
    </PreviousAddress>
  }

  def postalAddressStructureNewAddress(questionLabelCode: String, address: MultiLineAddress, postcode: String): NodeSeq = {
    <NewAddress>
      {questionLabel(questionLabelCode)}
      <Answer>
        {postalAddress(address, postcode)}
      </Answer>
    </NewAddress>
  }

  def postalAddressStructureCareeAddress(questionLabelCode: String, address: Option[MultiLineAddress], postcode:String): NodeSeq = {
    if(address.isDefined) {
      <CareeAddress>
        {questionLabel(questionLabelCode)}
        <Answer>
          {postalAddress(address.get, postcode)}
        </Answer>
      </CareeAddress>
    } else NodeSeq.Empty
  }

  private def postalAddress(address: MultiLineAddress, postcode: String):NodeSeq = {
    {if(address.lineOne.isEmpty){NodeSeq.Empty}else{<Line>{address.lineOne.get}</Line>}} ++
    {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.get}</Line>}} ++
    {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.get}</Line>}} ++
    {if(postcode == null || postcode.isEmpty) NodeSeq.Empty else <PostCode>{postcode}</PostCode>}
  }

  private def moneyStructure(amount: String):NodeSeq = {
    <Currency>{GBP}</Currency>
    <Amount>{currencyAmount(amount)}</Amount>
  }

  private def questionOptional[T](wrappingNode:Node,questionLabelCode: String, answer:Option[T],labelParameters: String* ): NodeSeq = {
    if (answer.isDefined) question(wrappingNode,questionLabelCode,answer.get,labelParameters:_*)
    else NodeSeq.Empty
  }

  private def questionLabel(questionLabelCode: String,labelParameters: String*) = {
    <QuestionLabel>{Messages(questionLabelCode, labelParameters: _*)}</QuestionLabel>
  }

  private def questionOptionalWhy[T](wrappingNode:Node,questionLabelCode: String, answerOption: Option[T], whyText: Option[String],questionWhyLabelCode: String,labelParameters: String*): NodeSeq = {
    if (answerOption.isDefined) {
      questionWhy(wrappingNode,questionLabelCode,answerOption.get,whyText,questionWhyLabelCode,labelParameters:_* )
    }
    else NodeSeq.Empty
  }
  
  private def statementOptional[T](wrappingNode:Node,answerText: Option[T]): NodeSeq = {
    if (answerText.isDefined) statement(wrappingNode,answerText.get)
    else NodeSeq.Empty
  }

  private def fromToStructure(period: PeriodFromTo): NodeSeq = {
    <DateFrom>{period.from.`yyyy-MM-dd`}</DateFrom>
    <DateTo>{period.to.`yyyy-MM-dd`}</DateTo>
  }

  private def paymentFrequency(freq: PaymentFrequency): NodeSeq =
      questionOther(<PayFrequency/>,"paymentFrequency",freq.frequency,freq.other)


  def optionalEmpty[T](option: Option[T], elem: Elem)(implicit classTag: ClassTag[T]): NodeSeq = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => NodeSeq.Empty
  }

  private def addChild(n: Node, children: NodeSeq) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) => Elem(prefix, label, attribs, scope, true, child ++ children : _*)
    case _ => <error>failed adding children</error>
  }

  private def booleanToYesNo(value:Boolean) = if (value) Yes else No

  def titleCase(s: String) = if(s != null && s.length() > 0) s.head.toUpper + s.tail.toLowerCase else ""

  private def formatValue(value:String):String = value match {
       case "yes" => Yes
       case "no" => No
       case "other" => Other
       case "true" => Yes
       case "false" => No
       case _ => value
     }

//  def extractIdFrom(xml:Elem):String = {(xml \\ "TransactionId").text}

  def textSeparatorLine(title: String) = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>
      {s"$padding$title$padding"}
    </TextLine>
  }

  def textLine(): NodeSeq = <TextLine/>

  def textLine(text: String): NodeSeq = <TextLine>
    {text}
  </TextLine>

  def textLine(label: String, value: String): NodeSeq = value match {
    case "" => NodeSeq.Empty
    case _ => <TextLine>
      {s"$label" + formatValue(value)}
    </TextLine>
  }

  def textLine(label: String, value: Option[String]): NodeSeq = value match {
    case Some(s) => textLine(label, value.get)
    case None => NodeSeq.Empty
  }

  def currencyAmount(currency:String) = {
    if(currency.split(poundSign).size >1) currency.split(poundSign)(1)
    else currency
  }

  def currencyAmount(currency:Option[_]):Option[_] = {
    currency match {
      case Some(s) => {
        if(s.toString.split(poundSign).size >1) Some(s.toString.split(poundSign)(1))
        else Some(s)
      }
      case _ => Some("")
    }
  }

  def questionLabel(claim:Claim, labelKey:String) = {
    labelForSelfEmployment(claim, claim.lang.getOrElse(new Lang("en")), labelKey)
  }

  def questionLabelEmployment(claim:Claim, labelKey:String, jobID: String) = {
    labelForEmployment(claim, claim.lang.getOrElse(new Lang("en")), labelKey, jobID)
  }

  def encrypt[T](text:T) = DatatypeConverter.printBase64Binary((new  EncryptorAES).encrypt(stringify(text)))

}
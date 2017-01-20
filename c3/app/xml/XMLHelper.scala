package xml

import javax.xml.bind.DatatypeConverter

import app.XMLValues._
import controllers.mappings.Mappings
import gov.dwp.carers.security.encryption.EncryptorAES
import models._
import models.domain.Claim
import org.joda.time.format.DateTimeFormat
import play.api.i18n.Lang
import utils.helpers.PastPresentLabelHelper._
import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.xml._

object XMLHelper {
  val datePattern = "dd-MM-yyyy"
  def question[T](wrappingNode:Node,questionLabelCode: String, answerText: T,labelParameters: String*): NodeSeq = {
    answerText match {
      case text:Option[_] => questionOptional(wrappingNode,questionLabelCode,text,labelParameters:_*)
      case _ =>
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
    answerText match {
      case text:Option[_] => questionOptionalWhy(wrappingNode,questionLabelCode,text,whyText,questionWhyLabelCode,labelParameters:_*)
      case _ =>
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
    <QuestionLabel>{messagesApi(questionLabelCode, labelParameters: _*)}</QuestionLabel>
  }

  private def questionOptionalWhy[T](wrappingNode:Node,questionLabelCode: String, answerOption: Option[T], whyText: Option[String],questionWhyLabelCode: String,labelParameters: String*): NodeSeq = {
    if (answerOption.isDefined) {
      questionWhy(wrappingNode,questionLabelCode,answerOption.get,whyText,questionWhyLabelCode,labelParameters:_* )
    }
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

  private def formatValue(value:String):String = value match {
       case "yes" => Yes
       case "no" => No
       case "other" => Other
       case "true" => Yes
       case "false" => No
       case "dontknow" => dontknow
       case _ => value
  }

  def currencyAmount(currency:String) = {
    if(currency.split(poundSign).size >1) currency.split(poundSign)(1)
    else currency
  }

  def questionLabel(claim:Claim, labelKey:String)(implicit lang: Lang) = {
    labelForSelfEmployment(claim, lang, labelKey)
  }

  def questionLabelEmployment(claim:Claim, labelKey:String, jobID: String)(implicit lang: Lang) = {
    labelForEmployment(claim, lang, labelKey, jobID)
  }

  def questionLabelSelfEmployment(claim:Claim, labelKey:String)(implicit lang: Lang) = {
    labelForSelfEmployment(claim, lang, labelKey)
  }

  def encrypt[T](text:T) = DatatypeConverter.printBase64Binary((new  EncryptorAES).encrypt(stringify(text)))
  // = text - use when printing out test xml for the pdfService

  def decrypt(text: String) = (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(text))

  def createFormattedDate(date: String) : DayMonthYear = {
    DayMonthYear(DateTimeFormat.forPattern(datePattern).parseDateTime(date))
  }

  def createFormattedDateOptional(date: String) = {
    if (date.nonEmpty) {
      Some(DayMonthYear(DateTimeFormat.forPattern(datePattern).parseDateTime(date)))
    } else None
  }

  def createStringOptional(stringVal: String) = {
    if (stringVal.isEmpty)None
    else Some(stringVal)
  }

  def createAddressFromXml(xml: NodeSeq) = {
    val addressLines = (xml \ "Address" \ "Answer" \ "Line").map{_.text}
    addressLines.length match {
      case 2 => MultiLineAddress(lineOne = createStringOptional(addressLines.head),lineTwo = createStringOptional(addressLines.last))
      case _ => MultiLineAddress(lineOne = createStringOptional(addressLines.head),lineTwo = createStringOptional(addressLines(1)), lineThree = createStringOptional(addressLines.last))
    }
  }

  def createAddressOptionalFromXml(xml: NodeSeq) = {
    val address = (xml \ "Address" \ "Answer" \ "Line")
    address.isEmpty match {
      case false =>
        val addressLines = (xml \ "Address" \ "Answer" \ "Line").map {_.text}
        addressLines.length match {
          case 2 => Some(MultiLineAddress(lineOne = createStringOptional (addressLines.head), lineTwo = createStringOptional (addressLines.last)))
          case _ => Some(MultiLineAddress(lineOne = createStringOptional (addressLines.head), lineTwo = createStringOptional (addressLines (1) ), lineThree = createStringOptional (addressLines.last)))
        }
      case true => None
    }
  }

  def createPaymentFrequencyFromXml(payment: NodeSeq, searchElement: String) = {
    val frequency = (payment \\ searchElement)
    PaymentFrequency(frequency = (frequency \ "Answer").text, createStringOptional((frequency \ "Other" \ "Answer").text))
  }

  def createPaymentFrequencyOptionalFromXml(payment: NodeSeq, searchElement: String) = {
    val frequency = (payment \\ searchElement)
    frequency.isEmpty match {
      case false => Some(PaymentFrequency(frequency = (frequency \ "Answer").text, createStringOptional((frequency \ "Other" \ "Answer").text)))
      case true => None
    }
  }

  def createYesNoText(yesNoValue: String) = {
    yesNoValue.isEmpty match {
      case false if(yesNoValue.toLowerCase=="yes") => Mappings.yes
      case _ => Mappings.no
    }
  }

  def createYesNoTextOptional(yesNoValue: String) = {
    yesNoValue.isEmpty match {
      case false => Some(createYesNoText(yesNoValue))
      case true => None
    }
  }

  def createNationalInsuranceNumberOptional(xml: NodeSeq) = {
    val national = (xml \ "NationalInsuranceNumber" \ "Answer").text
    national.isEmpty match {
      case false =>
        val de = decrypt(national)
        Some(NationalInsuranceNumber(createStringOptional(decrypt(national))))
      case true => None
    }
  }
}

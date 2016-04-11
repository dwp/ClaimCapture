package xml.claim

import app.PaymentTypes
import controllers.mappings.Mappings
import models.domain._
import play.api.Play._
import play.api.i18n.{MMessages, MessagesApi}
import xml.XMLComponent
import xml.XMLHelper._
import scala.language.postfixOps
import scala.xml.NodeSeq

object Incomes extends XMLComponent {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val incomes = claim.questionGroup[YourIncomes].getOrElse(YourIncomes())
    val empAdditionalInfo = claim.questionGroup[EmploymentAdditionalInfo].getOrElse(EmploymentAdditionalInfo())
    val anyOtherPayments = incomes.yourIncome_none match {
      case Some(_) => "None"
      case _ => "Some"
    }

    <Incomes>
      {question(<Employed/>, "aboutYou_beenEmployedSince6MonthsBeforeClaim.label", incomes.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => DWPCAClaim.displayClaimDate(dmy - 6 months)), claimDate)}
      {question(<SelfEmployed/>, "aboutYou_beenSelfEmployedSince1WeekBeforeClaim.label", incomes.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 1 week}")(dmy => DWPCAClaim.displayClaimDate(dmy - 1 week)), claimDate)}
      {question(<SickPayment/>, "yourIncome.ssp", incomes.yourIncome_sickpay)}
      {question(<PatMatAdopPayment/>, "yourIncome.spmp", incomes.yourIncome_patmatadoppay)}
      {question(<FosteringPayment/>, "yourIncome.fostering", incomes.yourIncome_fostering)}
      {question(<DirectPayment/>, "yourIncome.direct", incomes.yourIncome_directpay)}
      {question(<AnyOtherPayment/>, "yourIncome.anyother", incomes.yourIncome_anyother)}
      {question(<NoOtherPayment/>, "yourIncome.none", incomes.yourIncome_none)}
      {question(<OtherPaymentQuestion/>, "yourIncome.otherIncome.label", anyOtherPayments, claimDate)}
      {Employment.xml(claim)}
      {SelfEmployment.xml(claim)}
      {if(!empAdditionalInfo.empAdditionalInfo.answer.isEmpty) questionOther(<EmploymentAdditionalInfo/>, "empAdditionalInfo.answer", empAdditionalInfo.empAdditionalInfo.answer, empAdditionalInfo.empAdditionalInfo.text)}
      {sickPayXml(claim)}
      {statPatMatAdoptPayXml(claim)}
      {fosteringAllowanceXml(claim)}
      {directPaymentXml(claim)}
      {otherPaymentsXml(claim)}
    </Incomes>
  }

  def sickPayXml(claim: Claim): NodeSeq = {
    val data = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val showXml = claim.questionGroup[YourIncomes].getOrElse(YourIncomes()).yourIncome_sickpay.getOrElse("").toLowerCase == "true"
    if (showXml) {
      <SickPay>
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay", data.whoPaidYouThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<HowOftenPaidThisPayOther/>, "howOftenPaidThisPayOther", data.howOftenPaidThisPayOther)}
      </SickPay>
    }
    else {
      NodeSeq.Empty
    }
  }

  def statPatMatAdoptPayXml(claim: Claim): NodeSeq = {
    val data = claim.questionGroup[StatutoryMaternityPaternityAdoptionPay].getOrElse(StatutoryMaternityPaternityAdoptionPay())
    val showXml = claim.questionGroup[YourIncomes].getOrElse(YourIncomes()).yourIncome_patmatadoppay.getOrElse("").toLowerCase == "true"
    val paymentType = data.paymentTypesForThisPay match{
      case PaymentTypes.MaternityPaternity => messagesApi("maternityPaternity")
      case PaymentTypes.Adoption => messagesApi("adoption")
      case _ => ""
    }
    if (showXml) {
      <StatutoryMaternityPaternityAdopt>
        {question(<PaymentType/>, "paymentTypesForThisPay", data.paymentTypesForThisPay)}
        {question(<PaymentTypesForThisPay/>, "paymentTypesForThisPay", paymentType)}
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay_paternityMaternityAdoption", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_paternityMaternityAdoption", data.whoPaidYouThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<HowOftenPaidThisPayOther/>, "howOftenPaidThisPayOther", data.howOftenPaidThisPayOther)}
      </StatutoryMaternityPaternityAdopt>
    }
    else {
      NodeSeq.Empty
    }
  }

  def fosteringAllowanceXml(claim: Claim): NodeSeq = {
    val data = claim.questionGroup[FosteringAllowance].getOrElse(FosteringAllowance())
    val showXml = claim.questionGroup[YourIncomes].getOrElse(YourIncomes()).yourIncome_fostering.getOrElse("").toLowerCase == "true"
    val paymentType = data.paymentTypesForThisPay match {
      case PaymentTypes.LocalAuthority => messagesApi("paymentTypeLocalAuthority")
      case PaymentTypes.FosteringAllowance => messagesApi("paymentTypeMainFostering")
      case PaymentTypes.Other => messagesApi("paymentTypeOther")
      case _ => ""
    }
    if (showXml) {
      <FosteringAllowance>
        {question(<PaymentType/>, "fosteringAllowancePay", data.paymentTypesForThisPay)}
        {question(<PaymentTypesForThisPay/>, "fosteringAllowancePay", paymentType)}
        {question(<PaymentTypesForThisPayOther/>, "fosteringAllowancePayOther", data.paymentTypesForThisPayOther)}
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay_fosteringAllowance", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_fosteringAllowance", data.whoPaidYouThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<HowOftenPaidThisPayOther/>, "howOftenPaidThisPayOther", data.howOftenPaidThisPayOther)}
      </FosteringAllowance>
    }
    else {
      NodeSeq.Empty
    }
  }

  def directPaymentXml(claim: Claim): NodeSeq = {
    val data = claim.questionGroup[DirectPayment].getOrElse(DirectPayment())
    val showXml = claim.questionGroup[YourIncomes].getOrElse(YourIncomes()).yourIncome_directpay.getOrElse("").toLowerCase == "true"
    if (showXml) {
      <DirectPay>
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay_directPayment", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_directPayment", data.whoPaidYouThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<HowOftenPaidThisPayOther/>, "howOftenPaidThisPayOther", data.howOftenPaidThisPayOther)}
      </DirectPay>
    }
    else {
      NodeSeq.Empty
    }
  }

  def otherPaymentsXml(claim: Claim): NodeSeq = {
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val data = claim.questionGroup[OtherPayments].getOrElse(OtherPayments())
    val showXml = claim.questionGroup[YourIncomes].getOrElse(YourIncomes()).yourIncome_anyother.getOrElse("").toLowerCase == "true"
    if (showXml) {
        {question(<OtherPaymentsInfo/>, "otherPaymentsInfo", data.otherPaymentsInfo, claimDate)}
    }
    else {
      NodeSeq.Empty
    }
  }

  def fromXml(xml: NodeSeq, claim: Claim): Claim = {
    SelfEmployment.fromXml(xml, Employment.fromXml(xml, claim)).update(createYourIncomesFromXml(xml))
      .update(createStatutorySickPayFromXml(xml))
      .update(createStatutoryPayFromXml(xml))
      .update(createFosteringAllowanceFromXml(xml))
      .update(createDirectPaymentFromXml(xml))
      .update(createOtherPaymentsFromXml(xml))
  }

  private def createStatutorySickPayFromXml(xmlNode: NodeSeq) = {
    val statutorySickPay = (xmlNode \\ "SickPay")
    if (!statutorySickPay.isEmpty) {
      models.domain.StatutorySickPay(
        stillBeingPaidThisPay = createYesNoText((statutorySickPay \\ "StillBeingPaidThisPay" \ "Answer").text),
        whenDidYouLastGetPaid = createFormattedDateOptional((statutorySickPay \\ "WhenDidYouLastGetPaid" \ "Answer").text),
        whoPaidYouThisPay = (statutorySickPay \\ "WhoPaidYouThisPay" \ "Answer").text,
        amountOfThisPay = (statutorySickPay \\ "AmountOfThisPay" \ "Answer").text,
        howOftenPaidThisPay = (statutorySickPay \\ "HowOftenPaidThisPay" \ "Answer").text,
        howOftenPaidThisPayOther = createStringOptional((statutorySickPay \\ "HowOftenPaidThisPayOther" \ "Answer").text)
      )
    } else models.domain.StatutorySickPay()
  }

  private def createStatutoryPayFromXml(xmlNode: NodeSeq) = {
    val statutoryPay = (xmlNode \\ "StatutoryMaternityPaternityAdopt")
    if (!statutoryPay.isEmpty) {
      models.domain.StatutoryMaternityPaternityAdoptionPay(
        paymentTypesForThisPay = (statutoryPay \\ "PaymentType" \ "Answer").text,
        stillBeingPaidThisPay = createYesNoText((statutoryPay \\ "StillBeingPaidThisPay" \ "Answer").text),
        whenDidYouLastGetPaid = createFormattedDateOptional((statutoryPay \\ "WhenDidYouLastGetPaid" \ "Answer").text),
        whoPaidYouThisPay = (statutoryPay \\ "WhoPaidYouThisPay" \ "Answer").text,
        amountOfThisPay = (statutoryPay \\ "AmountOfThisPay" \ "Answer").text,
        howOftenPaidThisPay = (statutoryPay \\ "HowOftenPaidThisPay" \ "Answer").text,
        howOftenPaidThisPayOther = createStringOptional((statutoryPay \\ "HowOftenPaidThisPayOther" \ "Answer").text)
      )
    } else models.domain.StatutoryMaternityPaternityAdoptionPay()
  }

  private def createFosteringAllowanceFromXml(xmlNode: NodeSeq) = {
    val fosteringAllowance = (xmlNode \\ "FosteringAllowance")
    if (!fosteringAllowance.isEmpty) {
      models.domain.FosteringAllowance(
        paymentTypesForThisPay = (fosteringAllowance \\ "PaymentType" \ "Answer").text,
        stillBeingPaidThisPay = createYesNoText((fosteringAllowance \\ "StillBeingPaidThisPay" \ "Answer").text),
        whenDidYouLastGetPaid = createFormattedDateOptional((fosteringAllowance \\ "WhenDidYouLastGetPaid" \ "Answer").text),
        whoPaidYouThisPay = (fosteringAllowance \\ "WhoPaidYouThisPay" \ "Answer").text,
        amountOfThisPay = (fosteringAllowance \\ "AmountOfThisPay" \ "Answer").text,
        howOftenPaidThisPay = (fosteringAllowance \\ "HowOftenPaidThisPay" \ "Answer").text,
        howOftenPaidThisPayOther = createStringOptional((fosteringAllowance \\ "HowOftenPaidThisPayOther" \ "Answer").text)
      )
    } else models.domain.FosteringAllowance()
  }

  private def createDirectPaymentFromXml(xmlNode: NodeSeq) = {
    val directPay = (xmlNode \\ "DirectPay")
    if (!directPay.isEmpty) {
      models.domain.DirectPayment(
        stillBeingPaidThisPay = createYesNoText((directPay \\ "StillBeingPaidThisPay" \ "Answer").text),
        whenDidYouLastGetPaid = createFormattedDateOptional((directPay \\ "WhenDidYouLastGetPaid" \ "Answer").text),
        whoPaidYouThisPay = (directPay \\ "WhoPaidYouThisPay" \ "Answer").text,
        amountOfThisPay = (directPay \\ "AmountOfThisPay" \ "Answer").text,
        howOftenPaidThisPay = (directPay \\ "HowOftenPaidThisPay" \ "Answer").text,
        howOftenPaidThisPayOther = createStringOptional((directPay \\ "HowOftenPaidThisPayOther" \ "Answer").text)
      )
    } else models.domain.DirectPayment()
  }

  private def createOtherPaymentsFromXml(xmlNode: NodeSeq) = {
    val otherPaymentsInfo = (xmlNode \\ "OtherPaymentsInfo")
    if (!otherPaymentsInfo.isEmpty) {
      models.domain.OtherPayments(
        otherPaymentsInfo = (otherPaymentsInfo \ "Answer").text
      )
    } else models.domain.OtherPayments()
  }

  private def createYourIncomesFromXml(xmlNode: NodeSeq) = {
    val selfEmployment = (xmlNode \\ "SelfEmployment")
    val jobDetails = (xmlNode \\ "Employment" \ "JobDetails")
    val statutorySickPay = (xmlNode \\ "SickPayment")
    val statutoryPay = (xmlNode \\ "PatMatAdopPayment")
    val fosteringAllowance = (xmlNode \\ "FosteringPayment")
    val directPayment = (xmlNode \\ "DirectPayment")
    val otherPayments = (xmlNode \\ "AnyOtherPayment")
    val noOtherPayments = (xmlNode \\ "NoOtherPayment")
    models.domain.YourIncomes(
      beenSelfEmployedSince1WeekBeforeClaim = selfEmployment.isEmpty match {
        case false => Mappings.yes
        case true => Mappings.no
      },
      beenEmployedSince6MonthsBeforeClaim = jobDetails.isEmpty match {
        case false => Mappings.yes
        case true => Mappings.no
      },
      yourIncome_sickpay = statutorySickPay.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      },
      yourIncome_patmatadoppay = statutoryPay.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      },
      yourIncome_fostering = fosteringAllowance.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      },
      yourIncome_directpay = directPayment.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      },
      yourIncome_anyother = otherPayments.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      },
      yourIncome_none = noOtherPayments.isEmpty match {
        case false => Mappings.someTrue
        case true => None
      }
    )
  }
}

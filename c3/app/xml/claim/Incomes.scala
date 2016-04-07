package xml.claim

import app.PaymentTypes
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
    <Incomes>
      {question(<Employed/>, "aboutYou_beenEmployedSince6MonthsBeforeClaim.label", incomes.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => DWPCAClaim.displayClaimDate(dmy - 6 months)), claimDate)}
      {question(<SelfEmployed/>, "aboutYou_beenSelfEmployedSince1WeekBeforeClaim.label", incomes.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 1 week}")(dmy => DWPCAClaim.displayClaimDate(dmy - 1 week)), claimDate)}
      {question(<SickPayment/>, "yourIncome.ssp", incomes.yourIncome_sickpay)}
      {question(<PatMatAdopPayment/>, "yourIncome.spmp", incomes.yourIncome_patmatadoppay)}
      {question(<FosteringPayment/>, "yourIncome.fostering", incomes.yourIncome_fostering)}
      {question(<DirectPayment/>, "yourIncome.direct", incomes.yourIncome_directpay)}
      {question(<AnyOtherPayment/>, "yourIncome.anyother", incomes.yourIncome_anyother)}
      {question(<NoOtherPayment/>, "yourIncome.none", incomes.yourIncome_none)}
      {Employment.xml(claim)}
      {SelfEmployment.xml(claim)}
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
    // claim.questionGroup[YourIncomes].getOrElse(YourIncomes()
    if (showXml) {
      <SickPay>
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay", data.whoPaidYouThisPay)}
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
        {question(<PaymentTypesForThisPay/>, "paymentTypesForThisPay", paymentType)}
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay_paternityMaternityAdoption", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_paternityMaternityAdoption", data.whoPaidYouThisPay)}
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
        {question(<PaymentTypesForThisPay/>, "fosteringAllowancePay", paymentType)}
        {question(<PaymentTypesForThisPayOther/>, "fosteringAllowancePayOther", data.paymentTypesForThisPayOther)}
        {question(<StillBeingPaidThisPay/>, "stillBeingPaidThisPay_fosteringAllowance", data.stillBeingPaidThisPay)}
        {question(<WhenDidYouLastGetPaid/>, "whenDidYouLastGetPaid", data.whenDidYouLastGetPaid)}
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_fosteringAllowance", data.whoPaidYouThisPay)}
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
        {question(<HowOftenPaidThisPay/>, "howOftenPaidThisPay", data.howOftenPaidThisPay)}
        {question(<AmountOfThisPay/>, "amountOfThisPay", currencyAmount(data.amountOfThisPay))}
        {question(<WhoPaidYouThisPay/>, "whoPaidYouThisPay_directPayment", data.whoPaidYouThisPay)}
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
}

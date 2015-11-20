package xml.claim

import controllers.mappings.Mappings
import models.domain._
import app.XMLValues._
import models.domain.Claim
import models.domain.ClaimUtils
import scala.xml.{Elem, NodeSeq}
import xml.XMLHelper._
import models.MultiLineAddress
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object EvidenceList {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def buildXml(claim: Claim) = {
    <EvidenceList>
      {evidence(claim)}
    </EvidenceList>
  }

  def evidence(claim: Claim): NodeSeq = {
    val employed = Employed.isEmployed(claim)
    val selfEmployed = models.domain.SelfEmployment.isSelfEmployed(claim)
    val receivesStatutorySickPay = OtherMoney.receivesStatutorySickPay(claim)
    val receivesOtherStatutoryPay = OtherMoney.receivesOtherStatutoryPay(claim)
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    val isEmail = claim.questionGroup[ContactDetails].getOrElse(ContactDetails()).wantsContactEmail.getOrElse("") == Mappings.yes
    val evidenceRequired = employed || selfEmployed || receivesStatutorySickPay || receivesOtherStatutoryPay

    val evidenceEmployedStatements = Seq(messagesApi("evidence.employment.lastPayslip", stringify(claimDate.dateOfClaim)), "evidence.employment.payslipsSinceClaimDate")
    val evidenceSelfEmployedStatements = Seq("evidence.selfEmployment.accounts")
    val evidencePensionStatements = Seq("evidence.pensionStatements")
    val evidenceStatutorySickPay = Seq("evidence.otherMoney.statutorySickPay")
    val evidenceOtherStatutoryPay = Seq("evidence.otherMoney.otherStatutoryPay")

    var nodes = NodeSeq.Empty
    if (evidenceRequired) {
      nodes ++= recepientAddress("address.send")
    }

    nodes ++= evidenceTitle("next")

    if (isEmail) nodes ++= evidenceTitle("email.claim.thankYou")

    if (evidenceRequired) nodes ++= evidenceTitle("claim.next.main")
    else nodes ++= evidenceTitle("claim.next.nodocuments.1")

    if (evidenceRequired) {
      val commonMessages = Seq("address.details")
      val employment = emptySeqIfFalse(employed,evidenceEmployedStatements)
      val selfEmployment = emptySeqIfFalse(selfEmployed,evidenceSelfEmployedStatements)
      val pension = emptySeqIfFalse(ClaimUtils.pensionStatementsRequired(claim),evidencePensionStatements)
      val statutorySickPay = emptySeqIfFalse(receivesStatutorySickPay, evidenceStatutorySickPay)
      val otherStatutoryPay = emptySeqIfFalse(receivesOtherStatutoryPay, evidenceOtherStatutoryPay)
      nodes ++= evidenceSection(true,"evidence.required",Seq("thankyou.send")++employment++selfEmployment++
        pension++statutorySickPay++otherStatutoryPay++commonMessages)
    }
    nodes
  }

  def emptySeqIfFalse[T](statement:Boolean,seq:Seq[T]):Seq[T] =
    statement match{
      case true => seq
      case false => Seq.empty[T]
    }

  def  sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  /**
   * This is used to display some common titles
   * @param titles
   * @return
   */
  def evidenceTitle(titles: Seq[String]):NodeSeq = {
    {titles map(evidenceTitle)}
  }

  def evidenceTitle(titleValue: String):Elem =
    {<Evidence>{title(titleValue)}</Evidence>}



  def evidenceSection (condition: Boolean, titleText: String, contents: Seq[String]): NodeSeq = {
    condition match {
      case true =>{
        <Evidence>
          {title(titleText)}
          {contents map(c => content(c))}
        </Evidence>
      }
      case _ => NodeSeq.Empty
    }
  }

  private def title(text: String): NodeSeq = {
    <Title>{messagesApi(text)}</Title>
  }

  private def content(text: String): NodeSeq = {
    <Content>{messagesApi(text)}</Content>
  }

  def recepientAddress(questionLabelCode: String):NodeSeq = {
    val address = MultiLineAddress(Some(messagesApi("s11.g5.help11")),Some(messagesApi("s11.g5.help12")),Some(messagesApi("s11.g5.help13")))
    postalAddressStructureRecipientAddress(questionLabelCode, address, Some(messagesApi("s11.g5.help14")))
  }
}

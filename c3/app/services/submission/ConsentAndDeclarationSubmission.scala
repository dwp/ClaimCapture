package services.submission

import models.domain._
import play.api.i18n.Messages

object ConsentAndDeclarationSubmission {

  private def getQuestionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]].get
  }
  private def questionGroup[T](claim: Claim, questionGroup: QuestionGroup) = {
    claim.questionGroup(questionGroup).asInstanceOf[Option[T]]
  }

  def buildConsentAndDeclaration(claim: Claim) = {
    val additionalInfo: AdditionalInfo  =   getQuestionGroup(claim,AdditionalInfo)
    val consent: Consent =                  getQuestionGroup(claim,Consent)
    val disclaimer: Disclaimer =            getQuestionGroup(claim,Disclaimer)
    val declaration: Declaration =          getQuestionGroup(claim,Declaration)

    ConsentAndDeclaration(additionalInfo,consent,disclaimer,declaration)
  }

  def buildOtherInformation(consent: ConsentAndDeclaration) = {

  }

  def buildDeclaration(cad: ConsentAndDeclaration,care: CareYouProvide) = {
    <Declaration>
      {(1 to 4).foreach( n => <TextLine>{Messages(s"declaration.${n}")}</TextLine>)}
      <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
      <TextLine>Do you agree to us obtaining information from any current or previous employer(s) you may have listed on this claim form?</TextLine>
      <TextLine>{cad.consent.informationFromEmployer}</TextLine>
      {cad.consent.informationFromEmployer match{
        case "yes" =>
        case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{cad.consent.why}</TextLine>
      }
      }
      <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this claim form?</TextLine>
      <TextLine>{cad.consent.informationFromPerson}</TextLine>
      {cad.consent.informationFromEmployer match{
        case "yes" =>
        case "no" => <TextLine>If you answered No please tell us why</TextLine> <TextLine>{cad.consent.whyPerson}</TextLine>
      }
      }
      <TextLine>This is my claim for Carer's Allowance.</TextLine>
      {(1 to 7).foreach( n => <TextLine>{Messages(s"disclaimer.${n}").replace("[[first name, middle name, surname]]",(Some(care.theirPersonalDetails).fold("")(m => m.firstName+" "+m.middleName.getOrElse("")+(if (m.middleName.isDefined) " " else "")+m.surname )))}</TextLine>)}
      <TextLine>If you live in Wales and would like to receive future communications in Welsh, please select this box.</TextLine>
      <TextLine>{cad.additionalInfo.welshCommunication}</TextLine>
      <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
      <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
    </Declaration>
  }
}

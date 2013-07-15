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
    val additionalInfo          = getQuestionGroup(claim,AdditionalInfo)
    val consent = getQuestionGroup(claim,Consent)

    ConsentAndDeclaration(additionalInfo,consent)
  }

  def buildOtherInformation(consent: ConsentAndDeclaration) = {

  }

  def buildDeclaration(consent: ConsentAndDeclaration) = {
    <Declaration>
      {(1 to 4).foreach( n => <TextLine>{Messages(s"declaration.${n}")}</TextLine>)}
    </Declaration>
    <Declaration>
      <TextLine>I declare</TextLine>
      <TextLine>that I have read the Carer's Allowance claim notes, and that the information I have given on this form is correct and complete as far as I know and believe.</TextLine>
      <TextLine>I understand</TextLine>
      <TextLine>that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.</TextLine>
      <TextLine>I understand</TextLine>
      <TextLine>that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.</TextLine>
      <TextLine>The Secretary of State has made directions that allow you to use this service to make a claim to benefit or send a notification about a change of circumstances by an electronic communication. You should read the directions.</TextLine>
      <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
      <TextLine>Do you agree to us obtaining information from any current or previous employer(s) you may have listed on this claim form?</TextLine>
      <TextLine>Yes</TextLine>
      <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this claim form?</TextLine>
      <TextLine>Yes</TextLine>
      <TextLine>If you have answered No to any of the above and you would like the Carer's Allowance Unit to know the reasons why, please set out those reasons in the "Other information" section displayed to the left of your screen.</TextLine>
      <TextLine>This is my claim for Carer's Allowance.</TextLine>
      <TextLine>Please note that any or all of the information in this claim form may be checked.</TextLine>
      <TextLine>I certify that the name that I have typed in below as my signature is a valid method of establishing that this is my claim for benefit and that I</TextLine>
      <TextLine>Mickey Mouse</TextLine>
      <TextLine>have read and understood all the statements on this page. I agree with all the statements on this page where agreement is indicated, apart from any statements that I do not agree with, where I have given my reasons why not in the "Other information" section.</TextLine>
      <TextLine>yes</TextLine>
      <TextLine>If you live in Wales and would like to receive future communications in Welsh, please select this box.</TextLine>
      <TextLine>no</TextLine>
      <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
      <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
    </Declaration>
  }
}

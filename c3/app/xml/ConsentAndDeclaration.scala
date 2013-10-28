package xml

import models.domain.{Claim, CircumstancesDeclaration}
import scala.xml.NodeSeq
import xml.XMLHelper._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

object ConsentAndDeclaration {
  def xml(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    <Declaration>
      I declare that the information I have given on this form is correct and complete as far as I know and believe.
      I understand that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.
       I understand that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.
      <TextLine/>
      Do you agree to us obtaining information from any other persons or organisations you may have listed on this form? {declaration.obtainInfoAgreement}
    </Declaration>
    <EvidenceList>
      <Evidence>XML Generated at: {DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now())}</Evidence>
      Consent and declaration
      <Evidence>Please tick this box to confirm that you understand and make the declarations above = {booleanStringToYesNo(declaration.confirm)}</Evidence>
      {if(declaration.obtainInfoAgreement == "no"){
        <Evidence>I don't agree to you obtaining information from any other persons or organisations because = {declaration.obtainInfoWhy.getOrElse("")}</Evidence>
      }}
    </EvidenceList>
  }
}
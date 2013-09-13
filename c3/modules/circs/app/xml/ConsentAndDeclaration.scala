package xml

import models.domain.{CircumstancesDeclaration, Circs}
import scala.xml.NodeSeq

import xml.XMLHelper._

/**
 * @author Jorge Migueis
 *         Date: 13/09/2013
 */
object ConsentAndDeclaration {

  def xml(circs :Circs):NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    <Declaration>
      <TextLine>I declare that the information I have given on this form is correct and complete as far as I know and believe.</TextLine>
      <TextLine>I understand that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.</TextLine>
      <TextLine> I understand that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.</TextLine>
      <TextLine/>
      <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this form? {declaration.obtainInfoAgreement}</TextLine>
    </Declaration>
    <EvidenceList> 
      <TextLine>Please tick this box to confirm that you understand and make the declarations above = {booleanStringToYesNo(declaration.confirm)}</TextLine>
    </EvidenceList>
  }

}

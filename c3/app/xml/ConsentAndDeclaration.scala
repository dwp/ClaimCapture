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
      <TextLine>I declare that the information I have given on this form is correct and complete as far as I know and believe.</TextLine>
      <TextLine>I understand that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.</TextLine>
      <TextLine> I understand that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.</TextLine>
      <TextLine/>
      <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this form? {declaration.obtainInfoAgreement}</TextLine>
    </Declaration>
    <EvidenceList>
      <TextLine>XML Generated at: {DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now())}</TextLine>
      {textSeparatorLine("Consent and declaration")}
      <TextLine>Please tick this box to confirm that you understand and make the declarations above = {booleanStringToYesNo(declaration.confirm)}</TextLine>
      {if(declaration.obtainInfoAgreement == "no"){
        <TextLine>I don't agree to you obtaining information from any other persons or organisations because = {declaration.obtainInfoWhy.getOrElse("")}</TextLine>
      }}
    </EvidenceList>
  }

  def textSeparatorLine(title: String) = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>
      {s"$padding$title$padding"}
    </TextLine>
  }
}
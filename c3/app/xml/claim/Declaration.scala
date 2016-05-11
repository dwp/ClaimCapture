package xml.claim

import app.ConfigProperties._
import controllers.mappings.Mappings
import models.domain._
import models.view.Navigation
import models.yesNo.YesNoWithText
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.{Lang, MMessages, MessagesApi}
import play.api.Play.current

import scala.xml.NodeSeq

object  Declaration extends XMLComponent {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val thirdParty = claim.questionGroup[models.domain.ThirdPartyDetails].getOrElse(models.domain.ThirdPartyDetails())

    <Declaration>
      <DeclarationStatement>
        <Content>{messagesApi("declaration.openingParagraph")}</Content>
        <Content>{messagesApi("declaration.correct")}</Content>
        {if(isOriginGB){<Content>{messagesApi("declaration.maycheck")}</Content>}}
        <Content>{messagesApi("declaration.overpayment")}</Content>
        <Content>{messagesApi("declaration.reportChanges.pdf")}</Content>
        <Content>{messagesApi("declaration.warning")}</Content>
      </DeclarationStatement>
      {question(<DeclarationQuestion/>,"thirdParty", yesNoText(thirdParty))}
      {question(<DeclarationQuestion/>,"agreement", "Yes")}
      {if(thirdParty.thirdParty == ThirdPartyDetails.noCarer){
        {question(<DeclarationNameOrg/>,"thirdParty.nameAndOrganisation", thirdParty.nameAndOrganisation.getOrElse(""))}
      }}
    </Declaration>
  }

  private def isOriginGB(): Boolean = {
    getProperty("origin.tag", "GB") match {
      case "GB" => true
      case _ => false
    }
  }

  private def yesNoText(thirdParty : ThirdPartyDetails): String = {
    thirdParty.thirdParty == ThirdPartyDetails.noCarer match {
      case true => Mappings.no
      case false => Mappings.yes
    }
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    implicit val navigation = Navigation()
    claim.update(createThirdPartyDetailsFromXml(xml)).update(createAdditionalInfoFromXml(xml)).copy(lang = createLanguageFromXml(xml))
  }

  private def createThirdPartyDetailsFromXml(xmlNode: NodeSeq) = {
    val declaration = (xmlNode \\ "Declaration")
    val declarationNameOrg = (declaration \ "DeclarationNameOrg" \ "Answer").text
    ThirdPartyDetails(declarationNameOrg.isEmpty match { case false => ThirdPartyDetails.noCarer case true => ThirdPartyDetails.yesCarer }, createStringOptional(declarationNameOrg))
  }

  private def createAdditionalInfoFromXml(xmlNode: NodeSeq) = {
    val otherInformation = (xmlNode \\ "OtherInformation")
    models.domain.AdditionalInfo(
      anythingElse = YesNoWithText(
        answer = createYesNoText((otherInformation \ "AdditionalInformation" \ "Answer").text),
        text = createStringOptional((otherInformation \ "AdditionalInformation" \ "Why" \ "Answer").text)),
      welshCommunication = createYesNoText((otherInformation \ "WelshCommunication" \ "Answer").text)
    )
  }

  private def createLanguageFromXml(xml: NodeSeq) = {
    (xml \\ "DWPCATransaction" \ "LanguageUsed").text match {
      case "Welsh" => Some(Lang("cy"))
      case _ => Some(Lang("en"))
    }
  }
}

package xml.claim

import controllers.mappings.Mappings
import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object  Declaration extends XMLComponent {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val thirdParty = claim.questionGroup[models.domain.ThirdPartyDetails].getOrElse(models.domain.ThirdPartyDetails())

    <Declaration>
      <DeclarationStatement>
        <Content>{messagesApi("declaration.openingParagraph")}</Content>
        <Content>{messagesApi("declaration.correct")}</Content>
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

  def yesNoText(thirdParty : ThirdPartyDetails): String = {
    thirdParty.thirdParty == ThirdPartyDetails.noCarer match {
      case true => Mappings.no
      case false => Mappings.yes
    }
  }
}

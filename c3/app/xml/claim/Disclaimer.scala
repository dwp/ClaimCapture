package xml.claim

import models.domain._
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current
import app.ConfigProperties._

import scala.xml.NodeSeq

object  Disclaimer extends XMLComponent{
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val disclaimer = claim.questionGroup[models.domain.Disclaimer].getOrElse(models.domain.Disclaimer())

    <Disclaimer>
      <DisclaimerStatement>
        <Title>This is my claim for Carer's Allowance.</Title>
        <Content>{messagesApi("disclaimer.1")}</Content>
        <Content>{messagesApi("disclaimer.2")}</Content>
        <Content>{messagesApi("disclaimer.3")}</Content>
        {if(isOriginGB){<Content>{messagesApi("disclaimer.4", "", "")}</Content>}}
        <Content>{messagesApi("disclaimer.5", "", "")}</Content>
        <Content>{messagesApi("disclaimer.6", "", "")}</Content>
      </DisclaimerStatement>
      {question(<DisclaimerQuestion/>,"read", disclaimer.read)}
    </Disclaimer>
  }

  private def isOriginGB(): Boolean = {
    getProperty("origin.tag", "GB") match {
      case "GB" => true
      case _ => false
    }
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val disclaimers = (xml \\ "Disclaimer")
    val disclaimer = models.domain.Disclaimer(read = createYesNoText((disclaimers \ "DisclaimerQuestion" \ "Answer").text))
    claim.update(disclaimer)
  }
}

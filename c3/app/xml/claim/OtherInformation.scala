package xml.claim

import models.domain._
import play.api.Play._
import play.api.i18n.{MMessages, MessagesApi}
import xml.XMLComponent
import xml.XMLHelper._
import scala.language.postfixOps

object OtherInformation extends XMLComponent {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {
    val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
    <OtherInformation>
      {question(<WelshCommunication/>,"welshCommunication",additionalInfo.welshCommunication)}
      {questionWhy(<AdditionalInformation/>,"anythingElse.answer", additionalInfo.anythingElse.answer, additionalInfo.anythingElse.text,"anythingElse.text")}
    </OtherInformation>
  }
}

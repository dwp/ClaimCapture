package xml.claim

import models.domain._
import models.yesNo.YesNoWithText
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import controllers.mappings.Mappings
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

object Residency extends XMLComponent{
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def xml(claim: Claim) = {

    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))
    val nationality = nationalityAndResidency.nationality match{
      case NationalityAndResidency.anothercountry=> messagesApi("label.anothercountry")
      case _ => messagesApi("label.british")
    }

    <Residency>
      {question(<Nationality/>, "nationality", nationality)}
      {nationalityAndResidency.nationality match {
        case NationalityAndResidency.anothercountry => question(<ActualNationality/>, "actualnationality.text.label", nationalityAndResidency.actualnationality)
        case _ => NodeSeq.Empty
      }}

      {question(<AlwaysLivedInUK/>, "alwaysLivedInUK", nationalityAndResidency.alwaysLivedInUK)}
      {question(<LiveInUKNow/>, "liveInUKNow", nationalityAndResidency.liveInUKNow)}
      {question(<ArrivedInUK/>, "arrivedInUK", nationalityAndResidency.arrivedInUK)}
      {question(<ArrivedInUKDate/>, "arrivedInUKDate", nationalityAndResidency.arrivedInUKDate)}
      {question(<ArrivedInUKFrom/>, "arrivedInUKFrom", nationalityAndResidency.arrivedInUKFrom)}
      {question(<TimeOutsideGBLast3Years/>, "trip52weeks", nationalityAndResidency.trip52weeks)}
      {question(<TripDetails/>, "tripDetails", nationalityAndResidency.tripDetails)}
    </Residency>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createNationalityFromXml(xml))
  }

  private def createNationalityFromXml(xml: NodeSeq) = {
    val residency = (xml \\ "Residency")
    models.domain.NationalityAndResidency(
      nationality = (residency \ "Nationality" \ "Answer").text,
      actualnationality = createStringOptional((residency \ "ActualNationality" \ "Answer").text),
      alwaysLivedInUK = createYesNoText((residency \ "AlwaysLivedInUK" \ "Answer").text),
      liveInUKNow = createYesNoTextOptional((residency \ "LiveInUKNow" \ "Answer").text),
      arrivedInUK = createStringOptional((residency \ "ArrivedInUK" \ "Answer").text),
      arrivedInUKDate = createFormattedDateOptional((residency \ "ArrivedInUKDate" \ "Answer").text),
      arrivedInUKFrom = createStringOptional((residency \ "ArrivedInUKFrom" \ "Answer").text),
      trip52weeks = createYesNoText((residency \ "TimeOutsideGBLast3Years" \ "Answer").text),
      tripDetails = createStringOptional((residency \ "TripDetails" \ "Answer").text)
    )
  }
}

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
    val timeOutsideGBLastThreeYears = claim.questionGroup[AbroadForMoreThan52Weeks].getOrElse(AbroadForMoreThan52Weeks())

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

      {question(<NormallyLiveInGB/>, "resideInUK.answer", nationalityAndResidency.resideInUK.answer)}

      {nationalityAndResidency.resideInUK.answer match {
        case Mappings.no => question(<CountryNormallyLive/>, "resideInUK.text.label", nationalityAndResidency.resideInUK.text)
        case _ => NodeSeq.Empty
      }}

      <PeriodAbroad>
        {question(<TimeOutsideGBLast3Years/>, "52Weeks.label", timeOutsideGBLastThreeYears.anyTrips)}
        {question(<TripsDetails/>, "tripDetails", timeOutsideGBLastThreeYears.tripDetails)}
      </PeriodAbroad>

    </Residency>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    claim.update(createNationalityFromXml(xml)).update(createPeriodAbroadFromXml(xml))
  }

  private def createNationalityFromXml(xml: NodeSeq) = {
    val nationality = (xml \\ "Residency")
    models.domain.NationalityAndResidency(
      nationality = (nationality \ "Nationality" \ "Answer").text,
      actualnationality = createStringOptional((nationality \ "ActualNationality" \ "Answer").text),
      resideInUK = YesNoWithText(createYesNoText((nationality \ "NormallyLiveInGB" \ "Answer").text), createStringOptional((nationality \ "CountryNormallyLive" \ "Answer").text))
    )
  }

  private def createPeriodAbroadFromXml(xml: NodeSeq) = {
    val period = (xml \\ "PeriodAbroad")
    models.domain.AbroadForMoreThan52Weeks(
      anyTrips = createYesNoText((period \ "TimeOutsideGBLast3Years" \ "Answer").text),
      tripDetails = createStringOptional((period \ "TripsDetails" \ "Answer").text)
    )
  }
}

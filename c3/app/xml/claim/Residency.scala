package xml.claim

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import models.domain.Claim
import scala.Some

object Residency extends XMLComponent{

  def xml(claim: Claim) = {
    val livesInGB = claim.questionGroup[LivesInGB]
    val yourDetailsOption = claim.questionGroup[YourDetails]
    val tripsOption = claim.questionGroup[Trips]
    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())

    <Residency>
      { livesInGB match {
          case Some(n) => question(<NormallyLiveInGB/>, "liveInUK.answer", n.answerYesNo)
          case _ => NodeSeq.Empty
        }
      }
      {claim.questionGroup[NationalityAndResidency] match {
      case Some(normalResidence) =>
        question(<CountryNormallyLive/>, "liveInUK.whereDoYouLive", normalResidence.resideInUK.text)

      case _ => NodeSeq.Empty
    }}
      {if (yourDetailsOption.isDefined) <Nationality>{nationalityAndResidency.nationality}</Nationality>}
      {periodAbroadLastYear(tripsOption)}
    </Residency>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod) = {
      <PeriodAbroad>
        <Period>
          {question(<DateFrom/>, "start.trip", trip.start)}
          {question(<DateTo/>, "end.trip", trip.end)}
        </Period>
        {question(<Reason/>, "why", trip.why)}
        {question(<Country/>, "where", trip.where)}
      </PeriodAbroad>
    }
  }
}
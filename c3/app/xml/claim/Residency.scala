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

    <Residency>
      { livesInGB match {
          case Some(n) => question(<NormallyLiveInGB/>, "liveInUK.answer", n.answerYesNo)
          case _ => NodeSeq.Empty
        }
      }
      {claim.questionGroup[NormalResidenceAndCurrentLocation] match {
      case Some(normalResidence) =>
        question(<CountryNormallyLive/>, "liveInUK.whereDoYouLive", normalResidence.whereDoYouLive.text)

      case _ => NodeSeq.Empty
    }}
      {if (yourDetailsOption.isDefined) <Nationality>{yourDetailsOption.get.nationality}</Nationality>}
      {question(<TimeOutsideGBLast3Years/>, "anyTrips", trips.fourWeeksTrips.size > 0)}
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

    {for {fourWeeksTrip <- trips.fourWeeksTrips} yield xml(fourWeeksTrip)}
  }
}
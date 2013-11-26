package xml.claim

import app.XMLValues
import app.XMLValues._
import models.domain._
import models.yesNo.YesNoWithDate
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.domain.Claim
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
      case Some(normalResidence) => {
          normalResidence.whereDoYouLive.text match {
          case Some(n) => question(<CountryNormallyLive/>, "liveInUK.whereDoYouLive", n)
          case None => question(<CountryNormallyLive/>, "liveInUK.whereDoYouLive", NotAsked)
        }
      }
      case _ => NodeSeq.Empty
    }}

      <Nationality>{if (yourDetailsOption.isDefined)yourDetailsOption.get.nationality}</Nationality>

      {question(<TimeOutsideGBLast3Years/>, "anyTrips", booleanToYesNo(trips.fourWeeksTrips.size > 0))}

      <!--<InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>-->
      {periodAbroadLastYear(tripsOption)}
      <!--{otherNationality(claim)}-->
      <!--{periodAbroadDuringCare(tripsOption)}-->
    </Residency>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod) = {
      <PeriodAbroad>
        <Period>
          {question(<DateFrom/>, "start.trip", trip.start.`dd-MM-yyyy`)}
          {question(<DateTo/>, "end.trip", trip.end.`dd-MM-yyyy`)}
        </Period>
        {question(<Reason/>, "why", trip.why)}
        {question(<Country/>, "where", trip.where)}
      </PeriodAbroad>
    }

    {for {fourWeeksTrip <- trips.fourWeeksTrips} yield xml(fourWeeksTrip)}
  }

  def otherNationality(claim:Claim) = {
    val timeOutsideUKOption = claim.questionGroup[TimeOutsideUK]
    val timeOutsideUK = timeOutsideUKOption.getOrElse(TimeOutsideUK())
    val currentlyLivingInUK = timeOutsideUK.livingInUK.answer == yes
    if(currentlyLivingInUK) {
      val goBack = timeOutsideUK.livingInUK.goBack.getOrElse(YesNoWithDate("", None))
      <OtherNationality>
        <EUEEASwissNationalChildren/>
        <DateArrivedInGreatBritain>{NotAsked}</DateArrivedInGreatBritain>
        <CountryArrivedFrom>{timeOutsideUK.livingInUK.text.orNull}</CountryArrivedFrom>
        <IntendToReturn>{goBack.answer}</IntendToReturn>
        <DateReturn>{stringify(goBack.date)}</DateReturn>
        <VisaReferenceNumber>{NotAsked}</VisaReferenceNumber>
      </OtherNationality>

    } else NodeSeq.Empty
  }
}
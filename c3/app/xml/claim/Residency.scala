package xml.claim

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent
import models.domain.Claim
import controllers.Mappings

object Residency extends XMLComponent{

  def xml(claim: Claim) = {

    val tripsOption = claim.questionGroup[Trips]
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())

    <Residency>
      {question(<NormallyLiveInGB/>, "resideInUK.answer", nationalityAndResidency.resideInUK.answer)}

      {nationalityAndResidency.resideInUK.answer match {
        case Mappings.no => question(<CountryNormallyLive/>, "resideInUK.text.label", nationalityAndResidency.resideInUK.text)
        case _ => NodeSeq.Empty
      }}

      {question(<Nationality/>, "nationality", nationalityAndResidency.nationality)}

      {periodAbroadLastYear(tripsOption, claim)}

    </Residency>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips], claim: Claim) = {
    val trips = tripsOption.getOrElse(Trips())

    def fiftyWeeksLabel (label:String, answer:Boolean) = {
      question(<TimeOutsideGBLast3Years/>, label, answer, claim.dateOfClaim.get.`dd/MM/yyyy`)
    }

    def xmlNoTrip(noTrips:Boolean) = {
      <PeriodAbroad>
        {if(noTrips){
          {fiftyWeeksLabel("52Weeks.label", false)}
        }else {
          {fiftyWeeksLabel("52Weeks.more.label", false)}
        }}
      </PeriodAbroad>
    }

    def xml(trip: TripPeriod, index:Int) = {
      <PeriodAbroad>
        {index > 0 match {
          case true =>  fiftyWeeksLabel("52Weeks.more.label", true)
          case false => fiftyWeeksLabel("52Weeks.label", true)
        }}
        <Period>
          {question(<DateFrom/>, "start.trip", trip.start)}
          {question(<DateTo/>, "end.trip", trip.end)}
         </Period>
        {if(trip.why.isDefined){
          {questionOther(<Reason/>, "why", trip.why.get.reason, trip.why.get.other)}
        }}
        {question(<Country/>, "where", trip.where)}
        {question(<CareePresent/>, "personWithYou", trip.personWithYou)}
      </PeriodAbroad>
    }

    trips.fiftyTwoWeeksTrips.size == 0 match {
      case true => xmlNoTrip(true)
      case false => {for ((fiftyTwoWeeksTrip, index) <- trips.fiftyTwoWeeksTrips.zipWithIndex) yield xml(fiftyTwoWeeksTrip, index)} ++ xmlNoTrip(false)
    }
  }
}
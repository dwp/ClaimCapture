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

      <Nationality>{nationalityAndResidency.nationality}</Nationality>

      {periodAbroadLastYear(tripsOption, claim)}

    </Residency>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips], claim: Claim) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod, index:Int) = {
      <PeriodAbroad>
        {index > 0 match {
          case true =>  question(<TimeOutsideGBLast3Years/>, "52Weeks.more.label", trips.fiftyTwoWeeksTrips.size > 0, claim.dateOfClaim.get.`dd/MM/yyyy`)
          case false => question(<TimeOutsideGBLast3Years/>, "52Weeks.label", trips.fiftyTwoWeeksTrips.size > 0, claim.dateOfClaim.get.`dd/MM/yyyy`)
        }}
        <Period>
          {question(<DateFrom/>, "start.trip", trip.start)}
          {question(<DateTo/>, "end.trip", trip.end)}
         </Period>
        {questionOther(<Reason/>, "why", trip.why.get.reason, trip.why.get.other)}
        {question(<Country/>, "where", trip.where)}
        {question(<CareePresent/>, "personWithYou", trip.personWithYou)}
      </PeriodAbroad>
    }
    {for ((fiftyTwoWeeksTrip, index) <- trips.fiftyTwoWeeksTrips.zipWithIndex) yield xml(fiftyTwoWeeksTrip, index)}
  }
}
package xml

import models.domain._
import XMLHelper._
import models.yesNo.YesNoWithText
import controllers.Mappings.no
import controllers.Mappings.yes

object Residence {

  def xml(claim:Claim) = {
    val yourDetailsOption = questionGroup[YourDetails](claim, YourDetails)
    val normalResidenceOption = questionGroup[NormalResidenceAndCurrentLocation](claim, NormalResidenceAndCurrentLocation)
    val tripsOption = questionGroup[Trips](claim, Trips)

    <Residency>
      <Nationality>{if(yourDetailsOption.isDefined)yourDetailsOption.get.nationality}</Nationality>
      <EUEEASwissNational>{yes}</EUEEASwissNational>
      <CountryNormallyLive>{yes}</CountryNormallyLive>
      <CountryNormallyLiveOther>{if (normalResidenceOption.isDefined) normalResidenceOption.get.whereDoYouLive.text.orNull}</CountryNormallyLiveOther>
      {inGreatBritainNow(normalResidenceOption)}
      <InGreatBritain26Weeks>{yes}</InGreatBritain26Weeks>
      {periodAbroadLastYear(tripsOption)}
      <BritishOverseasPassport>{yes}</BritishOverseasPassport>
      <OutOfGreatBritain>{yes}</OutOfGreatBritain>
      {periodAbroadDuringCare(tripsOption)}
    </Residency>
  }

  def inGreatBritainNow(normalResidenceOption: Option[NormalResidenceAndCurrentLocation]) = {
    val normalResidence = normalResidenceOption.getOrElse(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText("", None), inGBNow = no))
    <InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>
  }

  def periodAbroadLastYear(tripsOption:Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip:TripPeriod) = {
      <PeriodAbroadLastYear>
        <Period>
          <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
          <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
        </Period>
        <Reason>{trip.why.orNull}</Reason>
        <Country>{trip.where}</Country>
      </PeriodAbroadLastYear>
    }

    {for {fourWeeksTrip <- trips.fourWeeksTrips} yield xml(fourWeeksTrip)}

  }

  def periodAbroadDuringCare(tripsOption:Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip:TripPeriod) = {
      <PeriodAbroadDuringCare>
        <Period>
          <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
          <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
        </Period>
        <Reason>{trip.why.orNull}</Reason>
      </PeriodAbroadDuringCare>
    }

    {for {fiftyTwoWeeksTrip <- trips.fiftyTwoWeeksTrips} yield xml(fiftyTwoWeeksTrip)}
  }

}

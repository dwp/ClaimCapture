package services.submission

import models.domain._
import services.submission.XMLHelper._
import models.yesNo.YesNoWithText
import controllers.Mappings.no
import controllers.Mappings.yes

object TimeSpentAbroadSubmission {

  def xml(claim:Claim) = {
    val aboutYouSection:Section = claim.section(AboutYou)
    val yourDetailsOption = questionGroup[YourDetails](aboutYouSection, YourDetails)

    val timeSpentAbroadSection:Section = claim.section(TimeSpentAbroad)
    val normalResidenceOption = questionGroup[NormalResidenceAndCurrentLocation](timeSpentAbroadSection, NormalResidenceAndCurrentLocation)
    val tripsOption = questionGroup[Trips](timeSpentAbroadSection, Trips)

    <Residency>
      <Nationality>{if(yourDetailsOption.isDefined)yourDetailsOption.get.nationality}</Nationality>
      <EUEEASwissNational>yes</EUEEASwissNational>
      <CountryNormallyLive>yes</CountryNormallyLive>
      <CountryNormallyLiveOther>{if (normalResidenceOption.isDefined) normalResidenceOption.get.whereDoYouLive.text}</CountryNormallyLiveOther>
      {inGreatBritainNow(normalResidenceOption)}
      <InGreatBritain26Weeks>yes</InGreatBritain26Weeks>
      {periodAbroadLastYear(tripsOption)}
      <BritishOverseasPassport>no</BritishOverseasPassport>
      <OutOfGreatBritain>no</OutOfGreatBritain>
      {periodAbroadDuringCare(tripsOption)}
    </Residency>
  }

  def inGreatBritainNow(normalResidenceOption: Option[NormalResidenceAndCurrentLocation]) = {
    val normalResidence = normalResidenceOption.getOrElse(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText("", None), inGBNow = no))
    <InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>
  }

  def periodAbroadLastYear(tripsOption:Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def periodAbroadLastYearStructure(trip:TripPeriod) = {
      <Period>
        <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
        <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
      </Period>
      <Reason>{trip.why.orNull}</Reason>
      <Country>{trip.where}</Country>
    }

    if(!trips.fourWeeksTrips.isEmpty) {
      <PeriodAbroadLastYear>
        {for {fourWeeksTrip <- trips.fourWeeksTrips} yield periodAbroadLastYearStructure(fourWeeksTrip)}
      </PeriodAbroadLastYear>
    }
  }

  def periodAbroadDuringCare(tripsOption:Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def periodAbroadDuringCareStructure(trip:TripPeriod) = {
      <Period>
        <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
        <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
      </Period>
      <Reason>{trip.why.orNull}</Reason>
    }

    if(!trips.fiftyTwoWeeksTrips.isEmpty) {
      <PeriodAbroadDuringCare>
        {for {fiftyTwoWeeksTrip <- trips.fiftyTwoWeeksTrips} yield periodAbroadDuringCareStructure(fiftyTwoWeeksTrip)}
      </PeriodAbroadDuringCare>
    }
  }

}

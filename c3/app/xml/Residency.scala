package xml

import app.XMLValues._
import models.domain._
import models.yesNo.YesNoWithDate
import scala.xml.NodeSeq
import xml.XMLHelper.stringify

object Residency {

  def xml(claim: Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val normalResidence = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    val tripsOption = claim.questionGroup[Trips]

    <Residency>
      <Nationality>{nationalityAndResidency.nationality}</Nationality>
      <EUEEASwissNational>{NotAsked}</EUEEASwissNational>
      <CountryNormallyLive>{normalResidence.whereDoYouLive.text.getOrElse(NotAsked)}</CountryNormallyLive>
      <CountryNormallyLiveOther>{NotAsked}</CountryNormallyLiveOther>
      <InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>
      <InGreatBritain26Weeks>{NotAsked}</InGreatBritain26Weeks>
      {periodAbroadLastYear(tripsOption)}
      <BritishOverseasPassport>{NotAsked}</BritishOverseasPassport>
      {otherNationality(claim)}
      <OutOfGreatBritain>{NotAsked}</OutOfGreatBritain>
      {periodAbroadDuringCare(tripsOption)}
    </Residency>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod) = {
      <PeriodAbroadLastYear>
        <Period>
          <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
          <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
        </Period>
        <Reason>{trip.why}</Reason>
        <Country>{trip.where}</Country>
      </PeriodAbroadLastYear>
    }

    {for {fourWeeksTrip <- trips.fourWeeksTrips} yield xml(fourWeeksTrip)}
  }

  def otherNationality(claim:Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val resideInUK = nationalityAndResidency.resideInUK.answer == yes
    if(!resideInUK) {
      <OtherNationality>
        <EUEEASwissNationalChildren/>
        <DateArrivedInGreatBritain>{NotAsked}</DateArrivedInGreatBritain>
        <CountryArrivedFrom>{nationalityAndResidency.resideInUK.text.orNull}</CountryArrivedFrom>
        <IntendToReturn>{/** TODO: Make sure this is OK in the future **/}{NotAsked}</IntendToReturn>
        <DateReturn>{/** TODO: Make sure this is OK in the future **/}{NotAsked}</DateReturn>
        <VisaReferenceNumber>{NotAsked}</VisaReferenceNumber>
      </OtherNationality>

    } else NodeSeq.Empty
  }

  def periodAbroadDuringCare(tripsOption: Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod) = {
      <PeriodAbroadDuringCare>
        <Period>
          <DateFrom>{trip.start.`yyyy-MM-dd`}</DateFrom>
          <DateTo>{trip.end.`yyyy-MM-dd`}</DateTo>
        </Period>
        <Reason>{trip.why}</Reason>
      </PeriodAbroadDuringCare>
    }

    {for {fiftyTwoWeeksTrip <- trips.fiftyTwoWeeksTrips} yield xml(fiftyTwoWeeksTrip)}
  }
}
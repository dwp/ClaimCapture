package xml

import models.domain._
import models.yesNo.{YesNoWithDate, YesNoWithText}
import controllers.Mappings.no
import controllers.Mappings.yes
import scala.xml.NodeSeq
import xml.XMLHelper.stringify

object Residency {

  def xml(claim: Claim) = {
    val yourDetailsOption = claim.questionGroup[YourDetails]
    val normalResidenceOption = claim.questionGroup[NormalResidenceAndCurrentLocation]
    val tripsOption = claim.questionGroup[Trips]

    <Residency>
      <Nationality>{if (yourDetailsOption.isDefined)yourDetailsOption.get.nationality}</Nationality>
      <EUEEASwissNational>Not asked</EUEEASwissNational>
      <CountryNormallyLive>{yes}</CountryNormallyLive>
      <CountryNormallyLiveOther>{if (normalResidenceOption.isDefined) normalResidenceOption.get.whereDoYouLive.text.orNull}</CountryNormallyLiveOther>
      {inGreatBritainNow(normalResidenceOption)}
      <InGreatBritain26Weeks>Not asked</InGreatBritain26Weeks>
      {periodAbroadLastYear(tripsOption)}
      <BritishOverseasPassport>Not asked</BritishOverseasPassport>
      {otherNationality(claim)}
      <OutOfGreatBritain>{yes}</OutOfGreatBritain>
      {periodAbroadDuringCare(tripsOption)}
    </Residency>
  }

  def inGreatBritainNow(normalResidenceOption: Option[NormalResidenceAndCurrentLocation]) = {
    val normalResidence = normalResidenceOption.getOrElse(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText("", None), inGBNow = no))

    <InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>
  }

  def periodAbroadLastYear(tripsOption: Option[Trips]) = {
    val trips = tripsOption.getOrElse(Trips())

    def xml(trip: TripPeriod) = {
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

  def otherNationality(claim:Claim) = {
    val yourDetailsOption = claim.questionGroup[YourDetails]
    val yourDetails = yourDetailsOption.getOrElse(YourDetails())
    val hasLivedAbroad = yourDetails.alwaysLivedUK == no

    if(hasLivedAbroad) {
      val timeOutsideUKOption = claim.questionGroup[TimeOutsideUK]
      val timeOutsideUK = timeOutsideUKOption.getOrElse(TimeOutsideUK())
      val goBack = timeOutsideUK.livingInUK.goBack.getOrElse(YesNoWithDate("", None))
      <OtherNationality>
        <EUEEASwissNationalChildren/>
        <DateArrivedInGreatBritain>{stringify(timeOutsideUK.livingInUK.date)}</DateArrivedInGreatBritain>
        <CountryArrivedFrom>{timeOutsideUK.livingInUK.text.orNull}</CountryArrivedFrom>
        <IntendToReturn>{goBack.answer}</IntendToReturn>
        <DateReturn>{stringify(goBack.date)}</DateReturn>
        <VisaReferenceNumber>{timeOutsideUK.visaReference.orNull}</VisaReferenceNumber>
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
        <Reason>{trip.why.orNull}</Reason>
      </PeriodAbroadDuringCare>
    }

    {for {fiftyTwoWeeksTrip <- trips.fiftyTwoWeeksTrips} yield xml(fiftyTwoWeeksTrip)}
  }
}
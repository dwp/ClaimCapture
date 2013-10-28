package xml

import app.XMLValues
import app.XMLValues._
import models.domain._
import models.yesNo.YesNoWithDate
import scala.xml.NodeSeq
import xml.XMLHelper.stringify
import models.domain.Claim

object Residency {

  def xml(claim: Claim) = {
    val livesInGB = claim.questionGroup[LivesInGB]
    val yourDetailsOption = claim.questionGroup[YourDetails]
    val normalResidence = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    val tripsOption = claim.questionGroup[Trips]
    val trips = claim.questionGroup[Trips].getOrElse(Trips())

    <Residency>

      <NormallyLiveInGB>
        <QuestionLabel>NormallyLiveInGB?</QuestionLabel>
        <Answer>{livesInGB match {
          case Some(n) => n.answerYesNo match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }
          case _ => NodeSeq.Empty
        }}</Answer>
      </NormallyLiveInGB>

      <CountryNormallyLive>{normalResidence.whereDoYouLive.text.getOrElse(NotAsked)}</CountryNormallyLive>
      <Nationality>{if (yourDetailsOption.isDefined)yourDetailsOption.get.nationality}</Nationality>

      <TimeOutsideGBLast3Years>
        <QuestionLabel>TimeOutsideGBLast3Years?</QuestionLabel>
        <Answer>{(trips.fourWeeksTrips.size > 0) match {
          case true => XMLValues.Yes
          case false => XMLValues.No
        }}</Answer>
      </TimeOutsideGBLast3Years>

      <!--<InGreatBritainNow>{normalResidence.inGBNow}</InGreatBritainNow>-->
      {periodAbroadLastYear(tripsOption)}
      {otherNationality(claim)}
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
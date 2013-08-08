package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.yesNo.{YesNoWithDate, YesNoWithText}
import controllers.Mappings
import models.{LivingInUK, DayMonthYear}
import Mappings.{yes, no}

class ResidencySpec extends Specification with Tags {

  val yourDetails = YourDetails(nationality = "Dutch", alwaysLivedUK = no)

  val normalResidence = NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = yes, text = Some("UK")), inGBNow = yes)

  val abroadForMoreThan4Weeks = AbroadForMoreThan4Weeks(anyTrips = no)

  val startDate = DayMonthYear(day = Some(3), month = Some(4), year = Some(2013))

  val endDate = DayMonthYear(day = Some(3), month = Some(5), year = Some(2013))

  val netherlands = "Netherlands"

  val holidayOption = Some("Holiday")

  val fourWeekTrip1 = Trip(id = "four-one", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fourWeekTrip2 = Trip(id = "four-two", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fiftyTwoWeeksTrip1 = Trip(id = "fiftyTwo-one", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fiftyTwoWeeksTrip2 = Trip(id = "fiftyTwo-two", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val trips = Trips(fourWeeksTrips = List(fourWeekTrip1, fourWeekTrip1), fiftyTwoWeeksTrips = List(fiftyTwoWeeksTrip1, fiftyTwoWeeksTrip2))

  "Residency" should {

    "generate xml when data is present" in {
      val claim = Claim().update(yourDetails)
        .update(normalResidence)
        .update(abroadForMoreThan4Weeks)
        .update(trips).update(TimeOutsideUK(visaReference = Some("visaReference")))

      val residencyXml = Residency.xml(claim)

      (residencyXml \\ "Nationality").text mustEqual yourDetails.nationality
      (residencyXml \\ "CountryNormallyLiveOther").text mustEqual "UK"
      (residencyXml \\ "InGreatBritainNow").text mustEqual yes
      (residencyXml \\ "OutOfGreatBritain").text mustEqual no

      val periodsAbroadLastYearXml = (residencyXml \\ "PeriodAbroadLastYear")
      val periodOne = periodsAbroadLastYearXml.theSeq(0)
      (periodOne \\ "Period" \\ "DateFrom").text mustEqual startDate.`yyyy-MM-dd`
      (periodOne \\ "Period" \\ "DateTo").text mustEqual endDate.`yyyy-MM-dd`
      (periodOne \\ "Reason").text mustEqual holidayOption.get
      (periodOne \\ "Country").text mustEqual netherlands

      val periodTwo = periodsAbroadLastYearXml.theSeq(0)
      (periodTwo \\ "Period" \\ "DateFrom").text mustEqual startDate.`yyyy-MM-dd`
      (periodTwo \\ "Period" \\ "DateTo").text mustEqual endDate.`yyyy-MM-dd`
      (periodTwo \\ "Reason").text mustEqual holidayOption.get
      (periodTwo \\ "Country").text mustEqual netherlands

      val periodsAbroadDuringCareXml = (residencyXml \\ "PeriodAbroadDuringCare")
      val periodCareOne = periodsAbroadDuringCareXml.theSeq(0)
      (periodCareOne \\ "Period" \\ "DateFrom").text mustEqual startDate.`yyyy-MM-dd`
      (periodCareOne \\ "Period" \\ "DateTo").text mustEqual endDate.`yyyy-MM-dd`
      (periodCareOne \\ "Reason").text mustEqual holidayOption.get

      val periodCareTwo = periodsAbroadDuringCareXml.theSeq(1)
      (periodCareTwo \\ "Period" \\ "DateFrom").text mustEqual startDate.`yyyy-MM-dd`
      (periodCareTwo \\ "Period" \\ "DateTo").text mustEqual endDate.`yyyy-MM-dd`
      (periodCareTwo \\ "Reason").text mustEqual holidayOption.get

      (residencyXml \\ "OtherNationality" \\ "VisaReferenceNumber").text shouldEqual "visaReference"
    }

    "generate <OtherNationality> if user has lived abroad" in {
      val yourDetails = YourDetails(alwaysLivedUK = no)
      val dayMonthYear = DayMonthYear(Some(1), Some(1), Some(2013))
      val dayMonthYearInUK = DayMonthYear(Some(2), Some(2), Some(2012))
      val goBack = YesNoWithDate(yes, Some(dayMonthYear))
      val netherlands = "Netherlands"
      val visaReference = "visaReference"
      val livingInUK = LivingInUK(answer = yes, date = Some(dayMonthYearInUK), text = Some(netherlands), goBack = Some(goBack))
      val timeOutsideUK = TimeOutsideUK(livingInUK = livingInUK, visaReference = Some(visaReference))

      val claim = Claim().update(yourDetails).update(timeOutsideUK)

      val xml = Residency.otherNationality(claim)

      (xml \\ "DateArrivedInGreatBritain").text shouldEqual dayMonthYearInUK.`yyyy-MM-dd`
      (xml \\ "CountryArrivedFrom").text shouldEqual netherlands
      (xml \\ "IntendToReturn").text shouldEqual yes
      (xml \\ "DateReturn").text shouldEqual dayMonthYear.`yyyy-MM-dd`
      (xml \\ "VisaReferenceNumber").text shouldEqual visaReference
    }

    "skip <OtherNationality> if user has always lived in UK" in {
      val yourDetails = YourDetails(alwaysLivedUK = yes)
      val claim = Claim().update(yourDetails)
      val xml = Residency.otherNationality(claim)

      xml.text must beEmpty
    }
  } section "unit"
}
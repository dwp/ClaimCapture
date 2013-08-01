package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.yesNo.YesNoWithText
import controllers.Mappings
import models.DayMonthYear
import Mappings.{yes, no}

class ResidenceSpec extends Specification with Tags {


  val yourDetails = YourDetails(nationality = "Dutch")

  val normalResidence = NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer=yes, text=Some("UK")), inGBNow=yes)

  val startDate = DayMonthYear(day = Some(3), month = Some(4), year = Some(2013))

  val endDate = DayMonthYear(day = Some(3), month = Some(5), year = Some(2013))

  val netherlands = "Netherlands"

  val holidayOption = Some("Holiday")

  val fourWeekTrip1 = Trip(id="four-one", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fourWeekTrip2 = Trip(id="four-two", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fiftyTwoWeeksTrip1 = Trip(id="fiftyTwo-one", start = startDate, end = endDate, where = netherlands, why = holidayOption)

  val fiftyTwoWeeksTrip2 = Trip(id="fiftyTwo-two", start = startDate, end = endDate, where = netherlands, why = holidayOption)


  val trips = Trips(fourWeeksTrips=List(fourWeekTrip1, fourWeekTrip1), fiftyTwoWeeksTrips = List(fiftyTwoWeeksTrip1, fiftyTwoWeeksTrip2))

  "Residence" should {

    "generate xml when data is present" in {
      val claim = Claim().update(yourDetails)
        .update(normalResidence).update(trips)

      val residenceXml = Residence.xml(claim)

      (residenceXml \\ "Nationality").text mustEqual yourDetails.nationality
      (residenceXml \\ "CountryNormallyLiveOther").text mustEqual "UK"
      (residenceXml \\ "InGreatBritainNow").text mustEqual yes

      val periodsAbroadLastYearXml = (residenceXml \\ "PeriodAbroadLastYear")
      val periodOne = periodsAbroadLastYearXml.theSeq(0)
      (periodOne \\ "Period" \\ "DateFrom").text  mustEqual startDate.`yyyy-MM-dd`
      (periodOne \\ "Period" \\ "DateTo").text  mustEqual endDate.`yyyy-MM-dd`
      (periodOne \\ "Reason").text mustEqual holidayOption.get
      (periodOne \\ "Country").text mustEqual netherlands

      val periodTwo =  periodsAbroadLastYearXml.theSeq(0)
      (periodTwo \\ "Period" \\ "DateFrom").text  mustEqual startDate.`yyyy-MM-dd`
      (periodTwo \\ "Period" \\ "DateTo").text  mustEqual endDate.`yyyy-MM-dd`
      (periodTwo \\ "Reason").text mustEqual holidayOption.get
      (periodTwo \\ "Country").text mustEqual netherlands

      val periodsAbroadDuringCareXml = (residenceXml \\ "PeriodAbroadDuringCare")
      val periodCareOne = periodsAbroadDuringCareXml.theSeq(0)
      (periodCareOne \\ "Period" \\ "DateFrom").text  mustEqual startDate.`yyyy-MM-dd`
      (periodCareOne \\ "Period" \\ "DateTo").text  mustEqual endDate.`yyyy-MM-dd`
      (periodCareOne \\ "Reason").text mustEqual holidayOption.get

      val periodCareTwo = periodsAbroadDuringCareXml.theSeq(1)
      (periodCareTwo \\ "Period" \\ "DateFrom").text  mustEqual startDate.`yyyy-MM-dd`
      (periodCareTwo \\ "Period" \\ "DateTo").text  mustEqual endDate.`yyyy-MM-dd`
      (periodCareTwo \\ "Reason").text mustEqual holidayOption.get

    }

    "generate xml when data is missing" in {
      val residenceXml = Residence.xml(Claim())

      (residenceXml \\ "Nationality").text mustEqual ""
      (residenceXml \\ "CountryNormallyLiveOther").text mustEqual ""
      (residenceXml \\ "InGreatBritainNow").text mustEqual no

      (residenceXml \\ "PeriodAbroadLastYear").text mustEqual ""
      (residenceXml \\ "PeriodAbroadDuringCare").text mustEqual ""
    }

  } section "unit"

}

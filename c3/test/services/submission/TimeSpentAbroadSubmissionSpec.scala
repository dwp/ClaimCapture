package services.submission

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import services.submission.XMLHelper._
import models.yesNo.YesNoWithText
import controllers.Mappings
import models.DayMonthYear

class TimeSpentAbroadSubmissionSpec extends Specification with Tags {


  val yourDetails = YourDetails(nationality = "Dutch")

  val normalResidence = NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer=Mappings.yes, text=Some("UK")), inGBNow=Mappings.yes)

  val startDate = DayMonthYear(day = Some(3), month = Some(4), year = Some(2013))

  val endDate = DayMonthYear(day = Some(3), month = Some(5), year = Some(2013))

  val where = "Netherlands"

  val why = None

  val fourWeekTrip1 = Trip(id="four-one", start = startDate, end = endDate, where = where, why = why)

  val fourWeekTrip2 = Trip(id="four-two", start = startDate, end = endDate, where = where, why = why)

  val fiftyTwoWeeksTrip1 = Trip(id="fiftyTwo-one", start = startDate, end = endDate, where = where, why = why)

  val fiftyTwoWeeksTrip2 = Trip(id="fiftyTwo-two", start = startDate, end = endDate, where = where, why = why)


  val trips = Trips(fourWeeksTrips=List(fourWeekTrip1, fourWeekTrip1), fiftyTwoWeeksTrips = List(fiftyTwoWeeksTrip1, fiftyTwoWeeksTrip2))

  "Time Spent Abroad Submission" should {

    "generate xml when data is present" in {
      val claim = Claim().update(yourDetails)
        .update(normalResidence).update(trips)

      val xml = TimeSpentAbroadSubmission.xml(claim)
    }

    "generate xml when data is missing" in {

    }

  } section "unit"

}

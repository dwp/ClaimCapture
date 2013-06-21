package helpers

import models.domain._
import models.DayMonthYear
import models.MultiLineAddress
import scala.Some

object ClaimBuilder {
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
    otherSurnames = Some("O'Dwyer"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(currentlyLivingInUK = "no", arrivedInUK = Some(DayMonthYear()),
    originCountry = None, planToGoBack = Some("yes"), whenPlanToGoBack = None,
    visaReference = None)

  val claimDate = ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013))

  val aboutYou = AboutYou(yourDetails, contactDetails, timeOutsideUK, claimDate)
}

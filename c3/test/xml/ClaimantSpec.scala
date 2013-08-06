package xml

import org.specs2.mutable.Specification
import models.domain._
import models.DayMonthYear
import models.MultiLineAddress

class ClaimantSpec extends Specification {
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
                                otherSurnames = Some("O'Dwyer"), None, nationality = "French",
                                dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
                                      postcode = Some("PR2 8AE"), phoneNumber = Some("01772 700806"), None)

  "Claimant" should {
    "generate XML from a given claim" in {
      val claim = Claim().update(ClaimDate(DayMonthYear(1, 1, 1999)))
                         .update(yourDetails).update(contactDetails)

      val claimant = Claimant.xml(claim)
      (claimant \\ "DateOfClaim").text shouldEqual "1999-01-01"
    }

    "generate XML from a given claim including middle name" in {
      val claim = Claim().update(ClaimDate(DayMonthYear(1, 1, 1999)))
                         .update(yourDetails.copy(middleName = Some("Joe"))).update(contactDetails)

      val claimant = Claimant.xml(claim)
      (claimant \\ "DateOfClaim").text shouldEqual "1999-01-01"
    }
  }
}
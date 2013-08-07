package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import controllers.Mappings.{yes,no}
import models.{DayMonthYear}
import models.yesNo.YesNoWithDate
import models.MultiLineAddress
import scala.Some

class PartnerSpec extends Specification with Tags {

  "Partner" should {
    "generate <Partner> xml when claimer had a partner" in {
      val moreAboutYou = MoreAboutYou(hadPartnerSinceClaimDate = yes)
      val livingTogetherDate = DayMonthYear(Some(1), Some(2), Some(1970))
      val separatedDate = DayMonthYear(Some(4), Some(5), Some(2013))
      val moreAboutYourPartner = MoreAboutYourPartner(startedLivingTogether = Some(YesNoWithDate(yes, Some(livingTogetherDate))), separated= YesNoWithDate(yes, Some(separatedDate)))
      val dateOfBirth = DayMonthYear(Some(3), Some(4), Some(1950))
      val yourPartnerPersonalDetails = YourPartnerPersonalDetails(title="mr", firstName="firstName", middleName=Some("middleName"), surname="surname", otherSurnames=Some("otherNames"), dateOfBirth=dateOfBirth)
      val yourPartnerContactDetails = YourPartnerContactDetails(address=Some(MultiLineAddress(lineOne = Some("line1"))), postcode=Some("SE1 6EH"))

      val claim = Claim().update(moreAboutYou).update(moreAboutYourPartner).update(yourPartnerPersonalDetails).update(yourPartnerContactDetails)

      val xml = Partner.xml(claim)

      (xml \\ "Surname").text shouldEqual yourPartnerPersonalDetails.surname
      (xml \\ "OtherNames").text shouldEqual yourPartnerPersonalDetails.firstName + " " + yourPartnerPersonalDetails.middleName.get
      (xml \\ "OtherSurnames").text shouldEqual yourPartnerPersonalDetails.otherSurnames.get
      (xml \\ "Title").text shouldEqual yourPartnerPersonalDetails.title
      (xml \\ "DateOfBirth").text shouldEqual dateOfBirth.`yyyy-MM-dd`
      (xml \\ "Address" \\ "PostCode").text shouldEqual yourPartnerContactDetails.postcode.get
      (xml \\ "RelationshipStatus" \\ "JoinedHouseholdAfterDateOfClaim").text shouldEqual yes
      (xml \\ "RelationshipStatus" \\ "JoinedHouseholdDate").text shouldEqual livingTogetherDate.`yyyy-MM-dd`
      (xml \\ "RelationshipStatus" \\ "SeparatedFromPartner").text shouldEqual yes
      (xml \\ "RelationshipStatus" \\ "SeparationDate").text shouldEqual separatedDate.`yyyy-MM-dd`
    }

    "skip <Partner> xml when claimer didn't had a partner" in {
      val moreAboutYou = MoreAboutYou(hadPartnerSinceClaimDate = no)
      val xml = Partner.xml(Claim().update(moreAboutYou))
      xml.text must beEmpty
    }
  }
}

package xml

import app.XMLValues
import models.domain._
import XMLHelper.{stringify, postalAddressStructure}
import scala.xml.NodeSeq
import app.XMLValues._
import models.Street

object Partner {

  def xml(claim: Claim) = {
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou(hadPartnerSinceClaimDate = no))

    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())

    val hadPartner = moreAboutYou.hadPartnerSinceClaimDate == yes

    if (hadPartner) {
      <Partner>
        <Surname>{yourPartnerPersonalDetails.surname}</Surname>
        <OtherNames>{yourPartnerPersonalDetails.firstName} {yourPartnerPersonalDetails.middleName.orNull}</OtherNames>
        <OtherSurnames>{yourPartnerPersonalDetails.otherSurnames.orNull}</OtherSurnames>
        <Title>{yourPartnerPersonalDetails.title}</Title>
        <DateOfBirth>{yourPartnerPersonalDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
        <NationalInsuranceNumber>{stringify(yourPartnerPersonalDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
        <NationalityPartner>{yourPartnerPersonalDetails.nationality.orNull}</NationalityPartner>
        <RelationshipStatus>
          <SeparatedFromPartner>
            <QuestionLabel>yourPartnerPersonalDetails.separatedFromPartner?</QuestionLabel>
            <Answer>{yourPartnerPersonalDetails.separatedFromPartner match {
              case "yes" => XMLValues.Yes
              case "no" => XMLValues.No
              case n => n
            }}</Answer>
          </SeparatedFromPartner>
        </RelationshipStatus>
      </Partner>
    } else NodeSeq.Empty
  }
}

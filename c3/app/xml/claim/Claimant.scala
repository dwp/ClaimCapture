package xml.claim

import app.XMLValues
import models.domain.{ContactDetails, YourDetails, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent

object Claimant extends XMLComponent {
  def xml(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())

    <Claimant>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      {if(!yourDetails.otherSurnames.isEmpty) <OtherSurnames>{yourDetails.otherSurnames.orNull}</OtherSurnames> }
      <Title>{yourDetails.title}</Title>
      <DateOfBirth>{yourDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{yourDetails.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
      {postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}
      {if(!contactDetails.phoneNumber.isEmpty){
        <DayTimePhoneNumber>{contactDetails.phoneNumber.orNull}</DayTimePhoneNumber>
      }}
      {if(!contactDetails.mobileNumber.isEmpty){
        <MobileNumber>{contactDetails.mobileNumber.orNull}</MobileNumber>
      }}
      <MaritalStatus>{yourDetails.maritalStatus}</MaritalStatus>
      {contactDetails.contactYouByTextphone match {
        case Some(n) =>
        <TextPhoneContact>
          <QuestionLabel>TextPhoneContact?</QuestionLabel>
          <Answer>{contactDetails.contactYouByTextphone match {
            case Some(n) => { n match {
                case "yes" => XMLValues.Yes
                case "no" => XMLValues.No
                case n => n
              }
            }
            case None => NodeSeq.Empty
          }}</Answer>
        </TextPhoneContact>
        case _ => NodeSeq.Empty
    }}
    </Claimant>
  }
}
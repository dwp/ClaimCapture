package services.submission

import models.domain._

object CareYouProvideSubmission {

  private def getQuestionGroup[T](claim:Claim,id: String) = {
    claim.questionGroup(id).asInstanceOf[Option[T]].get
  }
  private def questionGroup[T](claim:Claim,id: String) = {
    claim.questionGroup(id).asInstanceOf[Option[T]]
  }

  def buildCareYouProvide(claim: Claim) = {
    val theirPersonalDetails =          getQuestionGroup[TheirPersonalDetails](claim,TheirPersonalDetails.id)
    val theirContactDetails =           getQuestionGroup[TheirContactDetails](claim,TheirContactDetails.id)
    val moreAboutThePerson =            getQuestionGroup[MoreAboutThePerson](claim,MoreAboutThePerson.id)
    val previousCarerContactDetails =   questionGroup[PreviousCarerContactDetails](claim,PreviousCarerContactDetails.id)
    val previousCarerPersonalDetails =  questionGroup[PreviousCarerPersonalDetails](claim,PreviousCarerPersonalDetails.id)
    val representatives =               getQuestionGroup[RepresentativesForPerson](claim,RepresentativesForPerson.id)
    val moreAboutTheCare =              getQuestionGroup[MoreAboutTheCare](claim,MoreAboutTheCare.id)
    val oneWhoPays =                    questionGroup[OneWhoPaysPersonalDetails](claim,OneWhoPaysPersonalDetails.id)
    val contactDetailsPayingPerson =    questionGroup[ContactDetailsOfPayingPerson](claim,ContactDetailsOfPayingPerson.id)
    val breaksInCare =                  getQuestionGroup[BreaksInCare](claim,BreaksInCare.id)

    CareYouProvide(theirPersonalDetails,theirContactDetails,moreAboutThePerson,representatives,previousCarerContactDetails,previousCarerPersonalDetails,moreAboutTheCare,oneWhoPays,contactDetailsPayingPerson,breaksInCare)
  }

  def buildCaree(careYouProvide: CareYouProvide) = {
    <Caree>
      <Surname>{careYouProvide.theirPersonalDetails.surname}</Surname>
      <OtherNames>{s"${careYouProvide.theirPersonalDetails.firstName} ${careYouProvide.theirPersonalDetails.middleName.getOrElse("")}"}</OtherNames>
      <Title>{careYouProvide.theirPersonalDetails.title}</Title>
      <DateOfBirth>{careYouProvide.theirPersonalDetails.dateOfBirth.toXmlString}</DateOfBirth>
      <NationalInsuranceNumber>{careYouProvide.theirPersonalDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
      <Address>
        <gds:Line>{careYouProvide.theirContactDetails.address.lineOne.orNull}</gds:Line>
        <gds:Line>{careYouProvide.theirContactDetails.address.lineTwo.orNull}</gds:Line>
        <gds:Line>{careYouProvide.theirContactDetails.address.lineThree.orNull}</gds:Line>
        <gds:PostCode>{careYouProvide.theirContactDetails.postcode.orNull}</gds:PostCode>
      </Address>
      <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
      <HomePhoneNumber/>
      <DaytimePhoneNumber>
        <Number>{careYouProvide.theirContactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <RelationToClaimant>{careYouProvide.moreAboutThePerson.relationship}</RelationToClaimant>
      <Cared35hours>{careYouProvide.moreAboutTheCare.spent35HoursCaring}</Cared35hours>
      <CanCareeSign>yes</CanCareeSign>
      <CanSomeoneElseSign>{careYouProvide.representatives.someoneElseActForPerson}</CanSomeoneElseSign>
      <CanClaimantSign>{careYouProvide.representatives.actForPerson}</CanClaimantSign>
      <ClaimantActingType>
        <ParentOrGuardian></ParentOrGuardian>
        <PowerOfAttorney></PowerOfAttorney>
        <Appointee></Appointee>
        <JudicialFactor></JudicialFactor>
        <Receiver></Receiver>
      </ClaimantActingType>
      <BreaksSinceClaim>{if (careYouProvide.moreAboutTheCare.spent35HoursCaringBeforeClaim == "no" && careYouProvide.breaksInCare.breaks.size > 0) "yes" else "no"}</BreaksSinceClaim>
      {
        for(break <- careYouProvide.breaksInCare.breaks) yield{
          <CareBreak>
            <StartDateTime>{break.start.toXmlTimeString}</StartDateTime>
            <EndDateTime>{break.end.fold("")(d => d.toXmlTimeString)}</EndDateTime>
            <Reason>{if(break.whereYou.location != "Other") break.whereYou.location else break.whereYou.other.getOrElse("Other")}</Reason>
            <MedicalCare>{break.medicalDuringBreak}</MedicalCare>
            <AwayFromHome>yes</AwayFromHome>
          </CareBreak>
        }
      }
      <Cared35hoursBefore>{careYouProvide.moreAboutTheCare.spent35HoursCaringBeforeClaim}</Cared35hoursBefore>
      <BreaksBeforeClaim>{if (careYouProvide.moreAboutTheCare.spent35HoursCaringBeforeClaim == "yes" && careYouProvide.breaksInCare.breaks.size > 0) "yes" else "no"}</BreaksBeforeClaim>

      {
        if (careYouProvide.moreAboutTheCare.spent35HoursCaringBeforeClaim == "yes"){
          <DateStartedCaring>{careYouProvide.moreAboutTheCare.careStartDate.fold("")(d => d.toXmlString)}</DateStartedCaring>
        }
      }
      <PaidForCaring>{careYouProvide.moreAboutTheCare.hasSomeonePaidYou}</PaidForCaring>
      {
      if (careYouProvide.moreAboutTheCare.hasSomeonePaidYou == "yes" && careYouProvide.oneWhoPays.isDefined && careYouProvide.contactDetailsPayingPerson.isDefined ){
        <PayReceived>
          <PayerName>{s"${careYouProvide.oneWhoPays.get.organisation.orNull} ${careYouProvide.oneWhoPays.get.title.orNull} ${careYouProvide.oneWhoPays.get.firstName.orNull} ${careYouProvide.oneWhoPays.get.middleName.orNull} ${careYouProvide.oneWhoPays.get.surname.orNull}"}</PayerName>
          <PayerAddress>
            <gds:Line>{careYouProvide.contactDetailsPayingPerson.get.address.fold("")(a => a.lineOne.orNull)}</gds:Line>
            <gds:Line>{careYouProvide.contactDetailsPayingPerson.get.address.fold("")(a => a.lineTwo.orNull)}</gds:Line>
            <gds:Line>{careYouProvide.contactDetailsPayingPerson.get.address.fold("")(a => a.lineThree.orNull)}</gds:Line>
            <gds:PostCode>{careYouProvide.contactDetailsPayingPerson.get.postcode.orNull}</gds:PostCode>
          </PayerAddress>
          <ConfirmAddress>yes</ConfirmAddress>
          <Payment>
            <Currency>GBP</Currency>
            <Amount>{careYouProvide.oneWhoPays.get.amount.orNull}</Amount>
          </Payment>
          <DatePaymentStarted>{careYouProvide.oneWhoPays.get.startDatePayment.fold("")(d => d.toXmlString)}</DatePaymentStarted>
        </PayReceived>
      }
      }
      <ClaimedPreviously>{careYouProvide.moreAboutThePerson.claimedAllowanceBefore}</ClaimedPreviously>
      {
      if (careYouProvide.moreAboutThePerson.claimedAllowanceBefore == "yes" && careYouProvide.previousCarerPersonalDetails.isDefined && careYouProvide.previousCarerContactDetails.isDefined){
        <PreviousClaimant>
          <Surname>{careYouProvide.previousCarerPersonalDetails.get.surname.orNull}</Surname>
          <OtherNames>{s"${careYouProvide.previousCarerPersonalDetails.get.firstName.orNull} ${careYouProvide.previousCarerPersonalDetails.get.middleName.getOrElse("")}"}</OtherNames>
          <DateOfBirth>{careYouProvide.previousCarerPersonalDetails.get.dateOfBirth.fold("")(d => d.toXmlString)}</DateOfBirth>
          <NationalInsuranceNumber>{careYouProvide.previousCarerPersonalDetails.get.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
          <Address>
            <gds:Line>{careYouProvide.previousCarerContactDetails.get.address.fold("")(a => a.lineOne.orNull)}</gds:Line>
            <gds:Line>{careYouProvide.previousCarerContactDetails.get.address.fold("")(a => a.lineTwo.orNull)}</gds:Line>
            <gds:Line>{careYouProvide.previousCarerContactDetails.get.address.fold("")(a => a.lineThree.orNull)}</gds:Line>
            <gds:PostCode>{careYouProvide.previousCarerContactDetails.get.postcode.orNull}</gds:PostCode>
          </Address>

        </PreviousClaimant>
      }else{
        <PreviousClaimant>
          <Surname>null</Surname>
          <OtherNames>null</OtherNames>
          <DateOfBirth></DateOfBirth>
          <NationalInsuranceNumber></NationalInsuranceNumber>
          <Address>
            <gds:Line></gds:Line>
            <gds:Line></gds:Line>
            <gds:Line></gds:Line>
            <gds:PostCode></gds:PostCode>
          </Address>

        </PreviousClaimant>
      }
      }



    </Caree>
  }
}

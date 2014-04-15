package xml.claim

import app.XMLValues._
import models.domain._
import scala.xml.NodeSeq

object Residency {

  def xml(claim: Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())

    <Residency>
      <Nationality>{nationalityAndResidency.nationality}</Nationality>
      <EUEEASwissNational>{NotAsked}</EUEEASwissNational>
      <CountryNormallyLive>{NotAsked}</CountryNormallyLive>
      <CountryNormallyLiveOther>{NotAsked}</CountryNormallyLiveOther>
      <InGreatBritainNow>{NotAsked}</InGreatBritainNow>
      <InGreatBritain26Weeks>{NotAsked}</InGreatBritain26Weeks>
      <BritishOverseasPassport>{NotAsked}</BritishOverseasPassport>
      {otherNationality(claim)}
      <OutOfGreatBritain>{NotAsked}</OutOfGreatBritain>
    </Residency>
  }

  def otherNationality(claim:Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val resideInUK = nationalityAndResidency.resideInUK.answer == yes
    if(!resideInUK) {
      <OtherNationality>
        <EUEEASwissNationalChildren/>
        <DateArrivedInGreatBritain>{NotAsked}</DateArrivedInGreatBritain>
        <CountryArrivedFrom>{NotAsked}</CountryArrivedFrom>
        <IntendToReturn>{NotAsked}</IntendToReturn>
        <DateReturn>{NotAsked}</DateReturn>
        <VisaReferenceNumber>{NotAsked}</VisaReferenceNumber>
      </OtherNationality>

    } else NodeSeq.Empty
  }
}
package xml

import app.XMLValues._
import models.domain._
import scala.xml.NodeSeq

object Residency {

  def xml(claim: Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())

    <Residency>
      <Nationality>{nationalityAndResidency.nationality}</Nationality>
      <EUEEASwissNational>{NotAsked}</EUEEASwissNational>
      {/** TODO : These fields have to be addressed when we move this section to the new location : Start **/}
      <CountryNormallyLive>{NotAsked/**TODO**/}</CountryNormallyLive>
      <CountryNormallyLiveOther>{NotAsked/**TODO**/}</CountryNormallyLiveOther>
      <InGreatBritainNow>{NotAsked}</InGreatBritainNow>
      <InGreatBritain26Weeks>{NotAsked/**TODO**/}</InGreatBritain26Weeks>
      <BritishOverseasPassport>{NotAsked/**TODO**/}</BritishOverseasPassport>
      {otherNationality(claim)}
      <OutOfGreatBritain>{NotAsked/**TODO**/}</OutOfGreatBritain>
      {/** TODO : These fields have to be addressed when we move this section to the new location : End **/}
    </Residency>
  }

  def otherNationality(claim:Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val resideInUK = nationalityAndResidency.resideInUK.answer == yes
    if(!resideInUK) {
      <OtherNationality>
        <EUEEASwissNationalChildren/>
        <DateArrivedInGreatBritain>{NotAsked}</DateArrivedInGreatBritain>
        <CountryArrivedFrom>{nationalityAndResidency.resideInUK.text.orNull}</CountryArrivedFrom>
        <IntendToReturn>{/** TODO: Make sure this is OK in the future **/}{NotAsked}</IntendToReturn>
        <DateReturn>{/** TODO: Make sure this is OK in the future **/}{NotAsked}</DateReturn>
        <VisaReferenceNumber>{NotAsked}</VisaReferenceNumber>
      </OtherNationality>

    } else NodeSeq.Empty
  }
}
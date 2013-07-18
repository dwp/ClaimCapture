package services.submission

import models.domain.Section

object TimeSpentAbroadSubmission {

  def xml(timeSpentAbroad:Section) = {
    <Residency>
      <Nationality>Dutch</Nationality> <!-- OK -->
      <EUEEASwissNational></EUEEASwissNational> <!-- OK page 13 -->
      <CountryNormallyLive></CountryNormallyLive><!-- OK page 13 -->
      <CountryNormallyLiveOther></CountryNormallyLiveOther>
      <InGreatBritainNow>yes</InGreatBritainNow><!-- OK -->
      <InGreatBritain26Weeks></InGreatBritain26Weeks><!-- MISSING -->
      <BritishOverseasPassport></BritishOverseasPassport>
      <StayInGreatBritain>
        <StayingPurpose></StayingPurpose>
        <IntendToStay></IntendToStay> <!-- OK It asks if you plan to go back to the country you are from -->
      </StayInGreatBritain>
      <OutOfGreatBritain></OutOfGreatBritain> <!-- OK page 13 -->
      <PeriodAbroadDuringCare>
        <Period>
          <DateFrom></DateFrom> <!-- OK -->
          <DateTo></DateTo> <!-- OK -->
        </Period>
        <Reason></Reason> <!-- OK -->
      </PeriodAbroadDuringCare>
      <PeriodAbroadLastYear>
        <Period>
          <DateFrom></DateFrom> <!-- OK -->
          <DateTo></DateTo> <!-- OK -->
        </Period>
        <Reason></Reason> <!-- OK -->
        <Country></Country> <!-- OK -->
      </PeriodAbroadLastYear>

    </Residency>
  }

}

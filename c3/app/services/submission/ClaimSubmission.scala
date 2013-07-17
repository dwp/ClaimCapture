package services.submission

import models.domain._
import play.api.Logger


case class ClaimSubmission(claim: Claim, transactionId : String) {
  val aboutYou = AboutYouSubmission.buildAboutYou(claim)

  val yourPartner = YourPartnerSubmission.buildYourPartner(claim)

  val careYouProvide = CareYouProvideSubmission.buildCareYouProvide(claim)

  val consentAndDeclaration = ConsentAndDeclarationSubmission.buildConsentAndDeclaration(claim)

  def buildDwpClaim = {
    Logger.info(s"Build Claim : $transactionId")
    <DWPCAClaim id={transactionId}>
      {AboutYouSubmission.buildClaimant(aboutYou)}
      {CareYouProvideSubmission.buildCaree(careYouProvide)}
      <ClaimADI>no</ClaimADI>
      <Residency>
        <Nationality>British</Nationality>
        <EUEEASwissNational>yes</EUEEASwissNational>
        <CountryNormallyLive>Great Britain</CountryNormallyLive>
        <CountryNormallyLiveOther/>
        <InGreatBritainNow>yes</InGreatBritainNow>
        <InGreatBritain26Weeks>yes</InGreatBritain26Weeks>
        <BritishOverseasPassport>no</BritishOverseasPassport>
        <OutOfGreatBritain>no</OutOfGreatBritain>
      </Residency>
      <CourseOfEducation>yes</CourseOfEducation>
      <FullTimeEducation>
        <CourseDetails>
          <Type>BTEC</Type>
          <Title>Pottery</Title>
          <HoursSpent>15</HoursSpent>
          <DateStarted>2011-09-03</DateStarted>
          <DateStopped/>
          <ExpectedEndDate>2013-07-05</ExpectedEndDate>
        </CourseDetails>
        <LocationDetails>
          <Name>UCLAN</Name>
          <Address>
            <gds:Line>10 Madeup Street</gds:Line>
            <gds:Line/>
            <gds:Line/>
            <gds:PostCode/>
          </Address>
          <PhoneNumber>07890 3456789</PhoneNumber>
          <FaxNumber/>
          <StudentReferenceNumber/>
          <Tutor>Mrs Bloggs</Tutor>
        </LocationDetails>
      </FullTimeEducation>
      <SelfEmployed>no</SelfEmployed>
      <Employed>yes</Employed>
      <Employment>
        <CurrentlyEmployed>yes</CurrentlyEmployed>
        <DateLastWorked>2013-06-10</DateLastWorked>
        <JobDetails>
          <Employer>
            <DateJobStarted>2009-10-09</DateJobStarted>
            <DateJobEnded/>
            <JobType>Cheese Taster</JobType>
            <ClockPayrollNumber>89765432</ClockPayrollNumber>
            <Name>Cheeseworld</Name>
            <Address>
              <gds:Line>3</gds:Line>
              <gds:Line>Whocares Avenue</gds:Line>
              <gds:Line/>
              <gds:Line/>
              <gds:PostCode/>
            </Address>
            <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
            <EmployersPhoneNumber>08907 1234567</EmployersPhoneNumber>
            <EmployersFaxNumber/>
            <WagesDepartment/>
            <DepartmentPhoneFaxNumber>07890 1234567</DepartmentPhoneFaxNumber>
          </Employer>
          <Pay>
            <WeeklyHoursWorked>40</WeeklyHoursWorked>
            <DateLastWorked/>
            <DateLastPaid>2013-05-31</DateLastPaid>
            <GrossPayment>
              <Currency>GBP</Currency>
              <Amount>2000.00</Amount>
            </GrossPayment>
            <IncludedInWage>Basic wage</IncludedInWage>
            <PayPeriod>
              <DateFrom>2013-05-01</DateFrom>
              <DateTo>2013-05-31</DateTo>
            </PayPeriod>
            <PayFrequency>05</PayFrequency>
            <PayFrequencyOther/>
            <UsualPayDay>Last day of month</UsualPayDay>
            <VaryingEarnings>no</VaryingEarnings>
            <PaidForHolidays>no</PaidForHolidays>
          </Pay>
          <OtherThanMoney>no</OtherThanMoney>
          <OweMoney>no</OweMoney>
          <CareExpensesChildren>no</CareExpensesChildren>
          <CareExpensesCaree>no</CareExpensesCaree>
          <PaidForOccupationalPension>no</PaidForOccupationalPension>
          <PaidForPersonalPension>no</PaidForPersonalPension>
          <PaidForJobExpenses>no</PaidForJobExpenses>
        </JobDetails>
      </Employment>
      <PropertyRentedOut>
        <PayNationalInsuranceContributions>no</PayNationalInsuranceContributions>
        <RentOutProperty>no</RentOutProperty>
        <SubletHome>no</SubletHome>
      </PropertyRentedOut>
      <HavePartner>yes</HavePartner>
      {YourPartnerSubmission.buildClaimant(yourPartner)} 
      <OtherBenefits>
        <ClaimantBenefits>
          <JobseekersAllowance>no</JobseekersAllowance>
          <IncomeSupport>no</IncomeSupport>
          <PensionCredit>no</PensionCredit>
          <StatePension>no</StatePension>
          <IncapacityBenefit>no</IncapacityBenefit>
          <SevereDisablementAllowance>no</SevereDisablementAllowance>
          <MaternityAllowance>no</MaternityAllowance>
          <UnemployabilitySupplement>no</UnemployabilitySupplement>
          <WindowsBenefit>no</WindowsBenefit>
          <WarWidowsPension>no</WarWidowsPension>
          <IndustrialDeathBenefit>no</IndustrialDeathBenefit>
          <GovernmentTrainingAllowance>no</GovernmentTrainingAllowance>
          <LoneParentChildBenefit>no</LoneParentChildBenefit>
          <OtherSocialSecurityBenefit>no</OtherSocialSecurityBenefit>
          <NonSocialSecurityBenefit>no</NonSocialSecurityBenefit>
          <NoBenefits>yes</NoBenefits>
        </ClaimantBenefits>
        <PartnerBenefits>
          <JobseekersAllowance>no</JobseekersAllowance>
          <IncomeSupport>no</IncomeSupport>
          <PensionCredit>no</PensionCredit>
          <StatePension>no</StatePension>
          <IncapacityBenefit>no</IncapacityBenefit>
          <SevereDisablementAllowance>no</SevereDisablementAllowance>
          <MaternityAllowance>no</MaternityAllowance>
          <UnemployabilitySupplement>no</UnemployabilitySupplement>
          <WindowsBenefit>no</WindowsBenefit>
          <WarWidowsPension>no</WarWidowsPension>
          <IndustrialDeathBenefit>no</IndustrialDeathBenefit>
          <GovernmentTrainingAllowance>no</GovernmentTrainingAllowance>
          <OtherSocialSecurityBenefit>no</OtherSocialSecurityBenefit>
          <NonSocialSecurityBenefit>no</NonSocialSecurityBenefit>
          <NoBenefits>yes</NoBenefits>
        </PartnerBenefits>
        <ExtraMoney>no</ExtraMoney>
        <OtherMoneySSP>no</OtherMoneySSP>
        <OtherMoneySMP>no</OtherMoneySMP>
      </OtherBenefits>
      <Payment>
        <PaymentFrequency>everyWeek</PaymentFrequency>
        <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
        <Account>
          <DirectPayment>yes</DirectPayment>
          <AccountHolder>yourName</AccountHolder>
          <HolderName>Mickey Mouse</HolderName>
          <SecondHolderName/>
          <AccountType>bank</AccountType>
          <OtherBenefitsToBePaidDirect/>
          <BankDetails>
            <AccountNumber>12345678</AccountNumber>
            <SortCode>010101</SortCode>
            <Name>Toytown Bank</Name>
            <Branch/>
            <Address>
              <gds:Line/>
              <gds:Line/>
              <gds:Line/>
              <gds:PostCode/>
            </Address>
            <ConfirmAddress>yes</ConfirmAddress>
          </BankDetails>
        </Account>
      </Payment>
      <ThirdParty>no</ThirdParty>
      {ConsentAndDeclarationSubmission.buildDeclaration(consentAndDeclaration,careYouProvide)}
      <EvidenceList>
        <TextLine>Documents you need to send us</TextLine>
        <TextLine>You must send us all the documents we ask for. If you do not, any benefit you may be entitled to because of this claim may be delayed.</TextLine>
        <TextLine>Your pay details</TextLine>
        <TextLine>You need to send us the last payslip before 10 May 2013 and all the payslips you have had since then.</TextLine>
        <TextLine>Statement signed by Minnie Mouse</TextLine>
        <TextLine>You need to send us the completed and signed statement.</TextLine>
        <TextLine>Send us your signature with the documents we have asked for</TextLine>
        <TextLine>If you have printed this page, you must sign it in the box provided and send it with the documents we have asked for.</TextLine>
        <TextLine>If you made a note of your transaction details, you must sign the note and send it with the documents we have asked for.</TextLine>
        <TextLine>Where to send the documents</TextLine>
        <TextLine>Post the documents we have asked for with your signed transaction details to:</TextLine>
        <TextLine>CA Freepost</TextLine>
        <TextLine>Palatine House</TextLine>
        <TextLine>Preston</TextLine>
        <TextLine>PR1 1HN</TextLine>
      </EvidenceList>
    </DWPCAClaim>
  }
}

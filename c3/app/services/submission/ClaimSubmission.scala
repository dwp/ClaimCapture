package services.submission

import scala.xml.Elem
import models.domain.AboutYou

case class ClaimSubmission(aboutYou:AboutYou) {

  def createClaimSubmission : Elem = {
    <DWPCAClaim id="TO BE DECIDED">
      <Claimant>
        <DateOfClaim>{aboutYou.claimDate.dateOfClaim.toXmlString}</DateOfClaim>
        <Surname>{aboutYou.yourDetails.surname}</Surname>
        <OtherNames>{s"${aboutYou.yourDetails.firstName} ${aboutYou.yourDetails.middleName.getOrElse("")}"}</OtherNames>
        <OtherSurnames>{aboutYou.yourDetails.otherSurnames.orNull}</OtherSurnames>
        <Title>{aboutYou.yourDetails.title}</Title>
        <MaritalStatus>{aboutYou.yourDetails.maritalStatus}</MaritalStatus>
        <DateOfBirth>{aboutYou.yourDetails.dateOfBirth.toXmlString}</DateOfBirth>
        <NationalInsuranceNumber>{aboutYou.yourDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
        <Address>
          <gds:Line>{aboutYou.contactDetails.address.lineOne.orNull}</gds:Line>
          <gds:Line>{aboutYou.contactDetails.address.lineTwo.orNull}</gds:Line>
          <gds:Line>{aboutYou.contactDetails.address.lineThree.orNull}</gds:Line>
          <gds:PostCode>{aboutYou.contactDetails.postcode.orNull}</gds:PostCode>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
        <HomePhoneNumber>{aboutYou.contactDetails.mobileNumber.orNull}</HomePhoneNumber>
        <DaytimePhoneNumber>
          <Number>{aboutYou.contactDetails.phoneNumber.orNull}</Number>
          <Qualifier/>
        </DaytimePhoneNumber>
        <EmailAddress/>
        <ClaimedBefore>no</ClaimedBefore>  // Default to no
      </Claimant>

      <Caree>
        <Surname>Mouse</Surname>
        <OtherNames>Minnie</OtherNames>
        <Title>mrs</Title>
        <DateOfBirth>1956-03-03</DateOfBirth>
        <NationalInsuranceNumber>AB000000B</NationalInsuranceNumber>
        <Address>
          <gds:Line>10</gds:Line>
          <gds:Line>Anyplace Street</gds:Line>
          <gds:Line/>
          <gds:Line/>
          <gds:PostCode/>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
        <HomePhoneNumber/>
        <DaytimePhoneNumber>
          <Number/>
          <Qualifier/>
        </DaytimePhoneNumber>
        <RelationToClaimant>wife</RelationToClaimant>
        <Cared35hours>yes</Cared35hours>
        <CanCareeSign>yes</CanCareeSign>
        <CanSomeoneElseSign>no</CanSomeoneElseSign>
        <CanClaimantSign>no</CanClaimantSign>
        <BreaksSinceClaim>yes</BreaksSinceClaim>
        <CareBreak>
          <StartDateTime>2013-05-11T08:00:00</StartDateTime>
          <EndDateTime>2013-05-17T21:00:00</EndDateTime>
          <Reason>Minnie was in hospital</Reason>
          <MedicalCare>yes</MedicalCare>
          <AwayFromHome>yes</AwayFromHome>
          <BreakAddress>
            <Address>
              <gds:Line>Western Infirmary</gds:Line>
              <gds:Line>West Street</gds:Line>
              <gds:Line/>
              <gds:Line/>
              <gds:PostCode/>
            </Address>
            <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
          </BreakAddress>
        </CareBreak>
        <Cared35hoursBefore>yes</Cared35hoursBefore>
        <DateStartedCaring>2012-10-03</DateStartedCaring>
        <BreaksBeforeClaim>no</BreaksBeforeClaim>
        <PaidForCaring>no</PaidForCaring>
        <ClaimedPreviously>no</ClaimedPreviously>
      </Caree>

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
            <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
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
      <Partner>
        <NationalityPartner>British</NationalityPartner>
        <Surname>Mouse</Surname>
        <OtherNames>Minnie</OtherNames>
        <OtherSurnames/>
        <Title>mrs</Title>
        <DateOfBirth>1956-03-03</DateOfBirth>
        <NationalInsuranceNumber>AB000000B</NationalInsuranceNumber>
        <Address>
          <gds:Line>10</gds:Line>
          <gds:Line>Anyplace Street</gds:Line>
          <gds:Line/>
          <gds:Line/>
          <gds:PostCode/>
        </Address>
        <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>no</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate/>
          <SeparatedFromPartner>no</SeparatedFromPartner>
          <SeparationDate/>
        </RelationshipStatus>
      </Partner>

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
            <ConfirmAddress>yes</ConfirmAddress> // Always default to yes
          </BankDetails>
        </Account>
      </Payment>

      <ThirdParty>no</ThirdParty>

      <Declaration>
        <TextLine>I declare</TextLine>
        <TextLine>that I have read the Carer's Allowance claim notes, and that the information I have given on this form is correct and complete as far as I know and believe.</TextLine>
        <TextLine>I understand</TextLine>
        <TextLine>that if I knowingly give information that is incorrect or incomplete, I may be liable to prosecution or other action.</TextLine>
        <TextLine>I understand</TextLine>
        <TextLine>that I must promptly tell the office that pays my Carer's Allowance of anything that may affect my entitlement to, or the amount of, that benefit.</TextLine>
        <TextLine>The Secretary of State has made directions that allow you to use this service to make a claim to benefit or send a notification about a change of circumstances by an electronic communication. You should read the directions.</TextLine>
        <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
        <TextLine>Do you agree to us obtaining information from any current or previous employer(s) you may have listed on this claim form?</TextLine>
        <TextLine>Yes</TextLine>
        <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this claim form?</TextLine>
        <TextLine>Yes</TextLine>
        <TextLine>If you have answered No to any of the above and you would like the Carer's Allowance Unit to know the reasons why, please set out those reasons in the "Other information" section displayed to the left of your screen.</TextLine>
        <TextLine>This is my claim for Carer's Allowance.</TextLine>
        <TextLine>Please note that any or all of the information in this claim form may be checked.</TextLine>
        <TextLine>I certify that the name that I have typed in below as my signature is a valid method of establishing that this is my claim for benefit and that I</TextLine>
        <TextLine>Mickey Mouse</TextLine>
        <TextLine>have read and understood all the statements on this page. I agree with all the statements on this page where agreement is indicated, apart from any statements that I do not agree with, where I have given my reasons why not in the "Other information" section.</TextLine>
        <TextLine>yes</TextLine>
        <TextLine>If you live in Wales and would like to receive future communications in Welsh, please select this box.</TextLine>
        <TextLine>no</TextLine>
        <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
        <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
      </Declaration>

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

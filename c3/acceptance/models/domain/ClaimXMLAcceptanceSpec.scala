package models.domain

import org.specs2.mutable.Specification
import gov.dwp.carers.xml.validation.XmlValidatorFactory
import controllers.submission.XmlSubmitter

class ClaimXMLAcceptanceSpec extends Specification {
  "Claim XML" should {
    "validate" in {
      val xmlValidator = XmlValidatorFactory.buildCaValidator()
      val xmlString = new XmlSubmitter().buildFullClaim(xmlValidator, claimXML).buildString(stripComments = true)

      xmlValidator.validate(writeXML(xmlString)) should beTrue
    }
  }

  val claimXML =
    <DWPCAClaim id="TEST432">
      <Claimant>
        <DateOfClaim>2013-01-05</DateOfClaim>
        <Surname>CaseFour</Surname>
        <OtherNames>Test Middle</OtherNames>
        <OtherSurnames>Thornhill</OtherSurnames>
        <Title>Mr</Title>
        <MaritalStatus>m</MaritalStatus>
        <DateOfBirth>1951-01-01</DateOfBirth>
        <NationalInsuranceNumber>JB486248C</NationalInsuranceNumber>
        <ExistingNationalInsuranceNumber/>
        <Address><gds:Line>4 Preston Road</gds:Line><gds:Line>Preston</gds:Line><gds:Line>Lancashire</gds:Line><gds:PostCode>PR1 2TH</gds:PostCode></Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <HomePhoneNumber></HomePhoneNumber>
        <DaytimePhoneNumber>
          <Number>01772 888901</Number>
          <Qualifier/>
        </DaytimePhoneNumber>
        <EmailAddress/>
        <ClaimedBefore>Not asked</ClaimedBefore>
      </Claimant>
      <Caree>
        <Surname>watson</Surname>
        <OtherNames>Cloe scott</OtherNames>
        <Title>Dame</Title>
        <DateOfBirth>1951-07-03</DateOfBirth>
        <NationalInsuranceNumber></NationalInsuranceNumber>
        <Address><gds:Line>12 Preston Road</gds:Line><gds:Line>Preston</gds:Line><gds:Line>Lancashire</gds:Line><gds:PostCode>PR1 1HB</gds:PostCode></Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <HomePhoneNumber/>
        <DaytimePhoneNumber>
          <Number></Number>
          <Qualifier/>
        </DaytimePhoneNumber>
        <RelationToClaimant>mother</RelationToClaimant>
        <Cared35hours>yes</Cared35hours>
        <CanSomeoneElseSign>Not asked</CanSomeoneElseSign>
        <CanClaimantSign>Not asked</CanClaimantSign>
        <BreaksSinceClaim>yes</BreaksSinceClaim>
        <CareBreak>
          <StartDateTime>2010-07-10T10:00:00</StartDateTime>
          <EndDateTime>2010-08-17T17:45:00</EndDateTime>
          <Reason>Home</Reason>
          <MedicalCare>yes</MedicalCare>
          <AwayFromHome>Not asked</AwayFromHome>
        </CareBreak>
        <Cared35hoursBefore>yes</Cared35hoursBefore>
        <DateStartedCaring>2010-05-01</DateStartedCaring>
        <BreaksBeforeClaim>yes</BreaksBeforeClaim>
        <PaidForCaring>Not asked</PaidForCaring>
        <ClaimedPreviously>Not asked</ClaimedPreviously>
      </Caree>
      <Residency>
        <!--Nationality>British</Nationality-->
        <EUEEASwissNational>Not asked</EUEEASwissNational>
        <CountryNormallyLive>Not asked</CountryNormallyLive>
        <CountryNormallyLiveOther>Not asked</CountryNormallyLiveOther>
        <InGreatBritainNow>yes</InGreatBritainNow>
        <InGreatBritain26Weeks>Not asked</InGreatBritain26Weeks>

        <BritishOverseasPassport>Not asked</BritishOverseasPassport>

        <OutOfGreatBritain>Not asked</OutOfGreatBritain>

      </Residency>
      <CourseOfEducation>no</CourseOfEducation>

      <SelfEmployed>yes</SelfEmployed>
      <SelfEmployment>
        <SelfEmployedNow>yes</SelfEmployedNow>
        <CurrentJobDetails>
          <DateStarted>2010-02-01</DateStarted>
          <NatureOfBusiness>Fruit and veg, delivery service</NatureOfBusiness>
          <TradingYear>
            <DateFrom>2012-02-01</DateFrom>
            <DateTo>2013-01-31</DateTo>
          </TradingYear>
        </CurrentJobDetails>
        <CareExpensesChildren>
          <QuestionLabel>chld.expenses</QuestionLabel>
          <Answer>Yes</Answer>
        </CareExpensesChildren>
        <ChildCareExpenses>
          <CarerName>
            <QuestionLabel>child.carer</QuestionLabel>
            <Answer>Mr John Johnson</Answer>
          </CarerName>
          <RelationshipCarerToClaimant>
            <QuestionLabel>child.care.rel.claimant</QuestionLabel>
            <Answer>Brother</Answer>
          </RelationshipCarerToClaimant>
        </ChildCareExpenses>
        <CareExpensesCaree>
          <QuestionLabel>care.expenses</QuestionLabel>
          <Answer>Yes</Answer>
        </CareExpensesCaree>
        <CareExpenses>
          <CarerName>
            <QuestionLabel>child.carer</QuestionLabel>
            <Answer>Mrs Terry Thornhill</Answer>
          </CarerName>
          <RelationshipCarerToClaimant>
            <QuestionLabel>child.care.rel.claimant</QuestionLabel>
            <Answer>Aunt</Answer>
          </RelationshipCarerToClaimant>
          <RelationshipCarerToCaree>
            <QuestionLabel>care.carer.rel.caree</QuestionLabel>
            <Answer>adoptedSon</Answer>
          </RelationshipCarerToCaree>
        </CareExpenses>
        <PaidForPension>
          <QuestionLabel>self.pension</QuestionLabel>
          <Answer>Yes</Answer>
        </PaidForPension>
        <PensionScheme>
          <Payment>
            <QuestionLabel>self.pension.amount</QuestionLabel>
            <Answer>
              <Currency>GBP</Currency>
              <Amount>15.23</Amount>
            </Answer>
          </Payment>
          <Frequency>
            <QuestionLabel>self.pension.frequency</QuestionLabel>
            <Answer>Weekly</Answer>
          </Frequency>
        </PensionScheme>
      </SelfEmployment>
      <Employed>yes</Employed>
      <Employment>
        <CurrentlyEmployed>yes</CurrentlyEmployed>
        <JobDetails>
          <Employer>
            <DateJobStarted>
              <QuestionLabel>job.started</QuestionLabel>
              <Answer>01-01-2013</Answer>
            </DateJobStarted>
            <JobType>
              <QuestionLabel>job.title</QuestionLabel>
              <Answer>Hacker</Answer>
            </JobType>
            <Name>Tesco's Bank</Name>
            <Address><gds:Line>23 Yeadon Way</gds:Line><gds:Line>Blackpool</gds:Line><gds:Line>Lancashire</gds:Line><gds:PostCode>FY4 5TH</gds:PostCode></Address>
            <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
            <EmployersPhoneNumber>01253 667889</EmployersPhoneNumber>
            <EmployersFaxNumber>01253 667889</EmployersFaxNumber>
          </Employer>
          <Pay>
            <WeeklyHoursWorked>
              <QuestionLabel>job.hours</QuestionLabel>
              <Answer>25</Answer>
            </WeeklyHoursWorked>
            <DateLastPaid>
              <QuestionLabel>job.lastpaid</QuestionLabel>
              <Answer>08-07-2013</Answer>
            </DateLastPaid>
            <GrossPayment>
              <QuestionLabel>job.pay</QuestionLabel>
              <Answer>
                <Currency>GBP</Currency>
                <Amount>340.00</Amount>
              </Answer>
            </GrossPayment>
            <IncludedInWage>
              <QuestionLabel>job.pay.include</QuestionLabel>
              <Answer>All amounts due</Answer>
            </IncludedInWage>
            <PayFrequency>
              <QuestionLabel>job.pay.frequency</QuestionLabel>
              <Answer>Other</Answer>
            </PayFrequency>
            <UsualPayDay>two weeks ago</UsualPayDay>
          </Pay>
          <OweMoney>
            <QuestionLabel>job.owe</QuestionLabel>
            <Answer>No</Answer>
          </OweMoney>
          <CareExpensesChildren>
            <QuestionLabel>chld.expenses</QuestionLabel>
            <Answer>Yes</Answer>
          </CareExpensesChildren>
          <ChildCareExpenses>
          <CarerName>
            <QuestionLabel>child.carer</QuestionLabel>
            <Answer>Mr Grandfather Senior</Answer>
          </CarerName>
            <RelationshipCarerToClaimant>
              <QuestionLabel>child.care.rel.claimant</QuestionLabel>
              <Answer>Father</Answer>
            </RelationshipCarerToClaimant>
        </ChildCareExpenses>
          <CareExpensesCaree>
            <QuestionLabel>care.expenses</QuestionLabel>
            <Answer>Yes</Answer>
          </CareExpensesCaree>
          <CareExpenses>
          <CarerName>
            <QuestionLabel>child.carer</QuestionLabel>
            <Answer>Carers UK Ltd</Answer>
          </CarerName>
          <RelationshipCarerToClaimant>
            <QuestionLabel>child.care.rel.claimant</QuestionLabel>
            <Answer>Father</Answer>
          </RelationshipCarerToClaimant>
          <RelationshipCarerToCaree>
            <QuestionLabel>care.carer.rel.caree</QuestionLabel>
            <Other>None</Other>
            <Answer>Other</Answer>
          </RelationshipCarerToCaree>
        </CareExpenses>
          <PaidForOccupationalPension>
            <QuestionLabel>pension.occupational</QuestionLabel>
            <Answer>No</Answer>
          </PaidForOccupationalPension>
          <PaidForPersonalPension>
            <QuestionLabel>pension.personal</QuestionLabel>
            <Answer>No</Answer>
          </PaidForPersonalPension>
          <PaidForJobExpenses>
            <QuestionLabel>job.expenses</QuestionLabel>
            <Answer>Yes</Answer>
          </PaidForJobExpenses>
          <JobExpenses>
          <Expense>
            <QuestionLabel>job.expense</QuestionLabel>
            <Answer>Petrol money for driving</Answer>
          </Expense>
        </JobExpenses>
        </JobDetails>
      </Employment>
      <PropertyRentedOut>
        <PayNationalInsuranceContributions/>
        <RentOutProperty>Not asked</RentOutProperty>
        <SubletHome>Not asked</SubletHome>
      </PropertyRentedOut>
      <HavePartner>no</HavePartner>

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
          <OtherSocialSecurityBenefit>Not asked</OtherSocialSecurityBenefit>
          <NonSocialSecurityBenefit>Not asked</NonSocialSecurityBenefit>
          <NoBenefits>Not asked</NoBenefits>
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
          <OtherSocialSecurityBenefit>Not asked</OtherSocialSecurityBenefit>
          <NonSocialSecurityBenefit>Not asked</NonSocialSecurityBenefit>
          <NoBenefits>Not asked</NoBenefits>
        </PartnerBenefits>
        <ExtraMoney>Not asked</ExtraMoney>
        <OtherMoneySSP>no</OtherMoneySSP>
        <OtherMoneySMP>no</OtherMoneySMP>
      </OtherBenefits>
      <Payment>
        <PaymentFrequency>fourWeekly</PaymentFrequency>
        <InitialAccountQuestion>bankBuildingAccount</InitialAccountQuestion>
        <Account>
          <DirectPayment>Not asked</DirectPayment>
          <AccountHolder>yourName</AccountHolder>
          <HolderName>Mr Test Casefour</HolderName>
          <SecondHolderName/>
          <AccountType>bank</AccountType>
          <OtherBenefitsToBePaidDirect/>
          <BuildingSocietyDetails>
            <BuildingSocietyQualifier/>
            <AccountNumber>12345678</AccountNumber>
            <RollNumber>1.23E+12</RollNumber>
            <SortCode>090123</SortCode>
            <Name>Lloyds</Name>
            <Branch></Branch>
            <Address><gds:Line></gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
            <ConfirmAddress>yes</ConfirmAddress>
          </BuildingSocietyDetails>
        </Account>
      </Payment>
      <ThirdParty>no</ThirdParty>
      <Declaration>

        <TextLine>We may wish to contact any current or previous employers, or other persons or organisations you have listed on this claim form to obtain information about your claim. You do not have to agree to this but if you do not, it may mean that we are unable to obtain enough information to satisfy ourselves that you meet the conditions of entitlement for your claim.</TextLine>
        <TextLine>Do you agree to us obtaining information from any current or previous employer(s) you may have listed on this claim form?</TextLine>
        <TextLine>Yes</TextLine>

        <TextLine>Do you agree to us obtaining information from any other persons or organisations you may have listed on this claim form?</TextLine>
        <TextLine>Yes</TextLine>

        <TextLine>This is my claim for Carer's Allowance.</TextLine>

        <TextLine>If you live in Wales and would like to receive future communications in Welsh, please select this box.</TextLine>
        <TextLine>Yes</TextLine>
        <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
        <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
      </Declaration>
      <EvidenceList>
        <TextLine>
          Send us the following documents below including your Name and National Insurance (NI) number.
        </TextLine><TextLine/><TextLine>
        Your Employment documents
      </TextLine><TextLine>
        Last payslip you got before your claim date:  05/01/2013
      </TextLine><TextLine>
        Any payslips you have had since then
      </TextLine><TextLine/><TextLine>
        Your Self-employed documents
      </TextLine><TextLine>
        Most recent finalised accounts you have for your busines
      </TextLine><TextLine/><TextLine>
        Send the above documents to:
      </TextLine><TextLine>
        CA Freepost
      </TextLine><TextLine>
        Palatine House
      </TextLine><TextLine>
        Preston
      </TextLine><TextLine>
        PR1 1HN
      </TextLine><TextLine>
        The Carer's Allowance unit will contact you if they need any further information.
      </TextLine><TextLine/><TextLine>
        ============Can you get Carers Allowance?============
      </TextLine><TextLine>
        Does the person you care for get one of these benefits? =  yes
      </TextLine><TextLine>
        Do you spend 35 hours or more each week caring for the person you care for? =  yes
      </TextLine><TextLine>
        Do you normally live in Great Britain? =  yes
      </TextLine><TextLine>
        Are you aged 16 or over? =  yes
      </TextLine><TextLine>
        ======================About You======================
      </TextLine><TextLine>
        Have you always lived in the UK? =  yes
      </TextLine><TextLine>
        Mobile number =  0771 5419808
      </TextLine><TextLine/><TextLine>
        Do you get state Pension? =  no
      </TextLine><TextLine/><TextLine>
        ==================About Your Partner==================
      </TextLine><TextLine/><TextLine>
        ================About Care You Provide================
      </TextLine><TextLine>
        Do they live at the same address as you? =  yes
      </TextLine><TextLine>
        Does this person get Armed Forces Independence Payment? =  no
      </TextLine><TextLine>
        Where was the person you care for during the break? =  Hospital
      </TextLine><TextLine/><TextLine>
        =====================Time abroad=====================
      </TextLine><TextLine>
        Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands? =  yes
      </TextLine><TextLine>
        Have you had any more trips out of Great Britain for more than 52 weeks at a time, since 05/01/2013 (this is 156 weeks before your claim date)? =  no
      </TextLine><TextLine>
        Have you been out of Great Britain with the person you care for, for more than four weeks at a time, since 05/01/2013 (this is 3 years before your claim date)? =  no
      </TextLine><TextLine>
        ======================Employment======================
      </TextLine><TextLine>
        Employer:Tesco's Bank
      </TextLine><TextLine>

      </TextLine><TextLine>
        ===================Self Employment===================
      </TextLine><TextLine>
        Are the income, outgoings and profit in these accounts similar to your current level of trading? =  no
      </TextLine><TextLine>
        Please tell us why and when the change happened =  Depends on the seasons, and productivity of the fruit.
      </TextLine><TextLine>
        How often [[past=did you]] [[present=do you]] childcare expenses =  02
      </TextLine><TextLine>
        How often [[past=did you]] [[present=do you]] pay expenses related to the person you care for =  1235.1
      </TextLine><TextLine>
        =====================Other Money=====================
      </TextLine><TextLine>
        Have you &lt;or your partner/spouse&gt; claimed or received any other benefits since the date you want to claim? =  no
      </TextLine><TextLine>
        Have you received any payments for the person you care for or any other person since your claim date? =  no
      </TextLine><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine>
        Are you, your wife, husband, civil partner or parent you are dependent on, receiving  any pensions or benefits from another EEA State or Switzerland? =  no
      </TextLine><TextLine>
        Are you, your wife, husband, civil partner or parent you are dependent on working in or paying insurance to another EEA State or Switzerland? =  no
      </TextLine><TextLine>
        ===============Consent and Declaration===============
      </TextLine><TextLine>
        Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? =  yes
      </TextLine><TextLine/><TextLine>
        Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? =  yes
      </TextLine><TextLine/><TextLine>
        Disclaimer text and tick box =  yes
      </TextLine><TextLine>
        Declaration tick box =  yes
      </TextLine><TextLine>
        Someone else tick box =  yes
      </TextLine><TextLine>
        Do you live in Wales and would like to receive future communications in Welsh? =  yes
      </TextLine>
      </EvidenceList>
    </DWPCAClaim>
}
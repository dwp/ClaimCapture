package models.domain

import org.specs2.mutable.Specification
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import controllers.submission.XmlSubmitter
import xml.claim.DWPCAClaim

class ClaimAcceptanceSpec extends Specification {
  "Claim" should {
    """always lived in UK
       and other stuff""" in {

      val xmlValidator = XmlValidatorFactory.buildCaValidator()
      val claimXML = DWPCAClaim.xml(claim, "TEST224")
      val xmlString = new XmlSubmitter().buildFullClaim(xmlValidator, claimXML).buildString(stripComments = true)

      xmlValidator.validate(writeXML(xmlString)) should beTrue
    }
  }
}

/*
This case gives the following error:

[info] application - Starting new claim (old claim will be erased!)
[info] application - Build DWPCAClaim : TEST432
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 127; columnNumber: 38; cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
org.xml.sax.SAXParseException: cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 127; columnNumber: 38; cvc-type.3.1.3: The value '' of element 'DateStarted' is not valid.
org.xml.sax.SAXParseException: cvc-type.3.1.3: The value '' of element 'DateStarted' is not valid.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 133; columnNumber: 34; cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
org.xml.sax.SAXParseException: cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 133; columnNumber: 34; cvc-type.3.1.3: The value '' of element 'DateEnded' is not valid.
org.xml.sax.SAXParseException: cvc-type.3.1.3: The value '' of element 'DateEnded' is not valid.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 153; columnNumber: 24; cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
org.xml.sax.SAXParseException: cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 153; columnNumber: 24; cvc-type.3.1.3: The value '' of element 'DateJobStarted' is not valid.
org.xml.sax.SAXParseException: cvc-type.3.1.3: The value '' of element 'DateJobStarted' is not valid.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 166; columnNumber: 27; cvc-datatype-valid.1.2.1: '' is not a valid value for 'integer'.
org.xml.sax.SAXParseException: cvc-datatype-valid.1.2.1: '' is not a valid value for 'integer'.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] c.d.c.s.x.v.XmlErrorHandler - XmlErrorHandler: org.xml.sax.SAXParseException; lineNumber: 166; columnNumber: 27; cvc-type.3.1.3: The value '' of element 'WeeklyHoursWorked' is not valid.
org.xml.sax.SAXParseException: cvc-type.3.1.3: The value '' of element 'WeeklyHoursWorked' is not valid.
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:198) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:134) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:437) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:368) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:325) ~[na:1.7.0_25]
	at com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:453) ~[na:1.7.0_25]
[error] application - <DWPBody xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/ca/claim file:/Users/jmi/Temp/dwp-ca-claim-v1_10.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dsig="http://www.w3.org/2000/09/xmldsig#" xmlns:gms="http://www.govtalk.gov.uk/CM/gms" xmlns:dcq="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns="http://www.govtalk.gov.uk/dwp/ca/claim" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666">
      <DWPEnvelope>
        <DWPCAHeader>
          <TestMessage>5</TestMessage>
          <Keys>
            <Key type="}~e"></Key>
            <Key type="Z}"></Key>
          </Keys>
          <Language>en</Language>
          <DefaultCurrency>GBP</DefaultCurrency>
          <Manifest>
            <Reference>
              <Namespace>http://PtqKCMVh/</Namespace>
              <SchemaVersion></SchemaVersion>
              <TopElementName>FZXic.rwPpxsw5wsX</TopElementName>
            </Reference>
            <Reference>
              <Namespace>http://jwJGvJlj/</Namespace>
              <SchemaVersion></SchemaVersion>
              <TopElementName>vaN1Eh5z61pekYlfOv-vP0sGy</TopElementName>
            </Reference>
          </Manifest>
          <TransactionId>TEST432</TransactionId>
        </DWPCAHeader><DWPCAClaim id="TEST432">
      <Claimant>
      <DateOfClaim>2013-01-01</DateOfClaim>
      <Surname>Doo</Surname>
      <OtherNames>Scooby</OtherNames>
      <OtherSurnames></OtherSurnames>
      <Title>mr</Title>
      <MaritalStatus>m</MaritalStatus>
      <DateOfBirth>1980-01-01</DateOfBirth>
      <NationalInsuranceNumber></NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber/>
      <Address><gds:Line>Scooby Street</gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
      <ConfirmAddress>yes</ConfirmAddress>
      <HomePhoneNumber></HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number></Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>Not asked</ClaimedBefore>
    </Claimant>
      <Caree>
      <Surname>Schoo</Surname>
      <OtherNames>Daphne </OtherNames>
      <Title>mrs</Title>
      <DateOfBirth>1980-01-01</DateOfBirth>
      <NationalInsuranceNumber></NationalInsuranceNumber>
      <Address><gds:Line>Scooby Street</gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
      <ConfirmAddress>yes</ConfirmAddress>
      <HomePhoneNumber/>
      <DaytimePhoneNumber>
        <Number></Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <RelationToClaimant>wife</RelationToClaimant>
      <Cared35hours>yes</Cared35hours>
      <CanCareeSign>Not asked</CanCareeSign>
      <CanSomeoneElseSign>Not asked</CanSomeoneElseSign>
      <CanClaimantSign>Not asked</CanClaimantSign>
      <BreaksSinceClaim>yes</BreaksSinceClaim>
      <CareBreak>
        <StartDateTime>2000-01-01T00:00:00</StartDateTime>
        <EndDateTime></EndDateTime>
        <Reason>Home</Reason>
        <MedicalCare>no</MedicalCare>
        <AwayFromHome>Not asked</AwayFromHome>
      </CareBreak>
      <Cared35hoursBefore>yes</Cared35hoursBefore>
      <DateStartedCaring>2000-01-01</DateStartedCaring>
      <BreaksBeforeClaim>yes</BreaksBeforeClaim>
      <PaidForCaring>Not asked</PaidForCaring>
      <ClaimedPreviously>Not asked</ClaimedPreviously>
    </Caree>
      <ClaimADI>no</ClaimADI>
      <Residency>
      <Nationality>Scottish</Nationality>
      <EUEEASwissNational>Not asked</EUEEASwissNational>
      <CountryNormallyLive>Not asked</CountryNormallyLive>
      <CountryNormallyLiveOther>Not asked</CountryNormallyLiveOther>
      <InGreatBritainNow>yes</InGreatBritainNow>
      <InGreatBritain26Weeks>Not asked</InGreatBritain26Weeks>
      <PeriodAbroadLastYear>
        <Period>
          <DateFrom>2010-01-01</DateFrom>
          <DateTo>2010-02-01</DateTo>
        </Period>
        <Reason>Get a suntan</Reason>
        <Country>Scotland</Country>
      </PeriodAbroadLastYear>
      <BritishOverseasPassport>Not asked</BritishOverseasPassport>

      <OutOfGreatBritain>Not asked</OutOfGreatBritain>
      <PeriodAbroadDuringCare>
        <Period>
          <DateFrom>2010-02-01</DateFrom>
          <DateTo>2011-02-01</DateTo>
        </Period>
        <Reason>Get a suntan</Reason>
      </PeriodAbroadDuringCare>
    </Residency>
      <CourseOfEducation>yes</CourseOfEducation>
      <FullTimeEducation>
        <CourseDetails>
      <Type></Type>
      <Title>Hacking</Title>
      <HoursSpent></HoursSpent>
      <DateStarted></DateStarted>
      <DateStopped></DateStopped>
      <ExpectedEndDate></ExpectedEndDate>
    </CourseDetails>
        <LocationDetails>
      <Name>Hackerversity</Name>
      <Address><gds:Line></gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
      <PhoneNumber></PhoneNumber>
      <FaxNumber></FaxNumber>
      <StudentReferenceNumber></StudentReferenceNumber>
      <Tutor></Tutor>
    </LocationDetails>
      </FullTimeEducation>
      <SelfEmployed>yes</SelfEmployed>
      <SelfEmployment>
        <SelfEmployedNow>no</SelfEmployedNow>
        <RecentJobDetails>
          <DateStarted></DateStarted>
          <NatureOfBusiness></NatureOfBusiness>
          <TradingYear>
            <DateFrom></DateFrom>
            <DateTo></DateTo>
          </TradingYear>
          <DateEnded></DateEnded>
          <TradingCeased></TradingCeased>
        </RecentJobDetails>
        <Accountant>
          <HasAccountant>no</HasAccountant>
          <ContactAccountant>no</ContactAccountant>
        </Accountant>
        <CareExpensesChildren>no</CareExpensesChildren>

        <CareExpensesCaree>no</CareExpensesCaree>

        <PaidForPension>no</PaidForPension>

      </SelfEmployment>
      <Employed>yes</Employed>
      <Employment>
        <CurrentlyEmployed>yes</CurrentlyEmployed>
        <DateLastWorked>1970-01-01</DateLastWorked>
        <JobDetails>
            <Employer>
      <DateJobStarted/>
      <DateJobEnded/>
      <JobType></JobType>
      <ClockPayrollNumber/>
      <Name>Hackers R Us</Name>
      <Address><gds:Line></gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
      <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
      <EmployersPhoneNumber/>
      <EmployersFaxNumber/>
      <WagesDepartment/>
      <DepartmentPhoneFaxNumber/>
    </Employer>
            <Pay>
      <WeeklyHoursWorked/>
      <DateLastWorked/>
      <DateLastPaid/>
      <GrossPayment>
        <Currency>GBP</Currency>
        <Amount/>
      </GrossPayment>
      <IncludedInWage/>
      <PayPeriod>
        <DateFrom></DateFrom>
        <DateTo></DateTo>
      </PayPeriod>
      <PayFrequency/><PayFrequencyOther/>
      <UsualPayDay>Not asked</UsualPayDay>
      <VaryingEarnings>no</VaryingEarnings>
    </Pay>
            <OtherThanMoney>Not asked</OtherThanMoney>
            <OweMoney>no</OweMoney>
            <CareExpensesChildren>no</CareExpensesChildren>
            <CareExpensesCaree>no</CareExpensesCaree>
            <PaidForOccupationalPension>no</PaidForOccupationalPension><PaidForPersonalPension>no</PaidForPersonalPension>
            <PaidForJobExpenses>no</PaidForJobExpenses>
          </JobDetails>
      </Employment>
      <PropertyRentedOut>
      <PayNationalInsuranceContributions/>
      <RentOutProperty>Not asked</RentOutProperty>
      <SubletHome>Not asked</SubletHome>
    </PropertyRentedOut>
      <HavePartner>yes</HavePartner>
      <Partner>
        <NationalityPartner></NationalityPartner>
        <Surname>Schoo</Surname>
        <OtherNames>Daphne </OtherNames>
        <OtherSurnames></OtherSurnames>
        <Title>mrs</Title>
        <DateOfBirth>1980-01-01</DateOfBirth>
        <NationalInsuranceNumber></NationalInsuranceNumber>
        <Address><gds:Line>Not asked</gds:Line><gds:Line></gds:Line><gds:Line></gds:Line><gds:PostCode></gds:PostCode></Address>
        <ConfirmAddress>yes</ConfirmAddress>
        <RelationshipStatus>
          <JoinedHouseholdAfterDateOfClaim>Not asked</JoinedHouseholdAfterDateOfClaim>
          <JoinedHouseholdDate></JoinedHouseholdDate>
          <SeparatedFromPartner>no</SeparatedFromPartner>
          <SeparationDate></SeparationDate>
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
      <PaymentFrequency>Not asked</PaymentFrequency>
      <InitialAccountQuestion>Not asked</InitialAccountQuestion>

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
      <TextLine>No</TextLine>
      <TextLine>If you want to view or print a full, printer-friendly version of the information you have entered on this claim, please use the buttons provided.</TextLine>
      <TextLine>Do not send us the printed version. This is for your personal records only.</TextLine>
    </Declaration>
      <EvidenceList>
      <TextLine>Send us the following documents below including your Name and National Insurance (NI) number.</TextLine><TextLine/><TextLine>Your Employment documents</TextLine><TextLine>Last payslip you got before your claim date:  01/01/2013</TextLine><TextLine>Any payslips you have had since then</TextLine><TextLine/><TextLine>Your Self-employed documents</TextLine><TextLine>Most recent finalised accounts you have for your busines</TextLine><TextLine/><TextLine>Send the above documents to:</TextLine><TextLine>CA Freepost</TextLine><TextLine>Palatine House</TextLine><TextLine>Preston</TextLine><TextLine>PR1 1HN</TextLine><TextLine>The Carer's Allowance unit will contact you if they need any further information.</TextLine><TextLine/>
      <TextLine>============Can you get Carers Allowance?============</TextLine><TextLine>Does the person you care for get one of these benefits? =  yes</TextLine><TextLine>Do you spend 35 hours or more each week caring for the person you care for? =  yes</TextLine><TextLine>Do you normally live in Great Britain? =  yes</TextLine><TextLine>Are you aged 16 or over? =  yes</TextLine>
      <TextLine>======================About You======================</TextLine><TextLine>Have you always lived in the UK? =  yes</TextLine><TextLine/><TextLine/><TextLine>Do you get state Pension? =  yes</TextLine><TextLine/>
      <TextLine>==================About Your Partner==================</TextLine><TextLine>Does your partner/spouse live at the same address as you? =  Not asked</TextLine><TextLine>Is your partner/spouse the person you are claiming Carer's Allowance for? =  yes</TextLine>
      <TextLine>================About Care You Provide================</TextLine><TextLine>Do they live at the same address as you? =  yes</TextLine><TextLine/>
      <TextLine>Where was the person you care for during the break? =  Hospital</TextLine><TextLine/>
      <TextLine>=====================Time abroad=====================</TextLine><TextLine>Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands? =  yes</TextLine><TextLine>Have you had any more trips out of Great Britain for more than 52 weeks at a time, since 01/01/2013 (this is 156 weeks before your claim date)? =  yes</TextLine><TextLine>Have you been out of Great Britain with the person you care for, for more than four weeks at a time, since 01/01/2013 (this is 3 years before your claim date)? =  yes</TextLine>
      <TextLine>Where did you go? =  Scotland</TextLine>
      <TextLine>=====================Other Money=====================</TextLine><TextLine>Have you &lt;or your partner/spouse&gt; claimed or received any other benefits since the date you want to claim? =  no</TextLine><TextLine>Have you received any payments for the person you care for or any other person since your claim date? =  no</TextLine><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine/><TextLine>Are you, your wife, husband, civil partner or parent you are dependent on, receiving  any pensions or benefits from another EEA State or Switzerland? =  no</TextLine><TextLine>Are you, your wife, husband, civil partner or parent you are dependent on working in or paying insurance to another EEA State or Switzerland? =  no</TextLine>
      <TextLine>===============Consent and Declaration===============</TextLine><TextLine>Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? =  yes</TextLine><TextLine/><TextLine>Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? =  yes</TextLine><TextLine/><TextLine>Disclaimer text and tick box =  yes</TextLine><TextLine>Declaration tick box =  yes</TextLine><TextLine>Someone else tick box =  yes</TextLine><TextLine>Do you live in Wales and would like to receive future communications in Welsh? =  no</TextLine>
    </EvidenceList>
    </DWPCAClaim>
      </DWPEnvelope>
    </DWPBody>
*/
package services.submission

import models.domain._
import play.api.Logger
import xml.{Claimant, OtherBenefits, FullTimeEducation, Residence}


case class ClaimSubmission(claim: Claim, transactionId : String) {
  val yourPartner = YourPartnerSubmission.buildYourPartner(claim)

  val careYouProvide = CareYouProvideSubmission.buildCareYouProvide(claim)

  val consentAndDeclaration = ConsentAndDeclarationSubmission.buildConsentAndDeclaration(claim)

  def buildDwpClaim = {
    Logger.info(s"Build Claim : $transactionId")
    <DWPCAClaim id={transactionId}>
      {Claimant.xml(claim)}
      {CareYouProvideSubmission.buildCaree(careYouProvide)}
      <ClaimADI>no</ClaimADI>
      {Residence.xml(claim)}
      <CourseOfEducation>yes</CourseOfEducation>
      {FullTimeEducation.xml(claim)}
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
      {OtherBenefits.xml(claim)}
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

        <TextLine>=======================================================================================                                              </TextLine>
        <TextLine>================ =========== Can you get Carers Allowance? ============================                                              </TextLine>
        <TextLine>Does the person you care for get one of these benefits? = Yes                                                                        </TextLine>
        <TextLine>Do you spend 35 hours or more each week caring for the person you look after? = Yes                                                  </TextLine>
        <TextLine>Do you normally live in Great Britain? = Yes                                                                                         </TextLine>
        <TextLine>Are you aged 16 or over? = No                                                                                                        </TextLine>
        <TextLine>================ ================== About You  =====================================                                                 </TextLine>
        <TextLine>Have you always lived in the UK? = Yes                                                                                               </TextLine>
        <TextLine>Mobile number = 07721 7161819                                                                                                        </TextLine>
        <TextLine>Are you currently living in the UK? = No                                                                                             </TextLine>
        <TextLine>Do you get state Pension? = No                                                                                                       </TextLine>
        <TextLine>================ =============== About Your Partner ==================================                                               </TextLine>
        <TextLine>Middle name = John                                                                                                                   </TextLine>
        <TextLine>Does your partner or spouse live at the same address as you? = Yes                                                                   </TextLine>
        <TextLine>Is your partner or spouse the person you are claiming Carer's Allowance for? = Yes                                                   </TextLine>
        <TextLine>================ ============== About Care You Provide ==============================                                                </TextLine>
        <TextLine>Do they live at the same address as you? = No                                                                                        </TextLine>
        <TextLine>Does this person get Armed Forces Independence Payment? = No                                                                         </TextLine>
        <TextLine>Daytime phone number = 0191 2357761                                                                                                  </TextLine>
        <TextLine>Mobile number = 0181 3839839                                                                                                         </TextLine>
        <TextLine>You act as = Parent                                                                                                                  </TextLine>
        <TextLine>Person acts as = Attorney                                                                                                            </TextLine>
        <TextLine>Full name = Lawyer Brown                                                                                                             </TextLine>
        <TextLine>Where was the person you care for during the break? = Hospital                                                                       </TextLine>
        <TextLine>Have you had any more breaks in care for this person since your claim date: : 20/3/2012 ?  = Yes                                     </TextLine>
        <TextLine>================ ================ Employment === ==============================                                                      </TextLine>
        <TextLine>Have you finished this job? = Yes                                                                                                    </TextLine>
        <TextLine>Did you pay for anything else necessary to do your job? = Yes                                                                        </TextLine>
        <TextLine>Have you had another job at any time since 20/6/2011, this is six months before your claim date of 20/3/2013 ? = Yes                 </TextLine>
        <TextLine>================ ================ Self Employment === ==============================                                                 </TextLine>
        <TextLine>Are these accounts prepared on a cash flow basis?                                                                                    </TextLine>
        <TextLine>Are the income, outgoings and profit in these accounts similar to your current level of trading?                                     </TextLine>
        <TextLine>Please tell us why and when the change happened                                                                                      </TextLine>
        <TextLine>=======================================================================================                                              </TextLine>

      </EvidenceList>
    </DWPCAClaim>
  }
}

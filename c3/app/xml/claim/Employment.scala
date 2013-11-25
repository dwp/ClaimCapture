package xml.claim

import app.{PensionPaymentFrequency, StatutoryPaymentFrequency, XMLValues}
import models.domain._
import xml.XMLHelper._
import app.XMLValues._
import scala.xml.{Elem, NodeSeq}
import xml.XMLComponent
import play.api.i18n.Messages
import utils.helpers.PastPresentLabelHelper._

object Employment extends XMLComponent{

  def xml(claim: Claim) = {
    val jobsQG = claim.questionGroup[Jobs].getOrElse(Jobs())
    val employment = claim.questionGroup[models.domain.Employment]

    if (jobsQG.jobs.length > 0 && employment.fold(false)(_.beenEmployedSince6MonthsBeforeClaim == "yes")) {
      // We will search in all jobs for at least one case finishedThisJob = "no" because that means he is currently employed
      val currentlyEmployed = jobsQG.jobs.count(_.apply(JobDetails) match {
        case Some(j: JobDetails) => j.finishedThisJob == no
        case _ => false
      }) match {
        case i if i > 0 => yes
        case _ => no
      }

      <Employment>
        <CurrentlyEmployed>
          <QuestionLabel>{Messages("finishedThisJob")}</QuestionLabel>
          <Answer>{currentlyEmployed match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </CurrentlyEmployed>

        {for (job <- jobsQG) yield {
        val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
        val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
        val additionalWageDetails = job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails())

        <JobDetails>
          {employerXml(job)}
          {payXml(jobDetails, lastWage, additionalWageDetails, claim)}
          <OweMoney>
            <QuestionLabel>{Messages("employerOwesYouMoney")}</QuestionLabel>
            <Answer>{additionalWageDetails.employerOwesYouMoney match {
              case "yes" => XMLValues.Yes
              case "no" => XMLValues.No
              case n => n
            }}</Answer>
          </OweMoney>
          {childcareExpensesXml(job,claim)}
          {careExpensesXml(job, claim)}
          {pensionSchemeXml(job,claim)}
          {jobExpensesXml(job, claim)}
        </JobDetails>
      }}
      </Employment>
    } else {
      NodeSeq.Empty
    }
  }

  def employerXml(job: Job): Elem = {
    val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
    val employerContactDetails = job.questionGroup[EmployerContactDetails].getOrElse(EmployerContactDetails())

    <Employer>
      <DateJobStarted>
        <QuestionLabel>{Messages("jobStartDate")}</QuestionLabel>
        {<Answer/> +++ Some(jobDetails.jobStartDate.`dd-MM-yyyy`)}
      </DateJobStarted>
      {if(!jobDetails.lastWorkDate.isEmpty){
        <DateJobEnded>
          <QuestionLabel>{Messages("lastWorkDate")}</QuestionLabel>
          {<Answer/> +++ Some(jobDetails.lastWorkDate.get.`dd-MM-yyyy`)}
        </DateJobEnded>
      }}
      {job.title.isEmpty match {
        case false => {
          <JobType>
            <QuestionLabel>{
              Messages(s"jobTitle.${if (jobDetails.finishedThisJob == "yes") "was" else "is"}")
              }</QuestionLabel>
            <Answer>{job.title}</Answer>
          </JobType>
        }
        case true => NodeSeq.Empty
      }}
      {jobDetails.payrollEmployeeNumber.isEmpty match {
        case false => <ClockPayrollNumber>{jobDetails.payrollEmployeeNumber.orNull}</ClockPayrollNumber>
        case true => NodeSeq.Empty
      }}
      <Name>{jobDetails.employerName}</Name>
      {postalAddressStructure(employerContactDetails.address, employerContactDetails.postcode)}
      {employerContactDetails.phoneNumber.isEmpty match {
        case false => <EmployersPhoneNumber>{employerContactDetails.phoneNumber.orNull}</EmployersPhoneNumber>
        case true => NodeSeq.Empty
      }}
      {jobDetails.p45LeavingDate match {
      case Some(n) => {
        <P45LeavingDate>
          <QuestionLabel>{Messages("p45LeavingDate")}</QuestionLabel>
          {<Answer/> +++ Some(n.`dd-MM-yyyy`)}
        </P45LeavingDate>
      }
      case None => NodeSeq.Empty
    }}
    </Employer>
  }

  def payXml(jobDetails: JobDetails, lastWage: LastWage, additionalWageDetails: AdditionalWageDetails, claim: Claim): Elem = {
    <Pay>
      {jobDetails.hoursPerWeek match {
        case Some(n) => {
          <WeeklyHoursWorked>
            <QuestionLabel>{Messages("hoursPerWeek", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase, jobDetails.jobID))}</QuestionLabel>
            <Answer>{jobDetails.hoursPerWeek.orNull}</Answer>
          </WeeklyHoursWorked>
        }
        case None => NodeSeq.Empty
      }}
      {if(!lastWage.lastPaidDate.isEmpty){
        <DateLastPaid>
          <QuestionLabel>{Messages("lastPaidDate")}</QuestionLabel>
          {<Answer/> +++ Some(lastWage.lastPaidDate.get.`dd-MM-yyyy`)}
        </DateLastPaid>
      }}
      <GrossPayment>
        <QuestionLabel>{Messages("grossPay")}</QuestionLabel>
        <Answer>
          <Currency>{GBP}</Currency>
          <Amount>{lastWage.grossPay}</Amount>
        </Answer>
      </GrossPayment>


      {lastWage.payInclusions match {
        case Some(n) => {
          <IncludedInWage>
            <QuestionLabel>{Messages("payInclusions")}</QuestionLabel>
            {<Answer/> +++ lastWage.payInclusions}
          </IncludedInWage>}
        case None => NodeSeq.Empty
      }}
      {paymentFrequency(additionalWageDetails.oftenGetPaid)}
      {additionalWageDetails.whenGetPaid match {
        case Some(n) => {
          <UsualPayDay>
            <QuestionLabel>{Messages("whenGetPaid", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase, jobDetails.jobID))}</QuestionLabel>
            {<Answer/>+- additionalWageDetails.whenGetPaid}
          </UsualPayDay>}
        case None => NodeSeq.Empty
      }}
      {lastWage.sameAmountEachTime match {
        case Some(n) => {
          <ConstantEarnings>
            <QuestionLabel>{Messages("sameAmountEachTime", pastPresentLabelForEmployment(claim, didYou, doYou, jobDetails.jobID))}</QuestionLabel>
            <Answer>{n match {
                case "yes" => XMLValues.Yes
                case "no" => XMLValues.No
                case n => n
              }
            }</Answer>
          </ConstantEarnings>
        }
        case None => NodeSeq.Empty
      }}
    </Pay>
  }

  def howMuchOwedXml(s: Option[String]) = {
    val showXml = s.isDefined

    if (showXml) {
      <Payment>
        <Currency>{GBP}</Currency>
        <Amount>{s.orNull}</Amount>
      </Payment>
    } else {
      NodeSeq.Empty
    }
  }

  def pensionSchemeXml(job: Job, claim:Claim) = {
    val pensionScheme:PensionSchemes = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

    NodeSeq.Empty ++ {occupationalPensionSchemeXml(pensionScheme, claim, job)} ++ {personalPensionSchemeXml(pensionScheme, claim, job)}
  }

  def occupationalPensionSchemeXml(pensionScheme: PensionSchemes, claim:Claim, job:Job) = {
    val showXml = pensionScheme.payOccupationalPensionScheme == yes

    if (showXml) {
      <PaidForOccupationalPension>
        <QuestionLabel>{Messages("payOccupationalPensionScheme", pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID))}</QuestionLabel>
        <Answer>{pensionScheme.payOccupationalPensionScheme  match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
        }}</Answer>
      </PaidForOccupationalPension>

        <OccupationalPension>
          <Payment>
            <QuestionLabel>{Messages ("howMuchPension", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
            <Answer>
              <Currency>{GBP}</Currency>
              {<Amount/> +++ pensionScheme.howMuchPension}
            </Answer>
          </Payment>
          {pensionScheme.howOftenPension match{
            case Some(f) =>
            <Frequency>
              <QuestionLabel>{Messages("howOftenPension")}</QuestionLabel>
              {f.other match {
                case Some(s) => <Other>{s}</Other>
                case _ => NodeSeq.Empty
              }
              }
              <Answer>{f.frequency}</Answer>
            </Frequency>
            case _ => NodeSeq.Empty
            }
          }
        </OccupationalPension>
    } else {
      NodeSeq.Empty
    }
  }

  def personalPensionSchemeXml(pensionScheme:PensionSchemes, claim:Claim, job:Job): NodeSeq = {
    val showXml = pensionScheme.payPersonalPensionScheme == yes

    if (showXml) {
      <PaidForPersonalPension>
        <QuestionLabel>{Messages("payPersonalPensionScheme", pastPresentLabelForEmployment(claim, didYou, doYou,job.jobID))}</QuestionLabel>
        <Answer>{pensionScheme.payPersonalPensionScheme match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </PaidForPersonalPension>
        <PersonalPension>
          <Payment>
            <QuestionLabel>{Messages ("howMuchPersonal", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
            <Answer>
              <Currency>{GBP}</Currency>
              {<Amount/> +++ pensionScheme.howMuchPersonal}
            </Answer>
          </Payment>
          {
            pensionScheme.howOftenPersonal match{
              case Some(howOften) =>
                <Frequency>
                  <QuestionLabel>{Messages("howOftenPersonal")}</QuestionLabel>
                  {howOften.other match{
                    case Some(s) => <Other>{s}</Other>
                    case _ => NodeSeq.Empty
                  }}
                  <Answer>{howOften.frequency}</Answer>
                </Frequency>
              case _ => NodeSeq.Empty
            }
          }

        </PersonalPension>
    } else {
      NodeSeq.Empty
    }
  }

  def jobExpensesXml(job: Job, claim:Claim) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val necessaryExpenses: NecessaryExpenses = job.questionGroup[NecessaryExpenses].getOrElse(NecessaryExpenses())
    val showXml = aboutExpenses.payForAnythingNecessary == "yes"

    if (showXml) {
        <PaidForJobExpenses>
          <QuestionLabel>{Messages ("payForAnythingNecessary", pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID))}</QuestionLabel>
          <Answer>{aboutExpenses.payForAnythingNecessary match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </PaidForJobExpenses>
      <JobExpenses>
        <Expense>
          <QuestionLabel>{Messages("whatAreThose", pastPresentLabelForEmployment(claim, wereYou.toLowerCase.take(4), areYou.toLowerCase.take(3) , job.jobID))}</QuestionLabel>
          <Answer>{necessaryExpenses.whatAreThose}</Answer>
        </Expense>
      </JobExpenses>
    } else {
      <PaidForJobExpenses>
        <QuestionLabel>{Messages ("payForAnythingNecessary", pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID))}</QuestionLabel>
        <Answer>{aboutExpenses.payForAnythingNecessary match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </PaidForJobExpenses>
    }
  }

  def childcareExpensesXml(job: Job, claim:Claim) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val childcareExpenses: ChildcareExpenses = job.questionGroup[ChildcareExpenses].getOrElse(ChildcareExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren == yes

    if (showXml) {
        <CareExpensesChildren>
          <QuestionLabel>{Messages("payAnyoneToLookAfterChildren", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
          <Answer>{aboutExpenses.payAnyoneToLookAfterChildren match {
            case "yes" => XMLValues.Yes
            case "no" => XMLValues.No
            case n => n
          }}</Answer>
        </CareExpensesChildren>
      <ChildCareExpenses>
        <CarerName>
          <QuestionLabel>{Messages("whoLooksAfterChildren")}</QuestionLabel>
          <Answer>{childcareExpenses.whoLooksAfterChildren}</Answer>
        </CarerName>
        {childcareExpenses.howMuchCostChildcare.isEmpty match {
        case false => {
          <Expense>
              {childcareExpenses.howMuchCostChildcare.isEmpty match {
                case false => {
                  <Payment>
                    <QuestionLabel>{Messages("howMuchCostChildcare", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
                    <Answer>
                      <Currency>{GBP}</Currency>
                      <Amount>{childcareExpenses.howMuchCostChildcare}</Amount>
                    </Answer>
                  </Payment>
                }
                case true => NodeSeq.Empty
            }}
            <Frequency>
              <QuestionLabel>{Messages ("employment_howOftenPayChildCare", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
              {childcareExpenses.howOftenPayChildCare.other match {
                case Some(s) => <Other>{s}</Other>
                case _ => NodeSeq.Empty
              }}
              <Answer>{childcareExpenses.howOftenPayChildCare.frequency}</Answer>
            </Frequency>


          </Expense>
        }
        case _ => NodeSeq.Empty
      }}
        <RelationshipCarerToClaimant>
          <QuestionLabel>{Messages("relationToYou")}</QuestionLabel>
          <Answer>{childcareExpenses.relationToYou}</Answer>
        </RelationshipCarerToClaimant>
      </ChildCareExpenses>
    } else {
      <CareExpensesChildren>
        <QuestionLabel>{Messages ("payAnyoneToLookAfterChildren", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
        <Answer>{aboutExpenses.payAnyoneToLookAfterChildren match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </CareExpensesChildren>
    }
  }

  def careExpensesXml(job: Job, claim:Claim) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val personYouCareExpenses: PersonYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses].getOrElse(PersonYouCareForExpenses())

    val showXml = aboutExpenses.payAnyoneToLookAfterPerson == yes

    if (showXml) {
      <CareExpensesCaree>
        <QuestionLabel>{Messages ("payAnyoneToLookAfterPerson", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
        <Answer>{aboutExpenses.payAnyoneToLookAfterPerson match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </CareExpensesCaree>
      <CareExpenses>
        <CarerName>
          <QuestionLabel>{Messages("whoLooksAfterChildren")}</QuestionLabel>
          <Answer>{personYouCareExpenses.whoDoYouPay}</Answer>
        </CarerName>
        {personYouCareExpenses.howMuchCostCare.isEmpty match {
        case false => {
          <Expense>
            {personYouCareExpenses.howMuchCostCare.isEmpty match {
            case false => {
              <Payment>
                <QuestionLabel>{Messages("howMuchCostChildcare", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
                <Answer>
                  <Currency>{GBP}</Currency>
                  <Amount>{personYouCareExpenses.howMuchCostCare}</Amount>
                </Answer>
              </Payment>
            }
            case true => NodeSeq.Empty
          }}
            <Frequency>
              <QuestionLabel>{Messages("howMuchCostChildcare", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
              {personYouCareExpenses.howOftenPayCare.other match {
                case Some(s) => <Other>{s}</Other>
                case _ => NodeSeq.Empty
              }}
              <Answer>{personYouCareExpenses.howOftenPayCare.frequency}</Answer>
            </Frequency>


          </Expense>
        }
        case _ => NodeSeq.Empty
      }}
        <RelationshipCarerToClaimant>
          <QuestionLabel>{Messages("relationToYou")}</QuestionLabel>
          <Answer>{personYouCareExpenses.relationToYou}</Answer>
        </RelationshipCarerToClaimant>
        <RelationshipCarerToCaree>
          <QuestionLabel>{Messages("relationToPersonYouCare")}</QuestionLabel>
          <Answer>{personYouCareExpenses.relationToPersonYouCare}</Answer>
        </RelationshipCarerToCaree>
      </CareExpenses>
    } else {
      <CareExpensesCaree>
        <QuestionLabel>{Messages("didYouPayToLookAfterThePersonYouCaredFor", pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}</QuestionLabel>
        <Answer>{aboutExpenses.payAnyoneToLookAfterPerson match {
          case "yes" => XMLValues.Yes
          case "no" => XMLValues.No
          case n => n
        }}</Answer>
      </CareExpensesCaree>
    }
  }
}
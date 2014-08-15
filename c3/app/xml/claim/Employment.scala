package xml.claim

import app.XMLValues._
import models.domain._
import xml.XMLComponent
import xml.XMLHelper._
import models.{DayMonthYear, PaymentFrequency}

import scala.xml.{Elem, NodeSeq}
import models.PensionPaymentFrequency

object Employment extends XMLComponent{

  def xml(claim: Claim) = {
    val jobsQG = claim.questionGroup[Jobs].getOrElse(Jobs())
    val employment = claim.questionGroup[models.domain.Employment]

    if (jobsQG.jobs.length > 0 && employment.fold(false)(_.beenEmployedSince6MonthsBeforeClaim == "yes")) {

      <Employment>

        {for (job <- jobsQG) yield {
            val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
            val lastWage = job.questionGroup[LastWage].getOrElse(LastWage("", PaymentFrequency(),"",DayMonthYear(),"", None, None, ""))

            <JobDetails>
              {employerXml(job)}
              {payXml(jobDetails, lastWage, claim)}
              {question(<OweMoney/>, "employerOwesYouMoney",lastWage.employerOwesYouMoney)}
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

  private def employerXml(job: Job): Elem = {
    val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())

    <Employer>
      {question(<CurrentlyEmployed/>,"finishedThisJob",jobDetails.finishedThisJob)}
      {question(<DateJobStarted/>, "jobStartDate", jobDetails.jobStartDate)}
      {if(jobDetails.lastWorkDate.isDefined){
        {question(<DateJobEnded/>, "lastWorkDate",jobDetails.lastWorkDate)}
      }}
      {question(<JobType/>,s"jobTitle.${if (jobDetails.finishedThisJob == "yes") "past" else "present"}", job.title)}
      {question(<ClockPayrollNumber/>,"payrollEmployeeNumber", jobDetails.payrollEmployeeNumber)}
      {question(<Name/>, "employerName", jobDetails.employerName)}
      {postalAddressStructure("address", jobDetails.address, jobDetails.postcode.getOrElse("").toUpperCase)}
      {question(<EmployersPhoneNumber/>,"phoneNumber", jobDetails.phoneNumber)}
      {if(jobDetails.p45LeavingDate.isDefined){
        {question(<P45LeavingDate/>, "p45LeavingDate", jobDetails.p45LeavingDate)}
      }}
    </Employer>
  }

  private def payXml(jobDetails: JobDetails, lastWage: LastWage, claim: Claim): Elem = {
    val oftenPaid = lastWage.oftenGetPaid
    <Pay>
      {question(<WeeklyHoursWorked/>, "hoursPerWeek", jobDetails.hoursPerWeek, questionLabelEmployment(claim, "hoursPerWeek", jobDetails.jobID))}
      {question(<DateLastPaid/>, "lastPaidDate", lastWage.lastPaidDate)}
      {questionCurrency(<GrossPayment/>, "grossPay",Some(lastWage.grossPay))}
      {question(<IncludedInWage/>, "payInclusions", lastWage.payInclusions)}
      {questionOther(<PayFrequency/>,"oftenGetPaidFrequency",oftenPaid.frequency,oftenPaid.other, questionLabelEmployment(claim, "oftenGetPaidFrequency", jobDetails.jobID))}
      {question(<UsualPayDay/>, "whenGetPaid", lastWage.whenGetPaid, questionLabelEmployment(claim, "whenGetPaid", jobDetails.jobID))}
      {question(<ConstantEarnings/>,"sameAmountEachTime",lastWage.sameAmountEachTime,questionLabelEmployment(claim, "sameAmountEachTime", jobDetails.jobID))}
    </Pay>
  }

  private def pensionSchemeXml(job: Job, claim:Claim) = {
    val pensionScheme:PensionSchemes = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

    occupationalPensionSchemeXml(pensionScheme, claim, job) ++ personalPensionSchemeXml(pensionScheme, claim, job)
  }

  private def occupationalPensionSchemeXml(pensionScheme: PensionSchemes, claim:Claim, job:Job):NodeSeq = {
    val showXml = pensionScheme.payOccupationalPensionScheme.toLowerCase == yes

    question(<PaidForOccupationalPension/>,"payOccupationalPensionScheme",pensionScheme.payOccupationalPensionScheme,questionLabelEmployment(claim, "payOccupationalPensionScheme", job.jobID)) ++
    (if (showXml) {
        <OccupationalPension>
          {questionCurrency(<Payment/>,"howMuchPension",pensionScheme.howMuchPension,questionLabelEmployment(claim, "howMuchPension", job.jobID))}
          {questionOther(<Frequency/>,"howOftenPension",pensionScheme.howOftenPension.get.frequency,pensionScheme.howOftenPension.get.other)}
        </OccupationalPension>
    } else {
      NodeSeq.Empty
    })
  }

  private def personalPensionSchemeXml(pensionScheme:PensionSchemes, claim:Claim, job:Job): NodeSeq = {
    val showXml = pensionScheme.payPersonalPensionScheme.toLowerCase == yes

    question(<PaidForPersonalPension/>,"payPersonalPensionScheme",pensionScheme.payPersonalPensionScheme,questionLabelEmployment(claim, "payPersonalPensionScheme", job.jobID)) ++
    (if (showXml) {
        <PersonalPension>
          {questionCurrency(<Payment/>,"howMuchPersonal",pensionScheme.howMuchPersonal,questionLabelEmployment(claim, "howMuchPersonal", job.jobID))}
          {questionOther(<Frequency/>,"howOftenPersonal",pensionScheme.howOftenPersonal.get.frequency,pensionScheme.howOftenPersonal.get.other)}
        </PersonalPension>
    } else {
      NodeSeq.Empty
    })
  }

  private def jobExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val showXml = aboutExpenses.haveExpensesForJob.toLowerCase == "yes"

    if (showXml) {
        question(<PaidForJobExpenses/>,"haveExpensesForJob",aboutExpenses.haveExpensesForJob,questionLabelEmployment(claim, "haveExpensesForJob", job.jobID)) ++
        <JobExpenses>
          {question(<Expense/>,"whatExpensesForJob",aboutExpenses.whatExpensesForJob,questionLabelEmployment(claim, "whatExpensesForJob", job.jobID))}
        </JobExpenses>
    } else {
      question(<PaidForJobExpenses/>,"haveExpensesForJob",aboutExpenses.haveExpensesForJob,questionLabelEmployment(claim, "haveExpensesForJob", job.jobID))
    }
  }

  private def childcareExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren.toLowerCase == yes

    if (showXml) {
      question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren", aboutExpenses.payAnyoneToLookAfterChildren, questionLabelEmployment(claim, "payAnyoneToLookAfterChildren", job.jobID)) ++
      <ChildCareExpenses>
        {question(<CarerName/>, "nameLookAfterChildren",aboutExpenses.nameLookAfterChildren)}
        {aboutExpenses.howMuchLookAfterChildren.isEmpty match {
          case false =>
            <Expense>
              {questionCurrency(<Payment/>, "howMuchLookAfterChildren",aboutExpenses.howMuchLookAfterChildren, questionLabelEmployment(claim, "howMuchLookAfterChildren", job.jobID))}
              {questionOther(<Frequency/>, "employment_howOftenPayChildCare",aboutExpenses.howOftenLookAfterChildren.getOrElse(PensionPaymentFrequency("")).frequency,aboutExpenses.howOftenLookAfterChildren.getOrElse(PensionPaymentFrequency("")).other, "", questionLabelEmployment(claim, "employment_howOftenPayChildCare", job.jobID))}
            </Expense>

          case _ => NodeSeq.Empty
        }}
        {question(<RelationshipCarerToClaimant/>, "relationToYouLookAfterChildren",aboutExpenses.relationToYouLookAfterChildren)}
        {question(<RelationshipCarerToPersonYouCare/>, "relationToPersonLookAfterChildren",aboutExpenses.relationToPersonLookAfterChildren)}
      </ChildCareExpenses>
    } else {
      {question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren",aboutExpenses.payAnyoneToLookAfterChildren,questionLabelEmployment(claim, "payAnyoneToLookAfterChildren", job.jobID))}
    }
  }

  private def careExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterPerson.toUpperCase == yes.toUpperCase

    if (showXml) {
      question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,questionLabelEmployment(claim, "payAnyoneToLookAfterPerson", job.jobID)) ++
      <CareExpenses>
        {question(<CarerName/>,"nameLookAfterPerson",aboutExpenses.nameLookAfterPerson, questionLabelEmployment(claim, "nameLookAfterPerson", job.jobID))}
        {aboutExpenses.howMuchLookAfterPerson.isEmpty match {
          case false =>
            <Expense>
              {questionCurrency(<Payment/>,"howMuchLookAfterPerson",aboutExpenses.howMuchLookAfterPerson,questionLabelEmployment(claim, "howMuchLookAfterPerson", job.jobID))}
              {questionOther(<Frequency/>,"howOftenLookAfterPerson",aboutExpenses.howOftenLookAfterPerson.getOrElse(PensionPaymentFrequency("")).frequency,aboutExpenses.howOftenLookAfterPerson.getOrElse(PensionPaymentFrequency("")).other,"", questionLabelEmployment(claim, "howOftenPayCare", job.jobID))}
            </Expense>

          case _ => NodeSeq.Empty
        }}
        {question(<RelationshipCarerToClaimant/>,"relationToYouLookAfterPerson",aboutExpenses.relationToYouLookAfterPerson)}
        {question(<RelationshipCarerToCaree/>,"relationToPersonLookAfterPerson",aboutExpenses.relationToPersonLookAfterPerson)}
      </CareExpenses>
    } else {
      question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,questionLabelEmployment(claim, "payAnyoneToLookAfterPerson", job.jobID))
    }
  }
}
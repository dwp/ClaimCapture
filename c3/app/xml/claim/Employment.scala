package xml.claim

import models.domain._
import xml.XMLHelper._
import app.XMLValues._
import scala.xml.{Elem, NodeSeq}
import xml.XMLComponent
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
        {question(<CurrentlyEmployed/>,"finishedThisJob",currentlyEmployed)}
        {for (job <- jobsQG) yield {
            val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
            val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
            val additionalWageDetails = job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails())

            <JobDetails>
              {employerXml(job)}
              {payXml(jobDetails, lastWage, additionalWageDetails, claim)}
              {question(<OweMoney/>, "employerOwesYouMoney",additionalWageDetails.employerOwesYouMoney)}
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
    val employerContactDetails = job.questionGroup[EmployerContactDetails].getOrElse(EmployerContactDetails())

    <Employer>
      {question(<DateJobStarted/>, "jobStartDate", jobDetails.jobStartDate)}
      {question(<DateJobEnded/>, "lastWorkDate",jobDetails.lastWorkDate)}
      {question(<JobType/>,s"jobTitle.${if (jobDetails.finishedThisJob == "yes") "past" else "present"}", job.title)}
      {question(<ClockPayrollNumber/>,"payrollEmployeeNumber", jobDetails.payrollEmployeeNumber)}
      {question(<Name/>, "employerName", jobDetails.employerName)}
      {postalAddressStructure("address", employerContactDetails.address, employerContactDetails.postcode)}
      {question(<EmployersPhoneNumber/>,"phoneNumber", employerContactDetails.phoneNumber)}
      {question(<P45LeavingDate/>, "p45LeavingDate", jobDetails.p45LeavingDate)}
    </Employer>
  }

  private def payXml(jobDetails: JobDetails, lastWage: LastWage, additionalWageDetails: AdditionalWageDetails, claim: Claim): Elem = {
    val oftenPaid = additionalWageDetails.oftenGetPaid
    <Pay>
      {question(<WeeklyHoursWorked/>, "hoursPerWeek", jobDetails.hoursPerWeek, questionLabelEmployment(claim, "hoursPerWeek", jobDetails.jobID))}
      {question(<DateLastPaid/>, "lastPaidDate", lastWage.lastPaidDate)}
      {questionCurrency(<GrossPayment/>, "grossPay",Some(lastWage.grossPay))}
      {question(<IncludedInWage/>, "payInclusions", lastWage.payInclusions)}
      {questionOther(<PayFrequency/>,"oftenGetPaidFrequency",oftenPaid.frequency,oftenPaid.other, questionLabelEmployment(claim, "oftenGetPaidFrequency", jobDetails.jobID))}
      {question(<UsualPayDay/>, "whenGetPaid", additionalWageDetails.whenGetPaid, questionLabelEmployment(claim, "whenGetPaid", jobDetails.jobID))}
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
    val necessaryExpenses: NecessaryExpenses = job.questionGroup[NecessaryExpenses].getOrElse(NecessaryExpenses())
    val showXml = aboutExpenses.payForAnythingNecessary.toLowerCase == "yes"

    if (showXml) {
        question(<PaidForJobExpenses/>,"payForAnythingNecessary",aboutExpenses.payForAnythingNecessary,questionLabelEmployment(claim, "payForAnythingNecessary", job.jobID)) ++
        <JobExpenses>
          {question(<Expense/>,"whatAreThose",necessaryExpenses.whatAreThose,questionLabelEmployment(claim, "whatAreThose", job.jobID))}
        </JobExpenses>
    } else {
      question(<PaidForJobExpenses/>,"payForAnythingNecessary",aboutExpenses.payForAnythingNecessary,questionLabelEmployment(claim, "payForAnythingNecessary", job.jobID))
    }
  }

  private def childcareExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val childcareExpenses: ChildcareExpenses = job.questionGroup[ChildcareExpenses].getOrElse(ChildcareExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren.toLowerCase == yes

    if (showXml) {
      question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren", aboutExpenses.payAnyoneToLookAfterChildren, questionLabelEmployment(claim, "payAnyoneToLookAfterChildren", job.jobID)) ++
      <ChildCareExpenses>
        {question(<CarerName/>, "whoLooksAfterChildren",childcareExpenses.whoLooksAfterChildren)}
        {childcareExpenses.howMuchCostChildcare.isEmpty match {
          case false =>
            <Expense>
              {questionCurrency(<Payment/>, "howMuchCostChildcare",Some(childcareExpenses.howMuchCostChildcare), questionLabelEmployment(claim, "howMuchCostChildcare", job.jobID))}
              {questionOther(<Frequency/>, "employment_howOftenPayChildCare",childcareExpenses.howOftenPayChildCare.frequency,childcareExpenses.howOftenPayChildCare.other, "", questionLabelEmployment(claim, "employment_howOftenPayChildCare", job.jobID))}
            </Expense>

          case _ => NodeSeq.Empty
        }}
        {question(<RelationshipCarerToClaimant/>, "relationToYou",childcareExpenses.relationToYou)}
        {question(<RelationshipCarerToPartner/>, "relationToPartner",childcareExpenses.relationToPartner)}
        {question(<RelationshipCarerToPersonYouCare/>, "relationToPersonYouCare",childcareExpenses.relationToPersonYouCare)}
      </ChildCareExpenses>
    } else {
      {question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren",aboutExpenses.payAnyoneToLookAfterChildren,questionLabelEmployment(claim, "payAnyoneToLookAfterChildren", job.jobID))}
    }
  }

  private def careExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val personYouCareExpenses: PersonYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses].getOrElse(PersonYouCareForExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterPerson.toUpperCase == yes.toUpperCase

    if (showXml) {
      question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,questionLabelEmployment(claim, "payAnyoneToLookAfterPerson", job.jobID)) ++
      <CareExpenses>
        {question(<CarerName/>,"whoLooksAfterChildren",personYouCareExpenses.whoDoYouPay)}
        {personYouCareExpenses.howMuchCostCare.isEmpty match {
          case false =>
            <Expense>
              {questionCurrency(<Payment/>,"howMuchCostChildcare",Some(personYouCareExpenses.howMuchCostCare),questionLabelEmployment(claim, "howMuchCostChildcare", job.jobID))}
              {questionOther(<Frequency/>,"employment_howOftenPayChildCare",personYouCareExpenses.howOftenPayCare.frequency,personYouCareExpenses.howOftenPayCare.other,"", questionLabelEmployment(claim, "employment_howOftenPayChildCare", job.jobID))}
            </Expense>

          case _ => NodeSeq.Empty
        }}
        {question(<RelationshipCarerToClaimant/>,"relationToYou",personYouCareExpenses.relationToYou)}
        {question(<RelationshipCarerToCaree/>,"relationToPersonYouCare",personYouCareExpenses.relationToPersonYouCare)}
      </CareExpenses>
    } else {
      question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,questionLabelEmployment(claim, "payAnyoneToLookAfterPerson", job.jobID))
    }
  }
}
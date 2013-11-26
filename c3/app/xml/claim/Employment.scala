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
        {question(<CurrentlyEmployed/>,"finishedThisJob",currentlyEmployed)}

        {
          for (job <- jobsQG) yield {
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

  def employerXml(job: Job): Elem = {
    val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
    val employerContactDetails = job.questionGroup[EmployerContactDetails].getOrElse(EmployerContactDetails())

    <Employer>
      {question(<DateJobStarted/>, "jobStartDate", Some(jobDetails.jobStartDate.`dd-MM-yyyy`))}
      {question(<DateJobEnded/>, "lastWorkDate",jobDetails.lastWorkDate)}
      {question(<JobType/>,s"jobTitle.${if (jobDetails.finishedThisJob == "yes") "was" else "is"}", job.title)}

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
      {question(<P45LeavingDate/>, "p45LeavingDate", jobDetails.p45LeavingDate)}
    </Employer>
  }

  def payXml(jobDetails: JobDetails, lastWage: LastWage, additionalWageDetails: AdditionalWageDetails, claim: Claim): Elem = {
    <Pay>
      {question(<WeeklyHoursWorked/>, "hoursPerWeek", jobDetails.hoursPerWeek, pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase, jobDetails.jobID))}
      {question(<DateLastPaid/>, "lastPaidDate", lastWage.lastPaidDate)}
      {questionCurrency(<GrossPayment/>, "grossPay",Some(lastWage.grossPay))}
      {question(<IncludedInWage/>, "payInclusions", lastWage.payInclusions)}
      {additionalWageDetails.oftenGetPaid match {
        case Some(n) => questionOther(<PayFrequency/>,"paymentFrequency",n.frequency,n.other)
        case _ => NodeSeq.Empty
        }
      }
      {question(<UsualPayDay/>, "whenGetPaid", additionalWageDetails.whenGetPaid, pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase, jobDetails.jobID))}
      {question(<ConstantEarnings/>,"sameAmountEachTime",lastWage.sameAmountEachTime,pastPresentLabelForEmployment(claim, didYou, doYou, jobDetails.jobID))}
    </Pay>
  }

  def pensionSchemeXml(job: Job, claim:Claim) = {
    val pensionScheme:PensionSchemes = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

    NodeSeq.Empty ++ {occupationalPensionSchemeXml(pensionScheme, claim, job)} ++ {personalPensionSchemeXml(pensionScheme, claim, job)}
  }

  def occupationalPensionSchemeXml(pensionScheme: PensionSchemes, claim:Claim, job:Job) = {
    val showXml = pensionScheme.payOccupationalPensionScheme == yes

    {question(<PaidForOccupationalPension/>,"payOccupationalPensionScheme",pensionScheme.payOccupationalPensionScheme,pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID))} ++
    (if (showXml) {
        <OccupationalPension>
          {questionCurrency(<Payment/>,"howMuchPension",pensionScheme.howMuchPension,pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
          {questionOther(<Frequency/>,"howOftenPension",pensionScheme.howOftenPension.get.frequency,pensionScheme.howOftenPension.get.other)}
        </OccupationalPension>
    } else {
      NodeSeq.Empty
    })
  }

  def personalPensionSchemeXml(pensionScheme:PensionSchemes, claim:Claim, job:Job): NodeSeq = {
    val showXml = pensionScheme.payPersonalPensionScheme == yes

    {question(<PaidForPersonalPension/>,"payPersonalPensionScheme",pensionScheme.payPersonalPensionScheme,pastPresentLabelForEmployment(claim, didYou, doYou,job.jobID))} ++
    (if (showXml) {
        <PersonalPension>
          {questionCurrency(<Payment/>,"howMuchPersonal",pensionScheme.howMuchPersonal,pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
          {questionOther(<Frequency/>,"howOftenPersonal",pensionScheme.howOftenPersonal.get.frequency,pensionScheme.howOftenPersonal.get.other)}
        </PersonalPension>
    } else {
      NodeSeq.Empty
    })
  }

  def jobExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val necessaryExpenses: NecessaryExpenses = job.questionGroup[NecessaryExpenses].getOrElse(NecessaryExpenses())
    val showXml = aboutExpenses.payForAnythingNecessary == "yes"

    if (showXml) {
        question(<PaidForJobExpenses/>,"payForAnythingNecessary",aboutExpenses.payForAnythingNecessary,pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID)) ++
        <JobExpenses>
          {question(<Expense/>,"whatAreThose",necessaryExpenses.whatAreThose,pastPresentLabelForEmployment(claim, wereYou.toLowerCase.take(4), areYou.toLowerCase.take(3) , job.jobID))}
        </JobExpenses>
    } else {
      {question(<PaidForJobExpenses/>,"payForAnythingNecessary",aboutExpenses.payForAnythingNecessary,pastPresentLabelForEmployment(claim, didYou, doYou , job.jobID))}
    }
  }

  def childcareExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val childcareExpenses: ChildcareExpenses = job.questionGroup[ChildcareExpenses].getOrElse(ChildcareExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren == yes

    if (showXml) {

      question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren", aboutExpenses.payAnyoneToLookAfterChildren, pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID)) ++
      <ChildCareExpenses>
        {question(<CarerName/>, "whoLooksAfterChildren",childcareExpenses.whoLooksAfterChildren)}
        {childcareExpenses.howMuchCostChildcare.isEmpty match {
          case false => {
            <Expense>
              {questionCurrency(<Payment/>, "howMuchCostChildcare",Some(childcareExpenses.howMuchCostChildcare), pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
              {questionOther(<Frequency/>, "employment_howOftenPayChildCare",childcareExpenses.howOftenPayChildCare.frequency,childcareExpenses.howOftenPayChildCare.other, pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
            </Expense>
          }
          case _ => NodeSeq.Empty
        }}
        {questionOther(<RelationshipCarerToClaimant/>, "relationToYou",childcareExpenses.relationToYou,None)}
      </ChildCareExpenses>
    } else {
      {question(<CareExpensesChildren/>, "payAnyoneToLookAfterChildren",aboutExpenses.payAnyoneToLookAfterChildren,pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
    }
  }

  def careExpensesXml(job: Job, claim:Claim):NodeSeq = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val personYouCareExpenses: PersonYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses].getOrElse(PersonYouCareForExpenses())

    val showXml = aboutExpenses.payAnyoneToLookAfterPerson.toUpperCase == yes.toUpperCase

    if (showXml) {

      question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,pastPresentLabelForEmployment(claim,didYou.toLowerCase,doYou.toLowerCase,job.jobID)) ++
      <CareExpenses>
        {question(<CarerName/>,"whoLooksAfterChildren",personYouCareExpenses.whoDoYouPay)}
        {personYouCareExpenses.howMuchCostCare.isEmpty match {
          case false => {
            <Expense>
              {questionCurrency(<Payment/>,"howMuchCostChildcare",Some(personYouCareExpenses.howMuchCostCare),pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase,job.jobID))}
              {questionOther(<Frequency/>,"employment_howOftenPayChildCare",personYouCareExpenses.howOftenPayCare.frequency,personYouCareExpenses.howOftenPayCare.other,pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , job.jobID))}
            </Expense>
          }
          case _ => NodeSeq.Empty
        }}
        {questionOther(<RelationshipCarerToClaimant/>,"relationToYou",personYouCareExpenses.relationToYou,None)}
        {questionOther(<RelationshipCarerToCaree/>,"relationToPersonYouCare",personYouCareExpenses.relationToPersonYouCare,None)}
      </CareExpenses>
    } else {
      {question(<CareExpensesCaree/>,"payAnyoneToLookAfterPerson",aboutExpenses.payAnyoneToLookAfterPerson,pastPresentLabelForEmployment(claim,didYou.toLowerCase,doYou.toLowerCase,job.jobID))}
    }
  }
}
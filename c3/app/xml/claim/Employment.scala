package xml.claim

import models.domain._
import xml.XMLComponent
import xml.XMLHelper._
import models.{DayMonthYear, PaymentFrequency}
import scala.language.postfixOps
import play.api.i18n.Lang
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat

import scala.xml.{Elem, NodeSeq}


object Employment extends XMLComponent{

  def xml(claim: Claim) = {
    val jobsQG = claim.questionGroup[Jobs].getOrElse(Jobs())
    val employment = claim.questionGroup[models.domain.Employment]

    if (jobsQG.jobs.length > 0 && employment.fold(false)(_.beenEmployedSince6MonthsBeforeClaim == "yes")) {

      <Employment>

        {for ((job, index) <- jobsQG.zipWithIndex) yield {
            val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
            val lastWage = job.questionGroup[LastWage].getOrElse(LastWage("", PaymentFrequency(),"",DayMonthYear(),"", None, "", None))

            <JobDetails>
              {employerXml(job, claim)}
              {payXml(jobDetails, lastWage, claim)}
              {question(<OweMoney/>, "employerOwesYouMoney",lastWage.employerOwesYouMoney)}
              {pensionExpensesXml(job,claim)}
              {payForThingsXml(job, claim)}
              {jobExpensesXml(job, claim)}
              {anyMoreJobs(index == jobsQG.jobs.length -1, claim)}
            </JobDetails>
        }}
      </Employment>
    } else {
      NodeSeq.Empty
    }
  }

  private def employerXml(job: Iteration, claim: Claim): Elem = {
    val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())

    <Employer>
      {question(<CurrentlyEmployed/>,"finishedThisJob",jobDetails.finishedThisJob)}
      {question(<DidJobStartBeforeClaimDate/>, "startJobBeforeClaimDate", jobDetails.startJobBeforeClaimDate, claim.dateOfClaim.fold("")(dmy => displayPlaybackDatesFormat(Lang("en"),dmy - 1 months)))}
      {question(<DateJobStarted/>, "jobStartDate", jobDetails.jobStartDate)}
      {if(jobDetails.lastWorkDate.isDefined){
        {question(<DateJobEnded/>, "lastWorkDate",jobDetails.lastWorkDate)}
      }}
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
      {question(<WeeklyHoursWorked/>, "hoursPerWeek", jobDetails.hoursPerWeek, questionLabelEmployment(claim, "hoursPerWeek", jobDetails.iterationID))}
      {question(<DateLastPaid/>, "lastPaidDate", lastWage.lastPaidDate)}
      {questionCurrency(<GrossPayment/>, "grossPay",Some(lastWage.grossPay))}
      {question(<IncludedInWage/>, "payInclusions", lastWage.payInclusions)}
      {questionOther(<PayFrequency/>,"oftenGetPaidFrequency",oftenPaid.frequency,oftenPaid.other, questionLabelEmployment(claim, "oftenGetPaidFrequency", jobDetails.iterationID))}
      {question(<UsualPayDay/>, "whenGetPaid", lastWage.whenGetPaid, questionLabelEmployment(claim, "whenGetPaid", jobDetails.iterationID))}
      {question(<ConstantEarnings/>,"sameAmountEachTime",lastWage.sameAmountEachTime,questionLabelEmployment(claim, "sameAmountEachTime", jobDetails.iterationID))}
    </Pay>
  }

  private def pensionExpensesXml(job:Iteration, claim:Claim): NodeSeq = {
    val aboutExpenses: PensionAndExpenses = job.questionGroup[PensionAndExpenses].getOrElse(PensionAndExpenses())
    val showXml = aboutExpenses.payPensionScheme.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForPension/>,"payPensionScheme.answer",aboutExpenses.payPensionScheme.answer,questionLabelEmployment(claim, "payPensionScheme.answer", job.iterationID)) ++
      {<PensionExpenses>
        {question(<Expense/>,"payPensionScheme.text",aboutExpenses.payPensionScheme.text,questionLabelEmployment(claim, "payPensionScheme.text", job.iterationID))}
      </PensionExpenses>}
    } else {
      question(<PaidForPension/>,"payPensionScheme.answer",aboutExpenses.payPensionScheme.answer,questionLabelEmployment(claim, "payPensionScheme.answer", job.iterationID))
    }
  }

  private def jobExpensesXml(job: Iteration, claim:Claim):NodeSeq = {
    val aboutExpenses: PensionAndExpenses = job.questionGroup[PensionAndExpenses].getOrElse(PensionAndExpenses())
    val showXml = aboutExpenses.haveExpensesForJob.answer.toLowerCase == "yes"

    if (showXml) {
        question(<PaidForJobExpenses/>,"haveExpensesForJob.answer",aboutExpenses.haveExpensesForJob.answer,questionLabelEmployment(claim, "haveExpensesForJob.answer", job.iterationID)) ++
        <JobExpenses>
          {question(<Expense/>,"haveExpensesForJob.text",aboutExpenses.haveExpensesForJob.text,questionLabelEmployment(claim, "haveExpensesForJob.text", job.iterationID))}
        </JobExpenses>
    } else {
      question(<PaidForJobExpenses/>,"haveExpensesForJob.answer",aboutExpenses.haveExpensesForJob.answer,questionLabelEmployment(claim, "haveExpensesForJob.answer", job.iterationID))
    }
  }

  private def payForThingsXml(job: Iteration, claim:Claim):NodeSeq = {
    val aboutExpenses: PensionAndExpenses = job.questionGroup[PensionAndExpenses].getOrElse(PensionAndExpenses())
    val showXml = aboutExpenses.payForThings.answer.toLowerCase == "yes"

    if (showXml) {
      question(<PaidForThingsToDoJob/>,"payForThings.answer",aboutExpenses.payForThings.answer,questionLabelEmployment(claim, "payForThings.answer", job.iterationID)) ++
        <ExpensesToDoJob>
          {question(<Expense/>,"payForThings.text",aboutExpenses.payForThings.text,questionLabelEmployment(claim, "payForThings.text", job.iterationID))}
        </ExpensesToDoJob>
    } else {
      question(<PaidForThingsToDoJob/>,"payForThings.answer",aboutExpenses.payForThings.answer,questionLabelEmployment(claim, "payForThings.answer", job.iterationID))
    }
  }

  private def anyMoreJobs(isLast:Boolean, claim:Claim):NodeSeq = {
    //For all the added jobs, if there are more the user has to have answered Yes, however the last case
    //might the user has answered Yes or No but as there's a limit of 5 jobs they can't add anymore.
    val answer =
      if (!isLast) true
      else claim.questionGroup[BeenEmployed].exists(_.beenEmployed == "yes")

    question(<OtherEmployment/>, "beenEmployed", answer , claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => displayPlaybackDatesFormat(Lang("en"), dmy - 6 months)))
  }
}
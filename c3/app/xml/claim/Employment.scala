package xml.claim

import scala.language.reflectiveCalls
import scala.xml.{NodeSeq, Elem}
import models.domain._
import xml.XMLHelper._
import app.XMLValues._
import models.{DayMonthYear, PaymentFrequency}
import models.PaymentFrequency
import models.domain.Claim
import scala.Some

object Employment {

  def employerXml(job: Job): Elem = {
    val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())

    <Employer>
      <DateJobStarted>{jobDetails.jobStartDate.`yyyy-MM-dd`}</DateJobStarted>
      {<DateJobEnded/> +++ jobDetails.lastWorkDate}
      <JobType>{job.title}</JobType>
      {<ClockPayrollNumber/> +++ jobDetails.payrollEmployeeNumber}
      <Name>{jobDetails.employerName}</Name>
      <Address>{postalAddressStructure(jobDetails.address, jobDetails.postcode)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress> <!-- Always default to yes -->
      {<EmployersPhoneNumber/> +++ jobDetails.phoneNumber}
      <EmployersFaxNumber/>
      <WagesDepartment/>
      <DepartmentPhoneFaxNumber/>
    </Employer>
  }

  def payXml(jobDetails: JobDetails, lastWage: LastWage): Elem = {

    <Pay>
      {<WeeklyHoursWorked/> ?+ jobDetails.hoursPerWeek}
      <DateLastWorked/>

      <DateLastPaid>{lastWage.lastPaidDate.`yyyy-MM-dd`}</DateLastPaid>
      <GrossPayment>
        <Currency>{GBP}</Currency>
        <Amount>{currencyAmount(lastWage.grossPay)}</Amount>
      </GrossPayment>
      {<IncludedInWage/> +++ lastWage.payInclusions}
      <PayPeriod>
        <DateFrom></DateFrom>
        <DateTo></DateTo>
      </PayPeriod>
      {paymentFrequency(lastWage.oftenGetPaid)}
      {<UsualPayDay/> +- lastWage.whenGetPaid}
      <VaryingEarnings>{NotAsked}</VaryingEarnings>
    </Pay>
  }

  def howMuchOwedXml(s: Option[String]) = {
    val showXml = s.isDefined

    if (showXml) {
      <Payment>
        <Currency>{GBP}</Currency>
        <Amount>{s.get}</Amount>
      </Payment>
    } else {
      NodeSeq.Empty
    }
  }

  def pensionSchemeXml(job: Job) = {
    val pensionScheme:PensionSchemes = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

    NodeSeq.Empty ++ {occupationalPensionSchemeXml(pensionScheme)} ++ {personalPensionSchemeXml(pensionScheme)}
  }

  def occupationalPensionSchemeXml(pensionScheme: PensionSchemes) = {
    val showXml = pensionScheme.payOccupationalPensionScheme == yes

    if (showXml) {
      <PaidForOccupationalPension>{pensionScheme.payOccupationalPensionScheme}</PaidForOccupationalPension>
        <PensionScheme>
          <Type>occupational</Type>
          <Payment>
            <Currency>{GBP}</Currency>
            {<Amount/> +++ currencyAmount(pensionScheme.howMuchPension)}
          </Payment>
          <Frequency>{pensionScheme.howOftenPension.get.frequency}</Frequency>
        </PensionScheme>
    } else {
      <PaidForOccupationalPension>{pensionScheme.payOccupationalPensionScheme}</PaidForOccupationalPension>
    }
  }

  def personalPensionSchemeXml(pensionScheme:PensionSchemes): NodeSeq = {
    val showXml = pensionScheme.payPersonalPensionScheme == yes

    if (showXml) {
      <PaidForPersonalPension>{pensionScheme.payPersonalPensionScheme}</PaidForPersonalPension>
      <PensionScheme>
        <Type>personal_private</Type>
        <Payment>
          <Currency>{GBP}</Currency>
          {<Amount/> +++ currencyAmount(pensionScheme.howMuchPersonal)}
        </Payment>
        <Frequency>{pensionScheme.howOftenPersonal.get.frequency}</Frequency>
      </PensionScheme>
    } else {
      <PaidForPersonalPension>{pensionScheme.payPersonalPensionScheme}</PaidForPersonalPension>
    }
  }

  def jobExpensesXml(job: Job) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val showXml = aboutExpenses.haveExpensesForJob == "yes"

    if (showXml) {
      <PaidForJobExpenses>{aboutExpenses.haveExpensesForJob}</PaidForJobExpenses>
      <JobExpenses>
        {<Expense/> +++aboutExpenses.whatExpensesForJob}
        <Reason>{NotAsked}</Reason>
        <WeeklyPayment>
          <Currency></Currency>
          <Amount>{NotAsked}</Amount>
        </WeeklyPayment>
      </JobExpenses>
    } else {
      <PaidForJobExpenses>{aboutExpenses.haveExpensesForJob}</PaidForJobExpenses>
    }
  }

  def childcareExpensesXml(job: Job) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren == yes

    if (showXml) {
      <CareExpensesChildren>{aboutExpenses.payAnyoneToLookAfterChildren}</CareExpensesChildren>
      <ChildCareExpenses>
        {<CarerName/> +++ aboutExpenses.nameLookAfterChildren}
        <CarerAddress><gds:Line>{NotAsked}</gds:Line><gds:Line>{NotAsked}</gds:Line><gds:Line>{NotAsked}</gds:Line><gds:PostCode></gds:PostCode></CarerAddress>
        <ConfirmAddress>{yes}</ConfirmAddress>
        <WeeklyPayment>
          <Currency></Currency>
          <Amount>{NotAsked}</Amount>
        </WeeklyPayment>
        <RelationshipCarerToClaimant>{NotAsked}</RelationshipCarerToClaimant>
        <ChildDetails>
          <Name>{NotAsked}</Name>
          <RelationToChild>{NotAsked}</RelationToChild>
        </ChildDetails>
      </ChildCareExpenses>
    } else {
      <CareExpensesChildren>{aboutExpenses.payAnyoneToLookAfterChildren}</CareExpensesChildren>
    }
  }

  def careExpensesXml(job: Job) = {
    val aboutExpenses: AboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())

    val showXml = aboutExpenses.payAnyoneToLookAfterPerson == yes

    if (showXml) {
      <CareExpensesCaree>{aboutExpenses.payAnyoneToLookAfterPerson}</CareExpensesCaree>
      <CareExpenses>
        {<CarerName/> +++ aboutExpenses.nameLookAfterPerson}
        <CarerAddress><gds:Line>{NotAsked}</gds:Line><gds:Line>{NotAsked}</gds:Line><gds:Line>{NotAsked}</gds:Line><gds:PostCode></gds:PostCode></CarerAddress>
        <ConfirmAddress>{yes}</ConfirmAddress>
        <WeeklyPayment>
          <Currency></Currency>
          <Amount>{NotAsked}</Amount>
        </WeeklyPayment>
        <RelationshipCarerToClaimant>{NotAsked}</RelationshipCarerToClaimant>
        <RelationshipCarerToCaree>{NotAsked}</RelationshipCarerToCaree>
      </CareExpenses>
    } else {
      <CareExpensesCaree>{aboutExpenses.payAnyoneToLookAfterPerson}</CareExpensesCaree>
    }
  }

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
        <CurrentlyEmployed>{currentlyEmployed}</CurrentlyEmployed>
        <DateLastWorked>{NotAsked}</DateLastWorked>
        {for (job <- jobsQG) yield {
          val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
          val lastWage = job.questionGroup[LastWage].getOrElse(LastWage("", PaymentFrequency(),None,DayMonthYear(),"", None, None, ""))

          <JobDetails>
            {employerXml(job)}
            {payXml(jobDetails, lastWage)}
            <OtherThanMoney>{NotAsked}</OtherThanMoney>
            <OweMoney>{lastWage.employerOwesYouMoney}</OweMoney>
            {childcareExpensesXml(job)}
            {careExpensesXml(job)}
            {pensionSchemeXml(job)}
            {jobExpensesXml(job)}
          </JobDetails>
        }}
      </Employment>
    } else {
      NodeSeq.Empty
    }
  }
}
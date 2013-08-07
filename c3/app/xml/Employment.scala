package xml

import models.domain._
import xml.XMLHelper._
import models.{DayMonthYearComparator, DayMonthYear}
import scala.language.reflectiveCalls
import scala.xml.{NodeSeq, Elem}

object Employment {

  def employerXml(jobDetails: JobDetails, employerCD:EmployerContactDetails): Elem = {
    <Employer>
      {<DateJobStarted/> +++ jobDetails.jobStartDate}
      {<DateJobEnded/> +++ jobDetails.lastWorkDate}
      {<JobType/> +++ jobDetails.jobTitle}
      {<ClockPayrollNumber/> +++ jobDetails.payrollEmployeeNumber}
      <Name>{jobDetails.employerName}</Name>
      <Address>{postalAddressStructure(employerCD.address,employerCD.postcode)}</Address>
      <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
      {<EmployersPhoneNumber/> +++ employerCD.phoneNumber}
      <EmployersFaxNumber/>
      <WagesDepartment/>
      <DepartmentPhoneFaxNumber/>
    </Employer>
  }

  def payXml(jobDetails: JobDetails, lastWage: LastWage, additionalWageDetails: AdditionalWageDetails): Elem = {
    <Pay>
      {<WeeklyHoursWorked/> +++ jobDetails.hoursPerWeek}
      <DateLastWorked/>
      {<DateLastPaid/> +++ lastWage.lastPaidDate}
      <GrossPayment>
        <Currency>GBP</Currency>
        {<Amount/> +++ lastWage.grossPay}
      </GrossPayment>
      {<IncludedInWage/> +++ lastWage.payInclusions}
      <PayPeriod>
        {fromToStructure (lastWage.period)}
      </PayPeriod>
      {paymentFrequency(additionalWageDetails.oftenGetPaid)}
      {<UsualPayDay/> +++ additionalWageDetails.whenGetPaid}
      {<VaryingEarnings/> +!? lastWage.sameAmountEachTime}
      {<PaidForHolidays/> +++ additionalWageDetails.holidaySickPay}
    </Pay>
  }

  def howMuchOwed(s:Option[String]) = {
    val showXml = s.isDefined

    if (showXml){
      <Payment>
        <Amount>{s.get}</Amount>
      </Payment>
    }else{
      NodeSeq.Empty
    }
  }

  def moneyOwedXml(moneyOwed: MoneyOwedbyEmployer) = {
    val showXml = moneyOwed.jobID.nonEmpty
    if (showXml){
        <MoneyOwed>
          {howMuchOwed(moneyOwed.howMuchOwed)}
          {<Period/> ?+ moneyOwed.owedFor}
          {<PaymentDueDate/> ?+ moneyOwed.shouldBeenPaidBy}
          {<PaymentExpected/> +++ moneyOwed.whenWillGetIt}
        </MoneyOwed>
    }else{
      NodeSeq.Empty
    }
  }

  def occupationalPensionSchemeXml(pensionScheme: PensionSchemes) = {
    val showXml = pensionScheme.payOccupationalPensionScheme == "yes"

    if (showXml){
        <PensionScheme>
          <Type></Type>
          <Payment>
            <Currency>GBP</Currency>
            {<Amount/> +++ pensionScheme.howMuchPension}
          </Payment>
          {<Frequency/> +++ pensionScheme.howOftenPension}
        </PensionScheme>
    }else{
      NodeSeq.Empty
    }
  }

  def personalPensionSchemeXml(pensionScheme:PensionSchemes) = {
    val showXml = pensionScheme.payPersonalPensionScheme == "yes"

    if (showXml){
        <PensionScheme>
          <Type></Type>
          <Payment>
            <Currency>GBP</Currency>
            {<Amount/> +++ pensionScheme.howMuchPersonal}
          </Payment>
          {<Frequency/> +++ pensionScheme.howOftenPersonal}
        </PensionScheme>
    }else{
      NodeSeq.Empty
    }
  }

  def jobExpensesXml(aboutExpenses: AboutExpenses, necessaryExpenses: NecessaryExpenses) = {
    val showXml = aboutExpenses.payForAnythingNecessary == "yes"

    if (showXml){
        <JobExpenses>
          <Expense>{necessaryExpenses.whatAreThose}</Expense>
          <Reason>{necessaryExpenses.whyDoYouNeedThose}</Reason>
          <WeeklyPayment>
            <Currency>GBP</Currency>
            <Amount>{necessaryExpenses.howMuchCostEachWeek}</Amount>
          </WeeklyPayment>
        </JobExpenses>
    }else{
      NodeSeq.Empty
    }
  }

  def childcareExpensesXml(aboutExpenses: AboutExpenses, childcareExpenses: ChildcareExpenses, childcareProvider: ChildcareProvider) = {
    val showXml = aboutExpenses.payAnyoneToLookAfterChildren == "yes"

    if (showXml){
      <ChildCareExpenses>
        <CarerName>{childcareExpenses.whoLooksAfterChildren}</CarerName>
        <CarerAddress>{postalAddressStructure(childcareProvider.address,childcareProvider.postcode)}</CarerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <WeeklyPayment>
          <Currency>GBP</Currency>
          {<Amount/> +++ childcareExpenses.howMuchCostChildcare}
        </WeeklyPayment>
        <RelationshipCarerToClaimant>{childcareExpenses.relationToYou.orNull}</RelationshipCarerToClaimant>
        <ChildDetails><Name/>{<RelationToChild/> ?+ childcareExpenses.relationToPersonYouCare}</ChildDetails>
      </ChildCareExpenses>
    }else{
      NodeSeq.Empty
    }

  }

  def careExpensesXml(aboutExpenses: AboutExpenses, personYouCareExpenses: PersonYouCareForExpenses, careProvider: CareProvider) = {
    val showXml = aboutExpenses.payAnyoneToLookAfterPerson == "yes"

    if (showXml){
        <CareExpenses>
          <CarerName>{personYouCareExpenses.whoDoYouPay}</CarerName>
          <CarerAddress>{postalAddressStructure(careProvider.address,careProvider.postcode)}</CarerAddress>
          <ConfirmAddress>yes</ConfirmAddress>
          <WeeklyPayment>
            <Currency>GBP</Currency>
            {<Amount/> +++ personYouCareExpenses.howMuchCostCare}
          </WeeklyPayment>
          {<RelationshipCarerToClaimant/> +++ personYouCareExpenses.relationToYou}
          <RelationshipCarerToCaree>other</RelationshipCarerToCaree>
        </CareExpenses>
    }else{
      NodeSeq.Empty
    }
  }

  def xml(claim: Claim) = {
    val jobsQG = claim.questionGroup[Jobs].getOrElse(Jobs())

    if (jobsQG.jobs.length > 0) {
      // We will search in all jobs for at least one case finishedThisJob = "no" because that means he is currently employed
      val currentlyEmployed = jobsQG.jobs.count(_.apply(JobDetails) match {
        case Some(j: JobDetails) => j.finishedThisJob == "no"
        case _ => false
      }) match {
        case i if i > 0 => "yes"
        case _ => "no"
      }

      // The date when he last worked will be the greater date of all the "last work date" dates of all the jobs.
      val dateLastWorked = jobsQG.jobs.map(_.apply(JobDetails) match {
        case Some(j:JobDetails) => j.lastWorkDate
        case _ => None
      }).max(DayMonthYearComparator) match {
        case Some(d) => d
        case _ => DayMonthYear()
      }

      <Employment>
        <CurrentlyEmployed>{currentlyEmployed}</CurrentlyEmployed>
        <DateLastWorked>{dateLastWorked.toXmlString}</DateLastWorked>
        {for(job <- jobsQG)yield{

          val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
          val employerContactDetails = job.questionGroup[EmployerContactDetails].getOrElse(EmployerContactDetails())
          val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
          val additionalWageDetails = job.questionGroup[AdditionalWageDetails].getOrElse(AdditionalWageDetails())
          val moneyOwedbyEmployer = job.questionGroup[MoneyOwedbyEmployer].getOrElse(MoneyOwedbyEmployer())
          val pensionSchemes = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())
          val aboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
          val necessaryExpenses = job.questionGroup[NecessaryExpenses].getOrElse(NecessaryExpenses())
          val childcareExpenses = job.questionGroup[ChildcareExpenses].getOrElse(ChildcareExpenses())
          val childcareProvider = job.questionGroup[ChildcareProvider].getOrElse(ChildcareProvider())
          val personYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses].getOrElse(PersonYouCareForExpenses())
          val careProvider = job.questionGroup[CareProvider].getOrElse(CareProvider())

          <JobDetails>
            {employerXml(jobDetails,employerContactDetails)}
            {payXml(jobDetails,lastWage,additionalWageDetails)}
            <OtherThanMoney>no</OtherThanMoney>
            <OweMoney>{additionalWageDetails.employerOwesYouMoney}</OweMoney>
            {moneyOwedXml(moneyOwedbyEmployer)}
            <CareExpensesChildren>{aboutExpenses.payAnyoneToLookAfterChildren}</CareExpensesChildren>
            {childcareExpensesXml(aboutExpenses,childcareExpenses,childcareProvider)}
            <CareExpensesCaree>{aboutExpenses.payAnyoneToLookAfterPerson}</CareExpensesCaree>
            {careExpensesXml(aboutExpenses,personYouCareForExpenses,careProvider)}
            <PaidForOccupationalPension>{pensionSchemes.payOccupationalPensionScheme}</PaidForOccupationalPension>
            {occupationalPensionSchemeXml(pensionSchemes)}
            <PaidForPersonalPension>{pensionSchemes.payPersonalPensionScheme}</PaidForPersonalPension>
            {personalPensionSchemeXml(pensionSchemes)}
            <PaidForJobExpenses>{aboutExpenses.payForAnythingNecessary}</PaidForJobExpenses>
            {jobExpensesXml(aboutExpenses,necessaryExpenses)}
          </JobDetails>
        }}
      </Employment>
    }else{
      NodeSeq.Empty
    }
  }
}
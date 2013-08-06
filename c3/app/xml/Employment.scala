package xml

import models.domain._
import xml.XMLHelper._
import models.{MultiLineAddress, DayMonthYearComparator, DayMonthYear}
import scala.language.reflectiveCalls
import scala.xml.{Node, NodeSeq, Elem, NodeBuffer}
import scala.Some
import scala.Some

object EmploymentXml {


  def employerXml(jobDetails: JobDetails,employerCD:EmployerContactDetails):Elem = {
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

  def payXml(jobDetails: JobDetails,lastWage:LastWage,additionalWageDetails:AdditionalWageDetails):Elem = {
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
    s match {
      case Some(s) =>
        <Payment>
          <Amount>{s}</Amount>
        </Payment>
      case _ => NodeSeq.Empty
    }
  }


  def moneyOwedXml(moneyOwed:Option[MoneyOwedbyEmployer]) = {

    moneyOwed match {
      case Some(m) =>
        <MoneyOwed>
          {howMuchOwed(m.howMuchOwed)}
          {<Period/> ?+ m.owedFor}
          {<PaymentDueDate/> ?+ m.shouldBeenPaidBy}
          {<PaymentExpected/> +++ m.whenWillGetIt}
        </MoneyOwed>
      case _ => NodeSeq.Empty
    }
  }

  def occupationalPensionSchemeXml(pensionScheme:PensionSchemes) = {
    pensionScheme.payOccupationalPensionScheme match{
      case "yes" =>
        <PensionScheme>
          <Type></Type>
          <Payment>
            {<Amount/> +++ pensionScheme.howMuchPension}
          </Payment>
          {<Frequency/> +++ pensionScheme.howOftenPension}
        </PensionScheme>
      case _ => NodeSeq.Empty
    }

  }

  def personalPensionSchemeXml(pensionScheme:PensionSchemes) = {
    pensionScheme.payPersonalPensionScheme match{
      case "yes" =>
        <PensionScheme>
          <Type></Type>
          <Payment>
            {<Amount/> +++ pensionScheme.howMuchPersonal}
          </Payment>
          {<Frequency/> +++ pensionScheme.howOftenPersonal}
        </PensionScheme>
      case _ => NodeSeq.Empty
    }
  }

  def jobExpensesXml(aboutExpenses:AboutExpenses,necessaryExpenses:Option[NecessaryExpenses]) = necessaryExpenses match {
    case Some(n:NecessaryExpenses) if aboutExpenses.payForAnythingNecessary == "yes" =>
      <JobExpenses>
        <Expense>{n.whatAreThose}</Expense>
        <Reason>{n.whyDoYouNeedThose}</Reason>
        <WeeklyPayment>
          <Amount>{n.howMuchCostEachWeek}</Amount>
        </WeeklyPayment>
      </JobExpenses>
    case None => NodeSeq.Empty
  }


  def childcareExpensesXml(aboutExpenses:AboutExpenses, childcareExpenses:Option[ChildcareExpenses], childcareProvider:Option[ChildcareProvider]) = aboutExpenses.payAnyoneToLookAfterChildren match {
    case "yes" =>
      <ChildCareExpenses>
        <CarerName>{childcareExpenses.fold("")(_.whoLooksAfterChildren)}</CarerName>
        <CarerAddress>
          {postalAddressStructure(childcareProvider.collect{case c: ChildcareProvider if c.address.isDefined => c.address.get},
                                  childcareProvider.collect{case c: ChildcareProvider if c.postcode.isDefined => c.postcode.get})}
        </CarerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <WeeklyPayment>
          {<Amount/> +++ childcareExpenses.collect{case c: ChildcareExpenses if c.howMuchCostChildcare.isDefined => c.howMuchCostChildcare}}
        </WeeklyPayment>
        <RelationshipCarerToClaimant>other</RelationshipCarerToClaimant>
        <ChildDetails>
          <Name/>
        </ChildDetails>

      </ChildCareExpenses>
    case _ => NodeSeq.Empty

  }

  def careExpensesXml(aboutExpenses:AboutExpenses,personYouCareExpenses:Option[PersonYouCareForExpenses],careProvider:Option[CareProvider]) = aboutExpenses.payAnyoneToLookAfterPerson match{
    case "yes" =>
      <CareExpenses>
        <CarerName>{personYouCareExpenses.fold("")(_.whoDoYouPay)}</CarerName>
        <CarerAddress>
          {postalAddressStructure(careProvider.collect{case c: CareProvider if c.address.isDefined => c.address.get},
                                  careProvider.collect{case c: CareProvider if c.postcode.isDefined => c.postcode.get})}
        </CarerAddress>
        <ConfirmAddress>yes</ConfirmAddress>
        <WeeklyPayment>
          {<Amount/> +++ personYouCareExpenses.collect{case c:PersonYouCareForExpenses if c.howMuchCostCare.isDefined => c.howMuchCostCare.get}}
        </WeeklyPayment>
        {<RelationshipCarerToClaimant/> +++ personYouCareExpenses.collect{case c:PersonYouCareForExpenses if c.relationToYou.isDefined => c.relationToYou.get}}
        {//<RelationshipCarerToCaree/> +++ personYouCareExpenses.collect{case c:PersonYouCareForExpenses if c..isDefined => c.relationToYou.get}}
        }
        <RelationshipCarerToCaree/>
        </CareExpenses>
    case _ => NodeSeq.Empty
  }

  def xml(claim: Claim) = {

    val jobsQG = claim.questionGroup(Jobs) match { case Some(j:Jobs) => j case _ => Jobs()}

    if (jobsQG.jobs.length > 0){

      // We will search in all jobs for at least one case finishedThisJob = "no" because that means he is currently employed
      val currentlyEmployed = jobsQG.jobs.count(_.apply(JobDetails) match {case Some(j:JobDetails) => j.finishedThisJob == "no" case _ => false})
                              match {
                                case i if i > 0 => "yes"
                                case _ => "no"
                              }
      // The date when he last worked will be the greater date of all the "last work date" dates of all the jobs.
      val dateLastWorked = jobsQG.jobs.map(_.apply(JobDetails) match {case Some(j:JobDetails) => j.lastWorkDate case _ => None})
                           .max(DayMonthYearComparator) match{ case Some(d) => d case _ => DayMonthYear() }


      <Employment>
        <CurrentlyEmployed>{currentlyEmployed}</CurrentlyEmployed>
        <DateLastWorked>{dateLastWorked.toXmlString}</DateLastWorked>
        {for(job <- jobsQG)yield{

          val jobDetails = job.questionGroup[JobDetails].get
          val employerContactDetails = job.questionGroup[EmployerContactDetails].get
          val lastWage = job.questionGroup[LastWage].get
          val additionalWageDetails = job.questionGroup[AdditionalWageDetails].get
          val moneyOwedbyEmployer = job.questionGroup[MoneyOwedbyEmployer]
          val pensionSchemes = job.questionGroup[PensionSchemes].get
          val aboutExpenses = job.questionGroup[AboutExpenses].get
          val necessaryExpenses = job.questionGroup[NecessaryExpenses]
          val childcareExpenses = job.questionGroup[ChildcareExpenses]
          val childcareProvider = job.questionGroup[ChildcareProvider]
          val personYouCareForExpenses = job.questionGroup[PersonYouCareForExpenses]
          val careProvider = job.questionGroup[CareProvider]

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

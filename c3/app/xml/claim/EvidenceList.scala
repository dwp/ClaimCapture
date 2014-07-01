package xml.claim

import models.domain._
import xml.XMLHelper._
import scala.xml.NodeSeq
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}
import app.XMLValues._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.i18n.{MMessages => Messages}
import controllers.Mappings

object EvidenceList {

  def xml(claim: Claim) = {
    <EvidenceList>
      {xmlGenerated()}{evidence(claim)}{aboutYou(claim)}{yourPartner(claim)}{careYouProvide(claim)}{breaks(claim)}{employment(claim)}{selfEmployment(claim)}{otherMoney(claim)}{AssistedDecision.xml(claim)}
    </EvidenceList>
  }

  def xmlGenerated() = {
    textLine("XML Generated at: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now()))
  }


  def evidence(claim: Claim): NodeSeq = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    var buffer = NodeSeq.Empty

    if (employed || selfEmployed) {
      buffer ++= textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed) {
        buffer ++= textLine()
        buffer ++= textLine("Your Employment documents.")
        buffer ++= textLine("Last payslip you got before your claim date: ", claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer ++= textLine("Any payslips you have had since then.")
        buffer ++= textLine("Any pension statements you may have.")

      }

      if (selfEmployed) {
        buffer ++= textLine()
        buffer ++= textLine("Your Self-employed documents.")
        buffer ++= textLine("Most recent finalised accounts you have for your business.")
        buffer ++= textLine("Any pension statements you may have.")
      }

      buffer ++= textLine()
      buffer ++= textLine("Send the above documents to:")
      buffer ++= textLine("CA Freepost")
      buffer ++= textLine("Palatine House")
      buffer ++= textLine("Preston")
      buffer ++= textLine("PR1 1HN")
      buffer ++= textLine("The Carer's Allowance unit will contact you if they need any further information.")
      buffer ++= textLine()
    }
    buffer
  }

  def aboutYou(claim: Claim) = {
    val nationalityAndResidency = claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())

    var textLines = NodeSeq.Empty ++ textSeparatorLine("About You")
    textLines ++= textLine(Messages("nationality.pdf") + " = ", nationalityAndResidency.nationality)
    textLines ++= textLine(Messages("resideInUK.label") + " = ", nationalityAndResidency.resideInUK.answer)
    if (nationalityAndResidency.resideInUK.answer == Mappings.no){textLines ++= textLine(Messages("resideInUK") + " = ", nationalityAndResidency.resideInUK.text.get)}
    textLines ++= fiftyTwoWeeksTrips(claim)
    textLines ++= textLine("Do you, or any member of your family, receive any benefits or pensions from any other European Economic Area (EEA) state or Switzerland? = ", otherEEAState.benefitsFromEEA)
    textLines ++= textLine("Have you, or a member of your family, made a claim for any benefits or pensions from any other European Economic Area (EEA) state or Switzerland? = ", otherEEAState.claimedForBenefitsFromEEA)
    textLines ++= textLine("Are you, or a member of your family, working in or paying insurance to, another European Economic Area (EEA) state or Switzerland? = ", otherEEAState.workingForEEA)
    textLines ++= textLine("Do you get state Pension? = ", moreAboutYou.receiveStatePension) ++
      textLine("If you have speech or hearing difficulties, would you like us to contact you by textphone? = ", yourContactDetails.contactYouByTextphone)

    textLines
  }

  def yourPartner(claim: Claim) = {
    val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())

    if (personYouCareFor.isPartnerPersonYouCareFor.nonEmpty) {
      textSeparatorLine("About Your Partner") ++
        textLine("Is your partner/spouse the person you are claiming Carer's Allowance for? = ", personYouCareFor.isPartnerPersonYouCareFor)
    }
  }

  def careYouProvide(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutTheCare = claim.questionGroup[MoreAboutTheCare].getOrElse(MoreAboutTheCare())

    textSeparatorLine("About Care You Provide") ++
      textLine("Do they live at the same address as you? = ", theirPersonalDetails.liveAtSameAddressCareYouProvide) ++
      textLine("Does this person get Armed Forces Independence Payment? = ", theirPersonalDetails.armedForcesPayment) ++
      textLine("Do you spend 35 hours or more each week caring for this person? = ", moreAboutTheCare.spent35HoursCaring) ++
      textLine("Did you care for this person for 35 hours or more each week before your claim date ? = ", moreAboutTheCare.spent35HoursCaringBeforeClaim.answer)
  }

  def breaks(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for {break <- breaksInCare.breaks} yield {
      textLine("Where were you during the break? Other detail = ", break.whereYou.other) ++
        textLine("Where was the person you care for during the break? = ", break.wherePerson.location) ++
        textLine("Where was the person you care for during the break? Other detail = ", break.wherePerson.other)
    }
  }

  def fiftyTwoWeeksTrips(claim: Claim):NodeSeq = {
    import scala.language.postfixOps

    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    var textLines = NodeSeq.Empty

    textLines ++= textLine("Have you been out of England, Scotland or Wales for more than 52 weeks in the last 3 years before your claim date? = ", if (trips.fiftyTwoWeeksTrips.size > 0) Yes else No)

    for ((fiftyTwoWeekTrip,index) <- trips.fiftyTwoWeeksTrips.zipWithIndex) {
      if (index > 0){
        textLines ++= textLine("Have you been out of England, Scotland or Wales at any other time in the last 3 years before your claim date? = ", if (trips.fiftyTwoWeeksTrips.size > 0) Yes else No)
      }
      textLines ++= textLine("Which country did you go to? = ", fiftyTwoWeekTrip.where)

      fiftyTwoWeekTrip.start match {
        case Some(dayMonthYear) => textLines ++= textLine("Date you left = ", dayMonthYear.`dd/MM/yyyy`)
        case _ => NodeSeq.Empty
      }

      fiftyTwoWeekTrip.end match {
        case Some(dayMonthYear) => textLines ++= textLine("Date you returned = ", dayMonthYear.`dd/MM/yyyy`)
        case _ => NodeSeq.Empty
      }

      fiftyTwoWeekTrip.why match {
        case Some(reasonForBeingThere) => reasonForBeingThere.reason match {
          case Some (reason) =>
            textLines ++= textLine("Reason for being there? = ", reason)
            if (reasonForBeingThere.other.isDefined){
              textLines ++= textLine("Reason for being there? Other = ", reasonForBeingThere.other.get)
            }
          case _ => NodeSeq.Empty
        }
        case _ => NodeSeq.Empty
      }

      textLines ++= textLine("Was the person you care for with you? = ", fiftyTwoWeekTrip.personWithYou)
    }
    textLines
  }

  def selfEmployment(claim: Claim) = {
    val yourAccounts = claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())
    val childCare = claim.questionGroup[ChildcareExpensesWhileAtWork].getOrElse(ChildcareExpensesWhileAtWork())
    val expensesWhileAtWork = claim.questionGroup[ExpensesWhileAtWork].getOrElse(ExpensesWhileAtWork())
    val pensionScheme = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())

    var textLines = textLine("Are the income, outgoings and profit in these accounts similar to your current level of trading? = ", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading) ++
      textLine("Please tell us why and when the change happened = ", yourAccounts.tellUsWhyAndWhenTheChangeHappened) ++
      textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to childcare? = ", currencyAmount(childCare.howMuchYouPay)) ++
      textLine("How often [[past=did you]] [[present=do you]] - expenses related to childcare expenses? = ", PensionPaymentFrequency.mapToHumanReadableString(childCare.howOftenPayChildCare))
    if (childCare.howOftenPayChildCare.other.isDefined)
      textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to childcare expenses? = ", childCare.howOftenPayChildCare.other.get)
    if (childCare.relationToPartner.nonEmpty)
      textLines ++= textLine(Messages("relationToPartner") + " = ", childCare.relationToPartner.get)

    textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", currencyAmount(expensesWhileAtWork.howMuchYouPay)) ++
      textLine("How often [[past=did you]] [[present=do you]] - expenses related to person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(expensesWhileAtWork.howOftenPayExpenses))

    if (expensesWhileAtWork.howOftenPayExpenses.other.isDefined)
      textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to person you care for? = ", expensesWhileAtWork.howOftenPayExpenses.other.get)

    if (pensionScheme.howOften.isDefined && pensionScheme.howOften.get.other.isDefined)
      textLines ++= textLine("How often do you pay into a Pension? Other = ", pensionScheme.howOften.get.other.get)

    if (sectionEmpty(textLines)) NodeSeq.Empty else textSeparatorLine("Self Employment") ++ textLines
  }

  def employment(claim: Claim) = {
    claim.questionGroup[Jobs] match {
      case Some(jobs) =>
        var textLines = NodeSeq.Empty
        textLines ++= textSeparatorLine("Employment")

        for (job <- jobs) {

          val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
          val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
          val aboutExpenses = job.questionGroup[AboutExpenses].getOrElse(AboutExpenses())
          val pensionScheme = job.questionGroup[PensionSchemes].getOrElse(PensionSchemes())

          textLines ++= textLine("Employer:" + jobDetails.employerName)
          if (jobDetails.p45LeavingDate.isDefined)
            textLines ++= textLine("What is the leaving date on your P45, if you have one? = ", jobDetails.p45LeavingDate.get.`dd/MM/yyyy`)

          if (lastWage.sameAmountEachTime.isDefined)
            textLines ++= textLine("About your wage,[[past=Did you]] [[present=Do you]] get the same amount each time? = ", lastWage.sameAmountEachTime.get)

          if(aboutExpenses.payAnyoneToLookAfterChildren == "yes") {
            if (aboutExpenses.howMuchLookAfterChildren.nonEmpty)
              textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to childcare expenses? = ", currencyAmount(aboutExpenses.howMuchLookAfterChildren))
            textLines ++= textLine("How often [[past=did you]] [[present=do you]] - expenses related to childcare expenses? = ", PensionPaymentFrequency.mapToHumanReadableString(aboutExpenses.howOftenLookAfterChildren.get))
            if (aboutExpenses.howOftenLookAfterChildren.get.other.isDefined)
              textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to childcare expenses? = ", aboutExpenses.howOftenLookAfterChildren.get.other.getOrElse(""))
            if (aboutExpenses.relationToYouLookAfterChildren.nonEmpty)
              textLines ++= textLine(Messages("relationToYouLookAfterChildren") + " = ", aboutExpenses.relationToYouLookAfterChildren.get)
            if (aboutExpenses.relationToPersonLookAfterChildren.nonEmpty)
              textLines ++= textLine(Messages("relationToPersonLookAfterChildren") + " = ", aboutExpenses.relationToPersonLookAfterChildren.get)
          }
          if(aboutExpenses.payAnyoneToLookAfterPerson == "yes") {
            if (aboutExpenses.howMuchLookAfterPerson.nonEmpty)
              textLines ++= textLine("How much [[past=did you]] [[present=do you]] pay them - expenses related to person you care for? = ", currencyAmount(aboutExpenses.howMuchLookAfterPerson))
            textLines ++= textLine("How often [[past=did you]] [[present=do you]] - expenses related to the person you care for? = ", PensionPaymentFrequency.mapToHumanReadableString(aboutExpenses.howOftenLookAfterPerson.get))
            if (aboutExpenses.howOftenLookAfterPerson.get.other.isDefined)
              textLines ++= textLine("How often [[past=did you]] [[present=do you]] Other - expenses related to the person you care for? = ", aboutExpenses.howOftenLookAfterPerson.get.other.getOrElse(""))
            if (aboutExpenses.relationToYouLookAfterPerson.nonEmpty)
              textLines ++= textLine(Messages("relationToYouLookAfterPerson") + " = ", aboutExpenses.relationToYouLookAfterPerson.get)
            if (aboutExpenses.relationToPersonLookAfterPerson.nonEmpty)
              textLines ++= textLine(Messages("relationToPersonLookAfterPerson") + " = ", aboutExpenses.relationToPersonLookAfterPerson.get)
          }

          if (pensionScheme.howOftenPension.isDefined && pensionScheme.howOftenPension.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Occupational Pension Scheme? = ", pensionScheme.howOftenPension.get.other.getOrElse(""))
          if (pensionScheme.howOftenPersonal.isDefined && pensionScheme.howOftenPersonal.get.frequency == PensionPaymentFrequency.Other)
            textLines ++= textLine("How often other - Personal Pension Scheme? = ", pensionScheme.howOftenPersonal.get.other.getOrElse(""))
          textLine()
        }

        textLines
      case None => NodeSeq.Empty
    }
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())

    val aboutOtherMoney_howOftenOther = aboutOtherMoney.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val ssp_howOftenOther = aboutOtherMoney.statutorySickPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val smp_howOftenOther = aboutOtherMoney.otherStatutoryPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }

    textSeparatorLine("Other Money") ++
      textLine("Have you <or your partner/spouse> claimed or received any other benefits since the date you want to claim? = ", aboutOtherMoney.yourBenefits.answer) ++
      textLine("Have you received any payments for the person you care for or any other person since your claim date? = ", aboutOtherMoney.anyPaymentsSinceClaimDate.answer) ++
      textLine("Benefits and payments: Who pays you? = ", aboutOtherMoney.whoPaysYou) ++
      textLine("Benefits and payments: How much? = ",currencyAmount(aboutOtherMoney.howMuch)) ++
      textLine("Benefits and payments: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.howOften)) ++
      textLine("Benefits and payments: How often other? = ", aboutOtherMoney_howOftenOther) ++
      textLine("Statutory Sick Pay: How much? = ", currencyAmount(aboutOtherMoney.statutorySickPay.howMuch)) ++
      textLine("Statutory Sick Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.statutorySickPay.howOften)) ++
      textLine("Statutory Sick Pay: How often other? = ", ssp_howOftenOther) ++
      textLine("Other Statutory Pay: How much? = ", currencyAmount(aboutOtherMoney.otherStatutoryPay.howMuch)) ++
      textLine("Other Statutory Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.otherStatutoryPay.howOften)) ++
      textLine("Other Statutory Pay: How often other? = ", smp_howOftenOther)
  }

  private def sectionEmpty(nodeSeq: NodeSeq) = {
    if (nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty
  }

  def currencyAmount(currency: Option[String]) = {
    currency match {
      case Some(s) => {
        if(s.split(poundSign).size >1) s.split(poundSign)(1)
        else s
      }
      case _ =>""
    }
  }

  def currencyAmount(currency:String) = {
    if(currency.split(poundSign).size >1) currency.split(poundSign)(1)
    else currency
  }
}
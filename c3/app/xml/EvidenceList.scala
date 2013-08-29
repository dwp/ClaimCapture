package xml

import models.domain._
import controllers.Mappings.yes
import XMLHelper.{stringify, booleanStringToYesNo}
import scala.xml.{NodeSeq, NodeBuffer, Elem}
import app.{PensionPaymentFrequency, StatutoryPaymentFrequency}

object EvidenceList {

  def xml(claim: Claim) = {
    <EvidenceList>
      {evidence(claim)}{carersAllowance(claim)}{aboutYou(claim)}{yourPartner(claim)}{careYouProvide(claim)}{breaks(claim)}{timeSpentAbroad(claim)}{fiftyTwoWeeksTrips(claim)}{employment(claim)}{selfEmployment(claim)}{otherMoney(claim)}{consentAndDeclaration(claim)}
    </EvidenceList>
  }

  def evidence(claim: Claim): NodeBuffer = {
    val employment = claim.questionGroup[models.domain.Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val buffer = new NodeBuffer

    if (employed || selfEmployed) {
      buffer += textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed) {
        buffer += textLine()
        buffer += textLine("Your Employment documents")
        buffer += textLine("Last payslip you got before your claim date: ", claimDate.dateOfClaim.`dd/MM/yyyy`)
        buffer += textLine("Any payslips you have had since then")
      }

      if (selfEmployed) {
        buffer += textLine()
        buffer += textLine("Your Self-employed documents")
        buffer += textLine("Most recent finalised accounts you have for your busines")
      }

      buffer += textLine()
      buffer += textLine("Send the above documents to:")
      buffer += textLine("CA Freepost")
      buffer += textLine("Palatine House")
      buffer += textLine("Preston")
      buffer += textLine("PR1 1HN")
      buffer += textLine("The Carer's Allowance unit will contact you if they need any further information.")
      buffer += textLine()
    }

    buffer
  }

  def carersAllowance(claim: Claim) = {
    val benefits = claim.questionGroup[Benefits].getOrElse(Benefits())
    val hours = claim.questionGroup[Hours].getOrElse(Hours())
    val over16 = claim.questionGroup[Over16].getOrElse(Over16())
    val livesInGB = claim.questionGroup[LivesInGB].getOrElse(LivesInGB())

    textSeparatorLine("Can you get Carers Allowance?") ++
      textLine("Does the person you care for get one of these benefits? = ", benefits.answerYesNo) ++
      textLine("Do you spend 35 hours or more each week caring for the person you look after? = ", hours.answerYesNo) ++
      textLine("Do you normally live in Great Britain? = ", livesInGB.answerYesNo) ++
      textLine("Are you aged 16 or over? = ", over16.answerYesNo)
  }

  def aboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val timeOutsideUK = claim.questionGroup[TimeOutsideUK].getOrElse(TimeOutsideUK())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())

    textSeparatorLine("About You") ++
      textLine("Have you always lived in the UK? = ", yourDetails.alwaysLivedUK) ++
      textLine("Mobile number = ", yourContactDetails.mobileNumber) ++
      textLine("Are you currently living in the UK? = ", timeOutsideUK.livingInUK.answer) ++
      textLine("Do you get state Pension? = ", moreAboutYou.receiveStatePension) ++
      textLine("If you have speech or hearing difficulties, would you like us to contact you by textphone? = ", yourContactDetails.contactYouByTextphone)
  }

  def yourPartner(claim: Claim) = {
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())

    if (personYouCareFor.isPartnerPersonYouCareFor.nonEmpty) {
      textSeparatorLine("About Your Partner") ++
        textLine("Is your partner/spouse the person you are claiming Carer's Allowance for? = ", personYouCareFor.isPartnerPersonYouCareFor)
    }
  }

  def careYouProvide(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())

    textSeparatorLine("About Care You Provide") ++
      textLine("Do they live at the same address as you? = ", theirPersonalDetails.liveAtSameAddressCareYouProvide) ++
      textLine("Does this person get Armed Forces Independence Payment? = ", moreAboutThePerson.armedForcesPayment)
  }

  def breaks(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

    for {break <- breaksInCare.breaks} yield {
      textLine("Where were you during the break? Other detail =", break.whereYou.other) ++
      textLine("Where was the person you care for during the break? = ", break.wherePerson.location) ++
        textLine("Where was the person you care for during the break? Other detail = ", break.wherePerson.other)
      }
  }

  def timeSpentAbroad(claim: Claim) = {
    val normalResidenceAndCurrentLocation = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    textSeparatorLine("Time abroad") ++
      textLine("Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands? = ", normalResidenceAndCurrentLocation.whereDoYouLive.answer) ++
      textLine("Have you had any more trips out of Great Britain for more than 52 weeks at a time, " +
        s"since ${claimDate.dateOfClaim.`dd/MM/yyyy`} (this is 156 weeks before your claim date)? = ", if (trips.fiftyTwoWeeksTrips.size > 0) "yes" else "no") ++
      textLine(s"Have you been out of Great Britain with the person you care for, for more than four weeks at a time, " +
        s"since ${claimDate.dateOfClaim.`dd/MM/yyyy`} (this is 3 years before your claim date)? = ", if (trips.fourWeeksTrips.size > 0) "yes" else "no")
  }

  def fiftyTwoWeeksTrips(claim: Claim) = {
    val trips = claim.questionGroup[Trips].getOrElse(Trips())
    for {fiftyTwoWeekTrip <- trips.fiftyTwoWeeksTrips} yield textLine("Where did you go? = ", fiftyTwoWeekTrip.where)
  }

  def selfEmployment(claim: Claim) = {
    val yourAccounts = claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())
    val childCare = claim.questionGroup[ChildcareExpensesWhileAtWork].getOrElse(ChildcareExpensesWhileAtWork())
    val expensesWhileAtWork = claim.questionGroup[ExpensesWhileAtWork].getOrElse(ExpensesWhileAtWork())

    val textLines = textLine("Are the income, outgoings and profit in these accounts similar to your current level of trading? = ", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading) ++
      textLine("Please tell us why and when the change happened = ", yourAccounts.tellUsWhyAndWhenTheChangeHappened) ++
      textLine("How often [[past=did you]] [[present=do you]] childcare expenses = ", PensionPaymentFrequency.mapToHumanReadableString(childCare.howOftenPayChildCare)) ++
      textLine("How often [[past=did you]] [[present=do you]] pay expenses related to the person you care for = ", PensionPaymentFrequency.mapToHumanReadableString(expensesWhileAtWork.howOftenPayExpenses))

    if(sectionEmpty(textLines)) NodeSeq.Empty else textSeparatorLine("Self Employment") ++ textLines
  }

  def employment(claim: Claim) = {
    claim.questionGroup[Jobs] match {
      case Some(jobs) =>
        var nodeSeq = NodeSeq.Empty
        nodeSeq = nodeSeq ++ textSeparatorLine("Employment")

        for (job <- jobs) {

          val jobDetails = job.questionGroup[JobDetails].getOrElse(JobDetails())
          val lastWage = job.questionGroup[LastWage].getOrElse(LastWage())
          nodeSeq = nodeSeq ++ textLine("Employer:" + jobDetails.employerName)
          if (jobDetails.p45LeavingDate.isDefined) {
            nodeSeq = nodeSeq ++ textLine("What is the leaving date on your P45, if you have one? = ", jobDetails.p45LeavingDate.get.`dd/MM/yyyy`)
          }
          if (lastWage.sameAmountEachTime.isDefined) {
            nodeSeq = nodeSeq ++ textLine("About your wage,[[past=Did you]] [[present=Do you]] get the same amount each time? =", lastWage.sameAmountEachTime.get)
          }

          nodeSeq = nodeSeq ++ textLine("")
        }

        nodeSeq
      case None => NodeSeq.Empty
    }
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val otherStatutoryPay = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())

    val aboutOtherMoney_howOftenOther = aboutOtherMoney.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val ssp_howOftenOther = statutorySickPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }
    val smp_howOftenOther = otherStatutoryPay.howOften match {
      case Some(s) => s.other.getOrElse("")
      case _ => ""
    }

    textSeparatorLine("Other Money") ++
      textLine("Have you <or your partner/spouse> claimed or received any other benefits since the date you want to claim? = ", aboutOtherMoney.yourBenefits.answer) ++
      textLine("Have you received any payments for the person you care for or any other person since your claim date? = ", aboutOtherMoney.anyPaymentsSinceClaimDate.answer) ++
      textLine("Details about other money: Who pays you? = ", aboutOtherMoney.whoPaysYou) ++
      textLine("Details about other money: How much? = ", aboutOtherMoney.howMuch) ++
      textLine("Details about other money: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(aboutOtherMoney.howOften)) ++
      textLine("Details about other money: How often other? = ", aboutOtherMoney_howOftenOther) ++
      textLine("Statutory Sick Pay: How much? = ", statutorySickPay.howMuch) ++
      textLine("Statutory Sick Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(statutorySickPay.howOften)) ++
      textLine("Statutory Sick Pay: How often other? = ", ssp_howOftenOther) ++
      textLine("Other Statutory Pay: How much? = ", otherStatutoryPay.howMuch) ++
      textLine("Other Statutory Pay: How often? = ", StatutoryPaymentFrequency.mapToHumanReadableStringWithOther(otherStatutoryPay.howOften)) ++
      textLine("Other Statutory Pay: How often other? = ", smp_howOftenOther) ++
      textLine("Are you, your wife, husband, civil partner or parent you are dependent on, " +
        "receiving  any pensions or benefits from another EEA State or Switzerland? = ", otherEEAState.benefitsFromOtherEEAStateOrSwitzerland) ++
      textLine("Are you, your wife, husband, civil partner or parent you are dependent on " +
        "working in or paying insurance to another EEA State or Switzerland? = ", otherEEAState.workingForOtherEEAStateOrSwitzerland)
  }

  def consentAndDeclaration(claim: Claim) = {
    val disclaimer = claim.questionGroup[Disclaimer].getOrElse(Disclaimer())
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())
    val additionalInfo = claim.questionGroup[AdditionalInfo].getOrElse(AdditionalInfo())

    textSeparatorLine("Consent and Declaration") ++
      textLine("Disclaimer text and tick box = ", booleanStringToYesNo(disclaimer.read)) ++
      textLine("Declaration tick box = ", booleanStringToYesNo(declaration.read)) ++
      textLine("Someone else tick box = ", booleanStringToYesNo(stringify(declaration.someoneElse))) ++
      textLine("Do you live in Wales and would like to receive future communications in Welsh? = ", booleanStringToYesNo(additionalInfo.welshCommunication))
  }

  def textSeparatorLine(title: String) = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>
      {s"$padding$title$padding"}
    </TextLine>
  }


  def sectionEmpty(nodeSeq:NodeSeq) = { if(nodeSeq == null || nodeSeq.isEmpty) true else nodeSeq.text.isEmpty }

  private def textLine(): Elem = <TextLine/>

  private def textLine(text: String): Elem = <TextLine>
    {text}
  </TextLine>

  private def textLine(label: String, value: String): Elem = value match {
    case "" => <TextLine/>
    case _ => <TextLine>
      {s"$label $value"}
    </TextLine>
  }

  private def textLine(label: String, value: Option[String]): Elem = value match {
    case Some(s) => <TextLine>
      {s"$label $s"}
    </TextLine>
    case None => <TextLine/>
  }
}
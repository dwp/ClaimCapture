package xml

import models.domain._
import controllers.Mappings.yes
import XMLHelper.{stringify, booleanStringToYesNo}
import scala.xml.{NodeBuffer, Elem}
import app.{XMLValues, StatutoryPaymentFrequency}

object EvidenceList {

  def xml(claim: Claim) = {
    <EvidenceList>
      {evidence(claim)}
      {carersAllowance(claim)}
      {aboutYou(claim)}
      {yourPartner(claim)}
      {careYouProvide(claim)}
      {breaks(claim)}
      {timeSpentAbroad(claim)}
      {fiftyTwoWeeksTrips(claim)}
      {otherMoney(claim)}
      {consentAndDeclaration(claim)}
    </EvidenceList>
  }

  def evidence(claim: Claim): NodeBuffer = {
    val employment = claim.questionGroup[Employment].getOrElse(models.domain.Employment())
    val employed = employment.beenEmployedSince6MonthsBeforeClaim == yes
    val selfEmployed = employment.beenSelfEmployedSince1WeekBeforeClaim == yes
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    val buffer = new NodeBuffer

    if (employed || selfEmployed) {
      buffer += textLine("Send us the following documents below including your Name and National Insurance (NI) number.")

      if (employed){
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
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())

    textSeparatorLine("About Your Partner") ++
      textLine("Does your partner/spouse live at the same address as you? = ", {XMLValues.NotAsked}) ++
      textLine("Is your partner/spouse the person you are claiming Carer's Allowance for? = ", personYouCareFor.isPartnerPersonYouCareFor)
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

    for { break <- breaksInCare.breaks } yield {
      textLine("Where was the person you care for during the break? = ", break.wherePerson.location) ++
      textLine("Other detail ? = ", break.wherePerson.other)
    }
  }

  def timeSpentAbroad(claim: Claim) = {
    val normalResidenceAndCurrentLocation = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    val abroadForMoreThan52Weeks = claim.questionGroup[AbroadForMoreThan52Weeks].getOrElse(AbroadForMoreThan52Weeks())
    val abroadForMoreThan4Weeks = claim.questionGroup[AbroadForMoreThan4Weeks].getOrElse(AbroadForMoreThan4Weeks())
    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())

    textSeparatorLine("Abroad") ++
      textLine("Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands? = ", normalResidenceAndCurrentLocation.whereDoYouLive.answer) ++
      textLine("Have you had any more trips out of Great Britain for more than 52 weeks at a time, " +
        s"since ${claimDate.dateOfClaim.`dd/MM/yyyy`} (this is 156 weeks before your claim date)? = ", abroadForMoreThan52Weeks.anyTrips)
      textLine(s"Have you been out of Great Britain with the person you care for, for more than four weeks at a time, " +
        s"since ${claimDate.dateOfClaim.`dd/MM/yyyy`} (this is 3 years before your claim date)? = ", abroadForMoreThan4Weeks.anyTrips)
  }

  def fiftyTwoWeeksTrips(claim: Claim) = {
    val trips  = claim.questionGroup[Trips].getOrElse(Trips())
    for { fiftyTwoWeekTrip <- trips.fiftyTwoWeeksTrips } yield textLine("Where did you go? = ", fiftyTwoWeekTrip.where)
  }

  def selfEmployment(claim: Claim) = {
    val yourAccounts = claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())
    val childCare = claim.questionGroup[ChildcareExpensesWhileAtWork].getOrElse(ChildcareExpensesWhileAtWork())

    textSeparatorLine("Self Employment") ++
      textLine("Are the income, outgoings and profit in these accounts similar to your current level of trading? = ", yourAccounts.areIncomeOutgoingsProfitSimilarToTrading) ++
      textLine("Please tell us why and when the change happened = ", yourAccounts.tellUsWhyAndWhenTheChangeHappened) ++
      textLine("How often [[past=did you]] [[present=do you]] childcare expenses = ", childCare.howOftenPayChildCare)
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val otherStatutoryPay = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
println("************************************** statutorySickPay.howOften: " + statutorySickPay.howOften)
    textSeparatorLine("Other Money") ++
      textLine("Have you <or your partner/spouse> claimed or received any other benefits since the date you want to claim? = ", aboutOtherMoney.yourBenefits.answer) ++
      textLine("Have you received any payments for the person you care for or any other person since your claim date? = ", aboutOtherMoney.anyPaymentsSinceClaimDate.answer) ++
      textLine("Details about other money: Who pays you? = ", aboutOtherMoney.whoPaysYou) ++
      textLine("Details about other money: How much? = ", aboutOtherMoney.howMuch) ++
      textLine("Statutory Sick Pay: How much? = ", statutorySickPay.howMuch) ++
      textLine(" *********************************** Statutory Sick Pay: How often? = ", StatutoryPaymentFrequency.optionToString(statutorySickPay.howOften)) ++
      textLine("Other Statutory Pay: How much? = ", otherStatutoryPay.howMuch) ++
      textLine("Other Statutory Pay: How often? = ", StatutoryPaymentFrequency.optionToString(otherStatutoryPay.howOften)) ++
      textLine("Are you, your wife, husband, civil partner or parent you are dependent on, " +
        "receiving  any pensions or benefits from another EEA State or Switzerland? = ", otherEEAState.benefitsFromOtherEEAStateOrSwitzerland) ++
      textLine("Are you, your wife, husband, civil partner or parent you are dependent on " +
        "working in or paying insurance to another EEA State or Switzerland? = ", otherEEAState.workingForOtherEEAStateOrSwitzerland)
  }



  def consentAndDeclaration(claim: Claim) = {
    val consent = claim.questionGroup[Consent].getOrElse(Consent())
    val disclaimer = claim.questionGroup[Disclaimer].getOrElse(Disclaimer())
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    textSeparatorLine("Consent and Declaration") ++
      textLine("Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? = ", consent.informationFromEmployer.answer) ++
      textLine("Please tell us why = ", consent.informationFromEmployer.text) ++
      textLine("Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? = ", consent.informationFromPerson.answer) ++
      textLine("Please tell us why = ", consent.informationFromPerson.text) ++
      textLine("Disclaimer text and tick box = ", booleanStringToYesNo(disclaimer.read)) ++
      textLine("Declaration tick box = ", booleanStringToYesNo(declaration.read)) ++
      textLine("Someone else tick box = ", booleanStringToYesNo(stringify(declaration.someoneElse)))
  }

  def textSeparatorLine(title: String) = {
    val lineWidth = 54
    val padding = "=" * ((lineWidth - title.length) / 2)

    <TextLine>{s"$padding$title$padding"}</TextLine>
  }

  private def textLine(): Elem = <TextLine/>

  private def textLine(text: String): Elem = <TextLine>{text}</TextLine>

  private def textLine(label:String, value:String): Elem = value match {
    case "" => <TextLine/>
    case _ => <TextLine>{s"$label $value"}</TextLine>
  }

  private def textLine(label: String, value: Option[String]): Elem = value match {
    case Some(s) => <TextLine>{s"$label $s"}</TextLine>
    case None => <TextLine/>
  }
}
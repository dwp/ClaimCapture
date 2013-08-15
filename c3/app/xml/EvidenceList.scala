package xml

import models.domain._
import XMLHelper.stringify

object EvidenceList {

  val separationLine = "=========================================================================================="

  def xml(claim: Claim) = {
    <EvidenceList>
      <TextLine>Documents you need to send us</TextLine>
      <TextLine>You must send us all the documents we ask for. If you do not, any benefit you may be entitled to because of this claim may be delayed.</TextLine>
      <TextLine>Your pay details</TextLine>
      <TextLine>You need to send us the last payslip before 10 May 2013 and all the payslips you have had since then.</TextLine>
      <TextLine>Statement signed by Minnie Mouse</TextLine>
      <TextLine>You need to send us the completed and signed statement.</TextLine>
      <TextLine>Send us your signature with the documents we have asked for</TextLine>
      <TextLine>If you have printed this page, you must sign it in the box provided and send it with the documents we have asked for.</TextLine>
      <TextLine>If you made a note of your transaction details, you must sign the note and send it with the documents we have asked for.</TextLine>
      <TextLine>Where to send the documents</TextLine>
      <TextLine>Post the documents we have asked for with your signed transaction details to:</TextLine>
      <TextLine>CA Freepost</TextLine>
      <TextLine>Palatine House</TextLine>
      <TextLine>Preston</TextLine>
      <TextLine>PR1 1HN</TextLine>
      <TextLine>{separationLine}</TextLine>
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

  def carersAllowance(claim: Claim) = {
    val benefits = claim.questionGroup[Benefits].getOrElse(Benefits())
    val hours = claim.questionGroup[Hours].getOrElse(Hours())
    val over16 = claim.questionGroup[Over16].getOrElse(Over16())
    val livesInGB = claim.questionGroup[LivesInGB].getOrElse(LivesInGB())
    <TextLine>{sectionSeparationLine("Can you get Carers Allowance?")}</TextLine>
    <TextLine>Does the person you care for get one of these benefits? = {benefits.answerYesNo}</TextLine>
    <TextLine>Do you spend 35 hours or more each week caring for the person you look after? = {hours.answerYesNo}</TextLine>
    <TextLine>Do you normally live in Great Britain? = {livesInGB.answerYesNo}</TextLine>
    <TextLine>Are you aged 16 or over? = {over16.answerYesNo}</TextLine>
  }

  def aboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val timeOutsideUK = claim.questionGroup[TimeOutsideUK].getOrElse(TimeOutsideUK())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    <TextLine>{sectionSeparationLine("About You")}</TextLine>
    <TextLine>Have you always lived in the UK? = {yourDetails.alwaysLivedUK}</TextLine>
    <TextLine>Mobile number = {yourContactDetails.mobileNumber.orNull}</TextLine>
    <TextLine>Are you currently living in the UK? = {timeOutsideUK.livingInUK.answer}</TextLine>
    <TextLine>Do you get state Pension? = {moreAboutYou.receiveStatePension}</TextLine>
  }

  def yourPartner(claim: Claim) = {
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())
    <TextLine>{sectionSeparationLine("About Your Partner")}</TextLine>
    <TextLine>Does your partner/spouse live at the same address as you? = {yourPartnerPersonalDetails.liveAtSameAddress}</TextLine>
    <TextLine>Is your partner/spouse the person you are claiming Carer's Allowance for? = {personYouCareFor.isPartnerPersonYouCareFor}</TextLine>
  }

  def careYouProvide(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup[TheirPersonalDetails].getOrElse(TheirPersonalDetails())
    val moreAboutThePerson = claim.questionGroup[MoreAboutThePerson].getOrElse(MoreAboutThePerson())
    val previousCarerContactDetails = claim.questionGroup[PreviousCarerContactDetails].getOrElse(PreviousCarerContactDetails())
    val representativesForPerson = claim.questionGroup[RepresentativesForPerson].getOrElse(RepresentativesForPerson())
    <TextLine>{sectionSeparationLine("About Care You Provide")}</TextLine>
    <TextLine>Do they live at the same address as you? = {theirPersonalDetails.liveAtSameAddressCareYouProvide}</TextLine>
    <TextLine>Does this person get Armed Forces Independence Payment? = {moreAboutThePerson.armedForcesPayment.orNull}</TextLine>
    <TextLine>Daytime phone number = {previousCarerContactDetails.phoneNumber.orNull}</TextLine>
    <TextLine>Mobile number = {previousCarerContactDetails.mobileNumber.orNull}</TextLine>
    <TextLine>Person acts as = {representativesForPerson.someoneElseAct.dropDownValue.orNull}</TextLine>
    <TextLine>Full name = {representativesForPerson.someoneElseAct.text.orNull}</TextLine>
  }

  def breaks(claim: Claim) = {
    val breaksInCare = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
    for {break <- breaksInCare.breaks} yield <TextLine>Where was the person you care for during the break? = {break.wherePerson.location} {break.wherePerson.other.orNull}</TextLine>
  }

  def timeSpentAbroad(claim: Claim) = {
    val normalResidenceAndCurrentLocation = claim.questionGroup[NormalResidenceAndCurrentLocation].getOrElse(NormalResidenceAndCurrentLocation())
    val abroadForMoreThan52Weeks = claim.questionGroup[AbroadForMoreThan52Weeks].getOrElse(AbroadForMoreThan52Weeks())
    <TextLine>{sectionSeparationLine("Abroad")}</TextLine>
    <TextLine>Do you normally live in the UK, Republic of Ireland, Isle of Man or the Channel Islands? = {normalResidenceAndCurrentLocation.whereDoYouLive.answer}</TextLine>
    <TextLine>Have you had any more trips out of Great Britain for more than 52 weeks at a time, since [[Claim Date _ 156 weeks]] (this is 156 weeks before your claim date)? = {abroadForMoreThan52Weeks.anyTrips}</TextLine>
  }

  def fiftyTwoWeeksTrips(claim: Claim) = {
    val trips  = claim.questionGroup[Trips].getOrElse(Trips())
    for {fiftyTwoWeekTrip <- trips.fiftyTwoWeeksTrips} yield <TextLine>Where did you go? = {fiftyTwoWeekTrip.where}</TextLine>
  }

  def selfEmployment(claim: Claim) = {
    val yourAccounts = claim.questionGroup[SelfEmploymentYourAccounts].getOrElse(SelfEmploymentYourAccounts())
    <TextLine>{sectionSeparationLine("Self Employment")}</TextLine>
    <TextLine>Are the income, outgoings and profit in these accounts similar to your current level of trading? = {yourAccounts.areIncomeOutgoingsProfitSimilarToTrading.orNull} </TextLine>
    <TextLine>Please tell us why and when the change happened = {yourAccounts.tellUsWhyAndWhenTheChangeHappened.orNull}</TextLine>
  }

  def otherMoney(claim: Claim) = {
    val aboutOtherMoney = claim.questionGroup[AboutOtherMoney].getOrElse(AboutOtherMoney())
    val statutorySickPay = claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())
    val otherStatutoryPay = claim.questionGroup[OtherStatutoryPay].getOrElse(OtherStatutoryPay())
    val otherEEAState = claim.questionGroup[OtherEEAStateOrSwitzerland].getOrElse(OtherEEAStateOrSwitzerland())
    <TextLine>{sectionSeparationLine("Other Money")}</TextLine>
    <TextLine>Have you [or your partner/spouse] claimed or received any other benefits since the date you want to claim? = {aboutOtherMoney.yourBenefits.answer}</TextLine>
    <TextLine>Please tell us the names of the benefits or entitlements you receive = {aboutOtherMoney.yourBenefits.text.orNull}</TextLine>
    <TextLine>Statutory Sick Pay: How much? = {statutorySickPay.howMuch.orNull}</TextLine>
    <TextLine>Statutory Sick Pay: How often? = {stringify(statutorySickPay.howOften)}</TextLine>
    <TextLine>Other Statutory Pay: How much? = {otherStatutoryPay.howMuch.orNull}</TextLine>
    <TextLine>Other Statutory Pay: How often? = {stringify(otherStatutoryPay.howOften)}</TextLine>
    <TextLine>Are you, your wife, husband, civil partner or parent you are dependent on, receiving  any pensions or benefits from another EEA State or Switzerland? = {otherEEAState.benefitsFromOtherEEAStateOrSwitzerland}</TextLine>
    <TextLine>Are you, your wife, husband, civil partner or parent you are dependent on working in or paying insurance to another EEA State or Switzerland? = {otherEEAState.workingForOtherEEAStateOrSwitzerland}</TextLine>
  }

  def consentAndDeclaration(claim: Claim) = {
    val consent = claim.questionGroup[Consent].getOrElse(Consent())
    val disclaimer = claim.questionGroup[Disclaimer].getOrElse(Disclaimer())
    val declaration = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())
    <TextLine>{sectionSeparationLine("Consent and Declaration")}</TextLine>
    <TextLine>Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? = {consent.informationFromEmployer}</TextLine>
    <TextLine>Please tell us why = {consent.informationFromEmployer.text.orNull}</TextLine>
    <TextLine>Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? = {consent.informationFromPerson}</TextLine>
    <TextLine>Please tell us why = {consent.whyPerson.orNull}</TextLine>
    <TextLine>Disclaimer text and tick box = {booleanStringToYesNo(disclaimer.read)}</TextLine>
    <TextLine>Declaration tick box = {booleanStringToYesNo(declaration.read)}</TextLine>
    <TextLine>Someone else tick box = {booleanStringToYesNo(stringify(declaration.someoneElse))}</TextLine>
  }

  def booleanStringToYesNo(booleanString: String) = booleanString match {
    case "true" => "yes"
    case "false" => "no"
    case _ => ""
  }

  def sectionSeparationLine(section: String) = {
    val name = " " + section + " "
    val offsetLeft = (separationLine.length - name.length) / 2
    val offsetRight = separationLine.length - offsetLeft - (name.length % 2)

    val firstHalf = separationLine.splitAt(offsetLeft)._1
    val secondHalf = separationLine.splitAt(offsetRight)._2

    firstHalf + name + secondHalf
  }
}
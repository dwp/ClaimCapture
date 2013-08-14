package xml

import models.domain._

object EvidenceList {

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

      <TextLine>=======================================================================================                                              </TextLine>
      {carersAllowance(claim)}
      {aboutYou(claim)}
      {yourPartner(claim)}
      {careYouProvide(claim)}
      <TextLine>================ ================ Employment === ==============================                                                      </TextLine>
      <TextLine>Have you finished this job? = Yes                                                                                                    </TextLine>
      <TextLine>Did you pay for anything else necessary to do your job? = Yes                                                                        </TextLine>
      <TextLine>Have you had another job at any time since 20/6/2011, this is six months before your claim date of 20/3/2013 ? = Yes                 </TextLine>
      <TextLine>================ ================ Self Employment === ==============================                                                 </TextLine>
      <TextLine>Are these accounts prepared on a cash flow basis?                                                                                    </TextLine>
      <TextLine>Are the income, outgoings and profit in these accounts similar to your current level of trading?                                     </TextLine>
      <TextLine>Please tell us why and when the change happened                                                                                      </TextLine>
      <TextLine>=======================================================================================                                              </TextLine>

    </EvidenceList>
  }

  def carersAllowance(claim:Claim) = {
    val benefits = claim.questionGroup[Benefits].getOrElse(Benefits())
    val hours = claim.questionGroup[Hours].getOrElse(Hours())
    val over16 = claim.questionGroup[Over16].getOrElse(Over16())
    val livesInGB = claim.questionGroup[LivesInGB].getOrElse(LivesInGB())
    <TextLine>================ =========== Can you get Carers Allowance? ============================</TextLine>
    <TextLine>Does the person you care for get one of these benefits? = {benefits.answer}</TextLine>
    <TextLine>Do you spend 35 hours or more each week caring for the person you look after? = {hours.answer}</TextLine>
    <TextLine>Do you normally live in Great Britain? = {livesInGB.answer}</TextLine>
    <TextLine>Are you aged 16 or over? = {over16.answer}</TextLine>
  }


  def aboutYou(claim:Claim) = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val yourContactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())
    val timeOutsideUK = claim.questionGroup[TimeOutsideUK].getOrElse(TimeOutsideUK())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    <TextLine>================ ================== About You  =====================================</TextLine>
    <TextLine>Have you always lived in the UK? = {yourDetails.alwaysLivedUK}</TextLine>
    <TextLine>Mobile number = {yourContactDetails.mobileNumber.orNull}</TextLine>
    <TextLine>Are you currently living in the UK? = {timeOutsideUK.livingInUK.answer}</TextLine>
    <TextLine>Do you get state Pension?? = {moreAboutYou.receiveStatePension}</TextLine>
  }

  def yourPartner(claim:Claim) = {
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val personYouCareFor = claim.questionGroup[PersonYouCareFor].getOrElse(PersonYouCareFor())
    <TextLine>================ =============== About Your Partner ==================================</TextLine>
    <TextLine>Does your partner/spouse live at the same address as you? = {yourPartnerPersonalDetails.liveAtSameAddress}</TextLine>
    <TextLine>Is your partner/spouse the person you are claiming Carer's Allowance for? = {personYouCareFor.isPartnerPersonYouCareFor}</TextLine>
  }

  def careYouProvide(claim:Claim) = {
    <TextLine>================ ============== About Care You Provide ==============================</TextLine>
    <TextLine>Do they live at the same address as you? = No</TextLine>
    <TextLine>Does this person get Armed Forces Independence Payment? = No</TextLine>
    <TextLine>Daytime phone number = 0191 2357761</TextLine>
    <TextLine>Mobile number = 0181 3839839</TextLine>
    <TextLine>You act as = Parent</TextLine>
    <TextLine>Person acts as = Attorney</TextLine>
    <TextLine>Full name = Lawyer Brown</TextLine>
    <TextLine>Where was the Person you care for during the break? = Hospital</TextLine>
    <TextLine>Have you had any more breaks in care for this person since your claim date: : 20/3/2012 ?  = Yes</TextLine>
  }

}

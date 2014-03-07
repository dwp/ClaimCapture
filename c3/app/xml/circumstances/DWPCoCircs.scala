package xml.circumstances

import app.ReportChange._
import app.XMLValues._
import models.domain._
import scala.xml.Elem
import play.api.Logger
import models.domain.Claim
import scala.Some

object DWPCoCircs {
  def xml(circs: Claim): Elem = {
    Logger.info(s"Build DWPCoCircs")
    // TODO : Schema and in turn the followwing xml needs to be changed to reflect the new changes and to the new format: Prafulla Chandra
    /*
    <DWPCAChangeOfCircumstances>
      <Claim>
        {ClaimantDetails.xml(circs)}
        {CareeDetails.xml(circs)}
      </Claim>
      {OtherChanges.xml(circs)}
      {Declaration.xml(circs)}
      {Consents.xml(circs)}
    </DWPCAChangeOfCircumstances>
    */
    // TODO : Refactor xml method to accept transaction id or follow the DWPBody Method implementation : Prafulla Chandra
    <DWPCAChangeOfCircumstances id={/* Refactor xml transactionId*/ NotAsked}>
      <InitialQuestions>
        <ChangeAddress>
          {NotAsked}
        </ChangeAddress>
        <ChangeName>
          {NotAsked}
        </ChangeName>
        <ChangePayment>
          {NotAsked}
        </ChangePayment>
        <ChangeBankDetails>
          {NotAsked}
        </ChangeBankDetails>
        <PersonDied>
          {NotAsked}
        </PersonDied>
        <PermanentlyStoppedCaring>
          {stoppedCaring(circs)}
        </PermanentlyStoppedCaring>
        <BreakInCaring>
          {NotAsked}
        </BreakInCaring>
        <CaringForDifferentPerson>
          {NotAsked}
        </CaringForDifferentPerson>
        <StartedEmployment>
          {NotAsked}
        </StartedEmployment>
        <StartedSelfEmployment>
          {NotAsked}
        </StartedSelfEmployment>
        <ClaimADI>
          {NotAsked}
        </ClaimADI>
        <AnyOtherChanges>
          {anyOtherChanges(circs)}
        </AnyOtherChanges>
      </InitialQuestions>{// <Claim>
      CircsIdentification.xml(circs)}{// StoppedCaring
      CircsStoppedCaring.xml(circs)}{// <OtherChanges>
      AdditionalInfo.xml(circs)}<ThirdParty>
      {NotAsked}
    </ThirdParty>{// <Declaration> and <EvidenceList>
      ConsentAndDeclaration.xml(circs)}{CircsEvidenceList.xml(circs)}
    </DWPCAChangeOfCircumstances>
  }

    def stoppedCaring(circs: Claim) = {
      val reportChangesOption = circs.questionGroup[ReportChanges]

      reportChangesOption match {
        case Some(reportChanges) => {
          if (reportChanges.reportChanges == StoppedCaring.name) {
            yes
          }
          else {
            NotAsked
          }
        }
        case _ => NotAsked
      }
    }

    def anyOtherChanges(circs: Claim) = {
      val additionalInfoOption = circs.questionGroup[CircumstancesOtherInfo]
      lazy val selfEmploymentAdditionalInfoOption = circs.questionGroup[CircumstancesSelfEmployment]
      lazy val stoppedCaringOption = circs.questionGroup[CircumstancesStoppedCaring]
      lazy val paymentChangeOption = circs.questionGroup[CircumstancesPaymentChange]
      lazy val addressChangeOption = circs.questionGroup[CircumstancesAddressChange]

      if (additionalInfoOption.isDefined) {
        yes
      } else if (selfEmploymentAdditionalInfoOption.isDefined) {
        if (selfEmploymentAdditionalInfoOption.get.moreAboutChanges.isEmpty) {
          no
        } else {
          yes
        }
      } else if (stoppedCaringOption.isDefined) {
        if (stoppedCaringOption.get.moreAboutChanges.isEmpty) {
          no
        } else {
          yes
        }
      } else if (paymentChangeOption.isDefined) {
        if (paymentChangeOption.get.moreAboutChanges.isEmpty) {
          no
        } else {
          yes
        }
      } else if (addressChangeOption.isDefined) {
        if (addressChangeOption.get.moreAboutChanges.isEmpty) {
          no
        } else {
          yes
        }
      }
    }
}



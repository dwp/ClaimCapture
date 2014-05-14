package xml.circumstances

import models.domain._
import scala.xml.Elem
import play.api.Logger
import app.XMLValues._
import app.ReportChange._
import scala.Some
import xml.XMLComponent
import models.domain.Claim
import scala.Some

object DWPCoCircs extends XMLComponent {
  def xml(circs: Claim, transactionId : Option[String] = None):Elem = {
    Logger.info(s"Build DWPCoCircs : ${transactionId.getOrElse("")}")

    <DWPCAChangeOfCircumstances id={transactionId.getOrElse("")}>
      <InitialQuestions>
        <ChangeAddress>{NotAsked}</ChangeAddress>
        <ChangeName>{NotAsked}</ChangeName>
        <ChangePayment>{NotAsked}</ChangePayment>
        <ChangeBankDetails>{NotAsked}</ChangeBankDetails>
        <PersonDied>{NotAsked}</PersonDied>
        <PermanentlyStoppedCaring>{stoppedCaring(circs)}</PermanentlyStoppedCaring>
        <BreakInCaring>{NotAsked}</BreakInCaring>
        <CaringForDifferentPerson>{NotAsked}</CaringForDifferentPerson>
        <StartedEmployment>{NotAsked}</StartedEmployment>
        <StartedSelfEmployment>{NotAsked}</StartedSelfEmployment>
        <ClaimADI>{NotAsked}</ClaimADI>
        <AnyOtherChanges>{anyOtherChanges(circs)}</AnyOtherChanges>
      </InitialQuestions>
      { // <Claim>
        CircsIdentification.xml(circs)
      }
      {
        // StoppedCaring
        CircsStoppedCaring.xml(circs)
      }
      { // <OtherChanges>
        AdditionalInfo.xml(circs)
      }
      <ThirdParty>{NotAsked}</ThirdParty>
      {  // <Declaration> and <EvidenceList>
        ConsentAndDeclaration.xml(circs)
      }
      {
        CircsEvidenceList.xml(circs)
      }
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
    lazy val breaksFromCaringOption = circs.questionGroup[CircumstancesBreaksInCare]
    lazy val employmentChangesOptions = circs.questionGroup[CircumstancesEmploymentChange]

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
    } else if (breaksFromCaringOption.isDefined){
      if (breaksFromCaringOption.get.moreAboutChanges.isEmpty) {
        no
      } else {
        yes
      }
    } else if (employmentChangesOptions.isDefined){
      if (employmentChangesOptions.get.typeOfWork.text2b.isEmpty) {
        no
      } else {
        yes
      }
    }
  }
}

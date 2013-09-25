package xml

import models.domain.Circs
import scala.xml.Elem
import play.api.Logger
import app.XMLValues._

/**
 * Generate XML for a Change of Circumstances.
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
object DWPCoCircs {

  def xml(circs: Circs, transactionId : String):Elem = {
    Logger.info(s"Build DWPCoCircs : $transactionId")
    <DWPCAChangeOfCircumstances id={transactionId}>
      <InitialQuestions>
        <ChangeAddress>{NotAsked}</ChangeAddress>
        <ChangeName>{NotAsked}</ChangeName>
        <ChangePayment>{NotAsked}</ChangePayment>
        <ChangeBankDetails>{NotAsked}</ChangeBankDetails>
        <PersonDied>{NotAsked}</PersonDied>
        <PermanentlyStoppedCaring>{NotAsked}</PermanentlyStoppedCaring>
        <BreakInCaring>{NotAsked}</BreakInCaring>
        <CaringForDifferentPerson>{NotAsked}</CaringForDifferentPerson>
        <StartedEmployment>{NotAsked}</StartedEmployment>
        <StartedSelfEmployment>{NotAsked}</StartedSelfEmployment>
        <ClaimADI>{NotAsked}</ClaimADI>
        <AnyOtherChanges>yes</AnyOtherChanges>
      </InitialQuestions>
      { // <Claim>
        Identification.xml(circs)
      }
      { // <OtherChanges>
        AdditionalInfo.xml(circs)
      }
      <ThirdParty>{NotAsked}</ThirdParty>
      {  // <Declaration> and <EvidenceList>
        ConsentAndDeclaration.xml(circs)
      }
    </DWPCAChangeOfCircumstances>
  }

}

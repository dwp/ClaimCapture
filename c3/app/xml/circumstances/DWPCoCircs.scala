package xml.circumstances

import scala.xml.Elem
import play.api.Logger
import models.domain.Claim

object DWPCoCircs {

  def xml(circs: Claim): Elem = {
    Logger.info(s"Build XML for: ${circs.key} ${circs.uuid}.")

    val x=
    <DWPCAChangeOfCircumstances>
      {Claimant.xml(circs)}
      {Caree.xml(circs)}
      {BreaksInCare.xml(circs)}
      {StoppedCaring.xml(circs)}
      {AddressChange.xml(circs)}
      {SelfEmployment.xml(circs)}
      {PaymentChange.xml(circs)}
      {EmploymentChange.xml(circs)}
      {OtherChanges.xml(circs)}
      {Declaration.xml(circs)}
      {EvidenceList.buildXml(circs)}
      {Consents.xml(circs)}
    </DWPCAChangeOfCircumstances>
    println("CIRCS XML:"+x)
    x
  }

}

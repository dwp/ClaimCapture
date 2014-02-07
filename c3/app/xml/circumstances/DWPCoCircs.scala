package xml.circumstances

import models.domain.Claim
import scala.xml.Elem
import play.api.Logger
import xml.CircsEvidenceList

object DWPCoCircs {
  def xml(circs: Claim):Elem = {
    Logger.info(s"Build DWPCoCircs")

    <DWPCAChangeOfCircumstances>
      <Claim>
        {ClaimantDetails.xml(circs)}
        {CareeDetails.xml(circs)}
      </Claim>
      {OtherChanges.xml(circs)}
      {Declaration.xml(circs)}
      {Consents.xml(circs)}
      {CircsEvidenceList.xml(circs)}
    </DWPCAChangeOfCircumstances>
  }

}

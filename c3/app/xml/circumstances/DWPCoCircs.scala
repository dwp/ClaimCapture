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

    <DWPCAChangeOfCircumstances>
      {Claimant.xml(circs)}
      {Caree.xml(circs)}
      {StoppedCaring.xml(circs)}
      {AddressChange.xml(circs)}
      {PaymentChange.xml(circs)}
      {SelfEmployment.xml(circs)}
      {OtherChanges.xml(circs)}
      {Declaration.xml(circs)}
      {Consents.xml(circs)}
    </DWPCAChangeOfCircumstances>
  }

}



package xml.circumstances

import models.domain.{CircumstancesDeclaration, CircumstancesReportChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.domain.Claim
import scala.Some

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object Claimant {
  def xml(circs :Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <ClaimantDetails>
      {question(<FullName/>, "fullName", encrypt(reportChange.fullName))}
      {question(<DateOfBirth/>,"dateOfBirth", reportChange.dateOfBirth)}
      {question(<NationalInsuranceNumber/>, "nationalInsuranceNumber", encrypt(reportChange.nationalInsuranceNumber))}
      {question(<ContactPreference/>,"furtherInfoContact", reportChange.furtherInfoContact)}
    </ClaimantDetails>
  }
}

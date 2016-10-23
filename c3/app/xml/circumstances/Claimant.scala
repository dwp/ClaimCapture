package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object Claimant {
  def xml(circs :Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesYourDetails].getOrElse(CircumstancesYourDetails())

    <ClaimantDetails>
      {question(<Surname/>, "surname", encrypt(reportChange.surname))}
      {question(<OtherNames/>, "firstName", reportChange.firstName)}
      {question(<DateOfBirth/>,"dateOfBirth", reportChange.dateOfBirth)}
      {question(<NationalInsuranceNumber/>, "nationalInsuranceNumber", encrypt(reportChange.nationalInsuranceNumber))}
      {question(<ContactPreference/>,"furtherInfoContact", reportChange.furtherInfoContact)}
      {question(<WantsContactEmail/>,"wantsEmailContactCircs", reportChange.wantsContactEmail)}
      {question(<Email/>,"mail.output", reportChange.email)}
    </ClaimantDetails>
  }
}

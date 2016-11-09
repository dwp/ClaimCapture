package xml.circumstances

import models.domain.{CircumstancesYourDetails, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object Caree {
  def xml(circs :Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesYourDetails].getOrElse(CircumstancesYourDetails())

    <CareeDetails>
      {question(<Surname/>, "theirSurname", encrypt(reportChange.theirSurname))}
      {question(<OtherNames/>, "theirFirstName", reportChange.theirFirstName)}
      {question(<RelationToClaimant/>,"theirRelationshipToYou", reportChange.theirRelationshipToYou)}
    </CareeDetails>
  }
}

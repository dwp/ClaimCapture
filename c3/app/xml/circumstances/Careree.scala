package xml.circumstances

import models.domain.CircumstancesReportChange
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.domain.Claim

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object Careree {
  def xml(circs :Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <CareeDetails>
      <FullName>{reportChange.theirFullName}</FullName>
      {question(<RelationToClaimant/>,"theirRelationshipToYou", reportChange.theirRelationshipToYou)}
    </CareeDetails>
  }
}

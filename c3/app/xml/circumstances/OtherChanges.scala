package xml.circumstances

import models.domain.{CircumstancesOtherInfo, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object OtherChanges {
  def xml(circs :Claim): NodeSeq = {
    val circsOtherChangesOption: Option[CircumstancesOtherInfo] = circs.questionGroup[CircumstancesOtherInfo]

    circsOtherChangesOption match {
      case Some(circsOtherChanges) => {question(<OtherChanges/>,"reportChanges.anyOtherChanges", circsOtherChanges.change)}
      case _ => NodeSeq.Empty
    }
  }
}

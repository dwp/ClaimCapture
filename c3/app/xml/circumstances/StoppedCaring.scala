package xml.circumstances

import models.domain.{CircumstancesStoppedCaring, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * Created by neddakaltcheva on 3/13/14.
 */
object StoppedCaring {
  def xml(circs :Claim): NodeSeq = {
    val circsStoppedCaringOption: Option[CircumstancesStoppedCaring] = circs.questionGroup[CircumstancesStoppedCaring]

    circsStoppedCaringOption match {
      case Some(circsStoppedCaring) => {
        <StoppedCaring>
          {question(<DateStoppedCaring/>,"stoppedCaringDate", circsStoppedCaring.stoppedCaringDate)}
          {question(<OtherChanges/>, "moreAboutChanges", circsStoppedCaring.moreAboutChanges)}
        </StoppedCaring>
      }
      case _ => NodeSeq.Empty
    }
  }
}

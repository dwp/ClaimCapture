package xml

import app.ReportChange._
import app.XMLValues._
import models.domain.{CircumstancesStoppedCaring, Claim}
import scala.xml.NodeSeq
import XMLHelper._
import play.api.i18n.Messages
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import scala.Some
import scala.Some

object CircsStoppedCaring {
  def xml(circs: Claim) = {
    val circumstancesStoppedCaringOption = circs.questionGroup[CircumstancesStoppedCaring]

    if (circumstancesStoppedCaringOption.isDefined) {
      <StoppedCaring>
        <DateStoppedCaring>{circumstancesStoppedCaringOption.get.stoppedCaringDate.`yyyy-MM-dd`}</DateStoppedCaring>
      </StoppedCaring>
    }
  }
}

package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.domain.Claim


object SelfEmployment {

  def xml(circs :Claim): NodeSeq = {
    val circsSelfEmploymentOption: Option[CircumstancesSelfEmployment] = circs.questionGroup[CircumstancesSelfEmployment]

    circsSelfEmploymentOption match {
      case Some(circsSelfEmployment) => {
        <SelfEmployedChange>
          {question(<Caring35Hours/>,"stillCaring.answer", circsSelfEmployment.stillCaring.answer)}
          {
            circsSelfEmployment.stillCaring.answer match {
              case "no" => {question(<DateStoppedCaring35Hours/>, "whenStoppedCaring",circsSelfEmployment.stillCaring.date.get.`dd/MM/yyyy`)}
              case _ => NodeSeq.Empty
            }
          }
          {question(<BusinessStartDate/>, "whenThisSelfEmploymentStarted",circsSelfEmployment.whenThisSelfEmploymentStarted.`dd/MM/yyyy`)}
          {question(<BusinessType/>, "typeOfBusiness",circsSelfEmployment.typeOfBusiness)}
          {question(<MoreThan100/>, "totalOverWeeklyIncomeThreshold",circsSelfEmployment.totalOverWeeklyIncomeThreshold)}
          {statement(<OtherChanges/>, circsSelfEmployment.moreAboutChanges)}
        </SelfEmployedChange>
        }
      case _ => NodeSeq.Empty
    }
  }

}

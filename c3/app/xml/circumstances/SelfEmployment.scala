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
        <SelfEmployment>
          {question(<StillCaring/>, "stillCaring.answer",circsSelfEmployment.stillCaring.answer)}
          {
            circsSelfEmployment.stillCaring.answer match {
              case "no" => {question(<StoppedCaring/>, "whenStoppedCaring",circsSelfEmployment.stillCaring.date.get.`dd/MM/yyyy`)}
              case _ =>
            }
          }
          {question(<Started/>, "whenThisSelfEmploymentStarted",circsSelfEmployment.whenThisSelfEmploymentStarted.`dd/MM/yyyy`)}
          {question(<TypeOfBusiness/>, "typeOfBusiness",circsSelfEmployment.typeOfBusiness)}
          {question(<TotalOverWeekly/>, "totalOverWeeklyIncomeThreshold",circsSelfEmployment.totalOverWeeklyIncomeThreshold)}
        </SelfEmployment>
        }
      case _ =>
    }
  }

}

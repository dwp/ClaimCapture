package xml.circumstances

import app.CircsBreaksWhereabouts
import models.domain.{CircumstancesBreaksInCareSummary, CircumstancesBreaksInCare, CircumstancesReportChange, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._
import play.api.i18n.MMessages
import controllers.Mappings

/**
 * Created by neddakaltcheva on 4/15/14.
 */
object CircsBreaksInCare {

// TODO: Need to be updated to match the new schema structure
//def xml(circs :Claim): NodeSeq = {
//  val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())
//
//  <CareeDetails>
//  {question(<FullName/>, "theirFullName", reportChange.theirFullName)}
//  {question(<RelationToClaimant/>,"theirRelationshipToYou", reportChange.theirRelationshipToYou)}
//  </CareeDetails>
//  }
//
//  def breaksFromCaring(circs: Claim): NodeSeq = {
//    val breaksFromCaringOption = circs.questionGroup[CircumstancesBreaksInCare]
//
//    var buffer = NodeSeq.Empty
//
//    breaksFromCaringOption match {
//
//      case Some(breaksFromCaring) => {
//
//        buffer ++=  textSeparatorLine(Messages("c2.g7"))
//
//        buffer ++= textLine(Messages("breaksInCareStartDate") + " = " + breaksFromCaring.breaksInCareStartDate.`dd/MM/yyyy`)
//
//        if (breaksFromCaring.breaksInCareStartTime.isDefined) buffer ++= textLine(Messages("breaksInCareStartTime") + " = " + breaksFromCaring.breaksInCareStartTime.get)
//
//        if(breaksFromCaring.wherePersonBreaksInCare.answer == CircsBreaksWhereabouts.SomewhereElse){
//          buffer ++= textLine(Messages("wherePersonBreaksInCare.answer") + " Somewhere else = " +breaksFromCaring.wherePersonBreaksInCare.text.get)
//        } else {
//          buffer ++= textLine(Messages("wherePersonBreaksInCare.answer") + " = " + breaksFromCaring.wherePersonBreaksInCare.answer.replace("_", " "))
//        }
//
//        if(breaksFromCaring.whereYouBreaksInCare.answer == CircsBreaksWhereabouts.SomewhereElse){
//          buffer ++= textLine(Messages("whereYouBreaksInCare.answer") + " Somewhere else = " + breaksFromCaring.whereYouBreaksInCare.text.get)
//        } else {
//          buffer ++= textLine(Messages("whereYouBreaksInCare.answer") + " = " + breaksFromCaring.whereYouBreaksInCare.answer.replace("_", " "))
//        }
//
//        buffer ++= textLine(Messages("breakEnded.answer") + " = " + breaksFromCaring.breakEnded.answer)
//
//        breaksFromCaring.breakEnded.answer match {
//          case Mappings.yes => {
//            buffer ++= textLine(Messages("breakEnded.endDate") + " = " + breaksFromCaring.breakEnded.date.get.`dd/MM/yyyy`)
//            if (breaksFromCaring.breakEnded.time.isDefined) buffer ++= textLine(Messages("breakEnded_endTime") + " = " + breaksFromCaring.breakEnded.time.get)
//          }
//          case Mappings.no => {
//            val expectStartCaringAnswer = if(breaksFromCaring.expectStartCaring.answer.get == Mappings.dontknow) "Don't Know" else breaksFromCaring.expectStartCaring.answer.get
//            buffer ++= textLine(Messages("expectStartCaring.answer") + " = " + expectStartCaringAnswer)
//          }
//        }
//
//        breaksFromCaring.expectStartCaring.answer match {
//          case Some(n) => n match {
//            case Mappings.yes => if (breaksFromCaring.expectStartCaring.expectStartCaringDate.isDefined) buffer ++= textLine(Messages("expectStartCaring_expectStartCaringDate") + " = " + breaksFromCaring.expectStartCaring.expectStartCaringDate.get.`dd/MM/yyyy`)
//            case Mappings.no => buffer ++= textLine(Messages("expectStartCaring_permanentBreakDate") + " = " + breaksFromCaring.expectStartCaring.permanentBreakDate.get.`dd/MM/yyyy`)
//            case _ =>
//          }
//          case _ =>
//        }
//
//        buffer ++= textLine(Messages("medicalCareDuringBreak") + " = " + breaksFromCaring.medicalCareDuringBreak)
//
//        if (breaksFromCaring.moreAboutChanges.isDefined) buffer ++= textLine(Messages("moreAboutChanges") + " = " + breaksFromCaring.moreAboutChanges.get)
//      }
//      case _ =>
//    }
//    buffer
//  }
//
//  def breaksFromCaringSummary(circs: Claim): NodeSeq = {
//    val breaksFromCaringSummaryOption = circs.questionGroup[CircumstancesBreaksInCareSummary]
//    var buffer = NodeSeq.Empty
//
//    breaksFromCaringSummaryOption match {
//      case Some(breaksFromCaringSummary) => {
//        if (breaksFromCaringSummary.additionalBreaks.answer == Mappings.yes) buffer ++= textLine(Messages("additionalBreaks.label") + "yes = " + breaksFromCaringSummary.additionalBreaks.text.get)
//        else buffer ++= textLine(Messages("additionalBreaks.label") + " = " + breaksFromCaringSummary.additionalBreaks.answer)
//      }
//      case _ =>
//    }
//    buffer
//  }

}
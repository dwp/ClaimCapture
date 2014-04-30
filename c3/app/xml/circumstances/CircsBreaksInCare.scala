package xml.circumstances

import models.domain.{CircumstancesBreaksInCareSummary, CircumstancesBreaksInCare, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * Created by neddakaltcheva on 4/15/14.
 */
object CircsBreaksInCare {

  def xml(circs: Claim): NodeSeq = {
    val breaksFromCaringOption:Option[CircumstancesBreaksInCare] = circs.questionGroup[CircumstancesBreaksInCare]
    val breaksFromCaringSummaryOption:Option[CircumstancesBreaksInCareSummary] = circs.questionGroup[CircumstancesBreaksInCareSummary]

    breaksFromCaringOption match {
      case Some(breaksFromCaring) => {

        <BreakFromCaring>
          {question(<RecentBreakStartDate/>,"breaksInCareStartDate", breaksFromCaring.breaksInCareStartDate)}
          {question(<RecentBreakStartTime/>,"breaksInCareStartTime", breaksFromCaring.breaksInCareStartTime)}
          {questionOther(<WherePersonYouCare/>,"wherePersonBreaksInCare.answer", breaksFromCaring.wherePersonBreaksInCare.answer,breaksFromCaring.wherePersonBreaksInCare.text)}
          {questionOther(<WhereWereYou/>,"whereYouBreaksInCare.answer", breaksFromCaring.whereYouBreaksInCare.answer,breaksFromCaring.whereYouBreaksInCare.text)}
          <BreakFromCaringEnded>
            {question(<HasBreakFromCaringEnded/>,"breakEnded.answer", breaksFromCaring.breakEnded.answer)}
            {question(<EndDate/>,"breakEnded.endDate", breaksFromCaring.breakEnded.date)}
            {question(<EndTime/>,"breakEnded_endTime", breaksFromCaring.breakEnded.time)}
            {question(<ExpectStartCaringAgain/>,"expectStartCaring.answer", breaksFromCaring.expectStartCaring.answer)}
            {question(<ExpectStartCaringAgainDate/>,"expectStartCaring_expectStartCaringDate", breaksFromCaring.expectStartCaring.expectStartCaringDate)}
            {question(<PermanentBreakDate/>,"expectStartCaring_permanentBreakDate", breaksFromCaring.expectStartCaring.permanentBreakDate)}
          </BreakFromCaringEnded>
          {question(<MedicalTreatmentDuringBreak/>,"medicalCareDuringBreak", breaksFromCaring.medicalCareDuringBreak)}
          {question(<MoreChanges/>,"moreAboutChanges", breaksFromCaring.moreAboutChanges)}
          {breaksFromCaringSummaryOption match {
              case Some(breaksFromCaringSummary) => {
                {question(<AdditionalBreaksNotReported/>,"additionalBreaks.label", breaksFromCaringSummary.additionalBreaks.answer)} ++
                {question(<AdditionalBreaksNotReportedDesc/>,"additionalBreaks.text.label", breaksFromCaringSummary.additionalBreaks.text)}
              }
              case _ => NodeSeq.Empty
            }
          }
        </BreakFromCaring>
      }
      case _ => NodeSeq.Empty
    }
  }
}

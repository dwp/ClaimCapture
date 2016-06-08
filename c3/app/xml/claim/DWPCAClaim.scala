package xml.claim

import app.XMLValues._
import play.api.Logger
import models.domain._
import xml.XMLHelper._
import xml._
import scala.language.postfixOps
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat
import models.DayMonthYear
import play.api.i18n.Lang
import models.domain.Claim
import scala.xml.NodeSeq

object DWPCAClaim extends XMLComponent {

  def xml(claim: Claim) = {

    val courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no))
    val claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`)
    val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
    val havePartner = yourPartnerPersonalDetails.hadPartnerSinceClaimDate
    val qualifyingBenefit = claim.questionGroup[Benefits].getOrElse(Benefits())

    Logger.info(s"Build XML for: ${claim.key} ${claim.uuid}.")

    <DWPCAClaim>
      {question(<DateOfClaim/>, "dateOfClaim",claim.dateOfClaim)}
      {question(<QualifyingBenefit/>, "benefitsAnswer",qualifyingBenefit.benefitsAnswer)}
      {Claimant.xml(claim)}
      {Caree.xml(claim)}
      {Residency.xml(claim)}
      {question(<CourseOfEducation/>, "beenInEducationSinceClaimDate.label",courseDetails.beenInEducationSinceClaimDate, claimDate)}
      {FullTimeEducation.xml(claim)}
      {Incomes.xml(claim)}
      {question(<HavePartner/>,"hadPartnerSinceClaimDate",havePartner,claimDate)}
      {Partner.xml(claim)}
      {OtherBenefits.xml(claim)}
      {Payment.xml(claim)}
      {OtherInformation.xml(claim)}
      {Declaration.xml(claim)}
      {Disclaimer.xml(claim)}
      {EvidenceList.buildXml(claim)}
      {Consents.xml(claim)}
      {AssistedDecision.xml(claim)}
    </DWPCAClaim>

  }

  def displayClaimDate (date:DayMonthYear) = {
     displayPlaybackDatesFormat(Lang("en"), date)
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    Residency.fromXml(xml,
      Payment.fromXml(xml,
        Partner.fromXml(xml,
          OtherBenefits.fromXml(xml,
            FullTimeEducation.fromXml(xml,
              Incomes.fromXml(xml,
                Disclaimer.fromXml(xml,
                  Declaration.fromXml(xml,
                    Consents.fromXml(xml,
                      Claimant.fromXml(xml,
                        Caree.fromXml(xml,
                          AssistedDecision.fromXml(xml, claim)
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  }
}

package controllers

import models.domain.Claim
import gov.dwp.carers.xml.validation.XmlValidatorFactory
import models.view.CachedClaim
import xml.circumstances.DWPCoCircs
import xml.claim.DWPCAClaim

package object submission {
  val FULL_CLAIM = 1
  val CHANGE_CIRCUMSTANCES = 2
  type TransactionID = String

  def xmlGenerator(claim: Claim) = claim.key match {
    case CachedClaim.key => DWPCAClaim.xml(claim)
    case _ => DWPCoCircs.xml(claim)
  }

  def xmlValidator(claim: Claim) = XmlValidatorFactory.buildCaFutureValidator()

  def claimType(claim:Claim) = claim.key match {
    case CachedClaim.key => FULL_CLAIM
    case _ => CHANGE_CIRCUMSTANCES
  }
}

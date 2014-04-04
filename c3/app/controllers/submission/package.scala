package controllers

import models.domain.{ChangeOfCircs, FullClaim, Claim}
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import xml.circumstances.DWPCoCircs
import xml.claim.DWPCAClaim

package object submission {
  val FULL_CLAIM = 1
  val CHANGE_CIRCUMSTANCES = 2
  type TransactionID = String

  def xmlGenerator(claim: Claim) = claim match {
    case _: Claim with FullClaim => DWPCAClaim.xml(claim)
    case _: Claim with ChangeOfCircs => DWPCoCircs.xml(claim)
  }

  def xmlValidator(claim: Claim) = claim match {
    case _: Claim with FullClaim => XmlValidatorFactory.buildCaFutureValidator()
    case _: Claim with ChangeOfCircs => XmlValidatorFactory.buildCocFutureValidator()
  }

  def claimType(claim:Claim) = claim match {
    case _: Claim with FullClaim => FULL_CLAIM
    case _: Claim with ChangeOfCircs => CHANGE_CIRCUMSTANCES
  }
}
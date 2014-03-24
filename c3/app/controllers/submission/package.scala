package controllers

import models.domain.{ChangeOfCircs, FullClaim, Claim}
import com.dwp.carers.s2.xml.validation.{XmlValidator, XmlValidatorFactory}
import scala.xml.Elem
import xml.claim.DWPCAClaim
import xml.circumstances.DWPCoCircs

package object submission {
  val FULL_CLAIM = 1
  val CHANGE_CIRCUMSTANCES = 2

  def xmlGenerator(claim: Claim, txnID: Option[String] = None) = claim match {
    case _: Claim with FullClaim => DWPCAClaim.xml(claim, txnID)
    case _: Claim with ChangeOfCircs => DWPCoCircs.xml(claim, txnID)
  }

  def xmlValidator(claim: Claim) = claim match {
    case _: Claim with FullClaim => XmlValidatorFactory.buildCaLegacyValidator()
    case _: Claim with ChangeOfCircs => XmlValidatorFactory.buildCocLegacyValidator()
  }

  def claimType(claim:Claim) = claim match {
    case _: Claim with FullClaim => FULL_CLAIM
    case _: Claim with ChangeOfCircs => CHANGE_CIRCUMSTANCES
  }
}
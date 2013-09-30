package controllers

import models.domain.{ChangeOfCircs, FullClaim, Claim}
import xml.{DWPCoCircs, DWPCAClaim}
import com.dwp.carers.s2.xml.validation.{XmlValidator, XmlValidatorFactory}
import scala.xml.Elem

package object submission {
  type TransactionID = String

  def xml(claim: Claim, txnID: TransactionID) = claim match {
    case _: Claim with FullClaim => DWPCAClaim.xml(claim, txnID)
    case _: Claim with ChangeOfCircs => DWPCoCircs.xml(claim, txnID)
  }

  def xmlValidator(claim: Claim) = claim match {
    case _: Claim with FullClaim => XmlValidatorFactory.buildCaValidator()
    case _: Claim with ChangeOfCircs => XmlValidatorFactory.buildCocValidator()
  }

  def xmlAndValidator(claim: Claim, txnID: TransactionID): (Elem, XmlValidator) = claim match {
    case _: Claim with FullClaim => (DWPCAClaim.xml(claim, txnID), XmlValidatorFactory.buildCaValidator())
    case _: Claim with ChangeOfCircs => (DWPCoCircs.xml(claim, txnID), XmlValidatorFactory.buildCocValidator())
  }
}
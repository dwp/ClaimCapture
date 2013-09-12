package models.view

import models.domain.Claim


object  CachedClaim {
  val key = "claim"
}
/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
trait CachedClaim  extends CachedDigitalForm {

  def timeoutUrl = "/timeout"

  def buildForm = new Claim()()

  def cacheKey = CachedClaim.key

}

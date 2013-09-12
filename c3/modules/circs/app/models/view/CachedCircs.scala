package models.view

import models.domain.Circs

object CachedCircs {
  val key =  "claim"
}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
trait CachedCircs extends CachedDigitalForm {

  def timeoutUrl = "/circs-timeout"

  def buildForm = new Circs()()

  def cacheKey = CachedCircs.key

}

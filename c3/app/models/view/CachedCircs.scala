package models.view

import models.domain.Circs

object CachedCircs {
  val key = "claim"
}

trait CachedCircs extends CachedDigitalForm {

  def timeoutUrl = "/circs-timeout"

  def buildForm = new Circs()

  def cacheKey = CachedCircs.key
}
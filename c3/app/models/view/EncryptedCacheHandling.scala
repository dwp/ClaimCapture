package models.view

import app.ConfigProperties._
import models.domain.Claim
import play.api.mvc.{AnyContent, Request}
import utils.ClaimEncryption

trait EncryptedCacheHandling extends CacheHandling {

  override def fromCache(request: Request[AnyContent], required: Boolean = true): Option[Claim] = {
    getProperty("cacheEncryptionEnabled", false) match {
      case true => super.fromCache(request) match {
        case Some(claim) => Some(ClaimEncryption.decrypt(claim))
        case None => None
      }
      case false => super.fromCache(request)
    }
  }

  override def saveInCache(claim: Claim): Unit = {
    getProperty("cacheEncryptionEnabled", false) match {
      case true => super.saveInCache(ClaimEncryption.encrypt(claim))
      case false => super.saveInCache(claim)
    }
  }

}

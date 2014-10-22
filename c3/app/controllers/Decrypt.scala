package controllers

import play.api.mvc._
import play.api.Logger
import services.EncryptionService

object Decrypt extends Controller with EncryptionService{
  /**
   * Decrypt a value previously encrypted by the c3 EncryptionService
   *
   * @param encryptedValue - the value to decrypt
   * @return JsValue - the user
   */
  def decryptValue(encryptedValue: String) = Action {
    Logger.debug(s"Decrypting value = $encryptedValue")

    try {
      Ok(decrypt(encryptedValue))
    } catch {
      case e: Exception => BadRequest
    }
  }
}

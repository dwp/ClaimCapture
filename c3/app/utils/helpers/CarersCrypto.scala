package utils.helpers

import app.ConfigProperties._
import play.api.libs.Crypto

object CarersCrypto {
  val staticKey: String = "1234567890123456"

  val encrypt = getProperty("encryptFields", default = true)

  val staticSecret = getProperty("staticSecret", default = false)

  val secretKey = if (staticSecret) staticKey else getKey

  def decryptAES(v: String): String = if (encrypt) Crypto.decryptAES(v, secretKey) else v

  def encryptAES(v: String) = if (encrypt) Crypto.encryptAES(v, secretKey) else v

  def getKey: String = getProperty("application.secret", staticKey).substring(0, 16)
}
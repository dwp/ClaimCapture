package utils.helpers

import play.Configuration
import java.util.UUID

object CarersCrypto {

  def encryptAES(v: String) = {
    val encrypt = Configuration.root().getBoolean("encryptFields", false)
    val staticSecret = Configuration.root().getBoolean("staticSecret", false)
    if (encrypt) {
      if (staticSecret) {
        play.api.libs.Crypto.encryptAES(v, "1234567890123456")
      } else {
        play.api.libs.Crypto.encryptAES(v, secretKey.substring(0,16))
      }
    } else {
      v
    }
  }

  def generateKey: String = {
    UUID.randomUUID().toString
  }

  val secretKey : String = generateKey
}

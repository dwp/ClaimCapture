package utils.helpers

import javax.crypto._
import javax.crypto.spec.SecretKeySpec

import app.ConfigProperties._
import play.api.Play._
import play.api.libs.{Crypto, CryptoConfigParser, Codecs}

object CarersCrypto {
  val cryptoConfigParser = current.injector.instanceOf[CryptoConfigParser]
  val staticKey: String = "1234567890123456"
  val transformation: String = "AES"

  val encrypt = getProperty("encryptFields", default = true)

  val staticSecret = getProperty("staticSecret", default = false)

  val secretKey = if (staticSecret) staticKey else getKey

  def decryptAES(v: String): String = if (encrypt) Crypto.decryptAES(v, secretKey) else v

  def encryptAES(v: String): String = if (encrypt) encryptAES(v, secretKey) else v

  def getKey: String = getProperty("play.crypto.secret", staticKey).substring(0, 16)

  /**
   * Encrypt a String with the AES encryption standard and the supplied private key.
   *
   * The private key must have a length of 16 bytes.
   *
   * The provider used is by default this uses the platform default JSSE provider.  This can be overridden by defining
   * `application.crypto.provider` in `application.conf`.
   *
   * The transformation algorithm used is the provider specific implementation of the `AES` name.  On Oracles JDK,
   * this is `AES/ECB/PKCS5Padding`.  This algorithm is suitable for small amounts of data, typically less than 32
   * bytes, hence is useful for encrypting credit card numbers, passwords etc.  For larger blocks of data, this
   * algorithm may expose patterns and be vulnerable to repeat attacks.
   *
   * The transformation algorithm can be configured by defining `application.crypto.aes.transformation` in
   * `application.conf`.  Although any cipher transformation algorithm can be selected here, the secret key spec used
   * is always AES, so only AES transformation algorithms will work.
   *
   * @param value The String to encrypt.
   * @param privateKey The key used to encrypt.
   * @return An hexadecimal encrypted string.
   */
  def encryptAES(value: String, privateKey: String): String = {
    val raw = privateKey.getBytes("utf-8")
    val skeySpec = new SecretKeySpec(raw, "AES")
    val cipher = getCipherWithConfiguredProvider("AES")
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
    Codecs.toHexString(cipher.doFinal(value.getBytes("utf-8")))
  }

  private def getCipherWithConfiguredProvider(transformation: String): Cipher = {
    cryptoConfigParser.get.provider.map(p => Cipher.getInstance(transformation, p)).getOrElse(Cipher.getInstance(transformation))
  }

}


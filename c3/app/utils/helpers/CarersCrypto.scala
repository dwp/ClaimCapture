package utils.helpers

import java.io._
import javax.crypto._
import javax.crypto.spec.SecretKeySpec

import app.ConfigProperties._
import models.domain.Claim
import play.api.Play._
import play.api.libs.{Crypto, CryptoConfigParser, Codecs}

object CarersCrypto {
  val cryptoConfigParser = current.injector.instanceOf[CryptoConfigParser]
  val transformation: String = "AES"

  val encrypt = getBooleanProperty("encryptFields")

  val secretKey = getKey

  def decryptAES(v: String): String = if (encrypt) Crypto.decryptAES(v, secretKey) else v

  def encryptAES(v: String): String = if (encrypt) encryptAES(v, secretKey) else v

  def getKey: String = getStringProperty("play.crypto.secret").substring(0, 16)

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
    val cipher = getCipher(privateKey.getBytes("utf-8"), Cipher.ENCRYPT_MODE)
    Codecs.toHexString(cipher.doFinal(value.getBytes("utf-8")))
  }

  private def getCipher(key: Array[Byte], mode: Int): Cipher = {
    val skeySpec = new SecretKeySpec(key, "AES")
    val cipher = getCipherWithConfiguredProvider("AES")
    cipher.init(mode, skeySpec)
    cipher
  }

  def encryptClaimAES(claim: Claim, key: Array[Byte]) : Array[Byte] = {
    val cipher = getCipher(key, Cipher.ENCRYPT_MODE)
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher)
    val outputStream = new ObjectOutputStream(cipherOutputStream)
    outputStream.writeObject(claim)
    outputStream.close()
    byteArrayOutputStream.toByteArray
  }

  def decryptClaimAES(key: Array[Byte], data: Array[Byte]): Claim = {
    val cipher = getCipher(key, Cipher.DECRYPT_MODE)
    val byteArrayInputStream = new ByteArrayInputStream(data)
    val cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher)
    val objectInputStream = new ObjectInputStreamWithCustomClassLoader(cipherInputStream)
    val claim = readObject[Claim](objectInputStream)
    objectInputStream.close()
    claim
  }

  private def getCipherWithConfiguredProvider(transformation: String): Cipher = {
    cryptoConfigParser.get.provider.map(p => Cipher.getInstance(transformation, p)).getOrElse(Cipher.getInstance(transformation))
  }

  def readObject[A](objectInputStream: ObjectInputStream)(implicit tag : reflect.ClassTag[A]): A = {
    try {
      val obj = objectInputStream.readObject()
      obj match {
        case x if tag.runtimeClass.isInstance(x) => x.asInstanceOf[A]
        case _ => sys.error("Type not what was expected when reading from memcache")
      }
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
        throw ex
    }
  }

  class ObjectInputStreamWithCustomClassLoader(cipherInputStream: CipherInputStream) extends ObjectInputStream(cipherInputStream) {
    override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] = {
      try { Class.forName(desc.getName, false, getClass.getClassLoader) }
      catch { case ex: ClassNotFoundException => super.resolveClass(desc) }
    }
  }
}



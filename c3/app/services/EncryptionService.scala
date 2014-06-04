package services

import org.jasypt.digest.StandardStringDigester
import org.jasypt.salt.StringFixedSaltGenerator

object EncryptionService {
  val digester = new StandardStringDigester()
  // If this salt is not specified, a random salt will be used which will result
  // different output values being generated each time for the same input string
  val salt = new StringFixedSaltGenerator("claimsCache")
  digester.setSaltSizeBytes(4)
  digester.setSaltGenerator(salt)
}

trait EncryptionService {

  val digester = EncryptionService.digester

  /**
   * @param  value Plain string to encrypt
   * @return String - the encrypted string
   */
  def digest(value: String) = digester.digest(value)

  def matches(message: String, digest: String) = digester.matches(message, digest)

}

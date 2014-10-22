package services

import org.jasypt.util.text.BasicTextEncryptor

object EncryptionService {
  val textEncryptor = new BasicTextEncryptor()
  textEncryptor.setPassword("claimCache")
}

trait EncryptionService {

  val encryptor = EncryptionService.textEncryptor

  /**
   * @param  text Plain string to encrypt
   * @return String - the encrypted string
   */
  def encrypt(text: String): String = encryptor.encrypt(text)

  /**
   * @param encryptedText The encrypted value
   * @return String - the plain string resulting from decryption
   */
  def decrypt(encryptedText: String): String = encryptor.decrypt(encryptedText)

}

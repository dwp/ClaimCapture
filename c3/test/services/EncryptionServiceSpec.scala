package services

import org.specs2.mutable.{Tags, Specification}

class EncryptionServiceSpec extends Specification with Tags with EncryptionService {

  "EncryptionService" should {

    val plainText = "AB123456Dtest2014-01-02"

    "encrypt a value" in {
      encrypt(plainText) must not beEmpty
    }

    "decrypt a value" in {
      val encrypted = encrypt(plainText)
      val decrypted = decrypt(encrypted)

      plainText mustEqual(decrypted)
    }
  }
}
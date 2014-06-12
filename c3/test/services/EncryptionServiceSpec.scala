package services

import org.specs2.mutable.{Tags, Specification}

class EncryptionServiceSpec extends Specification with Tags with EncryptionService {

  "EncryptionService" should {

    "encrypt a password given a plain password" in {
      digest("AB123456Dtest3453-01-02") must not beEmpty
    }

    "generate the same encryption sequence for a given value" in {
      val text1 = EncryptionService.digester.digest("AB123456Dtest3453-01-02")
      val text2 = EncryptionService.digester.digest("AB123456Dtest3453-01-02")

      text1 mustEqual(text2)
    }

  }

}
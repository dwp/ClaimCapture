package services

import org.specs2.mutable.{Tags, Specification}

class EncryptionServiceSpec extends Specification with Tags {

  "PasswordService" should {

    "encrypt a password given a plain password" in {
      EncryptionService.encrypt("AB123456Dtest3453-01-02") must not beEmpty
    }

    "check that a password is valid" in {
      val check = EncryptionService.checkFingerprint("AB123456Dtest3453-01-02", "3VDaBdCDU9AN/ZbShZ5UDPPZKaM49PmX")
      check mustEqual true
    }
  }


}
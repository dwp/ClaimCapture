package utils

import java.security.MessageDigest
import java.util

import models.domain.Claim
import utils.helpers.CarersCrypto

object SaveForLaterEncryption {

  def encryptClaim(claim: Claim, key: String): Array[Byte] = {
    CarersCrypto.encryptClaimAES(claim, createKey(key))
  }

  def decryptClaim(key: String, claim: Array[Byte]): Claim = {
    CarersCrypto.decryptClaimAES(createKey(key), claim)
  }

  private def createKey(key: String) = {
    val sha = MessageDigest.getInstance("SHA-1")
    val newKey = sha.digest(key.getBytes("utf-8"))
    util.Arrays.copyOf(newKey, 32)
  }
}



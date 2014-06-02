package services

import org.jasypt.util.password.BasicPasswordEncryptor
import models.domain._
import models.domain.Claim

object EncryptionService {

  lazy val encryptor = new BasicPasswordEncryptor()

  /**
   * @param  value Plain string to encrypt
   * @return String - the encrypted string
   */
  def encrypt(value: String) = encryptor.encryptPassword(value)

//  /**
//   * @param claim The claim
//   * @return String - the encrypted claim fingerprint
//   */
//  def encryptClaimFingeprint(claim: Claim) = {
//    //carer surname + carer nino + claim date + carer postcode
//    val nino = claim.questionGroup[YourDetails].getOrElse(YourDetails()).nationalInsuranceNumber.stringify
//    val surname = claim.questionGroup[YourDetails].getOrElse(YourDetails()).surname
//    val claimDate = claim.questionGroup[ClaimDate].getOrElse(ClaimDate()).dateOfClaim.toString
//    val postcode = claim.questionGroup[ContactDetails].getOrElse(ContactDetails()).postcode.getOrElse("")
//
//    encryptor.encryptPassword(nino+surname+claimDate+postcode)
//  }

  /**
   * @param message Plain fingerprint
   * @param digest Encrypted fingerprint
   * @return Boolean - true if values match, false otherwise
   */
  def checkFingerprint(message: String, digest: String) = {
    val res = encryptor.checkPassword(message, digest)
    println("Plain : "+ message + " Encrpypted: " + digest + " Match is: "+res)
    res

  }

  /**
   * @param coc The cachedChangeOfCircs
   * @return String - the encrypted coc fingerprint
   */
  //def encryptCoC(coc: CachedChangeOfCircs) =  encryptor.encryptPassword(coc)
}

package models.domain

case class SaveForLater(claim: Array[Byte],
                        location: String,
                        remainingAuthenticationAttempts: Int,
                        status: String,
                        expiryDateTime: Long,
                        appVersion: String
                       ) {

  def update(numberOfAuthenticationAttemptLeft: Int): SaveForLater = {
    copy(remainingAuthenticationAttempts = numberOfAuthenticationAttemptLeft)
  }

  def update(newStatus: String): SaveForLater = {
    copy(status = newStatus)
  }
}


package models.domain

case class SaveForLater(claim: Object,
                        location: String,
                        remainingAuthenticationAttempts: Int,
                        status: String,
                        expiryDateTime: Long
                       )


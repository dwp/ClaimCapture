package services

trait TransactionIdService {
  /**
   * Generate a new unique ID
   */
  def generateId: String

  /**
   * Record that an ID has been used
   */
  def registerId(id: String, resultCode: Option[String])
}

/**
 * Exception thrown by UniqueTransactionId if it could not generate an id. The cause is described by the nested exception.
 * @param message  the detail message
 * @param nestedException  the cause
 */
class UnavailableTransactionIdException(message: String, nestedException: Exception)
  extends RuntimeException(message, nestedException) {}


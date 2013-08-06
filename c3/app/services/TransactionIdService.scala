package services

trait TransactionIdService {
  /**
   * Generate a new unique ID
   */
  def generateId: String

  def registerId(id: String, statusCode: String)

  def updateStatus(id: String, statusCode: String)
}

/**
 * Exception thrown by UniqueTransactionId if it could not generate an id. The cause is described by the nested exception.
 * @param message  the detail message
 * @param nestedException  the cause
 */
class UnavailableTransactionIdException(message: String, nestedException: Exception)
  extends RuntimeException(message, nestedException) {}


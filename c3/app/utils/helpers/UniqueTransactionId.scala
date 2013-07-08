package utils.helpers

import play.api.db.DB
import play.api.Play.current


/**
 * Exception thrown by UniqueTransactionId if it could not generate an id. The cause is described by the nested exception.
 * @param message  the detail message
 * @param nestedException  the cause
 */
class UnavailableTransactionIdException(message: String, nestedException: Exception)
  extends  RuntimeException(message,nestedException) {}

/**
 * Connects to PostgreSQL to get a new unique transaction id.
 * @author Jorge Migueis
 *         Date: 03/07/2013
 */
object UniqueTransactionId {

  def apply(): String = {
    DB.withConnection("carers") {
      connection =>
        try {
          val statement = connection.prepareCall("select get_new_transaction_id();")
          statement.execute()
          val result = statement getResultSet()
          result.next
          result.getString("get_new_transaction_id")
        }
        catch {
          case e: java.lang.Exception => throw new UnavailableTransactionIdException("Cannot generate an unique transaction ID.", e)
        }
    }
  }

}



package utils.helpers

import play.api.db.DB
import play.api.Play.current

/**
 * Connects to PostgreSQL to get a new unique transaction id.
 * @author Jorge Migueis
 *         Date: 03/07/2013
 */
object TransactionId {

  def getUniqueTransactionIt(): String = {
    DB.withConnection("carers") { connection =>
      val statement = connection.prepareCall("select get_new_transaction_id();")
      statement.execute()
      val result = statement.getResultSet()
      result.next
      result.getString("get_new_transaction_id")
    }
  }

}

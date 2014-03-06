package services

import play.api.db.DB
import play.api.Play.current

trait ClaimTransactionComponent {
  val claimTransaction : ClaimTransaction

  class ClaimTransaction {
    /**
     * Generate a new unique ID
     */
    def generateId: String = {
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

    /**
     * Record that an ID has been used
     */
    def registerId(id: String, statusCode:String, claimType:Int) {
      DB.withConnection("carers") {
        connection =>
          val insertSql: String = "INSERT INTO transactionstatus (transaction_id, status,type) VALUES (?,?,?);"
          val statement = connection.prepareStatement(insertSql)
          statement.setString(1, id)
          statement.setString(2, statusCode)
          statement.setInt(3,claimType)
          statement.execute()
      }
    }

    def updateStatus(id: String, statusCode:String, claimType:Int) {
      DB.withConnection("carers") {
        connection =>
          val insertSql: String = "UPDATE transactionstatus set status=?, type=? WHERE transaction_id=?;"
          val statement = connection.prepareStatement(insertSql)
          statement.setString(1, statusCode)
          statement.setInt(2,claimType)
          statement.setString(3, id)
          statement.executeUpdate()
      }
    }

    var id: String = _
  }
}

/**
 * Exception thrown by UniqueTransactionId if it could not generate an id. The cause is described by the nested exception.
 * @param message  the detail message
 * @param nestedException  the cause
 */
class UnavailableTransactionIdException(message: String, nestedException: Exception)
  extends RuntimeException(message, nestedException) {}

package services.submission

import services.TransactionIdService

/**
 * Occasionally used class for injecting when testing submit.
 */
class StaticTransactionIdService() extends TransactionIdService {
   var id:String = "TEST232"

  def generateId: String = {
    id
  }

  def registerId(id: String, statusCode: String, claimType:Int) {}

  def updateStatus(id: String, statusCode: String, claimType:Int) {}
}

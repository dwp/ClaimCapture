package submission

import services.TransactionIdService

class MockTransactionIdService() extends TransactionIdService {
   var id:String = ""

  def generateId: String = {
    id
  }

  def registerId(id: String, statusCode: String) {}

  def updateStatus(id: String, statusCode: String) {}
}

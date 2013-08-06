package services.submission

import services.TransactionIdService

class MockTransactionIdService() extends TransactionIdService {

  def generateId: String = {
    "TEST223"
  }

  def registerId(id: String, statusCode: String) {}

  def updateStatus(id: String, statusCode: String) {}
}

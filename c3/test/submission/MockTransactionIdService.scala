package submission

import services.TransactionIdService
import play.api.Logger

class MockTransactionIdService() extends TransactionIdService {
   var id:String = ""

  def generateId: String = {
    id
  }

  def registerId(id: String, statusCode: String, claimType:Int) { Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")}

  def updateStatus(id: String, statusCode: String, claimType:Int) {Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")}
}

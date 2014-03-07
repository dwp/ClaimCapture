package services

trait TransactionIdService {
  var id:String // To assist testing the mock
  /**
   * Generate a new unique ID
   */
  def generateId: String

  def registerId(id: String, statusCode: String, claimType:Int)

  def updateStatus(id: String, statusCode: String, claimType:Int)
}

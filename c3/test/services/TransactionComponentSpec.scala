package services

import org.specs2.mutable.{Tags, Specification}
import org.specs2.execute.AsResult

class TransactionComponentSpec extends Specification with Tags {

  "Transaction component" should {
    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction()
    }

    "successfully register an ID" in new WithApplicationAndDB {

      val id = DBTests.newId

      transactionComponent.claimTransaction.registerId(id,"0002",1,thirdParty = false)

      DBTests.getId(id) mustEqual Some(TransactionStatus(id,"0002",1,0))

    }

    "update an existing ID" in new WithApplicationAndDB {

      val id = DBTests.newId
      transactionComponent.claimTransaction.registerId(id,"0002",1,thirdParty = false)

      val existingId = DBTests.getId(id)

      transactionComponent.claimTransaction.updateStatus(id,"0001",0)

      val transactionStatusUpdated = DBTests.getId(id)
      transactionStatusUpdated mustNotEqual Some(existingId)
      transactionStatusUpdated mustEqual Some(TransactionStatus(id,"0001",0,0))
    }
  }

}
case class TransactionStatus(transactionID: String, status: String, typeI: Int, thirdparty: Int)



package services

import org.specs2.mutable.{Tags, Specification}
import play.api.i18n.Lang

class TransactionComponentSpec extends Specification with Tags {

  "Transaction component" should {
    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction()
    }

    "select a transaction status that does not exist in its table" in new WithApplicationAndDB {
      val id = "1234678"

      transactionComponent.claimTransaction.getTransactionStatusById(id) mustEqual None
    }

    "successfully register an ID" in new WithApplicationAndDB {

      val id = DBTests.newId

      transactionComponent.claimTransaction.registerId(id, "0002", 1, 1)
      transactionComponent.claimTransaction.recordMi(id, thirdParty = false, circsChange = Some(1), Some(Lang("en")))

      transactionComponent.claimTransaction.getTransactionStatusById(id) mustEqual Some(TransactionStatus(id, "0002", 1, Some(0), Some(1), Some("en")))

    }

    "update an existing ID" in new WithApplicationAndDB {

      val id = DBTests.newId
      transactionComponent.claimTransaction.registerId(id, "0002", 1, 1)
      transactionComponent.claimTransaction.recordMi(id, thirdParty = false, None, Some(Lang("en")))

      val existingId = transactionComponent.claimTransaction.getTransactionStatusById(id)

      transactionComponent.claimTransaction.updateStatus(id, "0001", 0)

      val transactionStatusUpdated = transactionComponent.claimTransaction.getTransactionStatusById(id)
      transactionStatusUpdated mustNotEqual Some(existingId)
      transactionStatusUpdated mustEqual Some(TransactionStatus(id, "0001", 0, Some(0), None, Some("en")))
    }

  }

}


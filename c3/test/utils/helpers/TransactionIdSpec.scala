package utils.helpers

import org.specs2.mutable.Specification
import play.api.test.{FakeApplication, WithApplication}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 03/07/2013
 */
class TransactionIdSpec extends Specification {

  "The transaction id" should {

    "Should be 7 characters long" in new WithApplication(FakeApplication(additionalConfiguration=Map("db.carers.driver" ->"org.postgresql.Driver",
      "db.carers.url" -> """jdbc:postgresql://localhost:5432/carerstransactions_db""",
       "db.carers.user" -> "carers_c3",
      "db.carers.password" -> "claimant23"))) {
      TransactionId.getUniqueTransactionIt() must have size(7)
    }

  }

}

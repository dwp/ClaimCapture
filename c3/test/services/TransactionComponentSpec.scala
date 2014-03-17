package services

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithApplication
import play.api.test.Helpers._
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import anorm.~
import play.api.test.FakeApplication

class TransactionComponentSpec extends Specification with Tags {

  "Transaction component" should {
    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction()
    }

    "successfully register an ID" in new WithApplicationAndDB{

      DBTests.prepareDDL

      val id = DBTests.newId

      transactionComponent.claimTransaction.registerId(id,"0002",1,thirdParty = false)

      val transactionStatus = DBTests.getId(id)

      transactionStatus mustEqual Some(TransactionStatus(id,"0002",1,0))

    }
  }

}

case class TransactionStatus(transactionID: String, status: String, typeI: Int, thirdparty: Int)

object DBTests{

  def prepareDDL(implicit app: FakeApplication) = {
    DB.withConnection("carers"){implicit c =>
      SQL(
        """
        DROP TABLE  IF EXISTS transactionids CASCADE;
        CREATE TABLE transactionids (transaction_id CHARACTER(7) NOT NULL, createdon TIMESTAMP(6) DEFAULT now() NOT NULL, PRIMARY KEY (transaction_id));
        DROP TABLE IF EXISTS transactionstatus CASCADE;
        CREATE TABLE transactionstatus
            ( transaction_id CHARACTER(7) NOT NULL, createdon TIMESTAMP(6) DEFAULT now() NOT NULL, status CHARACTER VARYING(10), type INTEGER, thirdparty INTEGER, PRIMARY KEY(transaction_id), CONSTRAINT transaction_fk FOREIGN KEY(transaction_id) REFERENCES transactionids(transaction_id)
            ) ;
        """
      ).execute()
    }
  }

  def newId(implicit app: FakeApplication) = {
    val id = (for(i <- 1 to 7)yield{ "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"((32 * Math.random()).toInt + 1)})(collection.breakOut)
    DB.withConnection("carers"){implicit c =>
      SQL("INSERT INTO transactionids (transaction_id) VALUES ({id});").on("id"->id).execute()
    }
    id
  }

  val parser = {
    get[String]("transaction_id") ~
      get[String]("status") ~
      get[Int]("type") ~
      get[Int]("thirdparty") map{
      case id~status~typeI~thirdparty => TransactionStatus(id,status,typeI,thirdparty)
    }
  }
  def getId(id: String)(implicit app:FakeApplication):Option[TransactionStatus] = {
    import scala.language.postfixOps
    DB.withConnection("carers"){implicit c =>
      SQL(
        """
          SELECT transaction_id, status,type,thirdparty
          FROM transactionstatus
          WHERE transaction_id = {id}
        """
      ).on("id"->id)
      .as(parser singleOpt)
    }
  }


}


class WithApplicationAndDB extends WithApplication(app = FakeApplication(additionalConfiguration = inMemoryDatabase("carers",options=Map("MODE" -> "PostgreSQL","DB_CLOSE_DELAY"->"-1"))))


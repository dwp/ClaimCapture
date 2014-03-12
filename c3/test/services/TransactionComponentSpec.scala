package services

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithApplication}
import play.api.test.Helpers._
import play.api.db.DB
import anorm._
import anorm.SqlParser._

class TransactionComponentSpec extends Specification with Tags {

  "Transaction component" should {
    val inMemDBApp = FakeApplication(additionalConfiguration = inMemoryDatabase("carers",options=Map("MODE" -> "PostgreSQL")))
    val transactionComponent = new ClaimTransactionComponent {
      override val claimTransaction = new ClaimTransaction()
    }
    "get different transaction id's for every execution" in new WithApplication(app = inMemDBApp) {

      DB.withConnection("carers"){implicit c =>
        SQL(
          """
            CREATE OR REPLACE FUNCTION get_new_transaction_id ()  RETURNS character varying
              VOLATILE
            AS $valtech$
            DECLARE
                id VARCHAR;
                allowed VARCHAR := '23456789ABCDEFGHJKLMNPQRSTUVWXYZ';
                pos INTEGER;
            BEGIN
                -- Try to generate an unique transaction id up to 5 times
                -- If it cannot generate an unique transaction id after 5 attemps then throws exception
                <<ATTEMPS>>
                FOR attemps IN 1..5 LOOP
                    <<GENERATION>> FOR i IN 1..7
                    LOOP
                        pos := FLOOR(32 * random()) + 1 ;
                        id := concat(id, SUBSTR(allowed, pos, 1)) ;
                    END LOOP GENERATION;
                    BEGIN
                        INSERT INTO transactionids (transaction_id) VALUES (id);
                        RETURN id;
                    EXCEPTION
                        WHEN unique_violation THEN
                        -- Need to reset id so try to build a new one
                        id := '';
                        END;
                END LOOP ATTEMPS;
                RAISE EXCEPTION 'Did not manage to generate unique transaction id';
            END;
            $valtech$ LANGUAGE plpgsql
          """
        ).execute()
      }

      val id = DB.withConnection("carers"){implicit c =>

        SQL("select get_new_transaction_id();").as(scalar[Long].single)
      }

      println("ID:"+id)

    }
  }

}


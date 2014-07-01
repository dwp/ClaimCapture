DROP TABLE IF EXISTS "public"."transactionstatus" CASCADE;
CREATE TABLE transactionstatus
    ( transaction_id CHARACTER(7) NOT NULL, createdon TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL, status CHARACTER VARYING(10), type INTEGER, thirdparty INTEGER, circs_type INTEGER, lang CHARACTER VARYING(10), js_enabled INTEGER, PRIMARY KEY(transaction_id), CONSTRAINT transaction_fk FOREIGN KEY(transaction_id) REFERENCES transactionids(transaction_id)
    ) ;

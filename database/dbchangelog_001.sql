--liquibase formatted sql

--changeset jmigueis:001_1
CREATE TABLE transactionids (transaction_id CHARACTER(7) NOT NULL, createdon TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL, PRIMARY KEY (transaction_id));
--rollback DROP TABLE  IF EXISTS transactionids CASCADE;

--changeset jmigueis:001_2
CREATE TABLE transactionstatus
    ( transaction_id CHARACTER(11) NOT NULL, createdon TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL, status CHARACTER VARYING(10), PRIMARY KEY(transaction_id), CONSTRAINT transaction_fk FOREIGN KEY(transaction_id) REFERENCES transactionids(transaction_id)
    ) ;
--rollback DROP TABLE IF EXISTS transactionstatus CASCADE;

--changeset jmigueis:001_3 splitStatements:false
CREATE OR REPLACE FUNCTION "public"."get_new_transaction_id" ()  RETURNS character varying
  VOLATILE
AS $dbvis$
DECLARE
    id VARCHAR := to_char(CURRENT_DATE,'YYMM');
    allowed VARCHAR := '23456789ABCDEFGHJKLMNPQRSTUVWXYZ';
    pos INTEGER;
BEGIN
    -- Try to generate an unique transaction id up to 5 times
    -- If it cannot generate an unique transaction id after 5 attemps then throws exception
    <<ATTEMPS>>
    FOR attemps IN 1..20 LOOP
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
            RAISE INFO 'Was not unique';
            END;
    END LOOP ATTEMPS;
    RAISE EXCEPTION 'Did not manage to generate unique transaction id';
END;
$dbvis$ LANGUAGE plpgsql
--rollback DROP FUNCTION IF EXISTS get_new_transaction_id();

--changeset jmigueis:001_4
DROP OWNED by carers_c3;
DROP USER IF EXISTS carers_c3;
CREATE USER carers_c3 PASSWORD 'claimant23';
GRANT USAGE on SCHEMA PUBLIC to carers_c3;
GRANT SELECT,INSERT ON PUBLIC.transactionids to carers_c3;
GRANT SELECT,INSERT,UPDATE ON PUBLIC.transactionstatus to carers_c3; 
GRANT EXECUTE ON FUNCTION PUBLIC.get_new_transaction_id() to carers_c3;
--rollback DROP USER IF EXISTS carers_c3;

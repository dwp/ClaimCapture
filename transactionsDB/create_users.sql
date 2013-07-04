DROP OWNED by carers_c3 CASCADE; 
DROP USER IF EXISTS carers_c3;
CREATE USER carers_c3 PASSWORD 'claimant23';
GRANT USAGE on SCHEMA PUBLIC to carers_c3;
GRANT SELECT,INSERT ON PUBLIC.transactionids to carers_c3;
GRANT EXECUTE ON FUNCTION PUBLIC.get_new_transaction_id() to carers_c3;
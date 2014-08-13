DROP TABLE  IF EXISTS transactionids CASCADE;
CREATE TABLE transactionids (transaction_id CHARACTER(11) NOT NULL, createdon TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT now() NOT NULL, PRIMARY KEY (transaction_id));

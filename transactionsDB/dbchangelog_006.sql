--liquibase formatted sql

--changeset pwhitehead:006_1
CREATE SEQUENCE public.gbtransactionid START 1 MAXVALUE 1000000 CYCLE
GRANT SELECT,UPDATE ON public.gbtransactionid TO carers_c3
--rollback DROP SEQUENCE public.gbtransactionid 
    
--changeset pwhitehead:006_2
CREATE SEQUENCE public.nitransactionid START 1000001 MAXVALUE 9999999 CYCLE
GRANT SELECT,UPDATE ON public.nitransactionid TO carers_c3
--rollback DROP SEQUENCE public.nitransactionid

--changeset pwhitehead:006_3
DROP FUNCTION IF EXISTS public.get_new_transaction_id();
--rollback jmigueis:003_3

--changeset pwhitehead:006_4 splitStatements:false
CREATE OR REPLACE FUNCTION public.get_new_transaction_id(originTag VARCHAR) RETURNS VARCHAR(11)
AS $dbvis$
DECLARE
  beginning VARCHAR := to_char(CURRENT_DATE,'YYMM');
  id VARCHAR := '';
BEGIN
  IF originTag = 'GB-NIR' THEN
    id := beginning || trim(to_char(nextval('nitransactionid'), '0000000'));
  ELSE
    id := beginning || trim(to_char(nextval('gbtransactionid'), '0000000'));
  END IF;
  INSERT INTO transactionids (transaction_id) VALUES (id);
  RETURN id;
EXCEPTION
  WHEN unique_violation THEN           
    -- Need to reset id so try to build a new one
    id := begining;
    RAISE INFO 'Was not unique';
END;
$dbvis$ LANGUAGE plpgsql
--rollback DROP FUNCTION IF EXISTS get_new_transaction_id(originTag VARCHAR);


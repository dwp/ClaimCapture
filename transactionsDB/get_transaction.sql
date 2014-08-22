CREATE OR REPLACE FUNCTION "public"."get_new_transaction_id" ()  RETURNS character varying
  VOLATILE
AS $dbvis$
DECLARE
    id VARCHAR := to_char(CURRENT_DATE,'YYMM');
    allowed VARCHAR := '23456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    pos INTEGER;
    begining VARCHAR := to_char(CURRENT_DATE,'YYMM');
BEGIN
    -- Try to generate an unique transaction id up to 5 times
    -- If it cannot generate an unique transaction id after 5 attemps then throws exception
    <<ATTEMPS>>
    FOR attemps IN 1..20 LOOP
        <<GENERATION>> FOR i IN 1..7
        LOOP
            pos := FLOOR(34 * random()) + 1 ;
            id := concat(id, SUBSTR(allowed, pos, 1)) ;
        END LOOP GENERATION;
        BEGIN
            INSERT INTO transactionids (transaction_id) VALUES (id);
            RETURN id;
        EXCEPTION
            WHEN unique_violation THEN 
            -- Need to reset id so try to build a new one
            id := begining;
            RAISE INFO 'Was not unique';
            END;
    END LOOP ATTEMPS;
    RAISE EXCEPTION 'Did not manage to generate unique transaction id';
END;
$dbvis$ LANGUAGE plpgsql


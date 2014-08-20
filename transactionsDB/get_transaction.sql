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
            END;
    END LOOP ATTEMPS;
    RAISE EXCEPTION 'Did not manage to generate unique transaction id';
END;
$valtech$ LANGUAGE plpgsql


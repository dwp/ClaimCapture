--liquibase formatted sql

--changeset jmigueis:002_1
ALTER TABLE public.transactionstatus ADD COLUMN type INTEGER;
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS type;

--changeset jmigueis:002_2
ALTER TABLE public.transactionstatus ADD COLUMN thirdparty INTEGER; 
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS thirdparty;

--changeset jmigueis:002_3
ALTER TABLE public.transactionstatus ADD COLUMN circs_type INTEGER; 
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS thirdparty;

--changeset jmigueis:002_4
ALTER TABLE public.transactionstatus ADD COLUMN lang CHARACTER VARYING(10); 
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS thirdparty;

--changeset jmigueis:002_5
ALTER TABLE public.transactionstatus ADD COLUMN js_enabled INTEGER; 
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS js_enabled;

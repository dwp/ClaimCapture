--liquibase formatted sql

--changeset rdiaz:004_1
ALTER TABLE public.transactionstatus ADD COLUMN email INTEGER;
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS email;


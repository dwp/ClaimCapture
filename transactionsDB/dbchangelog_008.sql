--liquibase formatted sql

--changeset pwhitehead:008_1
ALTER TABLE public.transactionstatus ADD CONSTRAINT transactionstatus_checkorigin CHECK (origintag in ('GB', 'GB-NIR'));
--rollback ALTER TABLE public.transactionstatus DROP CONSTRAINT IF EXISTS transactionstatus_checkorigin;

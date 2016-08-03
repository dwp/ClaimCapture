--liquibase formatted sql

--changeset pwhitehead:005_1
ALTER TABLE public.transactionstatus ADD COLUMN saveforlateremail INTEGER;
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS saveforlateremail;


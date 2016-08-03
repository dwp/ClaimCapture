--liquibase formatted sql

--changeset pwhitehead:007_1
ALTER TABLE public.transactionstatus ADD COLUMN originTag varchar(6) default 'GB' not null; 
--rollback ALTER TABLE public.transactionstatus DROP COLUMN IF EXISTS originTag;


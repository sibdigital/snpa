ALTER TABLE main.search_statistic RENAME TO reg_search_statistic;

ALTER TABLE main.reg_search_statistic ADD COLUMN results character varying(255)[];

ALTER TABLE main.reg_search_statistic ADD COLUMN results_count integer;

ALTER TABLE main.reg_search_statistic ADD COLUMN proceed_time bigint;

ALTER TABLE main.reg_search_statistic ADD COLUMN status smallint;

ALTER TABLE main.reg_search_statistic ADD COLUMN comment character varying(1024);
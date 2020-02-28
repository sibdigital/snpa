CREATE TABLE main.reg_practice_view_statistic
(
    id                  bigint                NOT NULL,
    date_view           timestamp             NOT NULL,
    ip                  character varying(32),
    reg_practice_code   character varying(32) NOT NULL,
    previous            character varying(32),
    id_search_statistic bigint
);

ALTER TABLE ONLY main.reg_practice_view_statistic
    ADD CONSTRAINT reg_practice_view_statistic_pkey PRIMARY KEY (id);

CREATE SEQUENCE main.reg_practice_view_statistic_id_seq;

ALTER SEQUENCE main.reg_practice_view_statistic_id_seq OWNED BY main.reg_practice_view_statistic.id;

ALTER TABLE ONLY main.reg_practice_view_statistic
    ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_view_statistic_id_seq'::regclass);


--
-- reg_practice_rating
--


CREATE TABLE main.reg_practice_rating
(
    id                         bigint                NOT NULL,
    date_view                  timestamp             NOT NULL,
    ip                         character varying(32),
    reg_practice_code          character varying(32) NOT NULL,
    id_practice_view_statistic bigint,
    id_search_statistic        bigint,
    status                     smallint,
    comment                    character varying(1024)
);

ALTER TABLE ONLY main.reg_practice_rating
    ADD CONSTRAINT reg_practice_rating_pkey PRIMARY KEY (id);

CREATE SEQUENCE main.reg_practice_rating_id_seq;

ALTER SEQUENCE main.reg_practice_rating_id_seq OWNED BY main.reg_practice_rating.id;

ALTER TABLE ONLY main.reg_practice_rating
    ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_rating_id_seq'::regclass);

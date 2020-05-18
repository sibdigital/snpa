--
-- PostgreSQL database dump
--

-- Dumped from database version 11.3
-- Dumped by pg_dump version 11.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: main; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA main;


ALTER SCHEMA main OWNER TO postgres;

--
-- Name: get_reg_practice(text, character varying[], character varying[], character varying[]); Type: FUNCTION; Schema: main; Owner: postgres
--

CREATE or replace FUNCTION main.get_reg_practice(text_query text, payment_types character varying[], actions character varying[], life_situations character varying[], questions character varying[]) RETURNS TABLE(id bigint, name character varying, content text, code character varying, number character varying)
    LANGUAGE plpgsql
AS $$
DECLARE
    payment_type_type integer = 1;
    action_type integer = 2;
    life_situation_type integer = 3;
    question_type integer = 5;
    attributes_count integer;
BEGIN

    SELECT CNT INTO attributes_count
    FROM (
             SELECT COUNT(*) AS CNT FROM (
                                             SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                                                 MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(payment_types, payment_type_type)
                                             UNION
                                             SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                                                 MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(actions, action_type)
                                             UNION
                                             SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                                                 MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(life_situations, life_situation_type)
                                             UNION
                                             SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                                                 MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(questions, question_type)
                                         ) AS S
         ) AS S;

    IF attributes_count > 0 THEN
        RETURN QUERY
            WITH ATTR AS(
                SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                    MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(payment_types, payment_type_type)
                UNION
                SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                    MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(actions, action_type)
                UNION
                SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                    MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(life_situations, life_situation_type)
                UNION
                SELECT CODE_ATTRIBUTE, ATTRIBUTE_TYPE FROM
                    MAIN.GET_REG_PRACTICE_PARAMETER_TABLE(questions, question_type)
            ),
                 ATTR_PRACT AS(
                     SELECT CODE_PRACTICE
                     FROM MAIN.REG_PRACTICE_ATTRIBUTE AS RPA
                          --         INNER JOIN ATTR ON (RPA.CODE_ATTRIBUTE, RPA.ATTRIBUTE_TYPE) = (ATTR.CODE_ATTRIBUTE, ATTR.ATTRIBUTE_TYPE)
                     WHERE
                             (select count (*) from MAIN.REG_PRACTICE_ATTRIBUTE AS RPA_in inner join ATTR ON (RPA_in.CODE_ATTRIBUTE, RPA_in.ATTRIBUTE_TYPE) = (ATTR.CODE_ATTRIBUTE, ATTR.ATTRIBUTE_TYPE)
                              where RPA_in.code_practice = rpa.code_practice)
                             = attributes_count
                 )
            select
                RP.id,
                cast (RP.name as character varying(255)) as name,
                RP.content,
                cast (RP.code as character varying(255)) as code,
                cast (RP.number as character varying(255)) as number
            from main.reg_practice AS RP
            WHERE
              --    (MAIN.make_tsvector(RP.content) @@ to_tsquery('russian', text_query)
              --  OR MAIN.make_tsvector(RP.name) @@ to_tsquery('russian', text_query)
              --  )
                case when text_query <> '' then
                             (
                                             setweight(coalesce(RP.ts_number,''), 'A') ||
                                             setweight(coalesce(RP.ts_name,''), 'B') ||
                                             setweight(coalesce(RP.ts_parent_name,''), 'B') ||
                                             setweight(coalesce(RP.ts_content,''), 'C')
                                 ) @@ plainto_tsquery('russian',text_query)
                     else
                         true
                    end
              AND RP.CODE IN (SELECT CODE_PRACTICE FROM ATTR_PRACT)
        ;
    ELSE
        RETURN QUERY select
                         RP.id,
                         cast (RP.name as character varying(255)) as name,
                         RP.content,
                         cast (RP.code as character varying(255)) as code,
                         cast (RP.number as character varying(255)) as number
                     from main.reg_practice as RP
                     WHERE
                         /*
                         (MAIN.make_tsvector(RP.content) @@ to_tsquery('russian', text_query)
                          OR MAIN.make_tsvector(RP.name) @@ to_tsquery('russian', text_query)
                          )*/
                             (
                                             setweight(coalesce(RP.ts_number,''), 'A') ||
                                             setweight(coalesce(RP.ts_name,''), 'B') ||
                                             setweight(coalesce(RP.ts_parent_name,''), 'B') ||
                                             setweight(coalesce(RP.ts_content,''), 'C')
                                 ) @@ plainto_tsquery('russian',text_query)
        ;
    END IF;
END
$$;
ALTER FUNCTION main.get_reg_practice(text_query text, payment_types character varying[], actions character varying[], life_situations character varying[], questions character varying[]) OWNER TO postgres;

--
-- Name: get_reg_practice_parameter_table(character varying[], integer); Type: FUNCTION; Schema: main; Owner: postgres
--

CREATE FUNCTION main.get_reg_practice_parameter_table(attributes character varying[], attr_type integer) RETURNS TABLE(code_attribute character varying, attribute_type integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT cast(s.unnest as  character varying) as code_attribute,
               attr_type as attribute_type
        from(
                SELECT unnest FROM unnest( attributes )
            ) as s
    ;
END
$$;


ALTER FUNCTION main.get_reg_practice_parameter_table(attributes character varying[], attr_type integer) OWNER TO postgres;

--
-- Name: make_tsvector(text); Type: FUNCTION; Schema: main; Owner: postgres
--

CREATE FUNCTION main.make_tsvector(description text) RETURNS tsvector
    LANGUAGE plpgsql IMMUTABLE
    AS $$
BEGIN
    RETURN to_tsvector('russian', description);
END
$$;


ALTER FUNCTION main.make_tsvector(description text) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cls_action; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_action (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(32) NOT NULL,
    parent_code character varying(32) NOT NULL
);


ALTER TABLE main.cls_action OWNER TO postgres;

--
-- Name: cls_action_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.cls_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.cls_action_id_seq OWNER TO postgres;

--
-- Name: cls_action_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.cls_action_id_seq OWNED BY main.cls_action.id;


--
-- Name: cls_attribute_type; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_attribute_type (
    id bigint NOT NULL,
    name character varying(255),
    code character varying(255)
);


ALTER TABLE main.cls_attribute_type OWNER TO postgres;

--
-- Name: cls_attribute_type_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

ALTER TABLE main.cls_attribute_type ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME main.cls_attribute_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 52345234534
    CACHE 1
    CYCLE
);


--
-- Name: cls_attribute_value; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_attribute_value (
    id bigint NOT NULL,
    name character varying(255),
    code character varying(255),
    attribute_code character varying(255)
);


ALTER TABLE main.cls_attribute_value OWNER TO postgres;

--
-- Name: cls_attribute_value_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

ALTER TABLE main.cls_attribute_value ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME main.cls_attribute_value_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 13452345235
    CACHE 1
    CYCLE
);


--
-- Name: cls_life_situation; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_life_situation (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(32) NOT NULL,
    parent_code character varying(32) NOT NULL
);


ALTER TABLE main.cls_life_situation OWNER TO postgres;

--
-- Name: cls_life_situation_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.cls_life_situation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.cls_life_situation_id_seq OWNER TO postgres;

--
-- Name: cls_life_situation_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.cls_life_situation_id_seq OWNED BY main.cls_life_situation.id;


--
-- Name: cls_payment_type; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_payment_type (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(32) NOT NULL,
    parent_code character varying(32) NOT NULL
);


ALTER TABLE main.cls_payment_type OWNER TO postgres;

--
-- Name: cls_payment_type_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.cls_payment_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.cls_payment_type_id_seq OWNER TO postgres;

--
-- Name: cls_payment_type_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.cls_payment_type_id_seq OWNED BY main.cls_payment_type.id;


--
-- Name: cls_practice_practice; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_practice_practice (
    id bigint NOT NULL,
    practice1_code character varying(255),
    practice2_code character varying(255),
    type character varying(255),
    doc_type1 character varying(32),
    doc_type2 character varying(32),
    condition text
);


ALTER TABLE main.cls_practice_practice OWNER TO postgres;

--
-- Name: cls_practice_practice_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

ALTER TABLE main.cls_practice_practice ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME main.cls_practice_practice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 4567856785678
    CACHE 1
    CYCLE
);


--
-- Name: cls_question; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.cls_question (
    id bigint NOT NULL,
    content text,
    practice_code character varying(32) NOT NULL
);


ALTER TABLE main.cls_question OWNER TO postgres;

--
-- Name: reg_practice; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.reg_practice (
    id bigint NOT NULL,
    name text NOT NULL,
    content text NOT NULL,
    code character varying(32) NOT NULL,
    number character varying(1023) NOT NULL,
    date_of_document date,
    code_parent character varying(32),
    doc_type character varying(32),
    pos_num integer,
    date_start date,
    date_end date,
    parent_doc_type character varying(32),
    id_parent bigint,
    parent_name character varying(255),
    ts_content tsvector,
    ts_number tsvector,
    ts_name tsvector,
    ts_parent_name tsvector,
    files character varying[]
);


ALTER TABLE main.reg_practice OWNER TO postgres;

--
-- Name: reg_practice_attribute; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.reg_practice_attribute (
    id bigint NOT NULL,
    code_attribute character varying(32) NOT NULL,
    code_practice character varying(32) NOT NULL,
    attribute_type integer NOT NULL,
    doc_type character varying(32)
);


ALTER TABLE main.reg_practice_attribute OWNER TO postgres;

--
-- Name: reg_practice_attribute_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.reg_practice_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.reg_practice_attribute_id_seq OWNER TO postgres;

--
-- Name: reg_practice_attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.reg_practice_attribute_id_seq OWNED BY main.reg_practice_attribute.id;


--
-- Name: reg_practice_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.reg_practice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.reg_practice_id_seq OWNER TO postgres;

--
-- Name: reg_practice_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.reg_practice_id_seq OWNED BY main.reg_practice.id;


--
-- Name: reg_practice_rating; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.reg_practice_rating (
    id bigint NOT NULL,
    date_view timestamp without time zone NOT NULL,
    ip character varying(32),
    reg_practice_code character varying(32) NOT NULL,
    id_practice_view_statistic bigint,
    id_search_statistic bigint,
    status smallint,
    comment character varying(1024)
);


ALTER TABLE main.reg_practice_rating OWNER TO postgres;

--
-- Name: reg_practice_rating_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.reg_practice_rating_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.reg_practice_rating_id_seq OWNER TO postgres;

--
-- Name: reg_practice_rating_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.reg_practice_rating_id_seq OWNED BY main.reg_practice_rating.id;


--
-- Name: reg_practice_view_statistic; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.reg_practice_view_statistic (
    id bigint NOT NULL,
    date_view timestamp without time zone NOT NULL,
    ip character varying(32),
    reg_practice_code character varying(32) NOT NULL,
    previous character varying(32),
    id_search_statistic bigint
);


ALTER TABLE main.reg_practice_view_statistic OWNER TO postgres;

--
-- Name: reg_practice_view_statistic_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

CREATE SEQUENCE main.reg_practice_view_statistic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.reg_practice_view_statistic_id_seq OWNER TO postgres;

--
-- Name: reg_practice_view_statistic_id_seq; Type: SEQUENCE OWNED BY; Schema: main; Owner: postgres
--

ALTER SEQUENCE main.reg_practice_view_statistic_id_seq OWNED BY main.reg_practice_view_statistic.id;


--
-- Name: reg_search_statistic; Type: TABLE; Schema: main; Owner: postgres
--

CREATE TABLE main.reg_search_statistic (
    id bigint NOT NULL,
    search_text character varying(1024),
    search_type character varying(255),
    search_date_of_document_start date,
    search_date_of_document_end date,
    search_relevance character varying(255),
    search_sort_type character varying(255),
    action_tags character varying(255)[],
    life_situation_tags character varying(255)[],
    payment_type_tags character varying(255)[],
    search_date_time timestamp without time zone,
    results character varying(255)[],
    results_count integer,
    proceed_time bigint,
    status smallint,
    comment character varying(1024)
);


ALTER TABLE main.reg_search_statistic OWNER TO postgres;

--
-- Name: reg_search_statistic_id_seq; Type: SEQUENCE; Schema: main; Owner: postgres
--

ALTER TABLE main.reg_search_statistic ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME main.reg_search_statistic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 6456456456456
    CACHE 1
);


--
-- Name: SequelizeMeta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."SequelizeMeta" (
    name character varying(255) NOT NULL
);


ALTER TABLE public."SequelizeMeta" OWNER TO postgres;

--
-- Name: cls_action id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.cls_action ALTER COLUMN id SET DEFAULT nextval('main.cls_action_id_seq'::regclass);


--
-- Name: cls_life_situation id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.cls_life_situation ALTER COLUMN id SET DEFAULT nextval('main.cls_life_situation_id_seq'::regclass);


--
-- Name: cls_payment_type id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.cls_payment_type ALTER COLUMN id SET DEFAULT nextval('main.cls_payment_type_id_seq'::regclass);


--
-- Name: reg_practice id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.reg_practice ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_id_seq'::regclass);


--
-- Name: reg_practice_attribute id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.reg_practice_attribute ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_attribute_id_seq'::regclass);


--
-- Name: reg_practice_rating id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.reg_practice_rating ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_rating_id_seq'::regclass);


--
-- Name: reg_practice_view_statistic id; Type: DEFAULT; Schema: main; Owner: postgres
--

ALTER TABLE ONLY main.reg_practice_view_statistic ALTER COLUMN id SET DEFAULT nextval('main.reg_practice_view_statistic_id_seq'::regclass);

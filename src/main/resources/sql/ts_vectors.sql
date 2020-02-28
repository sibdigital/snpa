/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  003-0807
 * Created: 27.02.2020
 */
alter table main.reg_practice add column ts_content tsvector
;
alter table main.reg_practice add column ts_number tsvector
;
alter table main.reg_practice add column ts_name tsvector
;
alter table main.reg_practice add column ts_parent_name tsvector
;

update main.reg_practice SET ts_content = to_tsvector('russian', coalesce(content, ''))
;
update main.reg_practice SET ts_number = to_tsvector('russian', coalesce(number, ''))
;
update main.reg_practice SET ts_name = to_tsvector('russian', coalesce(name, ''))
;
update main.reg_practice SET ts_parent_name = to_tsvector('russian', coalesce(parent_name, ''))
;


CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_ts_content ON main.reg_practice
  USING gin(ts_content)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_ts_number ON main.reg_practice
  USING gin(ts_number)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_ts_name ON main.reg_practice
  USING gin(ts_name)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_ts_parent_name ON main.reg_practice
  USING gin(ts_parent_name)
;

CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_code_attribute ON main.reg_practice_attribute (code_attribute)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_code_practice ON main.reg_practice_attribute (code_practice)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_attribute_type ON main.reg_practice_attribute (attribute_type)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_doc_type ON main.reg_practice_attribute(doc_type)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_code_attribute_code_practice ON main.reg_practice_attribute (code_attribute, code_practice)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_code_attribute_doc_type ON main.reg_practice_attribute (code_attribute, doc_type)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_attribute_type_code_attribute_doc_type
    ON main.reg_practice_attribute (attribute_type, code_attribute, doc_type)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_attribute_code_practice_attribute_type ON main.reg_practice_attribute (code_practice, attribute_type)
;

CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_number ON main.reg_practice (number)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_doc_type ON main.reg_practice (doc_type)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_date_of_document ON main.reg_practice (date_of_document)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_date_start ON main.reg_practice (date_start)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_date_end ON main.reg_practice (date_end)
;
CREATE INDEX IF NOT EXISTS idx_fts_reg_practice_date_start_date_end ON main.reg_practice (date_start, date_end)
;

CREATE TABLE main.cls_question (
    id bigint NOT NULL, --автогенерируемое
    content text, -- справочник Ответы ТЧ вопросы атрибут Текст
    practice_code character varying(32) NOT NULL -- справочник Ответы атрибут Код
);

alter table main.cls_question add column parent_code varchar(32);

alter table main.cls_question add column ts_content tsvector;

CREATE INDEX IF NOT EXISTS idx_fts_cls_question_ts_content ON main.cls_question
    USING gin(ts_content);

create sequence main.cls_question_id_seq;

alter table main.cls_question alter column id set default nextval('main.cls_question_id_seq');

alter sequence main.cls_question_id_seq owned by main.cls_question.id;


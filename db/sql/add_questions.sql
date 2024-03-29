
-- Отвены вносятся в reg_practice с типом q
-- по аналогии с другими документами
-- заполняется вопросами из табличной части Вопросы справочника Ответы
CREATE TABLE main.cls_question (
    id bigint NOT NULL, --автогенерируемое
    content text, -- справочник Ответы ТЧ вопросы атрибут Текст
    practice_code character varying(32) NOT NULL -- справочник Ответы атрибут Код
);

-- в связи между документами добавляем условие связи condition
-- вносятся связи из справочник Ответы ТЧ Связанные ответы
-- в поле condition  записывается значение из справочник Ответы ТЧ Связанные ответы поле Уточняющий вопрос
--
-- в     practice1_code вносистя справочник Ответы атрибут Код
-- в     practice2_code вносистя атрибут Код из значения в ТЧ Связанные ответы поле Вариант ответа
-- в     type  -  указывает текстовое значение из  справочник Ответы ТЧ Связанные ответы поле ВариантОтвета
-- в     doc_type1 - для ответа вводится и указывется тип q
-- в     doc_type2 - для ответа вводится и указывется тип q
alter table main.cls_practice_practice add column condition text;
-- вносятся связи из справочник Ответы ТЧ Правовые основания
-- в     practice1_code вносистя справочник Ответы атрибут Код
-- в     practice2_code вносистя атрибут Код из значения в ТЧ Правовые основания поле Основание
-- в     type  -  указывает текстовое значение Основывается
-- в     doc_type1 - для ответа вводится и указывется тип q
-- в     doc_type2 - указывется тип Правового основания
-- condition не заполняется

--Атрибуты из ТЧ Тематика, ВидыВыплат вносятся в reg_practice_atribute по аналогии

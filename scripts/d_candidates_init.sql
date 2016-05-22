-- Table: public.d_candidates

-- DROP TABLE public.d_candidates;

CREATE TABLE public.d_candidates
(
  id integer,
  first_name character varying(100),
  surname character varying(100),
  party_id integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.d_candidates
  OWNER TO postgres;

-- Sequence: d_candidates_seq

CREATE SEQUENCE d_candidates_seq INCREMENT BY 1
MINVALUE 1
MAXVALUE 9999
START WITH 1

-- Initial data for d_candidates table

INSERT INTO d_candidates VALUES
(nextval('d_candidates_seq'), 'Kokulak', 'Anna', '1'),
(nextval('d_candidates_seq'), 'Diermanska', 'Maria', '2'),
(nextval('d_candidates_seq'), 'Obernhuber', 'Laurencja', '3'),
(nextval('d_candidates_seq'), 'Szłapczyńska', 'Ludwika', '3'),
(nextval('d_candidates_seq'), 'Radziemska', 'Daniela', '4'),
(nextval('d_candidates_seq'), 'Grzecza', 'Barbara', '5'),
(nextval('d_candidates_seq'), 'Garozarczyk', 'Kamila', '5'),
(nextval('d_candidates_seq'), 'Skrzeczanowska', 'Lucja', '5'),
(nextval('d_candidates_seq'), 'Cetelmann', 'Agnieszka', '5'),
(nextval('d_candidates_seq'), 'Wołgusiak', 'Luiza', '4'),
(nextval('d_candidates_seq'), 'Szlahusek', 'Halina', '3'),
(nextval('d_candidates_seq'), 'Dysput', 'Anna', '2'),
(nextval('d_candidates_seq'), 'Fortuniewicz', 'Magdalena', '1'),
(nextval('d_candidates_seq'), 'Kręcek', 'Katarzyna', '1'),
(nextval('d_candidates_seq'), 'Rządczyk', 'Anna', '1'),
(nextval('d_candidates_seq'), 'Zaleska', 'Michalina', '5'),
(nextval('d_candidates_seq'), 'Szachmowicz', 'Mieszko', '4'),
(nextval('d_candidates_seq'), 'Napieraska', 'Bożena', '3'),
(nextval('d_candidates_seq'), 'Daniłowicz', 'Sylwester', '1'),
(nextval('d_candidates_seq'), 'Nicałak', 'Damian', '2'),
(nextval('d_candidates_seq'), 'Pazdro', 'Więczysław', '3'),
(nextval('d_candidates_seq'), 'Dziembicki', 'Igor', '4'),
(nextval('d_candidates_seq'), 'Rystok', 'Marian', '5'),
(nextval('d_candidates_seq'), 'Zenobia', 'Marcin', '1'),
(nextval('d_candidates_seq'), 'Choja', 'Marcel', '2'),
(nextval('d_candidates_seq'), 'Stajniec', 'Miłosław', '3'),
(nextval('d_candidates_seq'), 'Cygron', 'Hubert', '4'),
(nextval('d_candidates_seq'), 'Dziroki', 'Roman', '5'),
(nextval('d_candidates_seq'), 'Gerschel', 'Dariusz', '1'),
(nextval('d_candidates_seq'), 'Tartakowski', 'Paweł', '2'),
(nextval('d_candidates_seq'), 'Hamprecht', 'Paweł', '3'),
(nextval('d_candidates_seq'), 'Gregór', 'Andrzej', '4'),
(nextval('d_candidates_seq'), 'Nadrożny', 'Jakub', '5'),
(nextval('d_candidates_seq'), 'Szosiak', 'Mariusz', '1'),
(nextval('d_candidates_seq'), 'Demarczik', 'Mateusz', '2'),
(nextval('d_candidates_seq'), 'Dwarniczak', 'Jan', '3'),
(nextval('d_candidates_seq'), 'Telewiak', 'Jan', '4'),
(nextval('d_candidates_seq'), 'Tutlewski', 'Jan ', '5'),
(nextval('d_candidates_seq'), 'Florczykowski', 'Paweł', '1'),
(nextval('d_candidates_seq'), 'Knebler', 'Andrzej', '2'),
(nextval('d_candidates_seq'), 'Midzalska', 'Marcin', '3'),
(nextval('d_candidates_seq'), 'Satybrych', 'Tomasz', '4'),
(nextval('d_candidates_seq'), 'Wtulich', 'Wiesław', '5'),
(nextval('d_candidates_seq'), 'Góralewicz', 'Tomasz', '1'),
(nextval('d_candidates_seq'), 'Wątrobiński', 'Paweł', '2'),
(nextval('d_candidates_seq'), 'Podławska', 'Beniamin', '3'),
(nextval('d_candidates_seq'), 'Gazdajko', 'Adam', '4'),
(nextval('d_candidates_seq'), 'Nowicki', 'Adam', '5'),
(nextval('d_candidates_seq'), 'Jabłonka', 'Adam', '1'),
(nextval('d_candidates_seq'), 'Cemian', 'DawidTymon', '2'),
(nextval('d_candidates_seq'), 'Dziatkiewicz', 'Łukasz', '3'),
(nextval('d_candidates_seq'), 'Waplak', 'Łukasz ', '4'),
(nextval('d_candidates_seq'), 'Patymowski', 'Jakub', '5'),
(nextval('d_candidates_seq'), 'Krasuski', 'Szymon', '1'),
(nextval('d_candidates_seq'), 'Zaniemojski', 'Szymon', '2'),
(nextval('d_candidates_seq'), 'Sabłońska', 'Dominik', '3'),
(nextval('d_candidates_seq'), 'Prater', 'Antoni', '4'),
(nextval('d_candidates_seq'), 'Libigocki', 'Michał', '5'),
(nextval('d_candidates_seq'), 'Zuzmak', 'Michał', '1'),
(nextval('d_candidates_seq'), 'Pieciula', 'Bartosz', '2');

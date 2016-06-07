-- EMPTY ALL DICT TABLES AND RESTART SEQUENCE
TRUNCATE d_age CASCADE;
TRUNCATE d_candidates CASCADE;
TRUNCATE d_constituencies CASCADE;
TRUNCATE d_education CASCADE;
TRUNCATE d_parties CASCADE;
TRUNCATE d_sex CASCADE;

ALTER SEQUENCE seq_d_age RESTART WITH 1;
ALTER SEQUENCE seq_d_parties RESTART WITH 1;
ALTER SEQUENCE seq_d_parties RESTART WITH 1;
ALTER SEQUENCE seq_d_candidates WITH 1;
ALTER SEQUENCE seq_d_constituencies WITH 1;

--
-- Data for Name: d_age; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_age VALUES (nextval('seq_d_age'), '18-25');
INSERT INTO d_age VALUES (nextval('seq_d_age'), '26-40');
INSERT INTO d_age VALUES (nextval('seq_d_age'), '41-60');
INSERT INTO d_age VALUES (nextval('seq_d_age'), '61-200');


--
-- Data for Name: d_parties; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_parties VALUES (nextval('seq_d_parties'), 'PO');
INSERT INTO d_parties VALUES (nextval('seq_d_parties'), 'PiS');
INSERT INTO d_parties VALUES (nextval('seq_d_parties'), 'Petru');
INSERT INTO d_parties VALUES (nextval('seq_d_parties'), 'SLD');
INSERT INTO d_parties VALUES (nextval('seq_d_parties'), 'Kukiz');


--
-- Data for Name: d_candidates; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Diermanska', 'Maria', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Obernhuber', 'Laurencja', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Szłapczyńska', 'Ludwika', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Radziemska', 'Daniela', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Grzecza', 'Barbara', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Garozarczyk', 'Kamila', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Skrzeczanowska', 'Lucja', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Cetelmann', 'Agnieszka', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Wołgusiak', 'Luiza', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Szlahusek', 'Halina', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Dysput', 'Anna', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Fortuniewicz', 'Magdalena', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Kręcek', 'Katarzyna', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Rządczyk', 'Anna', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Zaleska', 'Michalina', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Szachmowicz', 'Mieszko', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Napieraska', 'Bożena', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Daniłowicz', 'Sylwester', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Nicałak', 'Damian', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Pazdro', 'Więczysław', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Dziembicki', 'Igor', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Rystok', 'Marian', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Zenobia', 'Marcin', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Choja', 'Marcel', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Stajniec', 'Miłosław', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Cygron', 'Hubert', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Dziroki', 'Roman', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Gerschel', 'Dariusz', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Tartakowski', 'Paweł', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Hamprecht', 'Paweł', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Gregór', 'Andrzej', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Nadrożny', 'Jakub', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Szosiak', 'Mariusz', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Demarczik', 'Mateusz', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Dwarniczak', 'Jan', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Telewiak', 'Jan', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Tutlewski', 'Jan ', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Florczykowski', 'Paweł', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Knebler', 'Andrzej', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Midzalska', 'Marcin', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Satybrych', 'Tomasz', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Wtulich', 'Wiesław', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Góralewicz', 'Tomasz', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Wątrobiński', 'Paweł', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Podławska', 'Beniamin', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Gazdajko', 'Adam', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Nowicki', 'Adam', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Jabłonka', 'Adam', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Cemian', 'DawidTymon', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Dziatkiewicz', 'Łukasz', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Waplak', 'Łukasz ', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Patymowski', 'Jakub', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Krasuski', 'Szymon', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Zaniemojski', 'Szymon', 2);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Sabłońska', 'Dominik', 3);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Prater', 'Antoni', 4);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Libigocki', 'Michał', 5);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Zuzmak', 'Michał', 1);
INSERT INTO d_candidates VALUES (nextval('seq_d_candidates'), 'Pieciula', 'Bartosz', 2);


--
-- Data for Name: d_constituencies; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	1:	Legnica	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	2:	Wałbrzych	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	3:	Wrocław	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	4:	Bydgoszcz	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	5:	Toruń	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	6:	Lublin	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	7:	Chełm	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	8:	Zielona Góra	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	9:	Łódź	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	10:	Piotrków Trybunalski	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	11:	Sieradz	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	12:	Chrzanów	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	13:	Kraków	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	14:	Nowy Sącz	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	15:	Tarnów	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	16:	Płock	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	17:	Radom	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	18:	Siedlce	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	19:	Warszawa	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	20:	Warszawa	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	21:	Opole	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	22:	Krosno	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	23:	Rzeszów	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	24:	Białystok	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	25:	Gdańsk	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	26:	Gdynia	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	27:	Bielsko-Biała	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	28:	Częstochowa	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	29:	Gliwice	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	30:	Rybnik	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	31:	Katowice	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	32:	Sosnowiec	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	33:	Kielce	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	34:	Elbląg	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	35:	Olsztyn	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	36:	Kalisz	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	37:	Konin	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	38:	Piła	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	39:	Poznań	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	40:	Koszalin	');
INSERT INTO d_constituencies VALUES (nextval('seq_d_constituencies'), '	41:	Szczecin	');


--
-- Data for Name: d_education; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_education VALUES (nextval('seq_d_education'), 'PODSTAWOWE');
INSERT INTO d_education VALUES (nextval('seq_d_education'), 'ZAWODOWE');
INSERT INTO d_education VALUES (nextval('seq_d_education'), 'ŚREDNIE');
INSERT INTO d_education VALUES (nextval('seq_d_education'), 'WYŻSZE');


--
-- Data for Name: d_sex; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO d_sex VALUES (1, 'KOBIETY');
INSERT INTO d_sex VALUES (2, 'MĘŻCZYZNI');


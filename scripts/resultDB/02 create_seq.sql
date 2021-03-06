CREATE SEQUENCE seq_d_age
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999
  START 1
  CACHE 1;
ALTER TABLE seq_d_age
  OWNER TO postgres;

CREATE SEQUENCE seq_d_candidates
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9999
  START 1
  CACHE 1;
ALTER TABLE seq_d_candidates
  OWNER TO postgres;

CREATE SEQUENCE seq_d_constituencies
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9999
  START 1
  CACHE 1;
ALTER TABLE seq_d_constituencies
  OWNER TO postgres;

CREATE SEQUENCE seq_d_education
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999
  START 1
  CACHE 1;
ALTER TABLE seq_d_education
  OWNER TO postgres;

CREATE SEQUENCE seq_d_parties
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9999
  START 1
  CACHE 1;
ALTER TABLE seq_d_parties
  OWNER TO postgres;


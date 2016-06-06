--slowniki

CREATE TABLE d_age
(
  age_id integer NOT NULL,
  age_group character varying(10) NOT NULL,
  CONSTRAINT pk_d_age PRIMARY KEY (age_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_age
  OWNER TO postgres;


--
CREATE TABLE d_parties
(
  party_id integer NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_d_party PRIMARY KEY (party_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_parties
  OWNER TO postgres;


--
CREATE TABLE d_candidates
(
  candidate_id integer NOT NULL,
  name character varying(30) NOT NULL,
  surname character varying(50) NOT NULL,
  party_id integer,
  CONSTRAINT pk_cand_id PRIMARY KEY (candidate_id),
  CONSTRAINT fk_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_candidates
  OWNER TO postgres;

--
CREATE TABLE d_constituencies
(
  constituency_id integer NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT pk_d_constituency PRIMARY KEY (constituency_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_constituencies
  OWNER TO postgres;

--
CREATE TABLE d_education
(
  education_id integer NOT NULL,
  education_type character varying(40) NOT NULL,
  CONSTRAINT pk_d_edu PRIMARY KEY (education_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_education
  OWNER TO postgres;

--
CREATE TABLE d_sex
(
  sex_id integer NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_d_sex PRIMARY KEY (sex_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE d_sex
  OWNER TO postgres;



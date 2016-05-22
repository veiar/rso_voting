-- rezultaty

-- % glosow na partie w zaleznosci od grupy wiekowej
CREATE TABLE res_party_age
(
  party_id integer NOT NULL,
  age_id integer NOT NULL,
  percentage double precision NOT NULL,
  CONSTRAINT pk_res_pa PRIMARY KEY (party_id, age_id),
  CONSTRAINT fk_res_pa_age_id FOREIGN KEY (age_id)
      REFERENCES d_age (age_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_res_pa_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_age
  OWNER TO postgres;

-- % glosow na kandydatow z poszczegolnych partii
CREATE TABLE res_party_candidates
(
  party_id integer NOT NULL,
  candidate_id integer NOT NULL,
  percentage double precision NOT NULL,
  CONSTRAINT pk_res_party_candidates PRIMARY KEY (party_id, candidate_id),
  CONSTRAINT fk_res_party_cand_candidate_id FOREIGN KEY (candidate_id)
      REFERENCES d_candidates (candidate_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_res_party_cand_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_candidates
  OWNER TO postgres;

-- % glosow na partie w poszczegolnych okregach
CREATE TABLE res_party_constituency
(
  party_id integer NOT NULL,
  constituency_id integer NOT NULL,
  percentage double precision,
  CONSTRAINT pk_res_party_constituency PRIMARY KEY (party_id, constituency_id),
  CONSTRAINT fk_res_pc_constituency_id FOREIGN KEY (constituency_id)
      REFERENCES d_constituencies (constituency_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_res_pc_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_constituency
  OWNER TO postgres;

-- % glosow na partie w zaleznosci od wyksztalcenia
CREATE TABLE res_party_education
(
  party_id integer NOT NULL,
  education_id integer NOT NULL,
  percentage double precision NOT NULL,
  CONSTRAINT pk_res_pe PRIMARY KEY (party_id, education_id),
  CONSTRAINT fk_res_pe_edu_id FOREIGN KEY (education_id)
      REFERENCES d_education (education_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_res_pe_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_education
  OWNER TO postgres;


--% glosow na parie ogolem
CREATE TABLE res_party_percent
(
  party_id integer NOT NULL,
  percentage double precision NOT NULL,
  CONSTRAINT pk_res_party_percent PRIMARY KEY (party_id),
  CONSTRAINT fk_res_party_percent FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_percent
  OWNER TO postgres;


-- % glosow na partie w zaleznosci od plci
-- mialo byc res_sex_party ale sie powstrzymalem
CREATE TABLE res_party_sex
(
  party_id integer NOT NULL,
  sex_id integer NOT NULL,
  percentage double precision NOT NULL,
  CONSTRAINT pk_res_ps PRIMARY KEY (party_id, sex_id),
  CONSTRAINT fk_res_ps_party_id FOREIGN KEY (party_id)
      REFERENCES d_parties (party_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_res_ps_sex_id FOREIGN KEY (sex_id)
      REFERENCES d_sex (sex_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE res_party_sex
  OWNER TO postgres;


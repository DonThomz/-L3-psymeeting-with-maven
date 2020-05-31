------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------


------------------------------------------------------------
-- Table: patient
------------------------------------------------------------
CREATE TABLE public.patient
(
    patient_id    INT         NOT NULL,
    name          VARCHAR(50) NOT NULL,
    last_name     VARCHAR(50) NOT NULL,
    birthday      DATE,
    gender        VARCHAR(50),
    relationship  VARCHAR(50),
    discovery_way VARCHAR(50),
    CONSTRAINT patient_PK PRIMARY KEY (patient_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: jobs
------------------------------------------------------------
CREATE TABLE public.jobs
(
    jobs_id  INT         NOT NULL,
    job_name VARCHAR(50) NOT NULL,
    CONSTRAINT jobs_PK PRIMARY KEY (jobs_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: consultation
------------------------------------------------------------
CREATE TABLE public.consultation
(
    consultation_id   INT  NOT NULL,
    consultation_date DATE NOT NULL,
    price             FLOAT,
    pay_mode          VARCHAR(20),
    anxiety           BOOL NOT NULL,
    couple            BOOL NOT NULL,
    CONSTRAINT consultation_PK PRIMARY KEY (consultation_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: feedback
------------------------------------------------------------
CREATE TABLE public.feedback
(
    feedback_id     INT NOT NULL,
    indicator       INT,
    consultation_id INT NOT NULL,
    CONSTRAINT feedback_PK PRIMARY KEY (feedback_id),
    CONSTRAINT feedback_consultation_FK FOREIGN KEY (consultation_id) REFERENCES public.consultation (consultation_id),
    CONSTRAINT feedback_consultation_AK UNIQUE (consultation_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: user_app
------------------------------------------------------------
CREATE TABLE public.user_app
(
    user_id    INT         NOT NULL,
    email      VARCHAR(50) NOT NULL,
    password   VARCHAR(50) NOT NULL,
    patient_id INT         NOT NULL,
    CONSTRAINT user_app_PK PRIMARY KEY (user_id),
    CONSTRAINT user_app_patient_FK FOREIGN KEY (patient_id) REFERENCES public.patient (patient_id),
    CONSTRAINT user_app_patient_AK UNIQUE (patient_id)
) WITHOUT OIDS;
/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

------------------------------------------------------------
-- Table: keyword
------------------------------------------------------------
CREATE TABLE public.keyword
(
    keyword_id  INT           NOT NULL,
    keyword     VARCHAR(2000) NOT NULL,
    feedback_id INT           NOT NULL,
    CONSTRAINT keyword_PK PRIMARY KEY (keyword_id),
    CONSTRAINT keyword_feedback_FK FOREIGN KEY (feedback_id) REFERENCES public.feedback (feedback_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: commentary
------------------------------------------------------------
CREATE TABLE public.commentary
(
    commentary_id INT           NOT NULL,
    commentary    VARCHAR(2000) NOT NULL,
    feedback_id   INT           NOT NULL,
    CONSTRAINT commentary_PK PRIMARY KEY (commentary_id),
    CONSTRAINT commentary_feedback_FK FOREIGN KEY (feedback_id) REFERENCES public.feedback (feedback_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: posture
------------------------------------------------------------
CREATE TABLE public.posture
(
    posture_id  INT           NOT NULL,
    posture     VARCHAR(2000) NOT NULL,
    feedback_id INT           NOT NULL,
    CONSTRAINT posture_PK PRIMARY KEY (posture_id),
    CONSTRAINT posture_feedback_FK FOREIGN KEY (feedback_id) REFERENCES public.feedback (feedback_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: patientjob
------------------------------------------------------------
CREATE TABLE public.patientjob
(
    jobs_id    INT  NOT NULL,
    patient_id INT  NOT NULL,
    job_date   DATE NOT NULL,
    CONSTRAINT patientjob_PK PRIMARY KEY (jobs_id, patient_id),
    CONSTRAINT patientjob_jobs_FK FOREIGN KEY (jobs_id) REFERENCES public.jobs (jobs_id),
    CONSTRAINT patientjob_patient0_FK FOREIGN KEY (patient_id) REFERENCES public.patient (patient_id)
) WITHOUT OIDS;


------------------------------------------------------------
-- Table: consultation_carryout
------------------------------------------------------------
CREATE TABLE public.consultation_carryout
(
    patient_id      INT NOT NULL,
    consultation_id INT NOT NULL,
    CONSTRAINT consultation_carryout_PK PRIMARY KEY (patient_id, consultation_id),
    CONSTRAINT consultation_carryout_patient_FK FOREIGN KEY (patient_id) REFERENCES public.patient (patient_id),
    CONSTRAINT consultation_carryout_consultation0_FK FOREIGN KEY (consultation_id) REFERENCES public.consultation (consultation_id)
) WITHOUT OIDS;




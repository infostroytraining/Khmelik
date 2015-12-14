--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.5
-- Started on 2015-12-15 00:45:43 EET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 174 (class 3079 OID 11895)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 174
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 16404)
-- Name: log; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE log (
    "idLog" integer NOT NULL,
    name character varying,
    type character varying,
    date timestamp with time zone,
    message character varying
);


ALTER TABLE log OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16407)
-- Name: log_idLog_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "log_idLog_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "log_idLog_seq" OWNER TO postgres;

--
-- TOC entry 2042 (class 0 OID 0)
-- Dependencies: 173
-- Name: log_idLog_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "log_idLog_seq" OWNED BY log."idLog";


--
-- TOC entry 1920 (class 2604 OID 16409)
-- Name: idLog; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY log ALTER COLUMN "idLog" SET DEFAULT nextval('"log_idLog_seq"'::regclass);


--
-- TOC entry 2032 (class 0 OID 16404)
-- Dependencies: 172
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY log ("idLog", name, type, date, message) FROM stdin;
9	logEvent	info	2015-12-15 00:22:48.499+02	Begin to initialize servlet context.
10	logEvent	info	2015-12-15 00:22:49.217+02	Context parameters initialized.
11	logEvent	info	2015-12-15 00:22:49.272+02	Transaction manager initialized.
12	logEvent	info	2015-12-15 00:22:49.309+02	PostgreUserService initialized.
13	logEvent	info	2015-12-15 00:22:49.375+02	Services and validation utils initialized
14	logEvent	info	2015-12-15 00:22:49.408+02	App context initialized
15	logEvent	info	2015-12-15 00:22:49.459+02	LoginFilter initialized.
\.


--
-- TOC entry 2043 (class 0 OID 0)
-- Dependencies: 173
-- Name: log_idLog_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"log_idLog_seq"', 15, true);


--
-- TOC entry 1922 (class 2606 OID 16417)
-- Name: logsPK; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY log
    ADD CONSTRAINT "logsPK" PRIMARY KEY ("idLog");


--
-- TOC entry 2040 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-15 00:45:44 EET

--
-- PostgreSQL database dump complete
--


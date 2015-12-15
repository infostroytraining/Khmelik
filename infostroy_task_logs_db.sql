--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.5
-- Started on 2015-12-15 04:21:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 174 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2003 (class 0 OID 0)
-- Dependencies: 174
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 16410)
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
-- TOC entry 173 (class 1259 OID 16416)
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
-- TOC entry 2004 (class 0 OID 0)
-- Dependencies: 173
-- Name: log_idLog_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "log_idLog_seq" OWNED BY log."idLog";


--
-- TOC entry 1882 (class 2604 OID 16418)
-- Name: idLog; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY log ALTER COLUMN "idLog" SET DEFAULT nextval('"log_idLog_seq"'::regclass);


--
-- TOC entry 1994 (class 0 OID 16410)
-- Dependencies: 172
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY log ("idLog", name, type, date, message) FROM stdin;
75	logEvent	info	2015-12-15 03:15:18.967+02	Begin to initialize servlet context.
76	logEvent	info	2015-12-15 03:15:19.239+02	Context parameters initialized.
77	logEvent	info	2015-12-15 03:15:19.281+02	Transaction manager initialized.
78	logEvent	info	2015-12-15 03:15:19.309+02	PostgreUserService initialized.
79	logEvent	info	2015-12-15 03:15:19.336+02	Services and validation utils initialized
80	logEvent	info	2015-12-15 03:15:19.362+02	App context initialized
81	logEvent	info	2015-12-15 03:15:19.391+02	LoginFilter initialized.
82	logEvent	info	2015-12-15 03:16:49.197+02	User Koloturka@gmail.com credentials are wrong.
\.


--
-- TOC entry 2005 (class 0 OID 0)
-- Dependencies: 173
-- Name: log_idLog_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"log_idLog_seq"', 82, true);


--
-- TOC entry 1884 (class 2606 OID 16420)
-- Name: logsPK; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY log
    ADD CONSTRAINT "logsPK" PRIMARY KEY ("idLog");


--
-- TOC entry 2002 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-15 04:21:43

--
-- PostgreSQL database dump complete
--


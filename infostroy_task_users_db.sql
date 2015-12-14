--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.5
-- Started on 2015-12-15 00:45:11 EET

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
-- TOC entry 2045 (class 0 OID 0)
-- Dependencies: 174
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 16390)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    "idUser" integer NOT NULL,
    email character varying,
    name character varying,
    surname character varying,
    password character varying,
    image character varying
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16388)
-- Name: users_idUser_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "users_idUser_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "users_idUser_seq" OWNER TO postgres;

--
-- TOC entry 2046 (class 0 OID 0)
-- Dependencies: 172
-- Name: users_idUser_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "users_idUser_seq" OWNED BY users."idUser";


--
-- TOC entry 1920 (class 2604 OID 16393)
-- Name: idUser; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN "idUser" SET DEFAULT nextval('"users_idUser_seq"'::regclass);


--
-- TOC entry 2037 (class 0 OID 16390)
-- Dependencies: 173
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users ("idUser", email, name, surname, password, image) FROM stdin;
2	koloturka@mail.com	cepera	cepera	cepera	ceperacepera.jpg
6	koloturkaaaaa@gmail.com	Sergey	Khmelik	cepera	\N
7	ceperqweqweqa@fasi.com	Сергей	Клемник	qweqweqwe	\N
\.


--
-- TOC entry 2047 (class 0 OID 0)
-- Dependencies: 172
-- Name: users_idUser_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"users_idUser_seq"', 7, true);


--
-- TOC entry 1922 (class 2606 OID 16398)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY ("idUser");


--
-- TOC entry 1924 (class 2606 OID 16400)
-- Name: users_unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_unique_email UNIQUE (email);


--
-- TOC entry 1926 (class 2606 OID 16402)
-- Name: users_unique_namesurname; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_unique_namesurname UNIQUE (name, surname);


--
-- TOC entry 2044 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-15 00:45:11 EET

--
-- PostgreSQL database dump complete
--


CREATE TABLE symbol (
    symbol character varying NOT NULL,
    name character varying NOT NULL,
    ipo_year character varying(4) NOT NULL,
    sector character varying,
    industry character varying,
    summary_quote character varying,
    updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    currency character varying,
    stock_exchange character varying
);

--
-- Name: symbol symbol_pkey; Type: CONSTRAINT; Schema: public; Owner: aha
--

ALTER TABLE ONLY public.symbol
    ADD CONSTRAINT symbol_pkey PRIMARY KEY (symbol);



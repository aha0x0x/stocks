CREATE TABLE historical_quote (
    symbol character varying,
    trade_date date,
    open numeric,
    high numeric,
    low numeric,
    close numeric,
    volume bigint,
    unadjusted_volume bigint,
    change numeric,
    change_percent numeric,
    vwap numeric,
    label character varying,
    change_over_time numeric
);


--
-- Name: historical_quote_symbol_trade_date_idx; Type: INDEX;
--

CREATE UNIQUE INDEX historical_quote_symbol_trade_date_idx ON public.historical_quote USING btree (symbol, trade_date);


--
-- Name: historical_quote historical_quote_symbol_fkey; Type: FK CONSTRAINT;
--

ALTER TABLE ONLY public.historical_quote
    ADD CONSTRAINT historical_quote_symbol_fkey FOREIGN KEY (symbol) REFERENCES public.symbol(symbol);


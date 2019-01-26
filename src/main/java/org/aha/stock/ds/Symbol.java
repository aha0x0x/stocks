package org.aha.stock.ds;

import java.util.Objects;

public class Symbol {

    private final String symbol;
    private final String name;
    private final String ipoYear;
    private final String sector;
    private final String industry;
    private final String summaryQuote;

    public Symbol(final String symbol, final String name,
                  final String ipoYear, final String sector, final String industry,
                  final String summaryQuote ){
        this.symbol = symbol;
        this.name = name;
        this.ipoYear = ipoYear;
        this.sector = sector;
        this.industry = industry;
        this.summaryQuote = summaryQuote;
    }


    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getIpoYear() {
        return ipoYear;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }

    public String getSummaryQuote() {
        return summaryQuote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return ipoYear == symbol1.ipoYear &&
                symbol.equals(symbol1.symbol) &&
                name.equals(symbol1.name) &&
                Objects.equals(sector, symbol1.sector) &&
                industry.equals(symbol1.industry) &&
                summaryQuote.equals(symbol1.summaryQuote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, name, ipoYear, sector, industry, summaryQuote);
    }
}

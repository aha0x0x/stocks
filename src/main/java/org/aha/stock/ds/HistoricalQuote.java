package org.aha.stock.ds;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Historical Quote
 */
public class HistoricalQuote {

    //date,open,high,low,close,volume,unadjustedVolume,change,changePercent,vwap,label,changeOverTime
    private final String symbol;
    private final LocalDate date;
    private final BigDecimal open;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal close;
    private final Long volume;
    private final Long unAdjustedVolume;
    private final BigDecimal change;
    private final BigDecimal changePercent;
    private final BigDecimal vwap;
    private final String label;
    private final BigDecimal changeOverTime;


    public HistoricalQuote(final String symbol, final LocalDate date, final BigDecimal open,
                           final BigDecimal high, final BigDecimal low, final BigDecimal close,
                           final Long volume, final Long undAdjVol, final BigDecimal change, final BigDecimal changePercent,
                           final BigDecimal vwap, final String label, final BigDecimal changeOverTime) {
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
        this.unAdjustedVolume = undAdjVol;
        this.change = change;
        this.changePercent = changePercent;
        this.vwap = vwap;
        this.label = label;
        this.changeOverTime = changeOverTime;

    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public Long getVolume() {
        return volume;
    }

    public Long getUnAdjustedVolume() {
        return unAdjustedVolume;
    }

    public BigDecimal getChange() {
        return change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public BigDecimal getVwap() {
        return vwap;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getChangeOverTime() {
        return changeOverTime;
    }
}

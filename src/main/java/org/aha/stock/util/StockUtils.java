package org.aha.stock.util;

import org.aha.stock.*;
import org.aha.stock.ds.Symbol;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.*;
//import yahoofinance.Stock;
//import yahoofinance.YahooFinance;
//import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.*;
import java.net.URL;
import java.nio.file.*;
import java.time.*;
import java.util.*;


public class StockUtils {

    /**
     * Load stock symbols provided in csv file
     *
     * @param csv
     * @return
     * @throws IOException
     */
    public static List<Symbol> loadStockSymbols(final URL csv) throws IOException {

        final List<Symbol> symbols = new ArrayList<>();

        try (final Reader reader = new InputStreamReader(new BOMInputStream(csv.openStream()), "UTF-8");
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader())) {

            for (final CSVRecord record : parser) {
                // "Symbol","Name","LastSale","MarketCap","IPOyear","Sector","industry","Summary Quote",
                final String symbol = record.get("Symbol");
                final String name = record.get("Name");
                final String ipoYear = record.get("IPOyear");
                final String sector = record.get("Sector");
                final String industry = record.get("industry");
                final String summaryQuote = record.get("Summary Quote");
                symbols.add(new Symbol(symbol, name, ipoYear, sector, industry, summaryQuote));
            }
        }
        return symbols;
    }

    public static List<HistoricalQuote> loadStockQuotes(final String symbol, final Path csvFolder) throws IOException {

        final List<HistoricalQuote> quotes = new ArrayList<>();
        final Path csvPath = csvFolder.resolve(symbol + ".csv");
        if(!csvPath.toFile().exists()){
            System.out.println("Failed to load quotes for " + symbol);
            return quotes;
        }

        try (final Reader reader = new InputStreamReader(new BOMInputStream(csvPath.toUri().toURL().openStream()),
                "UTF-8");
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader())) {

            for (final CSVRecord record : parser) {
                // date,open,high,low,close,volume,unadjustedVolume,change,changePercent,vwap,label,changeOverTime
                final LocalDate date = isValueSet(record,"date") ? LocalDate.parse(record.get("date")): null;
                final BigDecimal open = isValueSet(record, "open") ? new BigDecimal(record.get("open")) : null;
                final BigDecimal high = isValueSet(record, "high") ? new BigDecimal(record.get("high")) : null;
                final BigDecimal low = isValueSet(record, "low") ? new BigDecimal(record.get("low")) : null;
                final BigDecimal close = isValueSet(record, "close") ? new BigDecimal(record.get("close")) : null;
                final Long volume = isValueSet(record, "volume") ? Long.valueOf(record.get("volume")) : null;
                final Long unadjVol = isValueSet(record, "unadjustedVolume") ? Long.valueOf(record.get("unadjustedVolume")): null;
                final BigDecimal change = isValueSet(record, "change") ? new BigDecimal(record.get("change")) : null;
                final BigDecimal changePercent = isValueSet(record, "changePercent") ? new BigDecimal(record.get("changePercent")) : null;
                final BigDecimal vwap = isValueSet(record, "vwap") ? new BigDecimal(record.get("vwap")) : null;
                final String label = isValueSet(record, "label") ? record.get("label") : null;
                final BigDecimal changeOverTime = isValueSet(record, "changeOverTime") ? new BigDecimal(record.get("changeOverTime")) : null;
                quotes.add(new HistoricalQuote(symbol, date, open, high, low, close, volume, unadjVol, change,
                        changePercent, vwap, label, changeOverTime));
            }
        }
        return quotes;
    }

    private static boolean isValueSet(final CSVRecord record, final String key){
            return record.isSet(key) && !StringUtils.isBlank(record.get(key));
    }


//    public static Stock getStock(String symbol) throws IOException {
//        return YahooFinance.get(symbol);
//    }
//
//
//    public static Map<String, Stock> getStocks(Collection<String> symbols) throws IOException {
//        return YahooFinance.get(symbols.toArray(new String[symbols.size()]));
//    }
//
//    public static Optional<Stock> getStock(final String symbol, Date from, Date to) throws IOException {
//        final Calendar cFrom = Calendar.getInstance();
//        cFrom.setTime(from);
//
//        final Calendar cTo = Calendar.getInstance();
//        cTo.setTime(to);
//        try {
//            return Optional.of(YahooFinance.get(symbol, cFrom, cTo, Interval.DAILY));
//        } catch (Exception e) {
//            System.out.println("Failed to get " + symbol);
//            return Optional.empty();
//        }
//
//    }
//
//    public static Map<String, Stock> getStocks(Collection<String> symbols, final int since) throws IOException {
//        final Calendar from = Calendar.getInstance();
//        final Calendar to = Calendar.getInstance();
//        from.add(Calendar.YEAR, 0 - since);
//        return YahooFinance.get(symbols.toArray(new String[symbols.size()]), from, to, Interval.DAILY);
//    }
}

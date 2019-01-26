package org.aha.stock;

import com.google.common.collect.Lists;
import org.aha.stock.ds.*;
import org.aha.stock.util.ConfigUtils;
import org.aha.stock.util.StockUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import static org.aha.stock.ds.SymbolTable.insert;

/**
 * Populate db with given stock symbols
 */
public class SymbolPopulator {

    public static void main(String[] args) throws IOException {
        populateSymbols("nasdaq.csv");
        populateSymbols("nyse.csv");
        populateSymbols("amex.csv");
    }

    private static void populateSymbols(final String symbolcsv) throws IOException {
        final URL url = SymbolPopulator.class.getClassLoader().getResource(symbolcsv);
        List<Symbol> symbols = StockUtils.loadStockSymbols(url);
        System.out.println("Read " + symbols.size() + "  from " + symbolcsv);

        final Properties appProperties = ConfigUtils.loadAppProperties();
        final int batchSize = Integer.valueOf(appProperties.getProperty("datasource.batch", "1000"));
        for (List<Symbol> batch : Lists.partition(symbols, batchSize)) {
            insert.apply(batch, ConfigUtils.getDataSource());
        }
    }
}

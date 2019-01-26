package org.aha.stock;

import org.aha.stock.ds.DbResult;
import org.aha.stock.ds.HistoricalQuoteTable;
import org.aha.stock.ds.SymbolTable;
import org.aha.stock.util.ConfigUtils;
import org.aha.stock.util.StockUtils;
import org.postgresql.ds.common.BaseDataSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class StockPopulator {
    public static void main(String[] args) throws IOException {

        DbResult<List<String>, Exception> result = SymbolTable.getAllSymbols.apply(ConfigUtils.getDataSource());
        if(result.isError()){
            System.out.println("Error:" + result.getError());
            return;
        }


        final Properties appProps = ConfigUtils.loadAppProperties();
        final String csvFolder = appProps.getProperty("stocks.populator.folder");
        System.out.println("Folder" + csvFolder);

        final List<String> symbols = result.getResult();
        final BaseDataSource ds = ConfigUtils.getDataSource();

        for(final String sym : symbols){
            try {
                final List<HistoricalQuote> quotes = StockUtils.loadStockQuotes(sym, Paths.get(csvFolder));
                HistoricalQuoteTable.insert.apply(quotes, ds);
            }catch (IOException e){
                System.out.println("Error: failed to load" + sym + " " + result.getError());
            }
        }


    }
}

package org.aha.stock;


import org.aha.stock.ds.DbResult;
import org.aha.stock.ds.SymbolTable;
import org.aha.stock.util.ConfigUtils;

import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.net.URL;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static java.lang.Thread.sleep;

/**
 * A utility to retrieve stock data and store in data files.
 */
public class StockFetcher {

    public static void main(String[] args) throws IOException {
        getHistory();
    }

    private static void getHistory() throws IOException {

        List<String> symbols = loadSymbols();
        if (symbols.isEmpty()) {
            return;
        }

        final String storage = ConfigUtils.getProperty("stocks.history.storage.format");
        final String duration = ConfigUtils.getProperty("stocks.history.duration");

        Path storagePath = Paths.get(ConfigUtils.getProperty("stocks.history.storage.path"));
        storagePath = storagePath.resolve(LocalDate.now().toString()).resolve("-" + duration);
        storagePath.toFile().mkdirs();

        for (final String symbol : symbols) {
            final URL symUrl = ConfigUtils.getStockUrl(symbol, duration, storage);

            try {
                sleep(1);
                final Path toSave = storagePath.resolve(symbol + "." + storage);
                FileUtils.copyURLToFile(symUrl, toSave.toFile());
            } catch (Exception e) {
                System.out.println("failed to retrieve " + symbol);
            }
        }
    }

    /**
     * Loads all symbols from database
     * @return
     * @throws IOException
     */
    private static List<String> loadSymbols() throws IOException {
        final DbResult<List<String>, Exception> dbResult = SymbolTable.getAllSymbols.apply(ConfigUtils.getDataSource());;
        if (dbResult.isResult()) {
            return dbResult.getResult();
        }
        System.out.println("failed to load symbols " + dbResult.getError().getMessage());
        return new ArrayList<>();
    }
}

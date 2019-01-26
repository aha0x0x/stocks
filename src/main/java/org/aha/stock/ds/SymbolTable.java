package org.aha.stock.ds;

import org.postgresql.ds.common.BaseDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Symbol Table Persistence
 */
public class SymbolTable {

    private static final String SYMBOL_TABLE = "symbol";

    private static final String SYMBOL = "symbol";
    private static final String NAME = "name";
    private static final String IPOYEAR = "ipo_year";
    private static final String SECTOR = "sector";
    private static final String INDUSTRY = "industry";
    private static final String SUMMARYQUOTE = "summary_quote";

    public static BiFunction<Collection<Symbol>, BaseDataSource, DbResult<List<Integer>, Exception>> insert =
            (final Collection<Symbol> symbols, final BaseDataSource ds) -> {
                String sql = "INSERT INTO " + SYMBOL_TABLE
                        + " ( " + SYMBOL + " , " + NAME + " , " + IPOYEAR + " , " + SECTOR + " , " + INDUSTRY + " , " + SUMMARYQUOTE + " ) "
                        + "VALUES (?,?,?,?,?,?) "
                        + "ON CONFLICT (" + SYMBOL + ") "
                        + "DO UPDATE SET "
                        + " ( " + SYMBOL + " , " + NAME + " , " + IPOYEAR + " , " + SECTOR + " , " + INDUSTRY + " , " + SUMMARYQUOTE + " ) "
                        + " = "
                        + " ( EXCLUDED." + SYMBOL + " , EXCLUDED." + NAME + " , EXCLUDED." + IPOYEAR + " , EXCLUDED." + SECTOR + " , EXCLUDED." + INDUSTRY + " , EXCLUDED." + SUMMARYQUOTE + " ) ";

                try (final Connection conn = ds.getConnection()) {
                    try (final PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        conn.setAutoCommit(false);
                        for (final Symbol symbol : symbols) {
                            pstmt.setString(1, symbol.getSymbol());
                            pstmt.setString(2, symbol.getName());
                            pstmt.setString(3, symbol.getIpoYear());
                            pstmt.setString(4, symbol.getSector());
                            pstmt.setString(5, symbol.getIndustry());
                            pstmt.setString(6, symbol.getSummaryQuote());
                            pstmt.addBatch();
                        }
                        int[] results = pstmt.executeBatch();
                        conn.commit();
                        final List<Integer> boxed = Arrays.stream(results).boxed().collect(Collectors.toList());
                        return DbResult.result(boxed);

                    } catch (SQLException e) {
                        try {
                            conn.rollback();
                        } catch (final SQLException s) {
                        }
                        return DbResult.error(e);
                    }
                } catch (final Exception e) {
                    return DbResult.error(e);
                }
            };

    /**
     * Gets the symbol from the datasource
     *
     * @param symbol symbol
     * @return Symbol if present else empty
     * @throws SQLException
     */
    public static BiFunction<String, BaseDataSource, DbResult<Optional<Symbol>, Exception>> get =
            (final String symbol, final BaseDataSource ds) -> {

                String sql = "SELECT "
                        + SYMBOL + " , " + NAME + " , " + IPOYEAR + " , " + SECTOR + " , " + INDUSTRY + " , " + SUMMARYQUOTE
                        + " FROM " + SYMBOL_TABLE
                        + " WHERE " + SYMBOL + " = ?  LIMIT 1";

                try (final Connection conn = ds.getConnection();
                     final PreparedStatement pstmt = conn.prepareStatement(sql);) {

                    pstmt.setString(1, symbol.toUpperCase());
                    final ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String sym = rs.getString(SYMBOL);
                        String name = rs.getString(NAME);
                        String ipo = rs.getString(IPOYEAR);
                        String sector = rs.getString(SECTOR);
                        String ind = rs.getString(INDUSTRY);
                        String summ = rs.getString(SUMMARYQUOTE);

                        final Symbol s = new Symbol(sym, name, ipo, sector, ind, summ);
                        return DbResult.result(Optional.of(s));
                    }
                    return DbResult.result(Optional.empty());
                } catch (final Exception e) {
                    return DbResult.error(e);
                }
            };


    /**
     * Get all symbols
     *
     * @return
     */
    public static Function<BaseDataSource, DbResult<List<Symbol>, Exception>> getAll = (final BaseDataSource ds) -> {

        final String sql = "SELECT "
                + SYMBOL + " , " + NAME + " , " + IPOYEAR + " , " + SECTOR + " , " + INDUSTRY + " , " + SUMMARYQUOTE
                + " FROM " + SYMBOL_TABLE;

        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql);) {
            final ResultSet rs = pstmt.executeQuery();

            final List<Symbol> result = new ArrayList<>();
            while (rs.next()) {
                String sym = rs.getString(SYMBOL);
                String name = rs.getString(NAME);
                String ipo = rs.getString(IPOYEAR);
                String sector = rs.getString(SECTOR);
                String ind = rs.getString(INDUSTRY);
                String summ = rs.getString(SUMMARYQUOTE);
                result.add(new Symbol(sym, name, ipo, sector, ind, summ));
            }
            return DbResult.result(result);
        } catch (final Exception e) {
            return DbResult.error(e);
        }
    };


    /**
     * Get all Symbol strings
     */
    public static Function<BaseDataSource, DbResult<List<String>, Exception>> getAllSymbols = (final BaseDataSource ds) -> {

        final String sql = "SELECT " + SYMBOL + " FROM " + SYMBOL_TABLE;

        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql);) {
            final ResultSet rs = pstmt.executeQuery();

            final List<String> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getString(SYMBOL));
            }
            return DbResult.result(result);
        } catch (final Exception e) {
            return DbResult.error(e);
        }
    };


    /**
     * Deletes the given symbol from the datasource
     *
     * @param symbol symbol
     * @return number of records deleted
     */
    public BiFunction<String, BaseDataSource, DbResult<Integer, Exception>> delete =
            (final String symbol, final BaseDataSource ds) -> {

                final String sql = "DELETE from " + SYMBOL_TABLE
                        + " WHERE " + SYMBOL + " = ? CASCADE";

                try (final Connection conn = ds.getConnection();
                     final PreparedStatement pstmt = conn.prepareStatement(sql);) {
                    pstmt.setString(1, symbol.toUpperCase());
                    int rows = pstmt.executeUpdate();
                    return DbResult.result(rows);
                } catch (Exception e) {
                    return DbResult.error(e);
                }
            };

    /**
     * Checks if the given symbol exists
     *
     * @param symbol symbol
     * @return true if symbol is found, else false
     * @throws SQLException
     */
    public static BiFunction<String, BaseDataSource, DbResult> exists = (final String symbol, final BaseDataSource ds) -> {

        String sql = "SELECT EXISTS ( SELECT 1 FROM " + SYMBOL_TABLE
                + " WHERE " + SYMBOL + " = ?  )";

        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, symbol.toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            return DbResult.result(rs.next());

        } catch (final Exception e) {
            return DbResult.error(e);
        }
    };
}

package org.aha.stock.ds;

import com.google.common.base.Joiner;
import org.aha.stock.*;
import org.postgresql.ds.common.BaseDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class HistoricalQuoteTable {

    public static String HIST_QUOTE = "historical_quote";

    private static final String SYMBOL = "symbol";
    private static final String TRADE_DATE = "trade_date";
    private static final String OPEN = "open";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String CLOSE = "close";
    private static final String VOLUME = "volume";
    private static final String UNADJUSTED_VOLUME = "unadjusted_volume";
    private static final String CHANGE = "change";
    private static final String CHANGE_PERCENT = "change_percent";
    private static final String VWAP = "vwap";
    private static final String LABEL = "label";
    private static final String CHANGE_OVER_TIME = "change_over_time";

    public static String[] columns = {SYMBOL, TRADE_DATE, OPEN, HIGH, LOW, CLOSE, VOLUME, UNADJUSTED_VOLUME, CHANGE,
            CHANGE_PERCENT, VWAP, LABEL, CHANGE_OVER_TIME};

    public static BiFunction<Collection<HistoricalQuote>, BaseDataSource, DbResult> insert =
            (final Collection<HistoricalQuote> quotes, final BaseDataSource ds) -> {

                final String sql = "INSERT INTO " + HIST_QUOTE
                        + " ( "
                        + Joiner.on(',').join(columns)
                        + " ) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";

                try (final Connection conn = ds.getConnection()) {
                    try (final PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        for (final HistoricalQuote quote : quotes) {
                            pstmt.setString(1, quote.getSymbol()); // symbol
                            pstmt.setDate(2, java.sql.Date.valueOf(quote.getDate())); // trade date
                            pstmt.setObject(3, quote.getOpen(), Types.NUMERIC); // open
                            pstmt.setObject(4, quote.getHigh(), Types.NUMERIC); // high
                            pstmt.setObject(5, quote.getLow(), Types.NUMERIC); //low
                            pstmt.setObject(6, quote.getClose(), Types.NUMERIC); // close

                            pstmt.setObject(7, quote.getClose(), Types.NUMERIC); // volume
                            pstmt.setObject(8, quote.getClose(), Types.NUMERIC); // unadj_vol
                            pstmt.setObject(9, quote.getClose(), Types.NUMERIC); // change
                            pstmt.setObject(10, quote.getClose(), Types.NUMERIC); // change_percent
                            pstmt.setObject(11, quote.getClose(), Types.NUMERIC); // vwap
                            pstmt.setObject(12, quote.getClose(), Types.NUMERIC); // label
                            pstmt.setObject(13, quote.getClose(), Types.NUMERIC); // change_over_time
                            pstmt.addBatch();
                        }
                        int[] results = pstmt.executeBatch();
                        conn.commit();

                        final List<Integer> boxed = Arrays.stream(results).boxed().collect(Collectors.toList());
                        return DbResult.result(boxed);
                    } catch (final SQLException e) {
                        try {
                            conn.rollback();
                        } catch (final SQLException sqle) {
                        }
                        return DbResult.error(e);
                    }
                } catch (SQLException e) {
                    return DbResult.error(e);
                }
            };
}

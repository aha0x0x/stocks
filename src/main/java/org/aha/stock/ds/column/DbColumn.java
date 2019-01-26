package org.aha.stock.ds.column;

import java.sql.*;

public class DbColumn {
    public final String name;
    public final JDBCType type;

    public DbColumn(final String name, final JDBCType type){
        this.name = name;
        this.type = type;
    }
}


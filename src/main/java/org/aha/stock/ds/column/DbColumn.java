package org.aha.stock.ds.column;

import java.sql.JDBCType;
import java.util.Objects;

/**
 * A class representing a database column
 */
public class DbColumn {
    public final String name;
    public final JDBCType type;

    public DbColumn(final String name, final JDBCType type){
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbColumn dbColumn = (DbColumn) o;
        return name.equals(dbColumn.name) &&
                type == dbColumn.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}

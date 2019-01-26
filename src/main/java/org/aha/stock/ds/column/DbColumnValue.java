package org.aha.stock.ds.column;

import java.util.Objects;

/**
 * A class representing a database column and value
 */
public class DbColumnValue {

    public final DbColumn column;
    public final Object value;

    public DbColumnValue(final DbColumn column, final Object value){
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbColumnValue that = (DbColumnValue) o;
        return column.equals(that.column) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, value);
    }
}

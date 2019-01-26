package org.aha.stock.ds.column;

public class DbColumnValue {
    public final DbColumn column;
    public final Object value;

    public DbColumnValue(final DbColumn column, final Object value){
        this.column = column;
        this.value = value;
    }

}

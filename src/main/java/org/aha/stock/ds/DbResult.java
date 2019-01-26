package org.aha.stock.ds;

import java.util.NoSuchElementException;

public abstract class DbResult<R,E extends Exception> {

    public abstract boolean isResult();
    public abstract  boolean isError();

    public R getResult(){
        throw new NoSuchElementException();
    }
    public E getError(){
        throw new NoSuchElementException();
    }

    public static <R,E extends Exception> DbResult<R,E> result(final R result){
        return new Result(result);
    }
    public static <R,E extends Exception> DbResult<R,E> error(final E exception){
        return new Err<>(exception);
    }


    static final class Result<R> extends  DbResult{

        final R result;

        public Result(final R result){
            this.result = result;
        }

        @Override
        public boolean isResult() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public R getResult(){
            return result;
        }

    }

    static final class Err<E extends  Exception> extends  DbResult{

        final E exception;

        public Err(final E exception){
            this.exception = exception;
        }

        @Override
        public boolean isResult() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public E getError(){
            return exception;
        }

    }
}

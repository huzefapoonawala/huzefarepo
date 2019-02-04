package com.jh.etl.common.util;

@FunctionalInterface
public interface ThrowingPredicate<T, E extends Exception> {

	boolean test(T t) throws E;
}

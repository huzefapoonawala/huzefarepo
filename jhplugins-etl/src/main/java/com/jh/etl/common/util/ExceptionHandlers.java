package com.jh.etl.common.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ExceptionHandlers {

	public static <T, E extends Exception> Consumer<T> consumerWrapper(
			Consumer<T> consumer, Class<E> clazz) {
		return i -> {
	        try {
	            consumer.accept(i);
	        } catch (Exception ex) {
	        	throw ex;
	        }
	    };
	}
	
	public static <T, E extends Exception> Consumer<T> throwingConsumerWrapper(
			ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
		return i -> {
			try {
				throwingConsumer.accept(i);
			} catch (Exception ex) {
				/*
				 * try { E exCast = exceptionClass.cast(ex); System.err.println(
				 * "Exception occured : " + exCast.getMessage()); } catch (ClassCastException
				 * ccEx) { throw new RuntimeException(ex); }
				 */
				throw new RuntimeException(ex);
			}
		};
	}
	
	public static <T, E extends Exception> Predicate<T> throwingPredicateWrapper(
			ThrowingPredicate<T, E> throwingPredicate, Class<E> exceptionClass) {
		return i -> {
			try {
				return throwingPredicate.test(i);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}


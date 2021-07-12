package com.ls.luava.data;


import java.util.Collections;
import java.util.function.Function;

public interface Page2<T> extends Slice2<T> {

	/**
	 * Creates a new empty {@link Page2}.
	 *
	 * @return
	 * @since 2.0
	 */
	static <T> Page2<T> empty() {
		return empty(Pageable2.unpaged());
	}

	/**
	 * Creates a new empty {@link Page2} for the given {@link Pageable2}.
	 *
	 * @param pageable2 must not be {@literal null}.
	 * @return
	 * @since 2.0
	 */
	static <T> Page2<T> empty(Pageable2 pageable2) {
		return new Page2Impl(Collections.emptyList(), pageable2, 0);
	}

	/**
	 * Returns the number of total pages.
	 *
	 * @return the number of total pages
	 */
	int getTotalPages();

	/**
	 * Returns the total amount of elements.
	 *
	 * @return the total amount of elements
	 */
	long getTotalElements();

	@Override
  <U> Page2<U> map(Function<? super T, ? extends U> converter);
}

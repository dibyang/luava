package com.ls.luava.data;


import java.util.Collections;
import java.util.function.Function;

public interface Page<T> extends Slice<T> {

	/**
	 * Creates a new empty {@link Page}.
	 *
	 * @return
	 * @since 2.0
	 */
	static <T> Page<T> empty() {
		return empty(Pageable.unpaged());
	}

	/**
	 * Creates a new empty {@link Page} for the given {@link Pageable}.
	 *
	 * @param pageable must not be {@literal null}.
	 * @return
	 * @since 2.0
	 */
	static <T> Page<T> empty(Pageable pageable) {
		return new PageImpl(Collections.emptyList(), pageable, 0);
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
  <U> Page<U> map(Function<? super T, ? extends U> converter);
}

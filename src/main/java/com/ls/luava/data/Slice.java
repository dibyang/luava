package com.ls.luava.data;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface Slice<T> {

  Iterator<T> iterator();

	/**
	 * Returns the number of the current {@link Slice}. Is always non-negative.
	 *
	 * @return the number of the current {@link Slice}.
	 */
	int getNumber();

	/**
	 * Returns the size of the {@link Slice}.
	 *
	 * @return the size of the {@link Slice}.
	 */
	int getSize();

	/**
	 * Returns the number of elements currently on this {@link Slice}.
	 *
	 * @return the number of elements currently on this {@link Slice}.
	 */
	int getNumberOfElements();

	/**
	 * Returns the page content as {@link List}.
	 *
	 * @return
	 */
	List<T> getContent();

	/**
	 * Returns whether the {@link Slice} has content at all.
	 *
	 * @return
	 */
	boolean hasContent();


	/**
	 * Returns whether the current {@link Slice} is the first one.
	 *
	 * @return
	 */
	boolean isFirst();

	/**
	 * Returns whether the current {@link Slice} is the last one.
	 *
	 * @return
	 */
	boolean isLast();

	/**
	 * Returns if there is a next {@link Slice}.
	 *
	 * @return if there is a next {@link Slice}.
	 */
	boolean hasNext();

	/**
	 * Returns if there is a previous {@link Slice}.
	 *
	 * @return if there is a previous {@link Slice}.
	 */
	boolean hasPrevious();

	/**
	 * Returns the {@link Pageable} that's been used to request the current {@link Slice}.
	 *
	 * @return
	 * @since 2.0
	 */
	default Pageable getPageable() {
		return PageRequest.of(getNumber(), getSize());
	}

	/**
	 * Returns the {@link Pageable} to request the next {@link Slice}. Can be {@link Pageable#unpaged()} in case the
	 * current {@link Slice} is already the last one. Clients should check {@link #hasNext()} before calling this method.
	 *
	 * @return
	 * @see #nextOrLastPageable()
	 */
	Pageable nextPageable();

	/**
	 * Returns the {@link Pageable} to request the previous {@link Slice}. Can be {@link Pageable#unpaged()} in case the
	 * current {@link Slice} is already the first one. Clients should check {@link #hasPrevious()} before calling this
	 * method.
	 *
	 * @return
	 * @see #previousPageable()
	 */
	Pageable previousPageable();

	<U> Slice<U> map(Function<? super T, ? extends U> converter);

	/**
	 * Returns the {@link Pageable} describing the next slice or the one describing the current slice in case it's the
	 * last one.
	 *
	 * @return
	 * @since 2.2
	 */
	default Pageable nextOrLastPageable() {
		return hasNext() ? nextPageable() : getPageable();
	}

	/**
	 * Returns the {@link Pageable} describing the previous slice or the one describing the current slice in case it's the
	 * first one.
	 *
	 * @return
	 * @since 2.2
	 */
	default Pageable previousOrFirstPageable() {
		return hasPrevious() ? previousPageable() : getPageable();
	}
}

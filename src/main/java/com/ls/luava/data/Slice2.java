package com.ls.luava.data;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface Slice2<T> {

  Iterator<T> iterator();

	/**
	 * Returns the number of the current {@link Slice2}. Is always non-negative.
	 *
	 * @return the number of the current {@link Slice2}.
	 */
	int getNumber();

	/**
	 * Returns the size of the {@link Slice2}.
	 *
	 * @return the size of the {@link Slice2}.
	 */
	int getSize();

	/**
	 * Returns the number of elements currently on this {@link Slice2}.
	 *
	 * @return the number of elements currently on this {@link Slice2}.
	 */
	int getNumberOfElements();

	/**
	 * Returns the page content as {@link List}.
	 *
	 * @return
	 */
	List<T> getContent();

	/**
	 * Returns whether the {@link Slice2} has content at all.
	 *
	 * @return
	 */
	boolean hasContent();


	/**
	 * Returns whether the current {@link Slice2} is the first one.
	 *
	 * @return
	 */
	boolean isFirst();

	/**
	 * Returns whether the current {@link Slice2} is the last one.
	 *
	 * @return
	 */
	boolean isLast();

	/**
	 * Returns if there is a next {@link Slice2}.
	 *
	 * @return if there is a next {@link Slice2}.
	 */
	boolean hasNext();

	/**
	 * Returns if there is a previous {@link Slice2}.
	 *
	 * @return if there is a previous {@link Slice2}.
	 */
	boolean hasPrevious();

	/**
	 * Returns the {@link Pageable2} that's been used to request the current {@link Slice2}.
	 *
	 * @return
	 * @since 2.0
	 */
	default Pageable2 getPageable() {
		return PageRequest2.of(getNumber(), getSize());
	}

	/**
	 * Returns the {@link Pageable2} to request the next {@link Slice2}. Can be {@link Pageable2#unpaged()} in case the
	 * current {@link Slice2} is already the last one. Clients should check {@link #hasNext()} before calling this method.
	 *
	 * @return
	 * @see #nextOrLastPageable()
	 */
	Pageable2 nextPageable();

	/**
	 * Returns the {@link Pageable2} to request the previous {@link Slice2}. Can be {@link Pageable2#unpaged()} in case the
	 * current {@link Slice2} is already the first one. Clients should check {@link #hasPrevious()} before calling this
	 * method.
	 *
	 * @return
	 * @see #previousPageable()
	 */
	Pageable2 previousPageable();

	<U> Slice2<U> map(Function<? super T, ? extends U> converter);

	/**
	 * Returns the {@link Pageable2} describing the next slice or the one describing the current slice in case it's the
	 * last one.
	 *
	 * @return
	 * @since 2.2
	 */
	default Pageable2 nextOrLastPageable() {
		return hasNext() ? nextPageable() : getPageable();
	}

	/**
	 * Returns the {@link Pageable2} describing the previous slice or the one describing the current slice in case it's the
	 * first one.
	 *
	 * @return
	 * @since 2.2
	 */
	default Pageable2 previousOrFirstPageable() {
		return hasPrevious() ? previousPageable() : getPageable();
	}
}

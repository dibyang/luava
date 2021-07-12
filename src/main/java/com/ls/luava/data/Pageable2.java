/*
 * Copyright 2008-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ls.luava.data;

import java.util.Optional;


/**
 * Abstract interface for pagination information.
 *
 * @author Oliver Gierke
 */
public interface Pageable2 {

	/**
	 * Returns a {@link Pageable2} instance representing no pagination setup.
	 *
	 * @return
	 */
	static Pageable2 unpaged() {
		return Unpaged.INSTANCE;
	}

	/**
	 * Returns whether the current {@link Pageable2} contains pagination information.
	 *
	 * @return
	 */
	default boolean isPaged() {
		return true;
	}

	/**
	 * Returns whether the current {@link Pageable2} does not contain pagination information.
	 *
	 * @return
	 */
	default boolean isUnpaged() {
		return !isPaged();
	}

	/**
	 * Returns the page to be returned.
	 *
	 * @return the page to be returned.
	 */
	int getPageNumber();

	/**
	 * Returns the number of items to be returned.
	 *
	 * @return the number of items of that page
	 */
	int getPageSize();

	/**
	 * Returns the offset to be taken according to the underlying page and page size.
	 *
	 * @return the offset to be taken
	 */
	long getOffset();


	/**
	 * Returns the {@link Pageable2} requesting the next {@link Page2}.
	 *
	 * @return
	 */
	Pageable2 next();

	/**
	 * Returns the previous {@link Pageable2} or the first {@link Pageable2} if the current one already is the first one.
	 *
	 * @return
	 */
	Pageable2 previousOrFirst();

	/**
	 * Returns the {@link Pageable2} requesting the first page.
	 *
	 * @return
	 */
	Pageable2 first();

	/**
	 * Returns whether there's a previous {@link Pageable2} we can access from the current one. Will return
	 * {@literal false} in case the current {@link Pageable2} already refers to the first page.
	 *
	 * @return
	 */
	boolean hasPrevious();

	/**
	 * Returns an {@link Optional} so that it can easily be mapped on.
	 *
	 * @return
	 */
	default Optional<Pageable2> toOptional() {
		return isUnpaged() ? Optional.empty() : Optional.of(this);
	}
}

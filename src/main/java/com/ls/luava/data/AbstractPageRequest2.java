package com.ls.luava.data;

import java.io.Serializable;


/**
 * @author yangzj
 *
 */
public abstract class AbstractPageRequest2 implements Pageable2, Serializable {

	private static final long serialVersionUID = 1232825578694716871L;

	private final int page;
	private final int size;

	/**
	 * Creates a new {@link AbstractPageRequest2}. Pages are zero indexed, thus providing 0 for {@code page} will return
	 * the first page.
	 *
	 * @param page must not be less than zero.
	 * @param size must not be less than one.
	 */
	public AbstractPageRequest2(int page, int size) {

		if (page < 0) {
			throw new IllegalArgumentException("Page2 index must not be less than zero!");
		}

		if (size < 1) {
			throw new IllegalArgumentException("Page2 size must not be less than one!");
		}

		this.page = page;
		this.size = size;
	}

	@Override
  public int getPageSize() {
		return size;
	}

	@Override
  public int getPageNumber() {
		return page;
	}

	@Override
  public long getOffset() {
		return (long) page * (long) size;
	}

	@Override
  public boolean hasPrevious() {
		return page > 0;
	}

	@Override
  public Pageable2 previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	@Override
  public abstract Pageable2 next();

	/**
	 * Returns the {@link Pageable2} requesting the previous {@link Page2}.
	 *
	 * @return
	 */
	public abstract Pageable2 previous();

	@Override
  public abstract Pageable2 first();

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;

		result = prime * result + page;
		result = prime * result + size;

		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		AbstractPageRequest2 other = (AbstractPageRequest2) obj;
		return this.page == other.page && this.size == other.size;
	}
}

package com.ls.luava.data;

public class PageRequest2 extends AbstractPageRequest2 {

	private static final long serialVersionUID = -4541509938956089562L;


	protected PageRequest2(int page, int size) {

		super(page, size);

	}

	/**
	 * Creates a new unsorted {@link PageRequest2}.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @since 2.0
	 */
	public static PageRequest2 of(int page, int size) {
		return of(page, size);
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable2#next()
	 */
	@Override
	public Pageable2 next() {
		return new PageRequest2(getPageNumber() + 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * @see AbstractPageRequest2#previous()
	 */
	@Override
	public PageRequest2 previous() {
		return getPageNumber() == 0 ? this : new PageRequest2(getPageNumber() - 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable2#first()
	 */
	@Override
	public Pageable2 first() {
		return new PageRequest2(0, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageRequest2)) {
			return false;
		}

		PageRequest2 that = (PageRequest2) obj;

		return super.equals(that) ;
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() ;
	}

	@Override
	public String toString() {
		return String.format("Page2 request [number: %d, size %d]", getPageNumber(), getPageSize());
	}
}

package com.ls.luava.data;

public class PageRequest extends AbstractPageRequest {

	private static final long serialVersionUID = -4541509938956089562L;


	protected PageRequest(int page, int size) {

		super(page, size);

	}

	/**
	 * Creates a new unsorted {@link PageRequest}.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @since 2.0
	 */
	public static PageRequest of(int page, int size) {
		return of(page, size);
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#next()
	 */
	@Override
	public Pageable next() {
		return new PageRequest(getPageNumber() + 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * @see AbstractPageRequest#previous()
	 */
	@Override
	public PageRequest previous() {
		return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#first()
	 */
	@Override
	public Pageable first() {
		return new PageRequest(0, getPageSize());
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

		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		return super.equals(that) ;
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() ;
	}

	@Override
	public String toString() {
		return String.format("Page request [number: %d, size %d]", getPageNumber(), getPageSize());
	}
}

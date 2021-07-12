package com.ls.luava.data;

public enum Unpaged implements Pageable2 {
  INSTANCE;

	@Override
	public boolean isPaged() {
		return false;
	}


	@Override
	public Pageable2 previousOrFirst() {
		return this;
	}


	@Override
	public Pageable2 next() {
		return this;
	}


	@Override
	public boolean hasPrevious() {
		return false;
	}



	@Override
	public int getPageSize() {
		throw new UnsupportedOperationException();
	}


	@Override
	public int getPageNumber() {
		throw new UnsupportedOperationException();
	}


	@Override
	public long getOffset() {
		throw new UnsupportedOperationException();
	}


	@Override
	public Pageable2 first() {
		return this;
	}
}

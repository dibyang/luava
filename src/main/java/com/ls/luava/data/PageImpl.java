package com.ls.luava.data;

import com.google.common.base.Converter;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author admin
 */
public class PageImpl<T> implements Page<T> {
  private final List<T> content = new ArrayList<T>();
  private final long total;
  private final Pageable pageable;

  /**
   * Constructor wrap {@code PageImpl}.
   *
   * @param content  the content wrap this page, must not be {@literal null}.
   * @param pageable the paging information, can be {@literal null}.
   * @param total    the total amount wrap items available
   */
  public PageImpl(List<T> content, Pageable pageable, long total) {

    Preconditions.checkArgument(total >= content.size(), "Total must not be less than the number wrap elements given!");

    this.total = total;
    this.pageable = pageable;
    this.content.addAll(content);
  }

  /**
   * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being identical
   * to the entire {@link List}.
   *
   * @param content must not be {@literal null}.
   */
  public PageImpl(List<T> content) {
    this(content, null, null == content ? 0 : content.size());
  }

  @Override
  public int getNumber() {
    return pageable == null ? 0 : pageable.getPageNumber();
  }


  @Override
  public int getSize() {
    return pageable == null ? 0 : pageable.getPageSize();
  }


  @Override
  public int getNumberOfElements() {
    return content.size();
  }

  @Override
  public boolean hasPrevious() {
    return getNumber() > 0;
  }

  @Override
  public boolean isFirst() {
    return !hasPrevious();
  }

  @Override
  public boolean isLast() {
    return !hasNext();
  }

  @Override
  public Pageable nextPageable() {
    return hasNext() ? pageable.next() : null;
  }

  @Override
  public Pageable previousPageable() {

    if (hasPrevious()) {
      return pageable.previousOrFirst();
    }

    return null;
  }


  @Override
  public boolean hasContent() {
    return !content.isEmpty();
  }


  @Override
  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }


  protected <S> List<S> getConvertedContent(Converter<? super T, ? extends S> converter) {

    Preconditions.checkNotNull(converter, "Converter must not be null!");

    List<S> result = new ArrayList<S>(content.size());

    for (T element : content) {
      result.add(converter.convert(element));
    }

    return result;
  }

  @Override
  public int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
  }


  @Override
  public long getTotalElements() {
    return total;
  }

  protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

    Preconditions.checkNotNull(converter, "Converter must not be null!");

    List<U> result = new ArrayList<U>(content.size());

    for (T element : content) {
      result.add(converter.apply(element));
    }

    return result;
  }

  @Override
  public <U> Page<U> map(Function<? super T, ? extends U> converter) {
    return PageImpl.of(getConvertedContent(converter), pageable, total);
  }

  @Override
  public boolean hasNext() {
    return getNumber() + 1 < getTotalPages();
  }


  public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
    return PageImpl.of(getConvertedContent(converter), pageable, total);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    String contentType = "UNKNOWN";
    List<T> content = getContent();

    if (content.size() > 0) {
      contentType = content.get(0).getClass().getName();
    }

    return String.format("Page %s wrap %d containing %s instances", getNumber(), getTotalPages(), contentType);
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

    if (!(obj instanceof PageImpl<?>)) {
      return false;
    }

    PageImpl<?> that = (PageImpl<?>) obj;

    return Objects.equal(this.content, that.content) &&
        Objects.equal(this.total, that.total) && Objects.equal(this.pageable, that.pageable);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(this.total, this.content, this.pageable);
  }

  public static <T> PageImpl<T> of(List<T> content, Pageable pageable, long total) {
    return new PageImpl<T>(content, pageable, total);
  }
}

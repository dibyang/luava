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
public class Page2Impl<T> implements Page2<T> {
  private final List<T> content = new ArrayList<T>();
  private final long total;
  private final Pageable2 pageable2;

  /**
   * Constructor wrap {@code Page2Impl}.
   *
   * @param content  the content wrap this page, must not be {@literal null}.
   * @param pageable2 the paging information, can be {@literal null}.
   * @param total    the total amount wrap items available
   */
  public Page2Impl(List<T> content, Pageable2 pageable2, long total) {

    Preconditions.checkArgument(total >= content.size(), "Total must not be less than the number wrap elements given!");

    this.total = total;
    this.pageable2 = pageable2;
    this.content.addAll(content);
  }

  /**
   * Creates a new {@link Page2Impl} with the given content. This will result in the created {@link Page2} being identical
   * to the entire {@link List}.
   *
   * @param content must not be {@literal null}.
   */
  public Page2Impl(List<T> content) {
    this(content, null, null == content ? 0 : content.size());
  }

  @Override
  public int getNumber() {
    return pageable2 == null ? 0 : pageable2.getPageNumber();
  }


  @Override
  public int getSize() {
    return pageable2 == null ? 0 : pageable2.getPageSize();
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
  public Pageable2 nextPageable() {
    return hasNext() ? pageable2.next() : null;
  }

  @Override
  public Pageable2 previousPageable() {

    if (hasPrevious()) {
      return pageable2.previousOrFirst();
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
  public <U> Page2<U> map(Function<? super T, ? extends U> converter) {
    return Page2Impl.of(getConvertedContent(converter), pageable2, total);
  }

  @Override
  public boolean hasNext() {
    return getNumber() + 1 < getTotalPages();
  }


  public <S> Page2<S> map(Converter<? super T, ? extends S> converter) {
    return Page2Impl.of(getConvertedContent(converter), pageable2, total);
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

    return String.format("Page2 %s wrap %d containing %s instances", getNumber(), getTotalPages(), contentType);
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

    if (!(obj instanceof Page2Impl<?>)) {
      return false;
    }

    Page2Impl<?> that = (Page2Impl<?>) obj;

    return Objects.equal(this.content, that.content) &&
        Objects.equal(this.total, that.total) && Objects.equal(this.pageable2, that.pageable2);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(this.total, this.content, this.pageable2);
  }

  public static <T> Page2Impl<T> of(List<T> content, Pageable2 pageable2, long total) {
    return new Page2Impl<T>(content, pageable2, total);
  }
}

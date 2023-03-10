package com.ls.luava.common.io;

import com.google.common.annotations.VisibleForTesting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;


/**
 * 可关闭对象的辅助工具
 * 
 * @author 杨志坚 Email: dib.yang@gmail.com
 *
 */
public final class Closeables {
  @VisibleForTesting
  final static Logger LOG = LoggerFactory.getLogger(Closeables.class);

  private Closeables() {
  }

  /**
   * Closes a {@link Closeable}, with control over whether an
   * {@code IOException} may be thrown. This is primarily useful in a finally
   * block, where a thrown exception needs to be logged but not propagated
   * (otherwise the original exception will be lost).
   * 
   * <p>
   * If {@code swallowIOException} is true then we never throw
   * {@code IOException} but merely log it.
   * 
   * <p>
   * Example:
   * 
   * <pre>
   * {@code
   * 
   *   public void useStreamNicely() throws IOException {
   *     SomeStream stream = new SomeStream("foo");
   *     boolean threw = true;
   *     try {
   *       // ... code which does something with the stream ...
   *       threw = false;
   *     } finally {
   *       // If an exception occurs, rethrow it only if threw==false:
   *       Closeables.close(stream, threw);
   *     }
   *   }}
   * </pre>
   * 
   * @param closeable
   *          the {@code Closeable} object to be closed, or null, in which case
   *          this method does nothing
   * @param swallowIOException
   *          if true, don't propagate IO exceptions thrown by the {@code close}
   *          methods
   * @throws IOException
   *           if {@code swallowIOException} is false and {@code close} throws
   *           an {@code IOException}.
   */
  public static void close(@Nullable Closeable closeable, boolean swallowIOException)
      throws IOException {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (IOException e) {
      if (swallowIOException) {
        LOG.warn("IOException thrown while closing Closeable.", e);
      } else {
        throw e;
      }
    }
  }

  public static void closeQuietly(@Nullable Closeable closeable) {
    try {
      close(closeable, true);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
  }
  
  /**
   * Closes a {@link Closeable}, with control over whether an
   * {@code IOException} may be thrown. This is primarily useful in a finally
   * block, where a thrown exception needs to be logged but not propagated
   * (otherwise the original exception will be lost).
   * 
   * <p>
   * If {@code swallowIOException} is true then we never throw
   * {@code IOException} but merely log it.
   * 
   * <p>
   * Example:
   * 
   * <pre>
   * {@code
   * 
   *   public void useStreamNicely() throws IOException {
   *     SomeStream stream = new SomeStream("foo");
   *     boolean threw = true;
   *     try {
   *       // ... code which does something with the stream ...
   *       threw = false;
   *     } finally {
   *       // If an exception occurs, rethrow it only if threw==false:
   *       Closeables.close(stream, threw);
   *     }
   *   }}
   * </pre>
   * 
   * @param closeable
   *          the {@code AutoCloseable} object to be closed, or null, in which case
   *          this method does nothing
   * @param swallowIOException
   *          if true, don't propagate IO exceptions thrown by the {@code close}
   *          methods
   * @throws Exception
   */
  public static void close(@Nullable AutoCloseable closeable, boolean swallowIOException)
      throws Exception {
    if (closeable == null) {
      return;
    }
    try {
      closeable.close();
    } catch (Exception e) {
      if (swallowIOException) {
        LOG.warn("IOException thrown while closing Closeable.", e);
      } else {
        throw e;
      }
    }
  }
  
  public static void closeQuietly(@Nullable AutoCloseable closeable) {
    try {
      close(closeable, true);
    } catch (Exception impossible) {
      throw new AssertionError(impossible);
    }
  }

}

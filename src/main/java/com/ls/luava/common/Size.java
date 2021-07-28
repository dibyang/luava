package com.ls.luava.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 容量大小对象
 * 
 * @author 杨志坚 Email: dib.yang@gmail.com
 *
 */
public class Size implements Comparable<Size> {
  private final static Pattern pattern = Pattern
      .compile("((\\d*\\.)?\\d+((e|E)\\d+)?\\s*)(B|BYTES|K|KB|M|MB|G|GB|T|TB|P|PB|E|EB|Z|ZB)?");

  private final static DecimalFormat df = new DecimalFormat("#0.###",
      DecimalFormatSymbols.getInstance(Locale.US));

  private long byteSize = 0;
  private Unit unit = Unit.MB;

  public void applyPattern(String pattern) {
    df.applyPattern(pattern);
  }

  public long getByteSize() {
    return byteSize;
  }

  public void setByteSize(long byteSize) {
    this.byteSize = byteSize;
    this.unit = Unit.machUnit(byteSize);

  }

  public Size(long byteSize) {
    if (byteSize < 0) {
      throw new IllegalArgumentException("byteSize < 0");
    }
    this.setByteSize(byteSize);
  }
  
  public Size(double byteSize) {
    this((long)byteSize);
  }
  

  public Size(double size, Unit unit) {
    this.setUnit(unit);
    this.setSize(size);
  }

  public Unit getUnit() {
    if (unit == null) {
      unit = Unit.bytes;
    }
    return this.unit;
  }

  public double getSize() {
    return this.unit.machSize(byteSize);
  }

  public void setSize(double size) {
    this.setByteSize((long)(size*this.getUnit().getRate()));
  }

  public void setUnit(Unit unit) {
    if (unit != null) {
      this.unit = unit;
    } else {
      this.unit = Unit.bytes;
    }
  }

  @Override
  public boolean equals(Object obj) {
    // TODO Auto-generated method stub
    if (obj instanceof Size) {
      return this.byteSize == ((Size) obj).byteSize;
    }
    return false;
  }

  public Size add(Size size) {
    long byteSize = this.getByteSize() + size.getByteSize();
    return Size.toSize(byteSize);
  }

  public Size subtract(Size size) {
    return Size.toSize(this.getByteSize() - size.getByteSize());
  }

  public Size multiply(double num) {
    return Size.toSize(this.getByteSize() * num);
  }

  public Size divide(double num) {
    return Size.toSize(this.getByteSize() / num);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(df.format(this.getSize())).append(this.getUnit());
    return s.toString();
  }

  public static Size parse(String size) {
    Size rsize = Size.toSize(0);
    if (size == null) {
      size = "";
    }
    size = size.trim();
    if (!size.isEmpty()) {
      Matcher m = pattern.matcher(size);
      if (m.matches() && m.groupCount() == 5) {
        String n = m.group(1);
        String u = m.group(5);
        Unit unit = Unit.parse(u);
        if (unit == null) {
          rsize = new Size(Long.parseLong(n));
        } else {
          rsize = new Size(Double.parseDouble(n) * unit.getRate());
        }
      }
    }
    return rsize;
  }

  public static Size toSize(double byteSize) {
    return toSize((long) byteSize);
  }

  public static Size toSize(long byteSize) {
    // System.out.println("byteSize = " + byteSize);
    Unit unit = Unit.machUnit(byteSize);
    Size size = new Size(byteSize);
    size.setUnit(unit);
    return size;
  }

  public static void main(String[] args) {
    System.out.println(Size.parse("123456789000KB"));
    System.out.println(Size.parse("1e5KB"));
    Size s32 = Size.parse("32KB");
    System.out.println("s32 = " + s32);
    Size size = Size.parse("1GB");

    System.out.println(size.add(s32).getByteSize() + "=" + (s32.getByteSize() + size.getByteSize()));

  }

  @Override
  public int compareTo(Size o) {
    if(o!=null) {
      return Long.compare(this.byteSize, o.byteSize);
    }
    return 1;
  }
}

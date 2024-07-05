package com.ls.luava.utils;

import com.google.common.base.Stopwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author yangzj
 * @since 0.3.0
 */
public class ZipUtils {

  /**
   * 压缩
   *
   * @param data 待压缩数据
   * @return byte[] 压缩后的数据
   */
  public static byte[] compress(byte[] data) {
    
      byte[] output = new byte[0];

      Deflater compresser = new Deflater(-1, true);

      compresser.reset();
      compresser.setInput(data);
      compresser.finish();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      try {
          byte[] buf = new byte[1024];
          while (!compresser.finished()) {
              int i = compresser.deflate(buf);
              bos.write(buf, 0, i);
          }
          output = bos.toByteArray();
      } catch (Exception e) {
          output = data;
          e.printStackTrace();
      } finally {
          try {
              bos.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      compresser.end();
      return output;
  }

  /**
   * 解压缩
   *
   * @param data 待压缩的数据
   * @return byte[] 解压缩后的数据
   */
  public static byte[] decompress(byte[] data) {
      byte[] output = new byte[0];

      Inflater decompresser = new Inflater(true);
      decompresser.reset();
      decompresser.setInput(data);

      ByteArrayOutputStream o = new ByteArrayOutputStream();
      try {
          byte[] buf = new byte[1024];
          while (!decompresser.finished()) {
              int i = decompresser.inflate(buf);
              o.write(buf, 0, i);
          }
          output = o.toByteArray();
      } catch (Exception e) {
          output = data;
          e.printStackTrace();
      } finally {
          try {
              o.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

      decompresser.end();
      return output;
  }

  public static void main(String[] args) {
    
//    StringBuilder s = new StringBuilder("snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zl");
//    for(int i=0;i<10;i++){
//      s.append(" i=")
//      .append(i)
//      .append(";");
//
//      test(s.toString());
//    }

    String ss = "{\n" +
        "  \"id\": \"c70bc1e8-bac4-42de-aa33-7f934df11dee\",\n" +
        "  \"refId\": 4,\n" +
        "  \"ip\": \"13.15.26.27\",\n" +
        "  \"auto\": false,\n" +
        "  \"mountPath\": \"/datapool/\",\n" +
        "  \"path\": null,\n" +
        "  \"nodeName\": null,\n" +
        "  \"isMds\": false,\n" +
        "  \"isIstore\": false,\n" +
        "  \"subDir\": \"\",\n" +
        "  \"comment\": null,\n" +
        "  \"status\": 1,\n" +
        "  \"locked\": 0,\n" +
        "  \"noatime\": null,\n" +
        "  \"autoStartType\": \"no_start\",\n" +
        "  \"mdsRefId\": 0,\n" +
        "  \"monitor\": true,\n" +
        "  \"sort\": 1,\n" +
        "  \"role\": 0,\n" +
        "  \"roleName\": \"\",\n" +
        "  \"version\": \"20230411\",\n" +
        "  \"windows\": false,\n" +
        "  \"linux\": true,\n" +
        "  \"nextVersion\": \"\",\n" +
        "  \"module\": 0,\n" +
        "  \"nextModule\": 0,\n" +
        "  \"pushResult\": null,\n" +
        "  \"startParams\": {\n" +
        "    \"ns\": false,\n" +
        "    \"conn\": 1\n" +
        "  },\n" +
        "  \"resourcePoolName\": \"admin\",\n" +
        "  \"startFilter\": false\n" +
        "}";
    test(ss);
  }

  private static void test(String s) {
    System.out.println("");
    System.out.println("输入字符串:\t" + s);
    byte[] input = s.getBytes();
    System.out.println("输入字节长度:\t" + input.length);
    Stopwatch started = Stopwatch.createStarted();
    byte[] data = ZipUtils.compress(input);
    System.out.println("started = " + started);
    System.out.println("压缩后字节长度:\t" + data.length);
    Stopwatch started2 = Stopwatch.createStarted();
    byte[] output = ZipUtils.decompress(data);
    System.out.println("started2 = " + started2);
    System.out.println("解压缩后字节长度:\t" + output.length);
    String outputStr = new String(output);
    System.out.println("输出字符串:\t" + outputStr);
  }
}

package com.ls.luava.common;

import com.google.common.net.InetAddresses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络掩码计算工具
 * @author yangzj
 *
 */
public class NetMasks {
  static Logger logger = LoggerFactory.getLogger(NetMasks.class);
  
  public static short getPrefixLen(String ipString){
    return getPrefixLen(InetAddresses.forString(ipString));
  }
  
  public static short getPrefixLen(InetAddress address){
    short prefixLen = 0;
    byte[] addr = address.getAddress();
    for(byte addr_b : addr){
      int b = (int)addr_b;
      if((b & 0x80) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x40) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x20) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x10) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x08) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x04) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x02) > 0 ){
        prefixLen++;
      }else{
        break;
      }
      if((b & 0x01) > 0 ){
        prefixLen++;
      }else{
        break;
      }
    }
    return prefixLen;
  }
  
  
  public static String getMaskString(short prefixLen){
    InetAddress mask = getMask(prefixLen);
    return mask!=null?mask.getHostAddress():"";
  }
  
  public static InetAddress getMask(short prefixLen)
  {
    InetAddress mask = null;
    int addrLen = 4;
    byte[] addr = new byte[addrLen];

    for (int i = 0; i < addrLen; i++) {
      int offset = 8 * i;
      if (prefixLen > offset) {
        if (prefixLen < offset + 8) {
          addr[i] = (byte) (0xff << (8 + offset - prefixLen));
        } else {
          addr[i] = (byte) 0xff;
        }
      } else {
        addr[i] = 0x00;
      }
    }
    try {
      mask= InetAddress.getByAddress(addr);
    } catch (UnknownHostException e) {
      logger.warn(null,e);
    }
    return mask;
  }
  
  public static String getBroadcastString(String ipString, short prefixLen)
  {
    return getBroadcastString(InetAddresses.forString(ipString),prefixLen);
  }
  
  public static String getBroadcastString(InetAddress address, short prefixLen)
  {
    InetAddress broadcast = getBroadcast(address, prefixLen);
    return broadcast!=null?broadcast.getHostAddress():"";
  }
  
  
  public static InetAddress getBroadcast(String ipString, short prefixLen)
  {
    return getBroadcast(InetAddresses.forString(ipString), prefixLen);
  }
  
  public static InetAddress getBroadcast(InetAddress address, short prefixLen)
  {
    InetAddress broadcast = null;
    byte[] addr = address.getAddress();
    int addrLen = addr.length;
    byte[] baddr = new byte[addrLen];
    for (int i = 0; i < addrLen; i++) {
      int offset = 8 * i;
      if (prefixLen > offset) {
        if (prefixLen < offset + 8) {
          baddr[i] = (byte) (addr[i] & (0xff << (8 + offset - prefixLen)));
        } else {
          baddr[i] = addr[i];
        }
      } else {
        baddr[i] = (byte)0xff;
      }
    }
    try {
      broadcast = InetAddress.getByAddress(baddr);
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return broadcast;
  }

  public static String getNetIdString(String ipString, short prefixLen)
  {
    return getNetIdString(InetAddresses.forString(ipString),prefixLen);
  }

  public static String getNetIdString(InetAddress address, short prefixLen)
  {
    InetAddress netId = getNetId(address, prefixLen);
    return netId!=null?netId.getHostAddress():"";
  }

  public static InetAddress getNetId(InetAddress address, short prefixLen)
  {
    InetAddress netId = null;
    byte[] addr = address.getAddress();
    int addrLen = addr.length;
    byte[] baddr = new byte[addrLen];
    for (int i = 0; i < addrLen; i++) {
      int offset = 8 * i;
      if (prefixLen > offset) {
        if (prefixLen < offset + 8) {
          baddr[i] = (byte) (addr[i] & (0xff << (8 + offset - prefixLen)));
        } else {
          baddr[i] = addr[i];
        }
      } else {
        baddr[i] = (byte)0x00;
      }
    }
    try {
      netId = InetAddress.getByAddress(baddr);
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return netId;
  }
  
  public static boolean isSameSubnet(short prefixLen, InetAddress address1, InetAddress address2){
    boolean sameSubnet = false;
    sameSubnet = getBroadcast(address1,prefixLen).equals(getBroadcast(address2,prefixLen));
    return sameSubnet;
  }
  
  public static boolean isSameSubnet(short prefixLen, String ip1, String ip2){
    boolean sameSubnet = false;
    sameSubnet = getBroadcast(ip1,prefixLen).equals(getBroadcast(ip2,prefixLen));
    return sameSubnet;
  }

  public static boolean isHostReachable(String host, Integer timeOut) {
    try {
      return InetAddress.getByName(host).isReachable(timeOut);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void main(String[] args) {
    String s = NetMasks.getNetIdString("13.13.13.104", (short)16);
    System.out.println("s = " + s);
  }

}

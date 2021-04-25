package com.ls.luava.spi;

import java.util.Date;

/**
 * 时间服务
 * @author yangzj
 * @creation Mar 17, 2017 9:39:10 AM
 *
 */
public interface TimeService {
  
  int rank();

  Date now();
}

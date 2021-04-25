package com.ls.luava.common;

import com.google.common.collect.Lists;
import com.ls.luava.spi.TimeService;

import java.util.*;

/**
 * 获取当前时间
 * 支持通过第三方获取当前时间
 * @author dib
 *
 */
public enum TimeHelper {
  i;
  private final List<TimeService> timeServices = Lists.newArrayList();
  
  private TimeHelper(){
    ServiceLoader<TimeService> serviceLoader = ServiceLoader.load(TimeService.class);
    Iterator<TimeService> sels = serviceLoader.iterator();
    while(sels.hasNext()){
      this.timeServices.add(sels.next());
    }
    Collections.sort(timeServices, (o1, o2) -> o1.rank() == o2.rank() ? 0 : (o1.rank() > o2.rank() ? 1 : -1));
    
  }
  

  public synchronized Date now()
  {
    if(!timeServices.isEmpty())
    {
      return timeServices.get(0).now();
    }
    return new Date(System.currentTimeMillis());
  }

  public synchronized long getTime() {
    return now().getTime();
  }

}


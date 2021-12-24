package com.ls.luava.common;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @author yangzj
 */
public class TextParser {

  public static final String REGEX_ANY_EMPTY = "(\\s)+";
  public static final String REGEX_ONE_EMPTY = "\\s";

  public  N3Map parseMap(String str, String split) {
    N3Map data = new N3Map();
		String[] params=str.split(split);
		for(String param:params)
		{
			int i=param.indexOf(":");
			if(i>0)
			{
				String key=param.substring(0, i).trim();
				String value=param.substring(i+1).trim();
				
				data.put(key, value);
			}
		}
		return data;
	}
	
	
	public List<String> parseValues(String str)
	{
		return parseValues(str.trim(), REGEX_ANY_EMPTY);
	}
	
	public List<String> parseValues(String str,String regex)
	{
		List<String> values = Lists.newArrayList();
		String[] vs=str.split(regex);
		for(String v:vs){
		  values.add(v);
    }
		return values;
	}

  public List<N3Map> parseTableBySpliter(String str) {
    return parseTableBySpliter(str, REGEX_ANY_EMPTY);
  }
	
	public List<N3Map> parseTableBySpliter(String str,String regex) {
		List<String> lines= Lists.newArrayList(Splitter.on("\n").trimResults().omitEmptyStrings().splitToList(str));
		
		return parseTableBySpliter(lines,regex);
	}

  public List<N3Map> parseTableBySpliter(List<String> lines) {
    return parseTableBySpliter(lines, REGEX_ANY_EMPTY);
  }
	
	/**
	 * 分隔符解析表格
	 * @param lines
	 * @return
	 */
	public List<N3Map> parseTableBySpliter(List<String> lines, String regex) {
		List<N3Map> data=Lists.newArrayList();
		List<String> head=this.parseValues(lines.get(0),regex);
		for(int i=1;i<lines.size();i++)
		{
			List<String> lds=this.parseValues(lines.get(i),regex);
      N3Map map = new N3Map();
			for(int j=0;j<head.size();j++)
			{
				if(j<lds.size()){
				  map.put(head.get(j), lds.get(j));
        }
			}
			data.add(map);
		}
		
		
		return data;
	}
	

	public boolean startsWith(List<String> lines, String prefix) {
		for(String line:lines)
		{
			if(line.startsWith(prefix))
			{
				return true;
			}
		}
			
		return false;
	}
	

	public boolean endsWith(List<String> lines,String suffix) {
		for(String line:lines)
		{
			if(line.endsWith(suffix))
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean contains(List<String> lines, String... suffixs) {
		for(String line:lines)
		{
			boolean contains=true;
			for(String suffix:suffixs)
			{
				contains=contains&line.contains(suffix);
			}
			if(contains){
			  return true;
      }
		}
			
		return false;
	}
	
	/**
	 * 解析定长表头
	 * @param line
	 * @return
	 */
	public List<TH> parseTH(String line)
	{
		List<TH> ths=Lists.newArrayList();
		if(!Strings.isNullOrEmpty(line))
		{
			int index=0;
			List<String> names=this.parseValues(line);
			for(String name:names)
			{
				TH th=new TH(name);
				th.setBeginIndex(line.indexOf(name,index));
        th.setEndIndex(-1);
				if(ths.size()>0) {
          ths.get(ths.size()-1).setEndIndex(th.getBeginIndex());
        }
				index=th.getBeginIndex()+th.getName().length();
				ths.add(th);
			}
		}
		return ths;
	}
	public List<N3Map> parseTableByWidth(String str)
	{
		List<String> lines= Lists.newArrayList(str.split("\n"));
		return this.parseTableByWidth(lines);
	}
	
	/**
	 * 定长解析表格
	 * @param lines
	 * @return
	 */
  public List<N3Map> parseTableByWidth(List<String> lines) {
    List<N3Map> data = Lists.newArrayList();
    List<TH> ths = this.parseTH(lines.get(0));
    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      if (ths.size() > 0 && line != null) {
        N3Map ld = new N3Map();
        for (int j = 0; j < ths.size(); j++) {
          TH th = ths.get(j);
          if (line.length() >= th.getEndIndex()) {
            if (th.getEndIndex()==-1) {
              ld.put(th.getName(), line.substring(th.getBeginIndex()).trim());
            } else {
              ld.put(th.getName(), line.substring(th.getBeginIndex(), th.getEndIndex()).trim());
            }
          }
        }
        data.add(ld);
      }
    }

    return data;
  }

	public  static void main(String[] args)
	{
		TextParser p = new TextParser();
		List<?> d=p.parseValues("sdan 0 558.9G 0 disk 1 5000c5009a4768e3  ",REGEX_ONE_EMPTY);
		System.out.println(d);
	}
	
	 /**
   * 解析定长表头
   * @return
   */
  
  
  public class TH
  {
    String name;
    int beginIndex;
    int endIndex;
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public int getBeginIndex() {
      return beginIndex;
    }
    public void setBeginIndex(int beginIndex) {
      this.beginIndex = beginIndex;
    }
    public int getEndIndex() {
      return endIndex;
    }
    public void setEndIndex(int endIndex) {
      this.endIndex = endIndex;
    }
    public TH()
    {
      
    }
    public TH(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name + '[' +beginIndex +
        ", " + endIndex +
        ']';
    }
  }

}

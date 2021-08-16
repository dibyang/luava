package com.ls.luava.common;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @author yangzj
 */
public class TextParser {

	public  N3Map parseMap(String str,String split) {
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
		return parseValues(str.trim(),"(\\s)+");
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
		List<String> lines= Lists.newArrayList(Splitter.on("\n").trimResults().omitEmptyStrings().splitToList(str));
		
		return parseTableBySpliter(lines);
	}
	
	/**
	 * 分隔符解析表格
	 * @param lines
	 * @return
	 */
	public List<N3Map> parseTableBySpliter(List<String> lines) {
		List<N3Map> data=Lists.newArrayList();
		List<String> head=this.parseValues(lines.get(0));
		for(int i=1;i<lines.size();i++)
		{
			List<String> lds=this.parseValues(lines.get(i));
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
				th.setBeginIndex(index);
				th.setEndIndex(line.indexOf(name,th.getBeginIndex())+name.length());
				index=th.getEndIndex()+1;
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
            if (j == ths.size() - 1) {
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
		List<?> d=p.parseValues("02:25:45 PM     IFACE   rxpck/s   txpck/s    rxkB/s    txkB/s   rxcmp/s   txcmp/s  rxmcst/s");
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
    
  }

}

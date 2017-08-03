package print.dtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import action.BaseAction;

public class SylParser extends BaseAction {
	
	/**
	 * 解析簡介
	 * @return
	 */
	public Map parseIntr(String str){
		if(str==null||str.equals("")){return null;}
		
		Map map=new HashMap();	
		String a[] = str.split("#ln;");		
		if(a.length<1){
			map.put("eng", "");
			map.put("chi", "");
			map.put("book", "");
			return map;
		}		
		try{map.put("eng", a[1]);}catch(Exception e){map.put("eng", "無資料");}		
		try{map.put("chi", a[0]);}catch(Exception e){map.put("chi", "無資料");}		
		try{map.put("book", a[2]);}catch(Exception e){map.put("book", "無資料");}		
		return map;
	}
	
	/**
	 * 解析大綱
	 * @return
	 */
	public Map parseSyl(String str){
		if(str==null||str.equals("")){return null;}
		Map map=new HashMap();		
		String a[] = str.split("#ln;");		
		if(a.length<1){
			map.put("obj", " ");
			map.put("syl", " ");
			map.put("pre", " ");
			return map;
		}
		try{map.put("obj", a[0]);}catch(Exception e){map.put("obj", "無資料");}
		try{map.put("pre", a[1]);}catch(Exception e){map.put("pre", "無資料");}
		try{map.put("syl", a[2]);}catch(Exception e){map.put("syl", "無資料");}				
			
		return map;
	}
	
	/**
	 * 解析每週綱要
	 * 連續的週資料每筆後面必須追加#ln;
	 * 迴圈要-1
	 * @return
	 */
	public List parseSyls(String str){
		
		Map map;
		List list=new ArrayList();
		try{
			String a[] = str.split("#ln");		
			
			int cnt=0;
				
			for(int i=0; i<a.length-1; i=i+4){
				map=new HashMap();
				try{
					if(i==0){
						map.put("week", a[i]);
					}else{
						map.put("week", a[i].substring(1));
					}
					map.put("topic", a[i+1].substring(1));
					map.put("content", a[i+2].substring(1));
					map.put("hours", a[i+3].substring(1));
					list.add(map);
				}catch(Exception e){
					continue;
				}
			}
		}catch(Exception e){
			map=new HashMap();
			map.put("week", "");
			map.put("topic", "");
			map.put("content", "資料尚未編輯或已毀損");
			map.put("hours", "");
			list.add(map);
		}		
		return list;
	}

}

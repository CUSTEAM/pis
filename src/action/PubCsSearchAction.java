package action;

import java.util.List;
import java.util.Map;

public class PubCsSearchAction extends BaseAction{
	
	public String execute() throws Exception {		
		
		return this.SUCCESS;
	}
	
	public String cno, sno, dno, gno, zno;
	public String Dtime_oid;
	public String year, term;
	public String opt;//選別
	public String ele;//遠距型態
	public String pay;//實習費
	public String place;//地點
	public String week, begin, end;
	public String techid, cscode, chi_name;
	
	public String search(){
		
		String school_year=getContext().getAttribute("school_year").toString();
		if(year.trim().equals(""))year=school_year;
		if(year.equals(school_year)){
			if(place.trim().equals("")){
				request.setAttribute("dtimes", getDtimes());
			}else{
				request.setAttribute("dtimes", getDtimeClass(school_year));//教室課表
			}
			
		}else{
			request.setAttribute("dtimes", getSavedtimes());
		}
		
		
		return SUCCESS;
	}
	
	private List getDtimeClass(String school_year){
		try{
			return sortOut(df.sqlGet("SELECT e.Oid as emplOid, '"+school_year+"' as school_year, cdo.name as optName, (SELECT COUNT(*)FROM stmd, Seld WHERE "
					+ "stmd.student_no=Seld.student_no AND Seld.Dtime_oid=d.Oid AND stmd.sex='1') as bsed, (SELECT COUNT(*)FROM "
					+ "stmd, Seld WHERE stmd.student_no=Seld.student_no AND Seld.Dtime_oid=d.Oid AND stmd.sex='2') as gsed,cl.ClassName,"
					+ "e.cname, c.chi_name, d.Oid, d.credit, d.opt, d.open, d.thour, d.elearning, d.stu_select,"
					+ "d.techid, d.Sterm as school_term, cl.ClassNo FROM CODE_DTIME_OPT cdo,((Dtime d LEFT OUTER JOIN empl e ON d.techid=e.idno) "
					+ "LEFT OUTER JOIN Dtime_class dc ON d.Oid=dc.Dtime_oid), Csno c, Class cl WHERE cdo.id=d.opt AND d.cscode=c.cscode AND "
					+ "d.Sterm='1'AND d.depart_class=cl.ClassNo AND d.Oid IN(SELECT Dtime_oid FROM Dtime_class WHERE place='"+place.substring(0, place.indexOf(","))+"')"
					+ "GROUP BY d.Oid ORDER BY d.Sterm"), true);
		}catch(Exception e){
			return null;
		}
	}
	
	private List getDtimes(){
		StringBuilder depart_class=new StringBuilder();
		if(!cno.equals("")){depart_class.append(cno);}else{depart_class.append("_");}
		if(!sno.equals("")){depart_class.append(sno);}else{depart_class.append("__");}
		if(!dno.equals("")){depart_class.append(dno);}else{depart_class.append("_");}
		if(!gno.equals("")){depart_class.append(gno);}else{depart_class.append("_");}
		if(!zno.equals("")){depart_class.append(zno);}else{depart_class.append("_");}
		
		StringBuilder sql=new StringBuilder("SELECT e.Oid as emplOid, "+getContext().getAttribute("school_year")+" as school_year, cdo.name as optName, "
		+ "(SELECT COUNT(*)FROM stmd, Seld WHERE stmd.student_no=Seld.student_no AND Seld.Dtime_oid=d.Oid AND stmd.sex='1') as bsed, " +
		"(SELECT COUNT(*)FROM stmd, Seld WHERE stmd.student_no=Seld.student_no AND Seld.Dtime_oid=d.Oid AND stmd.sex='2') as "
		+ "gsed,cl.ClassName, e.cname, c.chi_name, d.Oid, d.credit, d.opt, " +
		"d.open, d.thour, d.elearning, d.stu_select, d.techid, d.Sterm as school_term, cl.ClassNo FROM CODE_DTIME_OPT cdo," +
		"(Dtime d LEFT OUTER JOIN empl e ON d.techid=e.idno), Csno c, Class cl " +
		"WHERE cdo.id=d.opt AND d.cscode=c.cscode AND d.Sterm='"+term+"'AND d.depart_class=cl.ClassNo ");		
		//if(!cno.equals(""))sql.append("AND cl.CampusNo='"+cno+"'");
		if(!sno.equals(""))sql.append("AND cl.SchoolNo='"+sno+"'");
		if(!dno.equals(""))sql.append("AND cl.DeptNo='"+dno+"'");
		if(!gno.equals(""))sql.append("AND cl.Grade='"+gno+"'");
		if(!zno.equals(""))sql.append("AND cl.SeqNo='"+zno+"'");
		
		//課程名稱是否模糊搜尋
		if(cscode.indexOf(",")<0){
			sql.append("AND c.chi_name LIKE '%"+cscode+"%' ");
		}else{
			cscode=cscode.substring(0, cscode.indexOf(","));
			sql.append("AND c.cscode = '"+cscode+"' ");
		}
		//教師
		//if(!techid.equals("")){
		if(techid.indexOf(",")>0){
			sql.append("AND e.Oid ="+techid.substring(0, techid.indexOf(","))+" ");
		}else{
			sql.append("AND cl.CampusNo='"+cno+"'");//若非查詢老師則要加上校區，查老師則不用
		}
		if(!opt.equals("")){sql.append("AND d.opt='"+opt+"'");}
		if(!pay.equals("")){sql.append("AND d.extrapay='"+pay+"'");}
		if(!ele.equals("")){sql.append("AND d.elearning='"+ele+"'");}
		
		if(!week.equals("")||!begin.equals("")||!end.equals("")){
			sql.append("AND d.Oid IN(SELECT Dtime_oid FROM Dtime_class WHERE ");
			if(!week.equals(""))sql.append("week='"+week+"'AND ");
			
			if(begin.equals(end)&&!begin.equals("")&&!end.equals("")){
				sql.append("(begin<="+begin+" AND end>="+begin+")AND ");
			}else{
				if(!begin.equals(""))sql.append("begin>="+begin+" AND ");
				if(!end.equals(""))sql.append("end<="+end+" AND ");
			}
			
			sql.delete(sql.length()-4, sql.length());
			sql.append(")");
		}
		sql.append("ORDER BY d.Sterm");
		
		//教師追加留校
		if(techid.indexOf(",")<0){
			return sortOut(df.sqlGet(sql.toString()), true);
		}else{
			List<Map>list=sortOut(df.sqlGet(sql.toString()), true);
			list.addAll(df.sqlGet("SELECT ''as emplOid, '"+year+"'as school_year, ''as optName,"
			+ "'' as bsed,'' as gsed,(CASE WHEN e.kind='1' THEN '課後輔導'ELSE '生活輔導'END )as ClassName,"
			+ "em.cname,'OFFICE HOUR'as chi_name,''as Oid,''as credit,''as opt,''as open,''as thour,"
			+ "''as elearning,''as stu_select,''as techid,'"+term+"'as school_term,''as ClassNo, e.week, e.period, ep.place FROM "
			+ "(Empl_stay_info e LEFT OUTER JOIN Empl_stay_place ep ON e.idno=ep.idno), empl em "
			+ "WHERE em.idno=e.idno AND em.Oid='"+techid+"' ORDER BY e.week"));
			return list;
		}
		
	}
	
	private List getSavedtimes(){
		StringBuilder depart_class=new StringBuilder();
		if(!cno.equals("")){depart_class.append(cno);}else{depart_class.append("_");}
		if(!sno.equals("")){depart_class.append(sno);}else{depart_class.append("__");}
		if(!dno.equals("")){depart_class.append(dno);}else{depart_class.append("_");}
		if(!gno.equals("")){depart_class.append(gno);}else{depart_class.append("_");}
		if(!zno.equals("")){depart_class.append(zno);}else{depart_class.append("_");}
		
		StringBuilder sql=new StringBuilder("SELECT e.Oid as emplOid, d.school_year, cdo.name as optName,cl.ClassName, e.cname, c.chi_name, d.Oid, d.credit, d.opt, " +
				"d.thour, d.stu_select, d.techid, d.school_term, cl.ClassNo FROM CODE_DTIME_OPT cdo," +
				"((Savedtime d LEFT OUTER JOIN empl e ON d.techid=e.idno) " +
				"LEFT OUTER JOIN Dtime_class dc ON d.Oid=dc.Dtime_oid), Csno c, Class cl " +
				"WHERE cdo.id=d.opt AND cl.ClassNo=d.depart_class AND d.cscode=c.cscode AND d.school_term='"+term+"'AND d.school_year='"+year+"'");
				if(!depart_class.equals("")){sql.append("AND d.depart_class LIKE'"+depart_class+"%' ");}
				
				//課程名稱是否模糊搜尋
				if(cscode.indexOf(",")<0){
					sql.append("AND c.chi_name LIKE '%"+cscode+"%' ");
				}else{
					cscode=cscode.substring(0, cscode.indexOf(","));
					sql.append("AND c.cscode = '"+cscode+"' ");
				}
				
				//教師
				if(!techid.equals("")){sql.append("AND e.cname ='"+techid.substring(0, techid.indexOf(","))+"' ");}				
				sql.append(" GROUP BY d.Oid ORDER BY cl.ClassNo");
				
		//return sortOut(df.sqlGet(sql.toString()));
		return sortOut(df.sqlGet(sql.toString()), false);
	}
	
	private List sortOut(List<Map>list, boolean thisTerm){
		StringBuilder str;
		List<Map>tmp;
		if(thisTerm)
		for(int i=0; i<list.size(); i++){
			list.get(i).put("time", df.sqlGet("SELECT week, begin, end, place FROM Dtime_class WHERE Dtime_oid="+list.get(i).get("Oid")));
		}
		return list;
	}

}

package action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import model.EnrollRegist;
import model.EnrollRegistDept;
import model.EnrollRegistHist;
import model.EnrollStmd;
import model.Message;

public class PubEnrollAction extends BaseAction {
	public String enrollOid, depts[];
	public String logidno, logbirthday;

	public String idno, student_name, sex, birthday, gradyear, gradu_status, curr_post, curr_addr, schl_name, grad_dept,
			parent_name, parent_phone, telephone, CellPhone, perm_post, perm_addr, Email, dis, low;

	@Override
	public String execute() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String now = sf.format(new Date());
		List<Map> list = df.sqlGet("SELECT e.sign_begin, e.sign_end, cc.name as "
				+ "CampusName, cs.name as SchoolName, e.enroll_name FROM Enroll e, Enroll_dept d, "
				+ "CODE_SCHOOL cs, CODE_CAMPUS cc WHERE cc.id=d.CampusNo AND cs.id=d.SchoolNo AND "
				+ "e.Oid=d.Enroll_oid AND e.sign_begin<='" + now + "'AND e.sign_end>='" + now
				+ "'GROUP BY d.CampusNo, d.SchoolNo ORDER BY e.sign_end");

		Message msg = new Message();
		msg.setMsg("目前正在進行報名的考試<br>");
		for (int i = 0; i < list.size(); i++) {

			msg.addMsg(list.get(i).get("CampusName") + ", " + list.get(i).get("enroll_name") + ", "
					+ list.get(i).get("sign_end").toString().substring(0, 10) + "<br>");

		}
		this.savMessage(msg);

		return SUCCESS;
	}

	public String login() throws ParseException {
		Message msg = new Message();
		if (logidno.trim().length() < 5 || logbirthday.length() < 8) {
			msg.setWarning("驗證資料未輸入完成!");
			savMessage(msg);
			getSession().invalidate();
			return SUCCESS;
		}

		DetachedCriteria c = DetachedCriteria.forClass(EnrollStmd.class);
		c.add(Restrictions.eq("idno", logidno));
		// c.add(Restrictions.eq("birthday", sf.parse(logbirthday)));
		List<EnrollStmd> stds = df.getHibernateDAO().getHibernateTemplate().findByCriteria(c);

		if (stds.size() > 0) {
			Map m = getReg(stds.get(0).getIdno());

			if (m == null) {
				request.setAttribute("std", stds.get(0));
			} else {
				request.setAttribute("oStd", m);
				return "view";
			}
		} else {
			EnrollStmd std = new EnrollStmd();
			std.setIdno(logidno);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			// String now=sf.format(new Date());
			std.setBirthday(sf.parse(logbirthday));
			std.setStudentName("");
			df.update(std);
			request.setAttribute("std", std);
		}
		request.setAttribute("enrolls", getDepts());
		return SUCCESS;
	}

	private boolean checkDepts() {
		if (CellPhone.equals(""))
			return false;
		if (curr_addr.equals(""))
			return false;
		if (student_name.equals(""))
			return false;
		if (depts != null) {
			for (int i = 0; i < depts.length; i++) {
				if (!depts[i].equals(""))
					return true;
			}
		}
		return false;
	}

	private List getDepts() {
		// 可報名
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String now = sf.format(new Date());
		List<Map> list = df
				.sqlGet("SELECT e.subsel,d.CampusNo,d.SchoolNo, e.*, cc.name as CampusName, cs.name as SchoolName, "
						+ "e.enroll_name FROM Enroll e, Enroll_dept d, CODE_SCHOOL cs, "
						+ "CODE_CAMPUS cc WHERE cc.id=d.CampusNo AND cs.id=d.SchoolNo AND "
						+ "e.Oid=d.Enroll_oid AND e.sign_begin<='" + now + "'AND " + "e.sign_end>='" + now
						+ "'GROUP BY d.CampusNo, d.SchoolNo ORDER BY e.sign_end");
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("depts",
					df.sqlGet("SELECT ed.* FROM Enroll_dept ed, Enroll e WHERE e.Oid=ed.Enroll_oid AND e.Oid="
							+ list.get(i).get("Oid") + " AND ed.CampusNo='" + list.get(i).get("CampusNo")
							+ "'AND ed.SchoolNo='" + list.get(i).get("SchoolNo") + "'"));
			list.get(i).put("attachs",
					df.sqlGet("SELECT * FROM Enroll_attach WHERE Enroll_oid=" + list.get(i).get("Oid")));
		}
		return list;
	}

	public String save() throws ParseException {
		EnrollRegist er;
		EnrollStmd s;
		EnrollRegistDept erd;
		EnrollRegistHist erh;

		s = (EnrollStmd) df.hqlGetListBy("FROM EnrollStmd WHERE idno='" + idno + "'").get(0);
		s.setStudentName(student_name);
		s.setCurrPost(curr_post);
		s.setCurrAddr(curr_addr);
		s.setSchlName(schl_name);
		s.setGradDept(grad_dept);
		s.setCellPhone(CellPhone);
		s.setTelephone(telephone);
		s.setPermPost(perm_post);
		s.setPermAddr(perm_addr);
		s.setEmail(Email);
		s.setDis(dis);
		s.setLow(low);
		df.update(s);

		Message msg = new Message();
		if (!checkDepts()) {
			DetachedCriteria c = DetachedCriteria.forClass(EnrollStmd.class);
			c.add(Restrictions.eq("idno", idno));
			// c.add(Restrictions.eq("birthday", sf.parse(logbirthday)));
			List<EnrollStmd> stds = df.getHibernateDAO().getHibernateTemplate().findByCriteria(c);
			request.setAttribute("std", stds.get(0));
			request.setAttribute("enrolls", getDepts());
			msg.setMsg("資料不完整, 請逐一檢查各步驟紅色欄位");
			this.savMessage(msg);

			return SUCCESS;
		}

		df.exSql("DELETE FROM Enroll_regist WHERE idno='" + idno + "'");
		er = new EnrollRegist();
		er.setEnrollOid(Integer.parseInt(enrollOid));
		er.setIdno(idno);
		df.update(er);

		df.exSql("DELETE FROM Enroll_regist_dept WHERE idno='" + idno + "'");
		for (int i = 0; i < depts.length; i++) {
			if (!depts[i].equals("")) {
				erd = new EnrollRegistDept();
				erd.setChoice(Byte.parseByte(String.valueOf(i + 1)));
				erd.setEnrollDeptOid(Integer.parseInt(depts[i]));
				erd.setIdno(idno);
				df.update(erd);
			}
		}

		Map m = getReg(idno);

		msg.setMsg("報名完成");
		this.savMessage(msg);
		request.setAttribute("oStd", m);
		return "view";

		// return SUCCESS;
	}

	private Map getReg(String idno) throws ParseException {

		Map reg = df.sqlGetMap("SELECT * FROM Enroll_regist WHERE idno='" + idno + "'");
		Map enroll;
		if (reg != null) {
			enroll = df.sqlGetMap("SELECT * FROM Enroll WHERE Oid=" + reg.get("Enroll_oid"));
			reg.put("enroll", enroll);
			reg.put("reg", df.sqlGetMap("SELECT * FROM Enroll_regist WHERE idno='" + idno + "'"));

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Long now = new Date().getTime();
			if (now >= sf.parse(enroll.get("open_match").toString()).getTime()) {
				request.setAttribute("o_match", true);
			} else {
				request.setAttribute("o_match", false);
			}
			if (now >= sf.parse(enroll.get("open_score").toString()).getTime()) {
				request.setAttribute("o_score", true);
			} else {
				request.setAttribute("o_score", false);
			}

			reg.put("dept",
					df.sqlGet("SELECT cs.name as SchoolName, cc.name as CampusName, cd.name as DeptName, d.dept_name, "
							+ "erd.choice, erd.rank FROM Enroll_regist_dept erd, Enroll_dept d, CODE_CAMPUS cc, CODE_SCHOOL cs, "
							+ "CODE_DEPT cd WHERE cs.id=d.SchoolNo AND cd.id=d.DeptNo AND cc.id=d.CampusNo AND d.Oid=erd.Enroll_dept_oid "
							+ "AND erd.idno='" + idno + "' ORDER BY erd.choice"));

			reg.put("atta", df.sqlGet("SELECT ea.Oid as eOid, ea.attach_name, ea.online, era.* FROM Enroll_attach ea "
					+ "LEFT OUTER JOIN Enroll_regist_attach era ON ea.Oid=era.Enroll_attach_oid AND "
					+ "era.Enroll_regist_oid=" + reg.get("Oid") + " WHERE ea.Enroll_oid=" + reg.get("Enroll_oid")));
		}
		// System.out.println("SELECT * FROM Enroll_regist WHERE idno='"+idno+"'");
		return reg;
	}

}

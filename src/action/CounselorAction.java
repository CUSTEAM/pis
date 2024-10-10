package action;

import java.util.List;
import java.util.Map;

import model.Mail;
import model.MailReceiver;
import model.Message;

public class CounselorAction extends BaseAction {

	@Override
	public String execute() {
		/*
		 * List<Map>d=df.
		 * sqlGet("SELECT d.id, d.name FROM CODE_DEPT d WHERE d.id IN (SELECT DeptNo FROM Class WHERE admission='1' AND Grade='1')ORDER BY d.id"
		 * ); for(int i=0; i<d.size(); i++) { d.get(i).put("s", df.
		 * sqlGet("SELECT id, name FROM CODE_SCHOOL WHERE id IN (SELECT SchoolNo FROM Class WHERE admission='1' AND Grade='1' AND DeptNo='"
		 * +d.get(i).get("id")+"')")); } request.setAttribute("Depts", d); 院:E=工程, B=商管,
		 * A=航空, H=健康, G=通識 學制:2:二專5:五專B2四技C二技D博士M碩士
		 */
		/*
		 * List<Map>schNames=new ArrayList(); Map<String,String>m; m=new HashMap();
		 * m.put("id", "2"); m.put("name", "二專"); schNames.add(m);
		 * 
		 * m=new HashMap(); m.put("id", "5"); m.put("name", "五專"); schNames.add(m);
		 * 
		 * m=new HashMap(); m.put("id", "B2"); m.put("name", "四技"); schNames.add(m);
		 * 
		 * m=new HashMap(); m.put("id", "C"); m.put("name", "二技"); schNames.add(m);
		 * 
		 * m=new HashMap(); m.put("id", "M"); m.put("name", "碩士"); schNames.add(m);
		 * 
		 * List<Map>s=df.
		 * sqlGet("SELECT d.schNo FROM CODE_SCHOOL d WHERE d.id IN (SELECT SchoolNo FROM Class WHERE admission='1' AND Grade='1')GROUP BY d.schNo"
		 * ); for(int i=0; i<s.size(); i++) { for(int j=0; j<schNames.size(); j++) {
		 * if(schNames.get(j).get("id").equals(s.get(i).get("schNo"))) {
		 * s.get(i).put("schName", schNames.get(j).get("name")); break; } }
		 * s.get(i).put("d", df.
		 * sqlGet("SELECT id, name FROM CODE_DEPT WHERE id IN (SELECT DeptNo FROM Class WHERE admission='1' AND Grade='1' AND SchNo='"
		 * +s.get(i).get("SchNo")+"')")); } request.setAttribute("Schools", s);
		 */
		return SUCCESS;
	}

	public String student_name, cell_phone, DeptNo, note, SchoolNo;

	public String confirm() {
		Message msg = new Message();
		if (df.sqlGetInt(
				"SELECT COUNT(*)FROM Counselor_stmd WHERE cell_phone='" + cell_phone + "'AND reply IS NULL") > 0) {
			msg.setMsg("本校正安排由專人準備與您連繫..");
			this.savMessage(msg);
			return execute();
		}

		try {
			df.exSql("INSERT INTO Counselor_stmd(student_name,cell_phone,DeptNo,SchoolNo,note)VALUES('" + student_name
					+ "','" + cell_phone + "','" + DeptNo + "','" + SchoolNo + "','" + note + "')");
			msg.setMsg("咨詢發送成功，專人將儘快與您連繫");

			try {
				List<Map> u = df.sqlGet(
						"SELECT e.cname, e.Email, c.* FROM Counselor_charge c, empl e WHERE c.idno=e.idno AND c.DeptNo='"
								+ DeptNo + "'");
				Mail m = new Mail();

				m.setContent("各位好:<br>" + student_name + "同學寄送了預約報名，請登資訊系統右上角點選「意見反應單 → 招生預約管理」進行連繫。");
				m.setFrom_addr("CIS@cc.cust.edu.tw");
				m.setSender("中華科技大學資訊系統");
				m.setSubject("預約報名通知");
				m.setSend("0");
				df.update(m);

				MailReceiver r = new MailReceiver();
				for (int i = 0; i < u.size(); i++) {
					r.setMail_oid(m.getOid());
					r.setAddr(u.get(i).get("Email").toString());
					r.setName(u.get(i).get("cname").toString());
					r.setType("to");
					df.update(r);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("咨詢發送不成功，請檢查必要欄位和內容");
		}
		this.savMessage(msg);
		return execute();
	}
}

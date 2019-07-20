/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.rongcapital.ruleengine.core.json.SampleParamBean;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.core.DbHelperImpl;
import cn.rongcapital.ruleengine.service.DbHelper;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * the ITest for DbHelper
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DbHelperITestSampleData {

	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException {
		// DruidDataSource
		final DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/ruleengine-test?useUnicode=true&characterEncoding=utf8");
		ds.setUsername("root");
		ds.setPassword(null);
		ds.setMaxActive(2);

		// DbHelper
		final DbHelper dbHelper = new DbHelperImpl();

		Connection conn = null;
		Object[] params = null;

		// clear
		try {
			conn = ds.getConnection();
			// delete all
//			dbHelper.update(conn, "delete from test_table_2", null);
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		

		// test-2: select
		try {
			conn = ds.getConnection();
			// get all students, remove duplicate lines
			String sql1="select *,count(distinct sss.uid) as num from ("+
"SELECT l.uid,l.status,l.sid,a.idcard_name,a.phone,a.bank_account,l.ctime,"+
"a.idcard,a.contact1,a.contact1_phone,a.contact2,'it' as apply_type"+
" from kz_loan l, kz_stu_addition a where l.ctime>'2016-05-16 00:00:00' and l.uid=a.uid ) sss group by sss.uid";
			List<Map<String, Object>> list = dbHelper.query(conn, sql1, null);
//			System.out.println(list.size());
			
			//get mobile-contacts of each student
			int count =1;
			final ObjectMapper jsonMapper = new ObjectMapper();
			List<SampleParamBean> sampleParams = new ArrayList<SampleParamBean>();
			FileOutputStream out=new FileOutputStream("/Users/hill/sampleParamsAll1.txt"); 
			
			
			for(Map<String,Object> data:list){
				params = new Object[1];
				params[0] = data.get("uid");
				String sql2="select uid,list from kz_mobile where uid=?";
				List<Map<String, Object>> list2 = dbHelper.query(conn, sql2, params);
				
				StringBuffer sb = new StringBuffer();
				
				for(Map<String, Object> mobileContact : list2){
//					System.out.println(mobileContact.get("list"));
					 if(count==802){
						 System.out.println(mobileContact.get("list").toString().replaceAll("\"", "'"));
					 }
					JSONArray myJsonArray = new JSONArray(mobileContact.get("list").toString().replaceAll("'", "").replaceAll("\"", "'"));
//					System.out.println(myJsonArray.toString());
					 for(int i=0 ; i < myJsonArray.length() ;i++){
						 JSONObject myjObject = myJsonArray.getJSONObject(i);
						 String name = myjObject.get("firstname")==null?"":myjObject.get("firstname").toString()+myjObject.get("lastname")==null?"":myjObject.get("lastname").toString();
						 name = name.trim().replaceAll("null", "").replaceAll(",", "").replaceAll("，", "");
//						 System.out.println(name);
						 String phone = "";
						 if(myjObject.get("mobile1")!=null && !myjObject.get("mobile1").toString().trim().equals("null")){
							 phone = myjObject.get("mobile1").toString();
						 }else if(myjObject.get("mobile2")!=null && !myjObject.get("mobile2").toString().trim().equals("null")){
							 phone = myjObject.get("mobile2").toString();
						 }else if(myjObject.get("mobile3")!=null && !myjObject.get("mobile3").toString().trim().equals("null")){
							 phone = myjObject.getString("mobile3").toString();
						 }
						 sb.append(name+"|"+phone+";");
						 if(i>=40){
							 break;
						 }
					 }
//					
				}
				
				
//				System.out.println(list2.size());
//				System.out.println(sb.length()==0?"":sb.substring(0, sb.length()-1));
				
				SampleParamBean sampleParam = new SampleParamBean();
				sampleParam.setApplyType((String)data.get("apply_type"));
				sampleParam.setContact((String)data.get("contact1"));
				sampleParam.setValidContacts((String)data.get("contact1")+"|"+(String)data.get("contact2"));
				sampleParam.setInst(String.valueOf(data.get("sid")));
				sampleParam.setName((String)data.get("idcard_name"));
				sampleParam.setCertificateNumber((String)data.get("idcard"));
				sampleParam.setCellphone((String)data.get("phone"));
				sampleParam.setBankAccount((String)data.get("bank_account"));
				sampleParam.setNameRef((String)data.get("idcard_name"));
				sampleParam.setCertificateNumberRef((String)data.get("idcard"));
				sampleParam.setCellphoneRef((String)data.get("phone"));
				sampleParam.setBankAccountRef((String)data.get("bank_account"));
				sampleParam.setContacts(sb.length()==0?"":sb.substring(0, sb.length()-1));
				sampleParam.setFirstContact((String)data.get("contact1"));
				sampleParam.setFirstContactRef((String)data.get("contact1"));
				sampleParam.setFirstCellphone((String)data.get("contact1_phone"));
				sampleParam.setFirstCellphoneRef((String)data.get("contact1_phone"));
				
				final String spbStr = jsonMapper.writeValueAsString(sampleParam);
				System.out.println(spbStr);
				byte[] buff=spbStr.getBytes();
				 
	            out.write(buff,0,buff.length);
	            out.write("\n".getBytes());
	           
	            System.out.println(count++);
				
			}
			
			System.out.println("finished!");
		
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		

		

		ds.close();
	}

}

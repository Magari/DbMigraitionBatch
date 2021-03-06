package bizisolution.jdbc.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;

import bizisolution.config.ConfigurationInfo;
import bizisolution.file.ContentsFileReader;
import bizisolution.jdbc.JDBCInterface;
import bizisolution.jdbc.JDBCManager;
import bizisolution.log.ErrorLog;

public class SurveyTemplate implements JDBCInterface{
	private Connection conn = null;
	private String path = null;
	private Logger logger = Logger.getLogger(this.getClass());
	private ArrayList errList = new ArrayList();
	
	public SurveyTemplate(String path){
		this.path = path;
	}
	
	String listQuery = "SELECT TEMPLATE_SEQ, TEMPLATE_NM, FILE_NM, FILE_NM2, FILE_NM3, OWNER_SHIP, REG_ID, REG_DT, UP_ID, UP_DT, THUMBNAIL FROM SR_TEMPLATE WHERE BASE_YN = 'N'";
	String updateQuery = "INSERT INTO SR_TEMPLATE(TEMPLATE_SEQ, TEMPLATE_NM, START_CONTENTS, MAIN_CONTENTS, END_CONTENTS, OWNER_SHIP, REG_ID, REG_DT, UP_ID, UP_DT, THUMBNAIL, BASE_YN) VALUES(?,?,?,?,?,?,?,?,?,?,?,'N')";

	@Override
	public void process() {
		conn=JDBCManager.getInstance().conn;
		if(ConfigurationInfo.DB_KIND62.trim().toLowerCase().equals("mysql"))
			listQuery = "SELECT TEMPLATE_SEQ, TEMPLATE_NM, FILE_NM, FILE_NM2, FILE_NM3, OWNER_SHIP, REG_ID, DATE_FORMAT(REG_DT,'%Y-%m-%d %H:%i:%s'), UP_ID, DATE_FORMAT(UP_DT,'%Y-%m-%d %H:%i:%s'), THUMBNAIL FROM SR_TEMPLATE WHERE BASE_YN = 'N'";
		if(ConfigurationInfo.DB_KIND62.trim().toLowerCase().equals("oracle"))
			listQuery = "SELECT TEMPLATE_SEQ, TEMPLATE_NM, FILE_NM, FILE_NM2, FILE_NM3, OWNER_SHIP, REG_ID, TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS'), UP_ID, TO_CHAR(UP_DT,'YYYY-MM-DD HH24:MI:SS'), THUMBNAIL FROM SR_TEMPLATE WHERE BASE_YN = 'N'";
		if(ConfigurationInfo.DB_KIND62.trim().toLowerCase().equals("mssql"))
			listQuery = "SELECT TEMPLATE_SEQ, TEMPLATE_NM, FILE_NM, FILE_NM2, FILE_NM3, OWNER_SHIP, REG_ID, CONVERT(VARCHAR(19),REG_DT,20), UP_ID, CONVERT(VARCHAR(19),UP_DT,20), THUMBNAIL FROM SR_TEMPLATE WHERE BASE_YN = 'N'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(listQuery);
			while(rs.next()){
				int i = 1;
				HashMap tempMap = new HashMap();
				tempMap.put("template_seq", rs.getString(i++));
				tempMap.put("template_nm", rs.getString(i++));
				tempMap.put("file_nm", rs.getString(i++));
				tempMap.put("file_nm2", rs.getString(i++));
				tempMap.put("file_nm3", rs.getString(i++));
				tempMap.put("owner_ship", rs.getString(i++));
				tempMap.put("reg_id", rs.getString(i++));
				tempMap.put("reg_dt", rs.getString(i++));
				tempMap.put("up_id", rs.getString(i++));
				tempMap.put("up_dt", rs.getString(i++));
				tempMap.put("thumbnail", rs.getString(i++));
				String targetPath = path+"/"+tempMap.get("reg_id")+"/"+tempMap.get("file_nm");
				File processingFile = new File(targetPath);
				if(!processingFile.exists()){
					tempMap.put("errmsg", "file not exists");
					errList.add(tempMap);
					continue;
				}
				tempMap.put("start_contents", ContentsFileReader.getInstance().readContentsFile(targetPath));
				targetPath = path+"/"+tempMap.get("reg_id")+"/"+tempMap.get("file_nm2");
				processingFile = new File(targetPath);
				if(!processingFile.exists()){
					tempMap.put("errmsg", "file not exists");
					errList.add(tempMap);
					continue;
				}
				tempMap.put("main_contents", ContentsFileReader.getInstance().readContentsFile(targetPath));
				targetPath = path+"/"+tempMap.get("reg_id")+"/"+tempMap.get("file_nm3");
				processingFile = new File(targetPath);
				if(!processingFile.exists()){
					tempMap.put("errmsg", "file not exists");
					errList.add(tempMap);
					continue;
				}
				tempMap.put("end_contents", ContentsFileReader.getInstance().readContentsFile(targetPath));
				updateContent(tempMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ErrorLog.getInstance().saveErrorFile(errList);
			JDBCManager.getInstance().closeConnection();
			System.out.println("migration complete");
		}
	}

	@Override
	public void updateContent(HashMap param) {
		conn = JDBCManager.getInstance().conn2;
		if(ConfigurationInfo.DB_KIND65.trim().toLowerCase().equals("oracle")){
			OraclePreparedStatement psmt = null;
			updateQuery = "INSERT INTO SR_TEMPLATE(TEMPLATE_SEQ, TEMPLATE_NM, START_CONTENTS, MAIN_CONTENTS, END_CONTENTS, OWNER_SHIP, REG_ID, REG_DT, UP_ID, UP_DT, THUMBNAIL, BASE_YN) VALUES(?,?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,'N')";
			try{
				psmt = (OraclePreparedStatement)conn.prepareStatement(updateQuery);
				psmt.setString(1, (String)param.get("template_seq"));
				psmt.setString(2, (String)param.get("template_nm"));
				psmt.setStringForClob(3, (String)param.get("start_contents"));
				psmt.setStringForClob(4, (String)param.get("main_contents"));
				psmt.setStringForClob(5, (String)param.get("end_contents"));
				psmt.setString(6, (String)param.get("owner_ship"));
				psmt.setString(7, (String)param.get("reg_id"));
				psmt.setString(8, (String)param.get("reg_dt"));
				psmt.setString(9, (String)param.get("up_id"));
				psmt.setString(10, (String)param.get("up_dt"));
				psmt.setString(11, (String)param.get("thumbnail"));
				psmt.executeUpdate();
				System.out.println("insert db - " + "template_seq: " + param.get("template_seq"));
			}catch(SQLException e){
				param.put("errmsg", e.getMessage());
				errList.add(param);
				e.printStackTrace();
			}finally{
				if(psmt!=null){
					try {
						psmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(ConfigurationInfo.DB_KIND65.trim().toLowerCase().equals("mssql")){
			String identityOn = "SET IDENTITY_INSERT SR_TEMPLATE ON";
			String identityOff = "SET IDENTITY_INSERT SR_TEMPLATE OFF";
			Statement stmt = null;
			PreparedStatement psmt = null;
			try {
				stmt = conn.createStatement();
				stmt.execute(identityOn);
				psmt = conn.prepareStatement(updateQuery);
				psmt.setString(1, (String)param.get("template_seq"));
				psmt.setString(2, (String)param.get("template_nm"));
				psmt.setString(3, (String)param.get("start_contents"));
				psmt.setString(4, (String)param.get("main_contents"));
				psmt.setString(5, (String)param.get("end_contents"));
				psmt.setString(6, (String)param.get("owner_ship"));
				psmt.setString(7, (String)param.get("reg_id"));
				psmt.setString(8, (String)param.get("reg_dt"));
				psmt.setString(9, (String)param.get("up_id"));
				psmt.setString(10, (String)param.get("up_dt"));
				psmt.setString(11, (String)param.get("thumbnail"));
				psmt.executeUpdate();
				stmt.execute(identityOff);
				System.out.println("insert db - " + "template_seq: " + param.get("template_seq"));
			} catch (SQLException e) {
				param.put("errmsg", e.getMessage());
				errList.add(param);
				e.printStackTrace();
			} finally{
				if(psmt!=null){
					try {
						psmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(stmt!=null){
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(ConfigurationInfo.DB_KIND65.trim().toLowerCase().equals("mysql")){
			PreparedStatement psmt = null;
			try {
				psmt = conn.prepareStatement(updateQuery);
				psmt.setString(1, (String)param.get("template_seq"));
				psmt.setString(2, (String)param.get("template_nm"));
				psmt.setString(3, (String)param.get("start_contents"));
				psmt.setString(4, (String)param.get("main_contents"));
				psmt.setString(5, (String)param.get("end_contents"));
				psmt.setString(6, (String)param.get("owner_ship"));
				psmt.setString(7, (String)param.get("reg_id"));
				psmt.setString(8, (String)param.get("reg_dt"));
				psmt.setString(9, (String)param.get("up_id"));
				psmt.setString(10, (String)param.get("up_dt"));
				psmt.setString(11, (String)param.get("thumbnail"));
				psmt.executeUpdate();
				System.out.println("insert db - " + "template_seq: " + param.get("template_seq"));
			} catch (SQLException e) {
				param.put("errmsg", e.getMessage());
				errList.add(param);
				e.printStackTrace();
			} finally{
				if(psmt!=null){
					try {
						psmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	

}

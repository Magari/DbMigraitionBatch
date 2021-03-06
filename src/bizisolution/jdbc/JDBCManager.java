package bizisolution.jdbc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

import bizisolution.config.ConfigurationInfo;

public class JDBCManager {
	private Logger logger = Logger.getLogger(this.getClass());
	private static JDBCManager manager = null;
	private JDBCInterface jdbcControler = null;
	private Properties confPro = new Properties();
	public Connection conn = null;
	public Connection conn2 = null;
	
	private JDBCManager(){
		String configDirPath = "./config/";
		String configFile = "workDB.properties";
		File confFile = new File(configDirPath + configFile);
		
		if(!confFile.exists()){
			System.out.println("File not found. Please check the location of the file.");
			System.out.println("File name: " +configDirPath + configFile);
			System.out.println("To exit the program.");
			System.exit(1);
		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(confFile));
			System.out.println("db to obtain information.");
			confPro.load(in);
			ConfigurationInfo.DB_KIND62 = confPro.getProperty("db.kind");
			ConfigurationInfo.DB_KIND65 = confPro.getProperty("db.kind2");
			System.out.println("DB driver sets.");
			Class.forName(confPro.getProperty("driver"));
			Class.forName(confPro.getProperty("driver2"));
			System.out.println("Gets the connection.");
			conn=DriverManager.getConnection(confPro.getProperty("url"),confPro.getProperty("username"),confPro.getProperty("password"));
			conn2=DriverManager.getConnection(confPro.getProperty("url2"),confPro.getProperty("username2"),confPro.getProperty("password2"));
			
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static JDBCManager getInstance(){
		setInstance();
		return manager;
	}
	
	private static synchronized void setInstance(){
		if(manager == null)
			manager = new JDBCManager();
	}
	
	public void closeConnection(){
		if(conn!=null&&conn2!=null){
			try {
				conn.close();
				conn2.close();
				System.out.println("DB Connection close.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}

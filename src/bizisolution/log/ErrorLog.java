package bizisolution.log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import bizisolution.config.ConfigurationInfo;

public class ErrorLog {
	
	private static ErrorLog errorLog = null;
	
	private ErrorLog(){
	}
	
	public static ErrorLog getInstance(){
		setInstance();
		return errorLog;
	}
	
	private synchronized static void setInstance(){
		if(errorLog==null)
			errorLog = new ErrorLog();
	}
	
	public void saveErrorFile(ArrayList errorList){
		String path = "./log/";
		//String filename = currentDate();
		String filename = ConfigurationInfo.SEL_TYPE;
		BufferedWriter out = null;
			File file = new File(path+filename+".log");
			if(errorList.size()<=0){
				return;
			}
			if(!file.exists()){
				//filename = currentDate();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				if(file.exists())
					out  = new BufferedWriter(new FileWriter(file,true));	
				else
					out  = new BufferedWriter(new FileWriter(file));
				out.append("error date : " + currentDate());
				out.newLine();
				for(int i = 0; i < errorList.size(); i++){
					HashMap tempMap = (HashMap)errorList.get(i);
					out.append(tempMap.get("template_seq") + ","+tempMap.get("errmsg"));
					out.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	private String currentDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentDate = format.format(cal.getTime());
		return currentDate;
	}
}

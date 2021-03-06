package bizisolution.file;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/*
 * @author yotoka
 * 템플릿 파일의 내용을 읽어와 HashMap 으로 파라미터를 정리하는 클래스
 */
public class ContentsFileReader {
	private static ContentsFileReader contents = null;
	private Logger logger = Logger.getLogger(this.getClass());
	private ContentsFileReader(){	
	}
	
	public static ContentsFileReader getInstance(){
		setInstance();
		return contents;
	}
	
	private static synchronized void setInstance(){
		if(contents==null)contents = new ContentsFileReader();
	}
	
	public ArrayList settingUpdateToDB(ArrayList modifyList, String targetPath, HashMap param){
		ArrayList return_list = new ArrayList();
		for(int i = 0 ; i < modifyList.size() ; i++){
			HashMap tempMap =new HashMap();
			tempMap.put("admin_id", param.get("admin_id"));
			tempMap.put("contents_file_nm",modifyList.get(i));
			tempMap.put("contents", readContentsFile(targetPath+"/"+modifyList.get(i)));
			return_list.add(tempMap);
		}
		return return_list;
	}
	
	// html 파일 내용을 읽어온다.
	public String readContentsFile(String filePath){
		StringBuffer contents = new StringBuffer();
		String str = null;
		BufferedReader in = null;
		File targetFile = new File(filePath);
		try {
			in = new BufferedReader(new FileReader(targetFile));
			while((str = in.readLine())!=null){
				contents.append(str + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return contents.toString();
	}
}

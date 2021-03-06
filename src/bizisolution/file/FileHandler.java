package bizisolution.file;
import org.apache.log4j.Logger;

import bizisolution.config.Configuration;
import bizisolution.config.ConfigurationInfo;

import java.io.File;
import java.util.ArrayList;

/*
 * @author yotoka
 * 파일 객체 관리 클래스
 */
public class FileHandler {
	private static Logger logger = Logger.getLogger(FileHandler.class);
	private static FileHandler fileHandler = null;
	private File file = null;
	public Directory directory = new Directory();
	public InnerFile innerFile = new InnerFile();
	
	private FileHandler(String path){
		file = new File(path);
	}
	
	public synchronized static FileHandler getInstance(){
		if(fileHandler == null)fileHandler = new FileHandler(ConfigurationInfo.HTML_ROOT + Configuration.getProperties().getProperty(ConfigurationInfo.SEL_TYPE));
		return fileHandler;
	}
	
	public File getFileInstance(){
		return file;
	}
	
	public void setFilePath(String path){
		file = new File(path);
	}
	
	public String attachFilePath(String old_path, String attach_path){
		String return_path = "";
		if(old_path.endsWith("/")){
			return_path = ConfigurationInfo.HTML_ROOT + Configuration.getProperties().getProperty(ConfigurationInfo.SEL_TYPE) + attach_path;
		}else{
			return_path = ConfigurationInfo.HTML_ROOT + Configuration.getProperties().getProperty(ConfigurationInfo.SEL_TYPE)+ "/" + attach_path;
		}
		return return_path;
	}
	
	/*
	 * 디렉토리만 관련 내부클래스
	 */
	public class Directory{
		public void printList(String[] list){
			for(int i = 0; i < list.length; i++){
				System.out.println(list[i]);
			}
		}
		
		public String[] getList(){
			File[] list = file.listFiles();
			ArrayList array = new ArrayList();
			try{
				for(int i = 0; i < list.length; i++){
					File temp = list[i];
					if(list[i].isDirectory()){
						array.add(list[i].getName());
					}
				}
			}catch(Exception e){
			}
			String[] str_return = new String[array.size()];
			for(int i = 0 ; i < array.size(); i++){
				str_return[i] = array.get(i) + "";
			}
			return str_return;
		}
	}
	
	//파일 관리 내부클래스
	public class InnerFile{
		
		public void printList(String[] list){
			for(int i = 0; i < list.length; i++){
				System.out.println(list[i]);
			}
		}
		
		public String[] getHtmlFileList(){
			File[] list = file.listFiles();
			ArrayList array = new ArrayList();
			try{
				for(int i = 0; i < list.length; i++){
					File temp = list[i];
					if(temp.isDirectory()){
					}else{
						if(temp.isFile()&&temp.getName().indexOf(".html")>-1)
						array.add(list[i].getName());
					}
				}
			}catch(Exception e){
			}
				
			String[] str_return = new String[array.size()];
			for(int i = 0 ; i < array.size(); i++){
				str_return[i] = array.get(i) + "";
			}
			return str_return;
		}
	}
}

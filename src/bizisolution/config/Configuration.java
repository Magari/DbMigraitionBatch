package bizisolution.config;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Properties;
import org.apache.log4j.Logger;

/*
 * @author yotoka
 * ./config/config.properties ������ �о�´�.
 */
public class Configuration {
	private Logger logger = Logger.getLogger(getClass());
	private String configDirPath = "./config/";
	private String configFile = "config.properties";
	private File confFile = null;
	private static Properties confPro = null;
	private static Configuration configuration = null;
		
	private Configuration(){ 
		System.out.println("get Config File..");
		confFile = new File(configDirPath + configFile);
		if(!confFile.exists()){
			System.out.println("File not found. Please check the location of the file.");
			System.out.println("File name: " +configDirPath + configFile);
			System.out.println("To exit the program.");
			System.exit(1);
		}
		convertFileToProperties();
	};

	//properties ��ü�� ����
	public static Properties getProperties(){
		return confPro;
	}
	
	//instance�� �ѹ��� ���� �ϱ� ���� ������ setter �޼���
	public static void setConfiguration(){
		if(configuration ==null)
		configuration = new Configuration();
		return;
	}
	
	//������ properties�� �о� ���� �޼���
	private void convertFileToProperties(){
		BufferedReader in = null;
		String str ="";
		confPro = new Properties();
		try {
			in = new BufferedReader(new FileReader(confFile));
			confPro.load(in);
			if(confPro.size()<=0){
				System.out.println("The phrase is not set.To exit the program.");
				System.exit(1);
			}
			System.out.println("properties reading complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

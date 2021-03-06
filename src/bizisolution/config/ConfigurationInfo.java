package bizisolution.config;
import java.util.Properties;
import org.apache.log4j.Logger;

/*
 * @author yotoka
 * config 변수 관리 클래스
 */
public class ConfigurationInfo {
	private static Logger logger = Logger.getLogger(ConfigurationInfo.class);
	private static Properties config = Configuration.getProperties();
	public static String HTML_ROOT = getValue("html.root");
	public static String MAIL_TEMPLATE = getValue("mail.template");
	public static String NEWSLETTER_TEMPLATE = getValue("nl.template");
	public static String SURVEY_TEMPLATE = getValue("survey.template");
	public static String SEL_TYPE = "";
	public static String DB_KIND62 = "";
	public static String DB_KIND65 = "";
	
	private ConfigurationInfo(){
		
	}
	
	// config.properties의 정보가 잘못될 경우 종료된다.
	private static String getValue(String request){
		String str ="";
		try{
			str = config.getProperty(request);
			if(str.equals(""));
		}catch(Exception e){
			System.out.println("Do not specify a value."+str+" To exit the program..");
			System.exit(1);
		}
			return str;
	}
}

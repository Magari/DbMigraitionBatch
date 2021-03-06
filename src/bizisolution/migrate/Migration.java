package bizisolution.migrate;
import bizisolution.config.ConfigurationInfo;
import bizisolution.jdbc.JDBCManager;
import bizisolution.jdbc.JDBCInterface;
import bizisolution.jdbc.dao.*;
import org.apache.log4j.Logger;


public class Migration {
	private Logger logger = Logger.getLogger(this.getClass());
	private JDBCInterface jdbc = null;
	String path = ConfigurationInfo.HTML_ROOT;
	public Migration(int sel){
		switch(sel){
		case 1:
			path = path + ConfigurationInfo.MAIL_TEMPLATE;
			ConfigurationInfo.SEL_TYPE = "mailtemplate";
			jdbc = new TemplateContent(path);
			break;
		case 2:
			path = path + ConfigurationInfo.NEWSLETTER_TEMPLATE;
			ConfigurationInfo.SEL_TYPE = "newslettertemplate";
			jdbc = new NewsletterTemplate(path);
			break;
		case 3:
			path = path + ConfigurationInfo.SURVEY_TEMPLATE;
			ConfigurationInfo.SEL_TYPE = "surveytemplate";
			jdbc = new SurveyTemplate(path);
			break;
		default:
			System.out.println("Invalid input. To exit the program.");
			System.exit(0);
			break;
		}
	}
	
	public void process(){
		System.out.println();
		System.out.println("choice " + ConfigurationInfo.SEL_TYPE);
		System.out.println();
		System.out.println("processing Migration Module....");
		System.out.println("processing Dir = " + path);
		//각 템플릿에 맞게 진행한다.
			jdbc.process();	
	}

}

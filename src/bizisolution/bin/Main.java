package bizisolution.bin;
import bizisolution.log.SetLogger;
import bizisolution.config.Configuration;
import bizisolution.config.ConfigurationInfo;
import bizisolution.migrate.Migration;
import java.util.Scanner;

/*
 * @author yotoka
 * @company bizisolution
 * config에 설정된 경로를 따라 템플릿파일을 DB화 한다.
 */
public class Main {
	Scanner in = new Scanner(System.in);
	int select = 0;
	public Main(){
		SetLogger.setLogger();
		Configuration.setConfiguration();
		init();
		select = checkInt();
		new Migration(select).process();
	}
	
	public void init(){
		System.out.println();
		System.out.println("bizisolution 6.2 or less migration module");
		System.out.println("Template File to DB.");
		System.out.println("Contents column on the table will be present.");
		System.out.println();
		System.out.println("Choose a template to migrate.");
		System.out.println();
		System.out.println("**************************");
		System.out.println("1. mail_template");
		System.out.println("2. newsletter_template");
		System.out.println("3. survey_template");
		System.out.print("select :");
	}
	
	/*
	 * 정수만 입력할수 있게 하는 함수.
	 */
	public int checkInt(){
		int select = 0;
		while(!in.hasNextInt()){
			in.next();
			init();
			System.out.println("Integers can be entered. Please re-type.");
			System.out.println("select :");
		}
		select = in.nextInt();
		return select;
	}
	
	public static void main(String[] args){
		new Main();
	}
}

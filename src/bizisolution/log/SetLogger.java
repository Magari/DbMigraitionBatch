package bizisolution.log;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/*
 * @author yotoka
 */
public class SetLogger {

	private static SetLogger logger = null;
	//bin으로 설정되있는 경로를 ./config/log4j.properties로 변경해준다.
	private SetLogger(){
		System.out.println("setting Logger..");
		PropertyConfigurator.configure( "./config/log4j.properties" );
	}
	public static void setLogger(){
		logger = new SetLogger();
	}
}

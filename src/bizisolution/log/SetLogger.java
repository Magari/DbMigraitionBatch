package bizisolution.log;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/*
 * @author yotoka
 */
public class SetLogger {

	private static SetLogger logger = null;
	//bin���� �������ִ� ��θ� ./config/log4j.properties�� �������ش�.
	private SetLogger(){
		System.out.println("setting Logger..");
		PropertyConfigurator.configure( "./config/log4j.properties" );
	}
	public static void setLogger(){
		logger = new SetLogger();
	}
}

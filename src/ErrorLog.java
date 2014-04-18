import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ErrorLog {
	
	private File errorLog = new File("ErrorLog.txt");

	  private BufferedWriter errorLogWriter;

	  private static final ErrorLog instance = new ErrorLog();

	  private ErrorLog() {
		  try {
			  errorLogWriter =  new BufferedWriter(new FileWriter(errorLog));
		  } catch (IOException e) {
			  
		  }
	  }

	  public static ErrorLog getInstance() {
		return instance;
	  }

	  public void write(String s) {
		  try {
			  errorLogWriter.write(s);
		  } catch(IOException e) {
			  
		  }
	  }
	  
	  public void close() {
		  try {
		     errorLogWriter.close();
		  } catch(IOException e) {
			  
		  }
	  }
}

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger implements LogChannel {
	
	
	public FileLogger(String logfileLocation, String logfileName) {
		Date date = new Date(); 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
		String filename = formatter.format(date) + logfileName + ".txt";
		try {
		      File myObj = new File(filename);
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

	@Override
	public void Log(String message) {
		// TODO Auto-generated method stub
		
	}
}

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Logger {
	public enum LogLevel
	{
	      Debug(2),Warning(1),Error(0);
	      private int value ;
	      LogLevel (int value)
	      {
	           this.value = value ;
	      }
	}
	
	private LogLevel verbosity = LogLevel.Error;
	public void SetVerbosity(LogLevel verbosity) {
		this.verbosity = verbosity;
	}
	public LogLevel GetVerbosity() {
		return this.verbosity;
	}
	private LinkedList<LogChannel> channels = new LinkedList<LogChannel>();
	
	public Logger(LogLevel verbosity, LogChannel... logChannels) {
		if (channels != null) {
			for (int i = 0; i < logChannels.length; i++) {
				channels.add(logChannels[i]);
			}
		}
		this.verbosity = verbosity;
	}
	
	public void AddChannel(LogChannel channel) {
		channels.add(channel);
	}
	
	public void LogDebug(String message) {
		LogOnAllChannels(LogLevel.Debug, message);
	}
	
	public void LogWarning(String message) {
		LogOnAllChannels(LogLevel.Warning, message);
	}
	
	public void LogError(String message) {
		LogOnAllChannels(LogLevel.Error, message);
	}
	
	private void LogOnAllChannels(LogLevel level, String message) {
		switch (level) {
			case Debug:
				if (verbosity != Logger.LogLevel.Debug) {
					return;
				}
			case Warning:
				if (verbosity == Logger.LogLevel.Error) {
					return;
				}
			default:
			}
		
		for (int i = 0; i < channels.size(); i++) {
			channels.get(i).Log(FormatMessage(level, message));
		}
	}
	
	
	private String FormatMessage(Logger.LogLevel level, String message) {
		Date date = new Date(); // This object contains the current date value
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		
		String msgPrefix = "[" + level.toString() + "] ";
		msgPrefix += formatter.format(date) + ": ";
		
		return msgPrefix + message;
	}
}

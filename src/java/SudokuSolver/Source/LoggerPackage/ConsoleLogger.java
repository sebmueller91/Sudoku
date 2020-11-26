public class ConsoleLogger implements LogChannel {
	@Override
	public void Log(String message) {
		System.out.println(message);
	}
}

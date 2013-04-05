package robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Houdt debug info bij voor te laten zien in GUI
public class DebugBuffer {
	
	/**
	 * DEBUG INFO
	 */
	
	private static List<String> debuginfo = Collections.synchronizedList(new ArrayList<String>());
	
	public static void updateDebuginfo(ArrayList<String> debuginfo) {
		for(int i = 0; i < debuginfo.size(); i++) {
			DebugBuffer.addInfo(debuginfo.get(i));
		}
	}
	
	public static void addInfo(String info) {
		debuginfo.add(System.currentTimeMillis() + " .. " + info);

		/*try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("out.txt", true)));
		    out.println(info);
		    out.close();
		} catch (IOException e) {
		    //oh noes!
		}*/
	}
	
	public static List<String> getDebuginfo() {
		return debuginfo;
	}
	
	/**
	 * COMM INFO
	 */
	
	private static List<String> comminfo = Collections.synchronizedList(new ArrayList<String>());
	
	public static void updateComminfo(ArrayList<String> comminfo) {
		for(int i = 0; i < comminfo.size(); i++) {
			DebugBuffer.addInfo(comminfo.get(i));
		}
	}
	
	public static void addComm(String info) {
		comminfo.add(System.currentTimeMillis() + " .. " + info);
	}
	
	public static List<String> getComminfo() {
		return debuginfo;
	}
}

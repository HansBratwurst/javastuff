import java.net.*;
import java.util.*;

public class jvfsck extends Thread {
	// MEMBERS
	private Socket socket;
	private int port;
	private String target;
	private boolean success;

	public jvfsck() {
		System.out.println("jvfsck()");
	}
	
	public jvfsck(int port) {
		socket = new Socket();
		this.port = port;
		this.success = false;
	}
	
		public jvfsck(String ip, int port) {
		socket = new Socket();
		this.port = port;
		this.success = false;
		this.target = ip;
	}
	
	public void connect(InetSocketAddress address) {
		try {
			socket.connect(address);
			System.out.println("Response on port: "+this.port);
			this.success = true;
		} catch(Exception e) {
			// fail silently
			//System.out.println("Error while connecting to port " + address.getPort());
		}
	}
	
	public void close() {
		try {
		socket.close();
		} catch(Exception e) {
			System.out.println("Error while closing!");
		}
	}
	
	public void run() {
		InetAddress inAd;
		try {
			inAd = InetAddress.getByName(this.target); //x.x.x.3
			this.connect(new InetSocketAddress(inAd, this.port));
			this.close();
		} catch(Exception e) {
			System.out.println("Error while getting address!");
		}
	}

	public static void main(String[] args) {
		System.out.println("args:");
		for(int o = 0; o < args.length; o++) {
			System.out.println("  "+o+" - "+args[o]);
		}
		String ip = args.length > 0 ? args[0] : "127.0.0.1";
		int portBegin = args.length > 1 ? Integer.parseInt(args[1]) : 0;
		int portEnd = args.length > 1 ? (args.length > 2 ? Integer.parseInt(args[2]) : Integer.parseInt(args[1])) : 64000;
		
		ArrayList trunk = new ArrayList();
		
		System.out.println("Attempting scan on address "+ip);
		System.out.println("Scanning ports "+ portBegin +" - "+portEnd);
		for(int i=portBegin; i<=portEnd; i++) {
			jvfsck tmp = new jvfsck(ip,i);
			tmp.run();
			if(tmp.success) {
				trunk.add(tmp);
			}
		}
	}

}

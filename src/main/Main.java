package main;

import java.io.IOException;

import GUI.Window;

import sockets.Client;
import sockets.Server;

public class Main {

	public static boolean clientMode;
	private static final int PORT = 4444;

	// Used only in client mode
	private static final String SERVERNAME = "192.168.1.114";
	public static Engine engine;

	public static void start(boolean cm, int base, int mod, int prikey) throws IOException {
		clientMode = cm;
		Window.textArea.setText("STARTED IN MODE: " + (clientMode ? "CLIENT" : "SERVER"));
		Window.append("\n");
		Window.append("PRIVATE KEY: " + prikey);
		Window.append("\n");
		if (clientMode) {
			Client client = new Client(SERVERNAME, PORT);
			engine = new Engine(client);
			engine.start();
			Engine.PRIKEY = prikey;
		} else {
			Server server = new Server(PORT);
			engine = new Engine(server);
			Engine.BASE = base;
			Engine.MOD = mod;
			Engine.PRIKEY = prikey;
			Window.append("Started with public Base: " + base + " and public Modulus: " + mod);
			
			//Starting the Engine is done in ListenThread
			server.setEngine(engine);
		}
	}

}

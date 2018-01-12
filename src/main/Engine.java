package main;

import java.io.IOException;
import java.math.BigInteger;

import GUI.Window;

import sockets.SocketWrapper;

public class Engine {

	// --------------------------------
	// MATH

	// Prime Modulus
	public static int MOD;

	// Base
	public static int BASE;

	// Private key
	public static int PRIKEY;

	// Internal Socket
	private SocketWrapper iSocket;

	private ReadThread readThread;

	// Command Messages
	private static final String START_MESSAGE = "STARTING DIFFIE-HELLMAN KEY EXCHANGE";
	private static final String BASE_REQUEST = "Request: Base";
	private static final String MOD_REQUEST = "Request: Mod";

	public static int FINAL;

	public STATE state;

	public Engine(SocketWrapper c) {
		state = STATE.Created;
		iSocket = c;
		System.out.println("Engine Created");
	}

	public void start() throws IOException {
		state = STATE.Started;
		if (!Main.clientMode) {
			iSocket.send(START_MESSAGE);
			Window.append("SENT START MESSAGE");
		} else {
			iSocket.send(BASE_REQUEST);
			Window.append("SENT BASE REQUEST");

			iSocket.send(MOD_REQUEST);
			Window.append("SENT MOD REQUEST");
		}
		state = STATE.Started;
		readThread = new ReadThread(iSocket);
		new Thread(readThread).start();
	}

	private enum STATE {
		Created, Started, Activated, Main, Finished
	}

	public void clientProcessInput(String input) throws IOException {
		System.out.println("PROCESSING: " + "\"" + input + "\"");

		if (input.equals("QUIT")) {
			try {
				iSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (state.equals(STATE.Started)) {
			if (input.equals(START_MESSAGE)) {	
				Window.append("START MESSAGE RECEIVED!");
				state = STATE.Activated;
			}
		} else if (state.equals(STATE.Activated)){	
			// Sent in that order
			if (BASE == 0) {
				BASE = Integer.parseInt(input);
				Window.append("BASE RECEIVED: " + BASE);
			} else if (MOD == 0) {
				MOD = Integer.parseInt(input);
				Window.append("MODULUS RECEIVED: " + MOD);
				state = STATE.Main;
				sendValue();
				return;
			}
		} else if (state.equals(STATE.Main)) {
			int num = Integer.parseInt(input);
			FINAL = modPow(num, MOD);
			Window.append("\n");
			Window.append("FINISHED DIFFIE-HELLMAN KEY EXCHANGE.");
			Window.append("GENERATED " + FINAL);
		}
	}

	public void sendValue(){
		int value = modPow(BASE, MOD);
		Window.append("SENT " + value);
		try {
			iSocket.send(String.valueOf(value));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void serverProcessInput(String input) throws IOException {
		System.out.println("PROCESSING: " + "\"" + input + "\"");

		if (input.equals("QUIT")) {
			try {
				iSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (state.equals(STATE.Started)) {
			if (input.equals(BASE_REQUEST)) {
				Window.append("Client has requested public base: " + BASE);
				iSocket.send(String.valueOf(BASE));
			} else if (input.equals(MOD_REQUEST)) {
				Window.append("Cient has requested public modulus: " + MOD);
				iSocket.send(String.valueOf(MOD));
				state = STATE.Main;
				sendValue();
				return;
			}
		} else if (state.equals(STATE.Main)) {
			try {
				int num = Integer.parseInt(input);
				FINAL = modPow(num, MOD);
				Window.append("\n");
				Window.append("FINISHED DIFFIE-HELLMAN KEY EXCHANGE.");
				Window.append("GENERATED " + FINAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int modPow(int num, int mod) {

		BigInteger number = new BigInteger(String.valueOf(num));
		BigInteger modulus = new BigInteger(String.valueOf(mod));

		number = number.pow(PRIKEY).mod(modulus);

		System.out.println(number + " ^ " + PRIKEY + " = " + number + " (mod "
				+ mod + ")");
		return number.intValue();
	}

	public class ReadThread implements Runnable {

		private SocketWrapper iSocket;

		private String processed;
		private boolean go;

		public ReadThread(SocketWrapper sw) {
			iSocket = sw;
			go = true;
		}

		@Override
		public void run() {
			while (go) {
				String input;
				try {
					input = iSocket.read();
				} catch (IOException e) {
					e.printStackTrace();
					go = false;
					return;
				}

				if (processed == null || !(processed.equals(input))) {
					try {
						if (Main.clientMode) {
							clientProcessInput(input);
						} else {
							serverProcessInput(input);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
					processed = input;
				}
			}
		}

	}
}

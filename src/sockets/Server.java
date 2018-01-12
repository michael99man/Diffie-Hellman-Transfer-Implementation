package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import GUI.Window;

import main.Engine;

public class Server implements SocketWrapper {

	public ServerSocket serverSocket = null;
	public Socket client = null;
	public Engine parent;

	public PrintWriter out;
	public BufferedReader in;
	
	private Thread t;
	
	public boolean connected = false;
	
	public Server(int port) throws IOException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ex) {
			System.err.println("ERROR: Could not listen on port: " + port);
			System.exit(1);
		}
		
		ListenThread lt = new ListenThread();
		t = new Thread(lt);
		t.start();
	}

	public void setEngine(Engine e){
		parent = e;
	}
	private void start(Socket c) throws IOException {
		connected = true;
		client = c;
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		parent.start();
	}

	@Override
	public void close() throws IOException {
		out.close();
		in.close();
		serverSocket.close();
	}

	public boolean listen() {
		System.out.print("Listening...");

		System.out.println("Connected to client.");
		return true;
	}

	public Socket getClient() {
		while (!(client == null)) {
			return client;
		}
		return null;
	}

	@Override
	public String read() throws IOException {
		return in.readLine();
	}

	@Override
	public void send(String s) {
		out.println(s);
	}

	public class ListenThread implements Runnable {

		boolean done = false;

		public ListenThread(){
			Window.append("Listening...");
		}
		
		@Override
		public void run() {
			while (done == false) {
				try {
					client = serverSocket.accept();
				} catch (IOException e) {
					System.err.println("Accept failed.");
					System.exit(1);
				}
				
				if (client != null) {
					// Sends Client Socket back
					try {
						done = true;
						Window.append("CONNECTED!");
						start(client);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}

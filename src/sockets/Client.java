package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements SocketWrapper{

	//The socket to the server
	public Socket socket;
	
	
	private PrintWriter out = null;
	public BufferedReader in = null;

	public Client(String servername, int port) throws IOException {
		try {
			socket = new Socket(servername, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("I/O Failed.");
			System.exit(1);
		}
	}

	@Override
	public void close() throws IOException{
		out.close();
		in.close();
		socket.close();
	}

	
	
	public InputStream getInputStream() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	public OutputStream getOutputStream(){
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String read() throws IOException {
		return in.readLine();
	}
	
	@Override
	public void send(String s){
		out.println(s);
	}

}

package sockets;

import java.io.IOException;

public interface SocketWrapper {
	public void send(String s) throws IOException;
	public String read() throws IOException;
	public void close() throws IOException;
}

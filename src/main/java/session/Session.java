package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import rs.ac.bg.fon.poslasticarnica.Administrator;

public class Session {

	private static Session instance;
	private Socket socket;
	private Administrator ulogovani;
	private PrintWriter out; // slanje json zahteva serveru
	private BufferedReader in; // primanje json odgovora sa servera

	private Session() {
		try {
			socket = new Socket("localhost", 10000); // za komunikaciju sa serverom

			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException ex) {
			System.err.println("Greska pri uspostavljanju konekcije ili kreiranju streamova: " + ex.getMessage());
		}
	}

	public static Session getInstance() {
		if (instance == null) {
			instance = new Session();
		}
		return instance;
	}

	public PrintWriter getWriter() {
		return out;
	}

	public BufferedReader getReader() {
		return in;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setUlogovani(Administrator ulogovani) {
		this.ulogovani = ulogovani;
	}

	public Administrator getUlogovani() {
		return ulogovani;
	}

}

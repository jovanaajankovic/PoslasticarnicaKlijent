package controller;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import rs.ac.bg.fon.poslasticarnica.Administrator;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.Racun;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;
import session.Session;
import transfer.Request;
import transfer.Response;
import transfer.util.Operation;
import transfer.util.ResponseStatus;

public class ClientController {

	private static ClientController instance;
	private final Gson gson = new Gson();

	private ClientController() {
	}

	public static ClientController getInstance() {
		if (instance == null) {
			instance = new ClientController();
		}
		return instance;
	}

	public Administrator login(Administrator administrator) throws Exception {
		Object data = sendRequest(Operation.LOGIN, administrator); // vraca LinkedTreeMap objekat

		if (data == null) {
			return null;
		}

		String jsonAdmin = this.gson.toJson(data); // iz LinkedTreeMap u JSON string
		Administrator ulogovani = this.gson.fromJson(jsonAdmin, Administrator.class); // iz JSON stringa u objekat klase
																						// Administrator

		return ulogovani;
	}

	public void logout(Administrator ulogovani) throws Exception {
		sendRequest(Operation.LOGOUT, ulogovani);
	}

	public void addAdministrator(Administrator administrator) throws Exception {
		sendRequest(Operation.ADD_ADMINISTRATOR, administrator);
	}

	public void addTipPoslastice(TipPoslastice tipPoslastice) throws Exception {
		sendRequest(Operation.ADD_TIP_POSLASTICE, tipPoslastice);
	}

	public void addPoslastica(Poslastica poslastica) throws Exception {
		sendRequest(Operation.ADD_POSLASTICA, poslastica);
	}

	public void addRacun(Racun racun) throws Exception {
		sendRequest(Operation.ADD_RACUN, racun);
	}

	public void deleteAdministrator(Administrator administrator) throws Exception {
		sendRequest(Operation.DELETE_ADMINISTRATOR, administrator);
	}

	public void deleteTipPoslastice(TipPoslastice tipPoslastice) throws Exception {
		sendRequest(Operation.DELETE_TIP_POSLASTICE, tipPoslastice);
	}

	public void deletePoslastica(Poslastica poslastica) throws Exception {
		sendRequest(Operation.DELETE_POSLASTICA, poslastica);
	}

	public void updateAdministrator(Administrator administrator) throws Exception {
		sendRequest(Operation.UPDATE_ADMINISTRATOR, administrator);
	}

	public void updatePoslastica(Poslastica poslastica) throws Exception {
		sendRequest(Operation.UPDATE_POSLASTICA, poslastica);
	}

	public void updateTipPoslastice(TipPoslastice tipPoslastice) throws Exception {
		sendRequest(Operation.UPDATE_TIP_POSLASTICE, tipPoslastice);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Administrator> getAllAdministrator() throws Exception {
		Type type = new TypeToken<ArrayList<Administrator>>() {
		}.getType();
		return (ArrayList<Administrator>) sendRequest(Operation.GET_ALL_ADMINISTRATOR, null, type);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Racun> getAllRacun(Administrator a) throws Exception {
		Type type = new TypeToken<ArrayList<Racun>>() {
		}.getType();
		return (ArrayList<Racun>) sendRequest(Operation.GET_ALL_RACUN, a, type);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Poslastica> getAllPoslastica(TipPoslastice tp) throws Exception {
		Type type = new TypeToken<ArrayList<Poslastica>>() {
		}.getType();
		return (ArrayList<Poslastica>) sendRequest(Operation.GET_ALL_POSLASTICA, tp, type);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TipPoslastice> getAllTipPoslastice() throws Exception {
		Type type = new TypeToken<ArrayList<TipPoslastice>>() {
		}.getType();
		return (ArrayList<TipPoslastice>) sendRequest(Operation.GET_ALL_TIP_POSLASTICE, null, type);
	}

	private Object sendRequest(int operation, Object data) throws Exception {
		return sendRequest(operation, data, null);
	}

	private Object sendRequest(int operation, Object data, Type responseType) throws Exception {
		Gson gson = this.gson;
		Request request = new Request(operation, data); // zahtev koji se salje serveru
		String jsonRequest = gson.toJson(request); // serijalizacija

		PrintWriter out = Session.getInstance().getWriter(); // slanje zahteva
		BufferedReader in = Session.getInstance().getReader();

		if (out == null || in == null) {
			throw new IOException("Greska u konekciji: Streams nisu inicijalizovani.");
		}
		out.println(jsonRequest);

		Response response; // odgovor koji se prima sa servera
		try {
			JsonReader reader = new JsonReader(in); // primanje odgovora
			response = gson.fromJson(reader, Response.class); // deserijalizacija
		} catch (JsonSyntaxException ex) {
			throw new IOException("Greska pri citanju ili parsiranju JSON odgovora sa servera.", ex);
		}

		if (response == null || response.getResponseStatus() == null) {
			throw new IOException("Server je poslao nevazeci odgovor ili je veza prekinuta.");
		}

		if (response.getResponseStatus() == ResponseStatus.ERROR) { // ako je doslo do greske
			String serverError = response.getErrorMessage();
			if (serverError == null || serverError.isEmpty()) {
				serverError = "Nepoznata serverska greska.";
			}
			throw new Exception("Greska na serveru: " + serverError);
		} else { // uspesno dobijen odgovor
			if (responseType != null) { // za liste
				return gson.fromJson(gson.toJson(response.getData()), responseType);
			}
			return response.getData(); // za jedan objekat
		}

	}

	// ORIGINAL IZ NETBEANSA
	/*
	 * Request request = new Request(operation, data); //--ORIGINALKOD
	 * ObjectOutputStream out = new
	 * ObjectOutputStream(Session.getInstance().getSocket().getOutputStream());
	 * out.writeObject(request);
	 * 
	 * ObjectInputStream in = new
	 * ObjectInputStream(Session.getInstance().getSocket().getInputStream());
	 * Response response = (Response) in.readObject();
	 * 
	 * if (response.getResponseStatus().equals(ResponseStatus.Error)) { throw
	 * response.getException(); } else { return response.getData(); }
	 */

	public Gson getGson() {
		return gson;
	}
}

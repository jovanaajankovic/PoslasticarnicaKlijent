package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

	private ClientController() {
	}

	public static ClientController getInstance() {
		if (instance == null) {
			instance = new ClientController();
		}
		return instance;
	}

	public Administrator login(Administrator administrator) throws Exception {
		return (Administrator) sendRequest(Operation.LOGIN, administrator);
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
		return (ArrayList<Administrator>) sendRequest(Operation.GET_ALL_ADMINISTRATOR, null);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Racun> getAllRacun(Administrator a) throws Exception {
		return (ArrayList<Racun>) sendRequest(Operation.GET_ALL_RACUN, a);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Poslastica> getAllPoslastica(TipPoslastice tp) throws Exception {
		return (ArrayList<Poslastica>) sendRequest(Operation.GET_ALL_POSLASTICA, tp);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TipPoslastice> getAllTipPoslastice() throws Exception {
		return (ArrayList<TipPoslastice>) sendRequest(Operation.GET_ALL_TIP_POSLASTICE, null);
	}

	private Object sendRequest(int operation, Object data) throws Exception {
		Request request = new Request(operation, data);

		ObjectOutputStream out = new ObjectOutputStream(Session.getInstance().getSocket().getOutputStream());
		out.writeObject(request);

		ObjectInputStream in = new ObjectInputStream(Session.getInstance().getSocket().getInputStream());
		Response response = (Response) in.readObject();

		if (response.getResponseStatus().equals(ResponseStatus.Error)) {
			throw response.getException();
		} else {
			return response.getData();
		}

	}

}

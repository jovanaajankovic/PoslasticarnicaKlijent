package models;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import controller.ClientController;
import rs.ac.bg.fon.poslasticarnica.Administrator;

public class TableModelAdministratori extends AbstractTableModel implements Runnable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Administrator> lista;
	private String[] kolone = { "ID", "Ime", "Prezime", "Username" };
	private String parametar = "";

	public TableModelAdministratori() {
		try {
			lista = ClientController.getInstance().getAllAdministrator();
		} catch (Exception ex) {
			Logger.getLogger(TableModelAdministratori.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return kolone.length;
	}

	@Override
	public String getColumnName(int i) {
		return kolone[i];
	}

	@Override
	public Object getValueAt(int row, int column) {
		Administrator a = lista.get(row);

		switch (column) {
		case 0:
			return a.getAdministratorID();
		case 1:
			return a.getIme();
		case 2:
			return a.getPrezime();
		case 3:
			return a.getUsername();

		default:
			return null;
		}
	}

	public Administrator getSelectedAdministrator(int row) {
		return lista.get(row);
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Thread.sleep(10000);
				refreshTable();
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(TableModelAdministratori.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setParametar(String parametar) {
		this.parametar = parametar;
		refreshTable();
	}

	public void refreshTable() {
		try {
			lista = ClientController.getInstance().getAllAdministrator();
			if (!parametar.equals("")) {
				ArrayList<Administrator> novaLista = new ArrayList<>();
				for (Administrator a : lista) {
					if (a.getIme().toLowerCase().contains(parametar.toLowerCase())
							|| a.getPrezime().toLowerCase().contains(parametar.toLowerCase())
							|| a.getUsername().toLowerCase().contains(parametar.toLowerCase())) {
						novaLista.add(a);
					}
				}
				lista = novaLista;
			}

			fireTableDataChanged();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

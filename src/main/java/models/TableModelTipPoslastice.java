package models;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import controller.ClientController;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;

public class TableModelTipPoslastice extends AbstractTableModel implements Runnable {

	private static final long serialVersionUID = 1L;
	private ArrayList<TipPoslastice> lista;
	private String[] kolone = { "ID", "Naziv" };
	private String parametar = "";

	public TableModelTipPoslastice() {
		try {
			lista = ClientController.getInstance().getAllTipPoslastice();
		} catch (Exception ex) {
			Logger.getLogger(TableModelTipPoslastice.class.getName()).log(Level.SEVERE, null, ex);
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
		TipPoslastice tp = lista.get(row);

		switch (column) {
		case 0:
			return tp.getTipPoslasticeID();
		case 1:
			return tp.getNaziv();

		default:
			return null;
		}
	}

	public TipPoslastice getSelectedTipPoslastice(int row) {
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
			Logger.getLogger(TableModelTipPoslastice.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setParametar(String parametar) {
		this.parametar = parametar;
		refreshTable();
	}

	public void refreshTable() {
		try {
			lista = ClientController.getInstance().getAllTipPoslastice();
			if (!parametar.equals("")) {
				ArrayList<TipPoslastice> novaLista = new ArrayList<>();
				for (TipPoslastice tp : lista) {
					if (tp.getNaziv().toLowerCase().contains(parametar.toLowerCase())) {
						novaLista.add(tp);
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

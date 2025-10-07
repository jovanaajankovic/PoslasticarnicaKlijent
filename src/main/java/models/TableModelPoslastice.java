package models;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import controller.ClientController;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;

public class TableModelPoslastice extends AbstractTableModel implements Runnable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Poslastica> lista;
	private String[] kolone = { "ID", "Tip poslastice", "Naziv", "Cena po komadu" };
	private String parametar = "";

	public TableModelPoslastice() {
		try {
			lista = ClientController.getInstance().getAllPoslastica(null);
		} catch (Exception ex) {
			Logger.getLogger(TableModelPoslastice.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public TableModelPoslastice(TipPoslastice tp) {
		try {
			lista = ClientController.getInstance().getAllPoslastica(tp);
		} catch (Exception ex) {
			Logger.getLogger(TableModelPoslastice.class.getName()).log(Level.SEVERE, null, ex);
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
		Poslastica p = lista.get(row);

		switch (column) {
		case 0:
			return p.getPoslasticaID();
		case 1:
			return p.getTipPoslastice();
		case 2:
			return p.getNaziv();
		case 3:
			return p.getCenaPoKomadu() + "din";

		default:
			return null;
		}
	}

	public Poslastica getSelectedPoslastica(int row) {
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
			Logger.getLogger(TableModelPoslastice.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setParametar(String parametar) {
		this.parametar = parametar;
		refreshTable();
	}

	public void refreshTable() {
		try {
			lista = ClientController.getInstance().getAllPoslastica(null);
			if (!parametar.equals("")) {
				ArrayList<Poslastica> novaLista = new ArrayList<>();
				for (Poslastica p : lista) {
					if (p.getNaziv().toLowerCase().contains(parametar.toLowerCase())) {
						novaLista.add(p);
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

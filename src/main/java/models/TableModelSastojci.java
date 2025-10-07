package models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import rs.ac.bg.fon.poslasticarnica.Sastojak;

public class TableModelSastojci extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Sastojak> lista;
	private String[] kolone = { "Rb", "Sastojak" };
	private int rb = 0;

	public TableModelSastojci() {
		lista = new ArrayList<>();
	}

	public TableModelSastojci(ArrayList<Sastojak> sastojci) {
		lista = sastojci;
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
		Sastojak s = lista.get(row);

		switch (column) {
		case 0:
			return s.getRb();
		case 1:
			return s.getNaziv();

		default:
			return null;
		}
	}

	public void dodajSastojak(Sastojak s) {
		rb = lista.size();
		s.setRb(++rb);
		lista.add(s);
		fireTableDataChanged();
	}

	public void obrisiSastojak(int row) {
		lista.remove(row);

		int rb = 0;
		for (Sastojak sastojak : lista) {
			sastojak.setRb(++rb);
		}

		fireTableDataChanged();
	}

	public ArrayList<Sastojak> getLista() {
		return lista;
	}

	public boolean postojiSastojak(Sastojak s) {
		for (Sastojak sastojak : lista) {
			if (sastojak.getNaziv().equals(s.getNaziv())) {
				return true;
			}
		}
		return false;
	}

}

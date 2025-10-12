package form_poslastica;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import controller.ClientController;
import forms.MainForm;
import models.TableModelSastojci;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.Sastojak;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;

public class FornNovaPoslastica extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNaziv;
	private JTable tblSastojci = new JTable();
	private JFormattedTextField txtCenaPoKom = new JFormattedTextField();
	private JTextArea txtOpis;
	private JComboBox<TipPoslastice> cmbTipPoslastice = new JComboBox<TipPoslastice>();
	private JComboBox<String> cmbSastojak = new JComboBox<String>();
	private JButton btnObrisiSastojak;
	private JButton btnDodajSastojak;
	private JButton btnOtkazi;
	private JButton btnDodajPoslasticu;

	/**
	 * Create the dialog.
	 */
	public FornNovaPoslastica(Frame parent, boolean modal) {
		super(parent, modal);
		setLocationRelativeTo(null);
		setTitle("Unos poslastice");

		popuniTipovePoslastica();
		tblSastojci.setModel(new TableModelSastojci());

		setBounds(100, 100, 515, 621);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel jLabel9 = new JLabel("Tip poslastice:");
			jLabel9.setBounds(10, 38, 120, 23);
			contentPanel.add(jLabel9);
		}
		{
			JLabel jLabel2 = new JLabel("Naziv:");
			jLabel2.setBounds(10, 72, 81, 23);
			contentPanel.add(jLabel2);
		}
		{
			JLabel jLabel3 = new JLabel("Cena:");
			jLabel3.setBounds(10, 105, 130, 23);
			contentPanel.add(jLabel3);
		}
		{
			JLabel jLabel8 = new JLabel("Opis:");
			jLabel8.setBounds(10, 138, 81, 23);
			contentPanel.add(jLabel8);
		}
		{
			cmbTipPoslastice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			cmbTipPoslastice.setBounds(143, 39, 238, 20);
			contentPanel.add(cmbTipPoslastice);
		}
		{
			txtNaziv = new JTextField();
			txtNaziv.setBounds(143, 77, 238, 18);
			contentPanel.add(txtNaziv);
			txtNaziv.setColumns(10);
		}

		txtCenaPoKom.setBounds(143, 107, 238, 18);
		contentPanel.add(txtCenaPoKom);

		txtOpis = new JTextArea();
		txtOpis.setBounds(143, 137, 238, 88);
		contentPanel.add(txtOpis);

		JLabel jLabel1 = new JLabel("Sastojak:");
		jLabel1.setBounds(84, 272, 81, 23);
		contentPanel.add(jLabel1);

		cmbSastojak.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Cokolada", "Keks", "Margarin", "Jaja", "Orasi", "Plazma", "Fanta", "Jaffa",
						"Testo od krompira", "Pistaci", "Sljive", "Ananas", "Kivi", "Nutela", "Maline" }));
		cmbSastojak.setBounds(191, 273, 190, 20);
		contentPanel.add(cmbSastojak);

		btnObrisiSastojak = new JButton("Obrisi sastojak");
		btnObrisiSastojak.addActionListener(this::btnObrisiSastojakActionPerformed);
		btnObrisiSastojak.setBounds(267, 305, 190, 20);
		contentPanel.add(btnObrisiSastojak);

		btnDodajSastojak = new JButton("Dodaj sastojak");
		btnDodajSastojak.addActionListener(this::btnDodajSastojakActionPerformed);
		btnDodajSastojak.setBounds(58, 305, 190, 20);
		contentPanel.add(btnDodajSastojak);

		tblSastojci.setBounds(45, 362, 410, 153);
		contentPanel.add(tblSastojci);

		btnOtkazi = new JButton("Zatvori");
		btnOtkazi.addActionListener(this::btnOtkaziActionPerformed);
		btnOtkazi.setBounds(345, 541, 84, 20);
		contentPanel.add(btnOtkazi);

		btnDodajPoslasticu = new JButton("Dodaj poslasticu");
		btnDodajPoslasticu.addActionListener(this::btnDodajPoslasticuActionPerformed);
		btnDodajPoslasticu.setBounds(45, 541, 149, 20);
		contentPanel.add(btnDodajPoslasticu);

		podesiCenaPoKomaduFormatter();
	}

	private void popuniTipovePoslastica() {
		try {
			ArrayList<TipPoslastice> tipoviPoslastica = ClientController.getInstance().getAllTipPoslastice();

			cmbTipPoslastice.removeAllItems();

			for (TipPoslastice tipPoslastice : tipoviPoslastica) {
				cmbTipPoslastice.addItem(tipPoslastice);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greska pri ucitavanju tipova poslastica: " + ex.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
			Logger.getLogger(FornNovaPoslastica.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void podesiCenaPoKomaduFormatter() {
		try {
			NumberFormat doubleFormat = NumberFormat.getNumberInstance(Locale.US);
			if (doubleFormat instanceof DecimalFormat) {
				((DecimalFormat) doubleFormat).setGroupingUsed(false);
				((DecimalFormat) doubleFormat).setMaximumFractionDigits(2);
				((DecimalFormat) doubleFormat).setMinimumFractionDigits(2);
			}

			NumberFormatter formatter = new NumberFormatter(doubleFormat);
			formatter.setValueClass(Double.class);
			formatter.setAllowsInvalid(false);
			formatter.setMinimum(0.0);

			DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);

			txtCenaPoKom.setFormatterFactory(factory);

			txtCenaPoKom.setValue(300.00);

		} catch (Exception e) {
			Logger.getLogger(FornNovaPoslastica.class.getName()).log(Level.SEVERE,
					"Greska pri podesavanju formatera za cenu.", e);
		}
	}

	private void btnOtkaziActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnDodajPoslasticuActionPerformed(ActionEvent evt) {
		try {
			if (txtNaziv.getText().isEmpty() || txtOpis.getText().isEmpty() || txtCenaPoKom.getValue() == null) {
				JOptionPane.showMessageDialog(this, "Sva tekstualna polja moraju biti popunjena!");
				return;
			}

			String naziv = txtNaziv.getText();
			String opis = txtOpis.getText();

			double cenaPoKomadu = (Double) txtCenaPoKom.getValue();

			TipPoslastice tipPoslastice = (TipPoslastice) cmbTipPoslastice.getSelectedItem();

			TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();

			if (tm.getLista().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Poslastica mora imati barem jedan sastojak!");
				return;
			}

			Poslastica poslastica = new Poslastica(null, naziv, cenaPoKomadu, opis, tipPoslastice, tm.getLista());

			ClientController.getInstance().addPoslastica(poslastica);

			if (getParent() instanceof MainForm) {
				MainForm mf = (MainForm) getParent();
				mf.popuniPoslastice();
			}

			JOptionPane.showMessageDialog(this, "Uspesno dodata poslastica.");
			this.dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greska pri dodavanju poslastice: " + ex.getMessage(), "Greska servera",
					JOptionPane.ERROR_MESSAGE);
			Logger.getLogger(FornNovaPoslastica.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void btnDodajSastojakActionPerformed(ActionEvent evt) {
		String nazivSastojka = (String) cmbSastojak.getSelectedItem();

		if (nazivSastojka == null || nazivSastojka.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Morate izabrati sastojak!", "Greska unosa", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Sastojak s = new Sastojak(-1, nazivSastojka);

		TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();

		if (tm.postojiSastojak(s)) {
			JOptionPane.showMessageDialog(this, "Vec ste uneli ovaj sastojak!", "Greska unosa",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		tm.dodajSastojak(s);
	}

	private void btnObrisiSastojakActionPerformed(ActionEvent evt) {
		int row = tblSastojci.getSelectedRow();

		if (row >= 0) {
			TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();
			tm.obrisiSastojak(row);
		} else {
			JOptionPane.showMessageDialog(this, "Izaberite sastojak za brisanje!");
		}
	}
}

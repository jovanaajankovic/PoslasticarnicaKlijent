package forms;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ClientController;
import form_poslastica.FornNovaPoslastica;
import models.TableModelStavke;
import rs.ac.bg.fon.poslasticarnica.Administrator;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.Racun;
import rs.ac.bg.fon.poslasticarnica.StavkaRacuna;
import session.Session;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable tblStavke = new JTable();
	private JTextField txtKolicina;
	private JTextField txtCenaStavke = new JTextField();
	private JLabel lblUlogovani = new JLabel("Ulogovani administrator:");
	private JButton btnOdjava;
	private JComboBox<Poslastica> cmbPoslastica = new JComboBox<>();
	private JLabel lblCena;
	private JButton btnSacuvaj;

	private Administrator ulogovani;
	private double cena;

	/**
	 * Create the frame.
	 */
	public MainForm() {
		setLocationRelativeTo(null);
		this.ulogovani = Session.getInstance().getUlogovani();
		lblUlogovani.setText("Ulogovani administrator: " + ulogovani);
		setTitle("Klijentska forma");
		popuniPoslastice();

		txtCenaStavke.setEditable(false);
		tblStavke.setModel(new TableModelStavke());

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				izvrsiOdjavu();
			}
		});
		setBounds(100, 100, 849, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 100, 21);
		contentPane.add(menuBar);

		JMenu jMenu6 = new JMenu("Poslastica");
		menuBar.add(jMenu6);

		JMenuItem miNovaPoslastica = new JMenuItem("Nova poslastica");
		miNovaPoslastica.addActionListener(this::miNovaPoslasticaActionPerformed);
		jMenu6.add(miNovaPoslastica);

		lblUlogovani.setBounds(10, 66, 354, 21);
		contentPane.add(lblUlogovani);

		btnOdjava = new JButton("Odjava");
		btnOdjava.setBounds(569, 66, 84, 20);
		btnOdjava.addActionListener(this::btnOdjavaActionPerformed);
		contentPane.add(btnOdjava);

		tblStavke.setBounds(10, 121, 424, 207);
		contentPane.add(tblStavke);

		JLabel jLabel2 = new JLabel("Poslastica:");
		jLabel2.setBounds(444, 135, 84, 26);
		contentPane.add(jLabel2);

		JLabel jLabel3 = new JLabel("Kolicina:");
		jLabel3.setBounds(444, 171, 84, 26);
		contentPane.add(jLabel3);

		JLabel jLabel4 = new JLabel("Cena:");
		jLabel4.setBounds(444, 222, 84, 26);
		contentPane.add(jLabel4);

		cmbPoslastica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		cmbPoslastica.addItemListener(this::cmbPoslasticaItemStateChanged);
		cmbPoslastica.setBounds(511, 138, 303, 20);
		contentPane.add(cmbPoslastica);

		txtKolicina = new JTextField();
		txtKolicina.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtKolicinaKeyReleased(e);
			}
		});
		txtKolicina.setBounds(511, 179, 303, 18);
		contentPane.add(txtKolicina);
		txtKolicina.setColumns(10);

		txtCenaStavke.setBounds(511, 226, 303, 18);
		contentPane.add(txtCenaStavke);
		txtCenaStavke.setColumns(10);

		JButton btnDodaj = new JButton("Dodaj stavku");
		btnDodaj.addActionListener(this::btnDodajActionPerformed);
		btnDodaj.setBounds(456, 269, 358, 20);
		contentPane.add(btnDodaj);

		JButton btnObrisi = new JButton("Obrisi stavku");
		btnObrisi.addActionListener(this::btnObrisiActionPerformed);
		btnObrisi.setBounds(456, 308, 358, 20);
		contentPane.add(btnObrisi);

		JLabel jLabel1 = new JLabel("Cena:");
		jLabel1.setBounds(235, 356, 84, 21);
		contentPane.add(jLabel1);

		lblCena = new JLabel("0.00");
		lblCena.setBounds(510, 356, 143, 21);
		contentPane.add(lblCena);

		btnSacuvaj = new JButton("Sacuvaj racun");
		btnSacuvaj.addActionListener(this::btnSacuvajActionPerformed);
		btnSacuvaj.setBounds(296, 426, 268, 64);
		contentPane.add(btnSacuvaj);

	}

	public void popuniPoslastice() {
		try {
			ArrayList<Poslastica> poslastice = ClientController.getInstance().getAllPoslastica(null);

			cmbPoslastica.removeAllItems();

			for (Poslastica poslastica : poslastice) {
				cmbPoslastica.addItem(poslastica);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greska pri ucitavanju poslastica: " + ex.getMessage(),
					"Greska komunikacije", JOptionPane.ERROR_MESSAGE);
			Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void izvrsiOdjavu() {
		int response = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da zelite da se odjavite?",
				"Potvrda odjave", JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.YES_OPTION) {
			try {
				ClientController.getInstance().logout(ulogovani);
				System.exit(0);
			} catch (Exception ex) {
				Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, "Greska pri odjavi (JSON komunikacija):",
						ex);
				System.exit(0);
			}
		}

	}

	private void resetujFormu() {
		txtKolicina.setText("");
		txtCenaStavke.setText("");
		lblCena.setText("0.00din");
		cena = 0;
		TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
		tm.getLista().clear();
		tm.fireTableDataChanged();
	}

	private void btnOdjavaActionPerformed(java.awt.event.ActionEvent evt) {
		izvrsiOdjavu();
	}

	private void miNovaPoslasticaActionPerformed(java.awt.event.ActionEvent evt) {
		new FornNovaPoslastica(this, true).setVisible(true);
	}

	private void btnSacuvajActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			TableModelStavke tm = (TableModelStavke) tblStavke.getModel();

			if (tm.getLista().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Racun mora sadrzati bar jednu stavku!", "Validacija",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			Racun r = new Racun(null, new Date(), cena, ulogovani, tm.getLista());
			ClientController.getInstance().addRacun(r);
			resetujFormu();

			JOptionPane.showMessageDialog(this, "Uspesno sacuvan racun!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greska pri cuvanju racuna: " + ex.getMessage(), "Greska servera",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void btnDodajActionPerformed(java.awt.event.ActionEvent evt) {
		if (cmbPoslastica.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Morate izabrati poslasticu!");
			return;
		}

		if (txtKolicina.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Kolicina mora biti popunjena!");
			return;
		}

		try {
			Poslastica p = (Poslastica) cmbPoslastica.getSelectedItem();
			int kolicina = Integer.parseInt(txtKolicina.getText());
			double cenaStavke = Double.parseDouble(txtCenaStavke.getText());

			if (kolicina <= 0) {
				JOptionPane.showMessageDialog(this, "Kolicina mora biti veca od nule!");
				return;
			}

			StavkaRacuna sr = new StavkaRacuna(null, -1, kolicina, cenaStavke, p);

			TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
			tm.dodajStavku(sr);

			cena = tm.vratiCenuRacuna();
			lblCena.setText(String.valueOf(cena) + "din");

			txtKolicina.setText("");
			txtCenaStavke.setText("");

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Kolicina mora biti broj!");
		}

	}

	private void btnObrisiActionPerformed(java.awt.event.ActionEvent evt) {
		int row = tblStavke.getSelectedRow();

		if (row >= 0) {
			TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
			tm.obrisiStavku(row);

			cena = tm.vratiCenuRacuna();
			lblCena.setText(String.valueOf(cena) + "din");
		} else {
			JOptionPane.showMessageDialog(this, "Izaberite stavku za brisanje!");
		}

	}

	private void txtKolicinaKeyReleased(java.awt.event.KeyEvent evt) {
		// provera da li je uneta cifra, inace resetuje polja
		if (!(evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9')) {
			if (!txtKolicina.getText().isEmpty()) {
				txtKolicina.setText("");
				txtCenaStavke.setText("");
			}
			return;
		}

		if (cmbPoslastica.getSelectedItem() == null || txtKolicina.getText().isEmpty()) {
			txtCenaStavke.setText("");
			return;
		}

		try {
			Poslastica p = (Poslastica) cmbPoslastica.getSelectedItem();
			int kolicina = Integer.parseInt(txtKolicina.getText());

			if (kolicina == 0) {
				txtKolicina.setText("");
				txtCenaStavke.setText("");
				return;
			}

			txtCenaStavke.setText(String.valueOf(p.getCenaPoKomadu() * kolicina));
		} catch (NumberFormatException e) {
			txtKolicina.setText("");
			txtCenaStavke.setText("");
		}

	}

	private void cmbPoslasticaItemStateChanged(java.awt.event.ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (cmbPoslastica.getSelectedItem() != null && !txtKolicina.getText().isEmpty()) {
				try {
					Poslastica p = (Poslastica) cmbPoslastica.getSelectedItem();
					int kolicina = Integer.parseInt(txtKolicina.getText());

					if (kolicina == 0) {
						txtCenaStavke.setText("");
						return;
					}

					txtCenaStavke.setText(String.valueOf(p.getCenaPoKomadu() * kolicina));
				} catch (NumberFormatException e) {
					txtCenaStavke.setText("");
				}
			} else {
				txtCenaStavke.setText("");
			}
		}
	}

}

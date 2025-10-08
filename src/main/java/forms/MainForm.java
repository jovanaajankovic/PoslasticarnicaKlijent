package forms;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ClientController;
import form_poslastica.FormNovaPoslastica;
import models.TableModelStavke;
import rs.ac.bg.fon.poslasticarnica.Administrator;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.Racun;
import rs.ac.bg.fon.poslasticarnica.StavkaRacuna;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;
import session.Session;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class MainForm extends JFrame {

	 private static final long serialVersionUID = 1L;
	    private JPanel contentPane;
	    private JTable tblStavke;
	    private JTextField txtKolicina;
	    private JTextField txtCenaStavke;
	    private JComboBox<Poslastica> cmbPoslastica;
	    private JLabel lblUlogovani;
	    private JLabel lblCena;
	    private JButton btnDodaj, btnObrisi, btnSacuvaj, btnOdjava;
	    private JMenuItem miNoviAdmin, miPretragaAdmina, miNoviTip, miPretragaTipa,
	            miNovaPoslastica, miPretragaPoslastice, miPretragaRacuna;
	    private Administrator ulogovani;
	    private double cena;

	
	/**
	 * Create the frame.
	 */
	public MainForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 667, 459);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu jMenu6 = new JMenu("Administrator");
		menuBar.add(jMenu6);
		
		miNoviAdmin = new JMenuItem("Novi admin");
		jMenu6.add(miNoviAdmin);
		
		miPretragaAdmina = new JMenuItem("Pretraga admina");
		jMenu6.add(miPretragaAdmina);
		
		JMenu jMenu9 = new JMenu("Tip poslastice");
		menuBar.add(jMenu9);
		
		miNoviTip = new JMenuItem("Novi tip poslastice");
		jMenu9.add(miNoviTip);
		
		miPretragaTipa = new JMenuItem("Pretraga tipa poslastice");
		jMenu9.add(miPretragaTipa);
		
		JMenu jMenu7 = new JMenu("Poslastica");
		menuBar.add(jMenu7);
		
		miNovaPoslastica = new JMenuItem("Nova poslastica");
		jMenu7.add(miNovaPoslastica);
		
		miPretragaPoslastice = new JMenuItem("Pretraga poslastice");
		jMenu7.add(miPretragaPoslastice);
		
		JMenu jMenu10 = new JMenu("Racun");
		menuBar.add(jMenu10);
		
		miPretragaRacuna = new JMenuItem("Pretraga racuna");
		jMenu10.add(miPretragaRacuna);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUlogovani = new JLabel("Ulogovani:");
		lblUlogovani.setBounds(24, 38, 327, 18);
		contentPane.add(lblUlogovani);
		
		tblStavke = new JTable();
		tblStavke.setBounds(10, 86, 382, 220);
		tblStavke.setModel(new TableModelStavke());
		contentPane.add(tblStavke);
		
		btnOdjava = new JButton("Odjava");
		btnOdjava.setBounds(491, 38, 84, 20);
		contentPane.add(btnOdjava);
		btnOdjava.addActionListener(e -> odjava());
		
		JLabel jLabel2 = new JLabel("Poslastica:");
		jLabel2.setBounds(402, 87, 84, 12);
		contentPane.add(jLabel2);
		
		JLabel jLabel3 = new JLabel("Kolicina:");
		jLabel3.setBounds(402, 127, 84, 12);
		contentPane.add(jLabel3);
		
		JLabel jLabel4 = new JLabel("Cena:");
		jLabel4.setBounds(402, 164, 84, 12);
		contentPane.add(jLabel4);
		
		cmbPoslastica = new JComboBox();
		cmbPoslastica.setBounds(536, 83, 95, 20);
		contentPane.add(cmbPoslastica);
		cmbPoslastica.addItemListener(e -> updateCenaStavke());
		
		txtKolicina = new JTextField();
		txtKolicina.setBounds(535, 124, 96, 18);
		contentPane.add(txtKolicina);
		txtKolicina.setColumns(10);
		txtKolicina.addKeyListener(new KeyAdapter() {
	            public void keyReleased(KeyEvent e) {
	                updateCenaStavke();
	            }
	        });
		
		txtCenaStavke = new JTextField();
		txtCenaStavke.setBounds(535, 161, 96, 18);
		contentPane.add(txtCenaStavke);
		txtCenaStavke.setColumns(10);
		txtCenaStavke.setEditable(false);
		
		btnDodaj = new JButton("Dodaj stavku");
		btnDodaj.setBounds(402, 215, 219, 20);
		contentPane.add(btnDodaj);
		btnDodaj.addActionListener(e -> dodajStavku());
		
		btnObrisi = new JButton("Obrisi stavku");
		btnObrisi.setBounds(402, 259, 219, 20);
		contentPane.add(btnObrisi);
		btnObrisi.addActionListener(e -> obrisiStavku());
		
		JLabel jLabel1 = new JLabel("Cena:");
		jLabel1.setBounds(231, 328, 84, 18);
		contentPane.add(jLabel1);
		
		lblCena = new JLabel("0.00din");
		lblCena.setBounds(375, 331, 84, 12);
		contentPane.add(lblCena);
		
		btnSacuvaj = new JButton("Sacuvaj racun");
		btnSacuvaj.setBounds(231, 370, 188, 20);
		contentPane.add(btnSacuvaj);
		btnSacuvaj.addActionListener(e -> sacuvajRacun());

        initMenu();
        
     // inicijalizacija
        this.ulogovani = Session.getInstance().getUlogovani();
        lblUlogovani.setText("Ulogovani administrator: " + ulogovani);
        popuniPoslastice();

        // window listener za zatvaranje
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                odjava();
            }
        });

	}

	private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu jMenu7 = new JMenu("Poslastica");
        menuBar.add(jMenu7);
        miNovaPoslastica = new JMenuItem("Nova poslastica");
        miNovaPoslastica.addActionListener(e -> new FormNovaPoslastica(this, true).setVisible(true));
        jMenu7.add(miNovaPoslastica);
    }

    private void updateCenaStavke() {
        try {
            Poslastica p = (Poslastica) cmbPoslastica.getSelectedItem();
            if (p == null || txtKolicina.getText().isEmpty()) {
                txtCenaStavke.setText("");
                return;
            }
            int kolicina = Integer.parseInt(txtKolicina.getText());
            if (kolicina <= 0) {
                txtKolicina.setText("");
                txtCenaStavke.setText("");
                return;
            }
            txtCenaStavke.setText(String.valueOf(p.getCenaPoKomadu() * kolicina));
        } catch (NumberFormatException ex) {
            txtKolicina.setText("");
            txtCenaStavke.setText("");
        }
    }

    private void dodajStavku() {
        if (txtKolicina.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kolicina mora biti popunjena!");
            return;
        }
        Poslastica p = (Poslastica) cmbPoslastica.getSelectedItem();
        int kolicina = Integer.parseInt(txtKolicina.getText());
        double cenaStavke = Double.parseDouble(txtCenaStavke.getText());
        StavkaRacuna sr = new StavkaRacuna(null, -1, kolicina, cenaStavke, p);

        TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
        tm.dodajStavku(sr);

        cena = tm.vratiCenuRacuna();
        lblCena.setText(String.valueOf(cena) + "din");
    }

    private void obrisiStavku() {
        int row = tblStavke.getSelectedRow();
        if (row >= 0) {
            TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
            tm.obrisiStavku(row);
            cena = tm.vratiCenuRacuna();
            lblCena.setText(String.valueOf(cena) + "din");
        }
    }

    private void sacuvajRacun() {
        try {
            TableModelStavke tm = (TableModelStavke) tblStavke.getModel();
            Racun r = new Racun(null, new Date(), cena, ulogovani, tm.getLista());
            ClientController.getInstance().addRacun(r);
            resetujFormu();
            JOptionPane.showMessageDialog(this, "Uspesno sacuvan racun!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void odjava() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Da li ste sigurni da zelite da se odjavite?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                ClientController.getInstance().logout(ulogovani);
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void popuniPoslastice() {
        try {
            ArrayList<Poslastica> poslastice = ClientController.getInstance().getAllPoslastica(null);
            cmbPoslastica.removeAllItems();
            for (Poslastica poslastica : poslastice) {
                // poslastica.getTipPoslastice() NE sme biti null
                if (poslastica.getTipPoslastice() == null) {
                    // Ako je tip null, preskoči tu poslasticu ili prikupi sve tipove iz baze
                    continue;
                }
                cmbPoslastica.addItem(poslastica);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
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
}

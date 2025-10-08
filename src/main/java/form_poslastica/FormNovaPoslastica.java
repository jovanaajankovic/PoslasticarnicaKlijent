package form_poslastica;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ClientController;
import forms.MainForm;
import models.TableModelSastojci;
import rs.ac.bg.fon.poslasticarnica.Poslastica;
import rs.ac.bg.fon.poslasticarnica.Sastojak;
import rs.ac.bg.fon.poslasticarnica.TipPoslastice;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

public class FormNovaPoslastica extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNaziv;
	private JTable tblSastojci;
	private JComboBox<TipPoslastice> cmbTipPoslastice;

	

	/**
	 * Create the dialog.
	 */
	public FormNovaPoslastica(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
        setLocationRelativeTo(null);
        setTitle("Unos poslastice");
        popuniTipovePoslastica();
        tblSastojci.setModel(new TableModelSastojci());
		setBounds(100, 100, 576, 666);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel jLabel9 = new JLabel("Tip poslastice:");
			jLabel9.setBounds(10, 29, 97, 26);
			contentPanel.add(jLabel9);
		}
		{
			JLabel jLabel2 = new JLabel("Naziv:");
			jLabel2.setBounds(10, 65, 97, 26);
			contentPanel.add(jLabel2);
		}
		{
			JLabel jLabel3 = new JLabel("Cena po komadu:");
			jLabel3.setBounds(10, 105, 97, 26);
			contentPanel.add(jLabel3);
		}
		{
			JLabel jLabel8 = new JLabel("Opis:");
			jLabel8.setBounds(10, 141, 97, 26);
			contentPanel.add(jLabel8);
		}
		
		cmbTipPoslastice = new JComboBox();
		cmbTipPoslastice.setBounds(202, 32, 192, 20);
		contentPanel.add(cmbTipPoslastice);
		
		txtNaziv = new JTextField();
		txtNaziv.setBounds(202, 69, 192, 18);
		contentPanel.add(txtNaziv);
		txtNaziv.setColumns(10);
		
		JFormattedTextField txtCenaPoKom = new JFormattedTextField();
		txtCenaPoKom.setBounds(202, 109, 192, 18);
		contentPanel.add(txtCenaPoKom);
		
		JTextArea txtOpis = new JTextArea();
		txtOpis.setBounds(202, 142, 192, 61);
		contentPanel.add(txtOpis);
		
		JLabel jLabel1 = new JLabel("Sastojak:");
		jLabel1.setBounds(88, 250, 78, 20);
		contentPanel.add(jLabel1);
		
		JComboBox<Sastojak> cmbSastojak = new JComboBox();
		cmbSastojak.setModel(new DefaultComboBoxModel(new String[] {"Cokolada", "Keks", "Margarin", "Jaja", "Orasi", "Plazma", "Fanta", "Jaffa", "Testo od krompira", "Pistaci", "Sljive", "Ananas", "Kivi", "Nutela", "Maline"}));
		cmbSastojak.setBounds(202, 250, 192, 20);
		contentPanel.add(cmbSastojak);
		
		JButton btnDodajSastojak = new JButton("Dodaj sastojak");
		btnDodajSastojak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nazivSastojka = (String) cmbSastojak.getSelectedItem();

		        Sastojak s = new Sastojak(null, -1, nazivSastojka);

		        TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();
		        
		        if(tm.postojiSastojak(s)){
		            JOptionPane.showMessageDialog(FormNovaPoslastica.this, "Vec ste uneli ovaj sastojak!");
		            return;
		        }
		        
		        tm.dodajSastojak(s);
			}
		});
		btnDodajSastojak.setBounds(85, 291, 117, 20);
		contentPanel.add(btnDodajSastojak);
		
		JButton btnObrisiSastojak = new JButton("Obrisi sastojak");
		btnObrisiSastojak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  int row = tblSastojci.getSelectedRow();

			        if (row >= 0) {
			            TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();
			            tm.obrisiSastojak(row);
			        }

			}
		});
		btnObrisiSastojak.setBounds(246, 291, 148, 20);
		contentPanel.add(btnObrisiSastojak);
		
		tblSastojci = new JTable();
		tblSastojci.setBounds(44, 346, 419, 193);
		contentPanel.add(tblSastojci);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnDodajPoslasticu = new JButton("Dodaj poslasticu");
				btnDodajPoslasticu.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
				            if (txtCenaPoKom.getText().isEmpty() || txtNaziv.getText().isEmpty()
				                    || txtOpis.getText().isEmpty()) {
				                JOptionPane.showMessageDialog(FormNovaPoslastica.this, "Sva polja moraju biti popunjena!");
				                return;
				            }

				            String naziv = txtNaziv.getText();
				            String opis = txtOpis.getText();
				            TipPoslastice tipPoslastice = (TipPoslastice) cmbTipPoslastice.getSelectedItem();
				            double cenaPoKomadu = Double.parseDouble(txtCenaPoKom.getText());

				            TableModelSastojci tm = (TableModelSastojci) tblSastojci.getModel();

				            Poslastica poslastica = new Poslastica(null, naziv, cenaPoKomadu, opis,
				                    tipPoslastice, tm.getLista());

				            ClientController.getInstance().addPoslastica(poslastica);
				            MainForm mf = (MainForm) getParent();
				            mf.popuniPoslastice();
				            JOptionPane.showMessageDialog(FormNovaPoslastica.this, "Uspesno dodata poslastica.");
				            dispose();

				        } catch (Exception ex) {
				            JOptionPane.showMessageDialog(FormNovaPoslastica.this, ex.getMessage());
				        }
					}
				});
				btnDodajPoslasticu.setActionCommand("OK");
				buttonPane.add(btnDodajPoslasticu);
				getRootPane().setDefaultButton(btnDodajPoslasticu);
			}
			{
				JButton btnOtkazi = new JButton("Otkazi");
				btnOtkazi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnOtkazi.setActionCommand("Cancel");
				buttonPane.add(btnOtkazi);
			}
		}
	}



	private void popuniTipovePoslastica() {
		try {
            ArrayList<TipPoslastice> tipoviPoslastica = ClientController.getInstance().getAllTipPoslastice();

            cmbTipPoslastice.removeAllItems();

            for (TipPoslastice tipPoslastice : tipoviPoslastica) {
                cmbTipPoslastice.addItem(tipPoslastice);
            }

        } catch (Exception ex) {
            Logger.getLogger(FormNovaPoslastica.class.getName()).log(Level.SEVERE, null, ex);
        }
		
	}
}

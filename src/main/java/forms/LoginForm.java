package forms;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ClientController;
import rs.ac.bg.fon.poslasticarnica.Administrator;
import session.Session;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginForm() {
		setTitle("Login forma");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 346, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 23, 86, 13);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 62, 86, 13);
		contentPane.add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setBounds(116, 20, 151, 19);
		txtUsername.setText("jovana123");
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(114, 59, 153, 19);
		contentPane.add(txtPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(10, 105, 86, 21);
		contentPane.add(btnLogin);

		JButton btnCancel = new JButton("Otkazi");
		btnCancel.setBounds(167, 105, 100, 21);
		contentPane.add(btnCancel);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 String username = txtUsername.getText().trim();
					    String password = new String(txtPassword.getPassword()).trim();

					    if (username.isEmpty() || password.isEmpty()) {
					        JOptionPane.showMessageDialog(LoginForm.this,
					                "Korisničko ime i lozinka moraju biti popunjeni!");
					        return;
					    }

					    Administrator a = new Administrator();
					    a.setUsername(username);
					    a.setPassword(password);

					    Administrator administrator = ClientController.getInstance().login(a);
					    Session.getInstance().setUlogovani(administrator);

					    new MainForm().setVisible(true);
					    dispose();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(LoginForm.this, ex.getMessage());
				}
			}
		});

		btnCancel.addActionListener(e -> dispose());

	}
}

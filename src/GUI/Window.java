package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import main.Main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Window extends JFrame {

	private static final long serialVersionUID = 7813054481987747538L;
	private JPanel contentPane;

	public static JTextArea textArea;
	private Window instance = this;
	private static JTextField base;
	private static JTextField mod;
	private static JTextField privateKey;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Window() {
		setTitle("DIFFIE-HELLMAN KEY EXCHANGE");
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JButton serverButton = new JButton("START SERVER");
		serverButton.setSize(225, 100);
		serverButton.setLocation(20, 10);
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance.start(false);
			}
		});
		contentPane.add(serverButton, BorderLayout.WEST);

		JButton clientButton = new JButton("CONNECT TO SERVER");
		clientButton.setSize(231, 100);
		clientButton.setLocation(250, 10);
		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance.start(true);
			}
		});

		contentPane.add(clientButton, BorderLayout.EAST);

		base = new JTextField();
		base.setText("BASE");
		base.setBounds(20, 179, 134, 28);
		contentPane.add(base);
		base.setColumns(10);

		mod = new JTextField();
		mod.setText("MOD");
		mod.setBounds(20, 219, 134, 28);
		contentPane.add(mod);
		mod.setColumns(10);

		privateKey = new JTextField();
		privateKey.setText("Private Key");
		privateKey.setBounds(265, 179, 134, 28);
		contentPane.add(privateKey);
		privateKey.setColumns(10);

		JLabel lblOnlyForRunning = new JLabel("ONLY FOR RUNNING SERVER");
		lblOnlyForRunning.setBounds(20, 151, 177, 28);
		contentPane.add(lblOnlyForRunning);

		JLabel lblMustHave = new JLabel("MUST HAVE");
		lblMustHave.setBounds(278, 157, 81, 16);
		contentPane.add(lblMustHave);
	}

	private void start(boolean cm) {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		textArea = new JTextArea();
		textArea.setBounds(10, 10, 480, 280);
		contentPane.add(textArea);

		try {
			if (!cm) {
				Main.start(cm, Integer.parseInt(base.getText()),
						Integer.parseInt(mod.getText()), Integer.parseInt(privateKey.getText()));
			} else {
				Main.start(cm, 0, 0, Integer.parseInt(privateKey.getText()));
			}

		} catch (IOException e) {
			return;
		}
	}

	public static void append(String s) {
		textArea.append(s);
		textArea.append("\n");
	}
}

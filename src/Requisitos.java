import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

class Requisitos extends JFrame {
	JLabel titleLabel;
	JButton okButton;
	JList requisitosLista;
	JScrollPane scrollPane;
	JPanel mainPanel, listaPanel, buttonsPanel;
	FlowLayout buttonsFlowLayout;
	GridBagLayout mainGridBagLayout;
	JFrame parent;
	Connection conn;
	Menu_Semanal menuSemanal;
	DefaultListModel modeloLista;
	ArrayList<String> requisitosTemporalesNoCumplidos, requisitosCaloricosNoCumplidos;

	public Requisitos(Connection conn, JFrame parent, Menu_Semanal menuSemanal) {
		this.conn = conn;
		this.parent = parent;
		this.menuSemanal = menuSemanal;
		menuSemanal.BDLeer(menuSemanal.getLunes());
		requisitosTemporalesNoCumplidos = menuSemanal.calcularRequisitosTemporalesNoCumplidos();
		requisitosCaloricosNoCumplidos = menuSemanal.calcularRequisitosCaloricosNoCumplidos();
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulOscuro = new Color(37, 40, 80);

	private void initDisplay() {
		titleLabel = new JLabel("Unfulfilled requirements");
		titleLabel.setBackground(azulOscuro);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(new LineBorder(azulOscuro));
		titleLabel.setOpaque(true);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));

		modeloLista = new DefaultListModel();
		modeloLista.clear();

		for (int i = 0; i < requisitosTemporalesNoCumplidos.size(); i++) {
			modeloLista.addElement(requisitosTemporalesNoCumplidos.get(i));
		}
		for (int i = 0; i < requisitosCaloricosNoCumplidos.size(); i++) {
			modeloLista.addElement(requisitosCaloricosNoCumplidos.get(i));
		}

		requisitosLista = new JList(modeloLista);

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(requisitosLista);
		requisitosLista.setLayoutOrientation(JList.VERTICAL);

		requisitosLista.setBackground(Color.WHITE);
		requisitosLista.setForeground(Color.BLACK);
	}

	private void initButtons() {
		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 20));
		okButton.addActionListener(new ButtonsClickListener());
		okButton.setActionCommand("OK");
	}

	private void initScreen() {
		setTitle("Unfulfilled Requirements");
		setMinimumSize(new Dimension(1200, 500));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		// poner icono personalizado a la app;
		ImageIcon icon = new ImageIcon(this.getClass().getResource("icono.png"));
		Image img = icon.getImage();
		setIconImage(img);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = insets;

		// creacion del panel principal
		mainPanel = new JPanel();
		mainGridBagLayout = new GridBagLayout();
		mainPanel.setBackground(azulOscuro);
		mainPanel.setLayout(mainGridBagLayout);
		add(mainPanel);

		mainPanel.add(titleLabel);

		constraints.gridy = 1;
		listaPanel = new JPanel(new BorderLayout());
		listaPanel.add(scrollPane);
		mainPanel.add(listaPanel, constraints);

		buttonsPanel = new JPanel();
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setBackground(azulOscuro);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(okButton);

		constraints.gridy = 2;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);

		parent.setVisible(false);

	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				parent.setVisible(true);
				dispose();
			}
		}
	}
}
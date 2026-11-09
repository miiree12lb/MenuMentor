import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;

class IntercambioMenus extends JDialog {
	JRadioButton diaEnteroRB, menuEnteroRB, almuerzoMenuRB, cenaMenuRB, unPlatoRB, cenaPlatoRB, almuerzoPlatoRB,
			primeroPlatoRB, segundoPlatoRB;
	JLabel dia1Label, dia2Label, aIntercambiarLabel;
	JButton okButton, xButton;
	JComboBox dia1ComboBox, dia2ComboBox;
	FlowLayout dia1FlowLayout, dia2FlowLayout, buttonsFlowLayout;
	JPanel buttonsPanel, dia1Panel, dia2Panel, mainPanel;
	GridBagLayout mainLayout;
	ButtonGroup bgPrincipal, bgMenu, bgPlato1, bgPlato2;
	Connection conn;
	Menu_Semanal menuSemanal;

	public IntercambioMenus(Connection conn, Menu_Semanal menuSemanal) {
		this.conn = conn;
		this.menuSemanal = menuSemanal;
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulClaro = new Color(131, 181, 221);

	private void initDisplay() {
		dia1Label = new JLabel("Day 1");
		dia1Label.setBackground(azulClaro);
		dia1Label.setForeground(Color.BLACK);
		dia1Label.setBorder(new LineBorder(azulClaro));
		dia1Label.setOpaque(true);
		dia1Label.setHorizontalAlignment(SwingConstants.LEFT);

		dia2Label = new JLabel("Day 2");
		dia2Label.setBackground(azulClaro);
		dia2Label.setForeground(Color.BLACK);
		dia2Label.setBorder(new LineBorder(azulClaro));
		dia2Label.setOpaque(true);
		dia2Label.setHorizontalAlignment(SwingConstants.LEFT);

		aIntercambiarLabel = new JLabel("To exchange:");
		aIntercambiarLabel.setBackground(azulClaro);
		aIntercambiarLabel.setForeground(Color.BLACK);
		aIntercambiarLabel.setBorder(new LineBorder(azulClaro));
		aIntercambiarLabel.setOpaque(true);
		aIntercambiarLabel.setHorizontalAlignment(SwingConstants.LEFT);

		String[] str = { "-", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		dia1ComboBox = new JComboBox(str);

		dia2ComboBox = new JComboBox(str);

	}

	private void initButtons() {
		diaEnteroRB = new JRadioButton("The whole day");
		diaEnteroRB.setBackground(azulClaro);
		diaEnteroRB.addActionListener(new ButtonsClickListener());

		menuEnteroRB = new JRadioButton("The whole day");
		menuEnteroRB.setBackground(azulClaro);
		menuEnteroRB.addActionListener(new ButtonsClickListener());

		almuerzoMenuRB = new JRadioButton("Lunch");
		almuerzoMenuRB.setBackground(azulClaro);
		almuerzoMenuRB.addActionListener(new ButtonsClickListener());

		cenaMenuRB = new JRadioButton("Dinner");
		cenaMenuRB.setBackground(azulClaro);
		cenaMenuRB.addActionListener(new ButtonsClickListener());

		unPlatoRB = new JRadioButton("A course");
		unPlatoRB.setBackground(azulClaro);
		unPlatoRB.addActionListener(new ButtonsClickListener());

		almuerzoPlatoRB = new JRadioButton("Lunch");
		almuerzoPlatoRB.setBackground(azulClaro);
		almuerzoPlatoRB.addActionListener(new ButtonsClickListener());

		cenaPlatoRB = new JRadioButton("Dinner");
		cenaPlatoRB.setBackground(azulClaro);
		cenaPlatoRB.addActionListener(new ButtonsClickListener());

		primeroPlatoRB = new JRadioButton("First Course");
		primeroPlatoRB.setBackground(azulClaro);
		primeroPlatoRB.addActionListener(new ButtonsClickListener());

		segundoPlatoRB = new JRadioButton("Second Course");
		segundoPlatoRB.setBackground(azulClaro);
		segundoPlatoRB.addActionListener(new ButtonsClickListener());

		bgPrincipal = new ButtonGroup();
		bgPrincipal.add(diaEnteroRB);
		bgPrincipal.add(menuEnteroRB);
		bgPrincipal.add(unPlatoRB);

		bgMenu = new ButtonGroup();
		bgMenu.add(almuerzoMenuRB);
		bgMenu.add(cenaMenuRB);

		bgPlato1 = new ButtonGroup();
		bgPlato1.add(almuerzoPlatoRB);
		bgPlato1.add(cenaPlatoRB);

		bgPlato2 = new ButtonGroup();
		bgPlato2.add(primeroPlatoRB);
		bgPlato2.add(segundoPlatoRB);

		EnableButtons();

		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 15));
		okButton.addActionListener(new ButtonsClickListener());
		okButton.setActionCommand("OK");

		// creación del botón 'X'
		xButton = new JButton("X");
		xButton.setBackground(Color.WHITE);
		xButton.setForeground(Color.RED);
		xButton.setBorder(new LineBorder(Color.BLACK));
		xButton.setPreferredSize(new Dimension(30, 15));
		xButton.addActionListener(new ButtonsClickListener());
		xButton.setActionCommand("cancel");
	}

	private void initScreen() {
		setTitle("Intercambio de menús");
		setSize(new Dimension(450, 450));
		setResizable(false);
		setModal(true);
		setBackground(azulClaro);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);

		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = insets;

		mainPanel = new JPanel();
		mainPanel.setBackground(azulClaro);
		mainLayout = new GridBagLayout();
		mainPanel.setLayout(mainLayout);
		add(mainPanel);

		mainPanel.add(aIntercambiarLabel, constraints);

		constraints.gridy = 1;
		mainPanel.add(diaEnteroRB, constraints);

		constraints.gridy = 2;
		mainPanel.add(menuEnteroRB, constraints);

		constraints.gridy = 3;
		constraints.gridx = 1;
		mainPanel.add(almuerzoMenuRB, constraints);

		constraints.gridy = 4;
		mainPanel.add(cenaMenuRB, constraints);

		constraints.gridy = 5;
		constraints.gridx = 0;
		mainPanel.add(unPlatoRB, constraints);

		constraints.gridy = 6;
		constraints.gridx = 1;
		mainPanel.add(almuerzoPlatoRB, constraints);

		constraints.gridy = 7;
		mainPanel.add(cenaPlatoRB, constraints);

		constraints.gridy = 6;
		constraints.gridx = 2;
		mainPanel.add(primeroPlatoRB, constraints);

		constraints.gridy = 7;
		mainPanel.add(segundoPlatoRB, constraints);

		dia1Panel = new JPanel();
		dia1Panel.setBackground(azulClaro);
		dia1FlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		dia1Panel.setLayout(dia1FlowLayout);
		dia1Panel.add(dia1Label);
		dia1Panel.add(dia1ComboBox);

		constraints.gridy = 8;
		constraints.gridx = 0;
		mainPanel.add(dia1Panel, constraints);

		dia2Panel = new JPanel();
		dia2Panel.setBackground(azulClaro);
		dia2FlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		dia2Panel.setLayout(dia2FlowLayout);
		dia2Panel.add(dia2Label);
		dia2Panel.add(dia2ComboBox);

		constraints.gridy = 9;
		mainPanel.add(dia2Panel, constraints);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulClaro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(xButton);
		buttonsPanel.add(okButton);

		constraints.gridx = 2;
		constraints.gridy = 10;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);
	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				if (!diaEnteroRB.isSelected() && !menuEnteroRB.isSelected() && !unPlatoRB.isSelected()) {
					JOptionPane.showMessageDialog(null, "Select an option", "SuppressWarnings", 1);
				} else if (menuEnteroRB.isSelected() && !almuerzoMenuRB.isSelected() && !cenaMenuRB.isSelected()) {
					JOptionPane.showMessageDialog(null, "Select lunch or dinner", "SuppressWarnings", 1);
				} else if (unPlatoRB.isSelected() && !almuerzoPlatoRB.isSelected() && !cenaPlatoRB.isSelected()) {
					JOptionPane.showMessageDialog(null, "Select lunch or dinner", "SuppressWarnings", 1);
				} else if (unPlatoRB.isSelected() && !primeroPlatoRB.isSelected() && !segundoPlatoRB.isSelected()) {
					JOptionPane.showMessageDialog(null, "Select first or second course", "SuppressWarnings", 1);
				} else if (dia1ComboBox.getSelectedIndex() == 0 || dia2ComboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Select days to exchange", "SuppressWarnings", 1);
				} else if (dia1ComboBox.getSelectedIndex() == dia2ComboBox.getSelectedIndex()
						&& dia1ComboBox.getSelectedIndex() != 0 && dia2ComboBox.getSelectedIndex() != 0) {
					JOptionPane.showMessageDialog(null, "Día 1 y Día 2 deben ser distintos", "SuppressWarnings", 1);
				} else {
					if (diaEnteroRB.isSelected()) {
						menuSemanal.intercambioDia(dia1ComboBox.getSelectedIndex() - 1,
								dia2ComboBox.getSelectedIndex() - 1);
					} else if (menuEnteroRB.isSelected()) {
						boolean bAlmuerzo;

						if (almuerzoMenuRB.isSelected())
							bAlmuerzo = true;
						else
							bAlmuerzo = false;

						menuSemanal.intercambioMenu(bAlmuerzo, dia1ComboBox.getSelectedIndex() - 1,
								dia2ComboBox.getSelectedIndex() - 1);
					} else if (unPlatoRB.isSelected()) {
						boolean bAlmuerzo, bPrimero;

						if (almuerzoPlatoRB.isSelected())
							bAlmuerzo = true;
						else
							bAlmuerzo = false;

						if (primeroPlatoRB.isSelected())
							bPrimero = true;
						else
							bPrimero = false;

						menuSemanal.intercambioPlato(bAlmuerzo, bPrimero, dia1ComboBox.getSelectedIndex() - 1,
								dia2ComboBox.getSelectedIndex() - 1);
					}
					dispose();
				}
			} else if (command.equals("cancel")) {
				dispose();
			} else if (e.getSource() == diaEnteroRB) {
				EnableButtons();
			} else if (e.getSource() == menuEnteroRB) {
				EnableButtons();
			} else if (e.getSource() == unPlatoRB) {
				EnableButtons();
			}
		}
	}

	private void EnableButtons() {
		if (menuEnteroRB.isSelected()) {
			almuerzoMenuRB.setEnabled(true);
			cenaMenuRB.setEnabled(true);
		} else {
			almuerzoMenuRB.setEnabled(false);
			cenaMenuRB.setEnabled(false);
		}
		if (unPlatoRB.isSelected()) {
			almuerzoPlatoRB.setEnabled(true);
			cenaPlatoRB.setEnabled(true);
			primeroPlatoRB.setEnabled(true);
			segundoPlatoRB.setEnabled(true);
		} else {
			almuerzoPlatoRB.setEnabled(false);
			cenaPlatoRB.setEnabled(false);
			primeroPlatoRB.setEnabled(false);
			segundoPlatoRB.setEnabled(false);
		}

	}
}
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

class InformacionCalorica extends JDialog {

	JLabel caloriasMediaLabel, numCaloriasMediaLabel, caloriasTotalLabel, numCaloriasTotalLabel, titleLabel;
	JButton okButton;
	JPanel tablaPanel, mainPanel, buttonsPanel, caloriasMediaPanel, caloriasTotalPanel;
	GridBagLayout mainGridBagLayout;
	FlowLayout buttonsFlowLayout, caloriasMediaFlowLayout, caloriasTotalFlowLayout;
	Connection conn;
	Menu_Semanal menuSemanal;
	JTable caloriasTable;

	Color azulClaro = new Color(131, 181, 221);

	public InformacionCalorica(Connection conn, Menu_Semanal menuSemanal) {
		this.conn = conn;
		this.menuSemanal = menuSemanal;
		initDisplay();
		initButtons();
		initScreen();
	}

	private void initDisplay() {
		titleLabel = new JLabel("kcal ingested during the week:");
		titleLabel.setBackground(azulClaro);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setBorder(new LineBorder(azulClaro));
		titleLabel.setOpaque(true);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
		titleLabel.setPreferredSize(new Dimension(400, 90));

		caloriasTotalLabel = new JLabel("Total kcal during the week:");
		caloriasTotalLabel.setBackground(azulClaro);
		caloriasTotalLabel.setBorder(new LineBorder(azulClaro));
		caloriasTotalLabel.setForeground(Color.BLACK);
		caloriasTotalLabel.setOpaque(true);

		numCaloriasTotalLabel = new JLabel();
		numCaloriasTotalLabel.setBackground(azulClaro);
		numCaloriasTotalLabel.setBorder(new LineBorder(azulClaro));
		numCaloriasTotalLabel.setForeground(Color.BLACK);
		numCaloriasTotalLabel.setOpaque(true);

		caloriasMediaLabel = new JLabel("Mean kcal ingested per day:");
		caloriasMediaLabel.setBackground(azulClaro);
		caloriasMediaLabel.setBorder(new LineBorder(azulClaro));
		caloriasMediaLabel.setForeground(Color.BLACK);
		caloriasMediaLabel.setOpaque(true);

		numCaloriasMediaLabel = new JLabel();
		numCaloriasMediaLabel.setBackground(azulClaro);
		numCaloriasMediaLabel.setBorder(new LineBorder(azulClaro));
		numCaloriasMediaLabel.setForeground(Color.BLACK);
		numCaloriasMediaLabel.setOpaque(true);

		menuSemanal.calcularCalorias();

		Object[] nombreColumns = { "", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		Object[][] data = {
				{ "Lunch", menuSemanal.getMenusDiarios().get(0).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(1).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(2).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(3).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(4).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(5).getAlmuerzo().getCalorias(),
						menuSemanal.getMenusDiarios().get(6).getAlmuerzo().getCalorias() },

				{ "Dinner", menuSemanal.getMenusDiarios().get(0).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(1).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(2).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(3).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(4).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(5).getCena().getCalorias(),
						menuSemanal.getMenusDiarios().get(6).getCena().getCalorias() },

				{ "Total", menuSemanal.getMenusDiarios().get(0).getCalorias(),
						menuSemanal.getMenusDiarios().get(1).getCalorias(),
						menuSemanal.getMenusDiarios().get(2).getCalorias(),
						menuSemanal.getMenusDiarios().get(3).getCalorias(),
						menuSemanal.getMenusDiarios().get(4).getCalorias(),
						menuSemanal.getMenusDiarios().get(5).getCalorias(),
						menuSemanal.getMenusDiarios().get(6).getCalorias() },
		};
		// instance table model
		DefaultTableModel tableModel = new DefaultTableModel(data, nombreColumns) {
			// No se puede editar la tabla
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		caloriasTable = new JTable(tableModel);

		// Impedir que el usuario pueda mover las columnas de la tabla
		caloriasTable.getTableHeader().setReorderingAllowed(false);
		caloriasTable.setSize(new Dimension(800, 90));
		caloriasTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		caloriasTable.setPreferredScrollableViewportSize(new Dimension(750, 49));
		caloriasTable.getTableHeader().setResizingAllowed(false);

	}

	private void initButtons() {
		// creación del botón 'OK'
		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 15));
		okButton.addActionListener(new ButtonsClickListener());
		okButton.setActionCommand("OK");
	}

	private void initScreen() {
		setTitle("Caloric Data");
		setSize(new Dimension(800, 400));
		setResizable(false);
		setModal(true);

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
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = insets;

		// creacion del panel principal
		mainPanel = new JPanel();
		mainGridBagLayout = new GridBagLayout();
		mainPanel.setBackground(azulClaro);
		mainPanel.setLayout(mainGridBagLayout);
		add(mainPanel);

		mainPanel.add(titleLabel, constraints);

		constraints.gridy = 1;
		// Creamos un scrollpanel y se lo agregamos a la tabla
		JScrollPane scrollpane1 = new JScrollPane(caloriasTable);
		scrollpane1.setBackground(azulClaro);

		tablaPanel = new JPanel();

		// Agregamos el scrollpanel al contenedor
		tablaPanel.add(scrollpane1, BorderLayout.CENTER);
		tablaPanel.setSize(new Dimension(800, 200));
		tablaPanel.setBackground(azulClaro);
		mainPanel.add(tablaPanel, constraints);

		int caloriasTotal = 0;
		for (int i = 1; i < 8; i++) {
			caloriasTotal += Integer.parseInt(String.valueOf(caloriasTable.getModel().getValueAt(2, i)));
		}

		numCaloriasTotalLabel.setText(String.valueOf(caloriasTotal));

		caloriasTotalPanel = new JPanel();
		caloriasTotalFlowLayout = new FlowLayout(FlowLayout.LEFT, 4, 4);
		caloriasTotalPanel.setBackground(azulClaro);
		caloriasTotalPanel.setLayout(caloriasTotalFlowLayout);
		caloriasTotalPanel.add(caloriasTotalLabel);
		caloriasTotalPanel.add(numCaloriasTotalLabel);

		constraints.gridy = 2;
		mainPanel.add(caloriasTotalPanel, constraints);

		numCaloriasMediaLabel.setText(String.valueOf(caloriasTotal / 7));

		caloriasMediaPanel = new JPanel();
		caloriasMediaFlowLayout = new FlowLayout(FlowLayout.LEFT, 4, 4);
		caloriasMediaPanel.setBackground(azulClaro);
		caloriasMediaPanel.setLayout(caloriasMediaFlowLayout);
		caloriasMediaPanel.add(caloriasMediaLabel);
		caloriasMediaPanel.add(numCaloriasMediaLabel);

		constraints.gridy = 3;
		mainPanel.add(caloriasMediaPanel, constraints);

		constraints.gridy = 4;
		buttonsPanel = new JPanel();
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 4, 4);
		buttonsPanel.setBackground(azulClaro);
		buttonsPanel.setLayout(buttonsFlowLayout);
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);

	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				dispose();
			}
		}
	}
}
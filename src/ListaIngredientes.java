import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

class ListaIngredientes extends JFrame {
	JLabel cabeceraLabel;
	JPanel mainPanel, ingredienteListaPanel, buttonsPanel, cabeceraPanel;
	JButton imprimirButton, okButton;
	FlowLayout cabeceraFlowLayout, buttonsFlowLayout;
	GridBagLayout mainGridBagLayout;
	JFrame parent;
	Connection conn;
	Menu_Semanal menuSemanal;
	JTable ingredienteTable;

	Color azulOscuro = new Color(37, 40, 80);

	public ListaIngredientes(Connection conn, JFrame parent, Menu_Semanal menuSemanal) {
		this.conn = conn;
		this.parent = parent;
		this.menuSemanal = menuSemanal;
		initDisplay();
		initButtons();
		initScreen();
	}

	private void initDisplay() {
		cabeceraLabel = new JLabel("Ingredients needed during the week");
		cabeceraLabel.setBackground(azulOscuro);
		cabeceraLabel.setForeground(Color.WHITE);
		cabeceraLabel.setBorder(new LineBorder(azulOscuro));
		cabeceraLabel.setPreferredSize(new Dimension(825, 70));
		cabeceraLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));

		Object[] nombreColumns = { "Ingredients", "Grams" };

		// instance table model
		DefaultTableModel tableModel = new DefaultTableModel(menuSemanal.generarListaIngredientes(), nombreColumns) {
			// No se puede editar la tabla
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		ingredienteTable = new JTable(tableModel);

		// Impedir que el usuario pueda mover las columnas de la tabla
		ingredienteTable.getTableHeader().setReorderingAllowed(false);
		ingredienteTable.getTableHeader().setResizingAllowed(false);
	}

	private void initButtons() {
		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 15));
		okButton.addActionListener(new ButtonsClickListener());
		okButton.setActionCommand("OK");

		ImageIcon icon = new ImageIcon(this.getClass().getResource("imprimir.png"));
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		imprimirButton = new JButton(icon);
		imprimirButton.setPreferredSize(new Dimension(55, 55));
		imprimirButton.addActionListener(new ButtonsClickListener());
		imprimirButton.setActionCommand("imprimir");
	}

	private void initScreen() {
		setTitle("List of ingredients needed during the week");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		setMinimumSize(new Dimension(1100, 700));
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

		cabeceraPanel = new JPanel();
		cabeceraFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		cabeceraPanel.setBackground(azulOscuro);
		cabeceraPanel.setPreferredSize(new Dimension(getWidth(), 75));
		cabeceraPanel.setLayout(cabeceraFlowLayout);
		cabeceraPanel.add(cabeceraLabel);
		cabeceraPanel.add(imprimirButton);

		mainPanel.add(cabeceraPanel, constraints);

		constraints.gridy = 1;
		ingredienteTable.setPreferredScrollableViewportSize(new Dimension(300, 500));
		ingredienteTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		ingredienteTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		ingredienteTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		ingredienteTable.setRowHeight(20);

		// Creamos un scrollpanel y se lo agregamos a la tabla
		JScrollPane scrollpane1 = new JScrollPane(ingredienteTable);

		ingredienteListaPanel = new JPanel();

		// Agregamos el scrollpanel al contenedor
		ingredienteListaPanel.add(scrollpane1, BorderLayout.CENTER);
		ingredienteListaPanel.setSize(new Dimension(900, 800));
		ingredienteListaPanel.setBackground(azulOscuro);
		mainPanel.add(ingredienteListaPanel, constraints);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulOscuro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
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
			} else if (command.equals("imprimir")) {
				try {
					ingredienteTable.print();
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "It couldn't be printed", "SuppressWarnings", 0);
				}
			}
		}
	}
}
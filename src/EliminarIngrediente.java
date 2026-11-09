import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class EliminarIngrediente extends JDialog {
	JLabel nombreLabel;
	JButton okButton, buscarButton, eliminarButton;
	JTextField ingredienteTextField;
	JList<CheckboxListItem> ingredienteLista;
	JPanel mainPanel, ingredientePanel, buttonsPanel, listaPanel;
	GridBagLayout mainLayout;
	FlowLayout ingredienteFlowLayout, buttonsFlowLayout;
	JScrollPane scrollPane;
	Boolean selected = false;
	Connection conn;
	ArrayList<Pair<String, Boolean>> listaIngredientes;
	DefaultListModel<CheckboxListItem> model;
	Ingrediente ingrediente;

	public EliminarIngrediente(Connection conn) {
		listaIngredientes = new ArrayList<Pair<String, Boolean>>();
		this.conn = conn;
		ingrediente = new Ingrediente(conn);
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulOscuro = new Color(37, 40, 80);
	Color azulClaro = new Color(131, 181, 221);

	private void initDisplay() {
		nombreLabel = new JLabel("Ingredient");
		nombreLabel.setBackground(azulClaro);
		nombreLabel.setForeground(Color.BLACK);
		nombreLabel.setBorder(new LineBorder(azulClaro));
		nombreLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));

		ingredienteTextField = new JTextField(20);

		// Creación de una lista modelo
		model = new DefaultListModel<CheckboxListItem>();

		actualizarModel();
		// Creación de la lista
		ingredienteLista = new JList<CheckboxListItem>(model);

		ingredienteLista.setCellRenderer(new CheckboxListRenderer());
		ingredienteLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Afegir un mouse listener per gestionar el canvi de propietats quan es clica
		ingredienteLista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				@SuppressWarnings("unchecked")
				JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();

				// Posición del elemento seleccionado
				int index = list.locationToIndex(event.getPoint());
				CheckboxListItem item = list.getModel().getElementAt(index);

				// Canvio de estado de la check box
				item.setSelected(!item.isSelected());

				// Repintar la fila
				ingredienteLista.repaint(list.getCellBounds(index, index));

				selected = false;
				int initialSize = listaIngredientes.size();
				for (int i = 0; i < list.getModel().getSize(); i++) {
					if (list.getModel().getElementAt(i).isSelected())
						selected = true;
					if (initialSize == 0) {
						listaIngredientes.add(Pair.createPair(list.getModel().getElementAt(i).toString(),
								list.getModel().getElementAt(i).isSelected()));
					} else {
						for (int j = 0; j < listaIngredientes.size(); j++) {
							if (listaIngredientes.get(j).getElement0().toLowerCase()
									.equals(list.getModel().getElementAt(i).toString().toLowerCase())) {
								listaIngredientes.set(j, Pair.createPair(listaIngredientes.get(j).getElement0(),
										list.getModel().getElementAt(i).isSelected()));
							}
						}
					}
				}
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(ingredienteLista);
		ingredienteLista.setLayoutOrientation(JList.VERTICAL);
		ingredienteLista.setVisibleRowCount(4);

	}

	private void initButtons() {
		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 15));
		okButton.addActionListener(new ButtonsClickListener());
		okButton.setActionCommand("OK");

		eliminarButton = new JButton("Delete");
		eliminarButton.setBackground(Color.WHITE);
		eliminarButton.setForeground(Color.BLACK);
		eliminarButton.setBorder(new LineBorder(Color.BLACK));
		eliminarButton.setPreferredSize(new Dimension(30, 15));
		eliminarButton.addActionListener(new ButtonsClickListener());
		eliminarButton.setActionCommand("eliminar");

		buscarButton = new JButton("Search");
		buscarButton.setBackground(Color.WHITE);
		buscarButton.setForeground(Color.BLACK);
		buscarButton.setBorder(new LineBorder(Color.BLACK));
		buscarButton.setPreferredSize(new Dimension(60, 20));
		buscarButton.addActionListener(new ButtonsClickListener());
		buscarButton.setActionCommand("buscar");
	}

	private void initScreen() {

		setTitle("Delete Ingredient");
		setSize(new Dimension(450, 300));
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
		mainLayout = new GridBagLayout();
		mainPanel.setBackground(azulClaro);
		mainPanel.setLayout(mainLayout);
		add(mainPanel);

		ingredienteFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		ingredientePanel = new JPanel();
		ingredientePanel.setBackground(azulClaro);
		ingredientePanel.setLayout(ingredienteFlowLayout);

		ingredientePanel.add(nombreLabel);
		ingredientePanel.add(ingredienteTextField);
		ingredientePanel.add(buscarButton);

		mainPanel.add(ingredientePanel, constraints);

		constraints.gridy = 1;
		listaPanel = new JPanel(new BorderLayout());
		listaPanel.add(scrollPane);
		mainPanel.add(listaPanel, constraints);

		constraints.gridy = 2;
		mainPanel.add(eliminarButton, constraints);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulClaro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(okButton);

		constraints.gridy = 3;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);

	}

	private void actualizarModel() {
		ResultSet rs;
		model.clear();
		try {
			Statement st;
			String query = "SELECT nombre FROM Ingredientes WHERE nombre LIKE '%"
					+ ingredienteTextField.getText().toLowerCase() + "%' ORDER BY nombre ASC";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				String rs_nombre = rs.getString("nombre");

				boolean found = false, selection = false;
				for (int i = 0; i < listaIngredientes.size() && !found; i++) {
					if (listaIngredientes.get(i).getElement0().toLowerCase().equals(rs_nombre.toLowerCase())) {
						if (listaIngredientes.get(i).getElement1()) {
							selection = true;
							selected = true;
						}
						found = true;
					}
				}
				char[] arr = rs_nombre.toCharArray();
				arr[0] = Character.toUpperCase(arr[0]);
				CheckboxListItem item = new CheckboxListItem(new String(arr));
				item.setSelected(selection);
				model.addElement(item);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				dispose();
			} else if (command.equals("cancel")) {
				dispose();
			} else if (command.equals("buscar")) {
				actualizarModel();
			} else if (command.equals("eliminar")) {
				if (ingredienteLista.isSelectionEmpty() && !selected) {
					JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
				} else if (!selected) {
					JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
				} else {
					int result = JOptionPane.showConfirmDialog(null,
							"Selected ingredients will be deleted permanently. Do you want to continue?",
							"SuppressWarnings", JOptionPane.OK_CANCEL_OPTION, 2);
					if (result == JOptionPane.OK_OPTION) {
						for (int i = 0; i < listaIngredientes.size(); i++) {
							if (listaIngredientes.get(i).getElement1()) {
								ingrediente.setNombre(listaIngredientes.get(i).getElement0());
								int eliminar = ingrediente.BDEliminar();

								if (eliminar == 2) {
									JOptionPane.showMessageDialog(null,
											"The ingredient " + ingrediente.getNombre() + " is used in a course",
											"SuppressWarnings", 0);
								} else if (eliminar == 0) {
									JOptionPane.showMessageDialog(null,
											ingrediente.getNombre() + " has been deleted permanently",
											"SuppressWarnings", 1);
								}
							}
						}
					}
					listaIngredientes.clear();
					actualizarModel();
				}
			}
		}
	}

	// Representa un element de la llista
	class CheckboxListItem {
		private String label;
		private boolean isSelected = false;

		public CheckboxListItem(String label) {
			this.label = label;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public String toString() {
			return label;
		}
	}

	// Gestiona el renderitzat de les files amb un checkbox
	class CheckboxListRenderer extends JCheckBox implements ListCellRenderer<CheckboxListItem> {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList<? extends CheckboxListItem> list, CheckboxListItem value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setEnabled(list.isEnabled());
			setSelected(value.isSelected());
			setFont(list.getFont());
			setBackground(list.getBackground());
			setText(value.toString());

			if (value.isSelected()) {
				setForeground(azulClaro);
				setFont(new Font(Font.DIALOG, Font.BOLD, 13));
			} else {
				setForeground(list.getForeground());
			}

			return this;
		}
	}

	static class Pair<K, V> {

		private final K element0;
		private final V element1;

		public static <K, V> Pair<K, V> createPair(K element0, V element1) {
			return new Pair<K, V>(element0, element1);
		}

		public Pair(K element0, V element1) {
			this.element0 = element0;
			this.element1 = element1;
		}

		public K getElement0() {
			return element0;
		}

		public V getElement1() {
			return element1;
		}
	}
}
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.text.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class PlatoNuevo extends JDialog {
	JLabel nombreLabel, linkLabel, ingredienteLabel, cantidadLabel, frecuenciaLabel, cuandoComerLabel, listaLabel,
			tiempoEsperaLabel;
	JList ingredienteLista, ingredienteBuscadoLista;
	JTextField nombreTextField, linkTextField, ingredienteTextField, cantidadTextField, frecuenciaTextField,
			tiempoEsperaTextField;
	JCheckBox almuerzoCheckBox, cenaCheckBox, primeroCheckBox, segundoCheckBox;
	JButton nuevoButton, agregarButton, buscarButton, okButton, xButton;
	JPanel mainPanel, nombrePanel, linkPanel, ingredientePanel, cantidadPanel, cuandoComerPanel, frecuenciaPanel,
			buttonsPanel, listaPanel, listaBuscadoPanel, tiempoEsperaPanel;
	JScrollPane scrollPane, scrollPane1;
	GridBagLayout mainLayout, cuandoComerGridBagLayout;
	FlowLayout nombreFlowLayout, linkFlowLayout, ingredienteFlowLayout, cantidadFlowLayout, frecuenciaFlowLayout,
			buttonsFlowLayout, tiempoEsperaFlowLayout;
	BorderLayout listaBorderLayout;
	Connection conn;
	DefaultListModel modeloIngredienteBuscadoLista, modeloIngredienteLista;
	Plato plato;

	public PlatoNuevo(Connection conn) {
		plato = new Plato(conn);
		this.conn = conn;
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulClaro = new Color(131, 181, 221);

	private void initDisplay() {
		nombreLabel = new JLabel("Name:");
		nombreLabel.setBackground(azulClaro);
		nombreLabel.setForeground(Color.BLACK);
		nombreLabel.setBorder(new LineBorder(azulClaro));
		nombreLabel.setPreferredSize(new Dimension(100, 20));
		nombreLabel.setOpaque(true);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		linkLabel = new JLabel("Link:");
		linkLabel.setBackground(azulClaro);
		linkLabel.setForeground(Color.BLACK);
		linkLabel.setBorder(new LineBorder(azulClaro));
		linkLabel.setPreferredSize(new Dimension(100, 20));
		linkLabel.setOpaque(true);
		linkLabel.setHorizontalAlignment(SwingConstants.LEFT);

		ingredienteLabel = new JLabel("Ingredient:");
		ingredienteLabel.setBackground(azulClaro);
		ingredienteLabel.setForeground(Color.BLACK);
		ingredienteLabel.setBorder(new LineBorder(azulClaro));
		ingredienteLabel.setPreferredSize(new Dimension(100, 20));
		ingredienteLabel.setOpaque(true);
		ingredienteLabel.setHorizontalAlignment(SwingConstants.LEFT);

		cantidadLabel = new JLabel("Quantity (g):");
		cantidadLabel.setBackground(azulClaro);
		cantidadLabel.setForeground(Color.BLACK);
		cantidadLabel.setBorder(new LineBorder(azulClaro));
		cantidadLabel.setPreferredSize(new Dimension(100, 20));
		cantidadLabel.setOpaque(true);
		cantidadLabel.setHorizontalAlignment(SwingConstants.LEFT);

		cuandoComerLabel = new JLabel("When to eat the course:");
		cuandoComerLabel.setBackground(azulClaro);
		cuandoComerLabel.setForeground(Color.BLACK);
		cuandoComerLabel.setBorder(new LineBorder(azulClaro));
		// cuandoComerLabel.setPreferredSize(new Dimension(200,30));
		cuandoComerLabel.setOpaque(true);
		cuandoComerLabel.setHorizontalAlignment(SwingConstants.LEFT);

		frecuenciaLabel = new JLabel("Frecuency (every 28 days):");
		frecuenciaLabel.setBackground(azulClaro);
		frecuenciaLabel.setForeground(Color.BLACK);
		frecuenciaLabel.setBorder(new LineBorder(azulClaro));
		frecuenciaLabel.setPreferredSize(new Dimension(190, 20));
		frecuenciaLabel.setOpaque(true);
		frecuenciaLabel.setHorizontalAlignment(SwingConstants.LEFT);

		tiempoEsperaLabel = new JLabel("Waiting time (days):");
		tiempoEsperaLabel.setBackground(azulClaro);
		tiempoEsperaLabel.setForeground(Color.BLACK);
		tiempoEsperaLabel.setBorder(new LineBorder(azulClaro));
		tiempoEsperaLabel.setPreferredSize(new Dimension(190, 20));
		tiempoEsperaLabel.setOpaque(true);
		tiempoEsperaLabel.setHorizontalAlignment(SwingConstants.LEFT);

		listaLabel = new JLabel("Ingredients of the plate");
		listaLabel.setBackground(azulClaro);
		listaLabel.setForeground(Color.BLACK);
		listaLabel.setBorder(new LineBorder(azulClaro));
		listaLabel.setOpaque(true);
		listaLabel.setHorizontalAlignment(SwingConstants.LEFT);

		nombreTextField = new JTextField(20);

		linkTextField = new JTextField(20);

		ingredienteTextField = new JTextField(20);

		cantidadTextField = new JTextField(20);
		// solo poder escribir digitos
		cantidadTextField.setDocument(new LimitadorCaracteres());

		frecuenciaTextField = new JTextField(10);
		// solo poder escribir digitos
		frecuenciaTextField.setDocument(new LimitadorCaracteres());

		tiempoEsperaTextField = new JTextField(10);
		// solo poder escribir digitos
		tiempoEsperaTextField.setDocument(new LimitadorCaracteres());

		modeloIngredienteLista = new DefaultListModel();
		actualizarIngredienteLista(plato.getIngredientes());
		ingredienteLista = new JList(modeloIngredienteLista);

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(ingredienteLista);
		ingredienteLista.setLayoutOrientation(JList.VERTICAL);

		ingredienteLista.setBackground(Color.WHITE);
		ingredienteLista.setForeground(Color.BLACK);

		modeloIngredienteBuscadoLista = new DefaultListModel();
		actualizarIngredienteBuscadoLista();
		ingredienteBuscadoLista = new JList(modeloIngredienteBuscadoLista);

		scrollPane1 = new JScrollPane();
		scrollPane1.setViewportView(ingredienteBuscadoLista);
		ingredienteBuscadoLista.setLayoutOrientation(JList.VERTICAL);

		ingredienteBuscadoLista.setBackground(Color.WHITE);
		ingredienteBuscadoLista.setForeground(Color.BLACK);
	}

	private void initButtons() {
		// creación del botón 'agregar'
		agregarButton = new JButton("Add");
		agregarButton.setBackground(Color.WHITE);
		agregarButton.setBorder(new LineBorder(Color.BLACK));
		agregarButton.setForeground(Color.BLACK);
		agregarButton.setPreferredSize(new Dimension(60, 20));
		agregarButton.addActionListener(new ButtonsClickListener());
		agregarButton.setActionCommand("agregar");

		// creación del botón 'nuevo'
		nuevoButton = new JButton("New");
		nuevoButton.setBackground(Color.WHITE);
		nuevoButton.setForeground(Color.BLACK);
		nuevoButton.setBorder(new LineBorder(Color.BLACK));
		nuevoButton.setPreferredSize(new Dimension(60, 20));
		nuevoButton.addActionListener(new ButtonsClickListener());
		nuevoButton.setActionCommand("nuevo");

		buscarButton = new JButton("Search");
		buscarButton.setBackground(Color.WHITE);
		buscarButton.setForeground(Color.BLACK);
		buscarButton.setBorder(new LineBorder(Color.BLACK));
		buscarButton.setPreferredSize(new Dimension(60, 20));
		buscarButton.addActionListener(new ButtonsClickListener());
		buscarButton.setActionCommand("buscar");

		// creación del botón 'OK'
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

		almuerzoCheckBox = new JCheckBox("Lunch", false);
		almuerzoCheckBox.setForeground(Color.BLACK);
		almuerzoCheckBox.setBackground(azulClaro);
		almuerzoCheckBox.setBorder(new LineBorder(azulClaro));
		// action listener

		cenaCheckBox = new JCheckBox("Dinner", false);
		cenaCheckBox.setForeground(Color.BLACK);
		cenaCheckBox.setBackground(azulClaro);
		cenaCheckBox.setBorder(new LineBorder(azulClaro));
		// action listener

		primeroCheckBox = new JCheckBox("First course", false);
		primeroCheckBox.setForeground(Color.BLACK);
		primeroCheckBox.setBackground(azulClaro);
		primeroCheckBox.setBorder(new LineBorder(azulClaro));
		// action listener

		segundoCheckBox = new JCheckBox("Second Course", false);
		segundoCheckBox.setForeground(Color.BLACK);
		segundoCheckBox.setBackground(azulClaro);
		segundoCheckBox.setBorder(new LineBorder(azulClaro));
		// action listener
	}

	private void initScreen() {
		setTitle("New Course");
		setSize(new Dimension(500, 750));
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
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = insets;

		// creacion del panel principal
		mainPanel = new JPanel();
		mainLayout = new GridBagLayout();
		mainPanel.setBackground(azulClaro);
		mainPanel.setLayout(mainLayout);
		add(mainPanel);

		// Añadir componentes al panel de cuando vamos a comer el plato
		cuandoComerPanel = new JPanel();
		cuandoComerGridBagLayout = new GridBagLayout();
		cuandoComerPanel.setBackground(azulClaro);
		cuandoComerPanel.setLayout(cuandoComerGridBagLayout);

		cuandoComerPanel.add(cuandoComerLabel, constraints);

		constraints.gridy = 1;
		constraints.gridx = 1;
		cuandoComerPanel.add(almuerzoCheckBox, constraints);

		constraints.gridy = 2;
		cuandoComerPanel.add(cenaCheckBox, constraints);

		constraints.gridy = 3;
		constraints.gridx = 0;
		cuandoComerPanel.add(primeroCheckBox, constraints);

		constraints.gridy = 4;
		cuandoComerPanel.add(segundoCheckBox, constraints);

		// Añadir componentes al panel principal
		nombrePanel = new JPanel();
		nombreFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		nombrePanel.setBackground(azulClaro);
		nombrePanel.setLayout(nombreFlowLayout);
		nombrePanel.add(nombreLabel);
		nombrePanel.add(nombreTextField);

		constraints.gridy = 0;
		mainPanel.add(nombrePanel, constraints);

		linkPanel = new JPanel();
		linkFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		linkPanel.setBackground(azulClaro);
		linkPanel.setLayout(linkFlowLayout);
		linkPanel.add(linkLabel);
		linkPanel.add(linkTextField);

		constraints.gridy = 1;
		mainPanel.add(linkPanel, constraints);

		ingredientePanel = new JPanel();
		ingredienteFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		ingredientePanel.setBackground(azulClaro);
		ingredientePanel.setLayout(ingredienteFlowLayout);
		ingredientePanel.add(ingredienteLabel);
		ingredientePanel.add(ingredienteTextField);
		ingredientePanel.add(buscarButton);
		ingredientePanel.add(nuevoButton);

		constraints.gridy = 2;
		mainPanel.add(ingredientePanel, constraints);

		constraints.gridy = 3;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		listaBuscadoPanel = new JPanel(new BorderLayout());
		listaBuscadoPanel.add(scrollPane1);
		mainPanel.add(listaBuscadoPanel, constraints);

		constraints.gridy = 4;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		cantidadPanel = new JPanel();
		cantidadFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		cantidadPanel.setBackground(azulClaro);
		cantidadPanel.setLayout(cantidadFlowLayout);
		cantidadPanel.add(cantidadLabel);
		cantidadPanel.add(cantidadTextField);
		cantidadPanel.add(agregarButton);
		mainPanel.add(cantidadPanel, constraints);

		constraints.gridy = 5;
		mainPanel.add(listaLabel, constraints);

		constraints.gridy = 6;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		listaPanel = new JPanel(new BorderLayout());
		listaPanel.add(scrollPane);
		mainPanel.add(listaPanel, constraints);

		constraints.gridy = 7;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		mainPanel.add(cuandoComerPanel, constraints);

		frecuenciaPanel = new JPanel();
		frecuenciaFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		frecuenciaPanel.setBackground(azulClaro);
		frecuenciaPanel.setLayout(frecuenciaFlowLayout);
		frecuenciaPanel.add(frecuenciaLabel);
		frecuenciaPanel.add(frecuenciaTextField);

		constraints.gridy = 8;
		mainPanel.add(frecuenciaPanel, constraints);

		tiempoEsperaPanel = new JPanel();
		tiempoEsperaFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		tiempoEsperaPanel.setBackground(azulClaro);
		tiempoEsperaPanel.setLayout(tiempoEsperaFlowLayout);
		tiempoEsperaPanel.add(tiempoEsperaLabel);
		tiempoEsperaPanel.add(tiempoEsperaTextField);

		constraints.gridy = 9;
		mainPanel.add(tiempoEsperaPanel, constraints);

		buttonsPanel = new JPanel();
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 10, 10);
		buttonsPanel.setBackground(azulClaro);
		buttonsPanel.setLayout(buttonsFlowLayout);
		buttonsPanel.add(xButton);
		buttonsPanel.add(okButton);

		constraints.gridy = 10;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);
	}

	private void actualizarIngredienteLista(ArrayList<Ingrediente_Plato> ingredientesPlato) {
		modeloIngredienteLista.clear();
		for (int i = 0; i < ingredientesPlato.size(); i++) {
			String nombre = ingredientesPlato.get(i).getIngrediente().getNombre();
			char[] arr = nombre.toCharArray();
			arr[0] = Character.toUpperCase(arr[0]);
			modeloIngredienteLista.addElement(
					new String(arr) + ", " + String.valueOf(ingredientesPlato.get(i).getGramos()) + " grams");
		}
	}

	private void actualizarIngredienteBuscadoLista() {
		ResultSet rs;
		modeloIngredienteBuscadoLista.clear();
		try {
			Statement st;
			String query = "SELECT nombre FROM Ingredientes WHERE nombre LIKE '%"
					+ ingredienteTextField.getText().toLowerCase() + "%' ORDER BY nombre ASC";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				String nombre = rs.getString("nombre");
				char[] arr = nombre.toCharArray();
				arr[0] = Character.toUpperCase(arr[0]);
				modeloIngredienteBuscadoLista.addElement(new String(arr));
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
				if (nombreTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add name to the plate", "SuppressWarnings", 1);
				} else if (frecuenciaTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add frecuency", "SuppressWarnings", 1);
				} else if (tiempoEsperaTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add minimum waiting time", "SuppressWarnings", 1);
				} else if ((!almuerzoCheckBox.isSelected() && !cenaCheckBox.isSelected())
						|| (!primeroCheckBox.isSelected() && !segundoCheckBox.isSelected())) {
					JOptionPane.showMessageDialog(null, "Add when to eat the plate", "SuppressWarnings", 1);
				} else if (plato.getIngredientes().size() == 0) {
					JOptionPane.showMessageDialog(null, "The course doesn't have any ingredient", "SuppressWarnings",
							1);
				} else if (Integer.parseInt(frecuenciaTextField.getText()) == 0) {
					JOptionPane.showMessageDialog(null, "Frecuency can't be 0", "SuppressWarnings", 1);
				} else if (Integer.parseInt(frecuenciaTextField.getText()) > 28) {
					JOptionPane.showMessageDialog(null, "Frecuency can't be higher than 28", "SuppressWarnings", 1);
				} else if (Integer.parseInt(tiempoEsperaTextField.getText()) == 0) {
					JOptionPane.showMessageDialog(null, "Waiting time can't be 0", "SuppressWarnings", 1);
				} else {
					plato.setNombre(nombreTextField.getText().toLowerCase());
					plato.setLink(linkTextField.getText());
					plato.setFrecuencia(Integer.parseInt(frecuenciaTextField.getText()));
					plato.setDiasEspera(Integer.parseInt(tiempoEsperaTextField.getText()));
					plato.setPrimero(primeroCheckBox.isSelected());
					plato.setSegundo(segundoCheckBox.isSelected());
					plato.setAlmuerzo(almuerzoCheckBox.isSelected());
					plato.setCena(cenaCheckBox.isSelected());
					plato.setHabilitado(true);
					int saved = plato.BDGuardar();
					if (saved == 0) {
						JOptionPane.showMessageDialog(null, "Course updated", "SuppressWarnings", 1);
						dispose();
					} else if (saved == 1) {
						JOptionPane.showMessageDialog(null, "Course couldn't be saved", "SuppressWarnings", 0);
						dispose();
					} else if (saved == 2) {
						JOptionPane.showMessageDialog(null, "This course already exists", "SuppressWarnings", 0);
						dispose();
					} else if (saved == 3) {
						Plato platoInhabilitado = new Plato(conn);
						platoInhabilitado.setNombre(nombreTextField.getText().toLowerCase());
						platoInhabilitado.BDLeer();

						String sIngredientes = "";
						ArrayList<Ingrediente_Plato> lIngredientes = platoInhabilitado.getIngredientes();
						for (int i = 0; i < lIngredientes.size(); i++) {
							char[] arr = lIngredientes.get(i).getIngrediente().getNombre().toCharArray();
							arr[0] = Character.toUpperCase(arr[0]);
							sIngredientes += "        - " + new String(arr) + ", " + lIngredientes.get(i).getGramos()
									+ " grams\n";
						}

						int result = JOptionPane.showConfirmDialog(null,
								"This course already exists but is disabled. \nYou can enabled or change the current name.\nThe saved course containd these ingredients:\n"
										+ sIngredientes + "Do you want to continue? You can modify the course after.",
								"Mensaje de aviso", JOptionPane.OK_CANCEL_OPTION, 0);
						if (result == JOptionPane.OK_OPTION) {
							try {
								String query = "UPDATE Platos SET habilitado = 1 WHERE Platos.idPlato = '"
										+ platoInhabilitado.getId() + "'";
								Statement st = conn.createStatement();
								st.executeUpdate(query);
								dispose();
							} catch (SQLException ex) {
								ex.printStackTrace();
								System.out.println(ex);
							}
						}
					}
				}
			} else if (command.equals("cancel")) {
				dispose();
			} else if (command.equals("nuevo")) {
				IngredienteNuevo ventana = new IngredienteNuevo(conn);

				ventana = null;
				actualizarIngredienteBuscadoLista();
			} else if (command.equals("buscar")) {
				actualizarIngredienteBuscadoLista();
			} else if (command.equals("agregar")) {
				if (ingredienteBuscadoLista.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
				} else if (cantidadTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add quantity to the ingredient", "SuppressWarnings", 1);
				} else if (Integer.parseInt(cantidadTextField.getText()) == 0) {
					JOptionPane.showMessageDialog(null, "Quantity has to be higher than 0 grams", "SuppressWarnings",
							1);
				} else if (Integer.parseInt(cantidadTextField.getText()) > 500) {
					JOptionPane.showMessageDialog(null, "Maximum quantity 500 grams", "SuppressWarnings", 1);
				} else {
					try {
						Statement st;
						String query;

						query = "SELECT idIngrediente FROM Ingredientes WHERE Ingredientes.nombre = '"
								+ ingredienteBuscadoLista.getSelectedValue().toString().toLowerCase() + "'";
						st = conn.createStatement();
						ResultSet rs = st.executeQuery(query);

						if (plato.addIngrediente(rs.getInt("idIngrediente"),
								Integer.parseInt(cantidadTextField.getText()))) {
							actualizarIngredienteLista(plato.getIngredientes());
						} else {
							JOptionPane.showMessageDialog(null, "This ingredient is already added", "SuppressWarnings",
									0);
						}

					} catch (SQLException ex) {
						ex.printStackTrace();
						System.out.println(ex);
					}
				}
			}
		}
	}

	class LimitadorCaracteres extends PlainDocument {
		public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
			for (int i = 0; i < arg1.length(); i++) {
				// si no es un digito returno
				if (!Character.isDigit(arg1.charAt(i)))
					return;
			}

			super.insertString(arg0, arg1, arg2);
		}
	}
}
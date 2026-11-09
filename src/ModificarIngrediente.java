import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;
import javax.swing.text.*;
import javax.swing.JOptionPane;

class ModificarIngrediente extends JDialog {

	JButton xButton, okButton, buscarButton, selectButton;
	JList ingredientesLista;
	JScrollPane scrollPane;
	JLabel nombreEditarLabel, editarLabel, nombreLabel, caloriasLabel;
	JTextField buscarTextField, nombreTextField, caloriasTextField;
	JPanel buscarPanel, buttonsPanel, editarPanel, caloriasPanel, nombrePanel, mainPanel;
	FlowLayout buscarFlowLayout, buttonsFlowLayout, nombreFlowLayout, caloriasFlowLayout;
	GridBagLayout mainLayout, editarLayout;
	DefaultListModel modeloIngredientesLista;
	Connection conn;
	Ingrediente ingrediente;

	Color azulClaro = new Color(131, 181, 221);
	Color azulOscuro = new Color(37, 40, 80);

	public ModificarIngrediente(Connection conn) {
		this.conn = conn;
		this.ingrediente = new Ingrediente(this.conn);
		initDisplay();
		initButtons();
		initScreen();
	}

	private void initDisplay() {
		buscarTextField = new JTextField(20);

		nombreEditarLabel = new JLabel("Ingredient's name: ");
		nombreEditarLabel.setBackground(azulClaro);
		nombreEditarLabel.setForeground(Color.BLACK);
		nombreEditarLabel.setBorder(new LineBorder(azulClaro));
		nombreEditarLabel.setOpaque(true);
		nombreEditarLabel.setHorizontalAlignment(SwingConstants.LEFT);

		// Creación etiqueta editar
		editarLabel = new JLabel("Edit");
		editarLabel.setBackground(azulOscuro);
		editarLabel.setForeground(azulClaro);
		editarLabel.setBorder(new LineBorder(azulOscuro));
		editarLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
		editarLabel.setOpaque(true);
		editarLabel.setHorizontalAlignment(SwingConstants.LEFT);

		// creación etiqueta nombre
		nombreLabel = new JLabel("Name: ");
		nombreLabel.setBackground(azulOscuro);
		nombreLabel.setForeground(Color.WHITE);
		nombreLabel.setBorder(new LineBorder(azulOscuro));
		nombreLabel.setPreferredSize(new Dimension(200, 30));
		nombreLabel.setOpaque(true);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		// creación del campo de texto para el nombre
		nombreTextField = new JTextField(20);

		// creacion de la etiqueta de las calorias
		caloriasLabel = new JLabel("kcal (in 100 g): ");
		caloriasLabel.setBackground(azulOscuro);
		caloriasLabel.setForeground(Color.WHITE);
		caloriasLabel.setBorder(new LineBorder(azulOscuro));
		caloriasLabel.setPreferredSize(new Dimension(200, 30));
		caloriasLabel.setOpaque(true);
		caloriasLabel.setHorizontalAlignment(SwingConstants.LEFT);

		// creación del campo de texto para las calorias, solo acepra cifras
		caloriasTextField = new JTextField(20);
		caloriasTextField.setDocument(new LimitadorCaracteres());

		modeloIngredientesLista = new DefaultListModel();
		actualizarIngredientesLista();
		ingredientesLista = new JList(modeloIngredientesLista);

		ingredientesLista.setBackground(Color.WHITE);
		ingredientesLista.setForeground(Color.BLACK);
		// ingredientesLista.setPreferredSize(new Dimension(450, 50));

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(ingredientesLista);
		ingredientesLista.setLayoutOrientation(JList.VERTICAL);

		buscarTextField = new JTextField(20);
	}

	private void initButtons() {
		// creación del botón 'OK'
		okButton = new JButton("OK");
		okButton.setBackground(Color.WHITE);
		okButton.setBorder(new LineBorder(Color.BLACK));
		okButton.setForeground(Color.BLACK);
		okButton.setPreferredSize(new Dimension(30, 15));
		okButton.addActionListener(new ButtonsClickListener());

		// creación del botón 'X'
		xButton = new JButton("X");
		xButton.setBackground(Color.WHITE);
		xButton.setForeground(Color.RED);
		xButton.setBorder(new LineBorder(Color.BLACK));
		xButton.setPreferredSize(new Dimension(30, 15));
		xButton.addActionListener(new ButtonsClickListener());

		Dimension d = new Dimension(80, 20);
		// Creación del botón 'Buscar'
		buscarButton = new JButton("Search");
		buscarButton.setBackground(Color.WHITE);
		buscarButton.setForeground(Color.BLACK);
		buscarButton.setBorder(new LineBorder(Color.BLACK));
		buscarButton.addActionListener(new ButtonsClickListener());
		buscarButton.setMinimumSize(d);
		buscarButton.setMaximumSize(d);
		buscarButton.setPreferredSize(d);

		d = new Dimension(100, 20);
		// Creación del botón 'Seleccionar'
		selectButton = new JButton("Select");
		selectButton.setBackground(Color.WHITE);
		selectButton.setForeground(Color.BLACK);
		selectButton.setBorder(new LineBorder(Color.BLACK));
		selectButton.addActionListener(new ButtonsClickListener());
		selectButton.setMinimumSize(d);
		selectButton.setMaximumSize(d);
		selectButton.setPreferredSize(d);
	}

	private void initScreen() {
		setTitle("Modify Ingredient");
		setSize(new Dimension(700, 500));
		setModal(true);
		setResizable(false);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);

		// Formación de los insets del mainLayout
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = insets;

		// Creación del panel Principal
		mainPanel = new JPanel();
		mainPanel.setBackground(azulClaro);
		mainLayout = new GridBagLayout();
		mainPanel.setLayout(mainLayout);
		mainPanel.setBackground(azulClaro);
		add(mainPanel);

		buscarPanel = new JPanel();
		buscarPanel.setBackground(azulClaro);
		buscarFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		buscarPanel.setLayout(buscarFlowLayout);
		buscarPanel.add(nombreEditarLabel);
		buscarPanel.add(buscarTextField);
		buscarPanel.add(buscarButton);
		buscarPanel.add(selectButton);

		mainPanel.add(buscarPanel, constraints);

		constraints.gridy = 1;
		// constraints.gridwidth = 2;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		mainPanel.add(scrollPane, constraints);

		// Creación del panel de edición
		editarPanel = new JPanel();
		editarPanel.setBackground(azulClaro);
		editarLayout = new GridBagLayout();
		editarPanel.setLayout(editarLayout);
		editarPanel.setBackground(azulOscuro);

		constraints.weightx = 0.0;
		constraints.gridy = 0;
		// constraints.gridwidth = 1;
		editarPanel.add(editarLabel);

		// construcción del panel del nombre
		nombrePanel = new JPanel();
		nombreFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		nombrePanel.setLayout(nombreFlowLayout);
		nombrePanel.setBackground(azulOscuro);
		nombrePanel.add(nombreLabel);
		nombrePanel.add(nombreTextField);

		constraints.gridy = 1;
		editarPanel.add(nombrePanel, constraints);

		// construcción del panel de las calorías
		caloriasPanel = new JPanel();
		caloriasFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		caloriasPanel.setLayout(nombreFlowLayout);
		caloriasPanel.setBackground(azulOscuro);
		caloriasPanel.add(caloriasLabel);
		caloriasPanel.add(caloriasTextField);

		constraints.gridy = 2;
		editarPanel.add(caloriasPanel, constraints);

		mainPanel.add(editarPanel, constraints);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulClaro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(xButton);
		buttonsPanel.add(okButton);

		constraints.gridy = 3;

		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);

	}

	private void actualizarIngredientesLista() {
		ResultSet rs;
		modeloIngredientesLista.clear();
		try {
			Statement st;
			String query = "SELECT nombre FROM Ingredientes WHERE nombre LIKE '%"
					+ buscarTextField.getText().toLowerCase() + "%' ORDER BY nombre ASC";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				char[] arr = rs.getString("nombre").toCharArray();
				arr[0] = Character.toUpperCase(arr[0]);
				modeloIngredientesLista.addElement(new String(arr));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				if (nombreTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add name", "SuppressWarnings", 1);
				} else if (caloriasTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add calories", "SuppressWarnings", 1);
				} else {
					ingrediente.setNombre(nombreTextField.getText());
					ingrediente.setCalorias(Integer.parseInt(caloriasTextField.getText()));
					int actualizar = ingrediente.BDActualizar();
					if (actualizar == 0) {
						JOptionPane.showMessageDialog(null, "Data updated", "SuppressWarnings", 1);
						dispose();
					} else if (actualizar == 2) {
						JOptionPane.showMessageDialog(null, "There's already an ingredient with this name",
								"SuppressWarnings", 0);
					}
				}
			} else if (e.getSource() == xButton) {
				dispose();
			} else if (e.getSource() == buscarButton) {
				actualizarIngredientesLista();
			} else if (e.getSource() == selectButton) {
				if (ingredientesLista.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
				} else {
					ingrediente.setNombre(ingredientesLista.getSelectedValue().toString());
					int read = ingrediente.BDLeer();
					if (read == 0) {
						nombreTextField.setText(ingrediente.getNombre());
						caloriasTextField.setText(String.valueOf(ingrediente.getCalorias()));
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
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import javax.swing.JOptionPane;

class IngredienteNuevo extends JDialog {
	JLabel nombreLabel, caloriasLabel, titleLabel;
	JTextField nombreTextField, caloriasTextField;
	JButton okButton, xButton;
	JPanel mainPanel, buttonsPanel, nombrePanel, caloriasPanel;
	FlowLayout buttonsFlowLayout, nombreFlowLayout, caloriasFlowLayout;
	GridBagLayout mainLayout;
	Connection conn;

	public IngredienteNuevo(Connection conn) {
		this.conn = conn;
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulClaro = new Color(131, 181, 221);

	private void initDisplay() {
		// creación etiqueta nombre
		nombreLabel = new JLabel("Name");
		nombreLabel.setBackground(azulClaro);
		nombreLabel.setForeground(Color.BLACK);
		nombreLabel.setBorder(new LineBorder(azulClaro));
		nombreLabel.setPreferredSize(new Dimension(200, 30));
		nombreLabel.setOpaque(true);
		nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

		titleLabel = new JLabel("New Ingredient");
		titleLabel.setBackground(azulClaro);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setBorder(new LineBorder(azulClaro));
		titleLabel.setOpaque(true);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));

		// creación del campo de texto para el nombre
		nombreTextField = new JTextField(20);

		// creacion de la etiqueta de las calorias
		caloriasLabel = new JLabel("kcal (in 100 g): ");
		caloriasLabel.setBackground(azulClaro);
		caloriasLabel.setForeground(Color.BLACK);
		caloriasLabel.setBorder(new LineBorder(azulClaro));
		caloriasLabel.setPreferredSize(new Dimension(200, 30));
		caloriasLabel.setOpaque(true);
		caloriasLabel.setHorizontalAlignment(SwingConstants.LEFT);

		// creación del campo de texto para las calorias
		caloriasTextField = new JTextField(20);
		caloriasTextField.setDocument(new LimitadorCaracteres());
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
		setTitle("New Ingredient");
		setSize(new Dimension(600, 300));
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
		add(mainPanel);

		mainPanel.add(titleLabel, constraints);

		// construcción del panel del nombre
		nombrePanel = new JPanel();
		nombreFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		nombrePanel.setLayout(nombreFlowLayout);
		nombrePanel.setBackground(azulClaro);
		nombrePanel.add(nombreLabel);
		nombrePanel.add(nombreTextField);

		constraints.gridy = 1;
		mainPanel.add(nombrePanel, constraints);

		// construcción del panel de las calorías
		caloriasPanel = new JPanel();
		caloriasFlowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		caloriasPanel.setLayout(nombreFlowLayout);
		caloriasPanel.setBackground(azulClaro);
		caloriasPanel.add(caloriasLabel);
		caloriasPanel.add(caloriasTextField);

		constraints.gridy = 2;
		mainPanel.add(caloriasPanel, constraints);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulClaro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(xButton);
		buttonsPanel.add(okButton);

		constraints.gridy = 3;
		constraints.gridx = 2;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);
	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				if (nombreTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add name", "SuppressWarnings", 1);
				} else if (caloriasTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add calories", "SuppressWarnings", 1);
				}

				else {
					Integer calorias = Integer.parseInt(caloriasTextField.getText());

					Ingrediente ingrediente = new Ingrediente(conn, nombreTextField.getText().toLowerCase(), calorias);
					int saved = ingrediente.BDGuardar();

					if (saved == 0) {
						JOptionPane.showMessageDialog(null, "Ingredient saved", "SuppressWarnings", 1);
						dispose();
					} else if (saved == 1) {
						JOptionPane.showMessageDialog(null, "Ingredient couldn't be saved", "SuppressWarnings", 0);
					} else if (saved == 2) {
						JOptionPane.showMessageDialog(null, "This ingredient exists", "SuppressWarnings", 0);
					}
				}
			} else if (command.equals("cancel")) {
				dispose();
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
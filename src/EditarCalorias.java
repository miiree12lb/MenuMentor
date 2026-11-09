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

class EditarCalorias extends JDialog {
	JLabel caloriasMinLabel, caloriasMaxLabel;
	JTextField caloriasMinTextField, caloriasMaxTextField;
	JPanel caloriasMinPanel, caloriasMaxPanel, buttonsPanel, mainPanel;
	JButton okButton, xButton;
	FlowLayout caloriasMinFlowLayout, caloriasMaxFlowLayout, buttonsFlowLayout;
	GridBagLayout mainLayout;
	Connection conn;

	public EditarCalorias(Connection conn) {
		this.conn = conn;
		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulClaro = new Color(131, 181, 221);

	private void initDisplay() {
		caloriasMinLabel = new JLabel("minimum diary kcal:");
		caloriasMinLabel.setBackground(azulClaro);
		caloriasMinLabel.setForeground(Color.BLACK);
		caloriasMinLabel.setBorder(new LineBorder(azulClaro));
		caloriasMinLabel.setOpaque(true);
		caloriasMinLabel.setHorizontalAlignment(SwingConstants.CENTER);

		caloriasMinTextField = new JTextField(10);
		caloriasMinTextField.setDocument(new LimitadorCaracteres());

		caloriasMaxLabel = new JLabel("maximum diary kcal:");
		caloriasMaxLabel.setBackground(azulClaro);
		caloriasMaxLabel.setForeground(Color.BLACK);
		caloriasMaxLabel.setBorder(new LineBorder(azulClaro));
		caloriasMaxLabel.setOpaque(true);
		caloriasMaxLabel.setHorizontalAlignment(SwingConstants.CENTER);

		caloriasMaxTextField = new JTextField(10);
		caloriasMaxTextField.setDocument(new LimitadorCaracteres());
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
		setTitle("Edit calories");
		setSize(new Dimension(400, 200));
		setModal(true);
		setResizable(false);

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

		mainPanel = new JPanel();
		mainLayout = new GridBagLayout();
		mainPanel.setBackground(azulClaro);
		mainPanel.setLayout(mainLayout);
		add(mainPanel);

		caloriasMinPanel = new JPanel();
		caloriasMinPanel.setBackground(azulClaro);
		caloriasMinFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		caloriasMinPanel.setLayout(caloriasMinFlowLayout);

		caloriasMinPanel.add(caloriasMinLabel);
		caloriasMinPanel.add(caloriasMinTextField);

		caloriasMaxPanel = new JPanel();
		caloriasMaxPanel.setBackground(azulClaro);
		caloriasMaxFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		caloriasMaxPanel.setLayout(caloriasMaxFlowLayout);

		caloriasMaxPanel.add(caloriasMaxLabel);
		caloriasMaxPanel.add(caloriasMaxTextField);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(azulClaro);
		buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		buttonsPanel.setLayout(buttonsFlowLayout);

		buttonsPanel.add(xButton);
		buttonsPanel.add(okButton);

		constraints.gridy = 0;
		mainPanel.add(caloriasMinPanel, constraints);

		constraints.gridy = 1;
		mainPanel.add(caloriasMaxPanel, constraints);

		constraints.gridy = 2;
		constraints.gridx = 2;
		mainPanel.add(buttonsPanel, constraints);

		setVisible(true);
	}

	private class ButtonsClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("OK")) {
				if (caloriasMinTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add minimum calories", "SuppressWarnings", 1);
				} else if (caloriasMaxTextField.getText().hashCode() == 0) {
					JOptionPane.showMessageDialog(null, "Add maximum calories", "SuppressWarnings", 1);
				} else {
					Integer caloriasMin = Integer.parseInt(caloriasMinTextField.getText()),
							caloriasMax = Integer.parseInt(caloriasMaxTextField.getText());

					if (caloriasMin < 700) {
						JOptionPane.showMessageDialog(null, "Minimum calories must be at least 700", "SuppressWarnings",
								1);
					} else if (caloriasMin > caloriasMax) {
						JOptionPane.showMessageDialog(null, "Maximum calories must be higher than minimum",
								"SuppressWarnings", 1);
					} else if (caloriasMax - caloriasMin < (10 * caloriasMin) / 100) {
						JOptionPane.showMessageDialog(null,
								"Maximum calories must be at least 10% higher than minimum calories",
								"SuppressWarnings", 1);
					} else if (caloriasMax - caloriasMin > (30 * caloriasMin) / 100) {
						JOptionPane.showMessageDialog(null,
								"Maximum calories must be at most 30% higher than minimum calories", "SuppressWarnings",
								1);
					} else {
						try {
							Statement st;
							String query = "UPDATE AppConfig SET valor = '" + caloriasMin
									+ "' WHERE idConfiguracion = 1";
							st = conn.createStatement();
							st.executeUpdate(query);

							query = "UPDATE AppConfig SET valor = '" + caloriasMax + "' WHERE idConfiguracion = 2";
							st = conn.createStatement();
							st.executeUpdate(query);

							JOptionPane.showMessageDialog(null,
									"Data has been saved successfully and will be taken into account for the next menus.",
									"SuppressWarnings", 1);
						} catch (SQLException ex) {
							ex.printStackTrace();
							System.out.println(ex);
							JOptionPane.showMessageDialog(null, "Data couldn't be saved", "SuppressWarnings", 0);
						}

						dispose();
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
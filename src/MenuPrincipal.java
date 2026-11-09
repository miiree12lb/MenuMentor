import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

class MenuPrincipal extends JFrame {
	JButton ingredienteButton, platoButton, menuButton, caloriasButton;
	JPanel mainPanel;
	GridBagLayout menuLayout;
	Connection conn;

	public MenuPrincipal(Connection conn) {
		this.conn = conn;
		initDisplay();
		initBotones();
		initPantalla();
	}

	Color azulOscuro = new Color(37, 40, 80);

	private void initDisplay() {
	}

	private void initBotones() {
		// Boton de Añadir ingredientes
		ingredienteButton = new JButton("New Ingredient");
		ingredienteButton.setBackground(Color.WHITE);
		ingredienteButton.setForeground(Color.BLACK);
		ingredienteButton.setBorder(new LineBorder(Color.WHITE));
		ingredienteButton.setPreferredSize(new Dimension(250, 50));
		ingredienteButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		// Añadir icono al botón
		ImageIcon icon = new ImageIcon(this.getClass().getResource("ingrediente.png"));
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		ingredienteButton.setIcon(icon);
		// Añadir evento al botón
		ingredienteButton.addActionListener(new ButtonsClickListener(this));
		ingredienteButton.setActionCommand("ingrediente");

		ingredienteButton.setHorizontalAlignment(SwingConstants.LEFT);

		// boton de añadir platos
		platoButton = new JButton("New Course");
		platoButton.setBackground(Color.WHITE);
		platoButton.setForeground(Color.BLACK);
		platoButton.setBorder(new LineBorder(Color.WHITE));
		platoButton.setPreferredSize(new Dimension(250, 50));
		platoButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		// añadir icono
		icon = new ImageIcon(this.getClass().getResource("plato.png"));
		img = icon.getImage();
		newimg = img.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		platoButton.setIcon(icon);

		platoButton.addActionListener(new ButtonsClickListener(this));
		platoButton.setActionCommand("plato");

		platoButton.setHorizontalAlignment(SwingConstants.LEFT);

		// boton de añadir calorias
		caloriasButton = new JButton("Edit diary calories");
		caloriasButton.setBackground(Color.WHITE);
		caloriasButton.setForeground(Color.BLACK);
		caloriasButton.setBorder(new LineBorder(Color.WHITE));
		caloriasButton.setPreferredSize(new Dimension(250, 50));
		caloriasButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		// añadir icono
		icon = new ImageIcon(this.getClass().getResource("calorias.png"));
		img = icon.getImage();
		newimg = img.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		caloriasButton.setIcon(icon);

		caloriasButton.addActionListener(new ButtonsClickListener(this));
		caloriasButton.setActionCommand("calorias");

		caloriasButton.setHorizontalAlignment(SwingConstants.LEFT);

		// boton de menú semanal
		menuButton = new JButton("Weekly menu");
		menuButton.setBackground(Color.WHITE);
		menuButton.setForeground(Color.BLACK);
		menuButton.setBorder(new LineBorder(Color.WHITE));

		menuButton.setPreferredSize(new Dimension(250, 50));
		menuButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));

		// poner icono al botón
		icon = new ImageIcon(this.getClass().getResource("menu.png"));
		img = icon.getImage();
		newimg = img.getScaledInstance(30, 40, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		menuButton.setIcon(icon);

		menuButton.addActionListener(new ButtonsClickListener(this));
		menuButton.setActionCommand("menu");

		menuButton.setHorizontalAlignment(SwingConstants.LEFT);
	}

	private void initPantalla() {
		setTitle("Main Menu");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// poner icono personalizado a la app;
		ImageIcon icon = new ImageIcon(this.getClass().getResource("icono.png"));
		Image img = icon.getImage();
		setIconImage(img);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (dimension.getWidth());
		int y = (int) (dimension.getHeight());
		setLocation(x, y);

		// creacion de los insets y las caracteristicas del GridBagLayout del mainPanel
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
		menuLayout = new GridBagLayout();
		mainPanel.setBackground(azulOscuro);
		mainPanel.setLayout(menuLayout);
		add(mainPanel);

		// añadir el botón de ingredientes en el panel principal
		mainPanel.add(ingredienteButton, constraints);

		// añadir el botón de platos en el panel principal
		constraints.gridy = 1;
		mainPanel.add(platoButton, constraints);

		// añadir el panel de las calorias en el panel principal
		constraints.gridy = 2;
		mainPanel.add(caloriasButton, constraints);

		// añadir el botón de menú semanal en el panel principal
		constraints.gridy = 3;
		mainPanel.add(menuButton, constraints);

		setVisible(true);
	}

	private class ButtonsClickListener implements ActionListener {
		JFrame frame;

		public ButtonsClickListener(JFrame frame) {
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("ingrediente")) {
				IngredienteNuevo ventana = new IngredienteNuevo(conn);
				ventana = null;
			} else if (command.equals("plato")) {
				PlatoNuevo ventana = new PlatoNuevo(conn);
				ventana = null;
			} else if (command.equals("calorias")) {
				EditarCalorias ventana = new EditarCalorias(conn);
				ventana = null;
			} else if (command.equals("menu")) {
				MenuSemanal ventana = new MenuSemanal(conn, this.frame);
				ventana = null;
			}
		}
	}
}
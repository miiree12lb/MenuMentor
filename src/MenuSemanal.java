import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import javax.swing.DefaultListModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.Cursor;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class MenuSemanal extends JFrame {
	JButton generarButton, consultarButton, eliminarButton, homeButton, recetaPrimeroButton, recetaSegundoButton;
	JLabel lunesLabel, almuerzoLabel, cenaLabel, seleccionLabel, ingredientePrimeroLabel, ingredienteSegundoLabel,
			caloriasLabel, numCaloriasLabel, nombrePrimeroLabel, nombreSegundoLabel, caloriasPrimeroLabel,
			numCaloriasPrimeroLabel, caloriasSegundoLabel, numCaloriasSegundoLabel, fechasLabel;
	JPanel mainPanel, menuPanel, almuerzoListaPanel, cenaListaPanel, infoPanel, listaPrimeroPanel, listaSegundoPanel,
			voidPanel, semanaPanel, caloriasPanel, caloriasPrimeroPanel, caloriasSegundoPanel;
	JComboBox semanaSeleccionComboBox;
	JList ingredientePrimeroLista, ingredienteSegundoLista;
	JScrollPane scrollPane, scrollPane1;
	GridBagLayout mainGridBagLayout, menuGridBagLayout, infoGridBagLayout;
	FlowLayout semanaFlowLayout, caloriasFlowLayout, caloriasPrimeroFlowLayout, caloriasSegundoFlowLayout;
	JMenuBar mb;
	JMenu menu1, modificarMenu;
	JMenuItem listaIngredienteItem, datosCaloricosItem, requisitosItem, intercambioItem, modificarItem, agregarItem,
			eliminarItem, habilitarItem, modificarIngItem, agregarIngItem, eliminarIngItem, caloriasItem,
			menuPrincipalItem;
	JFrame parent;
	Connection conn;
	// botones tabla
	JButton lunesPrimeroAlmuerzoButton, lunesSegundoAlmuerzoButton, martesPrimeroAlmuerzoButton,
			martesSegundoAlmuerzoButton, miercolesPrimeroAlmuerzoButton, miercolesSegundoAlmuerzoButton,
			juevesPrimeroAlmuerzoButton, juevesSegundoAlmuerzoButton, viernesPrimeroAlmuerzoButton,
			viernesSegundoAlmuerzoButton, sabadoPrimeroAlmuerzoButton, sabadoSegundoAlmuerzoButton,
			domingoPrimeroAlmuerzoButton, domingoSegundoAlmuerzoButton;
	JButton lunesPrimeroCenaButton, lunesSegundoCenaButton, martesPrimeroCenaButton, martesSegundoCenaButton,
			miercolesPrimeroCenaButton, miercolesSegundoCenaButton, juevesPrimeroCenaButton, juevesSegundoCenaButton,
			viernesPrimeroCenaButton, viernesSegundoCenaButton, sabadoPrimeroCenaButton, sabadoSegundoCenaButton,
			domingoPrimeroCenaButton, domingoSegundoCenaButton;
	TableCellRenderer tableAlmuerzoRenderer, tableCenaRenderer;
	JTable almuerzoTable, cenaTable;
	DefaultListModel modeloIngredientePrimeroLista, modeloIngredienteSegundoLista;
	Date hoyDate, lastMondayDate, nextMondayDate, menuDate;
	Calendar calendar;
	Menu_Semanal menuSemanal;
	JTableButtonModel modeloAlmuerzoTable, modeloCenaTable;
	URI uri;
	int diaSeleccionado = -1, comidaSeleccionada = -1;

	public MenuSemanal(Connection conn, JFrame parent) {
		menuSemanal = new Menu_Semanal(conn);
		this.conn = conn;
		this.parent = parent;
		hoyDate = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(hoyDate);

		if (calendar.get(Calendar.DAY_OF_WEEK) == 0) {
			int dia = 7;
			int deltaDays = dia - 2;

			calendar.add(Calendar.DAY_OF_WEEK, -deltaDays);
			lastMondayDate = calendar.getTime();

			calendar.setTime(lastMondayDate);
			calendar.add(Calendar.DAY_OF_WEEK, 7);
			nextMondayDate = calendar.getTime();
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			int dia = 8;
			int deltaDays = dia - 2;

			calendar.add(Calendar.DAY_OF_WEEK, -deltaDays);
			lastMondayDate = calendar.getTime();

			calendar.setTime(lastMondayDate);
			calendar.add(Calendar.DAY_OF_WEEK, 7);
			nextMondayDate = calendar.getTime();
		} else {
			int deltaDays = calendar.get(Calendar.DAY_OF_WEEK) - 2; // El lunes es el 2. Queremos la diferencia de dias
																	// respecto el lunes anterior.

			calendar.add(Calendar.DAY_OF_WEEK, -deltaDays);
			lastMondayDate = calendar.getTime();

			calendar.setTime(lastMondayDate);
			calendar.add(Calendar.DAY_OF_WEEK, 7);
			nextMondayDate = calendar.getTime();
		}

		initDisplay();
		initButtons();
		initScreen();
	}

	Color azulClaro = new Color(131, 181, 221);
	Color azulOscuro = new Color(37, 40, 80);

	private void initDisplay() {
		lunesLabel = new JLabel("Week:");
		lunesLabel.setBackground(azulOscuro);
		lunesLabel.setForeground(azulClaro);
		lunesLabel.setBorder(new LineBorder(azulOscuro));
		lunesLabel.setPreferredSize(new Dimension(200, 30));
		lunesLabel.setOpaque(true);
		lunesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lunesLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		fechasLabel = new JLabel(" ");
		fechasLabel.setBackground(azulOscuro);
		fechasLabel.setForeground(azulClaro);
		fechasLabel.setBorder(new LineBorder(azulOscuro));
		fechasLabel.setPreferredSize(new Dimension(200, 30));
		fechasLabel.setOpaque(true);
		fechasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fechasLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		almuerzoLabel = new JLabel("Lunch");
		almuerzoLabel.setBackground(azulOscuro);
		almuerzoLabel.setForeground(azulClaro);
		almuerzoLabel.setBorder(new LineBorder(azulOscuro));
		almuerzoLabel.setPreferredSize(new Dimension(200, 30));
		almuerzoLabel.setOpaque(true);
		almuerzoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		almuerzoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		cenaLabel = new JLabel("Dinner");
		cenaLabel.setBackground(azulOscuro);
		cenaLabel.setForeground(azulClaro);
		cenaLabel.setBorder(new LineBorder(azulOscuro));
		cenaLabel.setPreferredSize(new Dimension(200, 30));
		cenaLabel.setOpaque(true);
		cenaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cenaLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

		seleccionLabel = new JLabel("Meal information");
		seleccionLabel.setBackground(azulClaro);
		seleccionLabel.setForeground(Color.BLACK);
		seleccionLabel.setBorder(new LineBorder(azulClaro));
		seleccionLabel.setOpaque(true);
		seleccionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		seleccionLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

		ingredientePrimeroLabel = new JLabel("First Course");
		ingredientePrimeroLabel.setBackground(azulClaro);
		ingredientePrimeroLabel.setForeground(Color.BLACK);
		ingredientePrimeroLabel.setBorder(new LineBorder(azulClaro));
		ingredientePrimeroLabel.setOpaque(true);
		ingredientePrimeroLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ingredientePrimeroLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		nombrePrimeroLabel = new JLabel("            ");
		nombrePrimeroLabel.setBackground(azulClaro);
		nombrePrimeroLabel.setForeground(Color.BLACK);
		nombrePrimeroLabel.setBorder(new LineBorder(azulClaro));
		nombrePrimeroLabel.setOpaque(true);

		ingredienteSegundoLabel = new JLabel("Second Course");
		ingredienteSegundoLabel.setBackground(azulClaro);
		ingredienteSegundoLabel.setForeground(Color.BLACK);
		ingredienteSegundoLabel.setBorder(new LineBorder(azulClaro));
		ingredienteSegundoLabel.setOpaque(true);
		ingredienteSegundoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ingredienteSegundoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		nombreSegundoLabel = new JLabel("            ");
		nombreSegundoLabel.setBackground(azulClaro);
		nombreSegundoLabel.setForeground(Color.BLACK);
		nombreSegundoLabel.setBorder(new LineBorder(azulClaro));
		nombreSegundoLabel.setOpaque(true);

		caloriasPrimeroLabel = new JLabel("kcal of the first course:");
		caloriasPrimeroLabel.setBackground(azulClaro);
		caloriasPrimeroLabel.setForeground(Color.BLACK);
		caloriasPrimeroLabel.setBorder(new LineBorder(azulClaro));
		caloriasPrimeroLabel.setOpaque(true);
		caloriasPrimeroLabel.setHorizontalAlignment(SwingConstants.CENTER);
		caloriasPrimeroLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		numCaloriasPrimeroLabel = new JLabel();
		numCaloriasPrimeroLabel.setBackground(azulClaro);
		numCaloriasPrimeroLabel.setForeground(Color.BLACK);
		numCaloriasPrimeroLabel.setBorder(new LineBorder(azulClaro));
		numCaloriasPrimeroLabel.setOpaque(true);
		numCaloriasPrimeroLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numCaloriasPrimeroLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		caloriasSegundoLabel = new JLabel("kcal of the second course:");
		caloriasSegundoLabel.setBackground(azulClaro);
		caloriasSegundoLabel.setForeground(Color.BLACK);
		caloriasSegundoLabel.setBorder(new LineBorder(azulClaro));
		caloriasSegundoLabel.setOpaque(true);
		caloriasSegundoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		caloriasSegundoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		numCaloriasSegundoLabel = new JLabel();
		numCaloriasSegundoLabel.setBackground(azulClaro);
		numCaloriasSegundoLabel.setForeground(Color.BLACK);
		numCaloriasSegundoLabel.setBorder(new LineBorder(azulClaro));
		numCaloriasSegundoLabel.setOpaque(true);
		numCaloriasSegundoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numCaloriasSegundoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		caloriasLabel = new JLabel("kcal ingested from food:");
		caloriasLabel.setBackground(azulClaro);
		caloriasLabel.setForeground(Color.BLACK);
		caloriasLabel.setBorder(new LineBorder(azulClaro));
		caloriasLabel.setOpaque(true);
		caloriasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		caloriasLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		numCaloriasLabel = new JLabel();
		numCaloriasLabel.setBackground(azulClaro);
		numCaloriasLabel.setForeground(Color.BLACK);
		numCaloriasLabel.setBorder(new LineBorder(azulClaro));
		numCaloriasLabel.setOpaque(true);
		numCaloriasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numCaloriasLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		modeloIngredientePrimeroLista = new DefaultListModel();
		ingredientePrimeroLista = new JList(modeloIngredientePrimeroLista);

		modeloIngredienteSegundoLista = new DefaultListModel();
		ingredienteSegundoLista = new JList(modeloIngredienteSegundoLista);

		String[] str4 = { "This week", "Next week" };
		semanaSeleccionComboBox = new JComboBox(str4);
		// semanaSeleccionComboBox.addItemListener(new ComboBoxClickListener());

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(ingredientePrimeroLista);
		ingredientePrimeroLista.setLayoutOrientation(JList.VERTICAL);

		scrollPane1 = new JScrollPane();
		scrollPane1.setViewportView(ingredienteSegundoLista);
		ingredienteSegundoLista.setLayoutOrientation(JList.VERTICAL);

		ingredientePrimeroLista.setPreferredSize(new Dimension(300, 400));
		ingredientePrimeroLista.setBackground(Color.WHITE);
		ingredientePrimeroLista.setForeground(Color.BLACK);

		ingredienteSegundoLista.setPreferredSize(new Dimension(300, 400));
		ingredienteSegundoLista.setBackground(Color.WHITE);
		ingredienteSegundoLista.setForeground(Color.BLACK);

		ImageIcon icon = new ImageIcon(this.getClass().getResource("configuracion.png"));
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		mb = new JMenuBar();
		setJMenuBar(mb);
		menu1 = new JMenu();
		menu1.setIcon(icon);
		mb.add(menu1);

		listaIngredienteItem = new JMenuItem("Ingredients needed during the week");
		listaIngredienteItem.addActionListener(new ButtonsClickListener(this));
		listaIngredienteItem.setEnabled(false);
		menu1.add(listaIngredienteItem);

		datosCaloricosItem = new JMenuItem("Caloric Data");
		datosCaloricosItem.addActionListener(new ButtonsClickListener(this));
		datosCaloricosItem.setEnabled(false);
		menu1.add(datosCaloricosItem);

		requisitosItem = new JMenuItem("List of unfulfilled requirements");
		requisitosItem.addActionListener(new ButtonsClickListener(this));
		requisitosItem.setEnabled(false);
		menu1.add(requisitosItem);

		intercambioItem = new JMenuItem("Exchange of proposed menus");
		intercambioItem.addActionListener(new ButtonsClickListener(this));
		intercambioItem.setEnabled(false);
		menu1.add(intercambioItem);

		modificarMenu = new JMenu("Modify Data");
		menu1.add(modificarMenu);

		modificarItem = new JMenuItem("Modify Course");
		modificarItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(modificarItem);

		agregarItem = new JMenuItem("Add Course");
		agregarItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(agregarItem);

		eliminarItem = new JMenuItem("Delete Course");
		eliminarItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(eliminarItem);

		habilitarItem = new JMenuItem("Enable disabled courses");
		habilitarItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(habilitarItem);

		modificarIngItem = new JMenuItem("Modify ingredient");
		modificarIngItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(modificarIngItem);

		agregarIngItem = new JMenuItem("Add ingredient");
		agregarIngItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(agregarIngItem);

		eliminarIngItem = new JMenuItem("Delete ingredient");
		eliminarIngItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(eliminarIngItem);

		caloriasItem = new JMenuItem("Modify calories");
		caloriasItem.addActionListener(new ButtonsClickListener(this));
		modificarMenu.add(caloriasItem);

		menuPrincipalItem = new JMenuItem("Main Menu");
		menuPrincipalItem.addActionListener(new ButtonsClickListener(this));
		menu1.add(menuPrincipalItem);

		mb.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	private void initButtons() {
		Dimension d = new Dimension(100, 25);
		generarButton = new JButton("Generate");
		generarButton.setBackground(Color.WHITE);
		generarButton.setForeground(Color.BLACK);
		generarButton.setBorder(new LineBorder(Color.BLACK));
		generarButton.addActionListener(new ButtonsClickListener(this));
		generarButton.setMinimumSize(d);
		generarButton.setMaximumSize(d);
		generarButton.setPreferredSize(d);

		consultarButton = new JButton("Consult");
		consultarButton.setBackground(Color.WHITE);
		consultarButton.setForeground(Color.BLACK);
		consultarButton.setBorder(new LineBorder(Color.BLACK));
		consultarButton.addActionListener(new ButtonsClickListener(this));
		consultarButton.setMinimumSize(d);
		consultarButton.setMaximumSize(d);
		consultarButton.setPreferredSize(d);

		eliminarButton = new JButton("Delete");
		eliminarButton.setBackground(Color.WHITE);
		eliminarButton.setForeground(Color.BLACK);
		eliminarButton.setBorder(new LineBorder(Color.BLACK));
		eliminarButton.addActionListener(new ButtonsClickListener(this));
		eliminarButton.setMinimumSize(d);
		eliminarButton.setMaximumSize(d);
		eliminarButton.setPreferredSize(d);

		recetaPrimeroButton = new JButton("Recipe for the first course");
		recetaPrimeroButton.setBackground(Color.WHITE);
		recetaPrimeroButton.setForeground(Color.BLACK);
		recetaPrimeroButton.setBorder(new LineBorder(Color.BLACK));
		recetaPrimeroButton.addActionListener(new OpenUrlAction());

		recetaSegundoButton = new JButton("Recipe for the second course");
		recetaSegundoButton.setBackground(Color.WHITE);
		recetaSegundoButton.setForeground(Color.BLACK);
		recetaSegundoButton.setBorder(new LineBorder(Color.BLACK));
		recetaSegundoButton.addActionListener(new OpenUrlAction());

		ImageIcon icon = new ImageIcon(this.getClass().getResource("home.png"));
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		homeButton = new JButton(icon);
		homeButton.setPreferredSize(new Dimension(55, 55));
		homeButton.addActionListener(new ButtonsClickListener(this));
	}

	private void initScreen() {
		setTitle("Weekly Menu");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		setMinimumSize(new Dimension(1250, 700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// poner icono personalizado a la app;
		ImageIcon icon = new ImageIcon(this.getClass().getResource("icono.png"));
		Image img = icon.getImage();
		setIconImage(img);

		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = insets;

		menuPanel = new JPanel();
		menuGridBagLayout = new GridBagLayout();
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setLayout(menuGridBagLayout);

		semanaPanel = new JPanel();
		semanaPanel.setBackground(azulOscuro);
		semanaFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		semanaPanel.setLayout(semanaFlowLayout);

		semanaPanel.add(lunesLabel, constraints);
		semanaPanel.add(semanaSeleccionComboBox, constraints);
		semanaPanel.add(generarButton, constraints);
		semanaPanel.add(consultarButton, constraints);
		semanaPanel.add(eliminarButton, constraints);

		menuPanel.add(semanaPanel, constraints);

		constraints.gridy = 1;
		menuPanel.add(fechasLabel, constraints);

		constraints.gridy = 2;
		menuPanel.add(almuerzoLabel, constraints);

		constraints.gridy = 3;
		// Creación de la tabla de almuerzos
		lunesPrimeroAlmuerzoButton = new JButton(" ");
		lunesPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		lunesPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		lunesPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		martesPrimeroAlmuerzoButton = new JButton(" ");
		martesPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		martesPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		martesPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		miercolesPrimeroAlmuerzoButton = new JButton(" ");
		miercolesPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		miercolesPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		miercolesPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		juevesPrimeroAlmuerzoButton = new JButton(" ");
		juevesPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		juevesPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		juevesPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		viernesPrimeroAlmuerzoButton = new JButton(" ");
		viernesPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		viernesPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		viernesPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		sabadoPrimeroAlmuerzoButton = new JButton(" ");
		sabadoPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		sabadoPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		sabadoPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		domingoPrimeroAlmuerzoButton = new JButton(" ");
		domingoPrimeroAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		domingoPrimeroAlmuerzoButton.setBackground(Color.WHITE);
		domingoPrimeroAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		// *************************************************************************

		lunesSegundoAlmuerzoButton = new JButton(" ");
		lunesSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		lunesSegundoAlmuerzoButton.setBackground(Color.WHITE);
		lunesSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		martesSegundoAlmuerzoButton = new JButton(" ");
		martesSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		martesSegundoAlmuerzoButton.setBackground(Color.WHITE);
		martesSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		miercolesSegundoAlmuerzoButton = new JButton(" ");
		miercolesSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		miercolesSegundoAlmuerzoButton.setBackground(Color.WHITE);
		miercolesSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		juevesSegundoAlmuerzoButton = new JButton(" ");
		juevesSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		juevesSegundoAlmuerzoButton.setBackground(Color.WHITE);
		juevesSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		viernesSegundoAlmuerzoButton = new JButton(" ");
		viernesSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		viernesSegundoAlmuerzoButton.setBackground(Color.WHITE);
		viernesSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		sabadoSegundoAlmuerzoButton = new JButton(" ");
		sabadoSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		sabadoSegundoAlmuerzoButton.setBackground(Color.WHITE);
		sabadoSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		domingoSegundoAlmuerzoButton = new JButton(" ");
		domingoSegundoAlmuerzoButton.addActionListener(new ButtonsClickListener(this));
		domingoSegundoAlmuerzoButton.setBackground(Color.WHITE);
		domingoSegundoAlmuerzoButton.setBorder(new LineBorder(Color.WHITE));

		// *************************************************************************

		String[] nombreColumns1 = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		Object[][] almuerzoData = {
				{ lunesPrimeroAlmuerzoButton, martesPrimeroAlmuerzoButton, miercolesPrimeroAlmuerzoButton,
						juevesPrimeroAlmuerzoButton, viernesPrimeroAlmuerzoButton, sabadoPrimeroAlmuerzoButton,
						domingoPrimeroAlmuerzoButton },
				{ lunesSegundoAlmuerzoButton, martesSegundoAlmuerzoButton, miercolesSegundoAlmuerzoButton,
						juevesSegundoAlmuerzoButton,
						viernesSegundoAlmuerzoButton, sabadoSegundoAlmuerzoButton, domingoSegundoAlmuerzoButton }
		};
		modeloAlmuerzoTable = new JTableButtonModel(nombreColumns1, almuerzoData);
		almuerzoTable = new JTable(modeloAlmuerzoTable);
		almuerzoTable.addMouseListener(new JTableButtonMouseListener(almuerzoTable));
		tableAlmuerzoRenderer = almuerzoTable.getDefaultRenderer(JButton.class);
		almuerzoTable.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableAlmuerzoRenderer));
		almuerzoTable.setPreferredScrollableViewportSize(new Dimension(900, 130));
		almuerzoTable.getTableHeader().setReorderingAllowed(false);
		almuerzoTable.getTableHeader().setResizingAllowed(false);
		almuerzoTable.setRowHeight(65);

		JScrollPane scrollPane_1 = new JScrollPane(almuerzoTable);

		almuerzoListaPanel = new JPanel();

		// Agregamos el scrollpanel al contenedor
		almuerzoListaPanel.add(scrollPane_1, BorderLayout.CENTER);

		menuPanel.add(almuerzoListaPanel, constraints);
		menuPanel.setBackground(azulOscuro);

		constraints.gridy = 4;
		menuPanel.add(cenaLabel, constraints);

		constraints.gridy = 5;

		// Creación de la tabla de cenas
		lunesPrimeroCenaButton = new JButton(" ");
		lunesPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		lunesPrimeroCenaButton.setBackground(Color.WHITE);
		lunesPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		martesPrimeroCenaButton = new JButton(" ");
		martesPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		martesPrimeroCenaButton.setBackground(Color.WHITE);
		martesPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		miercolesPrimeroCenaButton = new JButton(" ");
		miercolesPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		miercolesPrimeroCenaButton.setBackground(Color.WHITE);
		miercolesPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		juevesPrimeroCenaButton = new JButton(" ");
		juevesPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		juevesPrimeroCenaButton.setBackground(Color.WHITE);
		juevesPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		viernesPrimeroCenaButton = new JButton(" ");
		viernesPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		viernesPrimeroCenaButton.setBackground(Color.WHITE);
		viernesPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		sabadoPrimeroCenaButton = new JButton(" ");
		sabadoPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		sabadoPrimeroCenaButton.setBackground(Color.WHITE);
		sabadoPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		domingoPrimeroCenaButton = new JButton(" ");
		domingoPrimeroCenaButton.addActionListener(new ButtonsClickListener(this));
		domingoPrimeroCenaButton.setBackground(Color.WHITE);
		domingoPrimeroCenaButton.setBorder(new LineBorder(Color.WHITE));

		// ***********************************************************************

		lunesSegundoCenaButton = new JButton(" ");
		lunesSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		lunesSegundoCenaButton.setBackground(Color.WHITE);
		lunesSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		martesSegundoCenaButton = new JButton(" ");
		martesSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		martesSegundoCenaButton.setBackground(Color.WHITE);
		martesSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		miercolesSegundoCenaButton = new JButton(" ");
		miercolesSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		miercolesSegundoCenaButton.setBackground(Color.WHITE);
		miercolesSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		juevesSegundoCenaButton = new JButton(" ");
		juevesSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		juevesSegundoCenaButton.setBackground(Color.WHITE);
		juevesSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		viernesSegundoCenaButton = new JButton(" ");
		viernesSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		viernesSegundoCenaButton.setBackground(Color.WHITE);
		viernesSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		sabadoSegundoCenaButton = new JButton(" ");
		sabadoSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		sabadoSegundoCenaButton.setBackground(Color.WHITE);
		sabadoSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		domingoSegundoCenaButton = new JButton(" ");
		domingoSegundoCenaButton.addActionListener(new ButtonsClickListener(this));
		domingoSegundoCenaButton.setBackground(Color.WHITE);
		domingoSegundoCenaButton.setBorder(new LineBorder(Color.WHITE));

		String[] nombreColumns2 = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		Object[][] cenaData = {
				{ lunesPrimeroCenaButton, martesPrimeroCenaButton, miercolesPrimeroCenaButton, juevesPrimeroCenaButton,
						viernesPrimeroCenaButton,
						sabadoPrimeroCenaButton, domingoPrimeroCenaButton },

				{ lunesSegundoCenaButton, martesSegundoCenaButton, miercolesSegundoCenaButton, juevesSegundoCenaButton,
						viernesSegundoCenaButton,
						sabadoSegundoCenaButton, domingoSegundoCenaButton }
		};
		modeloCenaTable = new JTableButtonModel(nombreColumns2, cenaData);
		cenaTable = new JTable(modeloCenaTable);
		cenaTable.addMouseListener(new JTableButtonMouseListener(cenaTable));
		tableCenaRenderer = cenaTable.getDefaultRenderer(JButton.class);
		cenaTable.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableCenaRenderer));
		cenaTable.setPreferredScrollableViewportSize(new Dimension(900, 130));
		cenaTable.getTableHeader().setReorderingAllowed(false);
		cenaTable.getTableHeader().setResizingAllowed(false);
		cenaTable.setRowHeight(65);

		JScrollPane scrollPane_2 = new JScrollPane(cenaTable);

		cenaListaPanel = new JPanel();

		// Agregamos el scrollpanel al contenedor
		cenaListaPanel.add(scrollPane_2, BorderLayout.CENTER);
		menuPanel.add(cenaListaPanel, constraints);

		// creacion del panel principal
		mainPanel = new JPanel();
		mainGridBagLayout = new GridBagLayout();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(mainGridBagLayout);
		add(mainPanel);

		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		mainPanel.add(menuPanel, constraints);

		// Creación del panel de información
		infoPanel = new JPanel();
		infoGridBagLayout = new GridBagLayout();
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(infoGridBagLayout);
		infoPanel.setBackground(azulClaro);
		infoPanel.setSize(getHeight(), 400);

		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.gridwidth = 2;
		infoPanel.add(seleccionLabel, constraints);

		constraints.gridy = 1;
		constraints.gridwidth = 1;

		constraints.gridx = 1;

		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.gridwidth = 2;

		constraints.gridy = 3;
		infoPanel.add(ingredientePrimeroLabel, constraints);

		constraints.gridy = 4;
		infoPanel.add(recetaPrimeroButton, constraints);

		constraints.gridy = 5;
		infoPanel.add(nombrePrimeroLabel, constraints);

		constraints.gridy = 6;
		listaPrimeroPanel = new JPanel(new BorderLayout());
		listaPrimeroPanel.add(scrollPane);
		infoPanel.add(listaPrimeroPanel, constraints);

		constraints.gridy = 7;
		caloriasPrimeroPanel = new JPanel();
		caloriasPrimeroPanel.setBackground(azulClaro);
		caloriasPrimeroFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		caloriasPrimeroPanel.setLayout(caloriasPrimeroFlowLayout);

		caloriasPrimeroPanel.add(caloriasPrimeroLabel);
		caloriasPrimeroPanel.add(numCaloriasPrimeroLabel);

		infoPanel.add(caloriasPrimeroPanel, constraints);

		constraints.gridy = 8;
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		infoPanel.add(ingredienteSegundoLabel, constraints);

		constraints.gridy = 9;
		infoPanel.add(recetaSegundoButton, constraints);

		constraints.gridy = 10;
		infoPanel.add(nombreSegundoLabel, constraints);

		constraints.gridy = 11;
		listaSegundoPanel = new JPanel(new BorderLayout());
		listaSegundoPanel.add(scrollPane1);
		infoPanel.add(listaSegundoPanel, constraints);

		constraints.gridy = 12;
		caloriasSegundoPanel = new JPanel();
		caloriasSegundoPanel.setBackground(azulClaro);
		caloriasSegundoFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		caloriasSegundoPanel.setLayout(caloriasSegundoFlowLayout);

		caloriasSegundoPanel.add(caloriasSegundoLabel);
		caloriasSegundoPanel.add(numCaloriasSegundoLabel);

		infoPanel.add(caloriasSegundoPanel, constraints);

		constraints.gridy = 13;
		caloriasPanel = new JPanel();
		caloriasPanel.setBackground(azulClaro);
		caloriasFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		caloriasPanel.setLayout(caloriasFlowLayout);

		caloriasPanel.add(caloriasLabel);
		caloriasPanel.add(numCaloriasLabel);

		infoPanel.add(caloriasPanel, constraints);

		constraints.gridy = 14;
		voidPanel = new JPanel();
		voidPanel.setBackground(azulClaro);
		voidPanel.setSize(new Dimension(50, 500));

		constraints.gridy = 15;
		infoPanel.add(voidPanel, constraints);

		constraints.gridy = 16;
		constraints.gridx = 0;
		infoPanel.add(homeButton, constraints);

		// añadir el panel de información al panel principal
		constraints.gridy = 0;
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		mainPanel.add(infoPanel, constraints);

		setVisible(true);

		parent.setVisible(false);
	}

	private void actualizarModeloIngredientePrimeroLista(ArrayList<Ingrediente_Plato> list) {
		modeloIngredientePrimeroLista.clear();
		for (int i = 0; i < list.size(); i++) {
			char[] arr = list.get(i).getIngrediente().getNombre().toCharArray();
			arr[0] = Character.toUpperCase(arr[0]);
			modeloIngredientePrimeroLista.addElement(new String(arr) + ", " + list.get(i).getGramos() + " grams");
		}
	}

	private void actualizarModeloIngredienteSegundoLista(ArrayList<Ingrediente_Plato> list) {
		modeloIngredienteSegundoLista.clear();
		for (int i = 0; i < list.size(); i++) {
			char[] arr = list.get(i).getIngrediente().getNombre().toCharArray();
			arr[0] = Character.toUpperCase(arr[0]);
			modeloIngredienteSegundoLista.addElement(new String(arr) + ", " + list.get(i).getGramos() + " grams");
		}
	}

	private void actualzarBotones() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String sLunes = dateFormat.format(menuSemanal.getLunes());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(menuSemanal.getLunes());
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		Date domingo = calendar.getTime();
		String sDomingo = dateFormat.format(domingo);

		fechasLabel.setText(sLunes + " - " + sDomingo);

		// Hacer la comprobación de si los platos de cada comida cumplen los requisitos
		// para cambiarles el color
		for (int dia = 0; dia < 7; dia++) {
			menuSemanal.getMenusDiarios().get(dia).getAlmuerzo()
					.calcularRequisitosTemporalesNoCumplidos(menuSemanal.getLunes(), dia);
		}

		for (int dia = 0; dia < 7; dia++) {
			menuSemanal.getMenusDiarios().get(dia).getCena()
					.calcularRequisitosTemporalesNoCumplidos(menuSemanal.getLunes(), dia);
		}

		// poner en los botones de los jtables el nombre del plato correspondiente y
		// ponerle el color correspondiente segun cumpla o no los requisitos temporales
		if (menuSemanal.getMenusDiarios().get(0).getAlmuerzo().getPrimeroCumpleRequisitos())
			lunesPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			lunesPrimeroAlmuerzoButton.setForeground(Color.RED);
		lunesPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(0).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(1).getAlmuerzo().getPrimeroCumpleRequisitos())
			martesPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			martesPrimeroAlmuerzoButton.setForeground(Color.RED);
		martesPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(1).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(2).getAlmuerzo().getPrimeroCumpleRequisitos())
			miercolesPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			miercolesPrimeroAlmuerzoButton.setForeground(Color.RED);
		miercolesPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(2).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(3).getAlmuerzo().getPrimeroCumpleRequisitos())
			juevesPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			juevesPrimeroAlmuerzoButton.setForeground(Color.RED);
		juevesPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(3).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(4).getAlmuerzo().getPrimeroCumpleRequisitos())
			viernesPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			viernesPrimeroAlmuerzoButton.setForeground(Color.RED);
		viernesPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(4).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(5).getAlmuerzo().getPrimeroCumpleRequisitos())
			sabadoPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			sabadoPrimeroAlmuerzoButton.setForeground(Color.RED);
		sabadoPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(5).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(6).getAlmuerzo().getPrimeroCumpleRequisitos())
			domingoPrimeroAlmuerzoButton.setForeground(Color.BLACK);
		else
			domingoPrimeroAlmuerzoButton.setForeground(Color.RED);
		domingoPrimeroAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(6).getAlmuerzo().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(0).getAlmuerzo().getSegundoCumpleRequisitos())
			lunesSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			lunesSegundoAlmuerzoButton.setForeground(Color.RED);
		lunesSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(0).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(1).getAlmuerzo().getSegundoCumpleRequisitos())
			martesSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			martesSegundoAlmuerzoButton.setForeground(Color.RED);
		martesSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(1).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(2).getAlmuerzo().getSegundoCumpleRequisitos())
			miercolesSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			miercolesSegundoAlmuerzoButton.setForeground(Color.RED);
		miercolesSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(2).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(3).getAlmuerzo().getSegundoCumpleRequisitos())
			juevesSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			juevesSegundoAlmuerzoButton.setForeground(Color.RED);
		juevesSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(3).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(4).getAlmuerzo().getSegundoCumpleRequisitos())
			viernesSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			viernesSegundoAlmuerzoButton.setForeground(Color.RED);
		viernesSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(4).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(5).getAlmuerzo().getSegundoCumpleRequisitos())
			sabadoSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			sabadoSegundoAlmuerzoButton.setForeground(Color.RED);
		sabadoSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(5).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(6).getAlmuerzo().getSegundoCumpleRequisitos())
			domingoSegundoAlmuerzoButton.setForeground(Color.BLACK);
		else
			domingoSegundoAlmuerzoButton.setForeground(Color.RED);
		domingoSegundoAlmuerzoButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(6).getAlmuerzo().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(0).getCena().getPrimeroCumpleRequisitos())
			lunesPrimeroCenaButton.setForeground(Color.BLACK);
		else
			lunesPrimeroCenaButton.setForeground(Color.RED);
		lunesPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(0).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(1).getCena().getPrimeroCumpleRequisitos())
			martesPrimeroCenaButton.setForeground(Color.BLACK);
		else
			martesPrimeroCenaButton.setForeground(Color.RED);
		martesPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(1).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(2).getCena().getPrimeroCumpleRequisitos())
			miercolesPrimeroCenaButton.setForeground(Color.BLACK);
		else
			miercolesPrimeroCenaButton.setForeground(Color.RED);
		miercolesPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(2).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(3).getCena().getPrimeroCumpleRequisitos())
			juevesPrimeroCenaButton.setForeground(Color.BLACK);
		else
			juevesPrimeroCenaButton.setForeground(Color.RED);
		juevesPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(3).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(4).getCena().getPrimeroCumpleRequisitos())
			viernesPrimeroCenaButton.setForeground(Color.BLACK);
		else
			viernesPrimeroCenaButton.setForeground(Color.RED);
		viernesPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(4).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(5).getCena().getPrimeroCumpleRequisitos())
			sabadoPrimeroCenaButton.setForeground(Color.BLACK);
		else
			sabadoPrimeroCenaButton.setForeground(Color.RED);
		sabadoPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(5).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(6).getCena().getPrimeroCumpleRequisitos())
			domingoPrimeroCenaButton.setForeground(Color.BLACK);
		else
			domingoPrimeroCenaButton.setForeground(Color.RED);
		domingoPrimeroCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(6).getCena().getPrimero().getNombre()));

		if (menuSemanal.getMenusDiarios().get(0).getCena().getSegundoCumpleRequisitos())
			lunesSegundoCenaButton.setForeground(Color.BLACK);
		else
			lunesSegundoCenaButton.setForeground(Color.RED);
		lunesSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(0).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(1).getCena().getSegundoCumpleRequisitos())
			martesSegundoCenaButton.setForeground(Color.BLACK);
		else
			martesSegundoCenaButton.setForeground(Color.RED);
		martesSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(1).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(2).getCena().getSegundoCumpleRequisitos())
			miercolesSegundoCenaButton.setForeground(Color.BLACK);
		else
			miercolesSegundoCenaButton.setForeground(Color.RED);
		miercolesSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(2).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(3).getCena().getSegundoCumpleRequisitos())
			juevesSegundoCenaButton.setForeground(Color.BLACK);
		else
			juevesSegundoCenaButton.setForeground(Color.RED);
		juevesSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(3).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(4).getCena().getSegundoCumpleRequisitos())
			viernesSegundoCenaButton.setForeground(Color.BLACK);
		else
			viernesSegundoCenaButton.setForeground(Color.RED);
		viernesSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(4).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(5).getCena().getSegundoCumpleRequisitos())
			sabadoSegundoCenaButton.setForeground(Color.BLACK);
		else
			sabadoSegundoCenaButton.setForeground(Color.RED);
		sabadoSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(5).getCena().getSegundo().getNombre()));

		if (menuSemanal.getMenusDiarios().get(6).getCena().getSegundoCumpleRequisitos())
			domingoSegundoCenaButton.setForeground(Color.BLACK);
		else
			domingoSegundoCenaButton.setForeground(Color.RED);
		domingoSegundoCenaButton
				.setText(firstToUpper(menuSemanal.getMenusDiarios().get(6).getCena().getSegundo().getNombre()));

		modeloAlmuerzoTable.fireTableDataChanged();
		modeloCenaTable.fireTableDataChanged();
		diaSeleccionado = -1;
		comidaSeleccionada = -1;
	}

	private void rellenarListasAlmuerzo(int dia) {
		menuSemanal.calcularCalorias();
		if (menuSemanal.getMenusDiarios().size() == 7) {
			actualizarModeloIngredientePrimeroLista(
					menuSemanal.getMenusDiarios().get(dia).getAlmuerzo().getPrimero().getIngredientes());
			actualizarModeloIngredienteSegundoLista(
					menuSemanal.getMenusDiarios().get(dia).getAlmuerzo().getSegundo().getIngredientes());

			numCaloriasPrimeroLabel.setText(
					String.valueOf(menuSemanal.getMenusDiarios().get(dia).getAlmuerzo().getPrimero().getCalorias()));
			numCaloriasSegundoLabel.setText(
					String.valueOf(menuSemanal.getMenusDiarios().get(dia).getAlmuerzo().getSegundo().getCalorias()));
			numCaloriasLabel
					.setText(String.valueOf(menuSemanal.getMenusDiarios().get(dia).getAlmuerzo().getCalorias()));
		}
	}

	private void rellenarListasCena(int dia) {
		if (menuSemanal.getMenusDiarios().size() == 7) {
			actualizarModeloIngredientePrimeroLista(
					menuSemanal.getMenusDiarios().get(dia).getCena().getPrimero().getIngredientes());
			actualizarModeloIngredienteSegundoLista(
					menuSemanal.getMenusDiarios().get(dia).getCena().getSegundo().getIngredientes());

			numCaloriasPrimeroLabel.setText(
					String.valueOf(menuSemanal.getMenusDiarios().get(dia).getCena().getPrimero().getCalorias()));
			numCaloriasSegundoLabel.setText(
					String.valueOf(menuSemanal.getMenusDiarios().get(dia).getCena().getSegundo().getCalorias()));
			numCaloriasLabel.setText(String.valueOf(menuSemanal.getMenusDiarios().get(dia).getCena().getCalorias()));
		}
	}

	private class OpenUrlAction implements ActionListener {
		String url = null;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (comidaSeleccionada == 1) {
				if (e.getSource() == recetaPrimeroButton) {
					url = menuSemanal.getMenusDiarios().get(diaSeleccionado).getAlmuerzo().getPrimero().getLink();
				} else if (e.getSource() == recetaSegundoButton) {
					url = menuSemanal.getMenusDiarios().get(diaSeleccionado).getAlmuerzo().getSegundo().getLink();
				}
			} else if (comidaSeleccionada == 2) {
				if (e.getSource() == recetaPrimeroButton) {
					url = menuSemanal.getMenusDiarios().get(diaSeleccionado).getCena().getPrimero().getLink();
				} else if (e.getSource() == recetaSegundoButton) {
					url = menuSemanal.getMenusDiarios().get(diaSeleccionado).getCena().getSegundo().getLink();
				}
			}
			if (url != null) {
				try {
					uri = new URI(url);
					open(uri);
				} catch (URISyntaxException ex) {
					ex.printStackTrace();
					System.out.println(ex);
				}
			}
		}
	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
	}

	private class ButtonsClickListener implements ActionListener {
		JFrame frame;

		public ButtonsClickListener(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();

			if (e.getSource() == listaIngredienteItem) {
				ListaIngredientes ventana = new ListaIngredientes(conn, this.frame, menuSemanal);

				ventana = null;
			} else if (e.getSource() == datosCaloricosItem) {
				InformacionCalorica ventana = new InformacionCalorica(conn, menuSemanal);

				ventana = null;
			} else if (e.getSource() == requisitosItem) {
				Requisitos ventana = new Requisitos(conn, this.frame, menuSemanal);

				ventana = null;
			} else if (e.getSource() == intercambioItem) {
				IntercambioMenus ventana = new IntercambioMenus(conn, menuSemanal);

				ventana = null;
				actualzarBotones();
			} else if (e.getSource() == modificarItem) {
				ModificarPlato ventana = new ModificarPlato(conn);

				ventana = null;

				if (menuSemanal.getLunes() != null) {
					menuSemanal.BDLeer(menuSemanal.getLunes());
					actualzarBotones();
					modeloIngredientePrimeroLista.clear();
					modeloIngredienteSegundoLista.clear();
					numCaloriasPrimeroLabel.setText("");
					numCaloriasSegundoLabel.setText("");
					numCaloriasLabel.setText("");
					nombrePrimeroLabel.setText(" ");
					nombreSegundoLabel.setText(" ");
				}

			} else if (e.getSource() == agregarItem) {
				PlatoNuevo ventana = new PlatoNuevo(conn);

				ventana = null;
			} else if (e.getSource() == eliminarItem) {
				EliminarPlato ventana = new EliminarPlato(conn);

				ventana = null;
			} else if (e.getSource() == habilitarItem) {
				HabilitarPlatos ventana = new HabilitarPlatos(conn);

				ventana = null;
			} else if (e.getSource() == modificarIngItem) {
				ModificarIngrediente ventana = new ModificarIngrediente(conn);
			} else if (e.getSource() == agregarIngItem) {
				IngredienteNuevo ventana = new IngredienteNuevo(conn);

				ventana = null;
			} else if (e.getSource() == eliminarIngItem) {
				EliminarIngrediente ventana = new EliminarIngrediente(conn);

				ventana = null;
			} else if (e.getSource() == caloriasItem) {
				EditarCalorias ventana = new EditarCalorias(conn);

				ventana = null;
			} else if (e.getSource() == menuPrincipalItem) {
				parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
				parent.setLocation(0, 0);
				parent.setVisible(true);

				dispose();
			} else if (e.getSource() == homeButton) {
				parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
				parent.setLocation(0, 0);
				parent.setVisible(true);

				dispose();
			} else if (e.getSource() == generarButton) {
				try {
					Statement st;
					String query;

					// Comprobar que la aplicacion tiene mas de 20 platos
					int count = 0;
					query = "SELECT COUNT(*) AS numTI FROM Platos WHERE habilitado = 1";
					st = conn.createStatement();
					ResultSet rs = st.executeQuery(query);
					while (rs.next()) {
						count = rs.getInt("numTI");
					}

					if (count < 20) {
						JOptionPane.showMessageDialog(null, "At least 20 courses are needed to generate the menu",
								"SuppressWarnings", 1);
					}

					else {
						// Comprobar que el usuario ha actualizado los requisitos de calorias diarias
						query = "SELECT * FROM AppConfig";
						st = conn.createStatement();
						rs = st.executeQuery(query);
						boolean zero = false;
						while (rs.next()) {
							if (rs.getInt("valor") == 0) {
								zero = true;
							}
						}
						if (zero) {
							JOptionPane.showMessageDialog(null, "Add calory requirements", "SuppressWarnings", 1);
						} else {
							menuDate = new Date();
							if (semanaSeleccionComboBox.getSelectedIndex() == 0)
								menuDate = lastMondayDate;
							else
								menuDate = nextMondayDate;

							menuSemanal.setLunes(menuDate);

							if (menuSemanal.BDLeer(menuDate))
								JOptionPane.showMessageDialog(null, "The menu is generated", "SuppressWarnings", 1);
							else {

								MenuSemanal.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
								menuSemanal.generarMenuSemanal();

								// poner en los botones de los jtables el nombre del plato correspondiente
								actualzarBotones();

								listaIngredienteItem.setEnabled(true);
								requisitosItem.setEnabled(true);
								datosCaloricosItem.setEnabled(true);
								intercambioItem.setEnabled(true);

								menuSemanal.calcularCalorias();

								MenuSemanal.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
					System.out.println(ex);
				}
			}

			else if (e.getSource() == consultarButton) {
				menuDate = new Date();
				if (semanaSeleccionComboBox.getSelectedIndex() == 0)
					menuDate = lastMondayDate;
				else
					menuDate = nextMondayDate;

				menuSemanal.setLunes(menuDate);
				if (!menuSemanal.BDLeer(menuDate)) {
					menuSemanal.getMenusDiarios().clear();
					JOptionPane.showMessageDialog(null, "The menu isn't generated", "SuppressWarnings", 1);
				} else {
					actualzarBotones();

					listaIngredienteItem.setEnabled(true);
					requisitosItem.setEnabled(true);
					datosCaloricosItem.setEnabled(true);
					intercambioItem.setEnabled(true);

					menuSemanal.calcularCalorias();

					modeloIngredientePrimeroLista.clear();
					modeloIngredienteSegundoLista.clear();
					nombrePrimeroLabel.setText("  ");
					nombreSegundoLabel.setText("  ");
					numCaloriasLabel.setText(" ");
					numCaloriasPrimeroLabel.setText(" ");
					numCaloriasSegundoLabel.setText(" ");
				}
			} else if (e.getSource() == eliminarButton) {
				// Comprobar si el menú se ha generado/consultado
				if (fechasLabel.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "generate or consult the menu", "SuppressWarnings", 1);
				} else {
					int result = JOptionPane.showConfirmDialog(null,
							"The selected menu (" + fechasLabel.getText()
									+ ") will be deleted permanently.\nDo you want to continue?",
							"SuppressWarnings", JOptionPane.OK_CANCEL_OPTION, 2);
					if (result == JOptionPane.OK_OPTION) {
						MenuSemanal.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

						menuSemanal.BDEliminar();

						fechasLabel.setText(" ");

						lunesPrimeroAlmuerzoButton.setText(" ");
						martesPrimeroAlmuerzoButton.setText(" ");
						miercolesPrimeroAlmuerzoButton.setText(" ");
						juevesPrimeroAlmuerzoButton.setText(" ");
						viernesPrimeroAlmuerzoButton.setText(" ");
						sabadoPrimeroAlmuerzoButton.setText(" ");
						domingoPrimeroAlmuerzoButton.setText(" ");

						lunesSegundoAlmuerzoButton.setText(" ");
						martesSegundoAlmuerzoButton.setText(" ");
						miercolesSegundoAlmuerzoButton.setText(" ");
						juevesSegundoAlmuerzoButton.setText(" ");
						viernesSegundoAlmuerzoButton.setText(" ");
						sabadoSegundoAlmuerzoButton.setText(" ");
						domingoSegundoAlmuerzoButton.setText(" ");

						lunesPrimeroCenaButton.setText(" ");
						martesPrimeroCenaButton.setText(" ");
						miercolesPrimeroCenaButton.setText(" ");
						juevesPrimeroCenaButton.setText(" ");
						viernesPrimeroCenaButton.setText(" ");
						sabadoPrimeroCenaButton.setText(" ");
						domingoPrimeroCenaButton.setText(" ");

						lunesSegundoCenaButton.setText(" ");
						martesSegundoCenaButton.setText(" ");
						miercolesSegundoCenaButton.setText(" ");
						juevesSegundoCenaButton.setText(" ");
						viernesSegundoCenaButton.setText(" ");
						sabadoSegundoCenaButton.setText(" ");
						domingoSegundoCenaButton.setText(" ");

						listaIngredienteItem.setEnabled(false);
						requisitosItem.setEnabled(false);
						datosCaloricosItem.setEnabled(false);
						intercambioItem.setEnabled(false);

						menuSemanal = null;

						menuSemanal = new Menu_Semanal(conn);

						modeloAlmuerzoTable.fireTableDataChanged();
						modeloCenaTable.fireTableDataChanged();

						MenuSemanal.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}

			else if (e.getSource() == lunesPrimeroAlmuerzoButton || e.getSource() == lunesSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(lunesPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(lunesSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(0);
				diaSeleccionado = 0;
				comidaSeleccionada = 1;
			} else if (e.getSource() == martesPrimeroAlmuerzoButton || e.getSource() == martesSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(martesPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(martesSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(1);
				diaSeleccionado = 1;
				comidaSeleccionada = 1;
			} else if (e.getSource() == miercolesPrimeroAlmuerzoButton
					|| e.getSource() == miercolesSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(miercolesPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(miercolesSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(2);
				diaSeleccionado = 2;
				comidaSeleccionada = 1;
			} else if (e.getSource() == juevesPrimeroAlmuerzoButton || e.getSource() == juevesSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(juevesPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(juevesSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(3);
				diaSeleccionado = 3;
				comidaSeleccionada = 1;
			}

			else if (e.getSource() == viernesPrimeroAlmuerzoButton || e.getSource() == viernesSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(viernesPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(viernesSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(4);
				diaSeleccionado = 4;
				comidaSeleccionada = 1;
			} else if (e.getSource() == sabadoPrimeroAlmuerzoButton || e.getSource() == sabadoSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(sabadoPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(sabadoSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(5);
				diaSeleccionado = 5;
				comidaSeleccionada = 1;
			} else if (e.getSource() == domingoPrimeroAlmuerzoButton || e.getSource() == domingoSegundoAlmuerzoButton) {
				nombrePrimeroLabel.setText(domingoPrimeroAlmuerzoButton.getText());
				nombreSegundoLabel.setText(domingoSegundoAlmuerzoButton.getText());
				rellenarListasAlmuerzo(6);
				diaSeleccionado = 6;
				comidaSeleccionada = 1;
			}

			else if (e.getSource() == lunesPrimeroCenaButton || e.getSource() == lunesSegundoCenaButton) {
				nombrePrimeroLabel.setText(lunesPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(lunesSegundoCenaButton.getText());
				rellenarListasCena(0);
				diaSeleccionado = 0;
				comidaSeleccionada = 2;
			} else if (e.getSource() == martesPrimeroCenaButton || e.getSource() == martesSegundoCenaButton) {
				nombrePrimeroLabel.setText(martesPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(martesSegundoCenaButton.getText());
				rellenarListasCena(1);
				diaSeleccionado = 1;
				comidaSeleccionada = 2;
			} else if (e.getSource() == miercolesPrimeroCenaButton || e.getSource() == miercolesSegundoCenaButton) {
				nombrePrimeroLabel.setText(miercolesPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(miercolesSegundoCenaButton.getText());
				rellenarListasCena(2);
				diaSeleccionado = 2;
				comidaSeleccionada = 2;
			} else if (e.getSource() == juevesPrimeroCenaButton || e.getSource() == juevesSegundoCenaButton) {
				nombrePrimeroLabel.setText(juevesPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(juevesSegundoCenaButton.getText());
				rellenarListasCena(3);
				diaSeleccionado = 3;
				comidaSeleccionada = 2;
			} else if (e.getSource() == viernesPrimeroCenaButton || e.getSource() == viernesSegundoCenaButton) {
				nombrePrimeroLabel.setText(viernesPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(viernesSegundoCenaButton.getText());
				rellenarListasCena(4);
				diaSeleccionado = 4;
				comidaSeleccionada = 2;
			} else if (e.getSource() == sabadoPrimeroCenaButton || e.getSource() == sabadoSegundoCenaButton) {
				nombrePrimeroLabel.setText(sabadoPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(sabadoSegundoCenaButton.getText());
				rellenarListasCena(5);
				diaSeleccionado = 5;
				comidaSeleccionada = 2;
			} else if (e.getSource() == domingoPrimeroCenaButton || e.getSource() == domingoSegundoCenaButton) {
				nombrePrimeroLabel.setText(domingoPrimeroCenaButton.getText());
				nombreSegundoLabel.setText(domingoSegundoCenaButton.getText());
				rellenarListasCena(6);
				diaSeleccionado = 6;
				comidaSeleccionada = 2;
			}
		}
	}

	private String firstToUpper(String str) {
		char[] arr = str.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		return new String(arr);
	}

	class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
				Object value = table.getValueAt(row, column);
				if (value instanceof JButton) {
					((JButton) value).doClick();
				}
			}
		}
	}

	class JTableButtonRenderer implements TableCellRenderer {
		private TableCellRenderer defaultRenderer;

		public JTableButtonRenderer(TableCellRenderer renderer) {
			defaultRenderer = renderer;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value instanceof Component)
				return (Component) value;
			return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	class JTableButtonModel extends AbstractTableModel {

		String[] columns;
		Object[][] rows;

		public JTableButtonModel(String[] columnTitles, Object[][] dataEntries) {
			rows = dataEntries;
			columns = columnTitles;
		}

		public String getColumnName(int column) {
			return columns[column];
		}

		public int getRowCount() {
			return rows.length;
		}

		public int getColumnCount() {
			return columns.length;
		}

		public Object getValueAt(int row, int column) {
			return rows[row][column];
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}
	}
}
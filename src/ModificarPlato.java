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
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.text.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class ModificarPlato extends JDialog {
   JLabel anadirIngredienteLabel, eliminarIngredienteLabel, ingredienteLabel, linkLabel, cantidadLabel, frecuenciaLabel,
         cuandoComerLabel, platoLabel, tiempoEsperaLabel, nombreLabel;
   JList<CheckboxListItem> ingredienteEliminarLista;
   JList ingredienteBuscadoLista, platoBuscadoLista;
   JTextField ingredienteTextField, cantidadTextField, frecuenciaTextField, platoTextField, tiempoEsperaTextField,
         nombreTextField, linkTextField;
   JCheckBox almuerzoCheckBox, cenaCheckBox, primeroCheckBox, segundoCheckBox;
   JButton nuevoButton, agregarButton, okButton, xButton, eliminarButton, buscarPlatoButton, buscarIngredienteButton,
         actualizarButton;
   JPanel mainPanel, ingredientePanel, cantidadPanel, cuandoComerPanel, linkPanel, frecuenciaPanel, buttonsPanel,
         listaPanel, listaBuscadoPanel, platoPanel, agregarPanel, eliminarPanel, listaPlatoBuscadoPanel,
         tiempoEsperaPanel, nombrePanel;
   JScrollPane scrollPane, scrollPane1, scrollPane2;
   GridBagLayout mainLayout, cuandoComerGridBagLayout, agregarGridBagLayout, eliminarGridBagLayout;
   FlowLayout ingredienteFlowLayout, cantidadFlowLayout, linkFlowLayout, frecuenciaFlowLayout, buttonsFlowLayout,
         platoFlowLayout, tiempoEsperaFlowLayout, nombreFlowLayout;
   BorderLayout listaBorderLayout;
   Boolean selected, ingredienteModificado;
   Connection conn;
   DefaultListModel modeloIngredienteBuscadoLista, modeloPlatoBuscadoLista;
   DefaultListModel<CheckboxListItem> modeloIngredienteEliminarLista;
   Plato plato;
   ArrayList<Integer> selectedValues;

   public ModificarPlato(Connection conn) {
      this.conn = conn;
      plato = new Plato(conn);
      selectedValues = new ArrayList<Integer>();
      ingredienteModificado = false;
      initDisplay();
      initButtons();
      initScreen();
   }

   Color azulClaro = new Color(131, 181, 221);
   Color azulOscuro = new Color(37, 40, 80);

   private void initDisplay() {
      platoLabel = new JLabel("Name of the course:");
      platoLabel.setBackground(azulClaro);
      platoLabel.setForeground(Color.BLACK);
      platoLabel.setOpaque(true);
      platoLabel.setBorder(new LineBorder(azulClaro));
      platoLabel.setPreferredSize(new Dimension(200, 20));
      platoLabel.setHorizontalAlignment(SwingConstants.LEFT);

      linkLabel = new JLabel("Link:");
      linkLabel.setBackground(azulClaro);
      linkLabel.setForeground(Color.BLACK);
      linkLabel.setOpaque(true);
      linkLabel.setBorder(new LineBorder(azulClaro));
      linkLabel.setPreferredSize(new Dimension(150, 20));
      linkLabel.setHorizontalAlignment(SwingConstants.LEFT);

      nombreLabel = new JLabel("Edit name:");
      nombreLabel.setBackground(azulClaro);
      nombreLabel.setForeground(Color.BLACK);
      nombreLabel.setOpaque(true);
      nombreLabel.setBorder(new LineBorder(azulClaro));
      nombreLabel.setPreferredSize(new Dimension(150, 20));
      nombreLabel.setHorizontalAlignment(SwingConstants.LEFT);

      anadirIngredienteLabel = new JLabel("Add ingredient");
      anadirIngredienteLabel.setBackground(azulOscuro);
      anadirIngredienteLabel.setForeground(azulClaro);
      anadirIngredienteLabel.setBorder(new LineBorder(azulOscuro));
      anadirIngredienteLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
      anadirIngredienteLabel.setOpaque(true);
      anadirIngredienteLabel.setHorizontalAlignment(SwingConstants.LEFT);

      eliminarIngredienteLabel = new JLabel("Delete ingredient");
      eliminarIngredienteLabel.setBackground(azulOscuro);
      eliminarIngredienteLabel.setForeground(azulClaro);
      eliminarIngredienteLabel.setBorder(new LineBorder(azulOscuro));
      eliminarIngredienteLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
      eliminarIngredienteLabel.setOpaque(true);
      eliminarIngredienteLabel.setHorizontalAlignment(SwingConstants.LEFT);

      ingredienteLabel = new JLabel("Ingredient:");
      ingredienteLabel.setBackground(azulOscuro);
      ingredienteLabel.setForeground(Color.WHITE);
      ingredienteLabel.setBorder(new LineBorder(azulOscuro));
      ingredienteLabel.setPreferredSize(new Dimension(200, 20));
      ingredienteLabel.setOpaque(true);
      ingredienteLabel.setHorizontalAlignment(SwingConstants.LEFT);

      cantidadLabel = new JLabel("Quantity (g):");
      cantidadLabel.setBackground(azulOscuro);
      cantidadLabel.setForeground(Color.WHITE);
      cantidadLabel.setBorder(new LineBorder(azulOscuro));
      cantidadLabel.setPreferredSize(new Dimension(200, 20));
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
      frecuenciaLabel.setPreferredSize(new Dimension(230, 20));
      frecuenciaLabel.setOpaque(true);
      frecuenciaLabel.setHorizontalAlignment(SwingConstants.LEFT);

      tiempoEsperaLabel = new JLabel("Waiting time (days):");
      tiempoEsperaLabel.setBackground(azulClaro);
      tiempoEsperaLabel.setForeground(Color.BLACK);
      tiempoEsperaLabel.setBorder(new LineBorder(azulClaro));
      tiempoEsperaLabel.setPreferredSize(new Dimension(230, 20));
      tiempoEsperaLabel.setOpaque(true);
      tiempoEsperaLabel.setHorizontalAlignment(SwingConstants.LEFT);

      platoTextField = new JTextField(15);
      linkTextField = new JTextField(25);
      nombreTextField = new JTextField(25);
      ingredienteTextField = new JTextField(15);
      frecuenciaTextField = new JTextField(15);
      // impedir la escritura de caracteres que no sean digitos
      frecuenciaTextField.setDocument(new LimitadorCaracteres());
      cantidadTextField = new JTextField(15);
      // Impedir que se puedan escribir caracteres que no sean digitos
      cantidadTextField.setDocument(new LimitadorCaracteres());
      tiempoEsperaTextField = new JTextField(15);
      // solo poder escribir digitos
      tiempoEsperaTextField.setDocument(new LimitadorCaracteres());

      modeloIngredienteBuscadoLista = new DefaultListModel();
      actualizarModeloIngredienteBuscadoLista();
      ingredienteBuscadoLista = new JList(modeloIngredienteBuscadoLista);

      scrollPane1 = new JScrollPane();
      scrollPane1.setViewportView(ingredienteBuscadoLista);
      ingredienteBuscadoLista.setLayoutOrientation(JList.VERTICAL);

      ingredienteBuscadoLista.setBackground(Color.WHITE);
      ingredienteBuscadoLista.setForeground(Color.BLACK);

      modeloPlatoBuscadoLista = new DefaultListModel();
      actualizarModeloPlatoBuscadoLista();
      platoBuscadoLista = new JList(modeloPlatoBuscadoLista);

      scrollPane2 = new JScrollPane();
      scrollPane2.setViewportView(platoBuscadoLista);
      platoBuscadoLista.setLayoutOrientation(JList.VERTICAL);

      platoBuscadoLista.setBackground(Color.WHITE);
      platoBuscadoLista.setForeground(Color.BLACK);

      // Creación de una lista modelo
      modeloIngredienteEliminarLista = new DefaultListModel<CheckboxListItem>();
      actualizarModeloIngredienteEliminarLista(plato.getIngredientes());
      // Creación
      ingredienteEliminarLista = new JList<CheckboxListItem>(modeloIngredienteEliminarLista);

      ingredienteEliminarLista.setCellRenderer(new CheckboxListRenderer());
      ingredienteEliminarLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      // Afegir un mouse listener per gestionar el canvi de propietats quan es clica
      ingredienteEliminarLista.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent event) {
            @SuppressWarnings("unchecked")
            JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();

            // Posició de l'element seleccionat
            int index = list.locationToIndex(event.getPoint());
            CheckboxListItem item = list.getModel().getElementAt(index);

            // Canvi d'estat
            item.setSelected(!item.isSelected());

            // Repintar la fila
            ingredienteEliminarLista.repaint(list.getCellBounds(index, index));

            selected = false;
            selectedValues.clear();
            for (int i = 0; i < list.getModel().getSize(); i++) {
               if (list.getModel().getElementAt(i).isSelected()) {
                  selected = true;
                  selectedValues.add(i);
               }
            }
         }
      });

      scrollPane = new JScrollPane();
      scrollPane.setViewportView(ingredienteEliminarLista);
      ingredienteEliminarLista.setLayoutOrientation(JList.VERTICAL);
      ingredienteEliminarLista.setBackground(Color.WHITE);
      ingredienteEliminarLista.setForeground(Color.BLACK);
   }

   private void initButtons() {
      // creación del botón 'agregar'
      agregarButton = new JButton("Add");
      agregarButton.setBackground(Color.WHITE);
      agregarButton.setBorder(new LineBorder(Color.BLACK));
      agregarButton.setForeground(Color.BLACK);
      agregarButton.setPreferredSize(new Dimension(70, 20));
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

      // creación del botón 'OK'
      okButton = new JButton("OK");
      okButton.setBackground(Color.WHITE);
      okButton.setBorder(new LineBorder(Color.BLACK));
      okButton.setForeground(Color.BLACK);
      okButton.setPreferredSize(new Dimension(30, 20));
      okButton.addActionListener(new ButtonsClickListener());
      okButton.setActionCommand("OK");

      // creación del botón 'X'
      xButton = new JButton("X");
      xButton.setBackground(Color.WHITE);
      xButton.setForeground(Color.RED);
      xButton.setBorder(new LineBorder(Color.BLACK));
      xButton.setPreferredSize(new Dimension(30, 20));
      xButton.addActionListener(new ButtonsClickListener());
      xButton.setActionCommand("cancel");

      eliminarButton = new JButton("Delete");
      eliminarButton.setBackground(Color.WHITE);
      eliminarButton.setForeground(Color.BLACK);
      eliminarButton.setBorder(new LineBorder(Color.BLACK));
      eliminarButton.setPreferredSize(new Dimension(70, 20));
      eliminarButton.addActionListener(new ButtonsClickListener());
      eliminarButton.setActionCommand("eliminar");

      buscarPlatoButton = new JButton("Search");
      buscarPlatoButton.setBackground(Color.WHITE);
      buscarPlatoButton.setForeground(Color.BLACK);
      buscarPlatoButton.setBorder(new LineBorder(Color.BLACK));
      buscarPlatoButton.setPreferredSize(new Dimension(60, 20));
      buscarPlatoButton.addActionListener(new ButtonsClickListener());
      buscarPlatoButton.setActionCommand("buscarPlato");

      buscarIngredienteButton = new JButton("Search");
      buscarIngredienteButton.setBackground(Color.WHITE);
      buscarIngredienteButton.setForeground(Color.BLACK);
      buscarIngredienteButton.setBorder(new LineBorder(Color.BLACK));
      buscarIngredienteButton.setPreferredSize(new Dimension(60, 20));
      buscarIngredienteButton.addActionListener(new ButtonsClickListener());
      buscarIngredienteButton.setActionCommand("buscarIngrediente");

      actualizarButton = new JButton("Update");
      actualizarButton.setBackground(Color.WHITE);
      actualizarButton.setForeground(Color.BLACK);
      actualizarButton.setBorder(new LineBorder(Color.BLACK));
      actualizarButton.setPreferredSize(new Dimension(80, 20));
      actualizarButton.addActionListener(new ButtonsClickListener());
      actualizarButton.setActionCommand("actualizar");

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

      primeroCheckBox = new JCheckBox("First Course", false);
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
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      setTitle("Modificar Plato");
      setSize(new Dimension((int) (dimension.getWidth() / 1.2), (int) (dimension.getHeight())));
      setResizable(false);
      setModal(true);

      int x = (int) ((dimension.getWidth() - getWidth()) / 2);
      int y = 0;
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

      platoPanel = new JPanel();
      platoFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      platoPanel.setBackground(azulClaro);
      platoPanel.setLayout(platoFlowLayout);

      platoPanel.add(platoLabel);
      platoPanel.add(platoTextField);
      platoPanel.add(buscarPlatoButton);
      platoPanel.add(actualizarButton);

      constraints.gridwidth = 1;
      mainPanel.add(platoPanel, constraints);

      nombrePanel = new JPanel();
      nombreFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      nombrePanel.setBackground(azulClaro);
      nombrePanel.setLayout(nombreFlowLayout);

      nombrePanel.add(nombreLabel);
      nombrePanel.add(nombreTextField);

      constraints.gridwidth = 1;
      constraints.gridy = 0;
      constraints.gridx = 1;
      mainPanel.add(nombrePanel, constraints);

      linkPanel = new JPanel();
      linkFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      linkPanel.setBackground(azulClaro);
      linkPanel.setLayout(linkFlowLayout);

      linkPanel.add(linkLabel);
      linkPanel.add(linkTextField);

      constraints.gridwidth = 1;
      constraints.gridy = 1;
      constraints.gridx = 1;
      mainPanel.add(linkPanel, constraints);

      constraints.gridy = 2;
      constraints.gridx = 0;
      constraints.gridwidth = 1;
      listaPlatoBuscadoPanel = new JPanel(new BorderLayout());
      listaPlatoBuscadoPanel.add(scrollPane2);
      mainPanel.add(listaPlatoBuscadoPanel, constraints);

      // Crear panel para agregar ingredientes
      agregarPanel = new JPanel();
      agregarGridBagLayout = new GridBagLayout();
      agregarPanel.setBackground(azulOscuro);
      agregarPanel.setLayout(agregarGridBagLayout);

      constraints.weighty = 0.0;
      constraints.weightx = 0.0;
      constraints.gridy = 0;
      agregarPanel.add(anadirIngredienteLabel, constraints);

      ingredientePanel = new JPanel();
      ingredienteFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      ingredientePanel.setBackground(azulOscuro);
      ingredientePanel.setLayout(ingredienteFlowLayout);

      ingredientePanel.add(ingredienteLabel);
      ingredientePanel.add(ingredienteTextField);
      ingredientePanel.add(buscarIngredienteButton);
      ingredientePanel.add(nuevoButton);

      constraints.gridy = 1;
      agregarPanel.add(ingredientePanel, constraints);

      constraints.gridy = 2;
      constraints.weighty = 1.0;
      listaBuscadoPanel = new JPanel(new BorderLayout());
      listaBuscadoPanel.add(scrollPane1);
      agregarPanel.add(listaBuscadoPanel, constraints);

      cantidadPanel = new JPanel();
      cantidadFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      cantidadPanel.setBackground(azulOscuro);
      cantidadPanel.setLayout(cantidadFlowLayout);

      cantidadPanel.add(cantidadLabel);
      cantidadPanel.add(cantidadTextField);
      cantidadPanel.add(agregarButton);

      constraints.gridy = 3;
      constraints.weighty = 0.0;
      agregarPanel.add(cantidadPanel, constraints);

      // Crear el panel para eliminar ingredientes
      eliminarPanel = new JPanel();
      eliminarGridBagLayout = new GridBagLayout();
      eliminarPanel.setBackground(azulOscuro);
      eliminarPanel.setLayout(eliminarGridBagLayout);

      constraints.gridy = 0;
      eliminarPanel.add(eliminarIngredienteLabel, constraints);

      constraints.gridy = 1;
      constraints.weighty = 0.0;
      listaPanel = new JPanel(new BorderLayout());
      listaPanel.add(scrollPane);
      constraints.weightx = 1.0;
      listaPanel.setPreferredSize(new Dimension(400, 150));
      eliminarPanel.add(listaPanel, constraints);

      constraints.gridy = 2;
      JPanel eliminarButtonPanel = new JPanel();
      FlowLayout eliminarButtonFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      eliminarButtonPanel.setBackground(azulOscuro);
      eliminarButtonPanel.setLayout(eliminarButtonFlowLayout);
      eliminarButtonPanel.add(eliminarButton);

      eliminarPanel.add(eliminarButtonPanel, constraints);

      // Crear panel de cuando se quiere comer el plato
      cuandoComerPanel = new JPanel();
      cuandoComerGridBagLayout = new GridBagLayout();
      cuandoComerPanel.setBackground(azulClaro);
      cuandoComerPanel.setLayout(cuandoComerGridBagLayout);

      constraints.gridy = 0;
      constraints.weightx = 0.0;
      cuandoComerPanel.add(cuandoComerLabel, constraints);

      constraints.gridy = 1;
      cuandoComerPanel.add(almuerzoCheckBox, constraints);

      constraints.gridy = 2;
      cuandoComerPanel.add(cenaCheckBox, constraints);

      constraints.gridy = 1;
      constraints.gridx = 1;
      cuandoComerPanel.add(primeroCheckBox, constraints);

      constraints.gridy = 2;
      constraints.gridx = 1;
      cuandoComerPanel.add(segundoCheckBox, constraints);

      frecuenciaPanel = new JPanel();
      frecuenciaFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      frecuenciaPanel.setBackground(azulClaro);
      frecuenciaPanel.setLayout(frecuenciaFlowLayout);

      frecuenciaPanel.add(frecuenciaLabel);
      frecuenciaPanel.add(frecuenciaTextField);

      constraints.gridx = 0;
      constraints.gridy = 3;
      cuandoComerPanel.add(frecuenciaPanel, constraints);

      tiempoEsperaPanel = new JPanel();
      tiempoEsperaFlowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
      tiempoEsperaPanel.setBackground(azulClaro);
      tiempoEsperaPanel.setLayout(tiempoEsperaFlowLayout);

      tiempoEsperaPanel.add(tiempoEsperaLabel);
      tiempoEsperaPanel.add(tiempoEsperaTextField);

      constraints.gridy = 4;
      constraints.gridx = 0;
      cuandoComerPanel.add(tiempoEsperaPanel, constraints);

      // Añadir los paneles al panel principal
      constraints.gridy = 3;
      constraints.weightx = 0.0;
      constraints.weighty = 0.0;
      mainPanel.add(agregarPanel, constraints);

      constraints.gridx = 1;
      mainPanel.add(eliminarPanel, constraints);

      constraints.gridy = 4;
      constraints.gridx = 0;
      mainPanel.add(cuandoComerPanel, constraints);

      buttonsPanel = new JPanel();
      buttonsFlowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
      buttonsPanel.setBackground(azulClaro);
      buttonsPanel.setLayout(buttonsFlowLayout);

      buttonsPanel.add(xButton);
      buttonsPanel.add(okButton);

      constraints.gridy = 5;
      constraints.gridx = 1;
      mainPanel.add(buttonsPanel, constraints);

      setVisible(true);
   }

   private void actualizarModeloPlatoBuscadoLista() {
      ResultSet rs;
      modeloPlatoBuscadoLista.clear();
      try {
         Statement st;
         String query = "SELECT nombre FROM Platos WHERE nombre LIKE '%" + platoTextField.getText().toLowerCase()
               + "%' AND habilitado = 1 ORDER BY nombre ASC";
         st = conn.createStatement();
         rs = st.executeQuery(query);

         while (rs.next()) {
            String nombre = rs.getString("nombre");
            char[] arr = nombre.toCharArray();
            arr[0] = Character.toUpperCase(arr[0]);
            modeloPlatoBuscadoLista.addElement(new String(arr));
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
         System.out.println(ex);
      }
   }

   private void actualizarModeloIngredienteBuscadoLista() {
      ResultSet rs;
      modeloIngredienteBuscadoLista.clear();
      if (ingredienteTextField.hashCode() == 0) {
         try {
            Statement st;
            String query = "SELECT nombre FROM Ingredientes ORDER BY nombre ASC";
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
      } else {
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
   }

   private void actualizarModeloIngredienteEliminarLista(ArrayList<Ingrediente_Plato> ingredientesPlato) {
      modeloIngredienteEliminarLista.clear();
      for (int i = 0; i < ingredientesPlato.size(); i++) {
         String nombre = ingredientesPlato.get(i).getIngrediente().getNombre();
         char[] arr = nombre.toCharArray();
         arr[0] = Character.toUpperCase(arr[0]);
         modeloIngredienteEliminarLista.addElement(new CheckboxListItem(
               new String(arr) + ", " + String.valueOf(ingredientesPlato.get(i).getGramos()) + " grams"));
      }
   }

   private class ButtonsClickListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();
         if (command.equals("OK")) {
            if (platoBuscadoLista.isSelectionEmpty()) {
               JOptionPane.showMessageDialog(null, "Select course", "SuppressWarnings", 1);
            } else if (!almuerzoCheckBox.isSelected() && !cenaCheckBox.isSelected()) {
               JOptionPane.showMessageDialog(null, "Select lunch and/or dinner", "SuppressWarnings", 1);
            } else if (!primeroCheckBox.isSelected() && !segundoCheckBox.isSelected()) {
               JOptionPane.showMessageDialog(null, "Select first and/or second course", "SuppressWarnings", 1);
            } else if (frecuenciaTextField.getText().hashCode() == 0) {
               JOptionPane.showMessageDialog(null, "Add frecuency", "SuppressWarnings", 1);
            } else if (Integer.parseInt(frecuenciaTextField.getText()) == 0) {
               JOptionPane.showMessageDialog(null, "Frecuency must be higher than 0", "SuppressWarnings", 1);
            } else if (Integer.parseInt(frecuenciaTextField.getText()) > 28) {
               JOptionPane.showMessageDialog(null, "Frecuency can't be higher than 28", "SuppressWarnings", 1);
            } else if (tiempoEsperaTextField.getText().hashCode() == 0) {
               JOptionPane.showMessageDialog(null, "Add minimum waiting time", "SuppressWarnings", 1);
            } else if (Integer.parseInt(tiempoEsperaTextField.getText()) == 0) {
               JOptionPane.showMessageDialog(null, "Quantity must be higher than 0", "SuppressWarnings", 1);
            } else if (nombreTextField.getText().hashCode() == 0) {
               JOptionPane.showMessageDialog(null, "Add name", "SuppressWarnings", 1);
            } else if (plato.getIngredientes().size() == 0) {
               JOptionPane.showMessageDialog(null, "Add ingredient", "SuppressWarnings", 1);
            } else {
               plato.setNombre(nombreTextField.getText().toString().toLowerCase());
               plato.setLink(linkTextField.getText());
               plato.setAlmuerzo(almuerzoCheckBox.isSelected());
               plato.setCena(cenaCheckBox.isSelected());
               plato.setPrimero(primeroCheckBox.isSelected());
               plato.setSegundo(segundoCheckBox.isSelected());
               plato.setFrecuencia(Integer.parseInt(frecuenciaTextField.getText()));
               plato.setDiasEspera(Integer.parseInt(tiempoEsperaTextField.getText()));

               int actualizar = plato.BDActualizar(ingredienteModificado);

               if (actualizar == 0) {
                  JOptionPane.showMessageDialog(null, "Course updated", "SuppressWarnings", 1);
                  dispose();
               } else if (actualizar == 2) {
                  JOptionPane.showMessageDialog(null, "It exists a course with this name", "SuppressWarnings", 0);
               }

            }
         } else if (command.equals("buscarPlato")) {
            actualizarModeloPlatoBuscadoLista();
         } else if (command.equals("buscarIngrediente")) {
            actualizarModeloIngredienteBuscadoLista();
         } else if (command.equals("cancel")) {
            dispose();
         } else if (command.equals("nuevo")) {
            IngredienteNuevo ventana = new IngredienteNuevo(conn);

            ventana = null;
            actualizarModeloIngredienteBuscadoLista();
         } else if (command.equals("agregar")) {
            if (platoBuscadoLista.isSelectionEmpty()) {
               JOptionPane.showMessageDialog(null, "Select course", "SuppressWarnings", 1);
            } else if (ingredienteBuscadoLista.isSelectionEmpty()) {
               JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
            } else if (cantidadTextField.getText().hashCode() == 0) {
               JOptionPane.showMessageDialog(null, "Add quantity", "SuppressWarnings", 1);
            } else if (Integer.parseInt(cantidadTextField.getText()) == 0) {
               JOptionPane.showMessageDialog(null, "Quantity must be higher than 0", "SuppressWarnings", 1);
            } else {
               ingredienteModificado = true;
               int cont = 0;
               boolean found = false;

               while (cont < plato.getIngredientes().size() && !found) {
                  if (ingredienteBuscadoLista.getSelectedValue().toString().toLowerCase()
                        .equals(plato.getIngredientes().get(cont).getIngrediente().getNombre().toLowerCase())) {
                     found = true;
                  }
                  cont++;
               }

               if (!found) {
                  try {
                     Statement st;
                     String query;

                     query = "SELECT idIngrediente FROM Ingredientes WHERE LOWER(Ingredientes.nombre) = '"
                           + ingredienteBuscadoLista.getSelectedValue().toString().toLowerCase() + "'";
                     st = conn.createStatement();
                     ResultSet rs = st.executeQuery(query);

                     if (plato.addIngrediente(rs.getInt("idIngrediente"),
                           Integer.parseInt(cantidadTextField.getText()))) {
                        actualizarModeloIngredienteEliminarLista(plato.getIngredientes());
                     }
                  } catch (SQLException ex) {
                     ex.printStackTrace();
                     System.out.println(ex);
                  }
               } else {
                  JOptionPane.showMessageDialog(null, "You have already added the ingredient", "SuppressWarnings", 1);
               }
            }
         }

         else if (command.equals("eliminar")) {
            if (ingredienteEliminarLista.isSelectionEmpty()) {
               JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
            } else if (!selected) {
               JOptionPane.showMessageDialog(null, "Select ingredient", "SuppressWarnings", 1);
            } else {
               ingredienteModificado = true;
               for (int i = 0; i < modeloIngredienteEliminarLista.size(); i++) {
                  if (modeloIngredienteEliminarLista.get(i).isSelected()) {
                     String nombre = modeloIngredienteEliminarLista.getElementAt(i).toString().toLowerCase();
                     for (int j = 0; j < plato.getIngredientes().size(); j++) {
                        String nombre1 = plato.getIngredientes().get(j).getIngrediente().getNombre() + ", "
                              + plato.getIngredientes().get(j).getGramos() + " grams";
                        if (nombre.equals(nombre1)) {
                           plato.eliminarIngrediente(plato.getIngredientes().get(j).getIngrediente());
                        }
                     }
                  }
               }
               actualizarModeloIngredienteEliminarLista(plato.getIngredientes());
            }
         } else if (command.equals("actualizar")) {
            if (platoBuscadoLista.isSelectionEmpty()) {
               JOptionPane.showMessageDialog(null, "Select course", "SuppressWarnings", 1);
            } else {
               plato.clearIngredientes();
               plato.setNombre(platoBuscadoLista.getSelectedValue().toString());
               int read = plato.BDLeer();
               if (read == 0) {
                  nombreTextField.setText(plato.getNombre());
                  linkTextField.setText(plato.getLink());
                  almuerzoCheckBox.setSelected(plato.getAlmuerzo());
                  cenaCheckBox.setSelected(plato.getCena());
                  primeroCheckBox.setSelected(plato.getPrimero());
                  segundoCheckBox.setSelected(plato.getSegundo());
                  frecuenciaTextField.setText(String.valueOf(plato.getFrecuencia()));
                  tiempoEsperaTextField.setText(String.valueOf(plato.getDiasEspera()));
                  actualizarModeloIngredienteEliminarLista(plato.getIngredientes());
               }
            }
         }
      }
   }

   // Representa un elemento de la lista
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

   // Gestiona el renderizado de las filas con un checkbox
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
            value.setSelected(false);
         }
         return this;
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
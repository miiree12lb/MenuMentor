import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;

class Plato {
	private int id;
	private String nombre;
	private String link;
	private ArrayList<Ingrediente_Plato> ingredientes;
	private Boolean almuerzo;
	private Boolean cena;
	private Boolean primero;
	private Boolean segundo;
	private Boolean habilitado;
	private int frecuencia;
	private int diasEspera;
	private int calorias;
	private int veces;
	private ArrayList<Date> ultimasFechas;
	private Connection conn;

	// Este método crea un plato a partir de la conexión a la BBDD
	public Plato(Connection conn) {
		this.conn = conn;
		veces = 0;
		calorias = 0;
		ingredientes = new ArrayList<Ingrediente_Plato>();
		ultimasFechas = new ArrayList<Date>();
	}

	// Este método le asigna el id dado al plato
	public void setId(int id) {
		this.id = id;
	}

	// este método devuelve el id del plato
	public int getId() {
		return id;
	}

	// este metodo le asigna al plato el nombre dado
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// este método devuelve el nombre del plato
	public String getNombre() {
		return nombre;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	// este método le asigna el bool almuerzo al plato
	public void setAlmuerzo(Boolean almuerzo) {
		this.almuerzo = almuerzo;
	}

	// este metodo devuelve el bool almuerzo del plato
	public Boolean getAlmuerzo() {
		return almuerzo;
	}

	// este método le asigna el bool cena al plato
	public void setCena(Boolean cena) {
		this.cena = cena;
	}

	// este méodo devuelve el bool cena del plato
	public Boolean getCena() {
		return cena;
	}

	// este método le asigna el bool primero dado al plato
	public void setPrimero(Boolean primero) {
		this.primero = primero;
	}

	// este método devuelve el bool primero del plato
	public Boolean getPrimero() {
		return primero;
	}

	// este método le asigna el bool segundo dado al plato
	public void setSegundo(Boolean segundo) {
		this.segundo = segundo;
	}

	// este método devuelve el bool segundo del plato
	public Boolean getSegundo() {
		return segundo;
	}

	// este método le asigna la frecuencia dada al plato
	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	// este método devuelve el método frecuencia del plato
	public int getFrecuencia() {
		return frecuencia;
	}

	// este método calcula las calorías que hay en el plato sumando las calorias que
	// cada ingrediente_plato aporta
	public void calcularCalorias() {
		calorias = 0;
		for (int i = 0; i < ingredientes.size(); i++) {
			calorias += ingredientes.get(i).getCalorias();
		}
	}

	// este método devuelve las calorías del plato
	public int getCalorias() {
		return calorias;
	}

	// este método devuelve las veces que se ha comido el plato en los últimos 28
	// días
	public int getVeces() {
		return veces;
	}

	// este método añade una fecha a ultimasFechas y actualiza las veces que se ha
	// comido el plato los últimos 28 días
	public void setUltimasFechas(Date initialDate) {
		ultimasFechas.clear();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		String initDate = dateFormat.format(initialDate);
		try {
			String query = "SELECT fecha FROM PlatosFecha WHERE PlatosFecha.idPlato = '" + id
					+ "' AND PlatosFecha.fecha > '" + initDate + "' ORDER BY fecha ASC";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Date lastDate = new SimpleDateFormat("yyyy/MM/dd").parse(rs.getString("fecha"));
				ultimasFechas.add(lastDate);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		// actualizar veces
		veces = ultimasFechas.size();
	}

	public void setUltimasFechas(Date initialDate, Date endDate) {
		ultimasFechas.clear();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		String initDate = dateFormat.format(initialDate);
		String sEndDate = dateFormat.format(endDate);

		try {
			String query = "SELECT fecha FROM PlatosFecha WHERE PlatosFecha.idPlato = '" + id
					+ "' AND PlatosFecha.fecha > '" + initDate + "' AND PlatosFecha.fecha < '" + sEndDate
					+ "' ORDER BY fecha ASC";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Date lastDate = new SimpleDateFormat("yyyy/MM/dd").parse(rs.getString("fecha"));
				ultimasFechas.add(lastDate);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		// actualizar veces
		veces = ultimasFechas.size();
	}

	// este método devuelve la lista de las ultimasFechas que se ha comido el plato
	public ArrayList<Date> getUltimasFechas() {
		return ultimasFechas;
	}

	// este método añade un ingrediente al plato dados el ingrediente y los gramos
	public boolean addIngrediente(int idIngrediente, int gramos) {
		Ingrediente_Plato ingredientePlato = new Ingrediente_Plato(conn, idIngrediente, gramos);
		Boolean found = false;
		for (int i = 0; i < ingredientes.size() && !found; i++) {
			if (idIngrediente == ingredientes.get(i).getIngrediente().getId()) {
				found = true;
			}
		}
		if (!found) {
			ingredientes.add(ingredientePlato);
			calcularCalorias();
			return true;
		}
		return false;
	}

	// Este método elimina un ingrediente de la lista de ingredientes del objeto y
	// calcula las calorías del plato
	public void eliminarIngrediente(Ingrediente ingrediente) {
		if (ingredientes.size() == 0)
			return;
		// eliminar Ingrediente
		boolean found = false;
		int cont = 0;
		while (cont < ingredientes.size() && !found) {
			if (ingrediente.getNombre().equals(ingredientes.get(cont).getIngrediente().getNombre())) {
				found = true;
				ingredientes.remove(cont);
			}

			cont++;
		}
		calcularCalorias();
	}

	// Este método vacía la lista de ingredientes
	public void clearIngredientes() {
		ingredientes.clear();
	}

	// Este método devuelve la lista de ingredientes
	public ArrayList<Ingrediente_Plato> getIngredientes() {
		return ingredientes;
	}

	// Este método asigna al plato el tiempo mínimo de espera para volver a ser
	// comido
	public void setDiasEspera(int diasEspera) {
		this.diasEspera = diasEspera;
	}

	// Este método devuelve el tiempo mínimo de espera
	public int getDiasEspera() {
		return diasEspera;
	}

	// Este método indica si el plato está habilitado o no. Si está habilitado se
	// puede seguir usando
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	// Este método devuelve el estado del boolean habilitado
	public Boolean getHabilitado() {
		return habilitado;
	}

	// Este método interambia las fechas en que se ha comido el plato en caso de que
	// se haga un cambio de menus propuestos
	public void BDCambioPlatosFecha(Date lunes, int diaAnterior, int diaNuevo) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lunes);
		calendar.add(Calendar.DAY_OF_WEEK, diaAnterior);
		Date fechaAnterior = calendar.getTime();

		calendar.setTime(lunes);
		calendar.add(Calendar.DAY_OF_WEEK, diaNuevo);
		Date fechaNueva = calendar.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String sDateAnterior = dateFormat.format(fechaAnterior);
		String sDateNueva = dateFormat.format(fechaNueva);

		try {
			String query = "UPDATE PlatosFecha SET fecha = '" + sDateNueva + "' WHERE idPlato = '" + id
					+ "' AND fecha = '" + sDateAnterior + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// Este método genera la lista de la compra o de los ingredientes necesarios
	// durante la comida, la lista la recibe por parámetro y la devuelve actualizada
	public ArrayList<Ingrediente_Compra> generarListaIngredientes(ArrayList<Ingrediente_Compra> list) {
		for (int i = 0; i < ingredientes.size(); i++) {
			boolean found = false;
			int j = 0;
			while (j < list.size() && !found) {
				if (ingredientes.get(i).getIngrediente().getNombre().equals(list.get(j).getIngrediente().getNombre())) {
					found = true;
					list.get(j).setGramos(list.get(j).getGramos() + ingredientes.get(i).getGramos());
				}
				j++;
			}
			if (!found) {
				list.add(new Ingrediente_Compra(ingredientes.get(i).getIngrediente(), ingredientes.get(i).getGramos()));
			}
		}
		return list;
	}

	// Este guarda el plato en la base de datos. Devuelve 0 si el plato se ha
	// guardado correctamente, 2 si el plato ya existe, 3 si el plato no está
	// habilitado o 1 si se produce un error con la base de datos
	public int BDGuardar() {
		try {
			Statement st;
			String query;

			query = "SELECT * FROM Platos WHERE Platos.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			int iAlmuerzo, iCena, iPrimero, iSegundo, iHabilitado;

			if (almuerzo)
				iAlmuerzo = 1;
			else
				iAlmuerzo = 0;

			if (cena)
				iCena = 1;
			else
				iCena = 0;

			if (primero)
				iPrimero = 1;
			else
				iPrimero = 0;

			if (segundo)
				iSegundo = 1;
			else
				iSegundo = 0;

			if (habilitado)
				iHabilitado = 1;
			else
				iHabilitado = 0;

			if (!rs.next()) {
				query = "INSERT INTO Platos(nombre, link, almuerzo, cena, primero, segundo, frecuencia, diasEspera, habilitado) VALUES ('"
						+ nombre.toLowerCase() + "', '" + link + "', '" + iAlmuerzo + "','" + iCena + "', '" + iPrimero
						+ "','" + iSegundo + "', '" + frecuencia + "','" + diasEspera + "', '" + iHabilitado + "')";
				st = conn.createStatement();
				st.executeUpdate(query);

				query = "SELECT * FROM Platos WHERE nombre = '" + nombre.toLowerCase() + "'";
				st = conn.createStatement();
				rs = st.executeQuery(query);

				for (int i = 0; i < ingredientes.size(); i++) {
					ingredientes.get(i).BDGuardar(rs.getInt("idPlato"), ingredientes.get(i).getIngrediente().getId());
				}
				return 0;
			}
			if (rs.getInt("habilitado") == 0)
				return 3;
			else
				return 2;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return 1;
		}
	}

	// Este método lee el plato de la BBDD. Devuelve 1 si se produce un error con la
	// BBDD y 0 si se puede leer
	public int BDLeer() {
		try {
			Statement st;
			String query;
			ResultSet rs;

			query = "SELECT * FROM Platos WHERE Platos.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (rs.next()) {
				this.id = rs.getInt("idPlato");
				this.link = rs.getString("link");

				if (rs.getInt("almuerzo") == 1)
					this.almuerzo = true;
				else
					this.almuerzo = false;

				if (rs.getInt("cena") == 1)
					this.cena = true;
				else
					this.cena = false;

				if (rs.getInt("primero") == 1)
					this.primero = true;
				else
					this.primero = false;

				if (rs.getInt("segundo") == 1)
					this.segundo = true;
				else
					this.segundo = false;

				this.frecuencia = rs.getInt("frecuencia");

				this.diasEspera = rs.getInt("diasEspera");
			}
			query = "SELECT Ingredientes.idIngrediente, gramos FROM IngredientesPlato, Ingredientes WHERE IngredientesPlato.idPlato = '"
					+ this.id + "' AND IngredientesPlato.idIngrediente = Ingredientes.idIngrediente";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				addIngrediente(rs.getInt("idIngrediente"), rs.getInt("gramos"));
			}

			return 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return 1;
		}
	}

	// Este método actualiza el plato en la BBDD. Recibe por parámetro un boolean
	// que indica si se ha modificado la lista de ingredientes del plato.
	// Devuelve 0 si se actualiza correctamente, 1 si se produce un error en la base
	// de datos y 2 si ya existe otro plato con el mismo nombre
	public int BDActualizar(boolean ingredienteModificado) {
		try {
			Statement st;
			String query;
			ResultSet rs;

			// Comporbar que no hay otro plato en la base de datos con el mismo nombre
			query = "SELECT * FROM Platos WHERE Platos.nombre = '" + nombre.toLowerCase() + "' AND Platos.idPlato != '"
					+ id + "'";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (!rs.next()) {
				int iAlmuerzo, iCena, iPrimero, iSegundo;

				if (almuerzo)
					iAlmuerzo = 1;
				else
					iAlmuerzo = 0;

				if (cena)
					iCena = 1;
				else
					iCena = 0;

				if (primero)
					iPrimero = 1;
				else
					iPrimero = 0;

				if (segundo)
					iSegundo = 1;
				else
					iSegundo = 0;

				// Actualizar los datos del plato excepto los ingredientes
				query = "UPDATE Platos SET nombre = '" + nombre.toLowerCase() + "', link = '" + link + "', almuerzo = '"
						+ iAlmuerzo + "', cena = '" + iCena + "', primero = '" + iPrimero + "', segundo = '" + iSegundo
						+ "', frecuencia = '" + frecuencia + "', diasEspera = '" + diasEspera
						+ "' WHERE Platos.idPlato = '" + id + "'";
				st = conn.createStatement();
				st.executeUpdate(query);

				if (ingredienteModificado) {
					// Borrar de la base de datos todod los ingredientes guardados para ese plato
					query = "DELETE FROM IngredientesPlato WHERE IngredientesPlato.idPlato = '" + id + "'";
					st = conn.createStatement();
					st.executeUpdate(query);

					// Insertar en la base de datos todos los ingredientes de la lista ingredientes
					for (int i = 0; i < ingredientes.size(); i++) {
						ingredientes.get(i).BDGuardar(id, ingredientes.get(i).getIngrediente().getId());
					}
				}

				return 0;
			}
			return 2;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return 1;
		}
	}

	// Este método elimina el plato de la BBDD. Devuelve 1 en caso de error y 0 en
	// caso de que funcione correctamente
	public int BDEliminar() {
		try {
			Statement st;
			String query;

			query = "SELECT idPlato FROM Platos WHERE Platos.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				id = rs.getInt("idPlato");
			}
			// Comprovar si el plat esta en el menu semanal

			else
				return 2;
			query = "DELETE FROM IngredientesPlato WHERE IngredientesPlato.idPlato = '" + id + "'";
			st = conn.createStatement();
			st.executeUpdate(query);

			query = "DELETE FROM Platos WHERE Platos.idPlato = '" + id + "'";
			st = conn.createStatement();
			st.executeUpdate(query);

			return 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return 1;
		}
	}

	// Guarda la fecha, la recibida por parámetro, en la que se ha comido el plato
	public int BDGuardarFecha(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String sDate = dateFormat.format(date);

			String query = "INSERT INTO PlatosFecha (idPlato, fecha) VALUES ('" + id + "', '" + sDate + "')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);

			return 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return 1;
		}
	}
}
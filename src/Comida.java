import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class Comida {
	private int id;
	private Plato primero;
	private int primeroFrecuenciaDeseada;
	private int primeroFrecuenciaReal;
	private int primeroDiasEsperaDeseado;
	private int primeroDiasEsperaReal;
	private Boolean primeroCumpleRequisitos;
	private Plato segundo;
	private int segundoFrecuenciaDeseada;
	private int segundoFrecuenciaReal;
	private int segundoDiasEsperaDeseado;
	private int segundoDiasEsperaReal;
	private Boolean segundoCumpleRequisitos;
	private int calorias;
	private Connection conn;

	// Este método crea un objeto comida a partir de la connexió a la BBDD, y deja
	// todos los otros atributos vacíos
	public Comida(Connection conn) {
		this.conn = conn;
	}

	// Este método crea un objeto comida a partir de la connexión a la BBDD y dos
	// Platos: primero y segundo.
	// También calcula las calorías de la comida
	public Comida(Connection conn, Plato primero, Plato segundo) {
		this.conn = conn;
		this.primero = primero;
		this.segundo = segundo;
		calcularCalorias();
	}

	// Este método le da un id el objeto comida correspondiente con el entero id que
	// recibe como parámetro
	public void setId(int id) {
		this.id = id;
	}

	// Este método devuelve el id del objeto comida
	public int getId() {
		return id;
	}

	// Este método calcula las calorías de la comida
	public void calcularCalorias() {
		primero.calcularCalorias();
		segundo.calcularCalorias();
		calorias = primero.getCalorias() + segundo.getCalorias();
	}

	// Este método es para asignarle un primer plato a la comida, ese plato es el
	// que recibe por parámetro
	public void setPrimero(Plato primero) {
		this.primero = primero;
	}

	// Este método devuelve el primer plato de la comida
	public Plato getPrimero() {
		return primero;
	}

	// Este método le asigna a la comida el plato recibido como parámetro como
	// segundo plato
	public void setSegundo(Plato segundo) {
		this.segundo = segundo;
	}

	// Este método devuelve el segundo plato de la comida
	public Plato getSegundo() {
		return segundo;
	}

	// Este método devuelve las calorías de la comida
	public int getCalorias() {
		return calorias;
	}

	// Este método llama al primer y al segundo plato para que generen la lista de
	// la compra
	public ArrayList<Ingrediente_Compra> generarListaIngredientes(ArrayList<Ingrediente_Compra> list) {
		list = primero.generarListaIngredientes(list);
		list = segundo.generarListaIngredientes(list);

		return list;

	}

	// Este método comprueba si los platos de la comida cumplen los requisitos
	// temporales del usuario y aquellos que no se cumplen los añade en la lista de
	// requisitos no cumplidos
	// Necesita como parámetros la fecha del lunes, un entero entre 0 y 6, los días
	// que han pasado respecto el día, y la lista donde añadir los requisitos
	// temporales no cumplidos
	// Devuelve la lista de requisitos no cumplidos actualizada
	public ArrayList<String> calcularRequisitosTemporalesNoCumplidos(Date initialDate, int dia,
			ArrayList<String> list) {
		String sDia = "";
		if (dia == 0)
			sDia = "monday";
		else if (dia == 1)
			sDia = "tuesday";
		else if (dia == 2)
			sDia = "wednesday";
		else if (dia == 3)
			sDia = "thursday";
		else if (dia == 4)
			sDia = "friday";
		else if (dia == 5)
			sDia = "saturday";
		else if (dia == 6)
			sDia = "sunday";

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(initialDate);
		calendar.add(Calendar.DAY_OF_WEEK, dia);
		Date date = calendar.getTime();

		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_WEEK, -28);
		Date date28 = calendar.getTime();

		// Calcular si se cumplen los requisitos de frecuencia
		primeroFrecuenciaDeseada = primero.getFrecuencia();
		primero.setUltimasFechas(date28, date);
		primeroFrecuenciaReal = primero.getVeces();

		if (primeroFrecuenciaReal > primeroFrecuenciaDeseada) {
			list.add("On " + sDia + " the course " + primero.getNombre() + " has been eaten "
					+ (primeroFrecuenciaReal - primeroFrecuenciaDeseada) + " times more than desired the last 28 days");
		}

		// Calcular si se cumplen los requisitos de tiempo de espera minima
		primeroDiasEsperaDeseado = primero.getDiasEspera();
		ArrayList<Date> lastDates = primero.getUltimasFechas();
		long diasDiferencia;

		if (lastDates.size() > 0) {
			long diferencia = date.getTime() - lastDates.get(lastDates.size() - 1).getTime();
			TimeUnit time = TimeUnit.DAYS;
			diasDiferencia = time.convert(diferencia, TimeUnit.MILLISECONDS);
			primeroDiasEsperaReal = (int) (diasDiferencia);

			if (primeroDiasEsperaDeseado > primeroDiasEsperaReal) {
				list.add("El " + sDia + " el plato " + primero.getNombre() + " se ha comido "
						+ (primeroDiasEsperaDeseado - primeroDiasEsperaReal)
						+ " días antes de cumplir el tiempo mínimo de espera");
			}
		}

		// Calcula si se cumplen los requisitos de frecuencia
		segundoFrecuenciaDeseada = segundo.getFrecuencia();
		segundo.setUltimasFechas(date28, date);
		segundoFrecuenciaReal = segundo.getVeces();

		if (segundoFrecuenciaReal > segundoFrecuenciaDeseada) {
			list.add("El " + sDia + " el plato " + segundo.getNombre() + " se ha comido "
					+ (segundoFrecuenciaReal - segundoFrecuenciaDeseada)
					+ " veces más de las deseadas en los últimos 28 días");
			segundoCumpleRequisitos = false;
		}

		// Calcula si se cumplen los requisitos de dias de espera
		segundoDiasEsperaDeseado = segundo.getDiasEspera();
		lastDates = segundo.getUltimasFechas();

		if (lastDates.size() > 0) {
			long diferencia = date.getTime() - lastDates.get(lastDates.size() - 1).getTime();
			TimeUnit time = TimeUnit.DAYS;
			diasDiferencia = time.convert(diferencia, TimeUnit.MILLISECONDS);
			segundoDiasEsperaReal = (int) (diasDiferencia);

			if (segundoDiasEsperaDeseado > segundoDiasEsperaReal) {
				list.add("El " + sDia + " el plato " + segundo.getNombre() + " se ha comido "
						+ (segundoDiasEsperaDeseado - segundoDiasEsperaReal)
						+ " días antes de cumplir el tiempo mínimo de espera");
			}
		}

		return list;
	}

	// Este método comprueba si los platos de la comida cumplen los requisitos
	// temporales del usuario
	// y aquellos que no se cumplen primeroCumpleRequisitos o
	// segundoCumpleRequisitos en false (dependiendo si el plato que no lo cumple es
	// primero o segundo)
	// Necesita la fecha del lunes del menú correspondiente i un entero del 0 al 6
	// indicando los días que han pasado desde el lunes
	public void calcularRequisitosTemporalesNoCumplidos(Date initialDate, int dia) {
		primeroCumpleRequisitos = true;
		segundoCumpleRequisitos = true;

		String sDia = "";
		if (dia == 0)
			sDia = "lunes";
		else if (dia == 1)
			sDia = "martes";
		else if (dia == 2)
			sDia = "miércoles";
		else if (dia == 3)
			sDia = "jueves";
		else if (dia == 4)
			sDia = "viernes";
		else if (dia == 5)
			sDia = "sábado";
		else if (dia == 6)
			sDia = "domingo";

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(initialDate);
		calendar.add(Calendar.DAY_OF_WEEK, dia);
		Date date = calendar.getTime();

		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_WEEK, -28);
		Date date28 = calendar.getTime();

		// Calcular si se cumplen los requisitos de frecuencia
		primeroFrecuenciaDeseada = primero.getFrecuencia();
		primero.setUltimasFechas(date28, date);
		primeroFrecuenciaReal = primero.getVeces();

		if (primeroFrecuenciaReal > primeroFrecuenciaDeseada) {
			primeroCumpleRequisitos = false;
		}

		// Calcular si se cumplen los requisitos de tiempo de espera minima
		primeroDiasEsperaDeseado = primero.getDiasEspera();
		ArrayList<Date> lastDates = primero.getUltimasFechas();
		long diasDiferencia;

		if (lastDates.size() > 0) {
			long diferencia = date.getTime() - lastDates.get(lastDates.size() - 1).getTime();
			TimeUnit time = TimeUnit.DAYS;
			diasDiferencia = time.convert(diferencia, TimeUnit.MILLISECONDS);
			primeroDiasEsperaReal = (int) (diasDiferencia);

			if (primeroDiasEsperaDeseado > primeroDiasEsperaReal) {
				primeroCumpleRequisitos = false;
			}
		}

		// Calcula si se cumplen los requisitos de frecuencia
		segundoFrecuenciaDeseada = segundo.getFrecuencia();
		segundo.setUltimasFechas(date28, date);
		segundoFrecuenciaReal = segundo.getVeces();

		if (segundoFrecuenciaReal > segundoFrecuenciaDeseada) {
			segundoCumpleRequisitos = false;
		}

		// Calcula si se cumplen los requisitos de dias de espera
		segundoDiasEsperaDeseado = segundo.getDiasEspera();
		lastDates = segundo.getUltimasFechas();

		if (lastDates.size() > 0) {
			long diferencia = date.getTime() - lastDates.get(lastDates.size() - 1).getTime();
			TimeUnit time = TimeUnit.DAYS;
			diasDiferencia = time.convert(diferencia, TimeUnit.MILLISECONDS);
			segundoDiasEsperaReal = (int) (diasDiferencia);

			if (segundoDiasEsperaDeseado > segundoDiasEsperaReal) {
				segundoCumpleRequisitos = false;
			}
		}
	}

	public Boolean getPrimeroCumpleRequisitos() {
		return primeroCumpleRequisitos;
	}

	public Boolean getSegundoCumpleRequisitos() {
		return segundoCumpleRequisitos;
	}

	// Guarda la información de la comida en la BBDD. Devuelve un entero en caso que
	// no se produzca un error devuelve el id de la comida si no devuelve -1.
	public int BDGuardar() {
		try {
			String query = "INSERT INTO Comidas (idPrimero, idSegundo) VALUES ('" + primero.getId() + "','"
					+ segundo.getId() + "')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);

			query = "SELECT * FROM Comidas WHERE idComida = (SELECT MAX(idComida) FROM Comidas)";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				id = rs.getInt("idComida");
			}

			return id;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);

			return -1;
		}
	}

	// Este método lee una comida de la base de datos. Para llamar este método la
	// comida tiene que tener el id asignado
	public Comida BDLeer() {
		Plato oPrimero = new Plato(conn);
		Plato oSegundo = new Plato(conn);

		try {
			String query = "SELECT * FROM Comidas WHERE idComida = '" + id + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				oPrimero.setId(rs.getInt("idPrimero"));
				oSegundo.setId(rs.getInt("idSegundo"));

				query = "SELECT nombre FROM Platos WHERE idPlato = '" + oPrimero.getId() + "'";
				st = conn.createStatement();
				ResultSet rs1 = st.executeQuery(query);

				int primero_leer = -1;
				while (rs1.next()) {
					oPrimero.setNombre(rs1.getString("nombre"));
					primero_leer = oPrimero.BDLeer();
				}

				query = "SELECT nombre FROM Platos WHERE idPlato = '" + oSegundo.getId() + "'";
				st = conn.createStatement();
				rs1 = st.executeQuery(query);

				int segundo_leer = -1;
				while (rs1.next()) {
					oSegundo.setNombre(rs1.getString("nombre"));
					segundo_leer = oSegundo.BDLeer();
				}

				if (primero_leer != 0 || segundo_leer != 0)
					return null;

				primero = oPrimero;
				segundo = oSegundo;
			}
			return this;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);

			return null;
		}
	}

	// Este método eliminar una comida. Para poder ejecutar el método la comida debe
	// tener un id asignado
	public void BDEliminar() {
		try {
			String query = "DELETE FROM Comidas WHERE idComida = '" + id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}
}
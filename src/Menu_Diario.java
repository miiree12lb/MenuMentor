import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

class Menu_Diario {
	private int id;
	private Comida almuerzo;
	private Comida cena;
	private int calorias;
	private Connection conn;

	// Este método crea un objeto Menu_Diario a partir de una conexión a la BBDD y
	// deja los otros atributos vacíos
	public Menu_Diario(Connection conn) {
		this.conn = conn;
	}

	// Este método crea un Menu_Diario a partir de una conexión a la BBDD y dos
	// Comida: almuerzo y cena. Calcula las calorías del menú diario
	public Menu_Diario(Connection conn, Comida almuerzo, Comida cena) {
		this.conn = conn;
		this.almuerzo = almuerzo;
		this.cena = cena;
		calcularCalorias();
	}

	// Este método asigna el valor recibido por parámetro al id del menu diario
	public void setId(int id) {
		this.id = id;
	}

	// Este método devuelve el id del menu diario
	public int getId() {
		return id;
	}

	// Este método calcula las calorías del menu diario
	public void calcularCalorias() {
		almuerzo.calcularCalorias();
		cena.calcularCalorias();
		calorias = almuerzo.getCalorias() + cena.getCalorias();
	}

	// Este método asigna el almuerzo a la comida
	public void setAlmuerzo(Comida almuerzo) {
		this.almuerzo = almuerzo;
	}

	// Este método devuelve el almuerzo
	public Comida getAlmuerzo() {
		return almuerzo;
	}

	// Este método asigna la cena a la comida
	public void setCena(Comida cena) {
		this.cena = cena;
	}

	// Este método devuelve la cena
	public Comida getCena() {
		return cena;
	}

	// Este método devuelve las calorías del menu diario
	public Integer getCalorias() {
		return calorias;
	}

	// Este método llama recibe por parámetro lista de la compra,y devuelve esta
	// misma lista actualizada.
	// Para actualizarla llama al metodo generarListaIngredientes de Comida del
	// almuerzo y de la cena.
	public ArrayList<Ingrediente_Compra> generarListaIngredientes(ArrayList<Ingrediente_Compra> list) {
		list = almuerzo.generarListaIngredientes(list);
		list = cena.generarListaIngredientes(list);

		return list;
	}

	// Este método llama al método calcularRequisitosTemporalesNoCumplidos de
	// almuerzo y cena para que actualizen la lista de requisitos no cumplidos.
	// Recibe por parámetro la fecha del lunes del menu Semanal correspondiente, los
	// días que han pasado desde el lunes y la lista de requisitos no cumplidos. Esa
	// lista la devuelve actualizada
	public ArrayList<String> calcularRequisitosTemporalesNoCumplidos(Date initialDate, int dia,
			ArrayList<String> list) {
		list = almuerzo.calcularRequisitosTemporalesNoCumplidos(initialDate, dia, list);
		list = cena.calcularRequisitosTemporalesNoCumplidos(initialDate, dia, list);

		return list;
	}

	// Este método calcula los requisitos calóricos no cumplidos en un dia
	// Recibe por parámetro un dia entre el 0 y el 6, siendo el 0 e lunes y el 6 el
	// domingo, las calorías mínimas deseadas y las calorías máximas deseadas
	public ArrayList<String> calcularRequisitosCaloricosNoCumplidos(int dia, ArrayList<String> list, int caloriasMin,
			int caloriasMax) {
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

		calcularCalorias();

		if (calorias < caloriasMin) {
			list.add("El " + sDia + " se ingerieron " + (caloriasMin - calorias)
					+ " calorías menos de las mínimas deseadas");
		} else if (calorias > caloriasMax) {
			list.add("El " + sDia + " se ingerieron " + (calorias - caloriasMax)
					+ " calorías más de las máximas deseadas");
		}

		return list;
	}

	// Este método guarda en la BBDD el menu diario. Devuelve el id del menu si
	// funciona o -1 si se produce un error
	public int BDGuardar() {
		int idAlmuerzo, idCena;
		idAlmuerzo = almuerzo.BDGuardar();
		idCena = cena.BDGuardar();

		if (idCena == -1 || idAlmuerzo == -1)
			return -1;

		try {
			String query = "INSERT INTO MenusDiarios (idAlmuerzo, idCena) VALUES ('" + idAlmuerzo + "','" + idCena
					+ "')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);

			query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = (SELECT MAX(idMenuDiario) FROM MenusDiarios)";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				id = rs.getInt("idMenuDiario");
			}

			return id;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return -1;
		}
	}

	// Este método lee de la BBDD el menú diario. Devuelve el menu diario leído si
	// funciona correctamente o null si hay un error.
	public Menu_Diario BDLeer() {
		Comida oAlmuerzo = new Comida(conn);
		Comida oCena = new Comida(conn);
		try {
			String query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + id + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			int cont = 0;
			while (rs.next()) {
				cont++;
				oAlmuerzo.setId(rs.getInt("idAlmuerzo"));
				oCena.setId(rs.getInt("idCena"));
			}

			almuerzo = oAlmuerzo.BDLeer();
			cena = oCena.BDLeer();

			if (almuerzo == null || cena == null || cont == 0)
				return null;

			return this;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);

			return null;
		}
	}

	// Este método borra el menu diario, para ello el menu diario debe tener
	// asignado un id
	public void BDEliminar() {
		almuerzo.BDEliminar();
		cena.BDEliminar();
		try {
			String query = "DELETE FROM MenusDiarios WHERE idMenuDiario = '" + id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}
}
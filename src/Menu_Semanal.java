import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class Menu_Semanal {
	private int id;
	private ArrayList<Menu_Diario> menusDiarios;
	private int calorias;
	private int caloriasMin;
	private int caloriasMax;
	private Date lunes;
	private ArrayList<String> requisitosTemporalesNoCumplidos;
	private ArrayList<String> requisitosCaloricosNoCumplidos;
	private ArrayList<Ingrediente_Compra> ingredientesSemana;
	private ArrayList<Plato> platosPrimeroAlmuerzo;
	private ArrayList<Plato> platosPrimeroCena;
	private ArrayList<Plato> platosSegundoAlmuerzo;
	private ArrayList<Plato> platosSegundoCena;
	private ArrayList<Plato> platosValidosPrimeroAlmuerzo;
	private ArrayList<Plato> platosValidosSegundoAlmuerzo;
	private ArrayList<Plato> platosValidosPrimeroCena;
	private ArrayList<Plato> platosValidosSegundoCena;
	private Connection conn;

	// Este método crea un menu Semanal a partir de la conexión a la BBDD
	public Menu_Semanal(Connection conn) {
		calorias = 0;
		this.conn = conn;
		menusDiarios = new ArrayList<Menu_Diario>();
		platosPrimeroAlmuerzo = new ArrayList<Plato>();
		platosSegundoAlmuerzo = new ArrayList<Plato>();
		platosPrimeroCena = new ArrayList<Plato>();
		platosSegundoCena = new ArrayList<Plato>();
		platosValidosPrimeroAlmuerzo = new ArrayList<Plato>();
		platosValidosSegundoAlmuerzo = new ArrayList<Plato>();
		platosValidosPrimeroCena = new ArrayList<Plato>();
		platosValidosSegundoCena = new ArrayList<Plato>();
		ingredientesSemana = new ArrayList<Ingrediente_Compra>();
		requisitosTemporalesNoCumplidos = new ArrayList<String>();
		requisitosCaloricosNoCumplidos = new ArrayList<String>();
	}

	// asigna el id al menu semanal
	public void setId(int id) {
		this.id = id;
	}

	// devuelve el id del menu Semanal
	public int getId() {
		return id;
	}

	// intercambia en la base de datos la información de dos dias, se han
	// intercambido en el menú.
	private void BDIntercambioDia(int dia1, int dia2) {
		String sDia1 = "", sDia2 = "";
		if (dia1 == 0)
			sDia1 = "idMenuLunes";
		else if (dia1 == 1)
			sDia1 = "idMenuMartes";
		else if (dia1 == 2)
			sDia1 = "idMenuMiercoles";
		else if (dia1 == 3)
			sDia1 = "idMenuJueves";
		else if (dia1 == 4)
			sDia1 = "idMenuViernes";
		else if (dia1 == 5)
			sDia1 = "idMenuSabado";
		else if (dia1 == 6)
			sDia1 = "idMenuDomingo";

		if (dia2 == 0)
			sDia2 = "idMenuLunes";
		else if (dia2 == 1)
			sDia2 = "idMenuMartes";
		else if (dia2 == 2)
			sDia2 = "idMenuMiercoles";
		else if (dia2 == 3)
			sDia2 = "idMenuJueves";
		else if (dia2 == 4)
			sDia2 = "idMenuViernes";
		else if (dia2 == 5)
			sDia2 = "idMenuSabado";
		else if (dia2 == 6)
			sDia2 = "idMenuDomingo";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String sLunes = dateFormat.format(lunes);

		try {
			int newIdDia1 = 0, newIdDia2 = 0;

			String query = "SELECT * FROM MenusSemanales WHERE lunes = '" + sLunes + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				newIdDia1 = rs.getInt(sDia2);
				newIdDia2 = rs.getInt(sDia1);

				query = "UPDATE MenusSemanales SET " + sDia1 + " = '" + newIdDia1 + "', " + sDia2 + " = '" + newIdDia2
						+ "' WHERE lunes = '" + sLunes + "'";
				st = conn.createStatement();
				st.executeUpdate(query);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// este método intercambia el menu de dos dias
	public void intercambioDia(int dia1, int dia2) {
		Menu_Diario temporal = menusDiarios.get(dia1);
		menusDiarios.set(dia1, menusDiarios.get(dia2));
		menusDiarios.set(dia2, temporal);

		menusDiarios.get(dia1).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
		menusDiarios.get(dia1).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);
		menusDiarios.get(dia1).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
		menusDiarios.get(dia1).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);

		menusDiarios.get(dia2).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);
		menusDiarios.get(dia2).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);
		menusDiarios.get(dia2).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);
		menusDiarios.get(dia2).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);

		BDIntercambioDia(dia1, dia2);
	}

	// intercambi el almuerzo (almuerzo = true) o la cena (almuerzo = false) de dos
	// dias en la BBDD.
	private void BDIntercambioMenu(Boolean almuerzo, int dia1, int dia2) {
		String sDia1 = "", sDia2 = "";
		if (dia1 == 0)
			sDia1 = "idMenuLunes";
		else if (dia1 == 1)
			sDia1 = "idMenuMartes";
		else if (dia1 == 2)
			sDia1 = "idMenuMiercoles";
		else if (dia1 == 3)
			sDia1 = "idMenuJueves";
		else if (dia1 == 4)
			sDia1 = "idMenuViernes";
		else if (dia1 == 5)
			sDia1 = "idMenuSabado";
		else if (dia1 == 6)
			sDia1 = "idMenuDomingo";

		if (dia2 == 0)
			sDia2 = "idMenuLunes";
		else if (dia2 == 1)
			sDia2 = "idMenuMartes";
		else if (dia2 == 2)
			sDia2 = "idMenuMiercoles";
		else if (dia2 == 3)
			sDia2 = "idMenuJueves";
		else if (dia2 == 4)
			sDia2 = "idMenuViernes";
		else if (dia2 == 5)
			sDia2 = "idMenuSabado";
		else if (dia2 == 6)
			sDia2 = "idMenuDomingo";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String sLunes = dateFormat.format(lunes);

		try {
			int idMenuDiario1, idMenuDiario2, newIdComida1 = 0, newIdComida2 = 0;
			String query = "SELECT * FROM MenusSemanales WHERE lunes = '" + sLunes + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				idMenuDiario1 = rs.getInt(sDia1);
				idMenuDiario2 = rs.getInt(sDia2);

				if (almuerzo) {
					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario1 + "'";
					st = conn.createStatement();
					ResultSet rs1 = st.executeQuery(query);

					while (rs1.next()) {
						newIdComida2 = rs1.getInt("idAlmuerzo");
					}

					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario2 + "'";
					st = conn.createStatement();
					ResultSet rs2 = st.executeQuery(query);

					while (rs2.next()) {
						newIdComida1 = rs2.getInt("idAlmuerzo");
					}
					query = "UPDATE MenusDiarios SET idAlmuerzo = '" + newIdComida1 + "' WHERE idMenuDiario = '"
							+ idMenuDiario1 + "'";
					st = conn.createStatement();
					st.executeUpdate(query);

					query = "UPDATE MenusDiarios SET idAlmuerzo = '" + newIdComida2 + "' WHERE idMenuDiario = '"
							+ idMenuDiario2 + "'";
					st = conn.createStatement();
					st.executeUpdate(query);
				} else {
					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario1 + "'";
					st = conn.createStatement();
					ResultSet rs1 = st.executeQuery(query);

					while (rs1.next()) {
						newIdComida2 = rs1.getInt("idCena");
					}

					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario2 + "'";
					st = conn.createStatement();
					ResultSet rs2 = st.executeQuery(query);

					while (rs2.next()) {
						newIdComida1 = rs2.getInt("idCena");
					}
					query = "UPDATE MenusDiarios SET idCena = '" + newIdComida1 + "' WHERE idMenuDiario = '"
							+ idMenuDiario1 + "'";
					st = conn.createStatement();
					st.executeUpdate(query);

					query = "UPDATE MenusDiarios SET idCena = '" + newIdComida2 + "' WHERE idMenuDiario = '"
							+ idMenuDiario2 + "'";
					st = conn.createStatement();
					st.executeUpdate(query);
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// intercambi el almuerzo (almuerzo = true) o la cena (almuerzo = false) de dos
	// dias-
	public void intercambioMenu(Boolean almuerzo, int dia1, int dia2) {
		if (almuerzo) {
			Comida temporal = menusDiarios.get(dia1).getAlmuerzo();
			menusDiarios.get(dia1).setAlmuerzo(menusDiarios.get(dia2).getAlmuerzo());
			menusDiarios.get(dia2).setAlmuerzo(temporal);

			menusDiarios.get(dia1).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
			menusDiarios.get(dia2).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);

			menusDiarios.get(dia1).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);
			menusDiarios.get(dia2).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);
		} else {
			Comida temporal = menusDiarios.get(dia1).getCena();
			menusDiarios.get(dia1).setCena(menusDiarios.get(dia2).getCena());
			menusDiarios.get(dia2).setCena(temporal);

			menusDiarios.get(dia1).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
			menusDiarios.get(dia2).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);

			menusDiarios.get(dia1).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);
			menusDiarios.get(dia2).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);
		}

		BDIntercambioMenu(almuerzo, dia1, dia2);
	}

	// intercambi el almuerzo (almuerzo = true) o la cena (almuerzo = false) y el
	// primero (primero = true) o el segundo (primero = false)
	// de dos dias en la BBDD.
	private void BDIntercambioPlato(Boolean almuerzo, Boolean primero, int dia1, int dia2) {
		String sDia1 = "", sDia2 = "";
		if (dia1 == 0)
			sDia1 = "idMenuLunes";
		else if (dia1 == 1)
			sDia1 = "idMenuMartes";
		else if (dia1 == 2)
			sDia1 = "idMenuMiercoles";
		else if (dia1 == 3)
			sDia1 = "idMenuJueves";
		else if (dia1 == 4)
			sDia1 = "idMenuViernes";
		else if (dia1 == 5)
			sDia1 = "idMenuSabado";
		else if (dia1 == 6)
			sDia1 = "idMenuDomingo";

		if (dia2 == 0)
			sDia2 = "idMenuLunes";
		else if (dia2 == 1)
			sDia2 = "idMenuMartes";
		else if (dia2 == 2)
			sDia2 = "idMenuMiercoles";
		else if (dia2 == 3)
			sDia2 = "idMenuJueves";
		else if (dia2 == 4)
			sDia2 = "idMenuViernes";
		else if (dia2 == 5)
			sDia2 = "idMenuSabado";
		else if (dia2 == 6)
			sDia2 = "idMenuDomingo";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String sLunes = dateFormat.format(lunes);

		try {
			int idMenuDiario1, idMenuDiario2, idComida1 = 0, idComida2 = 0, newIdPlato1 = 0, newIdPlato2 = 0;
			String query = "SELECT * FROM MenusSemanales WHERE lunes = '" + sLunes + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				idMenuDiario1 = rs.getInt(sDia1);
				idMenuDiario2 = rs.getInt(sDia2);

				if (almuerzo) {
					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario1 + "'";
					st = conn.createStatement();
					ResultSet rs1 = st.executeQuery(query);

					while (rs1.next()) {
						idComida1 = rs1.getInt("idAlmuerzo");
					}

					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario2 + "'";
					st = conn.createStatement();
					ResultSet rs2 = st.executeQuery(query);

					while (rs2.next()) {
						idComida2 = rs2.getInt("idAlmuerzo");
					}

					if (primero) {
						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida1 + "'";
						st = conn.createStatement();
						ResultSet rs3 = st.executeQuery(query);

						while (rs3.next()) {
							newIdPlato2 = rs3.getInt("idPrimero");
						}

						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida2 + "'";
						st = conn.createStatement();
						ResultSet rs4 = st.executeQuery(query);

						while (rs4.next()) {
							newIdPlato1 = rs4.getInt("idPrimero");
						}

						query = "UPDATE Comidas SET idPrimero = '" + newIdPlato1 + "' WHERE idComida = '" + idComida1
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);

						query = "UPDATE Comidas SET idPrimero = '" + newIdPlato2 + "' WHERE idComida = '" + idComida2
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);
					} else {
						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida1 + "'";
						st = conn.createStatement();
						ResultSet rs3 = st.executeQuery(query);

						while (rs3.next()) {
							newIdPlato2 = rs3.getInt("idSegundo");
						}

						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida2 + "'";
						st = conn.createStatement();
						ResultSet rs4 = st.executeQuery(query);

						while (rs4.next()) {
							newIdPlato1 = rs4.getInt("idSegundo");
						}

						query = "UPDATE Comidas SET idSegundo = '" + newIdPlato1 + "' WHERE idComida = '" + idComida1
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);

						query = "UPDATE Comidas SET idSegundo = '" + newIdPlato2 + "' WHERE idComida = '" + idComida2
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);
					}
				} else {
					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario1 + "'";
					st = conn.createStatement();
					ResultSet rs1 = st.executeQuery(query);

					while (rs1.next()) {
						idComida1 = rs1.getInt("idCena");
					}

					query = "SELECT * FROM MenusDiarios WHERE idMenuDiario = '" + idMenuDiario2 + "'";
					st = conn.createStatement();
					ResultSet rs2 = st.executeQuery(query);

					while (rs2.next()) {
						idComida2 = rs2.getInt("idCena");
					}
					if (primero) {
						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida1 + "'";
						st = conn.createStatement();
						ResultSet rs3 = st.executeQuery(query);

						while (rs3.next()) {
							newIdPlato2 = rs3.getInt("idPrimero");
						}

						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida2 + "'";
						st = conn.createStatement();
						ResultSet rs4 = st.executeQuery(query);

						while (rs4.next()) {
							newIdPlato1 = rs4.getInt("idPrimero");
						}

						query = "UPDATE Comidas SET idPrimero = '" + newIdPlato1 + "' WHERE idComida = '" + idComida1
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);

						query = "UPDATE Comidas SET idPrimero = '" + newIdPlato2 + "' WHERE idComida = '" + idComida2
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);
					} else {
						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida1 + "'";
						st = conn.createStatement();
						ResultSet rs3 = st.executeQuery(query);

						while (rs3.next()) {
							newIdPlato2 = rs3.getInt("idSegundo");
						}

						query = "SELECT * FROM Comidas WHERE idComida = '" + idComida2 + "'";
						st = conn.createStatement();
						ResultSet rs4 = st.executeQuery(query);

						while (rs4.next()) {
							newIdPlato1 = rs4.getInt("idSegundo");
						}

						query = "UPDATE Comidas SET idSegundo = '" + newIdPlato1 + "' WHERE idComida = '" + idComida1
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);

						query = "UPDATE Comidas SET idSegundo = '" + newIdPlato2 + "' WHERE idComida = '" + idComida2
								+ "'";
						st = conn.createStatement();
						st.executeUpdate(query);
					}
				}
			}
			st.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// intercambi el almuerzo (almuerzo = true) o la cena (almuerzo = false) y el
	// primero (primero = true) o el segundo (primero = false) de dos dias.
	public void intercambioPlato(Boolean almuerzo, Boolean primero, int dia1, int dia2) {
		if (almuerzo) {
			if (primero) {
				Plato temporal = menusDiarios.get(dia1).getAlmuerzo().getPrimero();
				menusDiarios.get(dia1).getAlmuerzo().setPrimero(menusDiarios.get(dia2).getAlmuerzo().getPrimero());
				menusDiarios.get(dia2).getAlmuerzo().setPrimero(temporal);

				menusDiarios.get(dia1).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
				menusDiarios.get(dia2).getAlmuerzo().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);
			} else {
				Plato temporal = menusDiarios.get(dia1).getAlmuerzo().getSegundo();
				menusDiarios.get(dia1).getAlmuerzo().setSegundo(menusDiarios.get(dia2).getAlmuerzo().getSegundo());
				menusDiarios.get(dia2).getAlmuerzo().setSegundo(temporal);

				menusDiarios.get(dia1).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);
				menusDiarios.get(dia2).getAlmuerzo().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);
			}
		} else {
			if (primero) {
				Plato temporal = menusDiarios.get(dia1).getCena().getPrimero();
				menusDiarios.get(dia1).getCena().setPrimero(menusDiarios.get(dia2).getCena().getPrimero());
				menusDiarios.get(dia2).getCena().setPrimero(temporal);

				menusDiarios.get(dia1).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia2, dia1);
				menusDiarios.get(dia2).getCena().getPrimero().BDCambioPlatosFecha(lunes, dia1, dia2);
			} else {
				Plato temporal = menusDiarios.get(dia1).getCena().getSegundo();
				menusDiarios.get(dia1).getCena().setSegundo(menusDiarios.get(dia2).getCena().getSegundo());
				menusDiarios.get(dia2).getCena().setSegundo(temporal);

				menusDiarios.get(dia1).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia2, dia1);
				menusDiarios.get(dia2).getCena().getSegundo().BDCambioPlatosFecha(lunes, dia1, dia2);
			}
		}
		BDIntercambioPlato(almuerzo, primero, dia1, dia2);
	}

	// Genera la lista de platos que se pueden comer como primer plato del almuerzo
	private void generarListaPlatosPrimeroAlmuerzo() {
		try {
			// Crear lista de los platos disponibles para el primer plato del almuerzo
			String query = "SELECT nombre FROM Platos WHERE Platos.primero = 1 AND Platos.almuerzo = 1 AND habilitado = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Plato plato = new Plato(conn);
				plato.setNombre(rs.getString("nombre"));
				plato.BDLeer();

				platosPrimeroAlmuerzo.add(plato);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// Genera la lista de platos que se pueden comer como segundo plato del almuerzo
	private void generarListaPlatosSegundoAlmuerzo() {
		try {
			// Crear lista de los platos disponibles para el segundo plato del almuerzo
			String query = "SELECT nombre FROM Platos WHERE Platos.segundo = 1 AND Platos.almuerzo = 1 AND habilitado = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Plato plato = new Plato(conn);
				plato.setNombre(rs.getString("nombre"));

				plato.BDLeer();

				platosSegundoAlmuerzo.add(plato);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// Genera la lista de platos que se pueden comer como primer plato de la cena
	private void generarListaPlatosPrimeroCena() {
		try {
			// Crea la lista de platos disponibles para el primer de la cena
			String query = "SELECT nombre FROM Platos WHERE Platos.primero = 1 AND Platos.cena = 1 AND habilitado = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Plato plato = new Plato(conn);
				plato.setNombre(rs.getString("nombre"));
				plato.BDLeer();
				platosPrimeroCena.add(plato);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// Genera la lista de platos que se pueden comer como segundo plato de la cena
	private void generarListaPlatosSegundoCena() {
		try {
			// Crea la lista de platos disponibles para el segundo plato de la cena
			String query = "SELECT nombre FROM Platos WHERE Platos.segundo = 1 AND Platos.cena = 1 AND habilitado = 1";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Plato plato = new Plato(conn);
				plato.setNombre(rs.getString("nombre"));

				plato.BDLeer();

				platosSegundoCena.add(plato);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// Genera y devuelve una lista de platos que cumplen los requisitos si son
	// comidos el dia pasado por parámetro
	private ArrayList<Plato> generarPlatosValidos(ArrayList<Plato> list, int dia) {
		ArrayList<Plato> validos = new ArrayList<Plato>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lunes);
		calendar.add(Calendar.DAY_OF_WEEK, dia);
		Date endDate = calendar.getTime();

		calendar.setTime(endDate);
		calendar.add(Calendar.DAY_OF_WEEK, -28);
		Date initialDate = calendar.getTime();

		for (int i = 0; i < list.size(); i++) {
			boolean frecuenciaValida = false, tiempoEsperaValido = false;
			list.get(i).setUltimasFechas(initialDate);

			/*
			 * Comprobar la frecuencia. Si el numero veces que se ha comido el plato en los
			 * ultimos 28 dias
			 * es inferior a la frecuencia deseada la frecuencia es valida
			 */
			if (list.get(i).getVeces() < list.get(i).getFrecuencia())
				frecuenciaValida = true;

			/*
			 * Comprobar el tiempo de espera. Si el tiempo que hace desde la ultima vez que
			 * se puso el plato en
			 * un menu es mayor o igual al tiempo de espera deseado el tiempo de espera es
			 * valido
			 */

			// Calcular los dias que hace desde que se comio el plato por ultima vez
			ArrayList<Date> lastDates = list.get(i).getUltimasFechas();

			long diasDiferencia;

			if (lastDates.size() > 0) {
				long diferencia = endDate.getTime() - lastDates.get(lastDates.size() - 1).getTime();
				TimeUnit time = TimeUnit.DAYS;
				diasDiferencia = time.convert(diferencia, TimeUnit.MILLISECONDS);
				if (diasDiferencia >= list.get(i).getDiasEspera())
					tiempoEsperaValido = true;
			} else
				tiempoEsperaValido = true;

			if (frecuenciaValida && tiempoEsperaValido) {
				validos.add(list.get(i));
			}
		}

		return validos;
	}

	// obtiene los requisitos calóricos en el momento de generar el menu Semanal
	private void obtenerRequisitosCaloricos() {
		try {
			String query = "SELECT idConfiguracion, valor FROM AppConfig";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				if (rs.getInt("idConfiguracion") == 1)
					caloriasMin = rs.getInt("valor");
				else if (rs.getInt("idConfiguracion") == 2)
					caloriasMax = rs.getInt("valor");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

	// calcula los requisitos temporales no cumplidos durante la semana
	public ArrayList<String> calcularRequisitosTemporalesNoCumplidos() {
		requisitosTemporalesNoCumplidos.clear();
		for (int dia = 0; dia < 7; dia++) {
			requisitosTemporalesNoCumplidos = menusDiarios.get(dia).calcularRequisitosTemporalesNoCumplidos(lunes, dia,
					requisitosTemporalesNoCumplidos);
		}
		return requisitosTemporalesNoCumplidos;
	}

	// calcula los requisitos caloricos no cumplidos durante la semana
	public ArrayList<String> calcularRequisitosCaloricosNoCumplidos() {
		requisitosCaloricosNoCumplidos.clear();
		for (int dia = 0; dia < 7; dia++) {
			requisitosCaloricosNoCumplidos = menusDiarios.get(dia).calcularRequisitosCaloricosNoCumplidos(dia,
					requisitosCaloricosNoCumplidos, caloriasMin, caloriasMax);
		}
		return requisitosCaloricosNoCumplidos;
	}

	// genera el menu semanal
	public void generarMenuSemanal() {
		menusDiarios.clear();
		Comida almuerzoComida = new Comida(conn), cenaComida = new Comida(conn);

		generarListaPlatosPrimeroAlmuerzo();
		generarListaPlatosSegundoAlmuerzo();
		generarListaPlatosPrimeroCena();
		generarListaPlatosSegundoCena();

		obtenerRequisitosCaloricos();

		// Algoritmo que genera el menu automatico a partir de las listas creadas
		for (int dia = 0; dia < 7; dia++) {
			calorias = 0;

			// Generar listas de los platos que cumplen los requisitos de frecuencia y dias
			// de espera para cada comida del Dia dia.
			platosValidosPrimeroAlmuerzo = generarPlatosValidos(platosPrimeroAlmuerzo, dia);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lunes);
			calendar.add(Calendar.DAY_OF_WEEK, dia);
			Date date = calendar.getTime();

			// Elegir platos
			Random random = new Random();
			int platoRandom, caloriasPrev;
			if (platosValidosPrimeroAlmuerzo.size() > 0) {
				do {
					platoRandom = random.nextInt(platosValidosPrimeroAlmuerzo.size());
					platosValidosPrimeroAlmuerzo.get(platoRandom).calcularCalorias();
					caloriasPrev = calorias + platosValidosPrimeroAlmuerzo.get(platoRandom).getCalorias();
					if (caloriasPrev > caloriasMax) {
						platosValidosPrimeroAlmuerzo.remove(platoRandom);
					}
				} while (caloriasPrev > caloriasMax && platosValidosPrimeroAlmuerzo.size() > 0);

				// En el caso que todos los platos superaran el maximo calorias diarias la lista
				// estara vacía
				if (platosValidosPrimeroAlmuerzo.size() == 0) {
					platoRandom = random.nextInt(platosPrimeroAlmuerzo.size());

					almuerzoComida.setPrimero(platosPrimeroAlmuerzo.get(platoRandom));
					platosPrimeroAlmuerzo.get(platoRandom).BDGuardarFecha(date);
				} else {
					// Guardar fecha en plato Fecha
					almuerzoComida.setPrimero(platosValidosPrimeroAlmuerzo.get(platoRandom));

					platosValidosPrimeroAlmuerzo.get(platoRandom).BDGuardarFecha(date);
				}
			} else {
				// Seleccionar plato aleatorio y calcular porque no se cumplen los requisitos de
				// tiempo de espera y de frecuencia
				platoRandom = random.nextInt(platosPrimeroAlmuerzo.size());

				almuerzoComida.setPrimero(platosPrimeroAlmuerzo.get(platoRandom));
				platosPrimeroAlmuerzo.get(platoRandom).BDGuardarFecha(date);
			}

			platosValidosSegundoAlmuerzo = generarPlatosValidos(platosSegundoAlmuerzo, dia);
			if (platosValidosSegundoAlmuerzo.size() > 0) {
				do {
					platoRandom = random.nextInt(platosValidosSegundoAlmuerzo.size());
					platosValidosSegundoAlmuerzo.get(platoRandom).calcularCalorias();
					caloriasPrev = calorias + platosValidosSegundoAlmuerzo.get(platoRandom).getCalorias();
					if (caloriasPrev > caloriasMax) {
						platosValidosSegundoAlmuerzo.remove(platoRandom);
					}
				} while (caloriasPrev > caloriasMax && platosValidosSegundoAlmuerzo.size() > 0);

				// En el caso que todos los platos superaran el maximo calorias diarias la lista
				// estara vacía
				if (platosValidosSegundoAlmuerzo.size() == 0) {
					platoRandom = random.nextInt(platosSegundoAlmuerzo.size());

					almuerzoComida.setSegundo(platosSegundoAlmuerzo.get(platoRandom));
					platosSegundoAlmuerzo.get(platoRandom).BDGuardarFecha(date);
				} else {
					// Guardar fecha en plato Fecha
					almuerzoComida.setSegundo(platosValidosSegundoAlmuerzo.get(platoRandom));

					platosValidosSegundoAlmuerzo.get(platoRandom).BDGuardarFecha(date);
				}
			} else {
				// Seleccionar plato aleatorio y calcular porque no se cumplen los requisitos de
				// tiempo de espera y de frecuencia
				platoRandom = random.nextInt(platosSegundoAlmuerzo.size());

				almuerzoComida.setSegundo(platosSegundoAlmuerzo.get(platoRandom));
				platosSegundoAlmuerzo.get(platoRandom).BDGuardarFecha(date);
			}

			platosValidosPrimeroCena = generarPlatosValidos(platosPrimeroCena, dia);
			if (platosValidosPrimeroCena.size() > 0) {
				do {
					platoRandom = random.nextInt(platosValidosPrimeroCena.size());
					platosValidosPrimeroCena.get(platoRandom).calcularCalorias();
					caloriasPrev = calorias + platosValidosPrimeroCena.get(platoRandom).getCalorias();
					if (caloriasPrev > caloriasMax) {
						platosValidosPrimeroCena.remove(platoRandom);
					}
				} while (caloriasPrev > caloriasMax && platosValidosPrimeroCena.size() > 0);

				// En el caso que todos los platos superaran el maximo calorias diarias la lista
				// estara vacía
				if (platosValidosPrimeroCena.size() == 0) {
					platoRandom = random.nextInt(platosPrimeroCena.size());

					cenaComida.setPrimero(platosPrimeroCena.get(platoRandom));
					platosPrimeroCena.get(platoRandom).BDGuardarFecha(date);
				} else {
					// Guardar fecha en plato Fecha
					cenaComida.setPrimero(platosValidosPrimeroCena.get(platoRandom));

					platosValidosPrimeroCena.get(platoRandom).BDGuardarFecha(date);
				}
			} else {
				// Seleccionar plato aleatorio y calcular porque no se cumplen los requisitos de
				// tiempo de espera y de frecuencia
				platoRandom = random.nextInt(platosPrimeroCena.size());

				cenaComida.setPrimero(platosPrimeroCena.get(platoRandom));
				platosPrimeroCena.get(platoRandom).BDGuardarFecha(date);
			}

			platosValidosSegundoCena = generarPlatosValidos(platosSegundoCena, dia);
			if (platosValidosSegundoCena.size() > 0) {
				do {
					platoRandom = random.nextInt(platosValidosSegundoCena.size());
					platosValidosSegundoCena.get(platoRandom).calcularCalorias();
					caloriasPrev = calorias + platosValidosSegundoCena.get(platoRandom).getCalorias();
					if (caloriasPrev > caloriasMax) {
						platosValidosSegundoCena.remove(platoRandom);
					}
				} while (caloriasPrev > caloriasMax && platosValidosSegundoCena.size() > 0);

				// En el caso que todos los platos superaran el maximo calorias diarias la lista
				// estara vacía
				if (platosValidosSegundoCena.size() == 0) {
					platoRandom = random.nextInt(platosSegundoCena.size());

					cenaComida.setSegundo(platosSegundoCena.get(platoRandom));
					platosSegundoCena.get(platoRandom).BDGuardarFecha(date);
				} else {
					// Guardar fecha en plato Fecha
					cenaComida.setSegundo(platosValidosSegundoCena.get(platoRandom));

					platosValidosSegundoCena.get(platoRandom).BDGuardarFecha(date);
				}
			} else {
				// Seleccionar plato aleatorio y calcular porque no se cumplen los requisitos de
				// tiempo de espera y de frecuencia
				platoRandom = random.nextInt(platosSegundoCena.size());

				cenaComida.setSegundo(platosSegundoCena.get(platoRandom));
				platosSegundoCena.get(platoRandom).BDGuardarFecha(date);
			}
			menusDiarios.add(
					new Menu_Diario(conn, new Comida(conn, almuerzoComida.getPrimero(), almuerzoComida.getSegundo()),
							new Comida(conn, cenaComida.getPrimero(), cenaComida.getSegundo())));
		}
		BDGuardar();

		generarListaIngredientes();
	}

	// genera la lista de la compra y la devuelve en forma de Objeto[][]
	public Object[][] generarListaIngredientes() {
		ingredientesSemana.clear();

		for (int dia = 0; dia < 7; dia++) {
			ingredientesSemana = menusDiarios.get(dia).generarListaIngredientes(ingredientesSemana);
		}

		Object[][] lista = new Object[ingredientesSemana.size()][2];

		for (int i = 0; i < ingredientesSemana.size(); i++) {
			char[] arr = ingredientesSemana.get(i).getIngrediente().getNombre().toCharArray();
			arr[0] = Character.toUpperCase(arr[0]);
			lista[i][0] = new String(arr);
			lista[i][1] = ingredientesSemana.get(i).getGramos();
		}
		return lista;
	}

	// devuelve la lista de menus diarios
	public ArrayList<Menu_Diario> getMenusDiarios() {
		return menusDiarios;
	}

	// calcula las calorías del menú
	public void calcularCalorias() {
		calorias = 0;
		for (int i = 0; i < menusDiarios.size(); i++) {
			menusDiarios.get(i).calcularCalorias();
			calorias += menusDiarios.get(i).getCalorias();
		}
	}

	// devuelve las calorías del menú
	public Integer getCalorias() {
		return calorias;
	}

	// le asigna la fecha del lunes al menu
	public void setLunes(Date lunes) {
		this.lunes = lunes;
	}

	// devuelve la fecha del lunes
	public Date getLunes() {
		return lunes;
	}

	// guarda el menu en la BBDD. Devuelve true si funciona y false si se produce un
	// error
	public Boolean BDGuardar() {
		int idMenuLunes, idMenuMartes, idMenuMiercoles, idMenuJueves, idMenuViernes, idMenuSabado, idMenuDomingo;
		idMenuLunes = menusDiarios.get(0).BDGuardar();
		idMenuMartes = menusDiarios.get(1).BDGuardar();
		idMenuMiercoles = menusDiarios.get(2).BDGuardar();
		idMenuJueves = menusDiarios.get(3).BDGuardar();
		idMenuViernes = menusDiarios.get(4).BDGuardar();
		idMenuSabado = menusDiarios.get(5).BDGuardar();
		idMenuDomingo = menusDiarios.get(6).BDGuardar();

		if (idMenuLunes == -1 || idMenuMartes == -1 || idMenuMiercoles == -1 || idMenuJueves == -1
				|| idMenuViernes == -1 || idMenuSabado == -1 || idMenuDomingo == -1)
			return false;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String sLunes = dateFormat.format(lunes);

		try {
			String query = "INSERT INTO MenusSemanales (idMenuLunes, idMenuMartes, idMenuMiercoles, idMenuJueves, idMenuViernes, idMenuSabado, idMenuDomingo, lunes, caloriasMinimas, caloriasMaximas) VALUES('"
					+ idMenuLunes + "','" + idMenuMartes + "', '" + idMenuMiercoles + "', '" + idMenuJueves + "', '"
					+ idMenuViernes + "', '" + idMenuSabado + "', '" + idMenuDomingo + "', '" + sLunes + "', '"
					+ caloriasMin + "', '" + caloriasMax + "')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return false;
		}
	}

	// lee el menu de la base de datos. recibe por parámetro la fecha del lunes del
	// menú a ser leído
	public Boolean BDLeer(Date date) {
		menusDiarios.clear();

		Menu_Diario oLunes = new Menu_Diario(conn);
		Menu_Diario oMartes = new Menu_Diario(conn);
		Menu_Diario oMiercoles = new Menu_Diario(conn);
		Menu_Diario oJueves = new Menu_Diario(conn);
		Menu_Diario oViernes = new Menu_Diario(conn);
		Menu_Diario oSabado = new Menu_Diario(conn);
		Menu_Diario oDomingo = new Menu_Diario(conn);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String sDate = dateFormat.format(date);

			String query = "SELECT * FROM MenusSemanales WHERE lunes = '" + sDate + "'";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			int cont = 0;
			while (rs.next()) {
				cont++;
				id = rs.getInt("idMenuSemanal");
				oLunes.setId(rs.getInt("idMenuLunes"));
				oMartes.setId(rs.getInt("idMenuMartes"));
				oMiercoles.setId(rs.getInt("idMenuMiercoles"));
				oJueves.setId(rs.getInt("idMenuJueves"));
				oViernes.setId(rs.getInt("idMenuViernes"));
				oSabado.setId(rs.getInt("idMenuSabado"));
				oDomingo.setId(rs.getInt("idMenuDomingo"));
				caloriasMin = rs.getInt("caloriasMinimas");
				caloriasMax = rs.getInt("CaloriasMaximas");
				id = rs.getInt("idMenuSemanal");
			}
			menusDiarios.add(oLunes.BDLeer());
			menusDiarios.add(oMartes.BDLeer());
			menusDiarios.add(oMiercoles.BDLeer());
			menusDiarios.add(oJueves.BDLeer());
			menusDiarios.add(oViernes.BDLeer());
			menusDiarios.add(oSabado.BDLeer());
			menusDiarios.add(oDomingo.BDLeer());

			boolean menuNull = false;
			if (cont == 0) {
				menuNull = true;
			}
			for (int i = 0; i < 7 && !menuNull; i++) {
				if (menusDiarios.get(i) == null)
					menuNull = true;
			}

			return !menuNull;

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
			return false;
		}
	}

	// elimina el menú de la BBDD
	public void BDEliminar() {
		for (int i = 0; i < 7; i++) {
			menusDiarios.get(i).BDEliminar();
		}
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lunes);
			calendar.add(Calendar.DAY_OF_WEEK, 6);
			Date domingo = calendar.getTime();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String sLunes = dateFormat.format(lunes);
			String sDomingo = dateFormat.format(domingo);

			String query = "DELETE FROM MenusSemanales WHERE lunes = '" + sLunes + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(query);

			query = "DELETE FROM PlatosFecha WHERE fecha >= '" + sLunes + "' AND fecha <= '" + sDomingo + "'";
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}
}
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

class Main{
	// Database used when no Config.txt is present (created next to where the app is run).
	private static final String DEFAULT_DB = "InternalDB.db";
	// Example data copied in on first run, so the app isn't empty.
	private static final String SAMPLE_DB = "data/sample.db";
	// Optional: a file whose last non-empty line is a custom database path.
	private static final String CONFIG_FILE = "Config.txt";

	static String resolveDbPath() throws IOException {
		String dbPath = DEFAULT_DB;
		Path config = Paths.get(CONFIG_FILE);
		if (Files.exists(config)) {
			List<String> lines = Files.readAllLines(config, StandardCharsets.UTF_8);
			for (String line : lines) {
				if (!line.trim().isEmpty()) {
					dbPath = line.trim();
				}
			}
		}

		Path db = Paths.get(dbPath);
		Path sample = Paths.get(SAMPLE_DB);
		if (!Files.exists(db) && Files.exists(sample)) {
			Files.copy(sample, db);
		}
		return dbPath;
	}

	public static void generarDB(Connection conn){
		try{
			Statement st;
			String query = "CREATE TABLE IF NOT EXISTS AppConfig (idConfiguracion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, valor INTEGER NOT NULL);";
			st = conn.createStatement();
		    st.executeUpdate(query);

		    int count = 0;
		    query = "SELECT COUNT(*) AS numTI FROM AppConfig";
		    st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
    		while(rs.next()){
    			count = rs.getInt("numTI");
    		}
    		if (count == 0){
		    	query = "INSERT INTO AppConfig (nombre, valor) VALUES ('caloriasMinimas', 0);";
    			st = conn.createStatement();
		    	st.executeUpdate(query);

		    	query = "INSERT INTO AppConfig (nombre, valor) VALUES ('caloriasMaximas', 0);";
    			st = conn.createStatement();
		    	st.executeUpdate(query);
    		}
		
		    query = "CREATE TABLE IF NOT EXISTS Ingredientes (idIngrediente	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, nombre TEXT NOT NULL, calorias	INTEGER NOT NULL);";
			st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS Platos (idPlato	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre	INTEGER NOT NULL, link TEXT, almuerzo INTEGER NOT NULL, cena	INTEGER NOT NULL, primero INTEGER NOT NULL, segundo	INTEGER NOT NULL, frecuencia INTEGER NOT NULL, diasEspera INTEGER NOT NULL, habilitado INTEGER NOT NULL);";
		    st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS IngredientesPlato (idIP	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idPlato INTEGER NOT NULL, idIngrediente INTEGER NOT NULL, gramos	INTEGER NOT NULL, FOREIGN KEY(idPlato) REFERENCES Platos(idPlato), FOREIGN KEY(idIngrediente) REFERENCES Ingredientes(idIngrediente));";
			st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS Comidas (idComida	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idPrimero	INTEGER NOT NULL, idSegundo INTEGER NOT NULL, FOREIGN KEY(idPrimero) REFERENCES Platos(idPlato), FOREIGN KEY(idSegundo) REFERENCES Platos(idPlato));";
		    st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS MenusDiarios (idMenuDiario	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idAlmuerzo INTEGER NOT NULL, idCena	INTEGER NOT NULL, FOREIGN KEY(idAlmuerzo) REFERENCES Comidas(idComida));";
		    st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS MenusSemanales (idMenuSemanal	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idMenuLunes	INTEGER NOT NULL, idMenuMartes	INTEGER NOT NULL, idMenuMiercoles	INTEGER NOT NULL, idMenuJueves	INTEGER NOT NULL, idMenuViernes	INTEGER NOT NULL, idMenuSabado	INTEGER NOT NULL, idMenuDomingo INTEGER NOT NULL, lunes	TEXT NOT NULL, caloriasMinimas	INTEGER NOT NULL, caloriasMaximas INTEGER NOT NULL, FOREIGN KEY(idMenuLunes) REFERENCES MenusDiarios(idMenuDiario), FOREIGN KEY(idMenuMartes) REFERENCES MenusDiarios(idMenuDiario), FOREIGN KEY(idMenuMiercoles) REFERENCES MenusDiarios(idMenuDiario), FOREIGN KEY(idMenuJueves) REFERENCES MenusDiarios(idMenuDiario), FOREIGN KEY(idMenuSabado) REFERENCES MenusDiarios(idMenuDiario),	FOREIGN KEY(idMenuViernes) REFERENCES MenusDiarios(idMenuDiario), FOREIGN KEY(idMenuDomingo) REFERENCES MenusDiarios(idMenuDiario));";
		    st = conn.createStatement();
		    st.executeUpdate(query);

		    query = "CREATE TABLE IF NOT EXISTS PlatosFecha (idPlatoFecha INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idPlato	INTEGER NOT NULL, fecha	TEXT NOT NULL, FOREIGN KEY(idPlato) REFERENCES Platos(idPlato));";
		    st = conn.createStatement();
		    st.executeUpdate(query);
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
        }
        
	}

	public static void main(String args[]){
		try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = resolveDbPath();

            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
            if (conn != null) {
                generarDB(conn);

                new MenuPrincipal(conn);
            }
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println (ex);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.out.println (ex);	
        }
	}
}
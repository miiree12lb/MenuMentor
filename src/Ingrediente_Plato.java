import java.sql.*;

class Ingrediente_Plato{
	private int idIP;
	private Ingrediente ingrediente;
	private int gramos;
	private int calorias;
	private Connection conn;

	/*este método crea un ingrediente plato dados el ingrediente y 
	los gramos de ingrediente que hay en el plato y una conexión a la BBDD, y calcula las 
	calorias que el ingrediente aporta al plato*/
	public Ingrediente_Plato (Connection conn, int idIngrediente, int gramos){
		this.conn = conn;
		try{
			Statement st;
			String query;

			query = "SELECT * FROM Ingredientes WHERE idIngrediente = '" + idIngrediente + "'";
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);

		    if(rs.next()){
				ingrediente = new Ingrediente(conn, rs.getString("nombre"), rs.getInt("calorias"));
				ingrediente.setId(rs.getInt("idIngrediente"));
		    }
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
        }
		this.gramos = gramos;
		calorias = (ingrediente.getCalorias() * gramos) / 100;
	}

	//este método le asigna el id dado al ingrediente plato
	public void setId (int idIP){
		this.idIP = idIP;
	}

	//este método devuelve el id del ingrediente plato
	public int getId (){
		return idIP;
	}

	//este metode le asigna el ingrediente dado al ingrediente plato
	public void setIngrediente (Ingrediente ingrediente){
		this.ingrediente = ingrediente;
	}

	//este metodo devuelve el ingrediente al ingrediente plato
	public Ingrediente getIngrediente (){
		return ingrediente;
	}

	//este metodo le asigna los gramos dados al ingrediente plato
	public void setGramos(int gramos){
		this.gramos = gramos;
	}

	//este método devuelve los gramos del ingrediente plato
	public int getGramos (){
		return gramos;
	}

	//este metodo calcula las calorias del ingrediente plato
	public void calcularCalorias(){
		calorias = (ingrediente.getCalorias() * gramos) / 100;
	}

	//este metodo devuelve las calorias del ingrediente plato
	public int getCalorias(){
		return calorias;
	}

	//este método guarda información en la base de datos
	public void BDGuardar(int idPlato, int idIngrediente){ 
		try{
			Statement st;
			String query;

			query = "SELECT * FROM IngredientesPlato WHERE IngredientesPlato.idPlato = '" + idPlato + "' AND IngredientesPlato.idIngrediente = '" + idIngrediente + "'";
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);

			if (!rs.next()){
				query = "INSERT INTO IngredientesPlato(idPlato, idIngrediente, gramos) VALUES ('" + idPlato + "', '" + idIngrediente + "', '" + getGramos() +"')";
				st = conn.createStatement();
			    st.executeUpdate(query);
			}
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
            return;
        }
	}

	//este método lee información de la base de datos
	public int BDLeer(){
		return 0;
	}
}
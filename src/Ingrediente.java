import java.sql.*;
class Ingrediente{
	private int id; //autogenerado por la base de datos
	private String nombre;
	private int calorias;
	private Connection conn;

	public Ingrediente(Connection conn){
		this.conn = conn;
	}

	/*Este método crea un ingrediente dados su nombre y las kcalorias cada 100g*/
	public Ingrediente (Connection conn, String nombre, int calorias){
		this.conn = conn;
		this.nombre = nombre;
		this.calorias = calorias;
	}

	//este método le asigna el id dado al ingrediente
	public void setId (int id){
		this.id = id;
	}

	//este metodo devuelve el id del ingrediente
	public int getId(){
		return id;
	}

	//este método le pone el nombre dado al ingrediente
	public void setNombre (String nombre){
		this.nombre = nombre;
	}

	//este metodo devuelve el nombre del ingrediente
	public String getNombre (){
		return nombre;
	}

	//este metodo asigna las calorias dadas al ingrediente
	public void setCalorias (int calorias){
		this.calorias = calorias;
	}

	//este método devuelve las calorias
	public int getCalorias(){
		return calorias;
	}

	//este método guarda información en la base de datos
	public int BDGuardar(){
		try{
			Statement st;
			String query;

			query = "SELECT * FROM Ingredientes WHERE Ingredientes.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);

		    if(!rs.next()){
				query = "INSERT INTO Ingredientes(nombre, calorias) VALUES ('" + nombre.toLowerCase() + "', '" + calorias + "')";
				st = conn.createStatement();
		    	st.executeUpdate(query);
		    	return 0;
		    }
		    else return 2;
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
            return 1;
        }
	}

	//Este método elimina un ingrediente de la BBDD
	//Devuelve un entero. Si puede eliminar y no hay ningún error devuelve 0, si el ingrediente está en algún plato devuelve 2 y si hay un error devuelve 1.
	public int BDEliminar(){
		try{
			Statement st;
			String query;

			query = "SELECT idIngrediente FROM Ingredientes WHERE Ingredientes.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);

		    if (rs.next()){
		    	id = rs.getInt("idIngrediente");
		    }

			query = "SELECT * FROM IngredientesPlato WHERE IngredientesPlato.idIngrediente = '" + id + "'";
			st = conn.createStatement();
		    rs = st.executeQuery(query);

			if (!rs.next()){
				query = "DELETE FROM Ingredientes WHERE Ingredientes.idIngrediente = '" + id + "'";
				st = conn.createStatement();
				st.executeUpdate(query);

				return 0;
			}
		    else return 2;
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
            return 1;
        }
	}

	//este método lee información de la base de datos
	//devuelve un entero. 0 si no hay errores, 2 si el ingrediente que queremos leer no está guardado y 1 si hay un error con la BBDD.
	public int BDLeer(){
		try{
			Statement st;
			String query;
			ResultSet rs;

			query = "SELECT * FROM Ingredientes WHERE Ingredientes.nombre = '" + nombre.toLowerCase() + "'";
			st = conn.createStatement();
		    rs = st.executeQuery(query);

		    if (rs.next()){
		    	this.id = rs.getInt("idIngrediente");
		    	this.calorias = rs.getInt("calorias");

		    	return 0;
		    }

		    return 2;
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
            return 1;
        }		
	}

	//Este método actualiza el plato en la BBDD
	//devuelve 0 si funciona correctamente, 1 si hay un error con la BBDD y 2 si ya existe un plato con el nombre que le queremos poner.
	public int BDActualizar(){
		try{
			Statement st;
			String query;
			ResultSet rs;

			query = "SELECT * FROM Ingredientes WHERE Ingredientes.nombre = '" + nombre.toLowerCase() + "' AND Ingredientes.idIngrediente != '" + id + "'";
			st = conn.createStatement();
		    rs = st.executeQuery(query);

		    if (!rs.next()){
		    	query = "UPDATE Ingredientes SET nombre = '" + nombre.toLowerCase() + "', calorias = '" + calorias + "' WHERE idIngrediente = '" + id + "'";
				st = conn.createStatement();
				st.executeUpdate(query);
		    	return 0;
		    }

		    return 2;
		}
		catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println (ex);
            return 1;
        }
	}
}
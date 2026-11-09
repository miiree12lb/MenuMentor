class Ingrediente_Compra{
	private int id;
	private Ingrediente ingrediente;
	private int gramos;

	//Este método crea un ingrediente plato dado el ingrediente y los gramos que hay que comprar para la semana
	public Ingrediente_Compra(Ingrediente ingrediente, int gramos){
		this.ingrediente = ingrediente;
		this.gramos = gramos;
	}

	//Este método asigna un id al ingrediente compra
	public void setId(int id){
		this.id = id;
	}

	//Este método devuelve el id del ingrediente compra
	public int getId(){
		return id;
	}

	//este método le asigna el ingrediente dado al ingrediente compra
	public void setIngrediente(Ingrediente ingrediente){
		this.ingrediente = ingrediente;
	}

	//este método devuelve el ingrediente del ingrediente compra
	public Ingrediente getIngrediente(){
		return ingrediente;
	}

	//este método le asigna los gramos dados al ingrediente compra
	public void setGramos(int gramos){
		this.gramos = gramos;
	}

	//este método devuelve los gramos del ingrediente compra
	public int getGramos(){
		return gramos;
	}
}
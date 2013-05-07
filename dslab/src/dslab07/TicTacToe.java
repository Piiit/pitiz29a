package dslab07;

public class TicTacToe {
	
	private enum Field {
		EMPTY, CROSS, CIRCLE;
	}
	
	private String name;
	
	public TicTacToe(String name) {
		super();
		this.name = name;
	}

	private Field grid[][] = {
			{Field.EMPTY, Field.EMPTY, Field.EMPTY}, 
			{Field.EMPTY, Field.EMPTY, Field.EMPTY}, 
			{Field.EMPTY, Field.EMPTY, Field.EMPTY}, 
			};
	

	public void setField(int x, int y, Field type) throws Exception {
		if(type == Field.EMPTY) {
			throw new Exception("Please set CROSS or CIRCLE, not type EMPTY");
		}
		if(x < 0 || x > 2 || y < 0 || y > 2) {
			throw new ArrayIndexOutOfBoundsException("Coordinate out of bound");
		}
		if(grid[x][y] != Field.EMPTY) {
			throw new Exception("This field has been set already!");
		}
		grid[x][y] = type;
	}


	@Override
	public String toString() {
		String row = "";
		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < 2; y++) {
				switch(grid[x][y]) {
					case EMPTY:
						row += ".";
					break;
					case CROSS:
						row += "x";
					break;
					case CIRCLE:
						row += "o";
					break;
				}
			}
			row += "\n";
		}
		return row;
	}
	
	public String getName() {
		return name; 
	}
	
	

}

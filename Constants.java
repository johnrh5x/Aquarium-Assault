package john.aquariumassault;

public interface Constants {

	// Fields
	
	public static final int NATE       = 0;
	public static final int MATTHEW    = 1;
	public static final int ALICE      = 2;
	public static final int DOGFISH    = 3;
	public static final int BACKGROUND = 4;
	public static final int FISHTANK   = 5;
	public static final int NONE       = 6;

	public static final String[] ID = {"Nate","Matthew","Alice","Dogfish","Fishtank","None"};

	public static final int GRID_ROWS    = 13;
	public static final int GRID_COLUMNS = 11;
	public static final int GRID_STEP    = 15;
	public static final int EXIT_ROW     = 2;
	public static final int WORLD_WIDTH  = GRID_COLUMNS*GRID_STEP;
	public static final int WORLD_HEIGHT = (GRID_ROWS + 1)*GRID_STEP;

}

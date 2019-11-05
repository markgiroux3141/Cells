
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Random;

public class Simulator {
	public static final float CELL_RADIUS = 30;
	public static final float CELL_MASS = 10;
	public static final int CELL_DIVISION_TIME = 25;
	public static final int CELL_MATURITY_TIME = 250;
	public static final int TIME_STEPS_PER_DRAW = 500;
	public static final float DRAG_COEFF = .8f;
	public static final float INTER_CELL_FORCE_CONST = .1f;
	public static final float BETWEEN_CELL_FORCE_CONSTANT = .001f;
	public static final float FORCE_CONST = 0.1f;
	public static final boolean CALCULATE_EDGES = true;
	public static final float CELL_FORCE_CUTOFF_THRESH = 1.2f;
	public static final int SIMULATOR_SIZE = 1024;
	public static Random rand = new Random(124);
	List<Organism> organisms;

	public Simulator() {
		organisms = new ArrayList<>();
		int numStartCells = 2;

		Organism organism = new Organism();

		for(int i=0;i<numStartCells;i++){
			Cell cell = new Cell(new Node(new Vector2D(rand.nextFloat() * SIMULATOR_SIZE,rand.nextFloat() * SIMULATOR_SIZE), 10),10,CELL_RADIUS);
			if(i != (numStartCells - 1)){
				cell.setChildCellId(i + 1);
			}else{
				cell.setChildCellId(-1);
			}
			organism.addCell(cell);
		}
		//cell.setVelocity(new Vector2D(2f,2f));

		organisms.add(organism);
	}

	public void divideRandomCell(){
		organisms.get(0).divideCell(organisms.get(0).getCells().get(1));
	}

	public void freezeOrganismShape(){
		Physics.freezeOrganismShape(organisms.get(0));
	}

	public void run(Graphics g){
		for(int i=0;i<TIME_STEPS_PER_DRAW;i++){
			for(Organism org:organisms){
				for(int n=0;n<org.cells.size();n++){
					Cell cell1 = org.getCells().get(n);
					Physics.calculateInterCellInteractions(cell1);

					Vector2D wallForce = Physics.scaleForce(Physics.calculateForceFromWalls(cell1.getCenterNode(),SIMULATOR_SIZE,SIMULATOR_SIZE),FORCE_CONST);
					Physics.calculateNewPositionFromForce(cell1.getCenterNode(), wallForce);

					for(int q=(n+1);q<org.cells.size();q++){
							Cell cell2 = org.getCells().get(q);
							boolean calculateCurrentCell = Physics.nodeWithinRange(CELL_FORCE_CUTOFF_THRESH, cell1.getCenterEquilibrium(), cell1.getCenterNode(), cell2.getCenterNode());
							if(calculateCurrentCell){
								Physics.calculateBetweenCellCenterInteractions(cell1, cell2);
								Physics.calculateBetweenCellEdgeInteractions(cell1,cell2);
							}
						}
					Physics.addFriction(cell1.getCenterNode(), DRAG_COEFF);
//					Physics.addFriction(cell.getEdgeNode(), DRAG_COEFF);
					if(i == 0)CellGraphics.drawCell(cell1,g);
				}
			}
		}
	}
}

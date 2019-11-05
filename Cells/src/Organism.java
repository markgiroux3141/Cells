import java.util.ArrayList;
import java.util.List;

public class Organism {
	public static final int DIVISION_DISPLACEMENT = 3;

	List<Cell> cells;
	int numCells;

	public Organism(){
		cells = new ArrayList<>();
		numCells = 0;
	}

	public void addCell(Cell cell){
		cell.setId(numCells);

		if (numCells == 0) {
			cell.setParentCellId(-1);
		}else{
			cell.setParentCellId(numCells - 1);
		}

		if(Simulator.CALCULATE_EDGES) Physics.createRandomEdgeEq(cell);
		cell.setCenterEquilibrium(2f * cell.getRadius());
		cells.add(cell);
		numCells++;
	}

	public void addCell(Cell cell, int parentId, int childId){
		cell.setId(numCells);
		cell.setParentCellId(parentId);
		cell.setChildCellId(childId);
		cells.add(cell);
		numCells++;
	}

	public void divideCell(Cell cell){
		Cell newCell = new Cell(cell.getCenterNode(),cell.getMass(),cell.getRadius());
		Physics.displaceCell(newCell, ((Simulator.rand.nextFloat() * 2f) - 1f) * DIVISION_DISPLACEMENT,((Simulator.rand.nextFloat() * 2f) - 1f) * DIVISION_DISPLACEMENT);
		newCell.setCenterEquilibrium(2f * cell.getRadius());
		addCell(newCell,cell.getId(),cell.getChildCellId());
		cell.setChildCellId(newCell.getId());
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public int getNumCells() {
		return numCells;
	}

	public void setNumCells(int numCells) {
		this.numCells = numCells;
	}
}

import java.util.List;

public class Cell {
	int id;
	Node centerNode;
	List<Node> edgeNodes;
	float mass;
	float radius;
	float centerEquilibrium;
	float edgeEquilibrium;
	int parentCellId;
	int childCellId;
	int edgeId;
	boolean calculated;

	public Cell (Node centerNode, float mass, float radius) {
		this.centerNode = centerNode;
		this.mass = mass;
		this.radius = radius;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Node getCenterNode() {
		return centerNode;
	}

	public void setCenterNode(Node centerNode) {
		this.centerNode = centerNode;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getCenterEquilibrium() {
		return centerEquilibrium;
	}

	public void setCenterEquilibrium(float centerEquilibrium) {
		this.centerEquilibrium = centerEquilibrium;
	}

	public float getEdgeEquilibrium() {
		return edgeEquilibrium;
	}

	public void setEdgeEquilibrium(float edgeEquilibrium) {
		this.edgeEquilibrium = edgeEquilibrium;
	}

	public int getParentCellId() {
		return parentCellId;
	}

	public void setParentCellId(int parentCellId) {
		this.parentCellId = parentCellId;
	}

	public int getChildCellId() {
		return childCellId;
	}

	public void setChildCellId(int childCellId) {
		this.childCellId = childCellId;
	}

	public List<Node> getEdgeNodes() {
		return edgeNodes;
	}

	public void setEdgeNodes(List<Node> edgeNodes) {
		this.edgeNodes = edgeNodes;
	}

	public void addEdgeNode(Node edgeNode){
		edgeNodes.add(edgeNode);
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

	public int getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}
}

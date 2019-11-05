public class Node {
	Vector2D position;
	Vector2D velocity;
	Vector2D acceleration;
	float mass;
	int targetNode;
	float distToNextNode;

	public Node(Vector2D position, float mass) {
		this.position = position;
		this.velocity = new Vector2D(0,0);
		this.acceleration = new Vector2D(0,0);
		this.mass = mass;
	}

	public Node(Vector2D position, float mass, int targetNode) {
		this.position = position;
		this.velocity = new Vector2D(0,0);
		this.acceleration = new Vector2D(0,0);
		this.mass = mass;
		this.targetNode = targetNode;
	}

	public Node(Vector2D position, float mass, int targetNode, float distToNextNode) {
		this.position = position;
		this.velocity = new Vector2D(0,0);
		this.acceleration = new Vector2D(0,0);
		this.mass = mass;
		this.targetNode = targetNode;
		this.distToNextNode = distToNextNode;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public Vector2D getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public int getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(int targetNode) {
		this.targetNode = targetNode;
	}

	public float getDistToNextNode() {
		return distToNextNode;
	}

	public void setDistToNextNode(float distToNextNode) {
		this.distToNextNode = distToNextNode;
	}
}

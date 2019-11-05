import java.util.Vector;
import java.util.Random;

public class Physics {

	public static final float WALL_FORCE = 1f;
	public static final float WALL_FORCE_THRESH = 300f;
	public static final float WALL_FORCE_GRADIENT = 0.01f;
	public static final float MAX_FORCE = 100000f;
	public static float GRAVITY = 0.000f;
	public static final float NO_DIV_BY_ZERO = 0.0001f;
	public static final float OUTER_FORCE_SCALER = 1f;

	public static Random rand = new Random(124);

	public static void calculateBetweenCellEdgeInteractions(Cell cell1, Cell cell2){
		if(cell1.getEdgeNodes() != null && cell2.getEdgeNodes() != null){
			calculateNodeInteractions(cell1.getEdgeNodes().get(cell2.getEdgeId()), cell2.getEdgeNodes().get(cell1.getEdgeId()),0f,Simulator.BETWEEN_CELL_FORCE_CONSTANT);
		}
	}

	public static void calculateBetweenCellCenterInteractions(Cell cell1, Cell cell2){
		calculateNodeInteractions(cell1.getCenterNode(), cell2.getCenterNode(),cell1.getCenterEquilibrium(),Simulator.BETWEEN_CELL_FORCE_CONSTANT);
	}

	public static void calculateInterCellInteractions(Cell cell){
		if(cell.getEdgeNodes() != null){
			for(int i=0;i<cell.getEdgeNodes().size();i++){
				calculateNodeInteractions(cell.getEdgeNodes().get(i),cell.getCenterNode(), Simulator.CELL_RADIUS, Simulator.INTER_CELL_FORCE_CONST);
				if(i != cell.getEdgeNodes().size() - 1) calculateNodeInteractions(cell.getEdgeNodes().get(i), cell.getEdgeNodes().get(i+1),cell.getEdgeNodes().get(i).getDistToNextNode(),Simulator.INTER_CELL_FORCE_CONST);
			}
		}
	}

	public static void calculateNodeInteractions(Node node1, Node node2, float eqDist, float forceConstant){
		Vector2D force = Physics.scaleForce(Physics.calculateForceBetweenNodes(node1, node2, eqDist, false),forceConstant);
		Physics.calculateNewPositionFromForce(node1, force);
		Physics.calculateNewPositionFromForce(node2,Physics.getOppositeForce(force));
	}

	public static void calculateNewPositionFromForce(Node node, Vector2D force){
		Vector2D currentAcceleration = node.getAcceleration();
		Vector2D newAcceleration = new Vector2D((force.getX()/node.mass), (force.getY()/node.mass) + GRAVITY);
		node.setAcceleration(newAcceleration);

		Vector2D currentVelocity = node.getVelocity();
		Vector2D newVelocity = new Vector2D(currentVelocity.getX() + currentAcceleration.getX(), currentVelocity.getY() + currentAcceleration.getY());
		node.setVelocity(newVelocity);

		Vector2D currentPosition = node.getPosition();
		Vector2D newPosition = new Vector2D(currentPosition.getX() + currentVelocity.getX(), currentPosition.getY() + currentVelocity.getY());
		node.setPosition(newPosition);
	}

	public static Vector2D calculateForceFromWalls(Node node, int width, int height){
		float distL = node.getPosition().getX();
		float distR = width - node.getPosition().getX();
		float distT = node.getPosition().getY();
		float distB = height - node.getPosition().getY();

		float forceX = 0;
		if(distL <= WALL_FORCE_THRESH){
			forceX = (1f / (distL * WALL_FORCE_GRADIENT)) * WALL_FORCE;
		}else if(distR <= WALL_FORCE_THRESH){
			forceX = -(1f / (distR * WALL_FORCE_GRADIENT)) * WALL_FORCE;
		}

		float forceY = 0;
		if(distT <= WALL_FORCE_THRESH){
			forceY = (1f / distT * WALL_FORCE_GRADIENT) * WALL_FORCE;
		}else if(distB <= WALL_FORCE_THRESH){
			forceY = -(1f / distB * WALL_FORCE_GRADIENT) * WALL_FORCE;
		}

		return new Vector2D(forceX, forceY);
	}

	public static boolean nodeWithinRange(float cutOffDist, float eqDist, Node node1, Node node2){
		return calculateDistance(node1.getPosition(), node2.getPosition()) < (cutOffDist * eqDist);
	}

	public static Vector2D calculateForceBetweenNodes(Node node1, Node node2, float eqDist, boolean useOuterForceScaler){
		return getSpringForceFromVectors(node1.getPosition(), node2.getPosition(), eqDist, useOuterForceScaler);
	}

	public static float calculateDistance(Vector2D vec1, Vector2D vec2){
		return (float)Math.sqrt(calculateSqrDistance(vec1, vec2));
	}

	public static float calculateSqrDistance(Vector2D vec1, Vector2D vec2){
		return ((vec1.getX() - vec2.getX()) * (vec1.getX() - vec2.getX())) + ((vec1.getY() - vec2.getY()) * (vec1.getY() - vec2.getY()));
	}

	public static Vector2D getSpringForceFromVectors(Vector2D vec1, Vector2D vec2, float eqDist, boolean useOuterForceScaler){
		float currentDist = calculateDistance(vec1, vec2);
		float scalerForce = currentDist - eqDist;
		if(useOuterForceScaler && scalerForce > 0) scalerForce *= OUTER_FORCE_SCALER;
		if(currentDist == 0) currentDist = NO_DIV_BY_ZERO;

		float xBasisVector = (vec2.getX() - vec1.getX())/currentDist;
		float yBasisVector = (vec2.getY() - vec1.getY())/currentDist;

		Vector2D force = new Vector2D(scalerForce * xBasisVector, scalerForce * yBasisVector);
		return force;
	}

	public static float getCenterEdgeDistance(float edgeEqDist, float radius){
		edgeEqDist /= radius;
		float x = 1f + (edgeEqDist/2f);
		float y = (float)Math.sin((Math.acos((edgeEqDist/2f) - 1f)));
		float result = (float)Math.sqrt((x*x) + (y*y));
		return result * radius;
	}

	public static void addFriction(Node node, float dragCoeff){
		node.setVelocity(scaleForce(node.getVelocity(), dragCoeff));
	}

	public static void displaceCell(Cell cell, float amtX, float amtY){
		Vector2D position = cell.getCenterNode().getPosition();
		cell.setCenterNode(new Node(new Vector2D(position.getX() + amtX, position.getY() + amtY), cell.getCenterNode().getMass()));
	}

	public static void createRandomEdgeEq(Cell cell){
		cell.setEdgeEquilibrium(2f * cell.getRadius());
	}

	public static Vector2D getOppositeForce(Vector2D vec){
		return new Vector2D(vec.getX() * -1f, vec.getY() * -1f);
	}

	public static Vector2D scaleForce(Vector2D vec, float scale){
		return new Vector2D(vec.getX() * scale, vec.getY() * scale);
	}

	public static Vector2D limitForce(Vector2D force, float maxForce){
		float xForce = force.getX();
		float yForce = force.getY();

		if(xForce > maxForce){
			xForce = maxForce;
		}else if(xForce < -maxForce){
			xForce = -maxForce;
		}

		if(yForce > maxForce){
			yForce = maxForce;
		}else if(yForce < -maxForce){
			yForce = -maxForce;
		}

		return new Vector2D(xForce, yForce);
	}

	public static Vector2D calculateMidPoint(Vector2D p1, Vector2D p2){
		return new Vector2D((p1.getX() + p2.getX())/2f, (p1.getY() + p2.getY())/2f);
	}

	public static void freezeOrganismShape(Organism org){
		int numCells = org.getCells().size();
		for(int i=0;i<numCells;i++){
			for(int n=(i+1);n<numCells;n++){
				Cell cell1 = org.getCells().get(i);
				Cell cell2 = org.getCells().get(n);

				int maxCellEdge1 = (cell1.getEdgeNodes() == null)?0:cell1.getEdgeNodes().size();
				int maxCellEdge2 = (cell2.getEdgeNodes() == null)?0:cell2.getEdgeNodes().size();

				boolean nodeWithinRange = nodeWithinRange(Simulator.CELL_FORCE_CUTOFF_THRESH,Simulator.CELL_RADIUS, cell1.getCenterNode(), cell2.getCenterNode());
				if(nodeWithinRange){
					Vector2D midPoint = calculateMidPoint(cell1.getCenterNode().getPosition(), cell2.getCenterNode().getPosition());
					cell1.addEdgeNode(new Node(midPoint, Simulator.CELL_MASS, maxCellEdge2));
					cell1.setEdgeId(maxCellEdge2);
					cell2.addEdgeNode(new Node(midPoint, Simulator.CELL_MASS, maxCellEdge1));
					cell2.setEdgeId(maxCellEdge1);
				}
			}
		}

		for(int i=0;i<org.getCells().size();i++){
			Cell cell = org.getCells().get(i);
			if(cell.getEdgeNodes().size() >=2){
				for(int n=0;n<cell.getEdgeNodes().size() - 1;i++){
					cell.getEdgeNodes().get(n).setDistToNextNode(calculateDistance(cell.getEdgeNodes().get(n).getPosition(), cell.getEdgeNodes().get(n+1).getPosition()));
				}
			}
		}
	}

	public static void setGravity(float gravity){
		GRAVITY = gravity;
	}

}

import java.awt.*;

public class CellGraphics {

	public static void drawCell(Cell cell, Graphics g){
		int xPos = (int)cell.getCenterNode().getPosition().getX();
		int yPos = (int)cell.getCenterNode().getPosition().getY();
		int radius = (int)cell.getRadius();



		//int currRadius = (Simulator.CALCULATE_EDGES)?(int)Physics.calculateDistance(new Vector2D(xPos,yPos), new Vector2D(edgeXPos, edgeYPos)):radius;
		g.setColor(new Color(0f,0f,1f));
		drawCircle(xPos, yPos,radius, g);
		g.setColor(new Color(0f,0.5f,0f));

		if(cell.getEdgeNodes() != null){
			for(int i=0;i<cell.getEdgeNodes().size();i++){
				int edgeXPos = (int)cell.getEdgeNodes().get(i).getPosition().getX();
				int edgeYPos = (int)cell.getEdgeNodes().get(i).getPosition().getY();
				drawCircle(edgeXPos, edgeYPos,5, g);
			}
		}

	}

	public static void drawCircle(int x, int y, int radius, Graphics g){
		g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
		g.drawLine(0,0,100,100);
	}

}


import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class DrawPanel extends JFrame {

	private static int DELAY = 40;
	Simulator simulator;
	int counter;

	public DrawPanel(){
		simulator = new Simulator();
		counter = 1;
	}

	public void paint(Graphics g) {
		super.paint(g);
		simulator.run(g);
	}

	public void go() {
		TimerTask task = new TimerTask() {
			public void run() {
				repaint();
				Toolkit.getDefaultToolkit().sync();
				if((counter % Simulator.CELL_DIVISION_TIME) == 0) simulator.divideRandomCell();
//				if(counter == Simulator.CELL_MATURITY_TIME) {
//					simulator.freezeOrganismShape();
//					Physics.setGravity(0.001f);
//				}
				counter++;
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, DELAY);
	}

	public static void main(String args[]) {
		DrawPanel f = new DrawPanel();
		f.setSize(Simulator.SIMULATOR_SIZE, Simulator.SIMULATOR_SIZE);
		f.show();
		f.go();
	}
}
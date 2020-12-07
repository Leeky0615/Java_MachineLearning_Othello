package othello;

import java.awt.Point;

import othello.Constants.EState;
import othello.Constants.EStone;

public class Main {

	public static void main(String[] args) {
		OMap oMap = new OMap();
		oMap.init();
		Othello othello = new Othello(oMap);
		othello.init();
		othello.countStones();
		System.out.println("=======================");
		othello.putStone(new Point(3,2));
		System.out.println("=======================");
		othello.putStone(new Point(2,4));
		System.out.println("=======================");
		othello.putStone(new Point(3,5));
		System.out.println("=======================");
		othello.putStone(new Point(4,2));
		
	}

}

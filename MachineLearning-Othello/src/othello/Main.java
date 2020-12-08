package othello;

import java.awt.Point;
import java.util.Vector;

import othello.Constants.EState;
import othello.Constants.EStone;

public class Main {

	public static void main(String[] args) {
		OMap oMap = new OMap();
		oMap.init();
		Othello othello = new Othello(oMap);
		othello.init();
		while(true) {
			othello.findPossible();
			if(othello.getPossiblePoints().size()==0) {
				othello.setCurrentS(othello.getCurrentS());
				othello.findPossible();
			}
			othello.putStone(othello.getPossiblePoints().get(0));
		}
		
//		int[][] asd = null;
//		for (int j = 0; j < 9; j++) {
//			System.out.println(othello.getCurrentS());
//			othello.findPossible();
//			othello.putStone(othello.getPossiblePoints().get(0));
//			if(j==8) asd = othello.getMap();
//		}
//		System.out.println("=========");
//		othello.setMap(asd);
//		othello.drawMap();
//		Point p = new Point(5,4);
//		System.out.println(othello.isEmpty(p));;
//		Vector<Point> psPs = othello.possibleSurroundingP(p);
//		System.out.println(psPs);
//		for(Point psP : psPs) {
//			System.out.println(psP);
//			Point ptoCheck = new Point((2*psP.x)-p.x, (2*psP.y)-p.y);
//			System.out.println(ptoCheck);
//			System.out.println(othello.isInMap(ptoCheck)&&!othello.isEmpty(ptoCheck));
//			System.out.println(othello.getStoneColor(ptoCheck) == othello.getCurrentS());
//			othello.addPoint(psP,ptoCheck);
//			System.out.println(othello.getAvailablechangePs().size());
//			System.out.println(othello.getPossiblePoints().size());
//		}
		
		
		
//		System.out.println("Step0=========================");
//		othello.drawMap();
//		System.out.println("Step1=========================");
//		othello.findPossible();
//		othello.putStone(new Point(3,2));
//		System.out.println("Step2=========================");
//		othello.findPossible();
//		othello.putStone(new Point(2,2));
//		System.out.println("Step3=========================");
//		othello.findPossible();
//		othello.putStone(new Point(1,2));
//		System.out.println("Step4=========================");
//		othello.findPossible();
//		othello.putStone(new Point(1,1));
//		System.out.println("Step5=========================");
//		othello.findPossible();
//		Vector<Point> points = new Vector<Point>();
//		for(EState eState: EState.values()) {
//			Point newP = new Point(1 + eState.getX(), 0 + eState.getY());
//			System.out.println(newP);
//			// 끝 값들은 제외 && 현재 놓는 돌의 색과 다르면 벡터에 널기
////			if(newP.x >= 0 && newP.y >= 0 ) {
////				
////			}
//			if(othello.isInMap(newP) && !othello.isEmpty(newP) && othello.getMap()[newP.x][newP.y] ==1){
//				points.add(newP);
//			}
//		}
//		System.out.println(points);
//		othello.possibleSurroundingP(new Point(1,0));
//		System.out.println("Step7=========================");
//		othello.findPossible();
//		othello.putStone(new Point(1,2));
//		othello.putStone(new Point(2,2));
//		othello.findPossible();
//		System.out.println("=======================");
//		othello.putStone(new Point(2,4));
//		System.out.println("=======================");
//		othello.putStone(new Point(3,5));
//		System.out.println("=======================");
//		othello.putStone(new Point(4,2));
//		System.out.println("=======================");
//		othello.putStone(new Point(4,5));
//		System.out.println("=======================");
//		othello.putStone(new Point(5,4));
//		System.out.println("=======================");
		
	}

}

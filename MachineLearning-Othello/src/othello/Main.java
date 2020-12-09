package othello;

import java.awt.Point;
import java.util.Scanner;

import othello.Constants.EStone;

public class Main {

	public static void main(String[] args) {
		OMap oMap = new OMap();
		oMap.init();
		Othello othello = new Othello(oMap);
		othello.init();

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (true) {
			if(othello.getCurrentS() == EStone.Black) System.out.println("플레이어의 돌 : ○");
			else if(othello.getCurrentS() == EStone.White) System.out.println("플레이어의 돌 : x");
			othello.findPossible();
			othello.drawMap();
			System.out.println("x 좌표를 입력하세요.");
			int x = sc.nextInt();
			System.out.println("y 좌표를 입력하세요.");
			int y = sc.nextInt();
			
			othello.putStone(new Point(x,y));
		}
	}

}

package othello;

import java.awt.Point;
import java.util.Vector;

import othello.Constants.EState;
import othello.Constants.EStone;

public class Othello {
	private EStone currentS;
	private EStone otherS;
	private OMap oMap;
	private int[][] map;
	private Vector<Point> availablechangePs;
	
	public Othello(OMap oMap) {
		this.oMap = oMap;
	}

	public void init() {
		this.currentS = EStone.Black;
		this.otherS = EStone.White;
		this.oMap.init();
		this.map = oMap.getMap();
		this.availablechangePs = new Vector<Point>();
	}
	// 놓는 돌의 색 초기화
	public void setCurrentS(EStone eStone) {
		this.otherS = currentS;
		this.currentS = eStone;
	}
	// 벡터 초기화
	private void clearVector() {
		this.availablechangePs = new Vector<Point>();
	}
	// 놓는 자리가 빈 공간인지 확인.
	public Boolean isEmpty(Point p) {
		if(this.map[p.x][p.y] == OMap.Empty) return true;
		else return false;
	}
	// 포인트가 Map을 벗어나는 위치인지 확인.
	public Boolean isInMap(Point P) {
		if(P.x > 0 && P.x < 7) {
			if(P.y > 0 && P.y < 7) return true;
			else return false;
		}else return false;
	}
	// 돌의 색 리턴
	public EStone getStoneColor(Point p) {
		return EStone.values()[this.map[p.x][p.y]];
	}
	// 놓는 자리의 주변에 놓을 수 있는 포인트들 담기(주변에 다른색의 돌만 있으면 가능) -> 1차 확인
	public Vector<Point> possibleSurroundingP(Point p) {
		// 새로 놓는 자리에 8방향의 포인트 넣기
		Vector<Point> points = new Vector<Point>();
		for(EState eState: EState.values()) {
			Point newP = new Point(p.x + eState.getX(), p.y + eState.getY());
			// 끝 값들은 제외 && 현재 놓는 돌의 색과 다르면 벡터에 널기
			if(isEmpty(newP) && isInMap(newP) && map[newP.x][newP.y] != this.currentS.ordinal()){
				points.add(newP);
			}
		}
		return points;
	}
	
	public void addPoint(Point currentP, Point possibleP) {
		Point ptoCheck = new Point((2*currentP.x)-possibleP.x, (2*currentP.y)-possibleP.y);
		if(!isEmpty(ptoCheck) && isInMap(ptoCheck)) {
			// 대칭되는 돌이 놓으려고 하는 돌과 다르면 다시 대칭되는 돌 확인. 
			if(this.getStoneColor(ptoCheck) == this.currentS) this.availablechangePs.add(ptoCheck);
			else addPoint(ptoCheck, currentP);
		}
	}
	
	// 1차 확인된 돌을 중심으로 대칭되는 돌이 놓으려고하는 돌과 같은지 비교해 돌을 놓았을 때 뒤집힐 수 있는 대칭되는 돌을 담음.
	public void possiblesPoints(Point p) {
		Vector<Point> possiblePs = possibleSurroundingP(p);
		for(Point possibleP : possiblePs) {
			addPoint(p,possibleP);
		}
	}
	// 돌 뒤집기.
	private void changeStones(Point p) {
		for(Point oppsiteP : this.availablechangePs) {
			if(oppsiteP.x == p.x) {
				for (int i = Math.min(oppsiteP.y, p.y); i <= Math.max(oppsiteP.y, p.y); i++) {
					this.map[p.x][i] = this.currentS.ordinal();
				}
			}else if(oppsiteP.y == p.y) {
				for (int i = Math.min(oppsiteP.x, p.x); i <= Math.max(oppsiteP.x, p.x); i++) {
					this.map[i][p.y] = this.currentS.ordinal();
				}
			}else {
				int x = Math.min(oppsiteP.x, p.x);
				int y;
				EState state;
				if(oppsiteP.x == x) {
					y = oppsiteP.y;
					if(y<p.y) state = EState.ES;
					else state = EState.NW;
				}else {
					y = p.y;
					if(y<oppsiteP.y) state = EState.ES;
					else state = EState.NW;
				}
				
				for (int i = x; i <= Math.max(oppsiteP.x, p.x); i++) {
					if(state == EState.ES) {
						this.map[i][y++] = this.currentS.ordinal();
					}else if(state == EState.NW) {
						this.map[i][y--] = this.currentS.ordinal();
					}
				}
			}
		}
	}
	public void putStone(Point p) {
		this.clearVector();
		if (isEmpty(p)) this.possiblesPoints(p);
		this.changeStones(p);
		this.countStones();
		this.setCurrentS(this.otherS);
	}
	
	public void countStones() {
		int black = 0;
		int white = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(this.map[j][i]);
				if(this.map[i][j]==EStone.Black.ordinal()) black++;
				else if(this.map[i][j]==EStone.White.ordinal()) white++;
			}
			System.out.println();
		}
		System.out.println("흑돌 : "+black+"개 / 흰돌 : "+white+"개");
	}
	

	
}

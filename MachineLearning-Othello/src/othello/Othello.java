package othello;

import java.awt.Point;
import java.util.Vector;

import othello.Constants.EState;
import othello.Constants.EStone;

public class Othello {
	private int playtime;
	private EStone currentS;
	private EStone otherS;
	private OMap oMap;
	private int[][] map;
	private Vector<Point> availablechangePs;
	private Vector<Point> possiblePoints;
	private Vector<int[][]> playBoards;
	
	public Othello(OMap oMap) {this.oMap = oMap;}
	public Vector<Point> getPossiblePoints() {return possiblePoints;}
	public void setPossiblePoints(Vector<Point> possiblePoints) {this.possiblePoints = possiblePoints;}
	public int[][] getMap() {return this.copyMap(map);}
	public void setMap(int[][] map) {this.map = this.copyMap(map);}
	public EStone getOtherS() {return otherS;}
	public void setOtherS(EStone otherS) {this.otherS = otherS;}
	public EStone getCurrentS() {return currentS;}
	public Vector<Point> getAvailablechangePs() {return availablechangePs;}
	public void setAvailablechangePs(Vector<Point> availablechangePs) {this.availablechangePs = availablechangePs;}

	
	public void init() {
		this.currentS = EStone.Black;
		this.otherS = EStone.White;
		this.oMap.init();
		this.map = oMap.getMap();
		this.availablechangePs = new Vector<Point>();
		this.possiblePoints = new Vector<Point>();
		this.playBoards = new Vector<int[][]>();
		this.playBoards.add(copyMap(this.map));
	}
	public int[][] copyMap(int[][] map){
		int[][] copyMap = new int[map.length][map[0].length];
		for (int i = 0; i < copyMap.length; i++) {
			System.arraycopy(map[i], 0, copyMap[i], 0, map[0].length);
		}
		return copyMap;
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
		if(P.x >= 0 && P.x < 8) {
			if(P.y >= 0 && P.y < 8) return true;
			else return false;
		}else return false;
	}
	// 돌의 색 리턴
	public EStone getStoneColor(Point p) {
		return EStone.values()[this.map[p.x][p.y]];
	}
	// 놓는 자리의 주변에 놓을 수 있는 포인트들 담기(주변에 다른색의 돌만 있으면 가능) -> 1차 확인
	public Vector<Point> possibleSurroundingP(Point currentP) {
		// 새로 놓는 자리에 8방향의 포인트 넣기
		Vector<Point> points = new Vector<Point>();
		for(EState eState: EState.values()) {
			Point newP = new Point(currentP.x + eState.getX(), currentP.y + eState.getY());
			// 끝 값들은 제외 && 현재 놓는 돌의 색과 다르면 벡터에 널기
			if(isInMap(newP) && !isEmpty(newP) && map[newP.x][newP.y] == this.otherS.ordinal()){
				points.add(newP);
			}
		}
		return points;
	}
	
	public void addPoint(Point currentP, Point psP) {
		Point ptoCheck = new Point((2*psP.x)-currentP.x, (2*psP.y)-currentP.y);
		if(isInMap(ptoCheck)&& !isEmpty(ptoCheck)) {
			// 놓는 돌이랑 같이 뒤집혀 질 수 있는 반대편의 돌들을 벡터에 담음. 
			if(this.getStoneColor(ptoCheck) == this.currentS) this.availablechangePs.add(ptoCheck);
			else addPoint(psP, ptoCheck);
		}
	}
	
	// 1차 확인된 돌을 중심으로 대칭되는 돌이 놓으려고하는 돌과 같은지 비교해 돌을 놓았을 때 뒤집힐 수 있는 대칭되는 돌을 담음.
	public void possiblesPoints(Point currentP) {
		try {
			Vector<Point> psPs = possibleSurroundingP(currentP);
			for(Point psP : psPs) {addPoint(currentP,psP);}
		}catch (ArrayIndexOutOfBoundsException e) {}
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
	public Boolean checkStone(Point p) {
		// 놓는 곳이 빈곳이면
		if (isEmpty(p)) {
			// 
			this.possiblesPoints(p);
			if(this.availablechangePs.size() != 0) {
				this.possiblePoints.add(p);
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	public void putStone(Point p) {
		this.possiblesPoints(p);
		this.changeStones(p);
		this.drawMap();
		this.clearVector();
		this.possiblePoints = new Vector<Point>();
		this.setCurrentS(this.otherS);
	}
	
	public void countPoints() {
		int point = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(this.currentS == EStone.Black) {
					if(this.map[j][i]==EStone.Black.ordinal()) point++;
					else if(this.map[j][i]==EStone.White.ordinal()) point--;
				}else {
					if(this.map[j][i]==EStone.Black.ordinal()) point--;
					else if(this.map[j][i]==EStone.White.ordinal()) point++;
				}
			}
		}
		System.out.println("점수: "+point);
	}
	
	public void findPossible() {
		int[][] originMap = copyMap(this.map);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				Point p = new Point(j, i);
				if(checkStone(p)) {
					System.out.print("좌표: (" + j + ", " + i + ") / ");
					this.changeStones(p);
					this.countPoints();
					System.out.println("- - - - - - - - - - - - -");
					this.map = copyMap(originMap);
					this.clearVector();
				}
			}
		}
		System.out.println("놓을 수 있는 포인트 개수  : " + this.possiblePoints.size());
		if (this.possiblePoints.size() == 0) {
			this.setCurrentS(this.otherS);
		}
	}
	public void drawMap() {
		int[][] originMap = this.map;
		for (int i = 0; i < originMap.length; i++) {
			for (int j = 0; j < originMap.length; j++) {
				System.out.print(originMap[j][i]);
				}
			System.out.println();
		}
	}
}

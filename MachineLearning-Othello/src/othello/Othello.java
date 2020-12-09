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
	private Vector<Point> possiblePoints;
	private Vector<int[][]> playBoards;
	
	public Othello(OMap oMap) {this.oMap = oMap;}
	
	// Getter & Setter
	public Vector<Point> getPossiblePoints() {return possiblePoints;}
	public void setPossiblePoints(Vector<Point> possiblePoints) {this.possiblePoints = possiblePoints;}
	public int[][] getMap() {return this.copyMap(map);}
	public void setMap(int[][] map) {this.map = this.copyMap(map);}
	public EStone getOtherS() {return otherS;}
	public void setOtherS(EStone otherS) {this.otherS = otherS;}
	public EStone getCurrentS() {return currentS;}
	public int getPlaytime() {return playtime;}
	public void setPlaytime(int playtime) {this.playtime = playtime;}
	public Vector<int[][]> getPlayBoards() {return playBoards;}
	public void setPlayBoards(Vector<int[][]> playBoards) {this.playBoards = playBoards;}
	
	public void init() {
		this.currentS = EStone.Black;
		this.otherS = EStone.White;
		this.oMap.init();
		this.map = oMap.getMap();
		this.possiblePoints = new Vector<Point>();
		this.playBoards = new Vector<int[][]>(10);
		this.playBoards.add(copyMap(this.map));
		this.playtime = 0;
	}
	public int[][] copyMap(int[][] map){
		int[][] copyMap = new int[map.length][map[0].length];
		for (int i = 0; i < copyMap.length; i++) {
			System.arraycopy(map[i], 0, copyMap[i], 0, map[0].length);
		}
		return copyMap;
	}
		
	// 놓는 돌의 색 초기화
	public void setCurrentS() {
		EStone temp = this.otherS;
		this.otherS = currentS;
		this.currentS = temp;
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
	public Vector<Point> possibleSurroundingP(Point cP) {
		// 새로 놓는 자리에 8방향의 포인트 넣기
		Vector<Point> points = new Vector<Point>();
		for(EState eState: EState.values()) {
			Point newP = new Point(cP.x + eState.getX(), cP.y + eState.getY());
			// 끝 값들은 제외 && 현재 놓는 돌의 색과 다르면 벡터에 널기
			if(isInMap(newP) && !isEmpty(newP) && getStoneColor(newP) == this.otherS){
				points.add(newP);
			}
		}
		return points;
	}
	// 1차 확인된 돌을 중심으로 대칭되는 돌이 놓으려고하는 돌과 같은지 비교해 돌을 놓았을 때 뒤집힐 수 있는 대칭되는 돌을 담음.
	public Vector<Point> variableEndPoints(Point cP) {
		try {
			Vector<Point> availablechangePs = new Vector<Point>();
			Vector<Point> psPs = possibleSurroundingP(cP);
			for(Point psP : psPs) {addPoint(availablechangePs,cP,psP);}
			return availablechangePs;
		}catch (ArrayIndexOutOfBoundsException e) {return null;}
	}
	
	public void addPoint(Vector<Point> vePVector, Point cP, Point veP) {
		Point ptoCheck = new Point((2*veP.x)-cP.x, (2*veP.y)-cP.y);
		if(isInMap(ptoCheck) && !isEmpty(ptoCheck)) {
			// 놓는 돌이랑 같이 뒤집혀 질 수 있는 반대편의 돌들을 벡터에 담음. 
			if(this.getStoneColor(ptoCheck) == this.currentS) vePVector.add(ptoCheck);
			else addPoint(vePVector, veP, ptoCheck);
		}
	}
	// 뒤집을 수 있는 포인트인지 확인하는 함수
	public Boolean checkStone(Point cP) {
		if (isEmpty(cP)) {
			if(this.variableEndPoints(cP).size() != 0) {
				this.possiblePoints.add(cP);
				return true;
			}else {return false;}
		}else {return false;}
	}
	
	// 돌 뒤집기.
	private void changeStones(Vector<Point> vePVector, Point cP) {
		for(Point veP : vePVector) {
			if(veP.x == cP.x) { // 세로
				for (int i = Math.min(veP.y, cP.y); i <= Math.max(veP.y, cP.y); i++) {
					this.map[cP.x][i] = this.currentS.ordinal();
				}
			}else if(veP.y == cP.y) { // 가로
				for (int i = Math.min(veP.x, cP.x); i <= Math.max(veP.x, cP.x); i++) {
					this.map[i][cP.y] = this.currentS.ordinal();
				}
			}else { // 대각선
				int x = Math.min(veP.x, cP.x);
				int y;
				EState state;
				// 방향설정
				if(veP.x == x) {
					y = veP.y;
					if(y < cP.y) state = EState.ES;
					else state = EState.NW;
				}else {
					y = cP.y;
					if(y < veP.y) state = EState.ES;
					else state = EState.NW;
				}
				// 뒤집기
				if (state == EState.ES) {
					for (int i = x; i <= Math.max(veP.x, cP.x); i++) {
						this.map[i][y++] = this.currentS.ordinal();
					}
				}else if(state == EState.NW) {
					for (int i = x; i <= Math.max(veP.x, cP.x); i++) {
						this.map[i][y--] = this.currentS.ordinal();
					}
				}
			}
		}
	}
	
	// 포인트에 돌을 놓는 함수
	public void putStone(Point cP) {
		if (this.variableEndPoints(cP).size() == 0) {
			System.out.println("놓을 수 없는 자리입니다.");
		}else {
			this.changeStones(this.variableEndPoints(cP), cP); // 돌 뒤집기
			this.possiblePoints = new Vector<Point>(); // 가능한 점 초기화
			this.setCurrentS(); // 플레이어 변경
			this.playtime++;
			this.playBoards.add(this.playtime,this.copyMap(this.map));
		}
	}
	
	// 점수 계산 함수.
	public int countPoints() {
		int point = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.currentS == EStone.Black) {
					if (this.map[j][i] == EStone.Black.ordinal()) point++; // 좌표의 돌이 '흑'이면 +1
					else if(this.map[j][i] == EStone.White.ordinal()) point--; // 좌표의 돌이 '백'이면 -1
				}else {
					if (this.map[j][i] == EStone.Black.ordinal()) point--; // 좌표의 돌이 '흑'이면 +1
					else if(this.map[j][i] == EStone.White.ordinal()) point++; // 좌표의 돌이 '백'이면 -1
				}
			}
		}
		return point;
	}

	public void countStone() {
		int black = 0;
		int white = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.currentS == EStone.Black)	black++;
				else white++;
			}
		}
		System.out.println("흑 : "+black+"개 // 백 : "+white+"개");
	}
	
	
	// 변환 가능한 점을 찾는 함수.
	public void findPossible() {
		System.out.println("---------확인 결과----------");
		int[][] currentMap = this.playBoards.get(this.playtime);
		this.possiblePoints = new Vector<Point>();
		for (int i = 0; i < currentMap.length; i++) {
			for (int j = 0; j < currentMap[i].length; j++) {
				Point p = new Point(j, i);
				if(checkStone(p)) {
					System.out.print("좌표: (" + j + ", " + i + ") / ");
					this.changeStones(this.variableEndPoints(p),p);
					System.out.println("점수 : "+this.countPoints()+"점");
					this.map = copyMap(currentMap);
				}
			}
		}
		System.out.println("놓을 수 있는 위치  : " + this.possiblePoints.size() +"개");
		if (this.possiblePoints.size() == 0) {
			if (checkEnd(currentMap)) {
				this.setCurrentS();
				System.out.println("뒤집을 수 있는 돌이 없어 플레이어를 변경합니다.");
			}else {
				System.out.println("======게임 종료======");
				countStone();
			}
		}
	}
	public Boolean checkEnd(int[][] currentMap) {
		int empty = 0;
		for (int i = 0; i < currentMap.length; i++) {
			for (int j = 0; j < currentMap.length; j++) {
				if(currentMap[j][i] == 2) empty++;
			}
		}
		if (empty == 0) return false;
		else return true;
	}
	public void checkNextNode() {
		for(Point pP : this.getPossiblePoints()) {
			this.putStone(pP);
			System.out.println("==================================");
			System.out.print("====> 플레이 타임: "+ this.playtime+" //");
			System.out.println("확인좌표: (" + pP.x + ", " + pP.y + ")");
			this.drawMap();
			this.findPossible();
			this.setCurrentS();
			this.playtime--;
			this.map = copyMap(this.getPlayBoards().get(this.playtime));
		}
	}

	public void drawMap() {
		int[][] currentMap = this.playBoards.get(this.playtime);
		int numY = 0;
		int numX = 0;
		System.out.print("  ");
		for (@SuppressWarnings("unused") int[] i : currentMap) {
			System.out.print(" " + numX);
			numX++;
		}
		System.out.print("\n");
		for (int i = 0; i < currentMap.length; i++) {
			System.out.print(numY < 10 ? numY + " " : numY);
			numY++;
			for (int j = 0; j < currentMap.length; j++) {
				if (currentMap[j][i] == 0) System.out.print(" ○");
				else if (currentMap[j][i] == 1) System.out.print(" x");
				else System.out.print(" ●");
			}
			System.out.print("\n");
		}
	}
}

package othello;

import othello.Constants.EStone;

public class OMap {
	static final int Empty = 2;
	private int[][] map;
	
	public OMap() {
		this.map = new int[8][8];
	}
	
	public int[][] getMap() {return map;}
	public void setMap(int[][] map) {this.map = map;}

	public void init() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j] = OMap.Empty;
			}
		}
		map[3][4] = EStone.Black.ordinal();
		map[4][3] = EStone.Black.ordinal();
		map[3][3] = EStone.White.ordinal();
		map[4][4] = EStone.White.ordinal();
	}
}

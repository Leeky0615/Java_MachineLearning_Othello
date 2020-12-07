package othello;


public class Constants {
	public enum EStone{Black,White}
//	public enum EState{
//		NW,
//		NN,
//		NE,
//		EE,
//		ES,
//		SS,
//		SW,
//		WW
//	}
	public enum EState{
		NW(-1,-1),
		NN(0,-1),
		NE(1,-1),
		EE(1,0),
		ES(1,1),
		SS(0,1),
		SW(-1,1),
		WW(-1,0);
		private int x;
		private int y;
		private EState(int x,int y) {
			this.x = x;
			this.y = y;
		}
		public int getX() {return this.x;}
		public int getY() {return this.y;}
		
	}
	
}

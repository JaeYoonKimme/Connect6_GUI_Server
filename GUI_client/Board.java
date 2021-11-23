class Board{
	public int[][] board;
	public String redStones;
	public int redStoneCount;
	public int[] lastStone;
	

	Board() {
		board = new int[20][20];
		redStones = new String("");
		redStoneCount = 0;
		lastStone = new int[4] ;
		lastStone[0] = -500;
		lastStone[1] = -500;
	}
	
	public void updateBoard(int x1, int y1, int x2, int y2, int color){
		board[18 - y1][x1] = color;
		board[18-y2][x2] = color;
		lastStone[0] = x1;
		lastStone[1] = y1;
		lastStone[2] = x2;
		lastStone[3] = y2;
	}

	private void deleteRedStone(int x , int y){
		String redString="";
		board[y][x]=0;
		if(x > 7)
			x += 1;
		y = 19 - y;
		char alphabet = (char)(65 + x);
		redString +=String.valueOf(alphabet);
		if(y < 10){
			redString += Integer.toString(0);
		}
		redString +=  Integer.toString(y);

		redStones = redStones.replaceAll(redString + ":", "");
		redStones = redStones.replaceAll(":" + redString, "");
		redStones = redStones.replaceAll(redString, "");
		redStoneCount -=1;

	}



	public boolean redStoneClickEvent(int x, int y){
		if(x > 18 || x < 0 || y > 18 || y < 0){
			return false;
		}
		if(board[y][x] == -1){
			deleteRedStone(x,y);
		}
		else if(redStoneCount < 5 && storeRedStones(x, 18 - y)){
			redStoneCount += 1;
			redStonesString(x, y);
		}
		else{
			return false;
		}
		return  true ;
	}
	private void resetBoard(){
		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 19; j++){
				board[i][j] = 0;
				}
		}
	}
	public void redStoneGenerater(int redStoneCount){
		int x, y, storedX, storedY;
		resetBoard();
		this.redStoneCount = redStoneCount;
		redStones = "";
		for(int i = 0; i < redStoneCount; i++){
			while(true) {
				if(i == 0){
					x = (int)((Math.random() * 6) + 7);
					y = (int)((Math.random() * 5) + 8);
				} else {
					x = (int)((Math.random() * 20));
					y = (int)((Math.random() * 19) + 1);
				}
				if(x != 8) {
					break;
				}
			}
			char alphabet = (char)(65 + x);
			if (x > 8) {
				storedX = x-1;
			}
			else {
				storedX = x;
			}
			storedY = y - 1;
			if(storeRedStones(storedX, storedY)){
				if(i != 0) {
					redStones = redStones + ":";
				}
				redStones = redStones+String.valueOf(alphabet);
				if(y < 10){
					redStones = redStones + Integer.toString(0);
				}
				redStones = redStones + Integer.toString(y);
			}
			else {
				i--;
			}
		}
	}

	private void redStonesString(int x, int y){
		if(x > 7)
			x += 1;
		y = 19 - y;
		char alphabet = (char)(65 + x);
		if(redStoneCount > 1){
			redStones = redStones + ":";
		}
		else{
			redStones = "";
		}
		redStones = redStones+String.valueOf(alphabet);
		if(y < 10){
			redStones = redStones + Integer.toString(0);
		}
		redStones = redStones + Integer.toString(y);

	}

	private boolean storeRedStones(int x, int y){
		if(x == 9 && y == 9){
			return false;
		}
		if(this.board[18 - y][x] != -1){
			board[18 - y][x] = -1;
			return true;
		}
		else{
			return false;
		}
	}
	
}

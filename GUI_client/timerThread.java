class timerThread extends Thread{
	private Gui g;
	public timerThread(Gui g){
		this.g = g;
	}
	@Override public void run(){
		g.startTimer();
	}
}

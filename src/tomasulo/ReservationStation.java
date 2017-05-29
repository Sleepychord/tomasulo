package tomasulo;

abstract public class ReservationStation {
	String name;
	int time;
	boolean isBusy;
	Command c;
	protected ReservationStation(String _n, int _t){
		name = _n;
		time = _t;
		isBusy = false;
	}
	abstract String[] toStringArr();
}

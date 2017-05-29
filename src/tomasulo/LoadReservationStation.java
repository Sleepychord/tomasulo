package tomasulo;

public class LoadReservationStation extends ReservationStation{
	protected LoadReservationStation(String _n) {
		super(_n, -1);
	}
	int addr;
	
	@Override
	String[] toStringArr() {
		String[] ret = new String[4];
		ret[1] = this.name;
		if(isBusy){
			ret[0] = Integer.toString(this.time);
			ret[2] = "Yes";
			ret[3] = Integer.toString(addr);
		}else{
			ret[2] = "No";
			ret[0] = ret[3] = "";
		}
		return ret;
	}
	
}

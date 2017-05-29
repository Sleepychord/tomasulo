package tomasulo;

public class StoreReservationStation extends ReservationStation{
	public int addr;
	public ReservationStation qj;
	public double vj;
	protected StoreReservationStation(String _n) {
		super(_n, 0);
	}
	
	@Override
	String[] toStringArr() {
		String[] ret = new String[6];
		ret[1] = this.name;
		if(isBusy){
			ret[0] = Integer.toString(this.time);
			ret[2] = "Yes";
			ret[3] = Integer.toString(addr);
			if(qj == null){
				ret[4] = Double.toString(vj);
				ret[5] = "";
			}else{
				ret[4] = "";
				ret[5] = qj.name;
			}
		}else{
			ret[2] = "No";
			ret[0] = ret[3] = ret[4] = ret[5] = "";
		}
		return ret;
	}
	
}

package tomasulo;

public class CalcReservationStation extends ReservationStation{
	public double vj, vk;
	public ReservationStation qj, qk;
	public String op;
	public CalcReservationStation(String _n) {
		super(_n, -1);
	}
	@Override
	public String[] toStringArr(){
		String[] ret = new String[8];
		ret[1] = this.name;
		if(isBusy){
			ret[0] = Integer.toString(time);
			ret[2] = "Yes";
			ret[3] = op;
			if(qj == null){
				ret[4] = Double.toString(vj);
				ret[6] = "";
			}else{
				ret[4] = "";
				ret[6] = qj.name;
			}
			if(qk == null){
				ret[5] = Double.toString(vk);
				ret[7] = "";
			}else{
				ret[5] = "";
				ret[7] = qk.name;
			}	
		}else{
			ret[2] = "No";
			ret[0] = ret[3] = ret[4] = ret[5] = ret[6] = ret[7] = "";
		}
		return ret;
	}

}

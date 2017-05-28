package tomasulo;

public class CalcReservationStation extends ReservationStation{
	public double vj, vk;
	public ReservationStation qj, qk;
	public String op;
	public CalcReservationStation(String _n) {
		super(_n, -1);
	}
	public String[] toStringArray(){
		String[] ret = new String[7];
		ret[0] = this.time == -1? "":Integer.toString(time);
		ret[1] = this.name;
		ret[2] = isBusy?"Yes":"No";
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
			ret[7] = qj.name;
		}	
		return ret;
	}
}

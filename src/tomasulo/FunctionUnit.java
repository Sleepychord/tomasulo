package tomasulo;

public class FunctionUnit {
	public String name;
	ReservationStation r;
	double v;
	public String getStringValue(){
		if(r == null) return Double.toString(v);
		else return r.name;
	}
	public FunctionUnit(String _n){
		name = _n;
	}
}

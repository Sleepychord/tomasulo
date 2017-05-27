package tomasulo;

public class CalcReservationStation extends ReservationStation{
	public double vj, vk;
	public ReservationStation qj, qk;
	public String op;
	public CalcReservationStation(String _n) {
		super(_n, -1);
	}
}

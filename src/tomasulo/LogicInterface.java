package tomasulo;

import java.util.ArrayList;

public interface LogicInterface {
	public FunctionUnit[] getFunctionUnits();
	public int getClock();
	public ReservationStation[] getReservationStations(String op);
	public ArrayList<Command> getCommands();//directly modified the object
	public int[] getRegisters();//directly modified the object
	public double[] getMemory();//directly modified the object
	public void clear();
	public boolean runCycle();
	public int getPc();
}

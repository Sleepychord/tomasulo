package tomasulo;

import java.util.ArrayList;

public class Logic implements LogicInterface {
	int pc;
	int clk;
	public ArrayList<Command> commands;
	public int[] ru;
	public double[] mem;
	public 
	FunctionUnit[] fu;
	CalcReservationStation[] radd;
	CalcReservationStation[] rmul;
	LoadReservationStation[] rload;
	StoreReservationStation[] rstore;
	public Logic(){
		this(11, 3, 2, 3, 3, 11, 4096);
	}
	public Logic(int nf, int nadd, int nmul, int nload, int nstore, int nru, int nmem){
		commands = new ArrayList<Command>();
		fu = new FunctionUnit[nf];
		for(int i = 0;i < nf;i++)
			fu[i] = new FunctionUnit("F"+Integer.toString(i));
		radd = new CalcReservationStation[nadd];
		for(int i = 0;i < nadd;i++)
			radd[i] = new CalcReservationStation("ADD" + Integer.toString(i));
		rmul = new CalcReservationStation[nmul];
		for(int i = 0;i < nmul;i++)
			rmul[i] = new CalcReservationStation("MULT" + Integer.toString(i));
		rload = new LoadReservationStation[nload];
		for(int i = 0;i < nload;i++)
			rload[i] = new LoadReservationStation("LOAD" + Integer.toString(i));
		rstore = new StoreReservationStation[nstore];
		for(int i = 0;i < nstore;i++)
			rstore[i] = new StoreReservationStation("STORE" + Integer.toString(i));
		ru = new int[nru];
		mem = new double[nmem];
		pc = 0;
		clk = 0;
	}
	@Override
	public FunctionUnit[] getFunctionUnits() {
		return fu.clone();
	}

	@Override
	public int getClock() {
		return clk;
	}

	@Override
	public ReservationStation[] getReservationStations(String op) {
		if(op.equals("ADD")){
			return radd;
		}else if(op.equals("MULT")){
			return rmul;
		}else if(op.equals("LOAD")){
			return rload;
		}else if(op.equals("STORE")){
			return rstore;
		}
		return null;
	}

	@Override
	public ArrayList<Command> getCommands() {
		return commands;
	}


	@Override
	public void clear() {
		pc = 0;
		clk = 0;
		for(Command c: commands){
			c.issue = c.comp = c.result = -1;
		}
		for(FunctionUnit r: fu){
			r.r = null;
			r.v = 0;
		}
		for(ReservationStation r: radd){
			r.isBusy = false;
		}
		for(ReservationStation r: rmul){
			r.isBusy = false;
		}
		for(ReservationStation r: rload){
			r.isBusy = false;
		}
		for(ReservationStation r: rstore){
			r.isBusy = false;
		}
	}

	private  boolean countDown(ReservationStation[] rs){
		boolean ret = false;
		for(ReservationStation r: rs){
			if(r.isBusy && r.time >= 0){
				ret = true;
				r.time--;
				if(r.time == 0){//finish a command
					r.c.comp = clk + 1;
				}
				if(r.time < 0){//write result
					r.c.result = clk + 1;
					r.isBusy = false;
					if(r instanceof CalcReservationStation){
						CalcReservationStation r1 = (CalcReservationStation)r;
						if(r1.op.equals("ADDD"))
							r1.c.value = r1.vj + r1.vk;
						else if(r1.op.equals("SUBD"))
							r1.c.value = r1.vj - r1.vk;
						else if(r1.op.equals("MULD"))
							r1.c.value = r1.vj * r1.vk;
						else if(r1.op.equals("DIVD"))
							r1.c.value = r1.vj / r1.vk;
					}else if(r instanceof LoadReservationStation){
						LoadReservationStation r1 = (LoadReservationStation)r;
						r1.c.value = mem[r1.addr];
					}else if(r instanceof StoreReservationStation){
						StoreReservationStation r1 = (StoreReservationStation)r;
						mem[r1.addr] = r1.vj;
					}else System.out.println("Error! Unsupported ReservationStation Type");
				}
			}
		}
		return ret;
	}
	@Override
	public boolean runCycle() {
		boolean ret = false;
		//count down
		ret |= countDown(radd) || countDown(rmul) || countDown(rload) || countDown(rstore);
		//check waiting
		for(CalcReservationStation r: radd)
			if(r.isBusy && r.time < 0)	//waiting 
			{
				if(r.qj != null && !r.qj.isBusy){
					r.vj = r.qj.c.value;
					r.qj = null;
					ret = true;
				}
				if(r.qk != null && !r.qk.isBusy){
					r.vk = r.qk.c.value;
					r.qk = null;
					ret = true;
				}
				if(r.qj == null && r.qk == null){
					r.time = 2;//add & sub : 2 cycle
				}
			}
		for(CalcReservationStation r: rmul)
			if(r.isBusy && r.time < 0)	//waiting 
			{
				if(r.qj != null && !r.qj.isBusy){
					r.vj = r.qj.c.value;
					r.qj = null;
					ret = true;
				}
				if(r.qk != null && !r.qk.isBusy){
					r.vk = r.qk.c.value;
					r.qk = null;
					ret = true;
				}
				if(r.qj == null && r.qk == null){
					if(r.c.op.equals("MULD")) r.time = 10;
					else if(r.c.op.equals("DIVD")) r.time = 40;
					else System.out.println("Error! Undefined Operator.");
				}
			}		
		for(StoreReservationStation r: rstore)
			if(r.isBusy && r.time < 0)	//waiting 
			{
				if(r.qj != null && !r.qj.isBusy){
					r.vj = r.qj.c.value;
					r.qj = null;
					ret = true;
					r.time = 2;
				}
			}
		for(FunctionUnit u: fu){
			if(u.r != null && !u.r.isBusy){
				u.v = u.r.c.value;
				u.r = null;
				ret = true;
			}
		}
		
		//issue
		if(pc < commands.size()){
			Command c = commands.get(pc);
			if(c.op.equals("ADDD") || c.op.equals("SUBD") || c.op.equals("MULD") || c.op.equals("DIVD")){
				CalcReservationStation[] rs = radd;
				if(c.op.equals("MULD") || c.op.equals("DIVD")) rs = rmul;
				for(CalcReservationStation r: rs)
					if(!r.isBusy){//find an idle reservation station
						ret = true;
						pc++;//successfully issued
						r.op = c.op;
						r.c = c;
						r.isBusy = true;
						if(!(c.arg[0].matches("F\\d+") && c.arg[1].matches("F\\d+") && c.arg[2].matches("F\\d+")))
							System.out.println("Error! Wrong Args.");
						// arg 1
						FunctionUnit tmp = fu[Integer.parseInt(c.arg[1].substring(1, c.arg[1].length()))];
						if(tmp.r == null){
							r.qj = null;
							r.vj = tmp.v;
						}else{
							r.qj = tmp.r;
							r.vj = 0;
						}
						// arg 2
						tmp = fu[Integer.parseInt(c.arg[2].substring(1, c.arg[2].length()))];
						if(tmp.r == null){
							r.qk = null;
							r.vk = tmp.v;
						}else{
							r.qk = tmp.r;
							r.vk = 0;
						}
						//can begin?
						if(r.qj == null && r.qk == null){
							if(c.op.equals("ADDD") || c.op.equals("SUBD")) r.time = 2;
							else if(c.op.equals("MULD")) r.time = 10;
							else if(c.op.equals("DIVD")) r.time = 40;
						}else r.time = -1;
						// target
						tmp = fu[Integer.parseInt(c.arg[0].substring(1, c.arg[0].length()))];
						tmp.r = r;
						break;
					}
			}else if(c.op.equals("ST")){
				for(StoreReservationStation r: rstore){
					if(!r.isBusy){//find an idle reservation station
						ret = true;
						pc++;//successfully issued
						r.c = c;
						r.isBusy = true;
						if(!(c.arg[0].matches("F\\d+") && c.arg[1].matches("\\d+") && c.arg[2].matches("R\\d+")))
							System.out.println("Error! Wrong Args.");
						r.addr = ru[Integer.parseInt(c.arg[2].substring(1, c.arg[2].length()))] + Integer.parseInt(c.arg[1]);
						FunctionUnit tmp = fu[Integer.parseInt(c.arg[0].substring(1, c.arg[0].length()))];
						if(tmp.r == null){
							r.qj = null;
							r.vj = tmp.v;
							r.time = -1;
						}else{
							r.qj = tmp.r;
							r.vj = 0;
							r.time = 2;
						}
						break;
					}
				}
			}else if(c.op.equals("LD")){
				for(LoadReservationStation r:rload)
					if(!r.isBusy)
					{
						ret = true;
						pc++;//successfully issued
						r.c = c;
						r.isBusy = true;
						if(!(c.arg[0].matches("F\\d+") && c.arg[1].matches("\\d+") && c.arg[2].matches("R\\d+")))
							System.out.println("Error! Wrong Args.");
						r.time = 2;
						r.addr = ru[Integer.parseInt(c.arg[2].substring(1, c.arg[2].length()))] + Integer.parseInt(c.arg[1]);
						FunctionUnit tmp = fu[Integer.parseInt(c.arg[0].substring(1, c.arg[0].length()))];
						tmp.r = r;
						break;
					}
			}else System.out.println("Error! issuing undefined Operator.");
		}
		if(ret) clk++;
		return ret;
	}

	@Override
	public int getPc() {
		return pc;
	}

	@Override
	public int[] getRegisters() {
		return ru;
	}
	@Override
	public double[] getMemory() {
		return mem;
	}

	public static void main(String[] args) {

	}

}

package tomasulo;

public class Command {
	//ADDD SUBD MULD DIVD LD ST
	String op;
	String arg[];
	int issue, comp, result;
	double value;
	Command(String _op, String[] _arg){
		op = _op;
		arg = _arg;
		issue = comp = result = -1;
	}
	String[] toStringArray(){
		return new String[]{op, arg[0], arg[1], arg[2], Integer.toString(issue), Integer.toString(comp), Integer.toString(result)};
	}
}
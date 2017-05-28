package tomasulo;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.*; 

public class UserWindow {
	JFrame mainframe;
	JToolBar toolbar;
	JPanel panetop,panecenter;
	JTable Instr,state,loadq,stroeq,station,funit,runit,mem;
	JLabel clock;
	//resources
	LogicInterface logic;
	ArrayList<Command> cs;
	double[] memory;
	int[] registers;
	int startAddr;//displayed start addr
	UniversalActionListener listener;
	
	public void runOneCycle(){
		//Logic.runCycle & updateTable
	}
	public void loadCommands(){
		//Helper.readCommandsFromFileByDialog & updateTable
	}
	public void reset(){
		//Logic.clear() & updateTable, clock, pc...
	}
	public void openEditableMode(){
		//disable some buttons
	}
	public void closeEditableMode(){
		//need to writeTable() & restore buttons
	}
	public void writeTable(){
		//set values in Registers, memory and commands tables to logic
	}
	public void updateTable(){
		//get all data from logic & set them in table, update
	}
	public void displayMemory(int addr){
		//update table
	}
	public UserWindow(){
		logic = new Logic();
		memory = logic.getMemory();
		registers = logic.getRegisters();
		cs = logic.getCommands();
		
		mainframe = new JFrame();
		mainframe.setTitle("Tomasulo");
		mainframe.setSize(600, 400);
		
		toolbar = new JToolBar();
		JPanel panetop= new JPanel();
		mainframe.getContentPane().add("North", panetop);
		panetop.setLayout(new FlowLayout(FlowLayout.LEFT));
		panetop.add(toolbar);
		JButton filebtn = new JButton();
		filebtn.setText("open a file");
		toolbar.add(filebtn);
		JButton inputbtn = new JButton();
		inputbtn.setText("input Instruction");
		toolbar.add(inputbtn);
		JButton databtn = new JButton();
		databtn.setText("input data");
		toolbar.add(databtn);
		JButton runbtn = new JButton();
		runbtn.setText("run");
		toolbar.add(runbtn);
		
		panecenter= new JPanel();
		mainframe.getContentPane().add("Center", panecenter);
		mainframe.setVisible(true);
	}
	
	public static void main(String args[]){
		new UserWindow();
	}
    
    
}

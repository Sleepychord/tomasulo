package tomasulo;

import java.awt.FlowLayout;
import java.io.IOException;
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
		logic.runCycle();
		updateTable();
	}
	public void loadCommands(){
		//Helper.readCommandsFromFileByDialog & updateTable
		try {
			Helper.readCommandsFromFileByDialog(cs, this.mainframe.getContentPane());
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateTable();
	}
	public void reset(){
		//Logic.clear() & updateTable, clock...
		logic.clear();
		updateTable();
		clock.setText("0");
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
		
		listener = new UniversalActionListener(this);
		
		toolbar = new JToolBar();
		JPanel panetop= new JPanel();
		mainframe.getContentPane().add("North", panetop);
		panetop.setLayout(new FlowLayout(FlowLayout.LEFT));
		panetop.add(toolbar);
		JButton filebtn = new JButton();
		filebtn.setText("open a file");
		toolbar.add(filebtn);
		JButton inputbtn = new JButton();
		inputbtn.setText("Edit");
		toolbar.add(inputbtn);
		JButton autobtn = new JButton();
		autobtn.setText("Auto");
		toolbar.add(autobtn);
		JButton stopbtn = new JButton();
		stopbtn.setText("Stop");
		toolbar.add(stopbtn);
		JButton runbtn = new JButton();
		runbtn.setText("run");
		toolbar.add(runbtn);
		JButton resetbtn = new JButton();
		resetbtn.setText("Reset");
		toolbar.add(resetbtn);
		filebtn.setActionCommand("loadCommands");
		inputbtn.setActionCommand("openEditableMode");
		autobtn.setActionCommand("autoRun");
		stopbtn.setActionCommand("stopAutoRun");
		runbtn.setActionCommand("runOneCycle");
		resetbtn.setActionCommand("reset");
		
		filebtn.addActionListener(listener);
		inputbtn.addActionListener(listener);
		autobtn.addActionListener(listener);
		stopbtn.addActionListener(listener);
		runbtn.addActionListener(listener);
		resetbtn.addActionListener(listener);
		panecenter= new JPanel();
		mainframe.getContentPane().add("Center", panecenter);
		
		clock = new JLabel("0");
		panecenter.add(clock);
		
		mainframe.setVisible(true);
	}
	
	public static void main(String args[]){
		new UserWindow();
	}
    
    
}

package tomasulo;

import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserWindow {
	JFrame mainframe;
	JToolBar toolbar;
	JPanel panetop,panecenter;
	JTable instr,loadq,storeq,station,funit,runit,mem;
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
	}
	public void openEditableMode(){
		//disable some buttons
	}
	public void closeEditableMode(){
		//need to writeTable() & restore buttons
	}
	public void writeTable(){
		//set values in Registers, memory and commands tables to logic
		DefaultTableModel model = (DefaultTableModel) (instr.getModel());
		for(int i = 0;i < model.getRowCount();i++)
		{
			String op = (String)model.getValueAt(i, 0);
			String[] arg = {(String) model.getValueAt(i, 1),
					(String) model.getValueAt(i, 2),
					(String) model.getValueAt(i, 3)};
			if(cs.size() <= i)
				cs.add(new Command(op, arg));
			else{
				if(cs.get(i).op != op) cs.get(i).op = op;
				for(int j = 0;j < arg.length;j++)
					if(cs.get(i).arg[j] != arg[j])
						cs.get(i).arg[j] = arg[j];
			}
		}
		//TODO others
	}
	public void updateTable(){
		//get all data from logic & set them in table, update
		
		//-------instr table
		for(int i = 0;i < cs.size();i++)
		{
//			 System.out.println(instr.getModel().getClass().getName());
			 DefaultTableModel model = (DefaultTableModel) (instr.getModel());
			 String [] data = cs.get(i).toStringArray();
			 if(model.getRowCount() <= i)//if cs is more than row count
				 model.addRow(data);
			 else{
				 for(int j = 0;j < data.length;j++){
					 model.setValueAt(data[j], i, j);
//					 System.out.println(model.getValueAt(i, j));
				 }
			 }				 
		}
		//-------TODO others
		for(int i=0;i<)
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
		mainframe.setSize(1000, 800);
		
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
		//construct tables
		//------command table
		final Object[] commandColumnNames = 
			{"Operation", "Arg1", "Arg2", "Arg3", "issue", "exec comp", "write result"};
		DefaultTableModel instr_dtm = new DefaultTableModel(new Object[][]{}, commandColumnNames);
		instr = new JTable(instr_dtm);
		instr.getTableHeader().setVisible(true);
		panecenter.add(new JScrollPane(instr));//or the header will not display!
		//-------TODO others
		//------load/store table
		final Object[] loadAndStoreColumnNames = 
			{"Name", "Busy", "Addr"};
		Object[][] loadData = {{"Load1","No",""},{"Load2","No",""},{"Load3","No",""}};
		loadq = new JTable(loadData,loadAndStoreColumnNames);
		loadq.getTableHeader().setVisible(true);
		panecenter.add(loadq);
		
		Object[][] storeData = {{"Store1","No",""},{"Store2","No",""},{"Store3","No",""}};
		storeq = new JTable(storeData,loadAndStoreColumnNames);
		storeq.getTableHeader().setVisible(true);
		panecenter.add(storeq);
		//------stations table
		final Object[] stationColumnNames = 
			{"Time", "Name", "Busy", "Op", "Vj", "Vk", "Qj", "Qk"};
		Object[][] statData = {
				{"","Add1","No","","","","",""},
				{"","Add2","No","","","","",""},
				{"","Add3","No","","","","",""},
				{"","Mult1","No","","","","",""},
				{"","Mult2","No","","","","",""}};
		station = new JTable(statData,stationColumnNames);
		station.getTableHeader().setVisible(true);
		panecenter.add(station);
		//------funit/runit table
		final Object[] fuColumnNames = {"F0","F1","F2","F3","F4","F5","F6","F7","F8",
				"F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20",
				"F21","F22","F23","F24","F25","F26","F27","F28","F29","F30"};
		Object[][] fuData = new Object[][]{};
		funit = new JTable(fuData,fuColumnNames);
		funit.getTableHeader().setVisible(true);
		panecenter.add(funit);
		
		final Object[] ruColumnNames = {"R0","R1","R2","R3","R4","R5","R6","R7","R8",
				"R9","R10","R11","R12","R13","R14","R15","R16","R17","R18","R19","R20",
				"R21","R22","R23","R24","R25","R26","R27","R28","R29","R30"};
		Object[][] ruData = new Object[][]{};
		runit = new JTable(ruData,ruColumnNames);
		runit.getTableHeader().setVisible(true);
		panecenter.add(runit);
		//------mem table
		mem = new JTable(2,6);
		mem.setVisible(true);
		panecenter.add(mem);
		
		updateTable();
		panecenter.setVisible(true);
		mainframe.setVisible(true);
	}
	
	public static void main(String args[]){
		new UserWindow();
	}
    
    
}

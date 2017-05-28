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
		//construct tables
		//------command table
		final Object[] commandColumnNames = 
			{"Operation", "Arg1", "Arg2", "Arg3", "issue", "exec comp", "write result"};
		DefaultTableModel instr_dtm = new DefaultTableModel(new Object[][]{}, commandColumnNames);
		instr = new JTable(instr_dtm);
		instr.getTableHeader().setVisible(true);
		panecenter.add(new JScrollPane(instr));//or the header will not display!
		//-------TODO others
		updateTable();
		panecenter.setVisible(true);
		mainframe.setVisible(true);
	}
	
	public static void main(String args[]){
		new UserWindow();
	}
    
    
}

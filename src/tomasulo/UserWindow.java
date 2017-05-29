package tomasulo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class UserWindow {
	final int DisplayMemNum = 6;
	static final int MaxMemory = 4096;
	JFrame mainframe;
	JToolBar toolbar;
	JPanel panetop,panecenter;
	JTable instr,loadq,storeq,station,funit,runit,mem;
	JLabel clock;
	JTextField gotoAddr;
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
		instr.setEnabled(true);
		runit.setEnabled(true);
		mem.setEnabled(true);
		//TODO buttons
	}
	public void closeEditableMode(){
		//need to writeTable() & restore buttons
		instr.clearSelection();
		runit.clearSelection();
		mem.clearSelection();
		if(mem.isEditing())
			mem.getCellEditor().stopCellEditing();
		if(instr.isEditing())
			instr.getCellEditor().stopCellEditing();
		if(runit.isEditing())
			runit.getCellEditor().stopCellEditing();
		writeTable();
		instr.setEnabled(false);
		runit.setEnabled(false);
		mem.setEnabled(false);
		//TODO buttons
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
		//-------memory
		for(int i = 0;i < DisplayMemNum;i++)
			if (startAddr + i < MaxMemory)	
			{
				if(mem.getValueAt(1, i) instanceof Double)
					memory[startAddr + i] = (Double)mem.getValueAt(1, i);
				else
					memory[startAddr + i] = Double.parseDouble((String) mem.getValueAt(1, i));
//				System.out.printf("%d %.2f %s\n", startAddr + i, memory[startAddr + i], (String) mem.getValueAt(1, i));
			}
		//-------registers
		for(int i = 0;i < registers.length;i++){
			//TODO check
			registers[i] = Integer.parseInt((String) runit.getValueAt(0, i));
		}
		
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
			 for(int j = 0;j < data.length;j++)
				 if(model.getValueAt(i, j).equals("-1"))
				 model.setValueAt("", i, j);
		}
		//load queue table
		LoadReservationStation[] rs = (LoadReservationStation[])logic.getReservationStations("LOAD");
		for(int i = 0;i < rs.length;i++)
		{
			String[] data = rs[i].toStringArr();
 			for(int j = 0;j < data.length;j++)
 				loadq.setValueAt(data[j], i, j);
		}
		//store queue table
		StoreReservationStation[] store_rs = (StoreReservationStation[])logic.getReservationStations("STORE");
		for(int i = 0;i < store_rs.length;i++)
		{
			String[] data = store_rs[i].toStringArr();
 			for(int j = 0;j < data.length;j++)
 				storeq.setValueAt(data[j], i, j);
		}
		//calc table
		CalcReservationStation[] add_rs = (CalcReservationStation[])logic.getReservationStations("ADD");
		for(int i = 0;i < add_rs.length;i++)
		{
			String[] data = add_rs[i].toStringArr();
			for(int j = 0;j < data.length;j++)
			station.setValueAt(data[j], i, j);
		}
		CalcReservationStation[] mul_rs = (CalcReservationStation[])logic.getReservationStations("MULT");
		for(int i = 0;i < mul_rs.length;i++)
		{
			String[] data = mul_rs[i].toStringArr();
			for(int j = 0;j < data.length;j++)
			station.setValueAt(data[j], i + add_rs.length, j);
		}
		//Function Units table
		FunctionUnit[] fus = logic.getFunctionUnits();
		for(int i = 0;i < fus.length;i++)
			funit.setValueAt(fus[i].getStringValue(), 0, i);
		//Memory table
		for(int i = 0;i < DisplayMemNum;i++)
		if (startAddr + i < MaxMemory)	
		{
			mem.setValueAt(startAddr + i, 0, i);
			mem.setValueAt(Double.toString(memory[startAddr + i]), 1, i);
		}else{
			mem.setValueAt("", 0, i);
			mem.setValueAt("", 1, i);
		}
	}
	public void displayMemory(int addr){
		//update table
		startAddr = addr;
		updateTable();
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
		JButton close_edit_btn = new JButton();
		close_edit_btn.setText("Ok");
		toolbar.add(close_edit_btn);
		
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
		close_edit_btn.setActionCommand("closeEditableMode");
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
		close_edit_btn.addActionListener(listener);
		panecenter= new JPanel();
		mainframe.getContentPane().add("Center", panecenter);
		
		clock = new JLabel("0");
		panecenter.add(clock);
		//construct tables
		//------command table
		final Object[] commandColumnNames = 
			{"Operation", "Arg1", "Arg2", "Arg3", "issue", "exec comp", "write result"};
		DefaultTableModel instr_dtm = new DefaultTableModel(new Object[][]{}, commandColumnNames){
			@Override
			public boolean isCellEditable(int row,int column){  
				return (column <= 3);
			}
		};
		instr = new JTable(instr_dtm);
		instr.getTableHeader().setVisible(true);
		JScrollPane instrPanel = new JScrollPane(instr);
		instrPanel.setPreferredSize(new Dimension(400, 200));
		panecenter.add(instrPanel);//or the header will not display!
		//------load/store table
		final Object[] loadColumnNames = 
			{"Time", "Name", "Busy", "Addr"};
		Object[][] loadData = {{"","Load1","No",""},{"","Load2","No",""},{"","Load3","No",""}};
		loadq = new JTable(loadData,loadColumnNames);
		loadq.getTableHeader().setVisible(true);
		JPanel loadPanel = new JPanel(new BorderLayout());
		loadPanel.add("North", new JLabel("Load Queue"));
		loadPanel.add("South", loadq);
		loadPanel.add("Center", loadq.getTableHeader());
		panecenter.add(loadPanel);
		
		final Object[] StoreColumnNames = 
			{"Time", "Name", "Busy", "Addr", "Vj", "Qj"};
		Object[][] storeData = {{"","Store1","No","","",""},{"","Store2","No","","",""},{"","Store3","No","","",""}};
		storeq = new JTable(storeData,StoreColumnNames);
		storeq.getTableHeader().setVisible(true);
		JPanel storePanel = new JPanel(new BorderLayout());
		storePanel.add("North", new JLabel("Store Queue"));
		storePanel.add("Center", storeq.getTableHeader());
		storePanel.add("South", storeq);
		panecenter.add(storePanel);
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
		JPanel stationPanel = new JPanel(new BorderLayout());
		stationPanel.add("North", new JLabel("Reservation Station"));
		stationPanel.add("Center", station.getTableHeader());
		stationPanel.add("South", station);
		panecenter.add(stationPanel);
		//------funit/runit table
		String[] fuColumnNames = new String[11];
		String[] fuData1 = new String[11];
		for(int i = 0;i < 11;i++){
			fuColumnNames[i] = "F" + Integer.toString(i);
			fuData1[i] = "";
		}
		Object[][] fuData = new Object[][]{fuData1};
		funit = new JTable(fuData,fuColumnNames);
		funit.getTableHeader().setVisible(true);
		JPanel funitPanel = new JPanel(new BorderLayout());
		funitPanel.add("North", new JLabel("Function Units"));
		funitPanel.add("Center", funit.getTableHeader());
		funitPanel.add("South", funit);
		panecenter.add(funitPanel);
		
		String[] ruColumnNames = new String[11];
		String[] ruData1 = new String[11];
		for(int i = 0;i < 11;i++){
			ruColumnNames[i] = "R" + Integer.toString(i);
			ruData1[i] = "0";
		}
		Object[][] ruData = new Object[][]{ruData1};
		runit = new JTable(ruData,ruColumnNames);
		runit.getTableHeader().setVisible(true);
		JPanel runitPanel = new JPanel(new BorderLayout());
		runitPanel.add("North", new JLabel("Registers"));
		runitPanel.add("Center", runit.getTableHeader());
		runitPanel.add("South", runit);
		panecenter.add(runitPanel);		//------mem table
		//memory
		mem = new JTable(2,DisplayMemNum);
		mem.setVisible(true);
		JPanel memPanel = new JPanel(new BorderLayout());
		memPanel.add("North", new JLabel("Memory"));
		memPanel.add("West", new JLabel("Input Address"));
		gotoAddr = new JTextField();
		memPanel.add("Center", gotoAddr);
		JButton gotoButton =  new JButton("Goto");
		gotoButton.setActionCommand("display");
		gotoButton.addActionListener(listener);
		memPanel.add("East", gotoButton);
		memPanel.add("South", mem);
		panecenter.add(memPanel);
		
		updateTable();
		closeEditableMode();
		panecenter.setVisible(true);
		mainframe.setVisible(true);
	}
	
	public static void main(String args[]){
		new UserWindow();
	}
    
    
}

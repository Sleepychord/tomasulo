package tomasulo;

import java.awt.FlowLayout;

import javax.swing.*; 

public class UserWindow {
	JFrame mainframe;
	JToolBar toolbar;
	JPanel panetop,panecenter;
	JTable Instr,state,loadq,stroeq,station,funit,runit,mem;
	JLabel clock;
	
	public void writeTable(){
		
	}
	public void reTable(){
		
	}
	public UserWindow(){
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

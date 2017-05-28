package tomasulo;

import java.awt.FlowLayout;

import javax.swing.*; 

public class UserWindow {
	public static void main(){
		JFrame frame = new JFrame();
		frame.setTitle("TomasuloËã·¨Ä£ÄâÆ÷");
		frame.setSize(600, 400);
		frame.setVisible(true);
		
		JToolBar toolBar = new JToolBar();
		JPanel pane0 = new JPanel();
		frame.getContentPane().add("North", pane0);
		pane0.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane0.add(toolBar);
		JButton filebtn = new JButton();
		filebtn.setText("open a file");
		toolBar.add(filebtn);
		JButton inputbtn = new JButton();
		inputbtn.setText("input Instruction");
		toolBar.add(inputbtn);
		JButton databtn = new JButton();
		databtn.setText("input data");
		toolBar.add(databtn);
		JButton runbtn = new JButton();
		runbtn.setText("run");
		toolBar.add(runbtn);
		
	}
    
    
}

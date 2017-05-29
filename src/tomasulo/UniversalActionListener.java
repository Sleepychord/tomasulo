package tomasulo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UniversalActionListener implements ActionListener {
	UserWindow w;
	public UniversalActionListener(UserWindow win) {
		w = win;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "loadCommands":
			w.loadCommands();
			break;
		case "openEditableMode":
			w.openEditableMode();
			break;
		case "closeEditableMode":
			w.closeEditableMode();
			break;
		case "autoRun":
			w.autoRun();
			break;
		case "stopAutoRun":
			w.stopAutoRun();
			break;
		case "runOneCycle":
			w.runOneCycle();
			break;
		case "reset":
			w.reset();
			break;
		case "display":
			String val = w.gotoAddr.getText();
			if(val.matches("\\d+") && Integer.parseInt(val) < w.MaxMemory)
				w.displayMemory(Integer.parseInt(val));
			else System.out.println("Segment Fault");
			break;
		default:
			System.out.println("Undefined Action.");
			break;
		}
	}

}

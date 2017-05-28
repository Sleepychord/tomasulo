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
		case "autoRun":
			break;
		case "stopAutoRun":
			break;
		case "runOneCycle":
			w.runOneCycle();
			break;
		case "reset":
			w.reset();
			break;
		default:
			System.out.println("Undefined Action.");
			break;
		}
	}

}

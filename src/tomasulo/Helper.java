package tomasulo;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Helper {
	static void readCommandsFromFile(String fileName, ArrayList<Command> cs) throws IOException{
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            String[] line = tempString.split("\\s+");
            if(line.length == 4)
            	cs.add(new Command(line[0], new String[]{line[1], line[2], line[3]}));
            else System.out.println("overlook : " + tempString);
        }
        reader.close();
	}
	static void readCommandsFromFileByDialog(ArrayList<Command> cs, Container parent) throws IOException{
		JFileChooser fChooser = new JFileChooser();
		if(fChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
			Helper.readCommandsFromFile(fChooser.getSelectedFile().getPath(), cs);
		}
	}
}

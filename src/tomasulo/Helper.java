package tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Helper {
	static void readCommandsFromFile(String fileName, ArrayList<Command> cs) throws IOException{
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            String[] line = tempString.split("\\s+");
            cs.add(new Command(line[0], new String[]{line[1], line[2], line[3]}));
        }
        reader.close();
	}
}

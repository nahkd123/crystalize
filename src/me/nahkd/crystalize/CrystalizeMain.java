package me.nahkd.crystalize;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import me.nahkd.crystalize.core.CrystalizeCore;
import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class CrystalizeMain {

	public static void main(String[] args) {
		if (args.length == 0) {
			// Enter the REPL mode
			System.out.println("");
			System.out.println("    Crystalize Java (unreleased)");
			System.out.println("    REPL Mode");
			System.out.println("");
		} else {
			// Load file
			try {
				List<String> lines = Files.readAllLines(new File("").toPath(), Charset.forName("UTF-8"));
				String file = "";
				for (String line : lines) file += line + "\n";
				
				ObjectsCompound instance = new ObjectsCompound();
				CrystalizeStatement[] compiled = CrystalizeCore.compile(file, instance);
				CrystalizeCore.eval(instance, compiled);
			} catch (IOException e) {
				System.err.println("<REPL Message> Unable to read file: IOException");
				e.printStackTrace();
			}
		}
	}

}

package org.gusmp.titleMp3OverWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Create jar with: mvn clean compile assembly:single
 *
 */
public class TitleMp3OverWriter {

	private static void printBanner() {

		System.out.println("TileMp3Overrider");
		System.out.println("===============");
		//System.out.println("TileMp3Overrider uses mp3agic by Michael Patricios");
		System.out.println("TileMp3Overrider uses MyID3 by Charles M. Chen");
		System.out.println("See http://www.fightingquaker.com/myid3/");
	}

	private static String getInput(String label) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(label);
		return br.readLine();
	}

	public static void main(String[] args) {

		printBanner();

		String folderToScan = "";
		String targetFolder = "";

		if (args.length >= 2) {
			folderToScan = args[0];
			targetFolder = args[1];
		} else if (args.length == 1) {

			folderToScan = args[0];

			try {
				targetFolder = getInput("Select target folder:");
			} catch (IOException exc) {
				System.out.println("Error reading target folder. Exiting");
				return;
			}
		} else {

			try {
				folderToScan = getInput("Select folder:");
			} catch (IOException exc) {
				System.out.println("Error reading forder to scan. Exiting");
				return;
			}

			try {
				targetFolder = getInput("Select target folder:");
			} catch (IOException exc) {
				System.out.println("Error reading target folder. Exiting");
				return;
			}
		}

		File folderToScanFile = new File(folderToScan);
		if ((!folderToScanFile.isDirectory()) && (!folderToScanFile.isFile())) {
			System.out.println(folderToScan + " is not a file nor a folder. Nothing to do!");
			return;
		}

		if (!(new File(targetFolder).isDirectory())) {
			System.out.println(targetFolder + " is not a folder. Nothing to do!");
			return;
		}

		// new
		// Mp3TagsMp3AgicOverWriter().init(targetFolderFile,"c:/soft/tmp/bbc2/2017");
		// JavaMusicTag does not work properly
		// new
		// Mp3TagsJavaMusicTagOverWriter().init(targetFolderFile,"c:/soft/tmp/bbc2/2017");
		// OK
		Mp3TagsMyID3OverWriter converter = new Mp3TagsMyID3OverWriter();
		converter.init(folderToScanFile, targetFolder);

		System.out.println("Total files converted: " + converter.getTotalFilesConverted());
		System.out.println("=====");
	}
}

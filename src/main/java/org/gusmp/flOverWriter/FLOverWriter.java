package org.gusmp.flOverWriter;

import java.io.File;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

public class FLOverWriter {

	public static void main(String[] args) {

		String folderToScan = args[0];
		String targetFolder = args[1];
		int total =0;

		for (File f : new File(folderToScan).listFiles()) {
			if (!f.getName().endsWith("mp3"))
				continue;

			try {
				MusicMetadataSet src_set = new MyID3().read(f);
				IMusicMetadata metadata = src_set.getSimplified();
				
				metadata.setSongTitle(f.getName().split("_", 2)[1].replace(".mp3", ""));
				metadata.setArtist("Frances Larrouse");
				metadata.setAlbum("Curso de Frances Disc 3");
				metadata.setBand("Frances Larrouse");
				metadata.setYear(2017);
				
				File fout = new File(targetFolder, f.getName().replace(".mp3", "_overwritten.mp3"));

				new MyID3().write(f, fout, src_set, metadata);
				total++;
				
			} catch (Exception exc) {
				System.out.println("Error: " + exc.toString());
			}
		}
		
		System.out.println("Total: " + total);

	}

}

package org.gusmp.titleMp3OverWriter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.*;

public class Mp3TagsMyID3OverWriter {

	private class MetaData {
		public String year;
		public String artist;
		public String title;
		public String album;
	}
	
	private int totalFilesConverted;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

	public int getTotalFilesConverted() {
		return totalFilesConverted;
	}
	
	public void init(File folderToScan, String targetOutput) {

		totalFilesConverted = 0;
		if (folderToScan.isFile()) {
			overWriteMp3TagsV2(folderToScan, targetOutput);
		} else if (folderToScan.isDirectory()) {
			findMp3Files(folderToScan.listFiles(), targetOutput);
		}

	}

	private void findMp3Files(File[] diretoryFiles, String targetOutput) {

		for (File f : diretoryFiles) {
			if (f.isFile()) {
				overWriteMp3TagsV2(f, targetOutput);
			} else if (f.isDirectory()) {
				findMp3Files(f.listFiles(), targetOutput);
			}
		}
	}

	private void overWriteMp3TagsV2(File mp3File, String targetOutput) {

		if (!mp3File.getName().endsWith(".mp3"))
			return;

		try {
			
			MusicMetadataSet src_set = new MyID3().read(mp3File);
			
			IMusicMetadata metadata = src_set.getSimplified();

			MetaData newMetaData = getData(mp3File);
			
			System.out.println("Title: " + metadata.getSongTitle() + " -> " + newMetaData.title);
			metadata.setSongTitle(newMetaData.title);
			metadata.setArtist(newMetaData.artist);
			metadata.setAlbum(newMetaData.album + "_" + newMetaData.year);
			metadata.setYear(Integer.valueOf(newMetaData.year));
			
			File fout = new File(getNewMp3FileName(mp3File,targetOutput));

			new MyID3().write(mp3File, fout, src_set, metadata);
			totalFilesConverted++;

		} catch (Exception exc) {
			System.out.println("Error: " + exc.toString());
		}
	}

	private MetaData getData(File mp3File) throws ParseException {

		String components[] = mp3File.getName().split("_");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(components[0]));

		MetaData metaData = new MetaData();
		metaData.artist = "BBC";
		metaData.year = String.valueOf(calendar.get(Calendar.YEAR));
		metaData.album = components[1];
		metaData.title = title(mp3File);

		return metaData;
	}

	private String title(File mp3File) {

		return mp3File.getName().replace("_download.mp3", "");
	}

	private String getNewMp3FileName(File mp3File, String targetOutput) {

		if (targetOutput == null)
			return mp3File.getAbsolutePath().replace(".mp3", "_overwritten.mp3");
		else
			return new File(targetOutput, mp3File.getName().replace(".mp3", "_overwritten.mp3")).getAbsolutePath();

	}

}

package org.gusmp.titleMp3OverWriter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v22Tag;
import com.mpatric.mp3agic.Mp3File;

/**
 * 
 * Problems with files of 320kbps
 */
public class Mp3TagsMp3AgicOverWriter {

	private class MetaData {
		public String year;
		public String artist;
		public String title;
		public String album;
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

	public void init(File targetFolderFile, String targetOutput) {

		if (targetFolderFile.isFile()) {
			overWriteMp3TagsV2(targetFolderFile, targetOutput);
		} else if (targetFolderFile.isDirectory()) {
			findMp3Files(targetFolderFile.listFiles(), targetOutput);
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

	/*
	 * private void overWriteMp3TagsV1(File mp3File, String targetOutput) {
	 * 
	 * if (!mp3File.getName().endsWith(".mp3")) return;
	 * 
	 * try { Mp3File mp3file = new Mp3File(mp3File); if (mp3file.hasId3v2Tag())
	 * { mp3file.removeId3v2Tag(); }
	 * 
	 * ID3v1 tags; if (mp3file.hasId3v1Tag()) { tags = mp3file.getId3v1Tag(); }
	 * else { tags = new ID3v1Tag(); }
	 * 
	 * MetaData metaData = getData(mp3File);
	 * 
	 * System.out.println("Title: " + tags.getTitle() + " -> " +
	 * metaData.title); tags.setTitle(metaData.title);
	 * 
	 * tags.setYear(metaData.year); tags.setArtist(metaData.artist);
	 * tags.setAlbum(metaData.album);
	 * 
	 * mp3file.save(getNewMp3FileName(mp3File,targetOutput));
	 * 
	 * } catch (Exception exc) { System.out.println("Error: " + exc.toString());
	 * } }
	 */

	private void overWriteMp3TagsV2(File mp3File, String targetOutput) {

		if (!mp3File.getName().endsWith(".mp3"))
			return;

		try {
			Mp3File mp3file = new Mp3File(mp3File);
			if (mp3file.hasId3v1Tag()) {
				mp3file.removeId3v1Tag();
			}

			ID3v2 tags;
			if (mp3file.hasId3v2Tag()) {
				tags = mp3file.getId3v2Tag();
			} else {
				tags = new ID3v22Tag();
			}

			MetaData metaData = getData(mp3File);

			// tags.getBPM();
			
			System.out.println("Title: " + tags.getTitle() + " -> " + metaData.title);
			tags.setTitle(metaData.title);

			tags.setYear(metaData.year);
			tags.setArtist(metaData.artist);
			tags.setAlbum(metaData.album);

			mp3file.save(getNewMp3FileName(mp3File, targetOutput));

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

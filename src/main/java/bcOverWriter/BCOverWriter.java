package bcOverWriter;

import java.io.File;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

public class BCOverWriter {

	private static int total = 0;

	public static void main(String[] args) {

		total = 0;
		String folderToScan = "c:/soft/tmp/bbc2/bc/";

		findMp3Files(new File(folderToScan).listFiles(), "");
		System.out.println("Total: " + total);

	}

	private static void findMp3Files(File[] diretoryFiles, String targetOutput) {

		for (File f : diretoryFiles) {
			if (f.isFile()) {
				overWriteMp3TagsV2(f, targetOutput);
			} else if (f.isDirectory()) {
				findMp3Files(f.listFiles(), targetOutput);
			}
		}
	}

	private static void overWriteMp3TagsV2(File mp3File, String targetOutput) {

		if (!mp3File.getName().endsWith(".mp3"))
			return;

		try {

			MusicMetadataSet src_set = new MyID3().read(mp3File);

			IMusicMetadata metadata = src_set.getSimplified();

			System.out.println("Title: " + metadata.getSongTitle());
			// metadata.setSongTitle(newMetaData.title);
			metadata.setArtist("British Council");
			metadata.setAlbum("LearnEnglish Central");
			// metadata.setYear(Integer.valueOf(newMetaData.year));

			File fout = new File(mp3File.getParent(),mp3File.getName().replace(".mp3", "_overwritten.mp3"));

			new MyID3().write(mp3File, fout, src_set, metadata);
			total++;

		} catch (Exception exc) {
			System.out.println("Error: " + exc.toString());
		}
	}

}

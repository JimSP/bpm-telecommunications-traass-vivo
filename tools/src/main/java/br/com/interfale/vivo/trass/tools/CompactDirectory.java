package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class CompactDirectory {

	private static final String ZIP = ".zip";

	public File zipDir(final File dir) throws IOException {
		final FileOutputStream fos = new FileOutputStream(dir.getName() + ZIP);
		final ZipOutputStream zipOut = new ZipOutputStream(fos);
		zipFile(dir, dir.getName(), zipOut);
		zipOut.close();
		fos.close();
		return new File(dir.getName() + ZIP);
	}

	private static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut)
			throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + File.separatorChar + childFile.getName(), zipOut);
			}
			return;
		}
		final FileInputStream fis = new FileInputStream(fileToZip);
		final ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		final byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}
}

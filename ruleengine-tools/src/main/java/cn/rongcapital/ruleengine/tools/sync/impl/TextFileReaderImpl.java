/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cn.rongcapital.ruleengine.tools.sync.TextFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.tools.sync.FileVerifier;

/**
 * the implementation for TextFileReader
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class TextFileReaderImpl implements TextFileReader {

	/**
	 * the content encoding
	 */
	private String contentEncoding = "UTF-8";

	@Autowired
	private FileVerifier fileVerifier;

	/*
	 * (non-Javadoc)
	 * 
	 * @see TextFileReader#readContext(java.io.File)
	 */
	@Override
	public String readContent(final File file) {
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			return null;
		}
		BufferedReader br = null;
		try {
			// the reader
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.contentEncoding));
			final StringBuilder buf = new StringBuilder();
			String line = null;
			// read
			while ((line = br.readLine()) != null) {
				buf.append(line);
			}
			return buf.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

	/**
	 * @param fileVerifier
	 *            the fileVerifier to set
	 */
	public void setFileVerifier(final FileVerifier fileVerifier) {
		this.fileVerifier = fileVerifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TextFileReader#setContentEncoding(java.lang.String)
	 */
	@Override
	public void setContentEncoding(final String contentEncoding) {
		if (!StringUtils.isEmpty(contentEncoding)) {
			this.contentEncoding = contentEncoding;
		}
	}

}

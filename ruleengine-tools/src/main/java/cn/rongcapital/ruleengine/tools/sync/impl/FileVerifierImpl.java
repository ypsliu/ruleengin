/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import java.io.File;

import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.tools.sync.FileVerifier;

/**
 * the implementation for FileVerifier
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class FileVerifierImpl implements FileVerifier {

	/*
	 * (non-Javadoc)
	 * 
	 * @see FileVerifier#canReadFile(java.io.File)
	 */
	@Override
	public boolean canReadFile(final File file) {
		return file != null && file.exists() && file.isFile() && file.canRead();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see FileVerifier#canReadPath(java.io.File)
	 */
	@Override
	public boolean canReadPath(final File path) {
		return path != null && path.exists() && path.isDirectory() && path.canRead();
	}

}

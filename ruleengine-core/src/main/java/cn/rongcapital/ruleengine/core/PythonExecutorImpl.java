/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.service.PythonExecutor;

/**
 * the implementation for PythonExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class PythonExecutorImpl implements PythonExecutor {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PythonExecutorImpl.class);

	/**
	 * the python path, default is "python"
	 */
	@Value("${python.path}")
	private String pythonPath = "python";

	/**
	 * the execution result text encoding, default is "UTF-8"
	 */
	@Value("${python.result.encoding}")
	private String resultEncoding = "UTF-8";

	/**
	 * the execution timeout in seconds, default is 10
	 */
	@Value("${python.execution.timeoutInSec}")
	private long executionTimeoutInSec = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see PythonExecutor#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<String> execute(final String command, final String[] args) {
		// check
		if (StringUtils.isEmpty(command)) {
			LOGGER.error("the command is null");
			throw new RuntimeException("the command is null");
		}
		int argsSize = 2;
		if (args != null) {
			argsSize += args.length;
		}
		final String[] params = new String[argsSize];
		params[0] = this.pythonPath;
		params[1] = command;
		if (args != null) {
			// copy the args
			System.arraycopy(args, 0, params, 2, args.length);
		}
		// execute
		BufferedReader br = null;
		try {
			LOGGER.debug("executing the python command, command: {}, args: {}", command, args);
			final Process process = Runtime.getRuntime().exec(params);
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), this.resultEncoding));
			String line = null;
			final List<String> result = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			if (!process.waitFor(this.executionTimeoutInSec, TimeUnit.SECONDS)) {
				LOGGER.error("the python command execute timeout, command: {}, args: {}, result: {}", command, args,
						result);
				throw new RuntimeException("the python command execute timout, command: " + command + ", args: "
						+ Arrays.toString(args));
			}
			LOGGER.info("the python command executed, command: {}, args: {}, result: {}", command, args, result);
			return result;
		} catch (Exception e) {
			LOGGER.error("execute the python command failed, command: " + command + ", args: " + Arrays.toString(args)
					+ ", error: " + e.getMessage(), e);
			throw new RuntimeException("execute the python command failed, command: " + command + ", args: "
					+ Arrays.toString(args) + ", error: " + e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					//
				}
			}
		}
	}

	/**
	 * @param pythonPath
	 *            the pythonPath to set
	 */
	public void setPythonPath(final String pythonPath) {
		if (!StringUtils.isEmpty(pythonPath)) {
			this.pythonPath = pythonPath;
		}
	}

	/**
	 * @param resultEncoding
	 *            the resultEncoding to set
	 */
	public void setResultEncoding(final String resultEncoding) {
		if (!StringUtils.isEmpty(resultEncoding)) {
			this.resultEncoding = resultEncoding;
		}
	}

	/**
	 * @param executionTimeoutInSec
	 *            the executionTimeoutInSec to set
	 */
	public void setExecutionTimeoutInSec(final long executionTimeoutInSec) {
		this.executionTimeoutInSec = executionTimeoutInSec;
	}

}

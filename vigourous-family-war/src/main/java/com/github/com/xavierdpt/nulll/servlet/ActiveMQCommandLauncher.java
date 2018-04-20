package com.github.com.xavierdpt.nulll.servlet;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ActiveMQCommandLauncher implements Runnable {

	private Process process;
	private volatile boolean started;
	private volatile boolean complete;
	private Exception lastException;
	private String command;

	public ActiveMQCommandLauncher(String command) {
		this.command = command;
	}

	private Path getActiveMQHomePath() {
		return FileSystems.getDefault().getPath("c:", "apache-activemq-5.15.3", "home");
	}

	private Path getActiveMQBinPath() {
		return FileSystems.getDefault().getPath("c:", "apache-activemq-5.15.3", "bin", "activemq.bat");
	}

	@Override
	public void run() {
		try {
			started = true;
			ProcessBuilder pb = new ProcessBuilder(getActiveMQBinPath().toString(), command);
			pb.directory(getActiveMQHomePath().toFile());
			process = pb.start();
			process.waitFor();
		} catch (Exception ex) {
			lastException = ex;
		} finally {
			complete = true;
		}
	}

	public Process getProcess() {
		return process;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isComplete() {
		return complete;
	}

	public Exception getLastException() {
		return lastException;
	}

}

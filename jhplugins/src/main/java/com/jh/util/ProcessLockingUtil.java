package com.jh.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.log4j.Logger;

public class ProcessLockingUtil {
	
	private Logger logger = Logger.getLogger(ProcessLockingUtil.class);

	private File lockFile;
	private FileChannel lockFileChannel;
	private FileLock lock;
	private String lockFileName = "TmsProcessLock.lck";
	
	public ProcessLockingUtil() {
		super();
	}

	public ProcessLockingUtil(String lockFileName) {
		super();
		this.lockFileName = lockFileName;
	}

	public void lockProcess() {
		lockFile = new File(lockFileName);
		/*if (lockFile.exists()) {
			lockFile.delete();
		}*/
		try {
			lockFileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
			lock = lockFileChannel.tryLock();
			if (lock == null) {
				lockFileChannel.close();
				logger.warn("This instance is already running.");
				System.exit(0);
			}
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					unlockProcess();
				}
			});
			
		} catch (Exception e) {
			throw new RuntimeException("Could not start process.", e);
		}
		logger.info("Process locked successfully.");
	}
	
	private void unlockProcess() {
		try {
			if (lock != null) {
				lock.release();
				lockFileChannel.close();
				lockFile.delete();
				logger.info("Released lock successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

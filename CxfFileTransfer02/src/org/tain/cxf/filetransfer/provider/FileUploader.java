package org.tain.cxf.filetransfer.provider;

import javax.activation.DataHandler;

public class FileUploader {

	private String name;
	private String fileType;
	private DataHandler handler;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public DataHandler getHandler() {
		return handler;
	}
	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}
}

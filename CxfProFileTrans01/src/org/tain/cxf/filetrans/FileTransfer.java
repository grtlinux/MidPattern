package org.tain.cxf.filetrans;

public interface FileTransfer {

	public void upload(String fileName, byte[] bytes);

	public byte[] download(String fileName);
}

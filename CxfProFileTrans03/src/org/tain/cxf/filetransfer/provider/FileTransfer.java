package org.tain.cxf.filetransfer.provider;

public interface FileTransfer {

	public void upload(String fileName, byte[] bytes);

	public byte[] download(String fileName);
}

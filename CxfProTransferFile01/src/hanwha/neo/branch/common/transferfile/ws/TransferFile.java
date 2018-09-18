
package hanwha.neo.branch.common.transferfile.ws;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TransferFile {

	//@XmlElement(required = true)
	@XmlMimeType("application/octet-stream")
	private DataHandler dataHandler;
	private String fileName;
	private String filePath;
	private String fileSize;

	//@XmlElement(required = true)
	//@XmlMimeType("application/octet-stream")
	public DataHandler getDataHandler() {
		return dataHandler;
	}
	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

}

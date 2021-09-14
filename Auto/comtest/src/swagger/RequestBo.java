package swagger;

import java.io.Serializable;

public class RequestBo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String description;
	private String header;
	private String requestBody;
	private String httpMethod;
	private String fileName;
	private String url;
	private String assertInfo;
	
	public String getRequestBody() { return requestBody; }
	public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
	public String getAssertInfo() {
		return assertInfo;
	}
	public void setAssertInfo(String assertInfo) { this.assertInfo = assertInfo; }
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String geturl() {
		return url;
	}
	public void seturl(String url) {
		this.url = url;
	}

}


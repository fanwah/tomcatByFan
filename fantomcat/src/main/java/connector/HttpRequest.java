package connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

	private Map<String, String> paramMap;

	private String uri;
	private MethodType type;

	public HttpRequest() {
		paramMap = new HashMap<String, String>();
	}

	public HttpRequest(Map<String, String> parseParamMap) {
		this.paramMap = parseParamMap;
	}

	public HttpRequest(InputStream is) {
		// TODO Auto-generated constructor stub
		try {
			this.parseRequest(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getParameter(String key) {
		return paramMap.get(key);
	}

	
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void parseRequest(InputStream is) throws IOException {
		byte[] buffer = new byte[2048];
		int len = is.read(buffer);
		if (len > 0) {
			String msg = new String(buffer, 0, len);
			int uriStart = msg.indexOf("GET") + 4;
			type = MethodType.GET;
			if ( msg.indexOf("POST") >= 0) {
				type = MethodType.POST;
				uriStart = msg.indexOf("POST") + 5;
			}

			int uriEnd = msg.indexOf("HTTP/1.1") - 1;
			uri = msg.substring(uriStart, uriEnd);
			int paramIndex = 0;
			if (type == MethodType.GET) {
				paramIndex = uri.lastIndexOf("?") + 1;
				setParamMap(uri.substring(paramIndex));
			} else if (type == MethodType.POST) {
				paramIndex = msg.lastIndexOf("\n") + 1;
				setParamMap(msg.substring(paramIndex));
			}

		}

	}

	private void setParamMap(String parameters) {
		if (parameters != null && parameters.length() > 0) {
			
			if(parameters.contains("&")){
				String[] params = parameters.split("&");
				for(String param :params){
					String[] paramValue = param.split("=");
					this.paramMap.put(paramValue[0], paramValue[1]);
				}
			}
			else{
				if(parameters.contains("=")){
					String[] paramValue = parameters.split("=");
					this.paramMap.put(paramValue[0], paramValue[1]);
				}
			}
		}
	}

}

enum MethodType {
	GET, POST;
}

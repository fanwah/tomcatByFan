package connector;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpRequestTest {
	String httpMsg;
	InputStream is;
	HttpRequest httpRequest;
	@Before
	public void SetUp(){
		httpMsg =
		"GET /?fea=1&username=2 HTTP/1.1"+"\r\n"+
		"Host: www.baidu.com"+"\r\n"+
		"Connection: keep-alive"+"\r\n"+
		"Pragma: no-cache"+"\r\n"+
		"Cache-Control: no-cache"+"\r\n"+
		"Upgrade-Insecure-Requests: 1"+"\r\n";
		
		is = new ByteArrayInputStream(httpMsg.getBytes()) ;
		httpRequest = new HttpRequest();
	}

	@After
	public void tearDown() throws Exception {
		is.close();
	}

	@Test
	public void parseRequestTest() throws IOException {
		httpRequest.parseRequest(is);
		assertEquals("/?fea=1&username=2", httpRequest.getUri());
		assertEquals("1",httpRequest.getParameter("fea"));
	}
	
	@Test
	public void getParmeterTest() throws IOException {
		httpRequest.parseRequest(is);
		//assertEquals("/?fea=1", httpRequest.getUri());
		assertEquals("1",httpRequest.getParameter("fea"));
	}
	
	@Test
	public void postTest() throws IOException {
		httpMsg =
				"POST / HTTP/1.1"+"\r\n"+
				"Host: www.baidu.com"+"\r\n"+
				"Connection: keep-alive"+"\r\n"+
				"Pragma: no-cache"+"\r\n"+
				"Cache-Control: no-cache"+"\r\n"+
				"Upgrade-Insecure-Requests: 1"+"\r\n";
				
				is = new ByteArrayInputStream(httpMsg.getBytes()) ;
		httpRequest.parseRequest(is);
		assertEquals("/", httpRequest.getUri());
		//assertEquals("1",httpRequest.getParameter("fea"));
		//assertEquals("1",httpRequest.getParameter("username"));
	}

	@Test
	public void postParameterTest() throws IOException {
		httpMsg =
				"POST / HTTP/1.1"+"\r\n"+
				"Host: www.baidu.com"+"\r\n"+
				"Connection: keep-alive"+"\r\n"+
				"Pragma: no-cache"+"\r\n"+
				"Cache-Control: no-cache"+"\r\n"+
				"Upgrade-Insecure-Requests: 1"+"\r\n"+
				"fea=1&username=2";
				is = new ByteArrayInputStream(httpMsg.getBytes()) ;
		httpRequest.parseRequest(is);
		//assertEquals("/?fea=1", httpRequest.getUri());
		assertEquals("1",httpRequest.getParameter("fea"));
		assertEquals("2",httpRequest.getParameter("username"));
	}
}

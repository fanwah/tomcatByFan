package connector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpResponseTest {

	String uri;
	OutputStream os;
	
	@Before
	public void setUp(){
		os = new ByteArrayOutputStream(); 
		uri="/pom.xml";
	}
	
	@After
	public void tearDown() throws Exception {
		byte[] byteArray = ((ByteArrayOutputStream)os).toByteArray();
		String file = new String(byteArray);
		System.out.println(file);
		os.close();
	}

	@Test
	public void writeFileTest_True() {
		HttpResponse res = new HttpResponse(os);
		assertTrue(res.writeFile(uri));
	}
	
	@Test
	public void writeFileTest_False() {
		uri = "/xdfa.file";
		HttpResponse res = new HttpResponse(os);
		assertFalse(res.writeFile(uri));
	}

	@Test
	public void print(){
		HttpResponse res = new HttpResponse(os);
		res.getWriter().println("XXXXXX");
		res.getWriter().flush();
	}
}

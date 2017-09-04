package connector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class HttpResponse {

	private PrintStream out;
	private ByteArrayOutputStream fileOut;
	private OutputStream source;

	public HttpResponse(OutputStream os) {
		out = new PrintStream(os);
	    source = os;
	}

	public PrintStream getWriter() {
		return out;
	}

	public boolean writeFile(String uri) {
		uri = uri.substring(1);
		File file = new File(uri);
		if (file.exists()) {
			try {
				FileInputStream fr = new FileInputStream(file);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				fileOut = new ByteArrayOutputStream();

				FileChannel channel = fr.getChannel();
				//int len = 0;
				// ByteArrayInputStream fileIn = new ByteArrayInputStream();
				while (channel.read(buffer) != -1) {
					buffer.flip();
					fileOut.write(buffer.array());
					buffer.clear();

				}
				source.write(fileOut.toByteArray());
				fr.close();
				return true;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}

}

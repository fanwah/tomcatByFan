package connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class HttpServer {

	private final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

	private final String SHUTDOWN_COMMAND = "/shutdown";

	private boolean isShutdown = false;

	public void await() {
		// 监听socket
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取流
		while (!isShutdown) {
			try {
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				/*
				 * byte[] buffer = new byte[2048]; is.read(buffer);
				 * System.out.println(new String(buffer,"UTF-8")); // 解析出请求
				 * 
				 * String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
				 * "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" +
				 * "\r\n" + "<h1>File Not Found</h1>";
				 * os.write(errorMessage.getBytes());
				 */

				HttpRequest rq = new HttpRequest(is);
				HttpResponse res = new HttpResponse(os);

				String uri = rq.getUri();

				if (isStaticFile(uri)) {
					boolean isFound = res.writeFile(uri);
					if (!isFound) {
						String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
								+ "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
						res.getWriter().println(errorMessage);
					}
				}
				else{
					String msg = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n"
							+ "Content-Length: 11\r\n" + "\r\n" + "<h1>OK</h1>";
					res.getWriter().println(msg);
				}

				// 构造出响应
				os.flush();
				is.close();
				os.close();
				socket.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public boolean isStaticFile(String uri) {
		List<String> staticFileSuffix = Arrays.asList(".jpg", ".css", ".js", ".gif", ".svg");
		for (String suffix : staticFileSuffix) {
			if (uri.endsWith(suffix))
				return true;
		}

		return false;
	}

	public static void main(String[] args) {

		HttpServer server = new HttpServer();
		server.await();
	}

}

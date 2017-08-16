package connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
				
				byte[] buffer = new byte[2048];
				is.read(buffer);
				System.out.println(new String(buffer,"UTF-8"));
				// 解析出请求
				
				String errorMessage = "HTTP/1.1 404 File Not Found\r\n"  
                        + "Content-Type: text/html\r\n"  
                        + "Content-Length: 23\r\n" + "\r\n"  
                        + "<h1>File Not Found</h1>";  
				os.write(errorMessage.getBytes());
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

	public static void main(String[] args) {

		HttpServer server = new HttpServer();
		server.await();
	}

}

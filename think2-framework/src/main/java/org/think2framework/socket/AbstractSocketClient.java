package org.think2framework.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 抽象socket客户端
 */
public abstract class AbstractSocketClient {

	private static Logger logger = LogManager.getLogger(AbstractSocketClient.class);
	private Socket socket; // socket
	private String address; // 地址
	private Integer port; // 端口

	public AbstractSocketClient(String address, Integer port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * 处理socket业务逻辑
	 * 
	 * @param inputStream
	 *            inputStream
	 * @param outputStream
	 *            outputStream
	 * @throws IOException
	 *             异常
	 */
	public abstract void doSocket(InputStream inputStream, OutputStream outputStream) throws IOException;

	/**
	 * 停止socket
	 */
	public void stop() {
		if (null != this.socket) {
			if (this.socket.isClosed()) {
				try {
					this.socket.close();
				} catch (IOException e) {
					logger.debug("Socket stop error {}", e);
				}
			}
			this.socket = null;
			logger.debug("Socket stop success");
		}

	}

	/**
	 * 启动
	 */
	public void start() {
		try {
			this.socket = new Socket(this.address, this.port);
			logger.debug("Socket connect to server, address {} port {}", this.socket.getInetAddress(),
					this.socket.getPort());
			this.doSocket(this.socket.getInputStream(), this.socket.getOutputStream());
			this.stop();
		} catch (Exception e) {
			logger.debug("Socket start failed, error {}", e);
			this.socket = null;
		}

	}

	/**
	 * 重启
	 */
	public void restart() {
		this.stop();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		this.start();
	}
}

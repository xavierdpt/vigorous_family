package com.github.com.xavierdpt.nulll.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/manage/activemq")
public class ActiveMQManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * @Resource private ManagedThreadFactory mtf;
	 */

	private static Object handle = new Object();
	private static ActiveMQCommandLauncher activeMQStarter;
	private static ActiveMQCommandLauncher activeMQStopper;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter writer = resp.getWriter();

		String command = req.getParameter("command");

		if (command == null) {
			writer.println("No command given.");
			return;
		}
		if ("start".equals(command)) {
			startActiveMQ(writer);
			return;
		}
		if ("stop".equals(command)) {
			stopActiveMQ(writer);
			return;
		}
		writer.println("Unknown command : '" + command + "'.");

	}

	private void startActiveMQ(PrintWriter writer) throws IOException {
		synchronized (handle) {
			if (activeMQStarter != null) {
				if (!activeMQStarter.isComplete()) {
					writer.println("ActiveMQ already running");
					return;
				}
			}
			activeMQStarter = new ActiveMQCommandLauncher("start");
		}
		new Thread(activeMQStarter).start();
		writer.println("ActiveMQ started.");
	}

	private void stopActiveMQ(PrintWriter writer) throws IOException {
		synchronized (handle) {
			if (activeMQStopper != null) {
				if (!activeMQStopper.isComplete()) {
					writer.println("ActiveMQ stopper already running");
					return;
				}
			}
			activeMQStopper = new ActiveMQCommandLauncher("stop");
		}
		new Thread(activeMQStopper).start();
		writer.println("ActiveMQ stopped.");
	}

}

package com.github.com.xavierdpt.nulll.servlet;

import java.io.IOException;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.resource.spi.ConnectionManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.ra.ActiveMQConnectionFactory;
import org.apache.activemq.ra.ActiveMQConnectionRequestInfo;
import org.apache.activemq.ra.ActiveMQManagedConnectionFactory;
import org.apache.activemq.ra.SimpleConnectionManager;

@WebServlet("/jmstest")
public class JMSTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doSomething(req, resp);

	}

	private void doSomething(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			ActiveMQManagedConnectionFactory factory = new ActiveMQManagedConnectionFactory();
			ConnectionManager manager = new SimpleConnectionManager();
			ActiveMQConnectionRequestInfo connectionRequestInfo = new ActiveMQConnectionRequestInfo();
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(factory, manager, connectionRequestInfo);
			QueueConnection queue = cf.createQueueConnection();
			queue.start();
			QueueSession session = queue.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue q = new ActiveMQQueue();
			QueueReceiver r = session.createReceiver(q);
			MessageListener listener = new MessageListener() {

				@Override
				public void onMessage(Message message) {
					// TODO Auto-generated method stub

				}
			};
			r.setMessageListener(listener);
			resp.getWriter().println("All good !");
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

}

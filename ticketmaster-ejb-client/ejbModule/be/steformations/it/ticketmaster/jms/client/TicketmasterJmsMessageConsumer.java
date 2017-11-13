package be.steformations.it.ticketmaster.jms.client;

import be.steformations.it.ticketmaster.dto.EvenementDto;

public class TicketmasterJmsMessageConsumer {

	public static void main(String[] args) throws Exception{
		System.out.println("TicketmasterJmsMessageConsumer.main()");
		java.util.Properties properties = new java.util.Properties();
		properties.put("java.naming.provider.url", "localhost:3700");
		javax.naming.InitialContext cxt = new javax.naming.InitialContext();
		
		javax.jms.ConnectionFactory factory = (javax.jms.ConnectionFactory) cxt.lookup("jms/TicketmasterTopicConnectionFactory");
		javax.jms.Topic topic = (javax.jms.Topic) cxt.lookup("jms/TicketmasterTopic");
	
		javax.jms.Connection connection = factory.createConnection();
		javax.jms.Session session = connection.createSession();
		javax.jms.MessageConsumer consumer = session.createConsumer(topic);
	
		connection.start();
		
		while (true){
			System.out.println("TicketmasterJmsMessageConsumer.main()");
			javax.jms.Message message = consumer.receive();
			javax.jms.ObjectMessage objectMessage = (javax.jms.ObjectMessage) message;
			java.io.Serializable serial = objectMessage.getObject();
			EvenementDto dto = (EvenementDto) serial;
			System.out.println("TicketmasterJmsMessageConsumer a reçu " + dto.toString());
		}
	
	
	
	}
	
}

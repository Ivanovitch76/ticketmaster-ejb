package be.steformations.it.ticketmaster.jms.server;

import java.io.Serializable;

import be.steformations.it.ticketmaster.dto.EvenementDto;

@javax.ejb.Singleton
public class TicketmasterJmsMessageProducer {

	@javax.annotation.Resource(mappedName="jms/TicketmasterTopicConnectionFactory")
	private javax.jms.ConnectionFactory factory;
	@javax.annotation.Resource(mappedName="jms/TicketmasterTopic")
	private javax.jms.Topic topic;
	
	public void broadcastNewTicketmasterEvent(EvenementDto dto){
		System.out.println("TicketmasterJmsMessageProducer.broadcastNewTicketmasterEvent()");
		try {
			javax.jms.Connection connection = this.factory.createConnection();
			javax.jms.Session session = connection.createSession();
			javax.jms.MessageProducer producer = session.createProducer(topic);
			javax.jms.Message message = session.createObjectMessage(dto);
			producer.send(message);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}

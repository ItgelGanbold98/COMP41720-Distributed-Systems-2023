import org.apache.activemq.ActiveMQConnectionFactory;
import service.GAQService;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.QuotationMessage;

import javax.jms.*;


public class Main {
    private static GAQService service = new GAQService();

    public static void main(String[] args) throws JMSException {
        System.out.println("Main class called.");

        String host = "localhost";

        if (args.length > 0) {
            host = args[0];
        }

        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        Connection connection = factory.createConnection();
        connection.setClientID("girlsallowed");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);


        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(topic);
        MessageProducer producer = session.createProducer(queue);

        connection.start();

        System.out.println("Connection successful in Girlsallowed.");

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Message received from APPLICATIONS topic");

                try {
                    ClientMessage request = (ClientMessage) ((ObjectMessage) message).getObject();
                    Quotation quotation = service.generateQuotation(request.getInfo());
                    Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));
                    producer.send(response);
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

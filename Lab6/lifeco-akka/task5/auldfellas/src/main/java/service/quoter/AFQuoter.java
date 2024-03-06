package service.quoter;

import akka.japi.pf.ReceiveBuilder;
import service.auldfellas.AFQService;
import akka.actor.AbstractActor;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.QuotationMessage;

public class AFQuoter extends AbstractActor {
    private AFQService service = new AFQService();
    @Override
    public Receive createReceive() {
        System.out.println("**AULDFELLAS ACTOR STARTED** " + getSelf());
        return new ReceiveBuilder()
                .match(ClientMessage.class,
                        msg -> {
                            Quotation quotation = service.generateQuotation(msg.getInfo());
                            getSender().tell(new QuotationMessage(msg.getToken(), quotation), getSelf());
                            System.out.println("Auldfellas sending message to: " + getSender());
                        })
                .build();
    }
}
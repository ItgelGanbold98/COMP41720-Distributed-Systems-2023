package service.quoter;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.core.Quotation;
import service.girlsallowed.GAQService;
import service.message.ClientMessage;
import service.message.QuotationMessage;

public class GAQuoter extends AbstractActor {
    private GAQService service = new GAQService();
    @Override
    public Receive createReceive() {
        System.out.println("**GIRLSALLOWED ACTOR STARTED**");
        return new ReceiveBuilder()
                .match(ClientMessage.class,
                        msg -> {
                            Quotation quotation = service.generateQuotation(msg.getInfo());
                            getSender().tell(new QuotationMessage(msg.getToken(), quotation), getSelf());
                            System.out.println("Girlsallowed sending message to: " + getSender());
                        })
                .build();
    }
}
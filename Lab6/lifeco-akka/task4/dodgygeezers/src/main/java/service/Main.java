package service;

import akka.actor.*;
import service.message.RegisterMessage;
import service.quoter.DGQuoter;

public class Main {
    static ActorSystem system = ActorSystem.create("ServiceSystem");

    public static void main(String[] args) {
        final Props DGQuoterProp = Props.create(DGQuoter.class);
        final ActorRef DGQuoterRef = system.actorOf(DGQuoterProp, "DGQuoter");

        String BrokerPath = "akka.tcp://BrokerSystem@localhost:2550/user/BrokerQuoter";
        ActorSelection remoteActor = system.actorSelection(BrokerPath);

        System.out.println("remoteActor: " + remoteActor);
        remoteActor.tell(new RegisterMessage(DGQuoterRef, "QService"), DGQuoterRef);

        ExtendedActorSystem extendedSystem = (ExtendedActorSystem) system;
        Address port = extendedSystem.provider().getDefaultAddress();
        System.out.println("The DG Service system is bound to port: " + port);
    }
}

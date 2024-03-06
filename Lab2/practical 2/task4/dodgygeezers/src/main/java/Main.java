import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import service.core.BrokerService;
import service.dodgygeezers.DGQService;
import service.core.QuotationService;
import service.core.Constants;
public class Main {
    public static void main(String[] args) {
        QuotationService dgqService = new DGQService();
        try {
            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry(args[0], 1099);
            }

            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(dgqService,0);

            BrokerService broker_service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

            broker_service.registerService(Constants.DODGY_GEEZERS_SERVICE, quotationService);

            System.out.println("(Dodgygeezers) STOPPING SERVER SHUTDOWN");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
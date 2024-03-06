import service.girlsallowed.GAQService;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        String host = "0.0.0.0";

        try {

            if (args.length > 0) {
                host = args[0];
            }
            Endpoint.publish("http://" + host + ":9003/quotations", new GAQService());

            System.out.println("Girlsallowed is published at: http://" + host + ":9003/quotations");

            jmdnsAdvertise(host);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private static void jmdnsAdvertise(String host) {
        try {
            String cnfg = "path=http://"+host+":9003/quotations?wsdl";

            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            System.out.println("Localhost address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("** Girlsallowed is advertised at: " + cnfg + " with service type: _quote._tcp.local.");

            ServiceInfo serviceInfo = ServiceInfo.create("_quote._tcp.local.", "ws-service", 9003, cnfg);
            jmdns.registerService(serviceInfo);

            Thread.sleep(100000);

            jmdns.unregisterAllServices();

        } catch (Exception e) {
            System.out.println("** Problem advertising Dodgygeezers service **");
            e.printStackTrace();
        }
    }
}

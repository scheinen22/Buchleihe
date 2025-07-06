package server.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import server.data.AusleiheDAO;
import server.data.BuchDAO;
import server.data.NutzerDAO;
import server.data.VormerkerlisteDAO;
import server.data.VorschlagDAO;
import server.service.AusleiheService;
import server.service.NutzerService;
import server.service.VorschlagService;

public class Server {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry gestartet.");

            AusleiheService ausleiheService = new AusleiheService(BuchDAO.getInstance(), VormerkerlisteDAO.getInstance(), AusleiheDAO.getInstance());
            NutzerService nutzerService = new NutzerService(NutzerDAO.getInstance());
            VorschlagService vorschlagService = new VorschlagService(VorschlagDAO.getInstance(), BuchDAO.getInstance());
            System.out.println("Service-Objekte erstellt.");

            Naming.bind("rmi://localhost/AusleiheService", ausleiheService);
            Naming.bind("rmi://localhost/NutzerService", nutzerService);
            Naming.bind("rmi://localhost/VorschlagService", vorschlagService);

            System.out.println("Server ist bereit und wartet auf Anfragen.");

        } catch (Exception e) {
            System.err.println("Fehler beim Starten des Servers: " + e);
            e.printStackTrace();
        }
    }
}
package server.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import server.data.AusleiheDAO;
import server.data.BuchDAO;
import server.data.NutzerDAO;
import server.data.VormerkerlisteDAO;
import server.data.VorschlagDAO;
import server.service.AusleiheServiceImpl;
import server.service.NutzerServiceImpl;
import server.service.VorschlagServiceImpl;

public class Server {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry gestartet.");

            AusleiheServiceImpl ausleiheService = new AusleiheServiceImpl(BuchDAO.getInstance(), VormerkerlisteDAO.getInstance(), AusleiheDAO.getInstance());
            NutzerServiceImpl nutzerService = new NutzerServiceImpl(NutzerDAO.getInstance());
            VorschlagServiceImpl vorschlagService = new VorschlagServiceImpl(VorschlagDAO.getInstance(), BuchDAO.getInstance());
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
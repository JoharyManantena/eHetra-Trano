package prelevement;

import javax.ejb.Stateless;
import java.sql.Connection;

@Stateless
public class PrelevementEJB implements PrelevementLocal {
    @Override
    public void generateAndPersistVenteRemote(Prelevement prelevement, String user, Connection connection) throws Exception {
        System.out.println("Insertion facture server facture");
        prelevement.generateAndPersistVente(user,connection);
    }
}

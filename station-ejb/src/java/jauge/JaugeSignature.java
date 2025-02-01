package jauge;

import java.sql.Connection;

public interface JaugeSignature {
    public Jauge jauger(Jauge jauge, Connection connection) throws Exception;

}

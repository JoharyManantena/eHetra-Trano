package jauge;

import utilitaire.UtilDB;

import java.sql.Connection;

public class JaugeService implements JaugeSignature{
    @Override
    public Jauge jauger(Jauge jauge, Connection connection) throws Exception {
        boolean isOpen = false;
        try{
            if (connection == null) {connection = new UtilDB().GetConn();isOpen=true;}
            jauge.createObject("1060",connection);
            jauge.validerObject("1060",connection);
//            if (isOpen) connection.commit();
            return jauge;
        }catch (Exception e){
            if (isOpen) connection.rollback();
            e.printStackTrace();
        }finally {
            if (isOpen) connection.close();
        }
        return null;
    }
}

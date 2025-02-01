package jauge;

public class JaugeCpl extends Jauge{
    String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public JaugeCpl() {
        this.setNomTable("v_cuve_jauge");
    }
}

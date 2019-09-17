public class GCItemQuantities implements GCUnit {
    public GCItemQuantities(String GCname, int serialNumber, String itemCode, String itemName, String unit, double gcQuantities, double unitPrice, double comboPrice) {
        this.GCname = GCname;
        this.serialNumber = serialNumber;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unit = unit;
        this.gcQuantities = gcQuantities;
        this.unitPrice = unitPrice;
        this.comboPrice = comboPrice;
    }

    private String GCname;

    private int serialNumber ;
    private String itemCode;
    private String itemName;

    private String unit;
    private double gcQuantities;

    private double unitPrice;
    private double comboPrice;
//////////////////////////////////////////////getters
    public String getGCname() {
        return GCname;
    }

    public void setGCname(String GCname) {
        this.GCname = GCname;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getGcQuantities() {
        return gcQuantities;
    }

    public void setGcQuantities(double gcQuantities) {
        this.gcQuantities = gcQuantities;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getComboPrice() {
        return comboPrice;
    }

    public void setComboPrice(double comboPrice) {
        this.comboPrice = comboPrice;
    }

//////////////////////////////////////////////////////////////////getters


    @Override
    public String toString() {
        return ""+getGCname()+"/"+getSerialNumber()+"/"+getItemCode()+"/"+getItemName()+"/"+getUnit()+"/"+getGcQuantities()+"/"+getUnitPrice()+"/"+getComboPrice() ;
    }
}

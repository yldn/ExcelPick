public class GCblockPriceItemQuantities implements GCUnit {
    String GCname ;

    int serialNumber ;
    String itemCode ;
    String itemName ;

    String unit ;
    double gcQuantities ;

    public GCblockPriceItemQuantities(String GCname, int serialNumber, String itemCode, String itemName, String unit, double gcQuantities) {
        this.GCname = GCname;
        this.serialNumber = serialNumber;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unit = unit;
        this.gcQuantities = gcQuantities;
    }

    public String getGCname() {
        return GCname;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUnit() {
        return unit;
    }

    public double getGcQuantities() {
        return gcQuantities;
    }
    @Override
    public String toString() {
        return ""+getGCname()+"/"+getSerialNumber()+"/"+getItemCode()+"/"+getItemName()+"/"+getUnit()+"/"+getGcQuantities() ;
    }
}

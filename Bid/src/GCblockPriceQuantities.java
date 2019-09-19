import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GCblockPriceQuantities implements GCList {
    String name ;
    List<GCblockPriceItemQuantities> quantitiesList =new ArrayList<GCblockPriceItemQuantities>();

    public String getName() {
        return name;
    }

    public List<GCblockPriceItemQuantities> getQuantitiesList() {
        return quantitiesList;
    }

    public GCblockPriceQuantities(String name , String path ) {
        this.name = name;
        quantitiesList = this.parseItemQuantity( new FileExtractor(path).getExtractedFiles()) ;
    }

    private  Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    private List<GCblockPriceItemQuantities> parseItemQuantity(List<File> listPath) {

        List<GCblockPriceItemQuantities> quantityList = new ArrayList<GCblockPriceItemQuantities>();



        for (File f : listPath) {
            Workbook wb = readExcel(f.getPath());
            if (wb != null) {
                Sheet sheet = wb.getSheet("表-2 分部分项工程量清单");
//                String GCname = sheet.getRow(1).getCell(0).getRichStringCellValue().toString();
                String GCname = f.getName();
                int dot = GCname.lastIndexOf('.');
                GCname = GCname.substring(0, dot);
//                System.out.println(GCname);


                if (sheet.getRow(0).getCell(0).getRichStringCellValue().toString().length() <=2) {
                    //从表1 第1行查起一直到 physicalnumberofRows
                    for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                        if (sheet.getRow(i).getCell(0) != null && sheet.getRow(i).getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            Row row = sheet.getRow(i);

                            int serialNumber = (int) row.getCell(0).getNumericCellValue();
                            String itemCode = row.getCell(1).getRichStringCellValue().toString();
                            String itemName = row.getCell(2).getRichStringCellValue().toString();
                            String unit = row.getCell(3).getRichStringCellValue().toString();
                            double gcQuantities = row.getCell(4).getNumericCellValue();

                            GCblockPriceItemQuantities itemQuantities = new GCblockPriceItemQuantities(GCname, serialNumber, itemCode, itemName, unit, gcQuantities);
//                        System.out.println(itemQuantities.toString());
                            quantityList.add(itemQuantities);
//                        System.out.println(itemQuantities.toString());
                        }
                    }
                }else
                {
                    for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
                        if (sheet.getRow(i).getCell(0) != null && sheet.getRow(i).getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {

                            Row row = sheet.getRow(i);
                            int serialNumber = (int)row.getCell(0).getNumericCellValue();
                            String itemCode = row.getCell(1).getRichStringCellValue().toString();
                            String itemName = row.getCell(2).getRichStringCellValue().toString();
//                        System.out.println(serialNumber);
                            String unit = row.getCell(4).getRichStringCellValue().toString();
                            double gcQuantities = row.getCell(6).getNumericCellValue();

                            GCblockPriceItemQuantities itemQuantities = new GCblockPriceItemQuantities(GCname, serialNumber, itemCode, itemName, unit, gcQuantities);
//                        System.out.println(itemQuantities.toString());
                            quantityList.add(itemQuantities);
//                        System.out.println(itemQuantities.toString());
                        }
                    }
                }

            }

        }














        return quantityList;
    }
}

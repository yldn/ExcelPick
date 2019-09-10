
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GCbillOfQuantities {
    String name ;
     List<GCItemQuantities> quantitiesList = new ArrayList<>();
//     List<GCItemQuantities> jyList = new ArrayList<>();
//     List<GCItemQuantities> rfList = new ArrayList<>();
//     List<GCItemQuantities> ygList = new ArrayList<>();

    public GCbillOfQuantities(String name , String path ) {
        this.name = name;
         quantitiesList = parseItemQuantity( new FileExtractor(path).getExtractedFiles()) ;
//         jyList = parseItemQuantity( new FileExtractor("src/九冶").getExtractedFiles()) ;
//         rfList = parseItemQuantity( new FileExtractor("src/荣丰").getExtractedFiles()) ;
//         ygList = parseItemQuantity( new FileExtractor("src/阳光").getExtractedFiles()) ;
    }

    public String getName() {
        return name;
    }

    public List<GCItemQuantities> getQuantitiesList() {
        return quantitiesList;
    }

    public static Workbook readExcel(String filePath){
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

    public static List<GCItemQuantities> parseItemQuantity(List<File> listPath){
        //读所有list里的
        List<GCItemQuantities> quantityList = new ArrayList<GCItemQuantities>();
        for (File f : listPath){
            Workbook wb = readExcel(f.getPath());
            if (wb != null){
                Sheet sheet = wb.getSheetAt(0);

                String GCname = sheet.getRow(1).getCell(0).getRichStringCellValue().toString();
//                System.out.println(GCname);

                //从表2 第五行查起一直到 physicalnumberofRows
                for (int i  = 5 ; i< sheet.getPhysicalNumberOfRows();i++){
                    if(sheet.getRow(i).getCell(0)!= null && sheet.getRow(i).getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
                        Row row = sheet.getRow(i);
                        int serialNumber = (int)row.getCell(0).getNumericCellValue();
                        String itemCode = row.getCell(1).getRichStringCellValue().toString();
                        String itemName = row.getCell(2).getRichStringCellValue().toString();
                        String unit = row.getCell(4).getRichStringCellValue().toString();
                        double gcQuantities = row.getCell(5).getNumericCellValue();
                        double unitPrice = row.getCell(6).getNumericCellValue();
                        double comboPrice = row.getCell(8).getNumericCellValue();

                        GCItemQuantities itemQuantities = new GCItemQuantities(GCname,serialNumber,itemCode,itemName,unit,gcQuantities,unitPrice,comboPrice);

                        quantityList.add(itemQuantities);
                    }
                }
            }
        }
        return quantityList;
    }





}

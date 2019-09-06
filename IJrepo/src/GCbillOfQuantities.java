
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GCbillOfQuantities {

    public static void main (String[]args) throws IOException {
        //所有分部分项工程量清单计价表 项目汇总为一个list： quantityList
//        FileExtractor ext = new FileExtractor("C:\\Users\\Administrator\\Desktop\\北京市市政(1)\\北京市市政\\汉宁路与汉阳路（西新街-天汉大道）供热管道工程Ⅰ标段");
        FileExtractor ext = new FileExtractor("src/北京市市政(1)/北京市市政/汉宁路与汉阳路（西新街-天汉大道）供热管道工程Ⅰ标段");
        List<File> listPath =   ext.getExtractedFiles();
        List<GCItemQuantities> quantityList = parseItemQuantity(listPath);

        System.out.println(quantityList.get(7));

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
                Sheet sheet = wb.getSheetAt(1);

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

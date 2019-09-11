
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GCbillOfQuantities implements GCList{
    String name ;
     List<GCItemQuantities> quantitiesList = new ArrayList<>();
    public GCbillOfQuantities(String name , String path ) {
        this.name = name;
         quantitiesList = parseItemQuantity( new FileExtractor(path).getExtractedFiles()) ;
    }

    public String getName() {
        return name;
    }

    public List<GCItemQuantities> getQuantitiesList() {
        return quantitiesList;
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
    ///将工程目录下所有的xlsx文件读取出来，将所有单项解析到list里
    public List<GCItemQuantities> parseItemQuantity(List<File> listPath){
        //读所有list里的
        List<GCItemQuantities> quantityList = new ArrayList<GCItemQuantities>();
        for (File f : listPath){
            Workbook wb = readExcel(f.getPath());
            if (wb != null){
                Sheet sheet = wb.getSheetAt(0);

                String GCname = sheet.getRow(1).getCell(0).getRichStringCellValue().toString();
//                System.out.println(GCname);

                //从表1 第五行查起一直到 physicalnumberofRows
                for (int i  = 5 ; i< sheet.getPhysicalNumberOfRows();i++){
                    if(sheet.getRow(i).getCell(0)!= null && sheet.getRow(i).getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
                        Row row = sheet.getRow(i);
                        int serialNumber = (int)row.getCell(0).getNumericCellValue();
                        String itemCode = row.getCell(1).getRichStringCellValue().toString();
                        String itemName = row.getCell(2).getRichStringCellValue().toString();
                        String unit = row.getCell(4).getRichStringCellValue().toString();
                        double gcQuantities = row.getCell(5).getNumericCellValue();
                        double unitPrice;
                        if(row.getCell(6) != null){
                            unitPrice = row.getCell(6).getNumericCellValue()  ;
                        }
                        else
                            unitPrice = 0;

                        double comboPrice ;
                        if(row.getCell(8) != null){
                            comboPrice = row.getCell(8).getNumericCellValue()  ;
                        }
                        else
                            comboPrice = 0;

                        GCItemQuantities itemQuantities = new GCItemQuantities(GCname,serialNumber,itemCode,itemName,unit,gcQuantities,unitPrice,comboPrice);

                        quantityList.add(itemQuantities);
                    }
                }
            }
        }
        return quantityList;
    }

    public  void exportExcelAsXSLX(String path ) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("GCBillOfQuantities");
        CellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("单位");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("工程名称");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("项目编码");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("项目名称");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("计量单位");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("工程数量");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("综合单价（元）");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("合价（元）");
        cell.setCellStyle(style);
        for (int i = 0; i < this.getQuantitiesList().size(); i++) {
            row = sheet.createRow(i+1);
            GCItemQuantities item = this.getQuantitiesList().get(i);
            row.createCell(0).setCellValue(this.getName());
            row.createCell(1).setCellValue(item.getGCname());
            row.createCell(2).setCellValue(item.getSerialNumber());
            row.createCell(3).setCellValue(item.getItemCode());
            row.createCell(4).setCellValue(item.getItemName());
            row.createCell(5).setCellValue(item.getUnit());
            row.createCell(6).setCellValue(item.getGcQuantities());
            row.createCell(7).setCellValue(item.getUnitPrice());
            row.createCell(8).setCellValue(item.getComboPrice());
        }
        //output
        File file = new File(path);
        FileOutputStream output = new FileOutputStream(file);
        wb.write(output);
        output.close();

    }




}

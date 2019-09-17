import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class main_1 {

    //拦标价清单
    static GCblockPriceQuantities blockPriceList;

    //decimal formatter
    static DecimalFormat df = new DecimalFormat("0.00");
    ///总表（所有单位，所有项目）
    static List<GCbillOfQuantities> companies = new ArrayList<GCbillOfQuantities>();
    //筛选表
    //<复查--导出excel>
    static List<Integer> GCchecklist = new ArrayList<Integer>();
    //单价表
    static List<List<Double>> GCUnitPrice = new ArrayList<>();
    //打分表
    static List<List<Double>> GCUnitpoints = new ArrayList<>();

    //挑出 number个
    static int number = 30 ;

    public static void main (String[]args) throws IOException {
        blockPriceList = new GCblockPriceQuantities("拦标价清单","/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/拦标价清单");

        companies.add(new GCbillOfQuantities("荣盛", "/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/荣盛"));
        companies.add(new GCbillOfQuantities("陕西正大", "/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/陕西正大"));
        companies.add(new GCbillOfQuantities("江豪", "/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/江豪"));
        companies.add(new GCbillOfQuantities("亚玄", "/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/亚玄"));

        if (blockPriceList.getQuantitiesList().size()> number ){
              GCchecklist =  pick(blockPriceList.getQuantitiesList().size() , number);
            //测试数组

//            for (int i = 0; i < number; i++) {
//                GCchecklist.add(i);
//            }

            GCUnitPrice = summerizeUnitPrice();
            GCUnitpoints = calculatePoint();
//            List<Double> summe = summerizePoint();
            exportAll("/Users/liuyang/Desktop/ExcelPick/大荔县集中供热管道工程四条管线项目供热管道工程/汇总.xlsx");

//            System.out.println("SUMME："+ Arrays.toString(summe.toArray()));
        }

    }
////////////////生成汇总列表
    public static List<Double> summerizePoint(){
        List<Double> summe = new ArrayList<Double>();
        for (int i = 0; i < GCUnitpoints.get(0).size(); i++) {
            double sum = 0.0;
            for (int j = 0; j < GCUnitpoints.size(); j++) {
                sum += GCUnitpoints.get(j).get(i);
            }
            summe.add( Double.valueOf(df.format(sum)) );
        }
        return  summe;
    }
///////////////生成综合单价评分表
    public static List<List<Double>> calculatePoint(){
        List<List<Double>> out = new ArrayList<>();
        for (int i = 0; i < GCUnitPrice.size(); i++) {
            out.add(calculateP(i));
        }
        return out;
    }
    //计算第i项所有单位的分数
    private static List<Double> calculateP(int i ){
        List<Double> out = new ArrayList<Double>();
//        保留两位小数
        for (int j = 0; j < GCUnitPrice.get(i).size(); j++) {
           double x = Double.valueOf(df.format(calculatepoint(discount(calculateOffset(GCUnitPrice.get(i).get(j),calculateBase(GCUnitPrice.get(i))))))) ;
            out.add(x);
        }
        return out;
    }

    //根据checklist里将所有公司的对应单价加入list
    //比较所有list里checklist对应项
    ///把每个公司每一项的得分记录在list里
    ///===综合单价表
    public static List<List<Double>> summerizeUnitPrice(){
        List<List<Double>> GCUnitPrice = new ArrayList<List<Double>>();
             //item
        for (int i = 0; i < GCchecklist.size(); i++) {
            //company
            GCUnitPrice.add(insertUnitPrices(i));
        }
        return  GCUnitPrice;
    }
    ///从checklist里 第x项
    private static List<Double> insertUnitPrices( int x ) {
        List<Double> unitOfCompanies = new ArrayList<Double>();
        for (int i = 0; i < companies.size(); i++) {
            GCbillOfQuantities bill = companies.get(i);
            unitOfCompanies.add(bill.getQuantitiesList().get(x).getUnitPrice());
        }
        return  unitOfCompanies;
    }

    ///////////////////////具体评标办法
    /////////////抽取 number 个项目
    public static List<Integer> pick (int range , int number){
        List<Integer> out = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int x = random.nextInt(range);
            while (out.contains(x)){
                x = random.nextInt(range);
            }
            out.add(x);
        }
        out.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1>o2){
                    return 1;
                }
                else if (o1==o2){
                    return 0;
                }else
                return -1;
            }
        });
        return out;

    }

    ////////////  综合单价评分方法
    ////第i项
    // 计算基准价
    private static double calculateBase(List<Double> unitPrices ){
        double sum = 0.0;
        for (double x: unitPrices ) {
            sum += x;
        }
//        System.out.println("Base :"+ sum/companies.size()*0.97);
        return Double.valueOf(df.format(sum/companies.size()*0.97))  ;
    }
    //计算偏差
    private static double calculateOffset(double unitPrice , double basePrice ){
//        System.out.println("offset :"+(company.getQuantitiesList().get(i).getUnitPrice()/basePrice - 1 ) * 100);
        return (unitPrice/basePrice - 1 ) * 100 ;
    }
    //计算扣分
    private static double discount(double offset ){
        return offset>0? offset* - 0.02 : offset * 0.01;
    }
    //计算得分
    private static double calculatepoint(double discount){
        return discount + 1 >0 ? discount+1 : 0 ;
    }

    //// export
    static Workbook wb = new XSSFWorkbook();

    public static void exportAll(String path) throws IOException {

        exportblockPrice(blockPriceList);
        for (GCbillOfQuantities bq : companies) {
            exportBillOfQuantities(bq);
        }
//        exportchecklist(GCchecklist);
        exportSummerize(companies,GCchecklist,GCUnitPrice,GCUnitpoints);

        File file = new File(path);
        FileOutputStream output = new FileOutputStream(file);
        wb.write(output);
        output.close();

    }

 ///////输出拦标价单项
     private  static  Sheet exportblockPrice(GCblockPriceQuantities bq){
         Sheet sh = wb.createSheet(bq.getName());
         CellStyle style = wb.createCellStyle();
         style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

         Row row = sh.createRow(0);
         Cell cell ;
         cell = row.createCell(0);
         cell.setCellValue("工程名称");
         cell.setCellStyle(style);
         cell = row.createCell(1);
         cell.setCellValue("序号");
         cell.setCellStyle(style);
         cell = row.createCell(2);
         cell.setCellValue("项目编码");
         cell.setCellStyle(style);
         cell = row.createCell(3);
         cell.setCellValue("项目名称");
         cell.setCellStyle(style);
         cell = row.createCell(4);
         cell.setCellValue("计量单位");
         cell.setCellStyle(style);
         cell = row.createCell(5);
         cell.setCellValue("工程数量");
         cell.setCellStyle(style);


         for (int i = 0; i < bq.getQuantitiesList().size(); i++) {
             row = sh.createRow(i+1);
             GCblockPriceItemQuantities item = bq.getQuantitiesList().get(i);
             row.createCell(0).setCellValue(item.getGCname());
             row.createCell(1).setCellValue(item.getSerialNumber());
             row.createCell(2).setCellValue(item.getItemCode());
             row.createCell(3).setCellValue(item.getItemName());
             row.createCell(4).setCellValue(item.getUnit());
             row.createCell(5).setCellValue(item.getGcQuantities());
         }
         return sh;


     }

////////输出工程量单项
    private static Sheet exportBillOfQuantities(GCbillOfQuantities bq){
        Sheet sh = wb.createSheet(bq.getName());
        CellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        Row row = sh.createRow(0);
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

        for (int i = 0; i < bq.getQuantitiesList().size(); i++) {
            row = sh.createRow(i+1);
            GCItemQuantities item = bq.getQuantitiesList().get(i);
            row.createCell(0).setCellValue(bq.getName());
            row.createCell(1).setCellValue(item.getGCname());
            row.createCell(2).setCellValue(item.getSerialNumber());
            row.createCell(3).setCellValue(item.getItemCode());
            row.createCell(4).setCellValue(item.getItemName());
            row.createCell(5).setCellValue(item.getUnit());
            row.createCell(6).setCellValue(item.getGcQuantities());
            row.createCell(7).setCellValue(item.getUnitPrice());
            row.createCell(8).setCellValue(item.getComboPrice());
        }
        return sh;
    }

    private static Sheet exportchecklist(List<Integer> checklist){
        Sheet sh = wb.createSheet("筛选表");
        CellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        Row row = sh.createRow(0);
        Cell cell ;
        cell = row.createCell(0);
        cell.setCellValue("工程名称");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("项目编码");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("项目名称");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("计量单位");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("工程数量");
        cell.setCellStyle(style);

        for (int i = 0; i < checklist.size(); i++) {
            row = sh.createRow(i+1);
            GCblockPriceItemQuantities item = blockPriceList.getQuantitiesList().get(checklist.get(i));
            row.createCell(0).setCellValue(item.getGCname());
            row.createCell(1).setCellValue(item.getSerialNumber());
            row.createCell(2).setCellValue(item.getItemCode());
            row.createCell(3).setCellValue(item.getItemName());
            row.createCell(4).setCellValue(item.getUnit());
            row.createCell(5).setCellValue(item.getGcQuantities());
        }

        return  sh ;
    }

    private static Sheet exportSummerize(List<GCbillOfQuantities> companies ,List<Integer> checklist, List<List<Double>> GCUnitPrice, List<List<Double>> GCUnitpoints){
        Sheet sh = wb.createSheet("综合单价");
        CellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        Row row = sh.createRow(0);
        Cell cell ;
        cell = row.createCell(0);
        cell.setCellValue("工程名称");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("项目编码");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("项目名称");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("计量单位");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("工程数量");
        cell.setCellStyle(style);
        //公司名字
        for (int i = 0; i < companies.size(); i++) {
            GCbillOfQuantities c = companies.get(i);
            cell = row.createCell(6+i);
            cell.setCellValue(c.getName());
        }
        cell = row.createCell(6+companies.size());
        cell.setCellValue("基准价");
        cell.setCellStyle(style);
        ///填内容
        int p = 1;
        for (int i = 0; i < checklist.size(); i++) {
            row = sh.createRow(p);
            GCblockPriceItemQuantities item = blockPriceList.getQuantitiesList().get(checklist.get(i));
            row.createCell(0).setCellValue(item.getGCname());
            row.createCell(1).setCellValue(item.getSerialNumber());
            row.createCell(2).setCellValue(item.getItemCode());
            row.createCell(3).setCellValue(item.getItemName());
            row.createCell(4).setCellValue(item.getUnit());
            row.createCell(5).setCellValue(item.getGcQuantities());
//            System.out.println(GCUnitpoints.size());
            for (int j = 0; j < companies.size(); j++) {
                row.createCell(6+j).setCellValue(GCUnitPrice.get(i).get(j));
            }
//            System.out.println(calculateBase(GCUnitPrice.get(i)));
            row.createCell(6+companies.size()).setCellValue(calculateBase(GCUnitPrice.get(i)));
            row = sh.createRow(p+1);
            /////得分
            row.createCell(0).setCellValue("得分: ");
            for (int j = 0; j < companies.size(); j++) {
                row.createCell(6+j).setCellValue(GCUnitpoints.get(i).get(j));
            }

            p+=2;
        }

        row = row = sh.createRow(++p);
        row.createCell(0).setCellValue("总分： ");

        List<Double> summe = summerizePoint();
        for (int j = 0; j < companies.size(); j++) {
            row.createCell(6+j).setCellValue(summe.get(j));
        }
        return sh;
    }




}

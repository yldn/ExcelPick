import sun.misc.GC;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class main {
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
        companies.add(new GCbillOfQuantities("北京", "src/北京"));
        companies.add(new GCbillOfQuantities("九冶", "src/九冶"));
        companies.add(new GCbillOfQuantities("荣丰", "src/荣丰"));
        companies.add(new GCbillOfQuantities("阳光", "src/阳光"));

        companies.get(0).exportExcelAsXSLX("src/北京.xlsx");
        companies.get(1).exportExcelAsXSLX("src/九冶.xlsx");
        companies.get(2).exportExcelAsXSLX("src/荣丰.xlsx");
        companies.get(3).exportExcelAsXSLX("src/阳光.xlsx");
//        System.out.println(companies.get(0).getQuantitiesList().get(0).getGCname());
        //所有分部分项工程量清单计价表 项目汇总为一个list： quantityList

        //如果
        if (companies.get(0).getQuantitiesList().size()> number ){
//              List<Integer> GCchecklist =  pick(companies.get(0).getQuantitiesList().size() , number);
            //测试数组
            for (int i = 0; i < number; i++) {
                GCchecklist.add(i);
            }
            ///罗列单价表
            GCUnitPrice = summerizeUnitPrice();
            //打分表
            GCUnitpoints = calculatePoint();
            ////TEST
            for (int i = 0; i < GCUnitPrice.size(); i++) {
//                System.out.println("单价：");
                System.out.println(Arrays.toString(GCUnitPrice.get(i).toArray()));
                System.out.println("-> "+"Base : "+ Double.valueOf(new DecimalFormat("0.00").format(calculateBase(GCUnitPrice.get(i)))));
                System.out.println(Arrays.toString(GCUnitpoints.get(i).toArray()));
                System.out.println("-----------------------------------");
            }

            ////汇总
            //根据每家公司的总分排序
            List<Double> summe = new ArrayList<Double>();
////            /////4
            for (int i = 0; i < GCUnitpoints.get(0).size(); i++) {
                DecimalFormat df = new DecimalFormat("0.00");
                double sum = 0.0;
                for (int j = 0; j < GCUnitpoints.size(); j++) {
                    sum += GCUnitpoints.get(j).get(i);
                }
                summe.add( Double.valueOf(df.format(sum)) );
            }
            System.out.println("SUMME："+ Arrays.toString(summe.toArray()));
        }
    }

    public static List<List<Double>>calculatePoint(){
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
        DecimalFormat df = new DecimalFormat("0.00");
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
    /////////////
    private static List<Integer> pick (int range , int number){
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
        return sum/companies.size()*0.97;
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


}

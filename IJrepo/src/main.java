import sun.misc.GC;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class main {

//    static GCbillOfQuantities bj = new GCbillOfQuantities("北京", "src/北京");
//    static GCbillOfQuantities jy = new GCbillOfQuantities("九冶", "src/九冶");
//    static GCbillOfQuantities rf = new GCbillOfQuantities("荣丰", "src/荣丰");
//    static GCbillOfQuantities yg = new GCbillOfQuantities("阳光", "src/阳光");
    static List<GCbillOfQuantities> companies = new ArrayList<GCbillOfQuantities>();

    public static void main (String[]args) throws IOException {
        companies.add(new GCbillOfQuantities("北京", "src/北京"));
        companies.add(new GCbillOfQuantities("九冶", "src/九冶"));
        companies.add(new GCbillOfQuantities("荣丰", "src/荣丰"));
        companies.add(new GCbillOfQuantities("阳光", "src/阳光"));

        System.out.println(companies.get(0).getQuantitiesList().get(0).getGCname());
        //所有分部分项工程量清单计价表 项目汇总为一个list： quantityList
//        FileExtractor ext = new FileExtractor("C:\\Users\\Administrator\\Desktop\\北京市市政(1)\\北京市市政\\汉宁路与汉阳路（西新街-天汉大道）供热管道工程Ⅰ标段");
//        System.out.println(companies.get(0).getQuantitiesList().size());
        int number = 30 ;
        if (companies.get(0).getQuantitiesList().size()> number ){

//              List<Integer> GCchecklist =  pick(companies.get(0).getQuantitiesList().size() , number);

            //测试数组
            List<Integer> GCchecklist = new ArrayList<Integer>();
            for (int i = 0; i < number; i++) {
                GCchecklist.add(i);
            }


              System.out.println(Arrays.toString(GCchecklist.toArray()) );
              //比较所有list里checklist对应项
            ///把每个公司每一项的得分记录在list里

            ///===综合单价表
            List<List<Double>> GCUnitPrice = new ArrayList<List<Double>>();
            ////initial
            for (int i = 0; i < GCchecklist.size(); i++) {
                List<Double> points =  new ArrayList<Double>();
                for (int j = 0; j < companies.size(); j++) {
                    points.add(new Double(0.0));
                }
                GCUnitPrice.add(points);
            }
//            System.out.println(GCUnitPrice.get(0).size());

//            //item
            for (int i = 0; i < GCchecklist.size(); i++) {
                //company
                for (int j = 0; j < companies.size(); j++) {
                    List<Double> itempoints = GCUnitPrice.get(i);
                    itempoints.set(j,calculatepoint(discount(calculateOffset(companies.get(j), GCchecklist.get(i), calculateBase(companies, i)))));
                    GCUnitPrice.set(i,itempoints);
                }
            }

            ////TEST
            for (int i = 0; i < GCUnitPrice.size(); i++) {
                System.out.println(Arrays.toString(GCUnitPrice.get(i).toArray()));
            }



            ////汇总
            //根据每家公司的总分排序
            List<Double> summe = new ArrayList<Double>();
            //initial
            for (int i = 0; i < companies.size(); i++) {
                summe.add(new Double(0.0));
            }
//            /////4
            for (int i = 0; i < GCUnitPrice.get(0).size(); i++) {
                double sum = 0.0;
                for (int j = 0; j < GCUnitPrice.size(); j++) {
                    sum += GCUnitPrice.get(j).get(i);
                }
                summe.set(i,sum);
            }

            System.out.println(Arrays.toString(summe.toArray()));


        }

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
    private static double calculateBase(List<GCbillOfQuantities> companies , int i ){
        double sum = 0.0;
        for (GCbillOfQuantities x: companies ) {
            sum += x.getQuantitiesList().get(i).getUnitPrice();
        }
//        System.out.println("Base :"+ sum/companies.size()*0.97);
        return sum/companies.size()*0.97;
    }
    //计算偏差
    private static double calculateOffset(GCbillOfQuantities company , int i , double basePrice ){
//        System.out.println("offset :"+(company.getQuantitiesList().get(i).getUnitPrice()/basePrice - 1 ) * 100);
        return (company.getQuantitiesList().get(i).getUnitPrice()/basePrice - 1 ) * 100 ;
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

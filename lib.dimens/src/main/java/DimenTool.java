
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by cdy on 2016/2/3.
 * 快速生成适配工具类 ,直接运行不成功需手动放入目录文件中
 */
public class DimenTool {

    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        String parentPath = "./lib.dimens/src/main/res/";
        File file = new File(parentPath + "values-mdpi/dimens.xml");
        BufferedReader reader = null;
        StringBuilder hdpi = new StringBuilder();
        StringBuilder xhdpi = new StringBuilder();
        StringBuilder xxhdpi = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字  
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    hdpi.append(start).append(num / 2).append(end).append("\r\n");
                    xhdpi.append(start).append(num / 2).append(end).append("\r\n");
                    xxhdpi.append(start).append(num / 2.8).append(end).append("\r\n");
                } else {
                    hdpi.append(tempString).append("\r\n");
                    xhdpi.append(tempString).append("\r\n");
                    xxhdpi.append(tempString).append("\r\n");
                }
            }
            reader.close();
            String hdpifile = parentPath + "values-hdpi/dimens.xml";
            String xhdpifile = parentPath + "values-xhdpi/dimens.xml";
            String xxhdpifile = parentPath + "values-xxhdpi/dimens.xml";

            //将新的内容，写入到指定的文件中去  
            writeFile(hdpifile, hdpi.toString());
            writeFile(xhdpifile, xhdpi.toString());
            writeFile(xxhdpifile, xxhdpi.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void gen2() {
        StringBuilder w1920 = new StringBuilder();
        float i =0;
        while (i<2000){
            i++;
            System.out.println("<dimen name=\"m" + i + "\">" + i + "dp</dimen>");
        }
    }

    /**
     * 写入方法
     */

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            File xmlFile = new File(file);
            if (!xmlFile.getParentFile().exists()) {
                xmlFile.getParentFile().mkdirs();
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public static void main(String[] args) {
        gen();
    }

}  
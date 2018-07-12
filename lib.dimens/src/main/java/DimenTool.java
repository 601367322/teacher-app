
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
        File file = new File(parentPath + "values/dimens.xml");

        BufferedReader reader = null;
        StringBuilder w1920 = new StringBuilder();

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
                    w1920.append(start).append(num * 2).append(end).append("\r\n");
                } else {
                    w1920.append(tempString).append("");
                }
            }

            reader.close();

            String w1920file = parentPath + "values-w1920dp/dimens.xml";
            //将新的内容，写入到指定的文件中去  

            writeFile(w1920file, w1920.toString());

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
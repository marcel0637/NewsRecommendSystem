package top.mar.recommend.utils;

import java.io.*;

public class JsonUtils {
    public static String readTxt2Json(String path) {
        System.out.println("path:"+path);
        File file = new File(path);
        String jsonStr = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            isr = new InputStreamReader(fis, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(isr);
        String line = null;
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonStr += line;
        }
        //System.out.println(jsonStr);
        return jsonStr;
    }
}

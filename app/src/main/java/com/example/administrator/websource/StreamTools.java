package com.example.administrator.websource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/12/11.
 */
public class StreamTools {
    public static String readStream(InputStream in) throws IOException {
        int len = -1;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while ((len=in.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        in.close();
        String content = new String(baos.toByteArray());

        return content;
    }
}

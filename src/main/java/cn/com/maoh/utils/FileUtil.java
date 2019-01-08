package cn.com.maoh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Author: Stephen
 * Create Date: 2018/11/8
 * Version: 1.0
 * Comments:
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取指定文件名的BufferedReader
     * @param fileName
     * @return
     * @throws IOException
     */
    public static BufferedReader getBufferedReader(String fileName)throws IOException{
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
    }

    /**
     * 获取BufferedWriter对象
     * @param fileName
     * @return
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String fileName,boolean append)throws IOException{
        return new BufferedWriter(new FileWriter(fileName,append));
    }

    /**
     * 关闭Reader或Writer
     * @param closeable
     */
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.info("close stream error",e);
        }
    }
}

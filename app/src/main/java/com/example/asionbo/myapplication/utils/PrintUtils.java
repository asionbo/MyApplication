package com.example.asionbo.myapplication.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintUtils {

    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    private static final int LEFT_LENGTH = 20;

    private static final int RIGHT_LENGTH = 12;

    /**
     * 左侧汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 11;

    /**
     * 小票打印菜品的名称，上限调到8个字
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;

    private static OutputStream outputStream = null;
    private static OutputStreamWriter writer = null;

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static void setOutputStream(OutputStream outputStream) {
        PrintUtils.outputStream = outputStream;
        try {
            writer = new OutputStreamWriter(outputStream, "gbk");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public static void printText(String text) {
        try {
            byte[] data = text.getBytes("gbk");
//            outputStream.write(data, 0, data.length);
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public static void selectCommand(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 复位打印机
     */
    public static final byte[] RESET = {0x1b, 0x40};


    public static final byte[] SPACE = {0x1b, 0x0a};

    /**
     * 左对齐
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x30};

    /**
     * 中间对齐
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x31};

    /**
     * 右对齐
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x32};

    /**
     * 选择加粗模式
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * 取消加粗模式
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * 宽高加倍
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * 宽加倍
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * 高加倍
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * 字体不放大
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};

    /**
     * 设置默认行间距
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};

    /**
     * 设置行间距
     */
	public static final byte[] LINE_SPACING = {0x1b, 0x32};//{0x1b, 0x33, 0x14};  // 20的行间距（0，255）


//	final byte[][] byteCommands = {
//			{ 0x1b, 0x61, 0x00 }, // 左对齐
//			{ 0x1b, 0x61, 0x01 }, // 中间对齐
//			{ 0x1b, 0x61, 0x02 }, // 右对齐
//			{ 0x1b, 0x40 },// 复位打印机
//			{ 0x1b, 0x4d, 0x00 },// 标准ASCII字体
//			{ 0x1b, 0x4d, 0x01 },// 压缩ASCII字体
//			{ 0x1d, 0x21, 0x00 },// 字体不放大
//			{ 0x1d, 0x21, 0x11 },// 宽高加倍
//			{ 0x1b, 0x45, 0x00 },// 取消加粗模式
//			{ 0x1b, 0x45, 0x01 },// 选择加粗模式
//			{ 0x1b, 0x7b, 0x00 },// 取消倒置打印
//			{ 0x1b, 0x7b, 0x01 },// 选择倒置打印
//			{ 0x1d, 0x42, 0x00 },// 取消黑白反显
//			{ 0x1d, 0x42, 0x01 },// 选择黑白反显
//			{ 0x1b, 0x56, 0x00 },// 取消顺时针旋转90°
//			{ 0x1b, 0x56, 0x01 },// 选择顺时针旋转90°
//	};


    /**
     * 初始化打印机
     */
    public static void initPrinter(){
        try {
            outputStream.write(RESET);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置行间距
     * @param n  0≤n≤255
     */
    public static void setLineSpace(int n){
        try {
            outputStream.write(new byte[]{27, 51, (byte) n});
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入一条横线
     *
     * @throws IOException 异常
     */
    public static void printLine(){
        int length = 16;
        String line = "";
        while (length > 0) {
            line += "─";
            length--;
        }
        printText(line+"\n");
    }

    /**
     *打印空行
     */
    public static void printNewLine(){
        try {
            outputStream.write("\n".getBytes("GB2312"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static void printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        printText(sb.toString());
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static void printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
//        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
//            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
//        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.append(rightText);
        printText(sb.toString());
    }

    /**
     * 打印四列
     * @param leftText
     * @param mid1
     * @param mid2
     * @param rightText
     */
    public static void printFourData(String leftText,String mid1,String mid2,String rightText){
        try {
            StringBuilder sb = new StringBuilder();
            int left = getBytesLength(leftText);
            int m1= getBytesLength(mid1);
            int m2 = getBytesLength(mid2);
            int right = getBytesLength(rightText);
            sb.append(leftText);
            for(int i = 0; i < LEFT_LENGTH - left - m1 - 4; i++){
                sb.append(" ");
            }
            sb.append(mid1);
            sb.append("    ");
            sb.append(mid2);
            for(int i = 0; i < RIGHT_LENGTH - right - m2 - 2; i++){
                sb.append(" ");
            }
            sb.append(rightText);
            printText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化菜品名称，最多显示MEAL_NAME_MAX_LENGTH个数
     *
     * @param name
     * @return
     */
    public static String formatMealName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > MEAL_NAME_MAX_LENGTH) {
            return name.substring(0, 8) + "..";
        }
        return name;
    }

    /**
     * 正常字体居中打印
     * @param text
     * @return
     */
    public static void printNormalMidText(String text){
        selectCommand(NORMAL);
        StringBuilder sb = new StringBuilder();
        //计算文字长度
        int length = getBytesLength(text);

        if (length > LINE_BYTE_SIZE){
            printText(text);
            return;
        }
        //计算左边打印的空格长度
        int leftSpace = (LINE_BYTE_SIZE - length) / 2;

        for (int i=0;i<leftSpace+1;i++){
            sb.append(" ");
        }
        sb.append(text);
        printText(sb.toString());
    }

    /**
     * 两倍宽高字体居中打印
     * @param text
     * @return
     */
    public static void printBigMidText(String text){
        selectCommand(DOUBLE_HEIGHT_WIDTH);
        StringBuilder sb = new StringBuilder();
        //计算文字长度
        int length = getBytesLength(text);

        if (length > LINE_BYTE_SIZE){
            printText(text);
            return;
        }
        //计算左边打印的空格长度
        int leftSpace = (LINE_BYTE_SIZE - length*2) / 2;

        for (int i=0;i<leftSpace/2;i++){
            sb.append(" ");
        }
        sb.append(text);
        printText(sb.toString());
    }

    /**
     * 打印图片
     * @param bmp
     * @param width 宽 最好为24倍数
     * @param height 高  最好为24倍数
     */
    public static void printBitmap(Bitmap bmp,int width,int height){
        try {
        bmp = compressPic(bmp,width,height);
        byte[] bmpByteArray = draw2PxPoint(bmp);
            outputStream.write(bmpByteArray);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private static byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;

        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private static Bitmap compressPic(Bitmap bitmapOrg,int newWidth,int newHeight) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    /**
     * 打印二维码
     * @param qr
     */
    public static void printQR(Bitmap qr){
        sendCode("0A");
        byte[] qrP = draw2PxPoint(qr);
        try {
            outputStream.write(qrP);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        initPrinter();
        printNewLine();
        printNewLine();
    }

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }

                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * 打印条形码
     * @param barcode
     */
    public static void printBarcode(String barcode){
        try {
            int codeLength = getCodeLength(barcode);
            LogUtils.e("codeLength="+codeLength);
            writer.write(0x1B);
            writer.write(97);
            //设置条码居中
            writer.write(1);

            //设置条码宽度
            writer.write(0x1D);
            writer.write('w');
            writer.write(2);//默认是2  2-6 之间
            writer.flush();

            //设置条形码的高度
            writer.write(0x1D);
            writer.write('h');
            writer.write(60);//默认是60
            writer.flush();

            //条码注释打印在条码下方
            writer.write(0x1D);
            writer.write(72);
            writer.write(2);

            writer.write(0x1D);
            writer.write('k');
//            //选择code128
//            writer.write(73);
            //选择code93
            writer.write(72);
            //设置字符个数
            writer.write(codeLength);
            //使用CODEB来打印
//            writer.write(123);
//            writer.write(66);
            //条形码内容
            writer.write(barcode);
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static int getCodeLength(String code){
        int count_abc=0, count_num=0, count_oth=0;
        char[] chars = code.toCharArray();
        //判断每个字符
        for(int i = 0; i < chars.length; i++){
            if((chars[i] >= 65 && chars[i] <= 90) || (chars[i] >= 97 && chars[i] <=122)){
                count_abc++;
            }else if(chars[i] >= 48 && chars[i] <= 57){
                count_num++;
            }else{
                count_oth++;
            }
        }
        int total = count_abc+count_oth+count_num;
        return total;
    }


    /**
     * 写入字符缓冲区
     * @param message
     */
    private static void sendCode(String message) {
        byte[] send = message.getBytes();    //发送给本地文字框的数据缓冲区
        byte[] buffer = message.substring(message.length() / 2).getBytes();    //发送给对端蓝牙设备的数据缓冲区
        byte temp = 0;                //转换十六进制的临时变量
        Boolean unite = false;        //转换十六进制的临时变量

        for (int j = 0; j < message.length() / 2; j++) {     //初始化发送缓冲区
            buffer[j] = 0;
        }
        for (int i = 0; i < message.length(); i++)    //对十六进制发送数据组合
        {
            if (send[i] != ' ') {
                if (send[i] >= '0' && send[i] <= '9') {
                    temp = (byte) (send[i] - '0');
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                } else if (send[i] >= 'a' && send[i] <= 'f') {
                    temp = (byte) (send[i] - 'a' + 10);
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                } else if (send[i] >= 'A' && send[i] <= 'F') {
                    temp = (byte) (send[i] - 'A' + 10);
                    if (unite) {
                        buffer[i / 2] += temp;
                        unite = !unite;
                    } else {
                        buffer[i / 2] = (byte) (temp << 4);
                        unite = !unite;
                    }
                }
            }
        }
        try {
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String prasePhoneNumber(String number){
        String req = "^\\d{11}$";
        Pattern compile = Pattern.compile(req);
        Matcher matcher = compile.matcher(number);
        if (matcher.matches()){
            char[] chars = number.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(chars[0]);
            sb.append(chars[1]);
            sb.append(chars[2]);
            sb.append("****");
            sb.append(chars[7]);
            sb.append(chars[8]);
            sb.append(chars[9]);
            sb.append(chars[10]);
            return sb.toString();
        }else {
            return number;
        }
    }
}

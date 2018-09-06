package com.example.newui_smartdrawer.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;

//new serial port
public class SerialPortInterface extends AppCompatActivity {
    FileOutputStream mOutputStream = null;
    FileInputStream mInputStream = null;
    SerialPort sp;
    Context context;


    public SerialPortInterface(Context context, String serialPortID,int baudrate) {
        this.context = context;
        try {
            sp = new SerialPort(new File(serialPortID), baudrate, 0);
            mOutputStream = (FileOutputStream) sp.getOutputStream();
            mInputStream = (FileInputStream) sp.getInputStream();
        } catch (SecurityException e) {
            if(baudrate==38400)
                Toast.makeText(this.context,"锁控板连接异常", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this.context,"LoRa连接异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            if(baudrate==38400)
                Toast.makeText(this.context,"锁控板连接异常", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this.context,"LoRa连接异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 计算产生校验码
     * @param data 需要校验的数据
     * @return 校验码
     */
    public static String Make_CRC(byte[] data, int start,int num) {
        byte[] buf = new byte[num];// 存储需要产生校验码的数据
        for (int i = 0; i < num; i++) {
            buf[i] = data[start+i];
        }
        int len = num;
        int crc = 0xFFFF;//16位
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < 0) {
                crc ^= (int) buf[pos] + 256; // XOR byte into least sig. byte of
                // crc
            } else {
                crc ^= (int) buf[pos]; // XOR byte into least sig. byte of crc
            }
            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else
                    // Else LSB is not set
                    crc >>= 1; // Just shift right
            }
        }
        String c = Integer.toHexString(crc);
        if (c.length() == 4) {
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            c = "0" + c;
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 2) {
            c = "0" + c.substring(1, 2) + "0" + c.substring(0, 1);
        }
        return c;
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
    * 输入字符串w 返回lrc校验部分
    * @param  w=需要传输的信息  ID是地址
    * @return finaldata
    */
    public String Lrc(String w) {
        char[] m = w.toCharArray();
        int x = 0;
        int length = m.length;
        int[] lrcdata = new int[length];
        for (int i = 0; i < length; i++) {
            if (m[i] >= 'A')
                lrcdata[i] = m[i] - 'A' + 10;
            else
                lrcdata[i] = m[i] - '0';
        }
        for (int i = 0; i < length / 2; i++) {
            x += (lrcdata[2 * i] * 16 + lrcdata[2 * i + 1]);
        }
        x = x % 256;
        x = 256 - x;
        String finaldata = Integer.toHexString(x % 256).toUpperCase();
        return finaldata;
    }

    /**
     * 发送数据
     *
     * @param sendData
     * @return
     */
    public int UART_Send(String sendData) {
        if(mOutputStream == null){
            Toast.makeText(this.context,"无法发送串口指令", Toast.LENGTH_SHORT).show();
            return -1;
        }

        byte[] mBuffer = (sendData + "\r\n").getBytes();
        try {
            mOutputStream.write(mBuffer);
        } catch (IOException e) {
            Toast.makeText(this.context,"发送串口指令失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return -1;
        }
        return 1;
    }


    /**
     * 发送开锁指令
     *
     * @param DID：锁控板地址号
     * @param drawerNu：抽屉号
     */
    public int sendOpenLock(int DID, int drawerNu) {
        StringBuilder drawerData = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (drawerNu == i + 1)
                drawerData.append("1");
            else
                drawerData.append("0");
        }
        String str = String.format("%02x", DID).toUpperCase() + "21" + drawerData;
        String sendData = ":" + str + Lrc(str);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 发送查询锁状态
     *
     * @param DID ：锁控板地址号, 柜子（板子）编号
     */
    public String sendGetStat(int DID) {
        String str = String.format("%02x", DID).toUpperCase() + "11";
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        String result;
        result = readData();
        return result;
    }

    /**
     * 控制LED等
     *
     * @param DID：锁控板地址号
     * @param data：1     开启；0关闭
     */
    public int sendLED(int DID, int data) {
        String str = String.format("%02x", DID) + "41" + String.format("%02x", data);
        String sendData = ":" + str + Lrc(str);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 控制风扇
     *
     * @param DID：锁控板地址号
     * @param data：1     开启；0关闭
     */
    public int sendFan(int DID, int data) {
        String str = String.format("%02x", DID).toUpperCase() + "31" + String.format("%02x", data);
        String sendData = ":" + str + Lrc(str);
        Log.d("data", sendData);
        UART_Send(sendData);
        return 1;
    }

    /**
     * 发送读取重量指令
     * @return
     */
    public int GetLoad(){
        byte[] data = new byte[8];
        data[0] = 0x00;
        data[1] = 0x03;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x00;
        data[5] = 0x02;
        String buf = Make_CRC(data,0,6);
        byte[] result = hexStringToBytes(buf);
        data[6] = result[0];
        data[7] = result[1];
        try {
            mOutputStream.write(data);
        } catch (IOException e) {
            Log.i("test", "发送失败");
            e.printStackTrace();
            return -1;
        }
        int load = readLoad();
        Log.d("load", String.valueOf(load));
        return load;
    }

    public int readLoad(){
        int Error = -1;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(50);
                if (mInputStream.available() > 0) {
                    if (mInputStream != null) {
                        byte[] buffer = new byte[2048];
                        int size = mInputStream.read(buffer);
                        if (size > 8){
                            int head = 0;
                            while (head < size-2){
                                if (buffer[head] != 0||buffer[head+1] != 3||buffer[head+2] != 4)
                                {
                                    head++;
                                    continue;
                                }
                                if (size-head > 8){
                                    String crc = Make_CRC(buffer,head,7);
                                    byte[] result = hexStringToBytes(crc);
                                    if (buffer[head+7] == result[0]&&buffer[head+8] == result[1]) {
                                        int load = (buffer[head+4]&0xff)+((buffer[head+3]&0xff)<<8)+((buffer[head+6]&0xff)<<16)+((buffer[head+5]&0xff)<<32);
                                        return load;
                                    }
                                    else{
                                        Log.d("SerialPort","校验码错误");
                                        return Error;
                                    }
                                }
                                else{
                                    return Error;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return Error;
    }

    /**
     * 接收数据
     * @param
     * @return
     */
    public String readData() {
        String readDatas = null;
        String Error = "";
        String lrc;
        byte[] buffer2 ;
        byte[] buffer3 = new byte[2];

        if(mInputStream ==  null){
            Toast.makeText(this.context,"无法读取串口数据", Toast.LENGTH_SHORT).show();
            return Error;
        }

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(50);
                if (mInputStream.available() > 0) {
                    if (mInputStream != null) {
                        byte[] buffer = new byte[2048];
                        int size = mInputStream.read(buffer);
                        if (size > 21) {
                            int head = 0;
                            while (head < size) {
                                if (buffer[head] != ':') {
                                    head++;
                                    continue;
                                }
                                if (size - head - 1 > 20) {
                                    buffer2 = new byte[20];
                                    for (int j = 0; j < 20; j++) {
                                        buffer2[j] = buffer[head + j + 1];
                                    }
                                    buffer3[0] = buffer[head + 21];
                                    buffer3[1] = buffer[head + 22];
                                    readDatas = new String(buffer2);
                                    lrc = new String(buffer3);
                                    if (lrc.equals(Lrc(readDatas))) {
                                        String str = readDatas.substring(4);
                                        return str;
                                    } else {
                                        return Error;
                                    }
                                } else
                                    return Error;
                            }
                            break;
                        } else
                            break;
                    }
                }
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }
    public String readTHData() {
        String readDatas = null;
        String Error ="" ;
        String lrc;
        byte[] buffer2 ;
        byte[] buffer3 = new byte[1];
        byte[] buffer4 = new byte[1];
        byte[] buffer5 = new byte[1];
        byte[] buffer6 = new byte[1];
        if(mInputStream ==  null){
//            Toast.makeText(this.context,"无法读取串口数据", Toast.LENGTH_SHORT).show();
            return Error;
        }

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(50);
                if (mInputStream.available() > 0) {
                    if (mInputStream != null) {
                        byte[] buffer = new byte[6];
                        int size = mInputStream.read(buffer);
                        if(size>5)
                        {
                            byte[] buffer7 = new byte[2];
                            byte[] buffer8 = new byte[2];
                            buffer3[0]=buffer[0];
                            buffer4[0]=buffer[1];
                            buffer5[0]=buffer[3];
                            buffer6[0]=buffer[4];
                            System.arraycopy(buffer3,0,buffer7,0,1);
                            System.arraycopy(buffer4,0,buffer7,1,1);
                            System.arraycopy(buffer5,0,buffer8,0,1);
                            System.arraycopy(buffer6,0,buffer8,1,1);

                            double template = Integer.parseInt(bytesToHexFun1(buffer7),16)*175.0/65535.0-45.0;
                            double rh = Integer.parseInt(bytesToHexFun1(buffer8),16)*100.0/65535.0;

                            String str =  String.format("%.1f", template);
                           String str1 = String.format("%.1f", rh) ;
                            return  str+str1;



                        }

                    }
                }
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }


    public  String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }



}
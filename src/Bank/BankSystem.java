package Bank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

//银行系统--负责注册，登录，用户信息存储
public class BankSystem implements Runnable {
    //使用哈希列表存储银行的银行卡号信息
    public static HashMap<Integer, BankCard> bankCards = new HashMap<>();
    private User user;


    //注册账户方法
    public static void register(int cardNo, String password,String userName,String userID) {
        //判断该卡号是否存在
        if (bankCards.containsKey(cardNo)) {
            System.out.println("该账户已经存在，麻烦重新选择业务");
            return;
        }
        //注册卡信息构造函数
        BankCard bankCard = new BankCard(cardNo, password, 0.00,userName,userID);
        bankCards.put(cardNo, bankCard);
        System.out.println("注册成功！");
    }

    //登录
    public static BankCard login(int cardNo, String password) {
        if (bankCards.size() == 0) {
            System.out.println("当前不存在任何银行卡信息，建议及时注册！");
            return null;
        }
        //判断该账户是否存在
        if (bankCards.containsKey(cardNo)) {
            if (password.equals(bankCards.get(cardNo).getPassword())) {
                // 如果密码正确，则登录成功
                return bankCards.get(cardNo);
            } else {
                System.out.println("登录失败，该密码错误！");
                //否则就返回null
                return null;
            }
        } else {
            System.out.println("登录失败，该账户不存在!");
        }
        return null;
    }

    // 注销账户的方法
    public static void logout(int cardNo) {
        try {
            if (bankCards.containsKey(cardNo)) {
                bankCards.remove(cardNo);
                System.out.println("账户注销成功！");
            } else if (bankCards.get(cardNo).getMoney() > 0) {
                System.out.println("账户注销失败，账户余额不为零！");

            } else {
                System.out.println("账户注销失败，该账户不存在！");
            }
        } catch (NullPointerException e) {
            System.out.println("账户注销失败，该账户不存在！");
        }
    }


    //进入银行界面
    @Override
    public void run() {
        if (Main.operator == 100) {
            synchronized (Main.lock) {


            }
        }
    }
    //系统记录交易信息
    public static void record ( double money, int cardNo, int i){
        //记录存款
        String fileName = "Bank/transaction.txt";
        //   添加交易时间信息
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date()); // 获取当前时间
        if (i == 1 && money>0) {

            try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                // 使用true参数以追加模式打开文件，以便将新的存款信息附加到文件末尾


                String depositInfo1 = "时间: " + timestamp + "，卡号: " + cardNo + ", 本次存款: " + money + ",当前余额：" + bankCards.get(cardNo).getMoney() + "\n";
                fileWriter.write(depositInfo1);
                System.out.println("存款信息已写入文件：" + fileName);
            } catch (IOException e) {
                System.err.println("写入文件时发生错误：" + e.getMessage());
            }
            //记录取款
        } else if (i == 2) {
            try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                // 使用true参数以追加模式打开文件，以便将新的存款信息附加到文件末尾
                String depositInfo2 = "时间: " + timestamp + "，卡号: " + cardNo + ", 本次取款: " + money + ",当前余额：" + bankCards.get(cardNo).getMoney() + "\n";
                fileWriter.write(depositInfo2);
                System.out.println("取款信息已写入文件：" + fileName);
            } catch (IOException e) {
                System.err.println("写入文件时发生错误：" + e.getMessage());
            }
        }
    }

    public static void record ( double money, int cardNo, int destID, int i) {
        //记录转账
        String fileName = "./Bank/transaction.txt";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date()); // 获取当前时间
        //判断写入正确金额写入条件
        if(money >=0){
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            // 使用true参数以追加模式打开文件，以便将新的存款信息附加到文件末尾
            String depositInfo3 = "时间: " + timestamp + "，卡号: " + cardNo + "，转入卡号:" + destID + "，本次转账: " + money + ",当前余额：" + bankCards.get(cardNo).getMoney() + "\n";
            fileWriter.write(depositInfo3);
            System.out.println("转账信息已写入文件：" + fileName);
        } catch (IOException e) {
            System.err.println("写入文件时发生错误：" + e.getMessage());
        }
    }   else{//不输出？？？
            System.out.println("输入金额错误，写入信息失败！");
        }
    }
}
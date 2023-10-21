package Bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;

import static Bank.Cashier.scanner;
import static Bank.Main.lock;
import static Bank.Main.operator;

public class  BankManager implements Runnable{
    private static final String FILE_NAME = "Bank/transaction.txt";
    private static final String DELETE_PASSWORD = "player877";



    public void run() {
        while (true){
            Main.lock.lock();
        if (Main.operator == 2) {
                int manageID;
                String managePassWord;
                System.out.println("请输入管理员账号：");
                manageID = scanner.nextInt();
                System.out.println("请输入管理员密码：");
                managePassWord = scanner.next();
                if ((manageID == 666) && (managePassWord.equals("877"))) {
                    System.out.printf("***管理员：%d登录成功***", manageID);
                    while (true) {
                        System.out.println("");
                        System.out.println("***请输入您需要的操作***");
                        System.out.println("1.添加账户");
                        System.out.println("2.查看所有账户信息");
                        System.out.println("3.删除账户");
                        System.out.println("4.查询指定账户信息");
                        System.out.println("5.查看交易记录");
                        System.out.println("6.清空交易记录");
                        System.out.println("7.退出管理员系统");
                        int num = scanner.nextInt();
                        if (num == 7) {
                            Main.operator = 0;
                            Main.conditionMain.signal();
                            try {
                                Main.conditionManage.await();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        BankManager bankManager = new BankManager();
                        bankManager.manageOperate(num);
                    }
                } else {
                    System.out.println("密码输入错误");
                }


        }
        lock.unlock();
    }
    }




    public void manageOperate(int num){

            switch (num){
                case 1://添加账户
                    addUser();
                    break;
                case 2://查看所有账户信息
                    printAllBankCardInfo();
                    break;
                case 3://删除账户
                    deleteUser();
                    break;
                case 4://查询指定账户信息
                    getBankCardInfo();
                    break;
                case 5://查看交易记录
                    readTransactionFile();
                    break;
                case 6://清空交易记录
                    clearTransactionFile();
                    break;
                default:
                    break;
            }

    }
    public void readTransactionFile() {
        String fileName = "./Bank/transaction.txt"; // 指定要查看的文件名
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("交易记录文件内容：");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("读取文件时发生错误：" + e.getMessage());
        }
    }
    // 清空交易记录文件的信息
    public void clearTransactionFile() {
        Scanner scanner = new Scanner(System.in);
        int chance = 3;
        while (chance > 0) {
            System.out.println("请输入删除权限密码:");
            String enteredPassword = scanner.next();

            if (enteredPassword.equals(DELETE_PASSWORD)) {
                clearFile(FILE_NAME);
                System.out.println("交易记录文件已清空：" + FILE_NAME);
                break;
            } else {
                System.out.println("权限密码错误！您还有" + (chance - 1) + "次机会");
            }
            chance--;
        }

    }
    //删除文件方法
    private void clearFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            // 打开文件并清空内容
        } catch (IOException e) {
            System.err.println("清空文件时发生错误：" + e.getMessage());
        }
    }

    public void printAllBankCardInfo() {
        System.out.println("所有银行卡信息：");
        for (Map.Entry<Integer, BankCard> entry : BankSystem.bankCards.entrySet()) {
            int cardNo = entry.getKey();
            BankCard card = entry.getValue();
            System.out.println("银行卡号: " + cardNo);
            System.out.println("余额: " + card.getMoney());
            System.out.println("客户姓名: " + BankSystem.bankCards.get(cardNo).getUserName());
            System.out.println("客户身份证: " + BankSystem.bankCards.get(cardNo).getUserID());
            System.out.println("----------------------");
        }
    }
    //查找卡号信息--通过卡号查找
    public void getBankCardInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要查询的卡号：");
        int cardNo = scanner.nextInt();
        if (BankSystem.bankCards.containsKey(cardNo)) {
            BankSystem.bankCards.get(cardNo);
            System.out.println("银行卡号: " + cardNo);
           // System.out.println("密码: " + BankSystem.bankCards.get(cardNo).getPassword());
            System.out.println("余额: " + BankSystem.bankCards.get(cardNo).getMoney());
            System.out.println("客户姓名: " + BankSystem.bankCards.get(cardNo).getUserName());
            System.out.println("客户身份证: " + BankSystem.bankCards.get(cardNo).getUserID());
            System.out.println("----------------------");

        } else {
            System.out.println("银行卡号 " + cardNo + " 不存在");
        }
    }
    //增加用户信息
    public void addUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要添加的卡号,密码,客户姓名,客户身份证信息");
        int cardNo = scanner.nextInt();
        String password = scanner.next();
        String userName = scanner.next();
        String userID = scanner.next();
        if (!BankSystem.bankCards.containsKey(cardNo)) {
            BankCard bankCard = new BankCard(cardNo, password,0.00,userName,userID);
            BankSystem.bankCards.put(cardNo, bankCard);
            System.out.println("用户添加成功！");
        } else {
            System.out.println("用户已存在，无法添加重复用户。");
        }
    }
    //删除用户信息
    public void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要删除的卡号");
        int cardNo = scanner.nextInt();
        if (BankSystem.bankCards.containsKey(cardNo)) {
            BankSystem.bankCards.remove(cardNo);
            System.out.println("用户删除成功！");
        } else {
            System.out.println("用户不存在，无法删除。");
        }
    }


}

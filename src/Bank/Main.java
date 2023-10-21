package Bank;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Bank.Cashier.scanner;

public class Main {

     static volatile int operator = 0;

    static Lock lock = new ReentrantLock();

    static volatile Condition conditionMain= lock.newCondition();
    static volatile Condition conditionUser= lock.newCondition();
    static volatile Condition conditionManage= lock.newCondition();




    public static void main(String[] args) {




        //测试账户
        BankCard Test1 = new BankCard(1,"a",10000,"张三","121121212");
        BankSystem.bankCards.put(1,Test1);
        BankCard Test2 = new BankCard(2,"b",1000,"李四","1210313");
        BankSystem.bankCards.put(2,Test2);

        //打开银行的系统界面
        BankSystem bankSystem = new BankSystem();
        Thread threadSystem = new Thread(bankSystem);
        threadSystem.start();

        //启动用户线程
        User user = new User();
        Thread threadUser = new Thread(user);
        threadUser.start();

        //启动管理员线程
        BankManager bankManager = new BankManager();
        Thread threadManage = new Thread(bankManager);
        threadManage.start();

        while(true)
        {
            lock.lock();
            if(operator == 0) {
                //lock.lock();--这是一个bug--QAQ
                    try {
                        System.out.println("***欢迎来到本银行，请选择您的业务：***");
                        System.out.println("1.登录账户");
                        System.out.println("2.注册账户");
                        System.out.println("3.注销账户");
                        System.out.println("4.登录管理员账户");
                        System.out.println("5.退出");
                        System.out.println("请输入您的选择：");

                        int key = scanner.nextInt();
                        switch (key) {
                            case 1:
                                operator = 1;
                                conditionMain.await();
                                conditionUser.signal();

                                break;
                            case 2:
                                int cardNo;
                                String password;
                                String userName;
                                String userID;
                                System.out.println("请输入您的银行卡账号：");
                                cardNo = scanner.nextInt();
                                System.out.println("请输入您的银行卡密码：");
                                password = scanner.next();
                                System.out.println("请输入您的客户姓名：");
                                userName = scanner.next();
                                System.out.println("请输入您的客户身份证：");
                                userID = scanner.next();
                                BankSystem.register(cardNo, password,userName,userID);
                                break;
                            case 3:
                                System.out.println("请输入您的银行卡账号：");
                                cardNo = scanner.nextInt();
                                BankSystem.logout(cardNo);
                                break;
                            case 4:
                                operator = 2;
                                conditionMain.await();
                                conditionManage.signal();
                                break;
                            case 5:
                                System.out.println("尊敬的用户，您已成功退出银行系统！欢迎下次光临~");
                                return;
                            default:
                                System.out.println("请重新输入正确选项！");

                        }
                    } catch (InputMismatchException e) {
                        System.out.println("输入卡号应为数字，请输入正确的卡号。");
                        scanner.nextLine(); // 清除输入缓冲
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }



               lock.unlock();

            }


        }




    }

}






package Bank;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;

import static Bank.Cashier.scanner;
import static Bank.Main.lock;

//操作
public class User implements Runnable {


    private static User user;
    private BankCard bankCard;
    public User(){
        this.bankCard = bankCard;
    }


    @Override
    public void run() {
    while(true){
        Main.lock.lock();
        if (Main.operator == 1) {
            while (true){
            scanner = new Scanner(System.in);
            int cardNo;
            String password;
            System.out.println("请输入您的银行卡账号：");
            cardNo = scanner.nextInt();
            System.out.println("请输入您的银行卡密码：");
            password = scanner.next();
            //填入信息
            BankCard card = BankSystem.login(cardNo, password);
            if (card != null) {
                System.out.printf("***卡号：%d登录成功***", cardNo);
                while (true) {
                    System.out.println("");
                    System.out.println("***请输入您需要的业务***");
                    System.out.println("1.存钱");
                    System.out.println("2.取钱");
                    System.out.println("3.转账");
                    System.out.println("4.查询余额");
                    System.out.println("5.退卡");
                    int num = scanner.nextInt();
                    if (num == 5) {
                        Main.operator = 0;
                        Main.conditionMain.signal();
                        try {
                            Main.conditionUser.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    try {
                        User.userOperate(num, card);
                    } catch (InputMismatchException e) {
                        System.out.println("输入金额应为数字，请输入正确的金额。");
                    }
                }
            }
            }

        }
        lock.unlock();
    }
    }




    // 用户基本操作--存款--取款--转账--查余额
    public static void userOperate( int num, BankCard card) {
        switch (num) {
            case 1://存款
                user.deposit(card);
                break;

            case 2://取款--static
                user.withdraw();
                Cashier.withdraw(card, user);
                break;

            case 3://转账
                user.transfer(card);
                break;
            case 4://查余额
                user.select(card);
                break;
            case 5 :
                user.changePW(card);
            default:
                break;
        }
    }

    //存款
    public static void deposit(BankCard bankCard) {
        System.out.println("请输入您的存入金额：");
        Scanner  scanner = new Scanner(System.in);
        double money = scanner.nextDouble();
        if (money > 0) {
            bankCard.setMoney(bankCard.getMoney() + money);
            System.out.println("存款成功，本次存款金额为："+money);
            select(bankCard);
        } else {
            System.out.println("请输入正确的金额!");
        }
        int cardNo = bankCard.getCardNo();
        BankSystem.record (money,cardNo,1);
    }

    //取款
    public static void withdraw() {
        System.out.println("请输入您要取出的金额：");

    }

    //查询余额--设定format格式输出
    public static void select(BankCard card) {
        DecimalFormat df = new DecimalFormat("#.00");
        double balance = card.getMoney();
        if (balance == 0.0) {
            System.out.println("您的余额为： 0.00");
        } else {
            System.out.println("您的余额为： " + df.format(balance));
        }
    }

    //转账
    public static void transfer(BankCard bankCard) {
        //限制输出账号的次数为3次
        int number = 3;
        while (number > 0) {
            System.out.println("请输入对方的卡号：");
            Scanner  scanner = new Scanner(System.in);
            int destID = scanner.nextInt();//目的账号
            Map<Integer, BankCard> bankCards = BankSystem.bankCards;
            if (bankCards.containsKey(destID)) {
                //查找目的账户存在，再进行转账操作
                BankCard transferAccounts = bankCards.get(destID);
                System.out.println("请输入您要转账的金额：");
                double money = scanner.nextDouble();
                if (money <= bankCard.getMoney()&&money>0) {
                    //修改账户余额
                    bankCard.setMoney(bankCard.getMoney() - money);
                    //修改对方账户余额
                    transferAccounts.setMoney(transferAccounts.getMoney() + money);
                    System.out.println("转账成功，本次转账金额为："+money);
                    select(bankCard);
                    BankSystem.record(money,bankCard.getCardNo(),destID,3);
                    return;
                } else if(money<=0){
                    System.out.println("请输入正常的转账金额！");

                }   else{
                    System.out.println("很抱歉，您的余额不足！");
                    System.out.println("您还有" + (number - 1) + "次机会，请重新输入转账账户");
                    number--;
                }
            }

        }
    }

    //修改密码
    public static void changePW(BankCard bankCard) {
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("请输入要修改的密码");
        String PW = scanner.next();
        bankCard.setPassword(PW);
        System.out.println("修改成功！");
    }
}


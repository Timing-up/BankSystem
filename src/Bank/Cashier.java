package Bank;

import java.util.Scanner;

    public class Cashier {

        static Scanner scanner =  new Scanner(System.in);
        //出纳员执行取款--静态方法，Bank system 可以通过类名来访问
        public static void withdraw(BankCard bankCard, User user) {
        double money = scanner.nextDouble();
        //取款金额大于0.且大于余额
        if (money > 0 && money <= bankCard.getMoney()) {
            //修改账户金额
            bankCard.setMoney(bankCard.getMoney() - money);
            System.out.println("取款成功，本次取款金额为："+money);
            user.select(bankCard);
            BankSystem.record(money, bankCard.getCardNo(), 2);
        } else if (money > bankCard.getMoney()) {
            System.out.println("很抱歉，您的余额不足！请先至少存款"+ (money - bankCard.getMoney()));
        } else {
            System.out.println("请输入正确取款金额！");
        }
    }
    }

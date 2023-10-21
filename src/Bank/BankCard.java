package Bank;

//****************  银行卡信息    ***************
public class BankCard {
    private int cardNo;//卡号
    //密码
    private String passWord;//密码
    private double money;//余额

    private String userName;//客户姓名

    private String userID;//客户身份证


    private boolean VIP;//会员

    //银行卡信息构造函数——初始化
//    public BankCard(int cardNo, double initialBalance) {
//        this(0, "123456",0.00);
//    }

    //银行卡信息构造函数——重载
    public BankCard(int cardNo, String password, double v,String userName,String userID) {
        this.cardNo = cardNo;
        this.passWord = password;
        //默认该账户初始值为0
        this.money = v;
        this.userName = userName;
        this.userID = userID;

    }

    //****************   Get--Set  **************************
    //设置密码
    public void setPassword(String password) {
        this.passWord = password;
    }

    public String getPassword() {
        return this.passWord;
    }
    //设置余额
    public void setMoney(double money) {

        this.money = money;
    }

    public double getMoney() {
        return this.money;
    }

    public int getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}


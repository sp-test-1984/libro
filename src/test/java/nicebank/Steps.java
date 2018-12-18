package nicebank;

import cucumber.api.PendingException;
import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import transforms.MoneyConverter;

public class Steps {

    KnowsTheDomain helper;

    public Steps(){
        helper = new KnowsTheDomain();
    }

    class KnowsTheDomain{
        private Account myAccount;
        private CashSlot cashSlot;
        private Teller teller;

        /**
         * @return the myAccount
         */
        public Account getMyAccount() {
            if(myAccount == null){
                myAccount = new Account();
            }
            return myAccount;
        }

        public CashSlot getCashslot() {
            if(cashSlot == null){
                cashSlot = new CashSlot();
            }
            return cashSlot;
        }

        public Teller getTeller() {
            if(teller == null){
                teller = new Teller(helper.getCashslot());
            }
            return teller;
        }
    }

    class Account{
        private Money balance = new Money();

        public void deposit(Money amount){
            balance = balance.add(amount);
        }

        public Money getBalance(){
            return balance;
        }
    }

    class Teller{

        private CashSlot cashSlot;

        public Teller(CashSlot cashSlot){
            this.cashSlot = cashSlot;
        }

        public void witdrawFrom(Account myAccount, int dollars){
            cashSlot.dispense(dollars);
        }
    }

    class CashSlot{

        private int contents;

        /**
         * @return the contents
         */
        public int contents() {
            return contents;
        }

        public void dispense(int dollars){
            this.contents = dollars;
        }

    }

    @Given("^I have deposited \\$(\\d+\\.\\d+) in my account$")
    public void iHaveDeposited$InMyAccount(@Transform(MoneyConverter.class) Money amount) throws Throwable {
        
        helper.getMyAccount().deposit(amount);

        Assert.assertEquals("Incorrect account balance - ",
                amount, helper.getMyAccount().getBalance());
    }


    @When("^I request \\$(\\d+)$")
    public void iRequest$(int amount) throws Throwable {

        helper.getTeller().witdrawFrom(helper.getMyAccount(), amount);
    }

    @Then("^\\$(\\d+) should be dispensed$")
    public void $ShouldBeDispensed(int dollars) throws Throwable {
        Assert.assertEquals("Incorrect amount dispensed - ", 
            dollars, helper.getCashslot().contents());
    }
}

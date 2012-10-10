package nl.d2n.model;

import nl.d2n.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    List<BankCategory> bank;

    public Bank(List<Item> items) {
        this.bank = createBank(items);
        orderItemsByAmount(this.bank);
    }

    public List<BankCategory> getBank() {
        return this.bank;
    }

    protected void orderItemsByAmount(List<BankCategory> bank) {
        for (BankCategory category : bank) {
            category.orderByAmount();
        }
    }

    protected List<BankCategory> createBank(List<Item> items) {
        List<BankCategory> bank = new ArrayList<BankCategory>();
        for (ItemCategory category : ItemCategory.values()) {
            BankCategory bankCategory = new BankCategory();
            bankCategory.category = category.getName();
            for (Item item : items) {
                if (item.getCategory() != category) {
                    continue;
                }
                bankCategory.items.add(item);
            }
            bank.add(bankCategory);
        }
        return bank;
    }

    public String toJson() {
        return GsonUtil.objectToJson(this.bank);
    }
}

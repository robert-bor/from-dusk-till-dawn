package nl.d2n.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BankCategory {
    @Expose
    public String category;

    @Expose
    public List<Item> items = new ArrayList<Item>();

    public void orderByAmount() {
        Collections.sort(items, new ItemAmountComparator());
    }

    public class ItemAmountComparator implements Comparator<Item> {
        public int compare(Item thisItem, Item otherItem) {
            return -((Integer)thisItem.getAmount()).compareTo(otherItem.getAmount());
        }
    }
}

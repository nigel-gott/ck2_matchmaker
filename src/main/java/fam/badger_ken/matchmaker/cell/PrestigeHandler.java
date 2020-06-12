// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import fam.badger_ken.matchmaker.Person;

import javax.swing.*;
import java.awt.*;

public class PrestigeHandler implements CellHandler {

    @Override
    public int compare(Person arg0, Person arg1) {
        if (arg0.prestige == null && arg1.prestige == null) {
            return 0;
        } else if (arg1.prestige == null) {
            return 1;
        } else if (arg0.prestige == null) {
            return -1;
        } else {
            return Double.compare(arg0.prestige, arg1.prestige);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Person person = (Person) value;
        String val;
        if(person.prestige == null){
            val = "??";
        } else {
            val = Integer.toString(person.prestige.shortValue());
        }
        JLabel label = new JLabel(val);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

}

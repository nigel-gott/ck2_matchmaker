// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.

package fam.badger_ken.matchmaker.cell;

import fam.badger_ken.matchmaker.Person;

import javax.swing.*;
import java.awt.*;

public class HealthHandler implements CellHandler {

  @Override
  public int compare(Person arg0, Person arg1) {
    return Double.compare(arg0.health, arg1.health) ;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    String val = Double.toString(person.health);
    JLabel label = new JLabel(val);
    label.setHorizontalAlignment(JLabel.RIGHT);
    return label;
  }

}

// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.

package fam.badger_ken.matchmaker.cell;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fam.badger_ken.matchmaker.Person;

public class AgeHandler implements TableCellRenderer, Comparator<Person> {

  @Override
  public int compare(Person arg0, Person arg1) {
    return arg0.ageInYears - arg1.ageInYears;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    String val = Integer.toString(person.ageInYears);
    JLabel label = new JLabel(val);
    label.setHorizontalAlignment(JLabel.RIGHT);
    return label;
  }

}

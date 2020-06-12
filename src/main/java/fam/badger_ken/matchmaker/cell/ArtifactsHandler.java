// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.cell;

import fam.badger_ken.matchmaker.Artifact;
import fam.badger_ken.matchmaker.Holding;
import fam.badger_ken.matchmaker.Matchmaker;
import fam.badger_ken.matchmaker.Person;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;

public class ArtifactsHandler implements CellHandler {

  public ArtifactsHandler() {
  }

  @Override
  public int compare(Person arg0, Person arg1) {
    boolean has0 = arg0.getArtifacts() != null && !arg0.getArtifacts().isEmpty();
    boolean has1 = arg1.getArtifacts() != null && !arg1.getArtifacts().isEmpty();
    if (!has0 && !has1) return 0;
    if (has0 && !has1) return -1;
    if (!has0) return 1;
    // they both have holdings - whoever has the highest-ranked holding wins.
    // if a tie, whoever has the most holdings wins.
    return arg1.getArtifacts().size() - arg0.getArtifacts().size();
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Person person = (Person) value;
    if (person == null) return null;
    return new JLabel(person.getDisplayableArtifacts());
  }

}

// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

import java.awt.Dimension;
import java.util.Vector;

import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import fam.badger_ken.matchmaker.Dynasty;
import fam.badger_ken.matchmaker.Filterer;

/**
 * The 'DynastyBox' is a combo box optimized for showing dynasties -
 * it allows auto-suggest as the list is so long.
 */
public class DynastySelector extends UnsettableComboBox  {
  private static final long serialVersionUID = -4216036537787539958L;
  private Filterer filterer = null;

  public DynastySelector(Filterer filterer) {
    super();
    this.filterer = filterer;
    this.setOpaque(true);
    this.setMaximumSize(new Dimension(1000, 100));
    this.setMaximumRowCount(10);
  }
  
  public void setDynasties(Vector<Dynasty> dynasties) {
    Vector<AnyDropdownable> asDropdownable = new Vector<AnyDropdownable>();
    // add the 'Any' item.
    asDropdownable.add(new AnyItemForDropdown());
    asDropdownable.addAll(dynasties);
    super.populate(asDropdownable, filterer);
    org.jdesktop.swingx.autocomplete.AutoCompleteDecorator.decorate(this,
        new ObjectToStringConverter() {
          @Override
          public String getPreferredStringForItem(Object arg0) {
            if (arg0 instanceof AnyItemForDropdown) {
              return ((AnyItemForDropdown) arg0).toString();
            } else {
              return arg0.toString();
            }
          }
        });
    this.setMaximumRowCount(10);
  }
}

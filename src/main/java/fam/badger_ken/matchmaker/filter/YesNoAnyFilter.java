// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import fam.badger_ken.matchmaker.Filterer;
import fam.badger_ken.matchmaker.ResultMaker;
import fam.badger_ken.matchmaker.SwingGui;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;

/**
 * An abstract base class that handles most of the processing
 * for 'Yes/No/Any' combo boxes. The labels may change, e.g.
 * for 'gender' the labels may be 'Female/Male/Any'.
 */
public abstract class YesNoAnyFilter implements Filterer {
  private final String yesLabel;
  private final String noLabel;
  private boolean onNow;
  private boolean wantsYes;  // does the filter want 'yes' selected?
  private final ResultMaker resultMaker;

  public YesNoAnyFilter(ResultMaker resultMaker, String yesLabel, String noLabel) {
    this.resultMaker = resultMaker;
    this.yesLabel = yesLabel;
    this.noLabel = noLabel;
    onNow = false;
  }

  protected boolean wantsYes() {
    return wantsYes;
  }


  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JComboBox box = (JComboBox) changee;
    AnyDropdownable item = (AnyDropdownable) box.getSelectedItem();
    String val = (item == null) ? "Any" : item.toString();
    boolean toBeOn;
    boolean toBeWantsYes = false;
    if (yesLabel.equalsIgnoreCase(val)) {
      toBeOn = true;
      toBeWantsYes = true;
    } else if (noLabel.equalsIgnoreCase(val)) {
      toBeOn = true;
      toBeWantsYes = false;
    } else {
      toBeOn = false;
    }
    boolean change = onNow != toBeOn;
    if (onNow && wantsYes != toBeWantsYes) {
      change = true;
    }
    onNow = toBeOn;
    wantsYes = toBeWantsYes;

    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
  }


  @Override
  public boolean isOn() {
    return onNow;
  }

}

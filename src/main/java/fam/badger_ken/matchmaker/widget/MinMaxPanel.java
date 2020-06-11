// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

import javax.swing.JPanel;

import fam.badger_ken.matchmaker.Filterer;

/**
 * A MinMaxPanel is a panel that can can display one or more rows,
 * where each row contains a box for the min value of something,
 * and then the max value of something.
 * Each something also has a number of columns, and a Filterer
 * that should be hooked up to be notified when the value changes.
 *
 */
public class MinMaxPanel extends JPanel {
  /**
   * The bundle describing one min/max row.
   */
  public static class PanelRow {
    public String minText;  // e.g. 'min # of claims'
    public String maxText;  // e.g. 'max # of claims'
    public int numColumns;  // e.g. 3
    public Filterer minFilterer;  // called when min box changes
    public Filterer maxFilterer;  // call when max box changes.
    
  }

  /**
   * 
   */
  private static final long serialVersionUID = 57544502421713669L;

}

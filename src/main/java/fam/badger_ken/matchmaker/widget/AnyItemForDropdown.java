// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;


/**
 * Via the default constructor, this returns the "---- Any ---" item for a dropdown.
 * Via the non-default constructor, this returns, well, any item :), you pass in
 * the values of the three fields.
 *
 */
public class AnyItemForDropdown implements AnyDropdownable {
  private final boolean isAny;
  private final Object filterValue;
  private final String stringRep;

  public AnyItemForDropdown(boolean isAny, Object filterValue, String stringRep) {
    this.isAny = isAny;
    this.filterValue = filterValue;
    this.stringRep = stringRep;
  }
  
  public AnyItemForDropdown() {
    this(true, null, "-- Any --");
  }
  
  @Override
  public boolean isAny() {
    return isAny;
  }

  @Override
  public Object infoForFilter() {
    return filterValue;
  }

  @Override
  public String toString() {
    return stringRep;
  }
}

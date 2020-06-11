// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

/**
 * The info about a particular attribute (e.g. stewardship),
 * across all people, not for a particular person.
 */
public class AttributeInfo {
  public String localisationLabel;  // e.g. CHAR_STEWARDSHIP
  public String displayLabel;  // e.g. Stewardship
  public String internalLabel;  // name in other files, e.g. stewardship
  public int ordinality;  // e.g. 2

  public AttributeInfo(String localisationLabel,
      String internalLabel, int ordinality) {
    this.localisationLabel = localisationLabel;
    this.displayLabel = internalLabel;  // until found in localisation file.
    this.internalLabel = internalLabel;
    this.ordinality = ordinality;
  }
}

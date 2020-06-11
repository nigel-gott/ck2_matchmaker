// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

/**
 * A single barony, bishopric, or county
 */
public class Demesne {
  public String label;  // e.g. b_ciudadrodrigo
  // note we don't know if this is the town of, castle of, temple of, etc.
  public String shortDisplayName;  // e.g. "Ciudad Rodrigo"

  public Demesne(String label) {
    this.label = label;
  }

  public Demesne(String key, String value) {
    this.label = key;
    this.shortDisplayName = value;
  }
}

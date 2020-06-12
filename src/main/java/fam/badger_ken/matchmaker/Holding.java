// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

/**
 * A 'Holding' is something whose title can be held -
 * a barony, county, town, duchy, kindgom, empire, etc.
 */
public class Holding {
  public final String prefix; // e.g. 'b' for a barony
  public final String suffix; // e.g. 'xacitarxan' for the barony of xacitarxan
  public final String internalLabel;  // e.g. b_xacitarxan
  public String displayLabel;  // e.g. Xacitarxan
  
  public final HoldingLevel holdingLevel;

  public Holding(String prefix, String suffix) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.internalLabel = prefix + "_" + suffix;
    this.displayLabel = null;
    holdingLevel = HoldingLevel.fromPrefix(prefix.charAt(0));
  }
  
  public String getAppelation() {
    if (holdingLevel == null) return "";
    return holdingLevel.displayLabel + " of ";
  }
  
  /**
   * Gets the display label.
   * This is a bit tricky in that it appears the game uses a shorthand,
   * where often the county-level names are missing, and it uses the barony
   * name, or sometimes the duchy name, or sometimes it just magically knows
   * (e.g. "chalkidike").
   * @param gameConfig
   * @return
   */
  public String getDisplayLabel(GameConfig gameConfig) {
    if (displayLabel == null) {
      if (gameConfig.demesnesByLabel.containsKey(internalLabel)) {
        displayLabel = gameConfig.demesnesByLabel.get(internalLabel).shortDisplayName;
      } else {
        char [] prefixes = { 'b', 'c', 'd', 'k'};
        displayLabel = internalLabel;
        for (char prefix : prefixes) {
          String key = prefix + "_" + suffix;
          if (gameConfig.demesnesByLabel.containsKey(key)) {
            displayLabel = gameConfig.demesnesByLabel.get(key).shortDisplayName;
            break;
          }
        }
      }
    }
    return displayLabel;
  }
}

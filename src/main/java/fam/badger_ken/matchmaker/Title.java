// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.HashMap;
import java.util.Map;

/**
 * A 'Title' is your addressed title, more or less.
 * It's different from your 'HoldingLevel'. If, for example, you are the
 * daughter of a Duke, you are a 'Duchess', though you are not the Duchess
 * of anything - it's an honorary title you get for being Daddy's little
 * girl.
 */
public class Title implements Comparable<Title> {
  public enum TitleRank {
    // the title ranks, IN ORDER OF RANK
    NONE,
    UNKNOWN,
    BARON,
    COUNT,
    DUKE,
    PRINCE,
    KING,
    EMPEROR
  };
  public enum HowHeld {
    // why you have the title, 'weightiest' reason FIRST
    BY_HOLDING,
    BY_MARRIAGE,
    BY_INHERITANCE_FROM_DAD,
    BY_INHERITANCE_FROM_MOM
  };
    
  private String displayName = null;  // e.g. 'Prince Consort' - created on first read.
  public TitleRank rank;  // the higher the rank, the more important this title is.
  public HowHeld howHeld;  // how did you get this title?
  public boolean isMale;  // is this title for a male?
  // maps from a holding level to its title rank
  private static Map<HoldingLevel, TitleRank> holdingTitleMap = null;
  
  public static Title TITLE_NONE = new Title(TitleRank.NONE, HowHeld.BY_HOLDING, true);
  
  public Title(TitleRank rank, HowHeld howHeld, boolean isMale) {
    this.rank = rank;
    this.howHeld = howHeld;
    this.isMale = isMale;
  }
  
  private static TitleRank getTitleRankFromHolding(HoldingLevel level) {
    if (level == null) {
      return TitleRank.NONE;
    }
    if (holdingTitleMap == null) {
      holdingTitleMap = new HashMap<HoldingLevel, TitleRank>();
      holdingTitleMap.put(HoldingLevel.BARONY, TitleRank.BARON);
      holdingTitleMap.put(HoldingLevel.COUNTY, TitleRank.COUNT);
      holdingTitleMap.put(HoldingLevel.DUCHY, TitleRank.DUKE);
      holdingTitleMap.put(HoldingLevel.KINGDOM, TitleRank.KING);
      holdingTitleMap.put(HoldingLevel.EMPIRE, TitleRank.EMPEROR);
    }
    TitleRank rank = holdingTitleMap.get(level);
    return rank == null ? TitleRank.UNKNOWN : rank;
  }

  /**
   * the title a person gets based on their holding level.
   * @param level the holding level - may be null.
   * @param isMale is the person male?
   */
  public static Title MakeTitle(HoldingLevel level, boolean isMale) {
    if (level == null) {
      return TITLE_NONE;
    }
    TitleRank rank = getTitleRankFromHolding(level);
    return new Title(rank, HowHeld.BY_HOLDING, isMale);
  }
  
  /**
   * The title you get for being married to a person with this 
   * holding level.
   * @param spouseLevel the holding level your spouse has
   * @param isMale is the requestor male?
   */
  public static Title MakeSpousalTitle(HoldingLevel spouseLevel, boolean isMale) {
    if (spouseLevel == null) {
      return TITLE_NONE;
    }
    TitleRank rank = getTitleRankFromHolding(spouseLevel);
    return new Title(rank, HowHeld.BY_MARRIAGE, isMale);
  }
  
  /**
   * The title you get for being the child of a person
   * with this TITLE (not holding level). This is how the
   * children of Prince Charles get to be Princes, not Dukes.
   */
  public static Title MakeInheritedTitle(Title dadTitle, Title momTitle, boolean isMale) {
    Title bestTitle;
    if (dadTitle == null && momTitle == null) {
      return TITLE_NONE;
    }
    boolean fromDad = true;
    if (dadTitle == null) {
      bestTitle = momTitle;
    } else if (momTitle == null) {
      bestTitle = dadTitle;
    } else {
      bestTitle = dadTitle.compareTo(momTitle) < 0 ? momTitle : dadTitle;
    }
    fromDad = bestTitle == dadTitle;
    // maybe these should each be objects?
    // children of emperors, kings, and princes are princes.
    // otherwise nothing.
    boolean isPrince = false;
    switch (bestTitle.rank) {
    case PRINCE:
    case KING:
    case EMPEROR:
      isPrince = true;
    }
    return isPrince ? new Title(TitleRank.PRINCE,
        fromDad ? HowHeld.BY_INHERITANCE_FROM_DAD : HowHeld.BY_INHERITANCE_FROM_MOM, isMale) : TITLE_NONE;
  }

  @Override
  public int compareTo(Title other) {
    // lower values are LESS important.
    // first, look at rank:
    int delta = rank.ordinal() - other.rank.ordinal();
    if (delta != 0) {
      return delta;
    }
    // within that, by how held
    delta = other.howHeld.ordinal() - howHeld.ordinal();
    return delta;
  }

  @Override
  public String toString() {
    if (displayName == null) {
      // this should be a field in the enum....
      switch (rank) {
      case BARON:
        displayName = isMale ? "Baron" : "Baroness";
        break;
      case COUNT:
        displayName = isMale ? "Count" : "Countess";
        break;
      case DUKE:
        displayName = isMale ? "Duke" : "Duchess";
        break;
      case KING:
        displayName = isMale ? "King" : "Queen";
        break;
      case EMPEROR:
        displayName = isMale ? "Emperor" : "Empress";
        break;
      case UNKNOWN:
        displayName = "Unknown";
        break;
      case NONE:
        displayName = "";
        return displayName;
      }
      switch (howHeld) {
      // this should be a field in the enum
      case BY_HOLDING:
        displayName += " (by holding)";
        break;
      case BY_MARRIAGE:
        displayName += " (by marriage)";
        break;
      case BY_INHERITANCE_FROM_DAD:
        displayName += " (inherited from father)";
        break;
      case BY_INHERITANCE_FROM_MOM:
        displayName += " (inherited from mother)";
        break;
      }
    }
    return displayName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((howHeld == null) ? 0 : howHeld.hashCode());
    result = prime * result + ((rank == null) ? 0 : rank.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Title other = (Title) obj;
    if (howHeld != other.howHeld)
      return false;
    if (rank != other.rank)
      return false;
    return true;
  }
}

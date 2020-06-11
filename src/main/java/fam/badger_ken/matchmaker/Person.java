// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import fam.badger_ken.matchmaker.Trait.AttributeImpact;

/**
 * A person in the game
 */
public class Person {
  public Integer key;
  public String birth_name;
  public String raw_birth_date;
  public Integer fatherKey;
  public Integer motherKey;
  public Set<Integer> spouseKeys;
  public Vector<Integer> attributes;
  public Vector<Integer> adjustedAttributes = null;
  public Vector<Integer> traits;
  public String religionLabel;
  public String cultureLabel;
  public Integer dynastyKey;
  public int numClaims = 0;
  public boolean isMale = true;
  public String nickname = null;
  public String primaryDemesneLabel;
  private String homeName = null;
  
  public Integer hostKey = null;  // whose court 'hosts' them, if any.
  public String jobLabel = null;
  // their title - e.g. they may be a 'prince' or 'princess'
  // but have neither a demesne nor a job.
  public String titleLabel = null;
  public int ageInYears = 0;
  public int numLivingMaleChildren = 0;
  public int numLivingFemaleChildren = 0;
  public Double prestige = null;
  private Double piety = null;
  private Double wealth = null;
  private String displayName = null;
  private String displayTraits = null;
  private String displayDynasty = null;
  private String displayReligion = null;
  private String displayCulture = null;
  private String displayTitle = null;
  private String displayHoldings = null;
  // holdings, most important one first
  private Set<Holding> holdings = null;
  // their title - set in a special pass
  private Title title = Title.TITLE_NONE;

  public Person(Integer key) {
    this.key = key;
    spouseKeys = new HashSet<Integer>();
    attributes = new Vector<Integer>();
    traits = new Vector<Integer>();
  }
  public void addSpouse(Integer spouseKey) {
    spouseKeys.add(spouseKey);
  }
  public void addAttribute(Integer attribute) {
    if (attribute != null) {
      attributes.add(attribute);
    }
  }
  public void addTrait(Integer traitKey) {
    if (traitKey != null) {
      traits.add(traitKey);
    }
  }
  public void addChild(boolean isMale) {
    if (isMale) {
      numLivingMaleChildren++;
    } else {
      numLivingFemaleChildren++;
    }
  }
  // the game considers missing piety/wealth as 0.
  public Double getPiety() {
    return piety == null ? 0 : piety;
  }
  
  public Double getWealth() {
    return wealth == null ? 0 : wealth;
  }
  
  public void setPiety(Double v) {
    this.piety = v;
  }
  
  public void setWealth(Double v) {
    this.wealth = v;
  }
  
  public void setHoldings(Set<Holding> holdings) {
    this.holdings = holdings;
  }

  // lazy eval.
  public String getDisplayName(GameConfig gameConfig, SaveState saveState) {
    if (displayName == null) {

      String answer = birth_name;
      if (nickname != null && !nickname.isEmpty()) {
        String displayNickname = gameConfig.nicknamesByLabel.get(nickname);
        if (displayNickname == null) {
          displayNickname = nickname;
        }
        answer += " (" + displayNickname + ")";
      }

      displayName = answer;	
    }
    return displayName;
  }
  
  public String getHomeName(GameConfig gameConfig, SaveState saveState) {
    if (homeName == null) {
      String label = null;
      // some people are hosted by themselves - so only look at the host
      // if there's no primary demesne.
      label = primaryDemesneLabel;
      if (label == null && hostKey != null && saveState.people.containsKey(hostKey)) {
        label = saveState.people.get(hostKey).primaryDemesneLabel;
      }
      if (label == null) {
        homeName = "Unknown";
      } else if (gameConfig.demesnesByLabel.containsKey(label)) {
        homeName = gameConfig.demesnesByLabel.get(label).shortDisplayName;
      } else {
        homeName = label;
      }
    }
    return homeName;
  }

  // lazy eval of the traits, as a single display string.
  public String getDisplayTraits(GameConfig gameConfig, SaveState saveState) {
    if (displayTraits == null) {
      if (traits == null || traits.size() == 0) {
        return "";
      }
      // sort 'em alphabetically by their name.
      List<String> names = new ArrayList<String>();
      for (Integer key : traits) {
        Trait trait = gameConfig.traits.get(key);
        names.add(trait == null ? ("#" + key) : trait.displayName);
      }
      Collections.sort(names);
      int i = 0;
      StringBuilder sb = new StringBuilder();
      // the web says - use HTML, not raw text, and it will word-wrap:
      sb.append("<html>");
      for (String name : names) {
        //if ((i % 3) == 0) {
        //	sb.append("<br>");
        //}
        if (i != 0) sb.append(",");
        sb.append(name);
        i++;
      }
      sb.append("</html>");
      displayTraits = sb.toString();
    }
    return displayTraits;
  }
  public int getNumTraits() {
    return traits == null ? 0 : traits.size();
  }

  public boolean isMarried() {
    return spouseKeys != null && spouseKeys.size() > 0;
  }
  
  public int getNumSpouses() {
    return spouseKeys == null ? 0 : spouseKeys.size();
  }

  public String getDisplayDynasty(GameConfig gameConfig, SaveState saveState) {
    if (displayDynasty == null) {
      if (dynastyKey == null) {
        displayDynasty = "_null"; //"??";
      } else {
        Dynasty dynasty = saveState.dynasties.get(dynastyKey);
        displayDynasty = (dynasty == null) ? "" : dynasty.name;
      }
    }
    return displayDynasty;
  }
  public String getDisplayReligion(GameConfig gameConfig, SaveState saveState) {
    if (displayReligion == null) {
      if (religionLabel == null) {
        displayReligion = "??";
      } else {
        Religion religion = gameConfig.religionsByLabel.get(religionLabel);
        displayReligion = religion == null ? religionLabel : religion.displayName;
      }
    }
    return displayReligion;
  }
  public String getDisplayCulture(GameConfig gameConfig, SaveState saveState) {
    if (displayCulture == null) {
      if (cultureLabel == null) {
        displayCulture = "??";
      } else {
        Culture culture = gameConfig.culturesByLabel.get(cultureLabel);
        displayCulture = culture == null ? cultureLabel : culture.displayName;
      }
    }
    return displayCulture;
  }
  public Integer getAdjustedAttribute(GameConfig gameConfig, int ordinality) {
    if (adjustedAttributes == null) {
      adjustedAttributes = new Vector<Integer>(attributes);
      if (traits != null) {
        for (Integer traitKey : traits) {
          Trait trait = gameConfig.traits.get(traitKey);
          if (trait != null && trait.impacts != null) {
            for (AttributeImpact impact : trait.impacts) {
              int attrOrdinality = impact.impactee.ordinality;
              adjustedAttributes.set(attrOrdinality,
                  adjustedAttributes.get(attrOrdinality) + impact.impact);
            }
          }
        }
      }
    }
    return adjustedAttributes.get(ordinality);
  }
  public boolean isRuler() {
    return primaryDemesneLabel != null;
  }
  public boolean hasTrait(Trait value) {
    return (traits != null) && traits.contains(value.key);
  }
  public int getNumKids() {
    return numLivingFemaleChildren + numLivingMaleChildren;
  }
  public Set<Holding> getHoldings() {
    return holdings;
  }
  
  public String getDisplayableHoldings(GameConfig gameConfig) {
    if (displayHoldings == null) {
      if (holdings == null || holdings.isEmpty()) {
        displayHoldings = "";
      } else {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Holding holding : holdings) {
          if (i != 0) {
            sb.append(", ");
          }
          sb.append(holding.getAppelation());
          String displayName = holding.getDisplayLabel(gameConfig);
          sb.append(displayName);
          i++;
        }
        displayHoldings = sb.toString();
      }
    }
    return displayHoldings;
  }
  
  public Title getTitle() {
    return title;
  }
  public void setTitle(Title t) {
    this.title = t;
  }
  public HoldingLevel getBestHoldingLevel() {
    Set<Holding> holdings = getHoldings();
    if (holdings == null) {
      return null;
    }
    Holding best = (Holding) holdings.toArray()[0];
    return best.holdingLevel;
  }
}

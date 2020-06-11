package fam.badger_ken.matchmaker;

import java.util.HashMap;
import java.util.Map;

// Christian, Muslim, Pagan, etc.
public class ReligionGroup {
  // the pruning modes we support:
  public static enum PruningMode {
    CHRISTIAN_ONLY,
    MUSLIM_ONLY,
    ALL
  };
  
  public String label;
  public String displayName;
  Map<String, Religion> religionsByLabel;
  boolean ignoreOnLoad = false;
  
  public ReligionGroup(String label) {
    this.label = label;
    displayName = label;
    religionsByLabel = new HashMap<String, Religion>();
  }
  
  public void addReligion(Religion relig) {
    if (relig == null) return;
    if (religionsByLabel.containsKey(relig.label)) return;
    religionsByLabel.put(relig.label, relig);
  }
  

}

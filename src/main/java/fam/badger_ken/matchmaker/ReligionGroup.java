package fam.badger_ken.matchmaker;

import java.util.HashMap;
import java.util.Map;

// Christian, Muslim, Pagan, etc.
public class ReligionGroup {
  // the pruning modes we support:
  public enum PruningMode {
    CHRISTIAN_ONLY,
    MUSLIM_ONLY,
    ALL
  }

  public final String label;
  public final String displayName;
  final Map<String, Religion> religionsByLabel;
  boolean ignoreOnLoad = false;
  
  public ReligionGroup(String label) {
    this.label = label;
    displayName = label;
    religionsByLabel = new HashMap<>();
  }
  
  public void addReligion(Religion relig) {
    if (relig == null) return;
    if (religionsByLabel.containsKey(relig.label)) return;
    religionsByLabel.put(relig.label, relig);
  }
  

}

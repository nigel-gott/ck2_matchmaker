// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * Main storage class that stores all the game configuration,
 * _independent_ of a particular save - what the traits are,
 * what the nicknames are, what the religions are, etc.
 */
public class GameConfig {
  // map from trait key to trait.
  public Map<Integer, Trait> traits;
  // the traits where the key is the label
  public Map<String, Trait> traitsByLabel;
  // map from nickname label (e.g. 'nick_the_great' to display name
  public Map<String, String> nicknamesByLabel;
  // map from job label (e.g. 'job_spiritual' to displayName
  public Map<String, Job> jobsByLabel;
  // map from religion label to religion
  public Map<String, Religion> religionsByLabel;
  // map from culture label to culture
  public Map<String, Culture> culturesByLabel;
  // map from title label to title
  public Map<String, Title> titlesByLabel;
  // map from demesne label to demesne
  public Map<String, Demesne> demesnesByLabel;
  // map from religion group tag to group:
  public Map<String, ReligionGroup> religionGroupsByLabel;
  // Attribute info
  public ArrayList<AttributeInfo> attributeInfo;
  public static final int NUM_ATTRIBUTES = 5;

  public GameConfig() {
    this.traits = new HashMap<Integer, Trait>();
    this.traitsByLabel = new HashMap<String, Trait>();
    this.nicknamesByLabel = new HashMap<String, String>();
    this.jobsByLabel = new HashMap<String, Job>();
    this.religionsByLabel = new HashMap<String, Religion>();
    this.culturesByLabel = new HashMap<String, Culture>();
    this.titlesByLabel = new HashMap<String, Title>();
    this.demesnesByLabel = new HashMap<String, Demesne>();
    this.religionGroupsByLabel = new HashMap<String, ReligionGroup>();
    initializeAttributeInfo();
  }

  private void initializeAttributeInfo() {
    int i = 0;
    attributeInfo = new ArrayList<AttributeInfo>(NUM_ATTRIBUTES);
    attributeInfo.add(new AttributeInfo("CHAR_DIPLOMACY", "diplomacy", i++));
    attributeInfo.add(new AttributeInfo("CHAR_MARTIAL", "martial", i++));
    attributeInfo.add(new AttributeInfo("CHAR_STEWARDSHIP", "stewardship", i++));
    attributeInfo.add(new AttributeInfo("CHAR_INTRIGUE", "intrigue", i++));
    attributeInfo.add(new AttributeInfo("CHAR_LEARNING", "learning", i++));
  }

  private boolean isAttributeLabel(String localKey, String displayAs) {
    for (AttributeInfo attr : attributeInfo) {
      if (localKey.equals(attr.localisationLabel)) {
        attr.displayLabel = displayAs;
        return true;
      }
    }
    return false;
  }

  /**
   * Parse the traits under a given root node.
   * The 'traitsTrace' is used to emit debug information for users.
   * @param traitsRoot
   */
  public int parseTraits(Node traitsRoot, StringBuilder traitsTrace) {
    /*
     * the i'th trait in the file gets key (i + 1)
     */
    int numTraits = traits.size();
    for (Node child : traitsRoot.children) {
      Trait trait = new Trait(numTraits + 1, child.tag);
      traits.put(numTraits + 1, trait);
      traitsByLabel.put(trait.label, trait);
      if (traitsTrace != null) {
        traitsTrace.append("\ntrait # " + (numTraits + 1) + " is '" + trait.label + "'");
      }
      numTraits++;
      // and now spin through the traits to find their effects:
      if (child.attributes != null) {
        for (Entry<String,String> entry : child.attributes.entrySet()) {
          String key = entry.getKey();
          String effect = entry.getValue();
          AttributeInfo attrInfo = getAttributeInfoFromInternalLabel(key);
          if (attrInfo != null) {
            try {
              Integer impact = Integer.parseInt(effect);
              trait.addImpact(attrInfo, impact);
              if (traitsTrace != null) {
                traitsTrace.append("\n " + attrInfo.displayLabel + " is adjusted by " + impact + " by trait '" + trait.displayName + "'");
              }
            } catch (NumberFormatException e) {
            }
          }
        }
      }
    }
    return numTraits;
  }

  private AttributeInfo getAttributeInfoFromInternalLabel(String key) {
    for (AttributeInfo info : attributeInfo) {
      if (key.equals(info.internalLabel)) {
        return info;
      }
    }
    return null;
  }

  public void loadReligions(String dirName) {
    File baseDir = new File(dirName);
    if (!baseDir.isDirectory() || !baseDir.canRead()) {
      return;
    }
    File [] files = baseDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String fileName) {
        return fileName.endsWith(".txt");
      }});
    if (files == null) return;
    religionGroupsByLabel = new HashMap<String, ReligionGroup>();
    for (File religionFile : Util.sortFiles(files)) {
      ParadoxParser parser;
      try {
        parser = new ParadoxParser(religionFile.getCanonicalPath(), "religion parser");
      } catch (FileNotFoundException e) {
        continue;
      } catch (IOException e) {
        continue;
      }
      Node root = new Node("religions");
      parser.Parse(root);
      if (root == null || root.children == null) continue;
      // each child is a group, each grandchild is a religion in that group.
      for (Node groupNode : root.children) {
        ReligionGroup group = religionGroupsByLabel.get(groupNode.tag);
        if (group == null) {
          group = new ReligionGroup(groupNode.tag);
          religionGroupsByLabel.put(groupNode.tag, group);
        }
        if (groupNode.children != null) {
          for (Node relig : groupNode.children) {
            Religion religion = new Religion(group, relig.tag, relig.tag);
            group.addReligion(religion);
            religionsByLabel.put(relig.tag, religion);
          }
        }
      }
    }
  }

  
  /**
   * load whatever we need from the localisation files.
   * @param dirName the name of the localisation dir.
   */
  public void loadLocalisation(String dirName,
      Set<String> neededCultures,
      Set<String> neededTitles,
      Set<String> neededDemesnes,
      StringBuilder traitsTrace) {
    File localisationDir = new File(dirName);
    if (!localisationDir.isDirectory() || !localisationDir.canRead()) {
      return;
    }
    File [] files = localisationDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String fileName) {
        return true // some mods don't start their files with text fileName.startsWith("text")
        && fileName.endsWith(".csv");
      }});
    if (files == null) return;
    for (File localisationFile : Util.sortFiles(files)) {
      loadLocalisationFile(localisationFile, neededCultures,
          neededTitles, neededDemesnes, traitsTrace);
    }
  }

  private void loadLocalisationFile(File localisationFile,
      Set<String> neededCultures,
      Set<String> neededTitles, Set<String> neededDemesnes, StringBuilder traitsTrace) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(localisationFile));
      for (;;) {
        try {
          String line = br.readLine();
          if (line == null) break;
          if (line.isEmpty()) continue;
          String [] fields = line.split(";");
          // key is the first field, english label is second.
          if (fields == null || fields.length < 2) continue;
          // could add support for other languages, but I did that for Vic2
          // and nobody ever cared :(.
          String key = fields[0];
          String value = fields[1];
          if (traitsByLabel.containsKey(key)) {
            traitsByLabel.get(key).displayName = value;
            if (traitsTrace != null) {
              traitsTrace.append("\n" + localisationFile.getCanonicalPath() + " defines trait "
                  + "'" + key + "' to display as '" + value + "'");
            }
          } else if (religionsByLabel.containsKey(key)) {
            religionsByLabel.get(key).displayName = value;
          } else if (neededCultures.contains(key)) {
            Culture culture = new Culture(key, value);
            culturesByLabel.put(key, culture);
          } else if (neededDemesnes.contains(key)) {
            demesnesByLabel.put(key, new Demesne(key, value));
          } else if (key.startsWith("nick_")) {
            nicknamesByLabel.put(key, value);
          } else if (key.startsWith("job_")) {
            Job job = new Job(key);
            job.displayName = value;
            jobsByLabel.put(key, job);
          } else if (isAttributeLabel(key, value)) {
            continue;
          }
        } catch (IOException e) {
          break;
        }

      }
    } catch (FileNotFoundException e) {
      return;
    }

  }
}

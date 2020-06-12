// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.Objects;

/**
 * An artifact owned by a player
 */
public class Artifact {

  private final String id;
  private final String type;
  private final String desc;
  private final Integer owner;
  private final String name;

  public Artifact(String id, String type, String desc, Integer owner, String name) {
    this.id = id;
    this.type = type;
    this.desc = desc;
    this.owner = owner;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }

  public Integer getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName(){
    if(this.name != null){
      return this.name;
    } else {
      return this.type;
    }
  }

  @Override
  public String toString() {
    return "Artifact{" +
            "id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", desc='" + desc + '\'' +
            ", owner=" + owner +
            ", name='" + name + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Artifact artifact = (Artifact) o;
    return id.equals(artifact.id) &&
            type.equals(artifact.type) &&
            desc.equals(artifact.desc) &&
            owner.equals(artifact.owner) &&
            name.equals(artifact.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, desc, owner, name);
  }
}

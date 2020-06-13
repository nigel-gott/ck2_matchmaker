// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.io.File;
import java.util.*;

public class Util {

  /**
   * Convert to integer, (default) if null or not integer
   */
  public static Integer toInt(String val, Integer dflt) {
    if (val == null) {
      return dflt;
    }
    try {
      return Integer.parseInt(val);
    } catch (NumberFormatException e) {
      return dflt;
    }
  }

  /**
   * Convert to double, (default) if null or not integer
   */
  public static Double toDouble(String val, Double dflt) {
    if (val == null) {
      return dflt;
    }
    try {
      return Double.parseDouble(val);
    } catch (NumberFormatException e) {
      return dflt;
    }
  }


  /**
   * Sort a set of files by name ascending.
   */
  public static List<File> sortFiles(File [] in) {
    List<File> sortedList = new ArrayList<>(Arrays.asList(in));
    sortedList.sort(Comparator.comparing(File::getName));
    return sortedList;
  }

  /**
   * CSV escape a column
   * @param text the text to escape
   * @return the escaped text
   */
  public static String csvEscape(String text) {
    if (text == null) return null;
    // if no comma, done.
    if (!text.contains(",")) {
      return text;
    }
    // if a comma, wrap in double-quotes,
    // and double the double-quotes.
    return "\"" + text.replace("\"", "\"\"") + "\"";
  }

  /**
   * Indicates whether two of a type T are equal, works if either/both is null
   * @param i1 the first
   * @param i2 the second.
   * @return If i1 equals i2
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static <T> boolean guardedEquals(T i1, T i2) {
    if ((i1 == null) != (i2 == null)) return false;
    if (i1 == null) return true;  // both are null
    // only case left is neither is null.
    return i1.equals(i2);
  }
  
  public static int guardedCompare(Double d1, Double d2, boolean nullAtEnd) {
    if (d1 == null && d2 == null) return 0;
    if (d1 == null) return nullAtEnd ? 1 : -1;
    if (d2 == null) return nullAtEnd ? -1 : 1;
    return d1.compareTo(d2);
  }
}

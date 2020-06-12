// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Parses paradox files into a tree.
 * @author badger_ken
 *
 */
public class ParadoxParser {
  ParadoxScanner iScanner = null;
  boolean debug = false;


  private enum States {
    AT_START,
    SAW_EQUALS
  };

  int tokenNumber = 0;
  States state = States.AT_START;
  String lastKey = null;
  Node currentParent = null;

  /*
   * Constructor
   *
   * Creates a parser for the specified file
   */
  public ParadoxParser(String fileName, String why) throws FileNotFoundException {
    try {
      // CK2 Save files are encoded in this Charset
      BufferedReader reader = new BufferedReader(new FileReader(fileName, Charset.forName("Windows-1252")));
      iScanner = new ParadoxScanner(reader);
    } catch (IOException e) {
      throw new FileNotFoundException("Could not open " + why + " file " + fileName);
    }
  }

  /**
   * This is the main workhorse procedure that sucks in various Paradox files,
   * and parses them into a tree rooted by (initialRoot).
   * The parsing code is pretty kludgey, but it works.
   */
  public void Parse(Node initialRoot, ParserPruner pruner) {
    currentParent = initialRoot;
    boolean debug = false;
    state = States.AT_START;
    lastKey = null;
    tokenNumber = 0;

    while (iScanner.hasNext()) {
      String s;
      s = iScanner.next();
      if (debug) {
	System.out.println("s = [" + s + "]");
      }
      DoToken(s, pruner);
    }
  }
  
  public void Parse(Node initialRoot) {
    Parse(initialRoot, null);
  }


  /**
   *
   * @param token
   *          token to process
   */
  private void DoToken(String token, ParserPruner pruner) {
    if (token == null) {
      return;
    }
    tokenNumber++;
    // if ((tokenNumber % 1000) == 0) {
    // System.out.println("on token number "
    // + tokenNumber + " (" + token + ")");
    // }
    // scanner can't be told to treat '=', '{', '}' as breaks....
    if (token.equals("=")) {
      if (state == States.AT_START) {
	state = States.SAW_EQUALS;
      } else {
	System.out.println("equals at weird place");
      }
    } else if (token.equals("{")) {
      String key = lastKey;
      key = (state == States.AT_START) ? "anonymous" : lastKey;
      if (debug) {
	System.out.println("pushing " + key + " as child of " + currentParent.tag);
      }
      Node newNode = new Node(key);
      currentParent.addChild(newNode);
      currentParent = newNode;
      state = States.AT_START;
      lastKey = null;
    } else if (token.equals("}")) {
      if (lastKey != null) {
	currentParent.value = lastKey;
      }
      if (currentParent.parent == null) {
	// happens at the very end
	return;
      }
      if (debug) {
	System.out.println("popping from " + currentParent.tag + " to "	+ currentParent.parent.tag);
      }
      if (pruner != null && pruner.discard(currentParent.parent, currentParent)) {
        // it was in there as the last kid:
        currentParent.parent.children.remove(currentParent.parent.children.size() - 1);
      }
      currentParent = currentParent.parent;
      state = States.AT_START;
      lastKey = null;
    } else {
      // if we saw the equals, this starts a new attribute.
      // otherwise, just remember it
      if (state == States.SAW_EQUALS) {
	currentParent.setAttribute(deQuote(lastKey), deQuote(token));
	if (debug) {
	  System.out.println("set " + lastKey + " attr to " + token);
	}
	state = States.AT_START;
	lastKey = null;
      } else {
	lastKey = (lastKey == null) ? token : (lastKey + " " + token);
      }
    }
  }

  public static String deQuote(String in) {
    if (in == null || in.length() < 2) {
      return in;
    } else if (in.startsWith("\"") && in.endsWith("\"")) {
      return in.substring(1, in.length() - 1);
    } else {
      return in;
    }
  }

}

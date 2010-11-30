/**
 * Perform a syntax check on a JavaScript file.
 */
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Script;

import java.io.Reader;
import java.io.FileReader;

public class Jagged {
  
  public static void main (String [] args) {
    int good = 0;
    int bad = 0;
    
    Context cxt = Context.enter();
    try {
      /*
      // Collect the arguments into a single string.
      String s = "";
      for (int i=0; i < args.length; i++) {
          s += args[i];
      }
      */
      if (args.length == 0) {
        System.out.println("Usage: PROG file1.js [file2.js [file3.js [...]]]");
      }
      
      boolean resp = false;
      
      for (String name: args) {
        resp = lintFile(name, cxt);
        if (resp) ++good; else ++bad;
      }

    } finally {
      // Exit from the context.
      Context.exit();
    }
    String summary = String.format(
      "Tests: %d, Successes: %d, Failures: %d",
      good + bad,
      good,
      bad
    );
    System.out.println(summary);
  }
  
  /**
   * Read each file and try to compile it.
   *
   * If compilation succeeds, this will silently return true.
   *
   * If the file cannot be loaded or loading/compiling fails, this will
   * emit a message on System.out and then return false.
   */
  public static boolean lintFile(String filename, Context cxt) {
    FileReader f;
    try {
      f = new FileReader(filename);
    }
    catch (Exception e) {
      System.out.println("Could not open " + filename + " :" + e.getMessage());
      return false;
    }
    
    try {
      Script s = cxt.compileReader(f, filename, 1, null);
    }
    catch (java.io.IOException ioe) {
      System.out.println("Unexpected IO exception: " + ioe.getMessage());
      return false;
    }
    catch (org.mozilla.javascript.EvaluatorException ee) {
      //System.out.println("Script " + filename + " failed to compile: " + ee.getMessage());
      String msg = String.format("%s (ln: %d, ch: %d): %s NEAR %s", 
        ee.sourceName(),
        ee.lineNumber(),
        ee.columnNumber(),
        ee.details(),
        diedOnThisChar(ee.lineSource(), ee.columnNumber())
      );
      System.out.println(msg);
      System.out.println(ee.lineSource());
      return false;
      
    }
    
    return true;
  }
  
  protected static String diedOnThisChar(String foo, int prob) {
    int start = prob > 1 ? prob - 2 : prob;
    int end = foo.length() > start + 9 ? start + 10 : foo.length() -1 ;
    
    //Character theChar = foo.charAt(prob);
    
    String icky = foo.substring(start, end);

    //return String.format("Char %d in %s", Character.digit(theChar, Character.MAX_RADIX), icky);
    return icky;
  }
  
}
/** — — — — — — — — —
    NAME: Ansi.java
 MADE BY: pumpkinPatchier
 CREATED: 2023-06-15
 DETAILS: Makes use of ANSI-Escape code in order to colour and format console output.  Its main uses are foreground/background colouring, text-formatting, and cursor control.  Mostly for fun, sometimes for visual clarity and other effects.
  
  NOTES: Adapted from my Python Class
       * See end of file (line 214*) for ANSI ESCAPE CODE basic formatting summary and explanations.

— — — — — — — — — — —
TOOLS SUMMARY:
  rs - reset
   b - bold
   i - italics
  ul - underline

 inv - invert
  st - strikethrough

  up/down/left/right - cursor controls
  nextLine/prevLine - line control (beginning of line)

  clr - clear
  clrAll - clear + scrollback
  clrLine - clear entire line (cursor doesn't move)

  save/load - cursor position

  top - cursor go to 0,0
  go - cursor go to (n,m)
— — — — — — — — — — — **/
import java.util.HashMap;
import java.util.ArrayList;

public class Ansi {
  private static final String UNI = "\u001b[";
  // * Unicode for "escape" key
  
  private HashMap<String, String> format = new HashMap<String, String>();
  // * HashMap similar to dictionary; key/value pairings
  // ^ Restricted access with public get/set vars
  private ArrayList<String> iterable = new ArrayList<String>();
  
  public String getFormat(String key) {
    // * Use to save different style presets or chains of commands under key
    // ^ Assembles format/tool/String sequences, print to use
    return format.get(key);
  }
  public void addFormat(String key, String val) {
    // * Hashmap's setter function, adds format types to 
    this.format.put(key, val);
    // this.listFormat("test");
    // // ^ Use if testing
  }
  public String getIterable(int index) {
    // * Use to save different iterable colours
    // ^ Assembles format/tool/String sequences, print to use
    return iterable.get(index%iterable.size());
    
  }
  public void addIterable(int r, int g, int b) {
    // * ArrayList setter function, iterable
    // ^ Useful for easy-gradients
    this.iterable.add(Foreground.rgb(r,g,b));
    // this.listFormat("test");
    // // ^ Use if testing
  }

  private void listFormat(String test) {
    // * Only used for testing within class,
    // * Iterates through formatting and displays test phrase w/ each format
    HashMap<String, String> map = this.format; 
    for (String i : map.keySet()) {
      System.out.println(Tools.rs + i + ": " + map.get(i) + test);
    }
    System.out.print(Tools.rs);
    // * Reset when finished
  }
  
  private static String sequence(String arg) {
    // * Formats parameter into useable ANSI escape code format
    // * Assemble codes using unicode prefix + arg
    // * Seperate parameters are seperated within arg by semicolon (no semicolon at end)
    return UNI + arg;
  }

  // SGR COLOUR PARAMS //
  // ^ SGR stands for Select Graphics Rendition,
  // ^ aka all the graphic changes, like colour + style
  // ^ suffix m and n denote graphical control sequence
  public static class Foreground {
    // * Codes 30-38
    // * Class to represent the ANSI escape sequences used in controlling the console's Foreground (text) colour.
    // ^ Note that Foreground colouring can appear dull if using a coloured background
    public static final String BLACK = sequence("30m");
    public static final String RED = sequence("31m");
    public static final String GREEN = sequence("32m");
    public static final String YELLOW = sequence("33m");
    public static final String BLUE = sequence("34m");
    public static final String MAGENTA = sequence("35m");
    public static final String CYAN = sequence("36m");
    public static final String WHITE = sequence("37m");
    
    public static String rgb(int r, int g, int b) {
      // * rgb accepts RGB input for 24-bit depth colouring the text.
      return sequence(String.format("38;2;%s;%s;%sm", r, g, b));
      // ^ \u001b[38;2;{r};{g};{b}m
    }
  }

  public static class Background {
    // * Codes 40-48
    // * Class to represent the ANSI escape sequences used in controlling the console's Background (text) colour.
    public static final String BLACK = sequence("40m");
    public static final String RED = sequence("41m");
    public static final String GREEN = sequence("42m");
    public static final String YELLOW = sequence("43m");
    public static final String BLUE = sequence("44m");
    public static final String MAGENTA = sequence("45m");
    public static final String CYAN = sequence("46m");
    public static final String WHITE = sequence("47m");
    
    public static String rgb(int r, int g, int b) {
      // * rgb accepts RGB input for 24-bit depth colouring the text.
      return sequence(String.format("48;2;%s;%s;%sm", r, g, b));
      // ^ \u001b[48;2;{r};{g};{b}m
    }
  }
  
  public static class Tools {
    // * Class to represent the ANSI escape sequences used in controlling certain aspects of the texts formatting.
    // * Although these are technically constants, I'm using camelCase naming convention because they're functionally functions.

    // SGR STYLE PARAMS //
    public static final String rs = sequence("0m");
    // ^ reset
    public static final String b = sequence("1m");
    // ^ bold
    public static final String i = sequence("3m");
    // ^ italics
    public static final String ul = sequence("4m");
    // ^ underline
    
    public static final String inv = sequence("7m");
    // ^ invert
    public static final String st = sequence("9m");
    // ^ strikethrough
  
    // MISC CURSOR PARAMS //
    // * Abbreviation varies, as most of these are seperate functions and codes
    // ^ However, despite being different functions, all these following Tools have to do with cursor control
    // * Seperation is abstract, so they're kept in same class for functional convenience 
    
    // * Move in direction n times
    public static String up(String n) {
      return sequence(n + "A");
    }
    public static String down(String n) {
      return sequence(n + "B");
    }
    public static String right(String n) {
      return sequence(n + "C");
    }
    public static String left(String n) {
      return sequence(n + "D");
    }
    // * If unspecified, move 1 line
    // ^ (Overloading for simplified use)
    public static String up() {
      return sequence("1A");
    }
    public static String down() {
      return sequence("1B");
    }
    public static String right() {
      return sequence("1C");
    }
    public static String left() {
      return sequence("1D");
    }
  
    public static final String nextLine = sequence("1E");
    public static final String prevLine = sequence("1F");
    // * Both of these move cursor to beginning of line.
  
    public static final String clr = sequence("2J");
    public static final String clrAll = sequence("3J");
    // ^ Deletes scrollback buffer
    public static final String clrLine = sequence("2K");
    // ^ Clear sets bg colour
  
    public static final String save = sequence("1s");
    public static final String load = sequence("1u");
    // * Saves and loads cursor location
  
    public static final String top = go(0, 0);
    // ^ Sends cursor to top left position
    
    public static String go(int n, int m) {
      // * Moves the cursor to the n'th row and the m'th column
      return sequence(String.format("%s;%sH", n, m));
      // ^ \u001b[{n};{m}H
    }

    // CURSOR VISIBILITY //
    public static final String show = sequence("?25h");
    public static final String hide = sequence("?25l");
  }
}

/*
ANSI ESCAPE CODE FORMATTING SUMMARY:
  * (Think of START as "calling a function", ARGS as the "function's" arguments that tell it what to do, and END as the "function's" name)
  * For more code/info, see [https://en.wikipedia.org/wiki/ANSI_escape_code]
  ^ (That's where I got my information)

START — — — — — — —
  \u001b[ :: unicode 1b, escape code; starts sequence.
       "[" acts as Control Sequence Introducer (CSI)
ARGS — — — — — — — —
        0 :: reset all styles and colours
        9 :: strikethrough
    30-37 :: set foreground colour
       38 :: set custom foreground colour
    40-47 :: set background colour
       48 :: set custom background colour
ENDING — — — — — — —
        m :: denotes ending
*/
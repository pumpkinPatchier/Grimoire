/** — — — — — — — — —
    NAMED: SpellBook.java
  MADE BY: pumpkinPatchier
  CREATED: 2023-06-15
  DETAILS: The SpellBook class is used to store Spell objects.  Spells are made from a title, level, school of magic, ritual tag (optional), cast time, range, components, duration, and description.  They can be constructed from those parameters, or parsed from raw-text (as long as it complies to a particular structure).  A SpellBook can be sorted by name or by level.  Spells are inspired by tabletop-RPG Dungeons & Dragons 5th edition.
— — — — — — — — — — — **/

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
// ^ Just using reverse from Collections, sort is done from scratch

public class SpellBook {
  ArrayList<Spell> spellsList = new ArrayList<Spell>();
  
  public void getSpellsFromFile(String fileName) {
    // * Convert raw-text from fileName into Spells in SpellBook
    FileHandler fh = new FileHandler(fileName);
    fh.read("\\n\\n");
    // * Split at double-line
    
    for (String str : fh.fileList) {
    // * Convert each raw-text string to its own spell
      this.addSpell(new Spell(str));
    }
  }
  public Spell getSpell(int index) {
    // * Get Spell object at index
    Spell sp = this.spellsList.get(index);
    return sp;
  }
  public String getSpellName(int index) {
    // * Get name of spell at index
    Spell sp = getSpell(index);
    return sp.getSpName();
  }

  public void removeSpell(int index) {
    // * Remove Spell at index
    this.spellsList.remove(index);
  }
  public void emptySpells() {
    // * Clears out SpellBook
    this.spellsList.removeAll(spellsList);
  }
  public void emptySpellsFile(String fileName) {
    // * Clears out file
    FileHandler fh = new FileHandler(fileName);
    fh.clear();
  }
  public void reverseSpells() {
    // * Reverses a SpellBook
    Collections.reverse(this.spellsList);
  }
  
  public void outputSpells(String fileName) {
    FileHandler fh = new FileHandler(fileName);
    for (Spell sp : this.spellsList) {
      fh.write(sp.getRawText() + "\n\n");
    }
  }
  public void outputTemplate(String fileName) {
    FileHandler fh = new FileHandler(fileName);
    Spell sp = new Spell("Spell Template", 0, "evocation/aburation/etc. (put a ritual tag here if you want!)", false, "X actions/minutes/units.", "Self/X feet/-foot radius", "V, S, M (and more materials go here if needed)", "Concentration (up to X hours)/Instantaneous/X minutes/X days", "\tAnd here is where you put your description!  Those are some ideas for what units you can use to fill in your custom spell!  Keep your spells 1 blank line apart (click enter key 2x) to keep your spells from mixing together, and try your best not to mess with the template to prevent errors.\n\tYou can make a new paragraph, but just keep them in the same group as the previous paragraphs.\n\tBasically, only click the enter key once!  Oh, and you don't have to indent if you don't want to.");
    fh.write(sp.getRawText() + "\n\n");
  }
  public void outputBlank(String fileName) {
    FileHandler fh = new FileHandler(fileName);
    Spell sp = new Spell("", 0, "", false, "", "", "", "", "");
    fh.write(sp.getRawText() + "\n\n");
  }

  public int searchSpells(String search) {
    // * Binary sort by name, then search by name
    // ^ If search found, return index
    // ^ If search not found, return -1
    // + arrayKey items need to be sorted ascending
    
    int start = 0;
    int end = this.spellsList.size()-1;
    int mid;
    int searchIndex = -1;
    // ^ Indexes
    SpellNameComparator sn = new SpellNameComparator();
    int searchResult;

    sortName();
    while (start <= end) {
      mid = (start+end)/2;
      searchResult = sn.compare(this.spellsList.get(mid), search);
      if (searchResult < 0) {
        start = mid + 1;
      } else if (searchResult > 0) {
        end = mid - 1;
      } else if (searchResult == 0) {
        searchIndex = mid;
        break;
      }
    }
    return searchIndex;
  } 
  
  public void sortName() {
    // * Sort all spells by name using selection-sort
    SpellNameComparator compareSName = new SpellNameComparator();
    int len = this.spellsList.size();
    
    for (int i=0; i<len-1; i++) {
      // * Move index of unsorted slice of array
      // * Look for min element
      int min_i = i;
       for (int j=i+1; j<len; j++) {
         if (compareSName.compare(this.spellsList.get(j), this.spellsList.get(min_i))<0) {
           min_i = j;
         }
       }
      Spell elem_min = this.spellsList.get(min_i);
      Spell elem_i = this.spellsList.get(i);
      this.spellsList.set(min_i, elem_i);
      this.spellsList.set(i, elem_min);
    }
  }
  public void sortLevel() {
    // * Sort all spells by Level using selection-sort
    SpellLevelComparator compareSLevel = new SpellLevelComparator();
    int len = this.spellsList.size();
    
    for (int i=0; i<len-1; i++) {
      // * Move index of unsorted slice of array
      // * Look for min element
      int min_i = i;
       for (int j=i+1; j<len; j++) {
         if (compareSLevel.compare(this.spellsList.get(j), this.spellsList.get(min_i))<0) {
           min_i = j;
         }
       }
      Spell elem_min = this.spellsList.get(min_i);
      Spell elem_i = this.spellsList.get(i);
      this.spellsList.set(min_i, elem_i);
      this.spellsList.set(i, elem_min);
    }
  }

  public void addSpell(Spell sp) {
    this.spellsList.add(sp);
  }
  public int countSpells() {
    // * Returns spell-count in SpellBook
    // ^ (how many spells there are)
    return spellsList.size();
  }
  private SpellBook topSpells(int spellCap) {
    // * Returns SpellBook of the top [spellCap] slice of spells (index-wise)
    // ^ Technically these spells are on the bottom (0, 1, 2, etc.),
    // ^ But practically, these are the earliest spells and work as top-spells
    SpellBook sb = new SpellBook();
    for (int i=0; i<spellCap; i++) {
      sb.addSpell(getSpell(i));
    }
    return sb;
  }
  
  public void listSpells(int spellCap) {
    // * Formats and prints first [spellCap] spells' details in list-grid format
    // ^ Omits description for compactness (can be found in detailed displaySpell)
    String SPACER = "║";
    SpellBook sb = this.topSpells(spellCap);
    
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> lvls = new ArrayList<String>();
    ArrayList<String> schools = new ArrayList<String>();
    ArrayList<String> cts = new ArrayList<String>();
    ArrayList<String> durs = new ArrayList<String>();
    ArrayList<String> comps = new ArrayList<String>();
    
    // DETERMINE SPACING //
    String titleName = "NAME";
    int longestName = titleName.length();
    String titleLvl = "LVL";
    int longestLvl = titleLvl.length();
    String titleSchool = "SCHOOL";
    int longestSchool = titleSchool.length();
    String titleCastTime = "CAST TIME";
    int longestCastTime = titleCastTime.length();
    String titleDuration = "DURATION";
    int longestDuration = titleDuration.length();
    String titleComponents = "COMPONENTS";
    int longestComponents = titleComponents.length();
    // DETERMINE SPACING //
    for (Spell sp : sb.spellsList) {
      names.add(sp.getSpName());
      lvls.add(Integer.toString(sp.getSpLevel()));
      schools.add(sp.getSpSchool());
      cts.add(sp.getSpCastTime());
      durs.add(sp.getSpDuration());
      comps.add(sp.getSpComponents());
      // * Add each attribute to its list respective
      // * Locate and evaluate longest string per column
      if (sp.getSpName().length() > longestName) {
        // * Cap at 20
        longestName = String.format("%.20s", sp.getSpName()).length();
      }
      if (Integer.toString(sp.getSpLevel()).length() > longestLvl) {
        // * Also turn int to String
        longestLvl = String.format("%.20s", Integer.toString(sp.getSpLevel())).length();
      }
      if (sp.getSpSchool().length() > longestSchool) {
        longestSchool = String.format("%.20s", sp.getSpSchool()).length();
      }
      if (sp.getSpCastTime().length() > longestCastTime) {
        longestCastTime = String.format("%.20s", sp.getSpCastTime()).length();
      }
      if (sp.getSpDuration().length() > longestDuration) {
        longestDuration = String.format("%.15s", sp.getSpDuration()).length();
      }
      if (sp.getSpComponents().length() > longestComponents) {
        longestComponents = String.format("%.20s", sp.getSpComponents()).length();
      }
    }
    // ADD TITLES //
    names.add(titleName);
    lvls.add(titleLvl);
    schools.add(titleSchool);
    cts.add(titleCastTime);
    durs.add(titleDuration);
    comps.add(titleComponents);
    
    // APPLY SPACING //
    for (int i=0; i<names.size(); i++) {
      // * Can take size from any list,
      // ^ can't be spell count because added titles
      names.set(i, String.format("%" + longestName + ".20s", names.get(i)));
      lvls.set(i, String.format("%" + longestLvl + ".20s", lvls.get(i)));
      schools.set(i, String.format("%-" + longestSchool + ".20s", schools.get(i)));
      cts.set(i, String.format("%-" + longestCastTime + ".20s", cts.get(i)));
      durs.set(i, String.format("%-" + longestDuration + ".15s", durs.get(i)));
      comps.set(i, String.format("%-" + longestComponents + ".20s", comps.get(i)));
      // * Set each element to itself with spacing/trim
      // * Cap length (Y in %X.Ys) to prevent overflow
    }

    // PRINTING //
    for (int i=0; i<names.size(); i++) {
      // * i represents spell # in each ArrayList
      // ^ For each, print and count charas for arrayWidth
      int arrayWidth = (longestName + longestLvl + longestSchool + longestCastTime + longestDuration + longestComponents + 19);
      // * End number accounts for spacing

      if (i==0 || i==names.size()) {
        System.out.println("─".repeat(arrayWidth));
        // * I want this border at the top and bottom,
        // ^ but not all in-between
      }
      System.out.printf("%s %s ", SPACER, names.get(i));
      System.out.printf("%s%s  ", SPACER, lvls.get(i));
      // * Spacing on levels is a bit weird because I wanted them sort of centered
      System.out.printf("%s %s ", SPACER, schools.get(i));
      System.out.printf("%s %s ", SPACER, cts.get(i));
      System.out.printf("%s %s ", SPACER, durs.get(i));
      System.out.printf("%s %s %s%n", SPACER, comps.get(i), SPACER);
      System.out.println("¯".repeat(arrayWidth));
    }
  }
  
  private class SpellNameComparator implements Comparator<Spell> {
    // * Compare lexicographically (ascending)
    @Override
    public int compare(Spell a, Spell b) {
      return a.getSpName().compareToIgnoreCase(b.getSpName());
    }
    public int compare(Spell sp, String str) {
      // * Used for search
      return sp.getSpName().compareToIgnoreCase(str);
    }
  }
  private class SpellLevelComparator implements Comparator<Spell> {
    // * Compare by spell level (ascending)
    @Override
    public int compare(Spell a, Spell b) {
      return Integer.compare(a.getSpLevel(), (b.getSpLevel()));
    }
  }
  
  public class Spell {
    Boolean error = false;
    // * True if any errors have occured during creation
    String rawText;
    
    String name;
    int level;
    String school;
    Boolean ritual = false;
    String castTime;
    String range;
    String components;
    String duration;
    String description;

    // CONSTRUCTION //
    private Spell(String text) {
      // * Create spell from raw text
      rawText = text;
      name = readAttribute("spell");
      readLvlSchoolTags();
      // ^ Special attribute exceptions which don't use the typical readAttribute().
      castTime = readAttribute("casting time");
      range = readAttribute("range");
      components = readAttribute("components");
      duration = readAttribute("duration");
      description = readDescription();
    }
    public Spell(String title, int lvl, String som, Boolean rit, String time, String comp, String rn, String dur, String desc) {
      // * Create spell from parameters
      name = title;
      level = lvl;
      school = som;
      ritual = rit;
      castTime = time;
      components = comp;
      range = rn;
      duration = dur;
      description = desc;
      rawText = createRawText();
    }
    private String readAttribute(String search) {
      // * Use regular expressions to read various attributes of a spell.
      // * Search based on attribute and affix reading to Spell's variables
      String output = "";
      Pattern attr = Pattern.compile("(?<=" + search + ":\\W\\b).*", Pattern.CASE_INSENSITIVE);
      // * Start lookbehind at start of string,
      // ^ search for attribute title,
      // ^ catch text after that until end of line
      Matcher match = attr.matcher(this.rawText);
      if(match.find()) {
        output = match.group(0);
        // * Return capture group
      } else {
        this.error = true;
      }
      return output;
    }
    private void readLvlSchoolTags() {
      // * Catches 2nd line (which has a different format than the rest of the attributes).  Contains level, school, and ritual tag info
      Pattern seperate = Pattern.compile("(?<=level )(\\d) - (\\b\\w*\\b)(?: \\((.*?)\\))?");
      // * Capture each piece of info as a different capture group
      Matcher match = seperate.matcher(this.rawText);
      try {
        if(match.find()) {
          this.level = Integer.parseInt(match.group(1));
          this.school = match.group(2);
          if ("ritual".equalsIgnoreCase(match.group(3))) {
            this.ritual = true;
          } else {
            this.ritual = false;
          }
        } else {
          this.error = true;
          // * Something was search-incompatible (incorrectly typed)
        }
      } catch (Exception e) {
        this.error=true;
      }
    }
    private String readDescription() {
      // * Picks up the last paragraphs and description of a spell and stores as string
      String output = "";
      Pattern desc = Pattern.compile("(?<=Duration:)[^\\n]*$\\n(.*)", Pattern.DOTALL | Pattern.MULTILINE);
      // * Catch all characters after the last parameter (duration)
      // ^ DOTALL makes [.] include \n
      // ^ MULTILINE makes [^|$] match start/end of line respectively
      Matcher match = desc.matcher(this.rawText);
      
      if(match.find()) {
        output = match.group(1);
      }
      return output;
    }
    
    // GETTERS //
    private String getSpName() {
      return this.name;
    }
    private int getSpLevel() {
      return this.level;
    }
    private String getSpSchool() {
      return this.school;
    }
    private String getSpCastTime() {
      return this.castTime;
    }
    private String getSpRange() {
      return this.range;
    }
    private String getSpComponents() {
      return this.components;
    }
    private String getSpDuration() {
      return this.duration;
    }
    private String getRawText() {
      return this.rawText;
    }
    private Boolean errorStatus() {
      return this.error;
    }

    // MISC //
    private String createRawText() {
      // * For outputting spells that don't already have raw text version
      String str = ("Spell: " + this.name);
      str = str + String.format("\nlevel %s - %s", this.level, this.school);
      if (this.ritual) {
        str = str + (" (ritual)");
      }
      str = str + String.format("\nCasting Time: " + this.castTime);
      str = str + String.format("\nRange: " + this.range);
      str = str + String.format("\nComponents: " + this.components);
      str = str + String.format("\nDuration: " + this.duration);
      str = str + String.format("\n" + this.description);
      this.rawText = str;
      return str;
    }
    public void displaySpell() {
      // * Display singular spell
      System.out.println(this.createRawText());
    }
  }
}
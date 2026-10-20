/** — — — — — — — — —
   NAMED: UserMenu.java
 MADE BY: pumpkinPatchier
 CREATED: 2023-06-15
 DETAILS: Menu screens, selection, and other navigation.  This handles most input, prompting, and number-ranges.  It makes use of Ansi.java and has optional colour-coding and formatting.  Each instance of UserMenu is essentially  a different theme.
— — — — — — — — — — — **/
import java.util.Scanner;
import java.util.ArrayList;

public class UserMenu {
  Ansi palette = new Ansi();
  public void addColour(int r, int g, int b) {
    // * Adds colour to the palette
    // ^ This palette is used for printing the options and various other implementations
    palette.addIterable(r, g, b);
  }
  public void setPromptFormat(String format) {
    // * Sets format for how prompts should look and print
    palette.addFormat("prompt", format);
  }
  public void setRangeFormat(String format) {
    // * Sets format for how ranges should look and print
    palette.addFormat("range", format);
  }
  public void setInputFormat(String format) {
    // * Sets format for how input should look and print
    palette.addFormat("input", format);
  }
  
  public void setOptionsFormat(String format) {
    // * Sets format for how options should look and print (aside from colours gradient)
    palette.addFormat("options", format);
  }
  
  public String inputString() {
    // * Scan for string
    // * Return string
    Scanner ui = new Scanner(System.in);
    System.out.print(palette.getFormat("input"));
    String name = ui.nextLine();
    System.out.print(Ansi.Tools.rs);
    return name;
  }
  public int inputInt() {
    // * Scan for integer
    // * Repeatedly prompts the user until receiving valid input.
    // * When finished, return integer
    Scanner ui = new Scanner(System.in);
    int input = 0;
    boolean isValid;

    do {
      System.out.print(palette.getFormat("input"));
      isValid=true;
      if(!ui.hasNextInt()){
        System.out.print(palette.getFormat("prompt"));
        System.out.println("Invalid integer, please try again.");
        ui.nextLine();
        isValid=false;
        continue;
      }
      input = ui.nextInt();
    } while (!isValid);
    System.out.print(Ansi.Tools.rs);
    return input;
  }
  
  public class Range {
    private int min;
    private int max;
    String txt;
    
    public Range(int mini, int maxi) {
      min = mini;
      max = maxi;
      updateTxt();
    }

    public int getIntInRange() {
      // * Prompt inputInt until input is within range
      Boolean isValid = true;
      int input;
      do {
        isValid = true;
        input = inputInt();
        if (!this.checkRange(input)) {
          System.out.print(palette.getFormat("prompt"));
          System.out.println("Choice out of range, please select again" + this.txt);
          isValid = false;
          continue;
        }
      } while (!isValid);
      return input;
    }
    
    public String getRange() {
      return this.txt;
    }
    public boolean checkRange(int num) {
      if (num<this.min || num>this.max) {
        return false;
      } else {
        return true;
      }
    }
    public void resetMinMax(int mini, int maxi) {
      this.min = mini;
      this.max = maxi;
      updateTxt();
    }
    private void updateTxt() {
      this.txt = " %s(%s-%s)%s".formatted(palette.getFormat("range"), min, max, Ansi.Tools.rs);
    }
  }
  
  public class Question {
    String prompt;
    ArrayList<String> options = new ArrayList<String>();
    Range r = new Range(1, options.size());
    int selection = 0;

    public Question(String pr) {
      this.prompt = pr;
    }

    public int ask() {
      // * Asks user for options' selection
      // * Repeatedly prompts the user until receiving valid input.
      // * When finished, return choice as int
      System.out.print(palette.getFormat("prompt") + this.prompt);
      System.out.print(palette.getFormat("range") + this.r.getRange());
      System.out.println(Ansi.Tools.rs);
      this.selection = this.r.getIntInRange();
      return this.selection;
    }
    
    public String askString() {
      // * Asks user for String
      System.out.print(palette.getFormat("prompt") + this.prompt);
      System.out.println(Ansi.Tools.rs);
      return inputString();
    }
    
    public void askPromptOnly() {
      // * Ask, just the prompt
      System.out.print(palette.getFormat("prompt") + this.prompt);
      System.out.println(Ansi.Tools.rs);
    }
    
    public void setPrompt(String pr) {
      // * Set the ask-functions' prompt
      this.prompt=pr;
    }
    public void addOption(String opt) {
      // * Adds option to menu
      this.options.add(opt);
      updateOptions();
    }
    public String getOption(int index) {
      // * Return option at index
      return this.options.get(index);
    }
    public void removeOption(int index) {
      // * Remove option at index from menu
      this.options.remove(index);
      updateOptions();
    }
    public void clearOptions() {
      // * Remove every option from menu
      // ^ Useful for having "part 1/2" questions
      while(this.options.size()>0) {
        this.options.remove(0);
      }
    }
    public void printOptions() {
      // * Formats option with number and option-text.
      for (int i = 0; i < this.options.size(); i++) {
        System.out.print(palette.getFormat("options"));
        System.out.print(palette.getIterable(i));
        System.out.printf("%4s", (i+1));
        System.out.print(" — " + this.options.get(i).toUpperCase());
        System.out.println();
      }
    }
    private void updateOptions() {
      // * Updates the registered input-range,
      // ^ based on the number of options to choose from
      r.resetMinMax(1, this.options.size());
    }
  }
}
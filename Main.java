/** — — — — — — — — —
  PROGRAM: Grimoire
  MADE BY: pumpkinPatchier
  CREATED: 2023-06-15
  DETAILS: Program designed to store and organize different spells (fictional ones, like from Dungeons & Dragons 5e) and overall make them easier to use and locate.  This is the Main.java file that brings together functions from SpellBook.java, UserMenu.java, and Ansi.java (though it mainly showcases SpellBook.java and its SpellBook/Spell datatypes).

  NOTES: I used this website to help with some of my later regex
          https://regex101.com/r/
         I literally only found out about it today but it's very helpful for testing and I like it a lot better than what I was doing originally (which was just testing my regex with CTRL+F; that sucked because it kept crashing my computer whenever I did really resource intensive on accident)
— — — — — — — — — — — **/

class Main {
  public static void main(String[] args) {
    // SETUP //
    Boolean runFlag = true;
    String outputFile = "output.txt";
    // ^ Assign output file for spells, can be reset/changed later
    String importFile = "canon_spell_list.txt";
    SpellBook grimoire = new SpellBook();
    FileHandler.lock("canon_spell_list.txt");
    grimoire.getSpellsFromFile("input.txt");
    
    UserMenu um = new UserMenu();
    // * Setting up UserMenu formatting/colours
    // ^ (Yay, rainbow!!!)
    um.addColour(255, 89, 94); // RED
    um.addColour(255, 146, 76); // ORANGE
    um.addColour(255, 202, 58); // YELLOW
    um.addColour(197, 202, 48); // LIME
    um.addColour(138, 201, 38); // GREEN
    um.addColour(82, 166, 117); // JADE
    um.addColour(25, 130, 196); // TEAL
    um.addColour(66, 103, 172); // BLUE
    um.addColour(106, 76, 147); // PURPLE
    um.addColour(215, 107, 166); // PINK
    um.setPromptFormat(Ansi.Tools.rs + Ansi.Tools.b + Ansi.Foreground.WHITE);
    um.setRangeFormat(Ansi.Tools.rs + Ansi.Foreground.rgb(215, 107, 166));
    um.setInputFormat(" ==> " + Ansi.Tools.rs + Ansi.Foreground.rgb(215, 107, 166));
    // ^ I want input and range connected, but I could make them different if I want
    um.setOptionsFormat("");
    // ^ I don't want any different formatting, so I set it to empty string
    
    UserMenu.Question mainMenu = um.new Question("Choose an operation");
    mainMenu.addOption("search spells");
    mainMenu.addOption("sort spells");
    mainMenu.addOption("import spells");
    mainMenu.addOption("output spells");
    mainMenu.addOption("create spell");
    mainMenu.addOption("delete spell");
    mainMenu.addOption("clear spells");
    mainMenu.addOption("list spells");
    mainMenu.addOption("exit");

    // MAIN PROGRAM LOOP //
    while (runFlag) {
      mainMenu.printOptions();
      switch(mainMenu.ask()) {
        case 1: // SEARCH SPELLS
          UserMenu.Question sq = um.new Question("What spell are you looking for?");
          int searchIndex = grimoire.searchSpells(sq.askString());
          System.out.println("Sorting by name for search purposes...");
          if (searchIndex==-1) {
            System.out.println("Couldn't find spell.  Make sure you're spelling the name correctly and that the spell exists.");
          } else {
            System.out.println("Spell found!");
            grimoire.getSpell(searchIndex).displaySpell();
          }
          break;
        case 2: // SORT SPELLS
          UserMenu.Question sortMenu = um.new Question("Sort by spell name or by level?");
          sortMenu.addOption("sort by name");
          sortMenu.addOption("sort by level");
          sortMenu.printOptions();
          switch(sortMenu.ask()) {
            case 1: // NAME
              grimoire.sortName();
              break;
            case 2: // LEVEL
              grimoire.sortLevel();
              break;
          }
          sortMenu.clearOptions();
          sortMenu.setPrompt("Ascending (A->Z, 0->20) or descending (Z->A, 20->0)?");
          sortMenu.addOption("ascending");
          sortMenu.addOption("descending");
          sortMenu.printOptions();
          switch(sortMenu.ask()) {
            case 1: // ASCENDING
              break;
            case 2: // DESCENDING
              grimoire.reverseSpells();
              break;
          }
          System.out.println("Done sorting!");
          break;
        case 3: // IMPORT SPELLS
          UserMenu.Question importMenu = um.new Question("Would you like to import the canon D&D5e spells, or another file?");
          importMenu.addOption("canon spells");
          importMenu.addOption("another file");
          importMenu.printOptions();
          switch(importMenu.ask()) {
            case 1:
              grimoire.getSpellsFromFile("canon_spell_list.txt");
              break;
            case 2:
              Boolean importFileValid = false;
              // * Need valid flag to escape
              while (!importFileValid) {
                importMenu.setPrompt("Enter file name (without file extension)");
                importFile = importMenu.askString() + ".txt";
                FileHandler ip = new FileHandler(importFile, false);
                if (ip.exists()) {
                  // * If exists
                  importFileValid = true;
                } else {
                  System.out.println("Something went wrong, please try again.  Make sure you use a valid file name (without .txt), and that the file you're refencing does exist.");
                }
              }
              grimoire.getSpellsFromFile(importFile);
              System.out.println("Done importing from file.  Those spells should show up in your overall spells list here.  To export them, see OUTPUT SPELLS from the main menu.");
            }
          break;
        case 4: // OUTPUT SPELLS
          UserMenu.Question outputMenu = um.new Question("Would you like to output to pre-existing " + outputFile + " file, or set a new file for output?");
          outputMenu.addOption("output to " + outputFile);
          outputMenu.addOption("set new file");
          outputMenu.printOptions();
          switch (outputMenu.ask()) {
            case 1:
              System.out.println("Directing output to " + outputFile);
              break;
            case 2:
              Boolean outputFileValid = false;
              // * Need valid flag to escape
              while (!outputFileValid) {
                outputMenu.setPrompt("Enter file name (without file extension)");
                outputFile = outputMenu.askString() + ".txt";
                FileHandler op = new FileHandler(outputFile);
                if (op.exists() && op.status()) {
                  // * If exists and writeable
                  System.out.println("Output file successfully set to " + outputFile);
                  outputFileValid = true;
                } else {
                  System.out.println("Something went wrong, please try again.  Make sure you use a valid file name (without .txt), and ensure that you aren't trying to use a locked file (like canon_spell_list.txt)");
                  // * Originally I had it say "only letters/numbers/underscores",
                  // ^ but apparently now you can use all sorts of symbols?
                  // * I did a test-keysmash for a name and thought it was bugging when it said it was valid,
                  // ^ but it worked apparently because replit made that file,
                  // ^ so I honestly don't know the criteria for a valid file-name
                }
              }
              break;
          }
          outputMenu.setPrompt("Output what kind of spell(s) to " + outputFile);
          outputMenu.clearOptions();
          outputMenu.addOption("output all spells");
          outputMenu.addOption("output template spell");
          outputMenu.addOption("output blank spell");
          outputMenu.printOptions();
          switch(outputMenu.ask()) {
            case 1: // ALL SPELLS
              System.out.println("Outputting all spells to " + outputFile);
              grimoire.outputSpells(outputFile);
              System.out.println("Done!  Your new spells are added to the bottom of " + outputFile);
              // ^ Can't reuse message because of plural/singular +  other variations
              break;
            case 2: // TEMPLATE
              System.out.println("Outputting template spell to " + outputFile);
              grimoire.outputTemplate(outputFile);
              System.out.println("Done!  Your new spell template is added to the bottom of " + outputFile + ".  This template is full of helpful hints and examples on how you can make a spell based on D&D5e standards, but bear in mind that you can make spells however you like!  If you want a blank shell for experienced spell-making, then you should consider using the blank spell template instead.");
              break;
            case 3: // BLANK
              System.out.println("Outputting blank spell to " + outputFile + ".  This spell is empty and can be filled-in with whatever valid details you like, which is good for experienced D&D5e players and people who understand this program relatively well.  If you need hints on where to start, then you should consider using the template-spell.  Be careful, if you make mistakes, it can get buggy.");
              grimoire.outputBlank(outputFile);
              break;
          }
          break;
        case 5: // CREATE SPELL
          UserMenu.Question spellWizard = um.new Question("");
          // ^ Pun intended
          UserMenu.Range lvlRange = um.new Range(0, 9);
          System.out.println("For the sake of simplicity and to help newer players, spells made here follow D&D5e rules and logic.  If you want to make something that bends the rules a bit (like an unlisted school of magic), then feel free to make one using a template created from the OUTPUT SPELLS menu.");
          spellWizard.setPrompt("What is your spell's name?");
          String spellName = spellWizard.askString();
          System.out.println("What is your spell's level? " + lvlRange.getRange());
          int spellLvl = lvlRange.getIntInRange();
          spellWizard.setPrompt("Can your spell be casted as a ritual?  A ritual spell is one that can be cast at its base level without expending a spell slot if performing a ritual (which takes 10 times the regular cast-time of a spell).");
          Boolean spellRitual=false;
          // * It wants an initialized value so I just give it a random one since it'll get changed soon anyways
          spellWizard.addOption("yes, it's a ritual spell");
          spellWizard.addOption("no, it's not a ritual spell");
          spellWizard.printOptions();
          switch (spellWizard.ask()) {
            case 1:
              System.out.println("Got it, your spell can be casted as a ritual.  Feel free to add any additional details about this in your spell's description later.");
              spellRitual=true;
              break;
            case 2:
              System.out.println("Got it, your spell cannot be casted as a ritual.");
              spellRitual=false;
              break;
          }

          spellWizard.clearOptions();
          spellWizard.setPrompt("What is your spell's school of magic?");
          spellWizard.addOption("abjuration");
          spellWizard.addOption("alteration");
          spellWizard.addOption("conjuration");
          spellWizard.addOption("divination");
          spellWizard.addOption("enchantment");
          spellWizard.addOption("evocation");
          spellWizard.addOption("illusion");
          spellWizard.addOption("necromancy");
          spellWizard.printOptions();
          String spellSchool = spellWizard.getOption(spellWizard.ask()-1);
          
          spellWizard.setPrompt("What is your spell's cast time?  Some examples of units include minutes, hours, actions.");
          String spellCastTime = spellWizard.askString();
          
          spellWizard.setPrompt("What is your spell's duration?  Some examples of units include minutes, hours, days, actions, instantaneous, concentration.");
          String spellDuration = spellWizard.askString();

          spellWizard.clearOptions();
          spellWizard.setPrompt("What is your spell's components?  What do you need to cast this spell?  This one requires a bit of a longer explanation, so buckle up:");
          spellWizard.addOption("explain please");
          spellWizard.addOption("skip");
          spellWizard.printOptions();

          switch (spellWizard.ask()) {
            case 1:
              System.out.println("Spells often use V(erbal), S(omatic), and/or M(aterial) components (with additional materials listed in braces).\n  Verbal spells require verbal activation,\n  Somatic spells require gestures to be casted.\n  Material spells require certain items to be casted.  But for some reason, this doesn't really mean anything, because apparently if you have a components pouch or an arcane focus, then you can just cast these spells anyways. There is one thing, and that's material items with a listed worth (often in gp, gold pieces).  So if you have a spell that requires a diamond worth at least 1,000gp, then you better cough up those diamonds!  Aside from that, a spell consumes its components only if it says so in the description, otherwise they can be re-used.\n  You list these by V, S, M (and material details go here).  So a spell with vocal and material components could be written as \"V, M (a phoenix feather worth at least 50gp and half a walnut)\"\n  It's okay if you deviate from the typical format though, there's no wizard-authority that'll stop you from inventing spells certain ways... none that I've met at least.");
              break;
            case 2:
              System.out.println("Skipped!");
              break;
          }
          spellWizard.setPrompt("What is your spell's components?  V, S, M (ingredients)");
          String spellComponents = spellWizard.askString();
          spellWizard.setPrompt("What is your spell's range?  Some ideas are X-feet line, cone, sphere, self, target, and others.");
          String spellRange = spellWizard.askString();
          spellWizard.setPrompt("Lastly, give us your spell description!  Go wild for this one, some descriptions are really short and others are really long.  It just depends.  This is where you go into detail about its effects, describe its damage-dice/types, or otherwise just clarify stuff about your spell.");
          String spellDescription = spellWizard.askString();

          System.out.println("Displaying your new spell:");
          SpellBook.Spell newSpell = grimoire.new Spell(spellName, spellLvl, spellSchool, spellRitual, spellCastTime, spellComponents, spellRange, spellDuration, spellDescription);
          newSpell.displaySpell();
          grimoire.addSpell(newSpell);
          break;
        case 6: // DELETE SPELL
          if (grimoire.countSpells()<=0) {
            System.out.println("You don't have any spells to delete.  Try creating or importing some spells before deleting them.");
            break;
          }
          int delIndex = -1;
          UserMenu.Question deleteMenu = um.new Question("Delete spell by name or by index?");
          deleteMenu.addOption("by index");
          deleteMenu.addOption("by name");
          deleteMenu.printOptions();
          switch (deleteMenu.ask()) {
            case 1:
              UserMenu.Range spRange = um.new Range(0, grimoire.countSpells()-1);
              deleteMenu.setPrompt("You have " + grimoire.countSpells() + " spells.  Which are you getting rid of? " + spRange.getRange());
              deleteMenu.askPromptOnly();
              delIndex = spRange.getIntInRange();
              break;
            case 2:
              deleteMenu.setPrompt("What spell will you delete?");
              delIndex = grimoire.searchSpells(deleteMenu.askString());
              System.out.println("Sorting by name for search purposes...");
              if (delIndex==-1) {
                System.out.println("Couldn't find spell.  Deletion cancelled.");
                break;
              } else {
                System.out.println("Spell found!");
              }
              break;
          }
          System.out.println("Deleting " + grimoire.getSpellName(delIndex));
          grimoire.removeSpell(delIndex);
          break;
        case 7: // CLEAR SPELLS
          System.out.println("Clearing out spells...");
          grimoire.emptySpells();
          break;
        case 8: // LIST SPELLS
          int maxSpells = grimoire.countSpells();
          int spellCap = 50;
          if (maxSpells>spellCap) {
            // * Cap spells so we don't flood the console with all spells
            // ^ (Importing spell list brings spellCount to ~350+)
            maxSpells=spellCap;
          }
          UserMenu.Range listRange = um.new Range(0, maxSpells);
          UserMenu.Question lq = um.new Question("You have " + grimoire.countSpells() + " spells.  How many would you like to view? " + listRange.getRange());
          lq.askPromptOnly();
          try {
            grimoire.listSpells(listRange.getIntInRange());
          } catch (Exception e) {
            System.out.println("Oh no!  Something went wrong with your spells :(  Some of them might be broken if you imported some bad spells.  Make sure you're really careful to follow the right format and template so that you don't have anything bad happen.  If you want to see your broken spells, you should OUTPUT or CLEAR them instead, because one or more of these spells don't fit the usual spell template.  If you want some functioning spells, consider using the CREATE-SPELL wizard or importing the working 5e canon spells.");
          }
          break;
        case 9: // EXIT
          runFlag = false;
          break;
      }
    }
  }
}
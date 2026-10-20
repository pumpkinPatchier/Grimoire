// FileHandler class used for handling files.  Can create, read, write, and clear files

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
  public ArrayList<String> fileList = new ArrayList<String>();
  // * .txt seperated into different strings by read()
  private File fileHandled;
  // * This is the file to-be handled

  public FileHandler(String fileName, boolean create) {
    // * FileHandler that doesn't auto-create new file
    File fileTrial = new File(fileName);
    if (create) { 
      create(fileName);
    }
    fileHandled = fileTrial;
  }
  
  public FileHandler(String fileName) {
    // * Assign handler a file.
    // * If no file exists under that name, create new one
    File fileTrial = new File(fileName);
    if (!fileTrial.exists()) {
      create(fileName);
    }
    fileHandled = fileTrial;
  }
  public void setFile(File fileSet) {
    this.fileHandled = fileSet;
  }
  
  private void runTest (File f) {
    // * For testing the states of files
    System.out.println(f.exists());
    System.out.println(f.canRead());
    System.out.println(f.canWrite());
    System.out.println(f.getName());
  } 
  
  public static Boolean create(String name) {
    // * Creates new file under name
    // * Performs and returns true if possible
    try {
      File f = new File(name);
      if (f.createNewFile()) {
        return true;
      } else {
        // * File already exists
        return false;
      }
    } catch(IOException e) {
      // * Error occured
      return false;
    }
  }
  public Boolean read(String delim) {
    // * Seperates file by delimeter
    // * Returns false if encountering error
    try {
      Scanner input = new Scanner(this.fileHandled);
      input.useDelimiter(delim);
      
      while (input.hasNext()) {
        this.fileList.add(input.next());
      }
      input.close();
    } catch(FileNotFoundException e) {
      // * Error occured
      return false;
    }
    return true;
  }
  public Boolean write(String newText) {
    // * Appends text to end of file
    // * Returns false if error encountered
    String fileName = this.fileHandled.getName();
    try {
      FileWriter fw = new FileWriter(fileName, true);
      fw.write(newText);
      fw.close();
    } catch(IOException e) {
      return false;
    }
    return true;
  }
  public Boolean clear() {
    // * Clears file of text (use with caution!)
    // * Returns false if error encountered
    String fileName = this.fileHandled.getName();
    try {
      FileWriter fw = new FileWriter(fileName);
      fw.write("");
      fw.close();
    } catch(IOException e) {
      return false;
    }
    return true;
  }

  public static Boolean lock(String fileName) {
    // * Sets file to read-only mode based on fileName
    try {
      File read = new File(fileName);
      read.setReadOnly();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  public Boolean exists() {
    // * Checks existence of current file
    try {
      return this.fileHandled.exists();
    } catch (Exception e) {
      return false;
    }
  }
  public Boolean status() {
    // * Checks lock-status of current file
    try {
      return this.fileHandled.canWrite();
    } catch (Exception e) {
      return false;
      // * Error can be considered locked, because something went wrong and we probably don't want to write to that
    }
  }
}
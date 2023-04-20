// This program finds gaps in volume numbers in journals from a library spreasheet.
// The program:
//   1. Reads data from a .txt file listing journal ids and holdings
//   2. Parses each holding to identify volume numbers associated with the id.
//   3. Finds holdings with gaps and records where the gaps occur.
//   4. Writes to a idsWithGaps.txt file on separate lines:
//      ids of holdings with gaps and vol numbers between which gaps occur.
//      Each id is separated from numbers with gaps writh a tab.
//      Example: "991026273406676	10-23, 23-39, 39-41, 41-44, 44-54"


import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

class Journal {
    String id;
    ArrayList<Integer> volumes = new ArrayList<>();
}

public class HoldingsWithGaps {

    static ArrayList<Journal> journals = new ArrayList<>();

    static String fileWithData = "idsAndHoldings.txt";

    static ArrayList<String> dataStrings = new ArrayList<>();

    public static void main(String[] args) {

        getDataFromFile ();
        getJournalIdsAndVolNums ();
        findGaps ();

    } // end-main

    
    static void getJournalIdsAndVolNums ()
    {
        for (String str: dataStrings) {

            Journal j = new Journal();
            char[] jChar = str.toCharArray();
            String id = "";
            int ind = 0;

            while (jChar[ind] != '\t') {
                id += jChar[ind];
                ind++;
            }
            j.id = id;
            //System.out.println(title);
            //System.out.println();

            String volNumStr = "";
            while (ind < jChar.length-3) {
                boolean volPresent = false;
                if ( (ind+2 < jChar.length) && (jChar[ind] == 'v') && (jChar[ind+1] == '.') && (jChar[ind+2] == ' ') ) {
                    volPresent = true;
                    volNumStr = "";
                    ind += 3;
                    while ( (ind < jChar.length) && (jChar[ind] >= '0') && (jChar[ind] <= '9') ) {
                        volNumStr += jChar[ind];
                        //System.out.println ("Current ind = " + ind);
                        //System.out.println ("Current volNumStr = " + volNumStr);
                        ind++;
                    }
                }

                if (volPresent) {
                    //System.out.print ("vol num in str = " + volNumStr);
                    if (volNumStr != "") {
                        int volNumInt = Integer.parseInt(volNumStr);
                        //System.out.print(" --> and in INT = " + volNumInt);
                        j.volumes.add(volNumInt);
                    }
                    //System.out.println();

                }
                else {
                    ind++;
                }
            } // end-while volNum

            journals.add(j);

        } // end-for loop
    } //end getJournalIdsandVolnums

    
    static void findGaps ()
    {
	int jWithGapsCount = 0;
	    
	String data = "";
	data += "FOUND VOLUME GAPS IN\n";
        //System.out.println("VOLUME GAPS IN");
	
        for (Journal j: journals) {
            boolean hasGaps = false;

            for (int i=0; i<j.volumes.size()-1; i++) {
                int curVol = j.volumes.get(i);
                int nexVol = j.volumes.get(i+1);
                if (curVol+1 != nexVol) {
                    hasGaps = true;
		    jWithGapsCount ++;
                }
            }

            if (hasGaps) {
		data += j.id + "\t";
                //System.out.print(j.id + "\t");
                boolean firstGap = true;
                for (int i=0; i<j.volumes.size()-1; i++) {
                    int curVol = j.volumes.get(i);
                    int nextVol = j.volumes.get(i+1);
                    if (curVol+1 != nextVol) {
                        if (firstGap) {
			    data += curVol + "-" + nextVol;
                            //System.out.print (curVol + "-" + nextVol);
                            firstGap = false;
                        }
                        else {
			    data += ", " + curVol + "-" + nextVol;
                            //System.out.print (", " + curVol + "-" + nextVol);
                        }

                    }
                }
		data += "\n";
                //System.out.println();
            }
        } // end-for each j in journals

        data += "END OF FILE. " + jWithGapsCount + " journals have gaps in volume numbers.\n";
	writeToFile (data);
	
    } // end-findGaps()


    static void writeToFile (String data)
    {
	try {
	    FileWriter myWriter = new FileWriter("idsAndGaps.txt");
	    myWriter.write (data);
	    myWriter.close ();
	} catch (IOException e) {
	    System.out.println("An error occurred.");
	    e.printStackTrace();
	}
    }

    
    static void createFile ()
    {
	try {
	    File myObj = new File("idsAndGaps.txt");
	    if (myObj.createNewFile()) {
		System.out.println("File created: " + myObj.getName());
	    } else {
		System.out.println("File already exists.");
	    }
	} catch (IOException e) {
	    System.out.println("An error occurred.");
	    e.printStackTrace();
	}
    }

    static void getDataFromFile ()
    {
        try {
            File myObj = new File(fileWithData);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                dataStrings.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } // end-getIdStringsFromFile ()

} // end-HoldingWithGaps

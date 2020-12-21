import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//TODO: FOR PART II: BACKTRACKING - WE WANT TO FIND ALL POSSIBLE SUBSETS AND CHECK THEIR SUMS
public class Main {

    private static boolean checkSumOfSubset(List<BigInteger> subset, BigInteger target) {
        BigInteger sum = new BigInteger("0");
        for (BigInteger number : subset) {
            sum = sum.add(number);
        }

        return sum.equals(target);
    }

    private static BigInteger checkSumOfAllSubsets(ArrayList<BigInteger> input, BigInteger target) {
        for (int size = 1; size <= input.size(); size++) {
            //if excl: + 1
            for (int i = 0; i < input.size() - size; i++) {
                List<BigInteger> subset = input.subList(i, i + size + 1);
                if (checkSumOfSubset(subset, target)) {
                    return Collections.min(subset).add(Collections.max(subset));
                }
            }
        }

        return null;
    }

    private static boolean check_number(ArrayList<BigInteger> preamble, BigInteger number) {
        for (int i = 0; i < preamble.size(); i++) {
            for (int j = i; j < preamble.size(); j++) {
                if (preamble.get(i).add(preamble.get(j)).equals(number)) {
                    return true;
                }
            }
        }

        return false;
    }
    public static void main(String[] args) {
        BigInteger first_invalid = new BigInteger("0");
        //PART I
        ArrayList<BigInteger> preamble = new ArrayList<>();
        try {
            File input = new File("inputChallenge9.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();

                if (!row.equals("")) {
                    BigInteger newNumber = new BigInteger(row);

                    if (preamble.size() == 25) {
                        boolean valid = check_number(preamble, newNumber);
                        if (!valid) {
                            first_invalid = newNumber;
                            System.out.println("Result Part I: " + newNumber);
                            break;
                        }
                        preamble.remove(0);
                    }

                    preamble.add(newNumber);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //PART II
        ArrayList<BigInteger> input_numbers = new ArrayList<>();
        try {
            File input = new File("inputChallenge9.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();

                if (!row.equals("")) {
                    BigInteger newNumber = new BigInteger(row);
                    input_numbers.add(newNumber);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BigInteger result = checkSumOfAllSubsets(input_numbers, first_invalid);

        System.out.println("Result Part II: " + result);

    }
}

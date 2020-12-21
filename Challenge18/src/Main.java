import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
  51 51
  26 46
  437 1445
  12240 669060
  13632 23340
  393192
 */

public class Main {

    private static ArrayList<String> getSymbolsAsArrayList(String expression) {
        int i = 0;
        ArrayList<String> result = new ArrayList<>();
        result.add("");
        for (int j = 0; j < expression.length(); j++) {
            String symbol = expression.substring(j, j + 1);
            if (symbol.equals(" ")) {
                if (!result.get(i).equals("")) {
                    i++;
                    result.add(i, "");
                }
            } else if (symbol.equals(")") || symbol.equals("(")) {
                if (!result.get(i).equals("")) {
                    i++;
                }
                result.add(i, symbol);
                i++;
                result.add(i, "");
            } else {
                result.add(i, result.get(i).concat(symbol));
            }
        }

        result.removeAll(Arrays.asList("", null));
        return result;
    }

    private static String compute_subexpression_PartII(ArrayList<String> subexpression) {
        ArrayList<String> updated = new ArrayList<>();
        for (int i = 0; i < subexpression.size(); i++) {
            String symbol = subexpression.get(i);
            updated.add(i, symbol);
        }

        while (updated.contains("+")) {
            int index = updated.indexOf("+");
            BigInteger intermediate = new BigInteger(updated.get(index - 1)).add(new BigInteger(updated.get(index + 1)));
            updated.add(index - 1, String.valueOf(intermediate));
            updated.remove(index + 2);
            updated.remove(index + 1);
            updated.remove(index);
        }

        BigInteger result = new BigInteger(updated.get(0));
        for (int i = 1; i < updated.size(); i++) {
            if (updated.get(i).equals("*")) {
                result = result.multiply(new BigInteger(updated.get(i + 1)));
            }
        }

        return result.toString();
    }

    private static String compute_subexpression_PartI(ArrayList<String> subexpression) {
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<BigInteger> values = new ArrayList<>();
        for (int i = 0; i < subexpression.size(); i++) {
            String symbol = subexpression.get(i);
            if (symbol.equals("+") || symbol.equals("*")) {
                operators.add(symbol);
            } else {
                values.add(new BigInteger(symbol));
            }
        }
        BigInteger result = values.get(0);
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("+")) {
                result = result.add(values.get(i + 1));
            } else if (operators.get(i).equals("*")) {
                result = result.multiply(values.get(i + 1));
            } else {
                System.out.println("Something went wrong when computing the value of this subexpression " + subexpression);
            }
        }
        return result.toString();
    }

    private static BigInteger process_expression(ArrayList<String> expression) {
        if (expression.size() == 1) {
            return new BigInteger(expression.get(0));
        }

        int open = 0;
        int opening_index = -1;
        int closing_index = -1;

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i).equals("(")) {
                opening_index = i;
            } else if (expression.get(i).equals(")")) {
                closing_index = i;
                break;
            }
        }

        if ((opening_index == -1 && closing_index == -1) || (opening_index == 0 && closing_index == expression.size())) {
            String value_PartI = compute_subexpression_PartI(expression);
            String value = compute_subexpression_PartII(expression);
            return new BigInteger(value);
        } else {
            ArrayList<String> arg = new ArrayList<>();
            for (int i = 0; i < (closing_index - opening_index - 1); i++) {
                arg.add(i, expression.get(i + opening_index + 1));
            }
            String value_PartI = compute_subexpression_PartI(arg);
            String value = compute_subexpression_PartII(arg);
            ArrayList<String> newExpression = new ArrayList<>();
            for (int i = 0; i < opening_index; i++) {
                newExpression.add(i, expression.get(i));
            }
            newExpression.add(opening_index, value);
            int temp = newExpression.size();
            for (int i = 0; i < expression.size() - closing_index - 1; i++) {
                newExpression.add(temp + i, expression.get(closing_index + i + 1));
            }
            return process_expression(newExpression);
        }
    }

    public static void main(String[] args) {
        BigInteger sum = new BigInteger("0");

        try {
            File input = new File("inputChallenge18.txt");
            //File input = new File("simpleInput.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                if (!row.equals("")) {
                    ArrayList<String> expression = getSymbolsAsArrayList(row);
                    BigInteger result = process_expression(expression);
                    //System.out.println("Result of a row: " + result);
                    sum = sum.add(result);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //System.out.println("Result Part I: " + sum);
        System.out.println("Result Part II: " + sum);
    }

}

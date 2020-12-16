import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

public class Main {

    private static ArrayList<Integer> valid_values_Part_I;
    private static int invalid;
    private static HashMap<String, ArrayList<Integer>> valid_values;
    private static HashMap<String, ArrayList<Integer>> possible_indices;
    private static String my_ticket;

    private static ArrayList <Integer> removeDuplicates(ArrayList<Integer> list) {
        //System.out.println(list.size());
        LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(list);
        return new ArrayList<>(hashSet);
        //System.out.println(list.size());
    }

    private static void process_notes_Part_I(String entry) {
        String[] words = entry.split(" ");

        for (String word : words) {
            if (word.contains("-")) {
                Integer lowerBound = Integer.parseInt(word.split("-")[0]);
                Integer upperBound = Integer.parseInt(word.split("-")[1]);
                while (lowerBound <= upperBound) {
                    valid_values_Part_I.add(lowerBound);
                    lowerBound++;
                }
            }
        }

    }

    private static void process_row_Part_I(String entry) {
        String[] values = entry.split(",");
        for (String value : values) {
            if (!valid_values_Part_I.contains(Integer.parseInt(value))){
                invalid = invalid + Integer.parseInt(value);
            }
        }
    }

    private static void initialize_possible_indices() {
        possible_indices = new HashMap<>();

        int n = valid_values.size();

        for (Map.Entry entry : valid_values.entrySet()) {
            //System.out.println(entry.getKey().getClass());
            possible_indices.put((String) entry.getKey(), new ArrayList<>());
            for (int i = 0; i < n; i++) {
                possible_indices.get(entry.getKey()).add(i);
            }

        }
    }

    private static boolean valid_ticket(String entry) {
        String[] values = entry.split(",");
        if (values.length != valid_values.size()) {
            return false;
        }

        for (String value : values) {
            if (!valid_values_Part_I.contains(Integer.parseInt(value))){
                return false;
            }
        }
        return true;
    }

    private static void process_notes_Part_II(String entry) {
        String[] words = entry.split(":");
        String[] numberRanges = words[1].split(" ");
        ArrayList<Integer> number_values = new ArrayList<>();

        for (String word : numberRanges) {
            if (word.contains("-")) {
                Integer lowerBound = Integer.parseInt(word.split("-")[0]);
                Integer upperBound = Integer.parseInt(word.split("-")[1]);
                while (lowerBound <= upperBound) {
                    number_values.add(lowerBound);
                    lowerBound++;
                }
            }
        }

        valid_values.put(words[0], number_values);
    }

    private static void process_row_Part_II(String entry) {
        String[] ticket_values = entry.split(",");
        for (int i = 0; i < ticket_values.length; i++) {
            for (Map.Entry valid_value_range : valid_values.entrySet()) {
                if (!((ArrayList) valid_value_range.getValue()).contains(Integer.parseInt(ticket_values[i]))){
                    ((ArrayList) possible_indices.get(valid_value_range.getKey())).remove((Integer) i);
                }
            }
        }
    }

    private static void get_final_indices() {
        int counter = 0;
        for (Map.Entry entry : possible_indices.entrySet()) {
            counter = counter + ((ArrayList)entry.getValue()).size();
        }
        System.out.println(counter);

        while (counter > possible_indices.size()) {
            for (Map.Entry entry : possible_indices.entrySet()) {
                if (((ArrayList)entry.getValue()).size() == 1) {
                    for (Map.Entry entry1 : possible_indices.entrySet()) {
                        if (((ArrayList)entry1.getValue()).contains(((ArrayList)entry.getValue()).get(0)) && entry != entry1) {
                            //System.out.println(((ArrayList) entry.getValue()).get(0));
                            ((ArrayList)entry1.getValue()).remove(((ArrayList)entry.getValue()).get(0));
                        }
                    }
                    counter = counter - 1;
                    //break;
                }
            }
        }
    }

    private static void process_my_ticket() {
        BigInteger result = new BigInteger("1");
        String[] ticket = my_ticket.split(",");
        for (Map.Entry field : possible_indices.entrySet()) {
            String[] name = ((String) field.getKey()).split(" ");
            if (name.length == 1) {
                continue;
            }

            if (name[0].equals("departure")) {
                result = result.multiply(new BigInteger(ticket[(int) ((ArrayList) field.getValue()).get(0)]));
            }
        }

        System.out.println("Result Part II: " + result);
    }

    public static void main(String[] args) {
        valid_values_Part_I = new ArrayList<>();
        valid_values = new HashMap<>();
        invalid = 0;
        boolean nearbyTickets = false;
        boolean myTicket = false;
        try {
            File input = new File("inputChallenge16.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                if (row.equals("your ticket:")) {
                    System.out.println("my ticket");
                    myTicket = true;
                } else if (row.equals("nearby tickets:")) {
                    myTicket = false;
                    nearbyTickets = true;
                    //Part I stuff
                    valid_values_Part_I = removeDuplicates(valid_values_Part_I);
                    Collections.sort(valid_values_Part_I);
                    //Part II stuff
                    initialize_possible_indices();
                } else if (!row.equals("") && myTicket) {
                    my_ticket = row;
                } else if (!row.equals("") && nearbyTickets) {
                    //Part I stuff
                    process_row_Part_I(row);
                    //Part II stuff
                    if (valid_ticket(row)) {
                        process_row_Part_II(row);
                    }
                } else if (!row.equals("")) {
                    process_notes_Part_I(row);
                    process_notes_Part_II(row);
                }
            }

            System.out.println(invalid);

            get_final_indices();

            for (Map.Entry entry : possible_indices.entrySet()) {
                System.out.println(entry.getKey());
                for (Object indices : (ArrayList) entry.getValue()) {
                    System.out.println(indices);
                }
            }

            process_my_ticket();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

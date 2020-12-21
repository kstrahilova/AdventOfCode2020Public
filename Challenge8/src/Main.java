import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static int accumulator;

    private static boolean run_program(ArrayList<Integer> already_seen_indices, ArrayList<String> program) {
        int index = 0;
        while (index < program.size()) {
            if (already_seen_indices.contains(index)) {
                return false;
            }

            already_seen_indices.add(index);
            String[] command = program.get(index).split(" ");

            if (command[0].equals("jmp")) {
                index += Integer.parseInt(command[1]);
                continue;
            }

            if (command[0].equals("acc")) {
                accumulator += Integer.parseInt(command[1]);
            }

            index++;
        }

        return true;
    }

    //have a while loop that continuously changes a command, checks if it works, if not changes it back, sets a last_changed and continues
    private static void fix_program(ArrayList<String> program) {
        boolean not_good = true;
        int last_changed = -1;
        while (not_good) {
            if (last_changed >= program.size()) {
                System.out.println("problem");
                return;
            }
            for (int i = last_changed + 1; i < program.size(); i++) {
                String[] command = program.get(i).split(" ");
                if (command[0].equals("jmp")) {
                    String newCommand = "nop".concat(" ").concat(command[1]);
                    program.set(i, newCommand);
                    last_changed = i;
                    break;
                } else if (command[0].equals("nop")) {
                    String newCommand = "jmp".concat(" ").concat(command[1]);
                    program.set(i, newCommand);
                    last_changed = i;
                    break;
                }

            }

            accumulator = 0;
            not_good = !run_program(new ArrayList<>(), program);

            if (not_good) {
                String[] command = program.get(last_changed).split(" ");
                if (command[0].equals("jmp")) {
                    String newCommand = "nop".concat(" ").concat(command[1]);
                    program.set(last_changed, newCommand);
                } else if (command[0].equals("nop")) {
                    String newCommand = "jmp".concat(" ").concat(command[1]);
                    program.set(last_changed, newCommand);
                }
            }
        }
    }

    public static void main(String[]  args) {
        accumulator = 0;
        ArrayList<String> program;
        program = new ArrayList<>();
        ArrayList<Integer> already_seen_indices = new ArrayList<>();

        try {
            File input = new File("inputChallenge8.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                if (!row.equals("")) {
                    program.add(row);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        run_program(already_seen_indices, program);
        System.out.println("Result Part I: " + accumulator);

        fix_program(program);
        System.out.println("Result Part II: " + accumulator);
    }

}

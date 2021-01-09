import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.util.Scanner;
import java.lang.String;
import java.io.File;

public class Main {
    public static void main(String[] args) throws IOException {
        File input = new File("C:\\Users\\Antonella\\IdeaProjects\\Tema1AnotherCopy\\queue.in.txt");
        Scanner scanner = new Scanner(input);
        String[] tokens;
        Family[] families_vector = new Family[20];
        Group[] groups_vector = new Group[20];
        Pasager[] singles_vector = new Pasager[30];
        Pasager temp;
        int pasagerNumeber;

        tokens = scanner.nextLine().split(" ");
        pasagerNumeber = Integer.parseInt(tokens[0]);

        for (int i = 0; i < pasagerNumeber; i++){

            tokens = scanner.nextLine().split(" ");
            String s = tokens[0].substring(1);
            int val = Integer.parseInt(s) - 1;
            String id = tokens[0];
            String name = tokens[1];
            int age = Integer.parseInt(tokens[2]);
            char tipe_ticket = tokens[3].charAt(0);
            boolean priority_embark = Boolean.parseBoolean(tokens[4]);
            boolean special_needs = Boolean.parseBoolean(tokens[5]);

            temp = new Pasager(id, name, age,
                    tipe_ticket, priority_embark, special_needs);

            switch (tokens[0].charAt(0)){
                case 'f':
                    if (families_vector[val] == null)
                        families_vector[val] = new Family(id, name, age,
                                tipe_ticket, priority_embark, special_needs);
                    families_vector[val].add(temp);
                    break;
                case 'g':
                    if (groups_vector[val] == null) {
                        groups_vector[val] = new Group(id, name, age,
                                tipe_ticket, priority_embark, special_needs);
                    }
                    groups_vector[val].add(temp);
                    break;
                case 's':
                    singles_vector[val] = temp;
                    singles_vector[val].idx++;
                    break;

                default:
                    System.out.println("'" + temp.getId().charAt(0) + "'" +
                            " - tip bilet inexistent");
            }
        }
        Queue queue = new Queue(pasagerNumeber);
        int priority, notFirstList = 0;
        queue.output = new PrintWriter("queue.out");
        queue.output.close();
        queue.output = new PrintWriter("queue.out");

        while(scanner.hasNextLine()){
            tokens = scanner.nextLine().split(" ");

            if(tokens[0].equals("insert")){
                String s = tokens[1].substring(1);
                int val = Integer.parseInt(s) - 1;

                switch (tokens[1].charAt(0)){
                    case 'f':
                        priority = queue.calcPriority(families_vector[val]);
                        queue.insert(families_vector[val], priority);
                        break;
                    case 'g':
                        priority = queue.calcPriority(groups_vector[val]);
                        queue.insert(groups_vector[val], priority);
                        break;
                    case 's':
                        priority = queue.calcPriority(singles_vector[val]);
                       queue.insert(singles_vector[val], priority);
                        break;
                    default:
                }
            }
            if(tokens[0].equals("embark"))
                queue.embark();
            if(tokens[0].equals("list")){
                if (notFirstList == 1)
                    queue.output.println();
                notFirstList = 1;
                queue.list();
            }
            if(tokens[0].equals("delete")){
                String id = tokens[1];

                if(tokens.length == 2 || id.charAt(0) == 's')
                    queue.delete(id);
                else {
                    String name = tokens[2];
                    String s = id.substring(1);
                    int val = Integer.parseInt(s) -1;
                    int pos;

                    if(id.charAt(0) == 'f'){
                        families_vector[val].deletePerson(name);
                        priority = queue.calcPriority(families_vector[val]);
                        queue.setPriority(families_vector[val], priority);
                        pos = queue.findPasager(id);
                        queue.sort(pos);
                    }
                    if(id.charAt(0) == 'g'){
                        groups_vector[val].deletePerson(name);
                        priority = queue.calcPriority(groups_vector[val]);
                        queue.setPriority(groups_vector[val], priority);
                        pos = queue.findPasager(id);
                        queue.sort(pos);
                    }
                }
            }
        }
        scanner.close();
        queue.output.close();
    }
}
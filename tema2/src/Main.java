import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /**
     * se calculeaza media
     * @param arr => vectorul cu valorile minime ale
     *            temperaturii/maxime ale umiditatii
     * @param arr2 => vectorul cu suprafetele camerelor
     * @return => media
     */
    static Double calcAverage(ArrayList<Double> arr, ArrayList<Double> arr2){
        Double sum1 = 0.0;
        Double sum2 = 0.0;

        for (int i = 0; i < arr.size(); i++) {
            sum1 += arr.get(i) * arr2.get(i);
            sum2 += arr2.get(i);
        }

        return sum1/sum2;
    }

    public static void main(String[] args) throws IOException {

        File input = new File("therm.in");
        PrintWriter output = new PrintWriter("therm.out");
        Scanner scanner = new Scanner(input);
        ArrayList<Room> room_array = new ArrayList<Room>();
        String[] tokens;
        int rooms_num;
        Double global_temperature;
        Double global_humidity = -1.0;
        int timestamp_reference;
        double erorr = 0.05;

        tokens = scanner.nextLine().split(" ");
        if (tokens.length == 3) {
            rooms_num = Integer.parseInt(tokens[0]);
            global_temperature = Double.parseDouble(tokens[1]);
            timestamp_reference = Integer.parseInt(tokens[2]);
        }
        else {
            rooms_num = Integer.parseInt(tokens[0]);
            global_temperature = Double.parseDouble(tokens[1]);
            global_humidity = Double.parseDouble(tokens[2]);
            timestamp_reference = Integer.parseInt(tokens[3]);
        }


        for (int i = 0; i < rooms_num; i++){
            tokens = scanner.nextLine().split(" ");
            String device_id = tokens[1];
            Double surface = Double.parseDouble(tokens[2]);
            Room tmp_room = new Room(device_id, surface);

            room_array.add(tmp_room);
        }

        while (scanner.hasNextLine()){
            tokens = scanner.nextLine().split(" ");

            if (tokens[0].equals("OBSERVE")){
                for (int i = 0; i < room_array.size(); i++)
                    if(room_array.get(i).device_id.equals(tokens[1])){
                        room_array.get(i).observe(Integer.parseInt(tokens[2]),
                            timestamp_reference, Double.parseDouble(tokens[3]));
                        break;
                    }

            }
            if (tokens[0].equals("OBSERVEH")){
                for (int i = 0; i < room_array.size(); i++)
                    if(room_array.get(i).device_id.equals(tokens[1])){
                        room_array.get(i).observeH(Integer.parseInt(tokens[2]),
                            timestamp_reference, Double.parseDouble(tokens[3]));
                        break;
                    }
            }
            if (tokens[0].equals("TRIGGER")){
                ArrayList<Double> min = new ArrayList<>();
                ArrayList<Double> surface = new ArrayList<>();
                ArrayList<Double> max = new ArrayList<>();

                for (int i = 0; i < rooms_num; i++){
                    if (room_array.get(i).temperatures.get(23).size() != 0) {
                        min.add(room_array.get(i).temperatures.get(23).get(0));
                        if(global_humidity != 0.0 && room_array.get(i).humidities.get(23).size() != 0) {
                            max.add(room_array.get(i).humidities.
                                    get(23).get(0));
                        }
                        surface.add(room_array.get(i).surface);
                    }
                }

                Double temp_average = calcAverage(min, surface);
                Double hum_average = 0.0;
                if(global_humidity != -1.0) {
                    hum_average = calcAverage(max, surface);
                }

                if (global_temperature - temp_average < erorr) {
                    output.println("NO");
                }
                else {
                    if (global_humidity != -1.0 &&
                            (hum_average <= global_humidity)) {
                        output.println("NO");
                    }
                    else {
                        output.println("YES");
                        }

                }

            }
            if (tokens[0].equals("TEMPERATURE")){
                global_temperature = Double.parseDouble(tokens[1]);
            }
            if (tokens[0].equals("LIST")){
                int room_idx = Integer.parseInt(String.
                    valueOf(tokens[1].charAt(4))) -1;
                output.print(tokens[1]);
                room_array.get(room_idx).list(Integer.parseInt(tokens[2]),
                    Integer.parseInt(tokens[3]), timestamp_reference, output);
                output.println();
            }
        }
        scanner.close();
        output.close();
    }
}

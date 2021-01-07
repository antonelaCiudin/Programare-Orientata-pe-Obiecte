import java.io.PrintWriter;
import java.util.ArrayList;

public class Room {
    String device_id;
    Double surface;
    ArrayList<ArrayList<Double>> temperatures = new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> humidities = new ArrayList<ArrayList<Double>>();

    /**
     * constructorul pentru o camera
     * @param device_id => id-ul device-ul
     * @param surface => suprafata camerei
     */
    public Room(String device_id, Double surface) {
        this.device_id = device_id;
        this.surface = surface;

        for (int i = 0; i < 24; i++) {
            temperatures.add(new ArrayList<Double>());
            humidities.add(new ArrayList<Double>());
        }
    }

    /**
     * calculeaza intervalul de timp unde s-a efectuat OBSERVE
     * @param timestamp_reference => timpul referinta
     * @param timestamp => timpul actual de la OBSERVE
     * @return => intervalul
     */
    public int calculateTimeInterval(int timestamp_reference, int timestamp){
        for (int i = 0; i < 24; i++){
            if(timestamp < timestamp_reference-3600*i
                    && timestamp >= timestamp_reference-3600*(i+1) )
                return 23-i;
        }
        return -1;
    }

    /**
     * sorteaza crescator temperaturile dintr-un interval
     * @param interval => intervalul in care se sorteaza
     */
    void sortTemp(int interval){
        for (int i = 0; i < temperatures.get(interval).size(); i++)
            for (int j = i; j < temperatures.get(interval).size(); j++)
                if (temperatures.get(interval).get(i) > temperatures.get(interval).get(j)) {
                    Double tmp = temperatures.get(interval).get(j);
                    temperatures.get(interval).set(j, temperatures.get(interval).get(i));
                    temperatures.get(interval).set(i, tmp);
                }
    }

    /**
     * sorteaza descrescator umiditatile dintr-un interval
     * @param interval => intervalul in care se sorteaza
     */
    void sortHum(int interval){
        for (int i = 0; i < humidities.get(interval).size(); i++)
            for (int j = i; j < humidities.get(interval).size(); j++)
                if (humidities.get(interval).get(i) > humidities.get(interval).get(j)) {
                    Double tmp = humidities.get(interval).get(j);
                    humidities.get(interval).set(j, humidities.get(interval).get(i));
                    humidities.get(interval).set(i, tmp);
                }
    }

    /**
     * functia pentru comanda observe la temperatura
     * @param timestamp => timpul la care se face OBSERVE
     * @param timestamp_reference => timpul referinta
     * @param temperature => temperatura observata
     */
    void observe(int timestamp, int timestamp_reference, Double temperature){
        int time_interval = calculateTimeInterval(timestamp_reference, timestamp);

        if(time_interval != -1) {
            temperatures.get(time_interval).add(temperature);
            sortTemp(time_interval);
        }
    }

    /**
     * functia pentru comanda observe la umiditate
     * @param timestamp => timpul la care se face OBSERVEH
     * @param timestamp_reference => timpul referinta
     * @param humidity => temperatura observata
     */
    void observeH(int timestamp, int timestamp_reference, Double humidity){
        int time_interval = calculateTimeInterval(timestamp_reference, timestamp);

        if(time_interval != -1) {
            humidities.get(time_interval).add(humidity);
            sortHum(time_interval);
        }
    }

    /**
     * sterge valorile care se repeta din intervalul de timp dat
     * @param start => timpul initial al intervalului
     * @param finish => timpul final al intervalului
     */
    void delete_Rep(int start, int finish){
        for (int i = start; i <=finish; i++)
            for (int j = 0; j < temperatures.get(i).size()-1; j++){
                if (temperatures.get(i).get(j).equals(temperatures.get(i).get(j+1))){
                    temperatures.get(i).remove(j);
                }
            }
    }

    /**
     * printeaza temperaturile din intervalul de timp dat
     * @param timestamp1 => timpul initial al intervalului
     * @param timestamp2 => timpul final al intervalului
     * @param timestamp_reference => timpul de referinta
     * @param output => file-ul output in care se printeaza
     */
    void list(int timestamp1, int timestamp2, int timestamp_reference, PrintWriter output){
        int first_idx = 24-(timestamp_reference- timestamp1)/3600;
        int last_idx = 23-(timestamp_reference-timestamp2)/3600;

        for (int i = last_idx; i >=first_idx; i--) {
            delete_Rep(first_idx, last_idx);
            for (int j = 0; j < temperatures.get(i).size(); j++) {
                String string = String.format("%.2f", temperatures.get(i).get(j));
                output.print(" " + string);
            }
        }
    }
}

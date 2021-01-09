import java.io.PrintWriter;

public class Queue {
    private Pasager[] queue;
    private int dimension;
    private int maxDim;
    PrintWriter output;

    /**
     *
     * @param maxDim - numarul maxim de pasageri
     */
    Queue(int maxDim){
        this.maxDim = maxDim;
        this.dimension = 0;
        this.queue = new Pasager[maxDim];
    }

    /**
     * intoarce prioritatea in functie de bilet
     * @param c => tipul biletului
     * @return => prioritatea biletului
     */
    int tipeTicket(char c){
        switch (c) {
            case 'b':
                return 35;
            case 'p':
                return 20;
            default: return 0;
        }
    }

    /**
     * mareste prioritatea in functie de varsta
     * @param v => varsta
     * @return => prioritatea dupa varsta
     */
    int age(int v){
        if(v < 2)
            return 20;
        if(v >= 2 &&
                v < 5)
            return 10;
        if(v >= 5 &&
                v < 10)
            return 5;
        if(v >= 60)
            return 15;
        return 0;
    }

    /**
     * calculeaza prioritatea
     * @param p => pasagerul
     * @return => prioritatea calculata
     */
    int calcPriority(Pasager p){
        int tempP = 0;

        if(p instanceof Family){
            tempP += 10;

            for(int i = 0; i < ((Family)p).idx; i++) {
                tempP += tipeTicket(((Family)p).family[i].getTipe_ticket());
                tempP += age(((Family)p).family[i].getAge());

                if(((Family) p).family[i].getPriority_embark())
                    tempP += 30;
                if(((Family) p).family[i].getSpecial_needs())
                    tempP += 100;
            }
        }
        else {
            if(p instanceof Group){
                tempP += 5;

                for(int i = 0; i < ((Group)p).idx; i++) {
                    tempP += tipeTicket(((Group)p).group[i].getTipe_ticket());
                    tempP += age(((Group)p).group[i].getAge());

                    if(((Group) p).group[i].getPriority_embark())
                        tempP += 30;
                    if(((Group)p).group[i].getSpecial_needs())
                        tempP += 100;
                }
            }
            else {
                tempP += tipeTicket(p.getTipe_ticket());
                tempP += age(p.getAge());

                if(p.getPriority_embark())
                    tempP += 30;
                if(p.getSpecial_needs())
                    tempP += 100;
            }
        }
        return tempP;
    }

    /**
     * determina parintele
     * @param poz => copilul parintelui returnat
     * @return => pozitia parintelui
     */
    private int parent(int poz) {
        return poz / 2;
    }

    /**
     * interschimba doi pasageri/familii/grupuri
     * @param p1 => pozitia I-ei entitati
     * @param p2 => pozitia entintatii II
     */
    private void swap(int p1, int p2)
    {
        Pasager tmp;
        tmp = queue[p1];
        queue[p1] = queue[p2];
        queue[p2] = tmp;
    }

    /**
     * intoarce prioritatea entitatii
     * @param p => pasagerul/familia/grupul
     * @return => prioritatea
     */
    public int getPriority(Pasager p){
        if(p instanceof Family)
            return ((Family)p).family[0].getPriority();
        else if (p instanceof Group)
            return ((Group) p).group[0].getPriority();
        else return p.getPriority();
    }

    /**
     * seteaza prioritatea entitatii
     * @param p => pasagerul/familia/grupul
     * @param prioritate => prioritatea
     */
    public void setPriority(Pasager p, int prioritate){
        if(p instanceof Family)
            ((Family)p).family[0].setPriority(prioritate);
        else if (p instanceof Group)
            ((Group) p).group[0].setPriority(prioritate);
        else p.setPriority(prioritate);
    }

    public String getID(Pasager p){
        if(p instanceof Family)
            return ((Family)p).family[0].getId();
        else if (p instanceof Group)
            return ((Group) p).group[0].getId();
        else return p.getId();
    }

    /**
     * insereaza entitatea, seteaza prioritatea
     * @param p => pasagerul/familia/grupul
     * @param priority => prioritatea entitatii
     */
    public void insert(Pasager p, int priority)
    {
        queue[++dimension] = p;
        setPriority(p, priority);

        int current = dimension;

        while (parent(current) != 0 && getPriority(queue[current]) > getPriority(queue[parent(current)])) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    /**
     * sorteaza coada dupa metoda heap-ului
     * @param i => pozitia root-ului
     */
    void sort(int i){
        int root = i;
        int left = i*2;
        int right = i*2 + 1;

        if(left <= dimension && right <= dimension){
            if (getPriority(queue[root]) < getPriority(queue[right]) ||
                    getPriority(queue[root]) < getPriority(queue[left])) {
                if (getPriority(queue[left]) >= getPriority(queue[right])) {
                    swap(root, left);
                    sort(left);
                } else {
                    swap(root, right);
                    sort(right);
                }
            }
            else return;
        }
        else {
            if (left <= dimension && getPriority(queue[root]) <= getPriority(queue[left])) {
                swap(root, left);
                sort(left);
            }
            else {
                if (right <= dimension && getPriority(queue[root]) <= getPriority(queue[right])) {
                    swap(root, right);
                    sort(right);
                } else return;
            }
        }
    }

    /**
     * embarcheaza entitatea cu prioritate maxima
     */
    void embark(){
        queue[1] = queue[dimension];
        queue[dimension] = null;
        dimension--;

        sort(1);
    }

    /**
     * printeaza in preordine
     * @param queue => coada cu pasageri
     * @param i => pozitita root-ului
     */
    void printPreorder(Pasager[] queue, int i)
    {
        int left = 2*i ;
        int right = 2*i +1;

        if (i > dimension || queue[i] == null)
            return;

        if (i != 1)
            output.print(" ");

        if (queue[i] instanceof Family)
            output.print(((Family) queue[i]).family[0].getId());
        else if (queue[i] instanceof Group)
            output.print(((Group) queue[i]).group[0].getId());
        else
            output.print(queue[i].getId());

        printPreorder(queue, left);
        printPreorder(queue, right);
    }

    /**
     * apeleaza metoda pentru afisarea cozii
     */
    void list() {
        printPreorder(queue, 1);
    }

    /**
     * cauta entitatea in coada
     * @param id => id-ul familiei/grupului/pasagerului singur
     * @return pozitia entitatii
     */
    int findPasager(String id){
        for (int i = 1; i <= dimension; i++)
            if (getID(queue[i]).equals(id))
                return i;

        return 0;
    }

    /**
     * sterge entitatea din coada
     * @param id => id-ul familiei/grupului/pasagerului singur
     */
    void delete(String id){
        int pos;
        pos = findPasager(id);

        queue[pos] = queue[dimension];
        queue[dimension] = null;
        dimension--;

        sort(pos);
    }
}
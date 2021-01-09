class Family extends Pasager{
    Pasager[] family = new Pasager[50];
    int idx;

    Family (String id, String name, int age, char tipe_ticket,
             Boolean priority_embark, Boolean special_needs) {
        super(id, name, age, tipe_ticket, priority_embark, special_needs);
    }

    /**
     * adauga persoana in familie
     * @param person => persoana ce trebuie adaugata
     */
    void add(Pasager person){
        family[idx] = person;
        idx++;
    }

    /**
     * sterge persoana din familie
     * @param name => numele persoanei
     */
    void deletePerson(String name){
        for (int i = 0; i < idx; i++){
            if(family[i].getName().equals(name))
                for (int j = i; j < idx-1; j++)
                    family[j] = family[j+1];
            idx--;
            family[idx] = null;
            break;
        }
    }
}

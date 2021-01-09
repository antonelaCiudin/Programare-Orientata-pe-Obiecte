class Group extends Pasager{
    Pasager[] group = new Pasager[50];
    int idx;

    Group (String id, String name, int age, char tipe_ticket,
         Boolean priority_embark, Boolean special_needs) {
        super(id, name, age, tipe_ticket, priority_embark, special_needs);
    }

    /**
     * adauga persoana in grup
     * @param person => persoan ce trebuie adaugata
     */
    void add(Pasager person){
        group[idx] = person;
        idx++;
    }

    /**
     * sterge persoana din grup
     * @param name => numele persoanei
     */
    void deletePerson(String name){
        for (int i = 0; i < idx; i++){
            if(group[i].getName().equals(name))
                for (int j = i; j < idx-1; j++)
                    group[j] = group[j+1];
                idx--;
                group[idx] = null;
                break;
        }
    }
}

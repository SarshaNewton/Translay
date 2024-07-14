import java.util.*;

public class Application {
    public static boolean latorChanger = false;
    public static boolean lateeChanger = false;
    public static void run(Input ID, Output OD){
        int whatToDo = ID.welcome();

        Translator[] preTranslators;
        Translatee[] translatees;

        OD.print(String.valueOf(whatToDo));

        if(whatToDo == 0){
            //Both files are empty/ do not exist
            preTranslators = ID.getTranslators(whatToDo);
            translatees = ID.getTranslatees(whatToDo);
            latorChanger = true;
            lateeChanger = true;
        }else if(whatToDo == 1){
            //Translator file is empty/ does not exist
            preTranslators = ID.getTranslators(whatToDo);
            translatees = ID.getTranslatees(whatToDo);
            latorChanger = true;
        }else if(whatToDo == 2){
            //Translatee file is empty/ does not exist
            preTranslators = ID.getTranslators(whatToDo);
            translatees = ID.getTranslatees(whatToDo);
            lateeChanger = true;
        }else{
            //Both files contain content
            preTranslators = ID.getTranslators(whatToDo);
            translatees = ID.getTranslatees(whatToDo);
        }

        ArrayList<Translator> translators;
        if(whatToDo > 1){
            translators = onlineLators(preTranslators, ID, OD);
        }else{
            translators = new ArrayList<>(Arrays.asList(preTranslators));
        }

        translators = appendLators(translators, ID, OD);

        //Matches translators with translatees
        for(Translatee translatee: translatees){
            if(!translatee.helped()){
                String message = (ID.matchEm(translators.toArray(new Translator[0]), translatee));
                OD.print(message);
            }
        }
        OD.print("");
        OD.print("5 MINUTES LATER...(We assume our translators are free)");
        OD.print("");
        //Unmatches translators
        for(Translatee translatee: translatees){
            translatee.allDone();
        }
        //Attempts to rematch with translatees still awaiting help
        for(Translatee translatee: translatees){
            if(!translatee.helped()){
                String message = (ID.matchEm(translators.toArray(new Translator[0]), translatee));
                OD.print(message);
            }
        }
        OD.print("");
        //if list of translators was changed option to save
        if(latorChanger || ID.isLatorChanger()){
            boolean done = false;
            OD.print("Save new list of Translators?");
            String input = ID.read();
            while(!done){
                switch(input){
                    case "Yes", "yes", "yii", "da", "Da", "DA" :
                        OD.file(translators.toArray(new Translator[0]));
                        done = true;
                        break;
                    case "No", "no", "NO", "nu", "Nu", "NU" :
                        done = true;
                        break;
                    default:
                        OD.print("Seems like you made an error. Would you like to save to the list of translators?");
                        input = ID.read();
                        break;
                }
            }
        }

        if(lateeChanger || ID.isLateeChanger()) {
            boolean done = false;
            OD.print("Save new list of Translatees?");
            String input = ID.read();
            while (!done) {
                switch (input) {
                    case "Yes", "yes", "yii", "da", "Da", "DA":
                        OD.file(translatees);
                        done = true;
                        break;
                    case "No", "no", "NO", "nu", "Nu", "NU":
                        done = true;
                        break;
                    default:
                        OD.print("Seems like you made an error. Would you like to save to the list of translatees?");
                        input = ID.read();
                        break;
                }
            }
        }

        boolean multiple = false;
        Translator bestest;
        bestest = translators.get(0);
        ArrayList<Translator> bestests = new ArrayList<>();

        for(int i = 1; i < translators.size(); i++){
            if(bestest.compareTo(translators.get(i)) == -1){
                bestest = translators.get(i);
            }else if(bestest.compareTo(translators.get(i)) == 0){
                if(!multiple){
                    bestests.add(bestest);
                }
                bestests.add(translators.get(i));
                multiple = true;
            }
        }

        if(multiple){
            OD.print("The superior active translators are:");
            for(Translator translator : bestests){
                OD.printNS(translator.getUserName() + "{" + translator.getName() + "} ");
            }
            OD.print("with a language count of " + bestests.get(0).getSpokenLansCount());
        }else{
            OD.print("The superior active translator is");
            OD.print(bestest.getUserName() + "{" + bestest.getName() + "} ");
            OD.print("with a language count of " + bestest.getSpokenLansCount());
        }
    }

    public User[] languageKnower(User[] Users, String toKnow){
        User[] weSpeakThis = new User[Users.length];
        int i = 0;
        for(User aUser : Users){
            String[] speakoWhato = aUser.getSpokenLans();
            for(String language : speakoWhato){
                if(toKnow == language){
                    weSpeakThis[i] = aUser;
                    i++;
                }
            }
        }
        return weSpeakThis;
    }

    public static Translator powerTrans(Translator translator1, Translator translator2){
        int compared = translator1.compareTo(translator2);
        if(compared == -1){
            return translator2;
        }else if (compared ==  1){
            return translator1;
        }else{
            return null;
        }
    }

    public static ArrayList<Translator> appendLators(ArrayList<Translator> oldLators, Input ID, Output OD){
        OD.print("Do you wish to add more Translators to the list?");
        String input = ID.read();
        ArrayList<Translator> translators;
        boolean done = false;
        while (!done){
            switch(input){
                case "Yes", "yes", "yii", "da", "Da", "DA" :
                    Translator[] extraLators = ID.getTranslators(0);
                    ArrayList<Translator> lators = new ArrayList<>(Arrays.asList(extraLators));
                    lators.addAll(oldLators);
                    Set<Translator> uLators = new LinkedHashSet<>(lators);
                    translators = new ArrayList<>(uLators);
                    latorChanger = true;
                    done = true;
                    return translators;
                case "No", "no", "NO", "nu", "Nu", "NU" :
                    done = true;
                    return oldLators;
                default:
                    OD.print("Seems like you made an error. Would you like to append to the list?");
                    input = ID.read();
                    break;
            }
        }
        return oldLators;
    }

    public static ArrayList<Translator> onlineLators(Translator[] preTranslators, Input ID, Output OD){
        //Asks user to state which translators are available to help
        OD.print("These are the users available");
        int nummy = 0;
        //Prints a numbered list of the translators on file
        for(int i = 1; i < preTranslators.length+1; i++){
            OD.printNS(i + ": ");
            OD.print(preTranslators[i-1]);
            nummy = i;
        }
        User[] lators = ID.whosUp(nummy, preTranslators);
        Translator[] translators = new Translator[lators.length];
        System.arraycopy(lators, 0, translators, 0,lators.length);
        ArrayList<Translator> allators = new ArrayList<>(Arrays.asList(translators));
        return allators;
    }
}

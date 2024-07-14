import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Input {

    public static boolean latorChanger = false;
    public static boolean lateeChanger = false;
    private InputStream inStream;
    private Scanner scanny;
    public Input(){
        inStream = System.in;
        scanny = new Scanner(inStream);
    }
    File allatorsFiley = new File("C:\\Users\\Sarsha\\IdeaProjects\\TranSlay\\Translators.txt");
    File allateesFiley = new File("C:\\Users\\Sarsha\\IdeaProjects\\TranSlay\\Translatees.txt");
    public static Languages RO = new Languages("Romanian");
    public static Languages EN = new Languages("English");
    public static Languages ES = new Languages("Spanish");
    public static Languages FR = new Languages("French");
    public static Languages AR = new Languages("Arabic");
    public static Languages GE = new Languages("German");

    public String read(){
        String input = scanny.nextLine();
        input.trim();
        return input;
    }
    public int welcome(){
        System.out.println("Hey! Welcome to Translay!");

        if((!allatorsFiley.exists() && !allateesFiley.exists()) || (allatorsFiley.length() == 0 && allateesFiley.length() == 0)){
            return 0;
        }else if(allatorsFiley.length() == 0){
            //Translator File does not exist / is empty
            return 1;
        }else if(allateesFiley.length() == 0){
            //Translatee File does not exist / is empty
            return 2;
        }else{
            //Both files are not empty
            return 3;
        }

    }
    public Translator[] getTranslators(int whatToDo){
        if(whatToDo <= 1){
            int nummy = addUsers(0);
            Translator[] allators = new Translator[nummy];

            for(int i = 0; i < nummy; i++) {
                System.out.println("What's the username for translator " + (i + 1));
                String userName = scanny.nextLine();

                userName = validateUN(allators, userName);
                String name = nameGrammarNazi(userName);
                Languages[] spokenLans = setLanguages(name);
                Translator lator = (Translator) buildALator(userName, name, spokenLans);
                allators[i] = lator;
            }
            return allators;
        }else{
            try {
                this.scanny = new Scanner(allatorsFiley);
                ArrayList<Translator> lators = new ArrayList<>();
                while (scanny.hasNextLine()) {
                    String[] line = scanny.nextLine().split("[{,:}]");
                    for(int k = 0; k < line.length; k++){
                        line[k] = line[k].trim();
                    }
                    String userName = line[0];
                    String name = line[1];
                    Languages[] spokenLans = setLanguage(line);
                    Translator lator = (Translator) buildALator(userName, name, spokenLans);
                    lators.add(lator);
                }
                Translator[] allators = lators.toArray(new Translator[0]);
                this.scanny = new Scanner(inStream);
                return allators;
            } catch (FileNotFoundException e) {
                System.out.println("We encountered an error retrieving our translators. Please enter a new group of translators");
                latorChanger = true;
                return getTranslators(0);
            }
        }
    }

    public Translatee[] getTranslatees(int whatToDo) {
        if(whatToDo == 0 || whatToDo == 2){
            int nummy = addUsers(1);
            Translatee[] allatees = new Translatee[nummy];

            for(int i = 0; i < nummy; i++) {
                System.out.println("What's the username for translatee " + (i + 1));
                String userName = scanny.nextLine();

                userName = validateUN(allatees, userName);
                String name = nameGrammarNazi(userName);
                Languages[] spokenLans = setLanguages(name);
                Languages helpLan = setHelpLan(name);
                Translatee latee = (Translatee) buildALatee(userName, name, spokenLans, helpLan);
                allatees[i] = latee;
            }
            return allatees;
        }else{
            System.out.println("Do you want to use the existing Translatees? (yes / no)");
            String input = scanny.nextLine();
            input.trim();
            boolean done = false;
            while(!done){
                switch(input){
                    case "Yes", "yes", "y", "Y", "YES","da","DA", "1":
                        try {
                            this.scanny = new Scanner(allateesFiley);
                            ArrayList<Translatee> latees = new ArrayList<>();
                            while (scanny.hasNextLine()) {
                                String[] line = scanny.nextLine().split("[{,:}\\->]");
                                List<String> vLine = Arrays.stream(line).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                                line = vLine.toArray(new String[0]);
                                for(int k = 0; k < line.length; k++){
                                    line[k] = line[k].trim();
                                }
                                String userName = line[0];
                                String name = line[1];

                                Languages[] spokenLans = setLanguage(line);
                                Languages helpLan = spokenLans[spokenLans.length - 1];
                                Languages[] justlans = new Languages[spokenLans.length - 1];
                                System.arraycopy(spokenLans, 0, justlans, 0, justlans.length);

                                Translatee latee = (Translatee) buildALatee(userName, name, justlans, helpLan);
                                latees.add(latee);
                            }
                            Translatee[] allatees = latees.toArray(new Translatee[0]);
                            this.scanny = new Scanner(inStream);
                            done = true;
                            return allatees;
                        } catch (FileNotFoundException e) {
                            System.out.println("We encountered an error retrieving our translators. Please enter a new group of translators");
                            done = true;
                            return getTranslatees(0);
                        }
                    case "No", "no", "n", "N", "NO","NU","nu", "0":
                        lateeChanger = true;
                        done = true;
                        return getTranslatees(0);
                    default:
                        System.out.println("Let's try again. Do you want to use a new list of translatees?");
                        input = scanny.nextLine();
                        break;
                }
            }
        }
        return getTranslatees(0);
    }

    public User[] whosUp(int nummy, User[] users){
        ArrayList<User> newUsers = new ArrayList<>();
        boolean done = false;
        System.out.println("Are any translators unavailable? (yes / no)");
        String input = scanny.nextLine();
        while(!done){
            switch (input){
                case "y", "Y", "yes", "Yes","1", "da","Da", "YES", "yii", "Yii":
                    System.out.println("Please enter the corresponding number(s) of the translator(s) you wish to turn offline");
                    String offline = scanny.nextLine();
                    String[] offIt = offline.split("[\\s+,;:.-]");
                    List<String> vOffIt = Arrays.stream(offIt).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                    Set<String> uniqueOffit = new LinkedHashSet<>(vOffIt);
                    vOffIt = new ArrayList<>(uniqueOffit);
                    for(String index: vOffIt){
                        try{
                            int dex = Integer.parseInt(index);
                            if(dex < nummy - 1){
                                users[dex-1].setOnline(false);
                            }
                        }catch(NumberFormatException e){
                            System.out.println(index + " will be ignored as it is not a number");
                        }
                    }
                    for(User aUser : users){
                        if(aUser.getOnline()){
                            newUsers.add(aUser);
                        }
                    }
                    done = true;
                    break;
                case "n", "N", "no", "No", "NO", "nu", "NU", "Nu","0":
                    ArrayList<User> users1 = new ArrayList<>(Arrays.asList(users));
                    newUsers.addAll(users1);
                    done = true;
                    break;
                default:
                    System.out.println("Huh? We didn't get that, please type yes or no");
                    input = scanny.nextLine();
                    break;
            }
        }
        return newUsers.toArray(new User[0]);
    }
    public String matchEm(Translator[] allLators, Translatee translatee){
        return translatee.latorMatcher(allLators);
    }
    public boolean isLatorChanger(){
        return latorChanger;
    }
    public boolean isLateeChanger(){
        return lateeChanger;
    }
    private int addUsers(int who){
        boolean done = false;
        int nummy = 0;
        if(who == 0){
            //Ensures user enters an integer
            while(!done){
                System.out.println("How much translators do you wanna add?");
                try{
                    nummy = Integer.parseInt(scanny.nextLine());
                    done = true;
                }catch (NumberFormatException e){
                    System.out.println("Please enter an integer");
                }
            }
            return nummy;
        }else{
            //Ensures user enters an integer
            while(!done){
                System.out.println("How much translatees do you wanna add?");
                try{
                    nummy = Integer.parseInt(scanny.nextLine());
                    done = true;
                }catch (NumberFormatException e){
                    System.out.println("Please enter an integer");
                }
            }
            return nummy;
        }
    }
    private String validateUN(User[] users, String userName){
        boolean done = false;
        while(!done){
            for(User user : users) {
                try{
                    if (users != null && user.getUserName().equals(userName)) {
                        System.out.println("This user is already in the system! Please enter another username");
                        userName = scanny.nextLine();
                    } else {
                        done = true;
                    }
                }catch (Exception e) {
                    done = true;
                }
            }
        }
        return userName;
    }

    private String nameGrammarNazi(String userName){
        StringBuilder name = new StringBuilder();
        if(userName.charAt(userName.length() - 1) == 's' || userName.charAt(userName.length() - 1) == 'S'){
            System.out.println("What's " + userName + "' name");
            name.append(scanny.nextLine());
        }else{
            System.out.println("What's " + userName + "'s name");
            name.append(scanny.nextLine());
        }
        return name.toString();
    }

    private Languages setHelpLan(String name){
        System.out.println("What language does " + name + " need help with?");
        String helpLan = scanny.nextLine();
        Languages lang = new Languages("");
        boolean done = false;
        while(!done){
            switch(helpLan) {
                case "English", "english", "ENGLISH", "EN", "En", "en":
                    done = true;
                    lang = EN;
                    break;
                case "Spanish", "spanish", "SPANISH", "ES", "Es", "es":
                    done = true;
                    lang = ES;
                    break;
                case "French", "french", "FRENCH", "FR", "Fr", "fr":
                    done = true;
                    lang = FR;
                    break;
                case "Romanian", "romanian", "ROMANIAN", "RO", "Ro", "ro":
                    done = true;
                    lang = RO;
                    break;
                case "Arabic", "arabic", "ARABIC", "AR", "Ar", "ar":
                    done = true;
                    lang = AR;
                    break;
                case "German", "german", "GERMAN", "GE", "Ge", "ge":
                    done = true;
                    lang = GE;
                    break;
                default:
                    System.out.println("Oh no! We don't support " + helpLan + " language yet :( Please enter English, Spanish, French, Romanian, Arabic or German");
                    helpLan = scanny.nextLine();
                    break;
            }
        }
        return lang;
    }

    private Languages[] setLanguages(String name) {
        System.out.println("Let's list all the languages that " + name + " speaks. Put their native language first ;)");
        String lans = scanny.nextLine();

        String[] langs = lans.split("[\\s+,;:.-]");
        List<String> vLangs = Arrays.stream(langs).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        Set<String> uniqueLangs = new LinkedHashSet<>(vLangs);
        vLangs = new ArrayList<>(uniqueLangs);
        Languages[] spokenLans = new Languages[vLangs.size()];

        boolean done;
        for (int j = 0; j < vLangs.size(); j++) {
            done = false;
            while (!done) {
                switch(vLangs.get(j)) {
                    case "English", "english", "ENGLISH", "EN", "En", "en":
                        spokenLans[j] = EN;
                        done = true;
                        break;
                    case "Spanish", "spanish", "SPANISH", "ES", "Es", "es":
                        spokenLans[j] = ES;
                        done = true;
                        break;
                    case "French", "french", "FRENCH", "FR", "Fr", "fr":
                        spokenLans[j] = FR;
                        done = true;
                        break;
                    case "Romanian", "romanian", "ROMANIAN", "RO", "Ro", "ro":
                        spokenLans[j] = RO;
                        done = true;
                        break;
                    case "Arabic", "arabic", "ARABIC", "AR", "Ar", "ar":
                        spokenLans[j] = AR;
                        done = true;
                        break;
                    case "German", "german", "GERMAN", "GE", "Ge", "ge":
                        spokenLans[j] = GE;
                        done = true;
                        break;
                    default:
                        System.out.println("Oh no! We don't support " + vLangs.get(j) + " language yet :( Please enter English, Spanish, French, Romanian, Arabic or German");
                        vLangs.set(j, scanny.nextLine());
                        break;
                }
            }
        }
        //Ensures spokenLans array is unique
        Set<Languages> uLangs = new LinkedHashSet<>(List.of(spokenLans));
        spokenLans = uLangs.toArray(new Languages[0]);

        return spokenLans;
    }

    private Languages[] setLanguage(String[] line){
        Languages[] spokenLans = new Languages[line.length - 2];
        for(int j = 2; j < line.length; j++){
            switch(line[j]){
                case "English":
                    spokenLans[j-2] = EN;
                    break;
                case "Spanish":
                    spokenLans[j-2] = ES;
                    break;
                case "French":
                    spokenLans[j-2] = FR;
                    break;
                case "Romanian":
                    spokenLans[j-2] = RO;
                    break;
                case "Arabic":
                    spokenLans[j-2] = AR;
                    break;
                case "German":
                    spokenLans[j-2] = GE;
                    break;
                default:
                    break;
            }
        }
        return spokenLans;
    }

    private void setLanguage(User aUser, Languages[] spokenLans){
        if (spokenLans.length > 1) {
            Languages[] faraNative = new Languages[spokenLans.length - 1];
            System.arraycopy(spokenLans, 1, faraNative, 0, spokenLans.length - 1);
            aUser.setSpokenLans(faraNative);
        } else {
            aUser.setSpokenLans();
        }
    }
    private User buildALator(String userName, String name, Languages[] spokenLans) {
        Translator lator = new Translator(userName, name, spokenLans[0]);
        setLanguage(lator, spokenLans);
        return lator;
    }
    private User buildALatee(String userName, String name, Languages[] spokenLans, Languages helpLan){
        Translatee latee = new Translatee(userName, name, spokenLans[0], helpLan);
        setLanguage(latee, spokenLans);
        return latee;
    }
}

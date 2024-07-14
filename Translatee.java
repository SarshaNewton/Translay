import java.util.Arrays;

public class Translatee extends User {
    private Languages helpLan;
    private boolean helped;
    private Translator latorMatched;
    public Translatee(String userName, String name, Languages nativeLan, Languages helpLan) {
        super(userName, name, nativeLan);
        this.helpLan = helpLan;
        this.helped = false;
    }
    public Languages getHelpLan(){
        return helpLan;
    }
    public boolean helped(){
        return helped;
    }
    public String latorMatcher(Translator[] translators){
        //Priority matches Translatee with a Translator whose native language matches the help language
        for(Translator translay : translators){
            if(translay.getNativeLan().equals( helpLan.getLangN())){
                helped = weSpeakSame(translay);
            }
        }
        int busy = 0;
        int noSpeako = 0;
        int huh = 0;
        StringBuilder message = new StringBuilder(54);

        //If no Translator's native language matches the help language searches if anyone can speak it
        for(Translator translay: translators){
            String[] layLans = translay.getSpokenLans();
            for(String layLan : layLans){
                if(helpLan.getLangN().equals(layLan)){
                    helped = weSpeakSame(translay);
                    if(helped){
                        //The translator and translatee have languages in common
                        boolean helping = translay.isHelping();
                        //Ensures the translator isn't helping someone else
                        if(helping == false){
                            helped = true;
                            latorMatched = translay;
                            translay.lateeMatcher(this);
                            translay.setHelping(helped);
                            message.append(this.getName() + "! You're matched with " + translay.getName());
                            return message.toString();
                        }else{
                            //However translator is already helping someone else
                            busy++;
                            helped = false;
                        }
                    }else{
                        //The translator and translatee have no languages in common
                        noSpeako++;
                    }
                }else{
                    //The language to be translated is not spoken by translator
                    huh++;
                }
            }
        }

        if(helped == false){
            if(busy != 0){
                message.append("Oop " + this.getName() + "! All our translators are busy");
            }else if(noSpeako != 0){
                message.append("So sorry " + this.getName() +". We found translators for " + this.getHelpLan().getLangN() + " but none of them speak ");
                String[] allLans = this.getSpokenLans();
                for(int i = 0; i < allLans.length; i++){
                    if(i == (allLans.length - 2)){
                        message.append(allLans[i] + " ");
                    }else if(i == (allLans.length - 1) && allLans.length >= 2){
                        message.append("or " + allLans[i]);
                    }else if(i != (allLans.length - 1) && allLans.length > 2){
                        message.append(allLans[i] + ", ");
                    }else if(allLans.length <= 2){
                        message.append(allLans[i] + " ");
                    }
                }
            }else if (huh != 0){
                message.append("Ack! Our bad " + this.getName() + ". None of our translators speak " + this.helpLan.getLangN());
            }
        }
        return message.toString();
    }

    public boolean weSpeakSame(Translator translay){
        String[] layLans = translay.getSpokenLans();
        String[] teeLans = this.getSpokenLans();
        for(String layLan : layLans){
            for(String teeLan : teeLans){
                if(teeLan == layLan){
                    latorMatched = translay;
                    translay.lateeMatcher(this);
                    return true;
                }
            }
        }
        return false;
    }

    public void allDone(){
        Translator translay = this.latorMatched;
        if(translay != null){
            translay.lateeMatcher(null);
            translay.setHelping(false);
            this.latorMatched = null;
        }
    }
}

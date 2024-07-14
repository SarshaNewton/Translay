import java.util.Arrays;

public abstract class User implements Comparable<User>{
    private String userName;
    private String name;
    private Languages nativeLan;
    private Languages[] spokenLans;
    private Birthday dob;
    private boolean online;
    public User (String userName, String name, Languages nativeLan){
        this.userName = userName;
        this.name = name;
        this.nativeLan = nativeLan;
        this.online = true;
    }
    public String getUserName(){return userName;}
    public String getName(){
        return name;
    }
    public void setSpokenLans(Languages...allLan){
        spokenLans = new Languages[allLan.length + 1];
        spokenLans[0] = nativeLan;
        for(int i = 1; i <= allLan.length; i++){
            spokenLans[i] = allLan[i - 1];
        }
    }
    public void setSpokenLans(){
        spokenLans = new Languages[1];
        spokenLans[0] = nativeLan;
    }

    public void setOnline(boolean online){
        this.online = online;
    }
    public String getNativeLan(){
        return nativeLan.getLangN();
    }
    public String[] getSpokenLans(){
        String[] allLangN = new String[spokenLans.length];
        for(int i = 0; i < spokenLans.length; i++){
            allLangN[i] = spokenLans[i].getLangN();
        }
        return allLangN;
    }

    public int getSpokenLansCount(){
        return spokenLans.length;
    }

    public boolean getOnline(){
        return online;
    }

    public int compareTo(User aUser){
        int lanS = this.spokenLans.length;
        int lanS1 = aUser.spokenLans.length;
        int compared = Integer.compare(lanS, lanS1);
        return compared;
    }
}

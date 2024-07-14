import java.io.*;


public class Output {
    private OutputStream outStream;

    public Output(){
        outStream = System.out;
    }

    /*public Output(OutputStream outStream){
        this.outStream = outStream;
    }*/
    public void print(String message){
        try {
            outStream.write(message.getBytes());
            outStream.write("\n".getBytes());
        } catch (IOException e) {
            System.out.println("We've encountered an error presenting the info. One sec while we figure it out");
            throw new RuntimeException(e);
        }
    }
    public void print(User aUser){
        try {
            outStream.write((aUser.getUserName()+ "{" + aUser.getName()+ ": ").getBytes());
            String[] langs = aUser.getSpokenLans();
            for (int i = 0; i < langs.length; i++) {
                if(i == langs.length-1){
                    outStream.write((langs[i]).getBytes());
                }else{
                    outStream.write((langs[i] + ", ").getBytes());
                }
            }
            if(aUser instanceof Translatee){
                outStream.write((" -> " + ((Translatee) aUser).getHelpLan().getLangN()).getBytes());
            }
            outStream.write("}\n".getBytes());
        } catch (IOException e) {
            System.out.println("We've encountered an error presenting the info. One sec while we figure it out");
            throw new RuntimeException(e);
        }
    }
    public void printNS(String message){
        try {
            outStream.write((message).getBytes());
        } catch (IOException e) {
            System.out.println("We've encountered an error presenting the info. One sec while we figure it out");
            throw new RuntimeException(e);
        }
    }
    public void file(User[] users){
        boolean start = false;
        for (User aUser: users) {
            if(aUser instanceof Translatee){
                try {
                    this.outStream = new FileOutputStream("Translatees.txt", true);
                } catch (FileNotFoundException e) {
                    print("We can't seem to locate the Translatees' file! Current Translatees will not be saved :(");
                }
            }else if(aUser instanceof Translator){
                if(!start){
                    try {
                        this.outStream = new FileOutputStream("Translators.txt", false);
                        start = true;
                    } catch (FileNotFoundException e) {
                        print("We can't seem to locate the Translators' file! Current Translators will not be saved :(");
                    }
                }else{
                    try {
                        this.outStream = new FileOutputStream("Translators.txt", true);
                        start = true;
                    } catch (FileNotFoundException e) {
                        print("We can't seem to locate the Translators' file! Current Translators will not be saved :(");
                    }
                }
            }
            print(aUser);
        }
        this.outStream =  System.out;
    }
}

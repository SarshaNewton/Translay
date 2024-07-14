public class Translator extends User{
    private boolean helping;
    private Translatee lateeMatched;
    public Translator(String userName, String name, Languages nativeLan) {
        super(userName, name, nativeLan);
        helping = false;
    }
    public void lateeMatcher(Translatee lateeMatched){
        this.lateeMatched = lateeMatched;
    }
    public boolean isHelping(){
        return helping;
    }
    public void setHelping(boolean helping){
        this.helping = helping;
    }
    public Translatee getLateeMatched() {
        return lateeMatched;
    }
    public void Comparable(){

    }
}

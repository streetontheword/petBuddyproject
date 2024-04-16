package vttp.server.Model;

public class Breeds {
    
private String primary;
private String secondary; 
private boolean mixed; 
private boolean unknown;

public String getPrimary() {
    return primary;
}
public void setPrimary(String primary) {
    this.primary = primary;
}
public String getSecondary() {
    return secondary;
}
public void setSecondary(String secondary) {
    this.secondary = secondary;
}
public boolean isMixed() {
    return mixed;
}
public void setMixed(boolean mixed) {
    this.mixed = mixed;
}
public boolean isUnknown() {
    return unknown;
}
public void setUnknown(boolean unknown) {
    this.unknown = unknown;
}
public Breeds(String primary, String secondary, boolean mixed, boolean unknown) {
    this.primary = primary;
    this.secondary = secondary;
    this.mixed = mixed;
    this.unknown = unknown;
}
public Breeds() {
}
@Override
public String toString() {
    return "Breeds [primary=" + primary + ", secondary=" + secondary + ", mixed=" + mixed + ", unknown=" + unknown
            + "]";
}




}

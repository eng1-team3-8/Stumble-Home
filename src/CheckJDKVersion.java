public class CheckJDKVersion {
    public static void main(String[] args) {
        System.out.println("=== Java Runtime Information ===");

        String version = System.getProperty("java.version");
        System.out.println("Java Version: " + version);

        String vendor = System.getProperty("java.vendor");
        System.out.println("Java Vendor: " + vendor);
        
        if (!(vendor.equals("Eclipse Adoptium")) || !(version.substring(0, 2).equals("17"))){
            System.out.println("\nInvalid Java Version In Use, Vendor: " + vendor + ", Version: " + version);
            System.out.println("Please use Vendor: Eclipse Adoptium, Version: 17.0.16");
        }
        else{
            System.out.println("\nJava runtime version is correct");
        }
    }
}
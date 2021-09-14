package Server;

/* Zur erstellung einer funktionierenden Jar datei war eine Main methode nötig, die nicht
von Application erbt. Daher startet die Main Methode "EntryPointServer" unsere eigentliche Main methode
ohne von Application zu erben
 */
public class EntryPointServer {
    public static void main(String[] args) {
        Main.main(args);
    }
}

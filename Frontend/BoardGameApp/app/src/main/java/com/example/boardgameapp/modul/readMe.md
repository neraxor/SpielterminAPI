#User

Die Klasse User dient als Datentyp, er speichert alle relevanten Informationen fuer ein User.
- die Klasse kann als Attribut an die Klasse API uebergeben werden\ z.B.:

````java
class User {
    private String name;
    private String passwort;
    private String strasse;
    private String hausnummer;
    private String plz;

    public User() {
    }

    public User(Sring name, String passwort, String strasse, String hausnummer, String plz) {
        this.name = name;
        this.passwort = passwort;
        this.strasse;
        this.hausnummer;
        this.String plz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

````

```java 


class API {
    private String name;
    private String passwort;
    private String strasse;
    private String hausnummer;
    private String plz;

    public newUser(User user) {
        this.name = user.getName();
        this.passwort = user.getPasswort();
        //usw.
    }
}
```
\
\
# Alternative:
Die Klasse User enthaelt eine Methode welche die einzelnen werte als Liste zurueck gibt.

```java
public String[] export(){
    return [name, passwort, strasse, hausnummer]
        } 
```
# Daten aus Request zurueckgeben

```java
public User getUserAPI(){
    //Request
        return new User("name", "passwort", "strasse", hausnummer);
        }

//Aufruf

User user = getUserAPI();
```
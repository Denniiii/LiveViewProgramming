Clerk.markdown(STR.
"""
# Nim Turtle View

_Dennis Diener, Technische Hochschule Mittelhessen_

## Implementierung der `equals` und `hashCode` Methoden

### 1. Hashcode Methode

Mit der `hashcode()` Methode wird der Hashcode aus dem Inhalt der einzelnen Reihen berechnet.
> Dadurch entsteht selbst bei unterschiedlicher Anordnung der Reihen der gleiche Hashcode.

```java	
\{Text.cutOut("./Nim.java", "// hashCode")}
```

Dabei wird das Array kopiert und mit einem Quicksort Algorithmus aufsteigend sortiert, danach wird pro Reihe der Wert mit einer Zehnerpotenz multipliziert und aufsummiert, das aufsummierte Ergebnis ist dann der Hashcode.

### 2. Equals Methode

In der Methode werden 5 Abfragen getätigt:
Frage | Umsetzung
------|----------
Ist das Objekt `null`? | `if (other == null) return false;`
Vergleichen wir das gleiche Objekt? | `if (other == this) return true;`
Sind es gleiche Klassen? | `if (other.getClass() != getClass()) return false;`
Casting | `Nim that = (Nim)other;`
Haben die Objekte den selben Inhalt? | `return this.hashCode() == other.hashCode();`

Mit diesen Fragen kann man in der JShell Objekte mit dem Befehl: `.equals(**Objekt**)` aufrufbar.

---

## Implementierung von View 

Die Klasse NimView erweitert Nim und erstellt zuerst eine Turtle-Leinwand, wenn eine Nim-Spiel gestartet wird, danach kann man mit `.show()` den aktuellen Spielstand anzeigen lassen.

        
""");

// hashCode
@Override
boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < sortedRows.length - 1; i++) {                   //Sortiere aufsteigend bsp. 1 2 3 4 (QuickSort)
                if (sortedRows[i] > sortedRows[i + 1]) {
                    int temp = sortedRows[i];
                    sortedRows[i] = sortedRows[i + 1];
                    sortedRows[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
        int hash = 0;                                                           //Hashwert intialisieren
        for (int i = 0; i < sortedRows.length; i++) {                           //Berechne den Hashwert
            hash += sortedRows[i] * Math.pow(10, i);                            //Nutze die Zehnerpotenz um die Reihenfolge zu speichern
        }
        return hash;
// hashCode


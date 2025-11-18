# Specifikacija Programskog Jezika: BrewScript
**Verzija 1.0**

## Opšti Principi
**BrewScript** je imperativni, statički tipiziran programski jezik opšte namene. Dizajniran je da bude čitljiv, robustan i ekspresivan, sa temom inspirisanom veštinom spravljanja pića.

*   **Statička Tipizacija:** Svaka promenljiva mora biti deklarisana sa eksplicitnim tipom pre upotrebe.
*   **Case-Sensitive:** Ključne reči su uvek napisane malim slovima (npr. `ale`). Identifikatori (imena promenljivih i funkcija) su case-sensitive.
*   **Naredbe:** Programi su sekvenca naredbi. Svaka naredba se mora završiti tačka-zarezom (`;`).
*   **Ulazna tačka:** Svaki izvršni program mora sadržati `pub main` blok, koji služi kao ulazna tačka programa.

---

## Tipovi Podataka

#### 1. Funkcionalnost: Integer (ale)
*   **Ova funkcionalnost predstavlja:** 32-bitni ceo broj sa znakom.
*   **Ova funkcionalnost se piše na sledeći način:** Prihvata cele dekadne brojeve, pozitivne i negativne.
*   **Primer:**
    ```
    ale x is 42;
    ```

#### 2. Funkcionalnost: Float (lager)
*   **Ova funkcionalnost predstavlja:** 32-bitni decimalni broj jednostruke preciznosti.
*   **Ova funkcionalnost se piše na sledeći način:** Prihvata decimalne brojeve sa pokretnim zarezom.
*   **Primer:**
    ```
    lager pi is 3.14;
    ```

#### 3. Funkcionalnost: Double (pilsner)
*   **Ova funkcionalnost predstavlja:** 64-bitni decimalni broj dvostruke preciznosti.
*   **Ova funkcionalnost se piše na sledeći način:** Prihvata decimalne brojeve veće preciznosti.
*   **Primer:**
    ```
    pilsner d is 2.71828;
    ```

#### 4. Funkcionalnost: Boolean (malt)
*   **Ova funkcionalnost predstavlja:** Logički tip podatka koji može imati vrednost tačno ili netačno.
*   **Ova funkcionalnost se piše na sledeći način:** Koriste se ključne reči `helles` za tačno (true) i `dunkel` za netačno (false).
*   **Primer:**
    ```
    malt flag is helles;
    ```

#### 5. Funkcionalnost: Char (glass)
*   **Ova funkcionalnost predstavlja:** Jedan karakter iz ASCII tabele.
*   **Ova funkcionalnost se piše na sledeći način:** Karakter se navodi unutar jednostrukih navodnika.
*   **Primer:**
    ```
    glass slovo is 'A';
    ```

#### 6. Funkcionalnost: Array (barrel)
*   **Ova funkcionalnost predstavlja:** Niz elemenata istog tipa.
*   **Ova funkcionalnost se piše na sledeći način:** Deklariše se tip `barrel`, a elementi se inicijalizuju unutar uglastih zagrada `[]`, odvojeni zarezima.
*   **Primer:**
    ```
    barrel brojevi is;
    ```

---

## Osnovne Funkcije i Naredbe

#### 1. Funkcionalnost: Main funkcija (pub main)
*   **Ova funkcionalnost predstavlja:** Centralni deo programa i tačku početka izvršavanja.
*   **Ova funkcionalnost se piše na sledeći način:** Program počinje `pub main` blokom.
*   **Primer:**
    ```
    pub main
        // ... kod pocinje ovde ...
    ```

#### 2. Funkcionalnost: Deklaracija promenljivih
*   **Ova funkcionalnost predstavlja:** Proces definisanja promenljivih i njihovih tipova.
*   **Ova funkcionalnost se piše na sledeći način:** Naredba počinje tipom, zatim sledi ime promenljive. Inicijalizacija vrednosti je opciona i vrši se operatorom `is`. Svaka naredba se završava sa `;`.
*   **Primer:** `ale x;`
*   **Primer sa inicijalizacijom:** `ale x is 10;`

#### 3. Funkcionalnost: Ispis na konzolu (pour)
*   **Ova funkcionalnost predstavlja:** Ispis podatka na standardni izlaz (konzolu).
*   **Ova funkcionalnost se piše na sledeći način:** Ispisuje vrednost string literala ili promenljive navedene nakon `pour`.
*   **Primer:**
    ```
    pour "Hello, World!";
    pour x;
    ```

#### 4. Funkcionalnost: Učitavanje sa tastature (order)
*   **Ova funkcionalnost predstavlja:** Proces unosa podataka sa standardnog ulaza (tastature).
*   **Ova funkcionalnost se piše na sledeći način:** `order` je izraz koji čeka da korisnik unese vrednost, koja se zatim može dodeliti promenljivoj.
*   **Primer:**
    ```
    ale userInput is order;
    ```

#### 5. Funkcionalnost: Definisanje funkcije (function)
*   **Ova funkcionalnost predstavlja:** Kreiranje imenovane procedure sa parametrima i povratnom vrednošću.
*   **Ova funkcionalnost se piše na sledeći način:** Definicija počinje sa `function`, zatim povratni tip, ime funkcije, i lista parametara.
*   **Primer:**
    ```
    function ale saberi(ale a, ale b)
        cheers a + b;
    ```

#### 6. Funkcionalnost: Vraćanje vrednosti (cheers)
*   **Ova funkcionalnost predstavlja:** Naredba za vraćanje vrednosti iz funkcije.
*   **Ova funkcionalnost se piše na sledeći način:** `cheers` prekida izvršavanje funkcije i vraća vrednost izraza koji sledi.
*   **Primer:**
    ```
    cheers 42;
    ```

#### 7. Funkcionalnost: Prekid petlje (cork)
*   **Ova funkcionalnost predstavlja:** Naredba za momentalni izlazak iz `brew` petlje.
*   **Ova funkcionalnost se piše na sledeći način:** `cork;` unutar petlje prekida njeno izvršavanje.
*   **Primer:**
    ```
    brew (helles) {
        cork;
    }
    ```

---

## Operatori

#### 1. Funkcionalnost: Dodela (is)
*   **Ova funkcionalnost predstavlja:** Proces dodeljivanja vrednosti promenljivoj.
*   **Ova funkcionalnost se piše na sledeći način:** Vrednost izraza sa desne strane ključne reči `is` se dodeljuje promenljivoj sa leve strane.
*   **Primer:** `x is 100;`

#### 2. Funkcionalnost: Aritmetički operatori (`+`, `-`, `*`, `/`, `%`)
*   **Ova funkcionalnost predstavlja:** Standardne matematičke operacije.
*   **Ova funkcionalnost se piše na sledeći način:** Binarni operatori koji se izvršavaju nad dva numerička operanda.
*   **Primer:** `x + y;`

#### 3. Funkcionalnost: Relacioni i operatori jednakosti (`>`, `>=`, `<`, `<=`, `==`, `!=`)
*   **Ova funkcionalnost predstavlja:** Poređenje vrednosti.
*   **Ova funkcionalnost se piše na sledeći način:** Porede se dve vrednosti. Rezultat je uvek logička vrednost (`helles` ili `dunkel`).
*   **Primer:** `x > 5;`

#### 4. Funkcionalnost: Logički operator (ili)
*   **Ova funkcionalnost predstavlja:** Logička operacija ILI (OR).
*   **Ova funkcionalnost se piše na sledeći način:** Proverava da li je bar jedan od dva logička izraza tačan.
*   **Primer:** `x > 5 ili flag == dunkel;`

---

## Kontrola Toka

#### 1. Funkcionalnost: if / else (bottle / tap)
*   **Ova funkcionalnost predstavlja:** Uslovno izvršavanje koda.
*   **Ova funkcionalnost se piše na sledeći način:** `bottle` prihvata uslov u zagradama. Ako je uslov `helles`, izvršava se naredni blok koda. Opcioni `tap` blok se izvršava ako uslov nije `helles`.
*   **Primer (if):**
    ```
    bottle (x > 10)
        pour "Vece od 10";
    ```
*   **Primer (if-else):**
    ```
    bottle (x > 10)
        pour "Vece";
    tap
        pour "Nije vece";
    ```

#### 2. Funkcionalnost: while petlja (brew)
*   **Ova funkcionalnost predstavlja:** Blok koda koji se izvršava dokle god je uslov tačan.
*   **Ova funkcionalnost se piše na sledeći način:** `brew` prihvata uslov u zagradama. Naredni blok koda se izvršava u petlji sve dok je uslov `helles`.
*   **Primer:**
    ```
    brew (i < 5)
        i is i + 1;
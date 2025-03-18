# Testovací scénáře pro EqualLibConfig

---

## Základní funkčnost

1. **Výchozí chování**  
   **Scénář:**
    - Vytvoříme dva jednoduché objekty (např. instancie třídy `Simple`) se stejnými hodnotami atributů.  
      **Test:**
    - Použijeme výchozí nastavení EqualLibConfig a porovnáme objekty metodou areEquals.  
      **Očekávaný výsledek:**
    - Výsledek porovnání je `true` (objekty jsou považovány za shodné).

2. **Porovnání různých hodnot atributů**  
   **Scénář:**
    - Vytvoříme dva objekty se stejnou strukturou, avšak s alespoň jedním rozdílným hodnotovým atributem.  
      **Test:**
    - Porovnáme objekty pomocí areEquals se standardním nastavením.  
      **Očekávaný výsledek:**
    - Výsledek porovnání je `false` (objekty nejsou shodné).

---

## Dědičnost

3. **Porovnání objektů s dědičností bez přepínače**  
   **Scénář:**
    - Máme rodičovskou třídu `Parent` s atributem `a` a dvě potomkové třídy `Child1` a `Child2` se svými vlastními atributy (`b` a `c`).  
      **Test:**
    - Objekty, i když mají stejné hodnoty u `a`, mají rozdílné hodnoty u vlastních atributů.  
      **Očekávaný výsledek:**
    - areEquals vrátí `false`, protože se porovnávají všechny atributy, včetně těch v potomcích.

4. **Porovnání objektů s dědičností a zapnutým `compareInheritedFields`**  
   **Scénář:**
    - Použijeme stejný setup jako v testu 3, ale s nastavením `compareInheritedFields = true`.  
      **Test:**
    - Porovnáme objekty, přičemž se ověřují pouze atributy definované v rodičovské třídě `Parent`.  
      **Očekávaný výsledek:**
    - areEquals vrátí `true`, pokud je atribut `a` shodný, a rozdíly v potomkovských atributech jsou ignorovány.

5. **Porovnání objektů s dědičností a vypnutým `compareInheritedFields`**  
   **Scénář:**
    - Použijeme stejný setup jako u testu 3, ale nastavíme `compareInheritedFields = false`.  
      **Test:**
    - Porovnáme objekty, kde se ověřují všechny atributy, včetně těch v potomcích.  
      **Očekávaný výsledek:**
    - areEquals vrátí `false`, pokud se hodnoty atributů potomků liší.

---

## Hloubka porovnávání

6. **Neomezená hloubka porovnání**  
   **Scénář:**
    - Vytvoříme strukturu s více úrovněmi zanoření (např. objekt obsahující vnořené objekty apod.).  
      **Test:**
    - Nastavíme `maxComparisonDepth = -1` a porovnáme tyto objekty.  
      **Očekávaný výsledek:**
    - areEquals rekurzivně porovná všechny úrovně zanoření, výsledek je `true` nebo `false` dle shody celé struktury.

8. **Omezená hloubka porovnání**  
   **Scénář:**
    - Použijeme opět strukturu s více úrovněmi zanoření.  
      **Test:**
    - Nastavíme `maxComparisonDepth = 2` a porovnáme objekty.  
      **Očekávaný výsledek:**
    - Porovnají se pouze dvě úrovně zanoření; rozdíly v úrovních hlouběji (3 a více) jsou ignorovány, což může vést k vyhodnocení shody i přes rozdíly v hlubších strukturách.

9. **Přepnutí na standardní equals po dosažení hloubky**  
   **Scénář:**
    - Použijeme hluboce zanořenou strukturu.  
      **Test:**
    - Nastavíme `useStandardEqualsAfterDepth = true` spolu s omezenou hloubkou (např. `maxComparisonDepth = 2`).  
      **Očekávaný výsledek:**
    - Pro zanořené objekty nad danou hloubkou se použije jejich standardní `equals()`, což může ovlivnit výsledné hodnocení (výsledek závisí na implementaci equals u těchto objektů).

---

## Ignorování polí

10. **Ignorování jednoho pole**  
    **Scénář:**
    - Vytvoříme objekt, který má několik polí, z nichž jedno (např. `timestamp`) přidáme do seznamu `ignoredFieldPaths`.  
      **Test:**
    - Porovnáme dva objekty, kde se liší pouze hodnota ignorovaného pole.  
      **Očekávaný výsledek:**
    - areEquals vrátí `true`, protože rozdílné hodnoty u ignorovaného pole nejsou brány v úvahu.

11. **Ignorování více polí**  
    **Scénář:**
    - Vytvoříme objekt s několika atributy a do `ignoredFieldPaths` přidáme více polí (např. `timestamp`, `id`, `metadata`).  
      **Test:**
    - Porovnáme dva objekty, kde se liší hodnoty u těchto ignorovaných polí, ale ostatní atributy jsou shodné.  
      **Očekávaný výsledek:**
    - areEquals vrátí `true`, protože rozdíly v ignorovaných polích se neporovnávají.

12. **Ignorování neexistujícího pole**  
    **Scénář:**
    - Do `ignoredFieldPaths` přidáme pole, které ve srovnávaných objektech neexistuje.  
      **Test:**
    - Porovnáme objekty se standardní strukturou bez daného pole.  
      **Očekávaný výsledek:**
    - areEquals funguje správně (nevyhazuje výjimku) a výsledek závisí pouze na existujících a porovnávaných atributech.

---

## Vlastní porovnání tříd

13. **Použití vlastního equals na konkrétní třídu**  
    **Scénář:**
    - Do `customEqualsClasses` přidáme konkrétní třídu (např. `SpecialClass`), která má vlastní implementaci `equals()`.  
      **Test:**
    - Porovnáme dvě instance `SpecialClass` s odlišným interním stavem, který ovlivňuje jejich vlastní equals.  
      **Očekávaný výsledek:**
    - areEquals využije metodu `equals()` třídy `SpecialClass` a vrátí výsledek dle její implementace.

14. **Použití vlastního equals na celý balíček**  
    **Scénář:**
    - Do `customEqualsClasses` přidáme název balíčku (např. `com.example.special`) tak, aby všechny třídy z tohoto balíčku používaly svůj vlastní `equals()`.  
      **Test:**
    - Porovnáme dvě instance tříd z tohoto balíčku s odlišnou implementací equals.  
      **Očekávaný výsledek:**
    - areEquals využije vlastní implementace `equals()` u všech tříd z uvedeného balíčku a výsledek bude záviset na těchto implementacích.

---

## Porovnání kolekcí

15. **Porovnání kolekcí jako celků**  
    **Scénář:**
    - Vytvoříme dva objekty obsahující kolekce (např. seznamy), jejichž obsah je shodný, ale interní stav kolekcí (např. počet modifikací) se liší.  
      **Test:**
    - Nastavíme `compareCollectionsAsWhole = true` a porovnáme objekty.  
      **Očekávaný výsledek:**
    - areEquals vyhodnotí kolekce jako celek (pouze podle obsahu) a vrátí `true`.

16. **Porovnání kolekcí položku po položce**  
    **Scénář:**
    - Vytvoříme dva objekty obsahující kolekce se stejným počtem položek, ale lišící se na úrovni jednotlivých položek.  
      **Test:**
    - Nastavíme `compareCollectionsAsWhole = false` a porovnáme objekty.  
      **Očekávaný výsledek:**
    - areEquals porovná kolekce jako objekty (položka po položce) a vrátí `false`, pokud se některá položka liší.

17. **Porovnání prázdných kolekcí**  
    **Scénář:**
    - Vytvoříme dva objekty, kde atribut obsahující kolekci je prázdný seznam nebo jiná prázdná kolekce.  
      **Test:**
    - Porovnáme objekty pomocí areEquals.  
      **Očekávaný výsledek:**
    - Prázdné kolekce jsou považovány za shodné a areEquals vrátí `true`.

---

## Ladění a výstupy

18. **Zapnutí debug režimu**  
    **Scénář:**
    - Nastavíme `debugEnabled = true`.  
      **Test:**
    - Provedeme sadu porovnání a sledujeme výstup ladících informací (např. logy, stopy porovnávání).  
      **Očekávaný výsledek:**
    - Ladící informace jsou zobrazeny, což pomáhá sledovat postup porovnávání.

19. **Vypnutí debug režimu**  
    **Scénář:**
    - Nastavíme `debugEnabled = false`.  
      **Test:**
    - Provedeme sadu porovnání a ověříme, že se žádné ladící informace nezobrazují.  
      **Očekávaný výsledek:**
    - Debug režim je vypnutý a žádné dodatečné ladící informace se neobjeví.

---

## Různé scénáře porovnání

20. **Porovnání objektů s různými třídami**  
    **Scénář:**
    - Vytvoříme dva objekty patřící do odlišných tříd, např. `ClassA` a `ClassB`, i když mají některé podobné atributy.  
      **Test:**
    - Porovnáme objekty pomocí areEquals.  
      **Očekávaný výsledek:**
    - areEquals vrátí `false`, jelikož objekty patří do různých tříd.

21. **Porovnání dvou `null` hodnot**  
    **Scénář:**
    - Porovnáme dvě `null` reference.  
      **Test:**
    - Použijeme areEquals na dvě hodnoty `null`.  
      **Očekávaný výsledek:**
    - Výsledek je `true` (dva `null` jsou shodné).

22. **Porovnání `null` a ne-null hodnoty**  
    **Scénář:**
    - Porovnáme objekt s platnou hodnotou a `null`.  
      **Test:**
    - Použijeme areEquals na hodnoty, kde jedna je `null` a druhá není.  
      **Očekávaný výsledek:**
    - areEquals vrátí `false`.

23. **Porovnání objektů s cyklickými odkazy**  
    **Scénář:**
    - Vytvoříme objekty, které obsahují cyklické odkazy (např. objekt A odkazuje na objekt B a B zpět na A).  
      **Test:**
    - Porovnáme tyto objekty pomocí areEquals, aby nedošlo k nekonečné rekurzi.  
      **Očekávaný výsledek:**
    - areEquals správně detekuje cykly a porovnání proběhne bez zacyklení; výsledek je určen shodou relevantních atributů.

24. **Porovnání složitě zanořených struktur**  
    **Scénář:**
    - Vytvoříme složitě zanořenou strukturu obsahující více úrovní vnořených objektů a kolekcí.  
      **Test:**
    - Porovnáme tyto struktury pomocí areEquals s ohledem na nastavení hloubky (`maxComparisonDepth`).  
      **Očekávaný výsledek:**
    - Výsledek porovnání odpovídá konfiguraci hloubky – pokud je nastaveno omezení, rozdíly pod touto úrovní budou ignorovány, jinak bude zohledněna celá struktura.

---

## Kombinace nastavení

25. **Kombinace dědičnosti, hloubky porovnání a vlastního equals**  
    **Scénář:**
    - Použijeme dědičnost (s potomky obsahujícími vlastní atributy), nastavíme `compareInheritedFields = true`, `maxComparisonDepth = 3` a `useStandardEqualsAfterDepth = true`, a zároveň přidáme konkrétní třídu do `customEqualsClasses`.  
      **Test:**
    - Porovnáme objekty, kde jsou rodičovské atributy shodné, ale potomkové atributy se liší.  
      **Očekávaný výsledek:**
    - areEquals vyhodnotí objekty jako shodné díky shodě rodičovských atributů; po dosažení definované hloubky se použije standardní equals pro hlubší úrovně a pro specifikovanou třídu její vlastní equals.

26. **Kombinace ignorování polí a porovnání kolekcí jako celků**  
    **Scénář:**
    - Do konfigurace přidáme více polí do `ignoredFieldPaths` a nastavíme `compareCollectionsAsWhole = true`.  
      **Test:**
    - Porovnáme objekty, kde se liší hodnoty pouze u ignorovaných polí a vnitřní stav kolekcí (např. počet modifikací) se liší, ale obsah kolekcí je stejný.  
      **Očekávaný výsledek:**
    - areEquals vrátí `true`, protože rozdíly v ignorovaných polích a interních detailech kolekcí nejsou brány v úvahu.

27. **Kombinace limitované hloubky a položkového porovnání kolekcí**  
    **Scénář:**
    - Nastavíme `maxComparisonDepth = 1` a `compareCollectionsAsWhole = false`.  
      **Test:**
    - Porovnáme objekty obsahující kolekce, kde se hodnotí položka po položce, ale pouze do maximální úrovně zanoření.  
      **Očekávaný výsledek:**
    - areEquals porovná kolekce detailně, avšak vnořené rozdíly pod úrovní 1 budou ignorovány, což může vést k shodě, pokud se liší pouze hlubší struktury.

28. **Kombinace dědičnosti a ignorování polí**  
    **Scénář:**
    - Nastavíme `compareInheritedFields = false` a do `ignoredFieldPaths` přidáme pole z rodičovské i potomkových tříd.  
      **Test:**
    - Porovnáme objekty, kde rozdíly se vyskytují v ignorovaných polích i v porovnávaných polích.  
      **Očekávaný výsledek:**
    - Porovnání bude brát v potaz pouze ta pole, která nejsou ignorována; pokud jsou tyto pole shodná, areEquals vrátí `true`, jinak `false`.

29. **Komplexní kombinace nastavení**  
    **Scénář:**
    - Nastavíme:
        - `compareInheritedFields = true`
        - `maxComparisonDepth = 2`
        - `useStandardEqualsAfterDepth = true`
        - `compareCollectionsAsWhole = false`
        - Přidáme konkrétní třídy i balíčky do `customEqualsClasses`
        - Nastavíme několik položek v `ignoredFieldPaths`  
          **Test:**
    - Porovnáme objekty, které obsahují dědičnost, zanořené struktury, kolekce a ignorované atributy.  
      **Očekávaný výsledek:**
    - areEquals porovná objekty s ohledem na dědičnost (pouze rodičovské atributy jsou brány v úvahu), po dosažení hloubky 2 se použije standardní equals pro zanořené struktury, kolekce se porovnají položku po položce a ignorované atributy se vynechají. Výsledek bude záviset na shodě všech relevantních (neignorovaných) atributů.

---

---

---

# Výkonostní testy

---

## Porovnání velkých objektů

### Scénář A: Objekty s velkou šířkou (mnoho atributů)
**Popis:**
- Vytvoříme dva objekty se stejnou strukturou, kde bude například několik polí s velkým počtem prvků .
- Atributy budou jednoduchého typu (čísla, řetězce apod.).

**Test:**
- Provedeme areEquals na těchto objektech a změříme dobu porovnání.

**Očekávaný výsledek:**
- Doba porovnání bude úměrná počtu atributů, tedy rostoucí lineárně

---

### Scénář B: Objekty s velkou hloubkou zanoření
**Popis:**
- Vytvoříme strukturu, kde každý objekt obsahuje jeden nebo více zanořených objektů, přičemž počet zanoření může dosáhnout např. 50 úrovní.
- Rozdíly mohou být detekovány pouze na poslední úrovni nebo zcela chybět.

**Test:**
- Provedeme areEquals s různým nastavením `maxComparisonDepth` (např. -1 vs. 10 vs. 50) a změříme dobu porovnání.

**Očekávaný výsledek:**
- Při nastavení `maxComparisonDepth = -1` (neomezená hloubka) bude doba porovnání výrazně vyšší.
- Při omezené hloubce (např. `maxComparisonDepth = 10`) se rozdíly v hlubších úrovních nebudou porovnávat, což povede k rychlejšímu vyhodnocení, ale potenciálně i k přehlédnutí rozdílů.

---

### Scénář C: Kombinace velké šířky a hloubky
**Popis:**
- Vytvoříme komplexní objekty, kde na každé úrovni (např. 10 úrovní zanoření) obsahuje objekt velký počet polí
- Tato kombinace simuluje reálné, komplexní datové struktury.

**Test:**
- Spustíme areEquals a zaznamenáme dobu porovnání.
- Testujeme jak s nastavením `maxComparisonDepth = -1`, tak s omezenou hloubkou.

**Očekávaný výsledek:**
- Výkon bude výrazně ovlivněn jak počtem zanořených úrovní, tak počtem atributů na každé úrovni.
- Omezením hloubky se může zrychlit porovnání, pokud rozdíly v hlubších úrovních nejsou pro výsledek kritické.

---

### Scénář D: Vliv ignorovaných polí a vlastních equals metod
**Popis:**
- Vytvoříme velké objekty s mnoha atributy, z nichž některé budou přidány do `ignoredFieldPaths`.
- Některé objekty budou obsahovat atributy, které mají vlastní implementaci `equals()` a jsou zahrnuty v `customEqualsClasses`.

**Test:**
- Provedeme areEquals na objektech s a bez ignorovaných polí a s využitím vlastních equals metod.
- Změříme dobu porovnání a analyzujeme, jaký vliv má každé nastavení na výkon.

**Očekávaný výsledek:**
- Ignorování polí sníží počet porovnávaných atributů, což by mělo zrychlit celý proces.
- Použití vlastních equals metod může mít dvojí efekt – pokud jsou implementace optimalizované, mohou zrychlit porovnání, ale pokud jsou složité, může dojít k prodloužení času porovnávání.



### Scénář E: Porovnání kolekcí 
**Popis:**
- Vytvoříme objekty obsahující kolekce s různým počtem prvků.
- Nastavíme `compareCollectionsAsWhole = true` a `compareCollectionsAsWhole = false`.
- Porovnáme objekty s různými kolekcemi (shodný obsah, různý počet prvků, různé prvky).
- Měříme dobu porovnání.
- 
**Test:**
- Provedeme areEquals na objektech s různými kolekcemi a změříme dobu porovnání.
- Porovnáme výsledky s nastavením `compareCollectionsAsWhole = true` a `compareCollectionsAsWhole = false`.
- Testujeme různé scénáře s kolekcemi (prázdné, s jedním prvkem, s různým počtem prvků).

**Očekávaný výsledek:**
- Porovnání kolekcí jako celků by mělo být rychlejší než položka po položce, pokud jsou kolekce shodné.

### Scénář F: Porovnání Extrémní hloubky
**Popis:**
- Vytvoříme objekty s extrémní hloubkou zanoření, například reprezentované binárním stromem s hloubkou 20.
- Algoritmus areEquals projde celou strukturu, tedy až do poslední úrovně.

**Test:**
- Spustíme areEquals na objektech s extrémní hloubkou a změříme dobu porovnání.

**Očekávaný výsledek:**
- Čas porovnání by měl zůstat v rozumných mezích, pokud je implementace optimalizovaná.
- U extrémně zanořených struktur (např. hloubka 20) může dojít ke zvýšení výpočetního času, ale správná optimalizace (detekce cyklů, omezení rekurze, případně přepnutí na iterativní přístup) zajistí, že výsledná doba nebude nepřijatelná.
- Pokud optimalizace chybí, může dojít k výraznému prodloužení doby porovnání, což by bylo problém – proto je důležité testovat a optimalizovat implementaci.


### Scénář G: Porovnání objektů pomocí areEquals a standardního equals
**Popis:**
- Vytvoříme objekty, které mají implementovaný standardní equals a zároveň použijeme areEquals.
- Mají nějakou hloubku zanoření a obsahují několik atributů.

**Test:**
- Provedeme porovnání objektů pomocí areEquals a standardního equals.
- Změříme dobu porovnání a porovnáme výsledky.
- Testujeme různé scénáře s objekty (shodné, s rozdíly, s různými atributy).

**Očekávaný výsledek:**
- areEquals by mělo být rychlejší než standardní equals, pokud jsou objekty shodné.
- Testujeme co je rychlejší, porovnání pomocí areEquals nebo standardního equals.
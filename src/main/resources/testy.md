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

### Scénář A: Objekty s velkou šířkou (mnoho atributů)
**Popis:**
- Vytvoříme dva objekty se stejnou strukturou, kde bude například několik polí s velkým počtem prvků (100, 1000, 10 000, 100 000, 1 000 000).
- Atributy budou jednoduchého typu (čísla, řetězce apod.).

**Test:**
- Provedeme areEquals na těchto objektech a změříme dobu porovnání.

**Očekávaný výsledek:**
- Doba porovnání bude úměrná počtu atributů, tedy rostoucí lineárně

---

### Scénář B: Objekty s velkou hloubkou zanoření
**Popis:**
- Vytvoříme linkovou strukturu objektů, kde každý objekt obsahuje odkaz na další objekt.
- Tato struktura bude mít velkou hloubku zanoření (100, 1000, 10 000, 100 000, 1 000 000).

**Test:**
- Spustíme areEquals na těchto objektech a změříme dobu porovnání.

**Očekávaný výsledek:**
- Doba porovnání bude úměrná hloubce zanoření, tedy rostoucí lineárně s počtem úrovní.

---

### Scénář C: Kombinace velké šířky a hloubky
**Popis:**
- Vytvoříme komplexní objekty, které budou obsahovat mnoho atributů a zároveň budou mít velkou hloubku zanoření (100, 500, 1000, 2000, 5000).
- Tato kombinace simuluje reálné, komplexní datové struktury.
- Pokud bude objekt mít například 1000 atributů a 1000 úrovní zanoření, bude mít celkem 1 000 000 prvků.

**Test:**
- Spustíme areEqual a zaznamenáme dobu porovnání.

**Očekávaný výsledek:**
- V poměru s počtem atributů by se doba porovnání měla zvyšovat lineárně.

---

### Scénář D: Vliv ignorování atributů
**Popis:**
- Vytvoříme objekt, který má několik atributů, z nichž některé budou ignorovány (např. `ignoredField`).
- V jednom případě bude ignorované pole komplexní objekt (reference na jiný objekt), v druhém případě bude ignorované pole jednoduchého typu (např. `string`).
- Objekt se bude rozšiřovat pouze o počet elementů v kolekcích (100, 1000, 10 000, 100 000, 1 000 000).

**Test:**
- Provedeme areEqual na objektech s a bez ignorovaných polí.
- Změříme dobu porovnání a analyzujeme, jaký vliv má každé nastavení na výkon.

**Očekávaný výsledek:**
- V případě ignorování komplexního objektu by mělo být porovnání rychlejší než bez ignorování z důvodu režie, která je spojena s hledáním, zda-li je pole ignorováno.
- Pokud je ignorováno pole jednoduchého typu, je pravděpodobné, že doba spojená s režií, která hledá, zda-li je pole ignorováno, bude vyšší než doba porovnání samotného pole, což by mělo vést k delší době porovnání než bez ignorování.


### Scénář E: Porovnání kolekcí 
**Popis:**
- Vytvoříme objekty obsahující kolekce s různým počtem prvků (100, 1000, 10 000, 100 000, 1 000 000).
- Nastavíme `compareCollectionsAsWhole = true` a `compareCollectionsAsWhole = false`.
- Testovaná kolekce je SET, z důvodu neseřaditelnosti prvků.

**Test:**
- Provedeme areEqual na objektu s kolekcí a změříme dobu porovnání.
- Porovnáme výsledky s nastavením `compareCollectionsAsWhole = true` a `compareCollectionsAsWhole = false`.

**Očekávaný výsledek:**
- Porovnání kolekcí jako celků by mělo být rychlejší než položka po položce, pokud jsou kolekce shodné, z důvodu rekurzivního porovnávání a hledání shody v položkách.

### Scénář F: Porovnání rozsáhlé struktury
**Popis:**
- Vytvoříme binární stromovou strukturu, kde každý uzel obsahuje hodnotu a odkazy na levého a pravého potomka
- Hloubka stromu bude (5, 10, 15, 20, 25).
- Algoritmus areEqual projde celou strukturu, tedy až do poslední úrovně.

**Test:**
- Spustíme areEqual na stromové struktuře a změříme dobu porovnání.

**Očekávaný výsledek:**
- V závislosti na počtu prvků ve stromech by se doba porovnání měla zvyšovat lineárně.

### Scénář G: Porovnání areEqual a standardní metody equals
**Popis:**
- Vytvoříme objekty, které mají implementovaný standardní equals a zároveň použijeme areEqual.
- Mají nějakou hloubku zanoření a obsahují několik atributů.

**Test:**
- Provedeme porovnání objektů pomocí areEqual a standardního equals.
- Změříme dobu porovnání a porovnáme výsledky.
- Testujeme různé scénáře s objekty (shodné, s rozdíly, s různými atributy).

**Očekávaný výsledek:**
- equals by mělo být rychlejší než areEqual, pokud jsou objekty shodné, z důvodu použití reflexe a dalších operecí, které areEqual provádí.
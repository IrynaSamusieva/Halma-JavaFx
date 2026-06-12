# Halma-JavaFx

PRAVIDLÁ HRY

Halma alebo čínske dáma je hra pre 2 až 6 hráčov.
Cieľom je premiestniť všetky svoje kamene do cieľového rohu v opačnej časti hracej plochy skôr, ako to urobia súperi.
Tento cieľový roh sa nazýva "domov".
Postup hry:
Hráči sa striedajú v ťahoch, pričom v jednom ťahu môžu:
Presunúť jeden kameň o jedno políčko v ľubovoľnom smere na susedné voľné miesto,
Preskakovať cez jeden alebo viac vlastných alebo súperových kameňov v jednom alebo viacerých po sebe idúcich skokoch.

Dôležité obmedzenie:
Hráč nemôže kombinovať skok a jednoduchý krok v jednom ťahu – každý ťah pozostáva len z jedného typu pohybu (buď len posun, alebo len skoky)

GAME IMPLEMENTATION
1. Class Hole
   Reprezentuje jedno pole (jamku) na hracej doske.
   Uchováva:
   Circle – grafický prvok JavaFX.
   Koordináty v kubickom formáte (x, y, z).
   Stav poľa (FREE alebo OCCUPIED).
   Zoznam susedných polí.
   Reaguje na kliknutia myšou: ak je pole voľné, umožní presun vybranej figúrky.

2. Class Piece
   Reprezentuje hernú figúrku.
   Dedi Circle, takže sa priamo vykresľuje na doske.
   Uchováva:
   Farbu hráča.
   Aktuálne pole (Hole), na ktorom stojí.
   Poskytuje logiku pohybu:
   Bežné ťahy do voľných susedných polí.
   Skoky cez obsadené polia (rekurzívne).
   Reaguje na kliknutia: výber/odznačenie figúrky, zvýraznenie.
   Kontroluje podmienky víťazstva po presune.

3. Class BoardController
   Hlavný kontrolér hracej dosky.
   Zodpovedá za:
   Inicializáciu hry (initialize()).
   Vytvorenie polí z FXML (createHole()).
   Načítanie počiatočných pozícií z Coord.chc (startBoard()).
   Vytváranie figúrok (createPiece()).
   Správu poradia ťahov (turnOrder, switchTurn()).
   Kontrolu víťazstva (checkWinner()).
   Resetovanie zvýraznenia (resetColorAllGroup, resetColorHoles).

MCTS IMPLEMENTATION

MCTS je heuristický vyhľadávací algoritmus, ktorý sa používa v hrách s rozsiahlym stavovým priestorom.
Metóda MCTS nevyžaduje úplné vyhodnotenie všetkých možností – simuluje náhodné hry a vytvára rozhodovací strom,
pričom sa postupne zameriava na sľubné vetvy.

1.Class Node

Uchováva stav a prepojenie s nadradeným uzlom.
Kopíruje všetky povolené ťahy.
Vytvorí nový stav a podradený uzol, vráti nový uzol. Ak sú všetky možné ťahy pridané do stromu, je už úplne rozvinutý. Realizuje vzorec UCT. Rieši kľúčový problém: ako vybrať, ktorý uzol ďalej preskúmať – ten, ktorý už vykazuje dobré výsledky (využívanie), alebo ten, ktorý sme ešte takmer nevyskúšali (výskum).
ako vyzerá vzorec na obrázku:



<img width="810" height="328" alt="image" src="https://github.com/user-attachments/assets/97e17038-7d26-4eb2-9fc2-849502bda363" />



2.Class MonteCarloPlayer

Je stanovený časový limit (v kóde 2 sekundy, podľa potreby môže byť aj dlhší).
Vytvoríme koreňový uzol (aktuálny stav)
Kým nevyprší čas (2 sekundy):
Selection → vyberieme perspektívny list (select(root))
Expansion → ak list nie je terminálny, rozbalíme ho (expand())
Simulation → odohráme náhodnú partiu z tohto stavu
Backpropagation → aktualizujeme štatistiky predkov
Po uplynutí času vyberieme ťah s najlepšou winrate spomedzi potomkov koreňa

3.Class GameState

Aby mohol MCTS fungovať, potrebuje niekoľko vecí:
1. Prehľad o stave (šachovnica, na kom je ťah)
2. Možnosť získať všetky povolené ťahy
3. Možnosť vykonať ťah a získať nový stav
4. Vyhodnotenie konečného stavu (kto vyhral)
5. Heuristické vyhodnotenie (pre simulácie)
   GameState poskytuje všetko toto


Screen of gameplay:


  <img width="1239" height="1060" alt="image" src="https://github.com/user-attachments/assets/eb11ab0c-1601-4ad3-a815-5efb3a6b2b4a" />




Biela figúrka je tá figúrka, ktorá bola vybraná a bude sa pohybovať.

Kľúčové koncepty

Kubické koordináty: používajú sa na hexagonálnu mriežku pre výpočet susedov.
Každý kruh v súbore Board.fxml má svoje vlastné ID, ktoré sa zhoduje s jeho súradnicami. V súbore Coord.chc
sú súradnice kruhov každého tímu, ktoré potom slúžia ako základ pre implementáciu hernej logiky.

Base Camps: počiatočné a cieľové zóny pre každý tím.

FXML: Vizualizácia hernej dosky.

Zhrnutie:

Implementácia spája JavaFX UI (Circle, AnchorPane, Group) s hernou logikou (Hole, Piece, BoardController)
a implementáciou počítačového hráča pomocou MCTS (GameState, MonteCarloPlayer,Node).
Výsledkom je plne hrateľná verzia Halma s pravidlami pohybu, skokmi, správou ťahov a kontrolou víťazstva.






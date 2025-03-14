Structura fișierelor din folderul Game:

Game.Cards:

Card.java - Definiția unei cărți, incluzând atribute precum numele, atacul, apărarea și alte caracteristici speciale. Conține metode pentru modificarea atributelor cărții și aplicarea efectelor specifice.

Deck.java - Gestionarea unui pachet de cărți, incluzând funcții pentru adăugarea, eliminarea și amestecarea cărților. Permite extragerea cărților în timpul jocului.

Game.Change:

Actions.java - Implementarea acțiunilor disponibile în joc, precum atacul, utilizarea abilităților speciale și schimbarea turei.

GameMaster.java - Controlul principal al jocului, gestionând progresul și regulile. Coordonează interacțiunile dintre jucători și mecanicile jocului.

Mechanics.java - Implementarea mecanismelor specifice pentru efectele și interacțiunile dintre cărți. Conține logica pentru aplicarea abilităților speciale ale cărților.

PlayerCommand.java - Gestionarea comenzilor introduse de jucători, interpretând și aplicând acțiunile acestora în joc.

Game.Players:

Player.java - Reprezentarea unui jucător, incluzând mana, cărțile din mână și cele de pe teren. Conține metode pentru utilizarea cărților și gestionarea resurselor.

Error.java - Definirea erorilor și gestionarea mesajelor de eroare, incluzând scenarii precum mana insuficientă sau acțiuni invalide.

Utilizare

Aceste fișiere definesc logica principală a jocului și sunt utilizate pentru a controla interacțiunile dintre jucători, cărți și regulile jocului.
Fiecare componentă contribuie la desfășurarea jocului, asigurând respectarea regulilor și gestionarea resurselor fiecărui jucător, fiind asemănăor în proporție de 80% cu jocul Hearthstone.

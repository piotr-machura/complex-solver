### Programowanie obiektowe | Etap "Realease Candidate" projektu

_Piotr Machura, Kacper Ledwosiński_

# Kalkulator rozwiązujący i rysujący funkcje zespolone (***Complex solver***)
## Skrót specyfikacji

Celem projektu jest stworzenie kalkulatora rysującego oraz znajdującego miejsca zerowe funkcji zespolonych. Do znalezienia miejsc zerowych wykorzystywany jest **indeks punktu względem krzywej**, tj. ilość obrotów wykonywanych przez wektor wodzący zaczepiony w 0+0i *output space* podczas jednego okrążenia prostokąta $R$ w *input space*. Wykres rysowany jest  metodą **kolorowania dziedziny**.

## Dokonane postępy

Klasa `Rectangle` została zmieniona na klasę `Solver` dostępną jako `static` i zwracającą sformatowaną `ArrayList`ę znalezionych rozwiązań. Dodatkowo klasa `Solver` posiada teraz trzy ustawienia dokładności: `LOW`, `MED` i `HIGH`, które zwracają rozwiązania z dokładnością do (kolejno) 4, 5 oraz 6 cyfr po przecinku.

Do klasy `Complex` dodano interfejs `Comperable` pozwalający na sortowanie listy rozwiązań.

Napisano 90 `JUnitTest`'ów, z czego 21 testów sprawdza działanie klasy `Parser` a 69 działanie klasy `Solver` (dawniej `Rectangle`). Program przechodzi wszystkie napisane testy, których wyniki zostały sprawdzone w programie Woflram Mathematica.

Dodano możliwość zapisywania wyników działania programu (wykresów do pliku `.png` oraz znalezionych miejsc zerowych do pliku `.txt`.)

Zaimplementowano singleton `Help`, który w przyszłości zawierać będzie artykuły opisujące działanie programu (póki co wszystkie artykuły wypełnione są tymczasowym tekstem z rodziny _lorem ipsum_).

Zaimplementowano prototyp klasy `GraphicSolver`, której zadaniem będzie odtworzyć animację symulującą działanie programu.

Napotkano problem z rozwiązywaniem funkcji wymiernych, z powodów matematycznych jeśli w jednym kwadracie znajdą się miejsce zerowe i biegun, wtedy _winding number_ takiego kwadrata wynosi zero i algorytm "gubi" miejsca zerowe. Tymczasowe rozwiązanie polega na podzieleniu startowego kwadrata na wiele mniejszych, co (__zazwyczaj__) sprawia, że miejsce zerowe i biegun nie występują w jednym kwadracie.






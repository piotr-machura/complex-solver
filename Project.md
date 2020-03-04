# Program rozwiązujący funkcje zespolone

_Piotr Machura, Kacper Ledwosiński_

## Cel główny

Celem projektu jest stworzenie kalkulatora znajdującego miejsca zerowe funkcji zespolonych zmiennej zespolonej. Użytkownik za pomocą interfejsu graficznego podaje funkcję, z której zostaje utworzony obiekt `Function` który zawiera listę rozwiązań `solutions`. Następnie algorytm matematyczny wykorzystujący **indeks punktu względem krzywej** _(patrz sekcja "Podłoże matematyczne")_ oraz **rekurencyjną bisekcję** na płaszczyźnie zespolonej aby znaleźć przybliżone miejsca zerowe `Function` i umieścić je w `solutions`, skąd będą mogły zostać **wyświetlone na ekran**.

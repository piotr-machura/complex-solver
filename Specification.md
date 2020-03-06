# Program rozwiązujący funkcje zespolone

_Piotr Machura, Kacper Ledwosiński_

## Cel główny

Celem projektu jest stworzenie kalkulatora znajdującego miejsca zerowe funkcji zespolonych zmiennej zespolonej. Użytkownik za pomocą interfejsu graficznego podaje funkcję, z której zostaje utworzony obiekt `Function` zawierający listę rozwiązań `solutions`. Następnie algorytm matematyczny wykorzystujący indeks punktu względem krzywej _(en. "winding number", patrz sekcja "Podłoże matematyczne")_ oraz rekurencyjną bisekcję na płaszczyźnie zespolonej **znajduje przybliżone miejsca zerowe** `Function` i umieszcza je w `solutions`, skąd mogą zostać **wyświetlone na ekran**.

Dodatkowo kalkulator wykorzystuje metodę kolorowania dziedziny aby wyświetlić obszary _input space_ i _output space_ funkcji _(patrz sekcja "podłoże matematyczne)_

## Podłoże matematyczne

Rozważmy funkcję $f:D\sub\mathbb{C}\rightarrow E\sub\mathbb{C}$ (obszary $D$ i $E$ nazywane będą dalej zamiennie z _"input space"_ i _"output space"_) o miejscu zerowym $f(z_0) = 0$. Rozważmy również prostokąt $R$ znajdujący się w $D$ oraz punkt $z_t$ "wędrujący" w czasie po tym prostokącie.

Wtedy ___winding number___ $W$ prostokatą $R$ będzie __liczbą obrotów__ wykonanych przez wektor wodzący obrazu $f(z_t)$ wędrujacego po $E$ podczas jednego okrążenia punktu $z_t$ wokół $R$. Przyjmuje zatem jedynie wartości całkowite oraz zero.

Formalna definicja indeksu punku (miejsca zerowego $z_0$) względem krzywej ($R$) mówi:
$$
W(R, z_0) = \frac{1}{2\pi i} \oint _R \frac{dz}{z-z_0}
$$
Możemy zatem wywnioskować, że jeśli wewnątrz prostokata $R$ __znajduje się miejsce zerowe__, to $W(R)$ będzie __niezerowe__, tj. wektor wodzący $f(z_t)$ wędrującego po _output space_ wykonał niezerową ilosć obrotów (zmiana kąta wektora wodzącego w kierunku zgodnym z ruchem wskazówek zegara jest traktowana jako ujemna). Natomiast jeśli wewnątrz $R$ nie znajduje się miejsce zerowe, to jego _winding number_ będzie równy zero.

_(dodać rysunek)_

Algorytm znajdujący miejsca zerowe zaczyna więć od sprawdzenia $W$ prostokąta $R_0$ wystarczająco dużego, aby znalazło się w nim miejsce zerowe. Następnie dzieli go na cztery mniejsze i sprawdza $W$ każdego z nich. Jeśli $W$ któregoś z mniejszych prostokątów jest zerowe, to zostaje odrzucony (wewnątrz nie ma miejsca zerowego), jeśli jest niezerowe zostaje on podzielony na cztery mniejsze itd. _(metoda [quad tree](https://en.wikipedia.org/wiki/Quadtree))_

![Wizualizacja modelu quad tree](https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Point_quadtree.svg/600px-Point_quadtree.svg.png)


Algorytm zatrzymuje się, gdy prostokąty zawierajace miejsca zerowe są ___wystarczająco małe___, po czym zwraca ich środki jako przybliżone miejsca zerowe.

## Interfejs użytkownika

## Cele dodatkowe

- Użycie systemu kontroli wersji `git` - KONIECZNE
- 


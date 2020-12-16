# Generator ewolucji

## Wprowadzenie

Projekt ma na celu nauczyć podejścia obiektowego przy tworzenia aplikacji w języku Java.

## Cel projektu
Stwórzmy grę! Nie będzie to jednak gra, w którą my gramy. Zamiast tego będzie to świat, który ewoluuje na naszych oczach! Stworzymy środowisko stepów i dżungli ze zwierzętami, które biegają, buszują w zaroślach, jedzą i rozmnażają się. A po kilka milionach lat zobaczymy, że wyewoluowały w różne gatunki!

Świat naszej gry jest niezmiernie prosty. Składa się ze zwykłej, prostokątnej połaci, której brzegi zawijają się na drugą stronę. Większość świata pokrywają stepy, na których rośnie niewiele roślin stanowiących pożywienie zwierząt. Na środku świata znajduje się niewielka dżungla, gdzie rośliny rosną dużo szybciej.


## Technologie
* Java
* JavaFx
* Libki...

## Uruchomienie
Do uruchomienia potrzebne jest oprogramowanie java 15.

## Przykłady użycia
...

## Status projektu

#### Nowa mechanika:
- [ ] 1 Wykonaj ruchy zwierząt (mogą być kolizje)
  - [ ] zapisz pozycje ```grass + animal```
  - [ ] zapisz pozycje ```multi animal```
- [ ] 2 Przydziel energię za trawę i ją usuń (pozycje ```grass + animal```)
- [ ] 3 Usuń zwierzęta, których ```lifeEnergy <= 0``` ("przenieś je do statystk")
- [ ] 4 Zrób dzieci (pozycje ```multi animal```)
- [ ] 5 Dodaj trawę

#### Mechanika mapy:
- [x] Tury - zwierzę może ruszyć się tylko raz w ciągu dnia
- [x] Generator codziennego dodawania roślin
  - [x] 1 na ```sawannie``` (jeśli pozycja naley do stepu to ponownie doaj)
  - [x] 1 na ```stepie```
- [x] Poziom życia zwierząt
  - [x] Każdego dnia zwierze traci określoną ilość ```energii``` za poruszanie się po mapie
- [ ] Rozmnażanie i umieranie zwierzaków
  - [ ] Dzieci dostają 2 częsćigenów od rodziców i 1 w zależności od obszaru w którym się urodzą
  - [x] umieranie gdy ``` energia <= 0```
- [x] Generator ruchów zwierząt
- [ ] w klasie cell przechowujemy info o pozycjac z zwierzętami...

#### Zwierzęta:
- [x] Genetyka zwierząt
- [ ] Rodzice

##### Wygeneruj mapę:
- [x] ```step``` jako prostokąt ```a * b```
- [x] ```sawanna``` na środku ma rozmiar proporcjonalny do mapy ``` jungleRatio * (a * b)```
- [x] Unikaj kolizji przy pierwszym dodawaiu zwierząt - zmnień pozycję rośliny
- [x] Trawa pojawia się w turach
- [x] 8 obrotów (stron świata)
- [x] Zawija się na krańcach dla 8 kierunków świata
- [ ] Dane wejściowe są ładowane z pliku parameters.json znajdującego się w katalogu, w których uruchamiana jest aplikacja.
- [x] ilość energii początkowej zwierząt ```(startEnergy)```
- [x] ilość energii traconej w każdym dniu ```(moveEnergy)```
- [x] ilość energii zyskiwanej przy zjedzeniu rośliny ```(plantEnergy)```
- [ ] Wiele zwierząt może znajdować się na jednej pozycji
- [x] Gdy dżungla jest pełna to nie dodajemy nowych roślin
- [x] Genertor losowych początkowych pozycji zwierząt
    - [x] losowe i unikalne pozycje
    - [x] losowy kierunek
    
- [ ] Minimalna energia potrzebna do rozmnożenia to połowa energii początkowej zwierzęcia, tzn. 50% ```startEnergy```
- [ ] gdy para zwierząt się rozmnaża, a wszystkie pozycje dookoła są zajęte to potomek ląduje na losowo zajętym kwadracie

#### Engine:
- [ ] Testy
- [ ] Symulacja
- [ ] Statystyki - wykres
  - [ ] liczby wszystkich zwierząt,
  - [ ]  liczby wszystkich roślin,
  - [ ]  dominujących genotypów,
  - [ ]  średniego poziomu energii dla żyjących zwierząt,
  - [ ]  średniej długości życia zwierząt dla martwych zwierząt,
  - [ ]  średniej liczby dzieci dla żyjących zwierząt.
- [ ] Program musi umożliwiać zatrzymywanie oraz wznawianie animacji w dowolnym momencie
- [ ] Po zatrzymaniu programu można:
  - [ ] wskazać pojedyncze zwierzę, co powoduje wyświetlenie jego genomu,
  - [ ] wskazać pojedyncze zwierzę, w celu śledzenia jego historii:
    - [ ] określenia liczby wszystkich dzieci, po n-epokach,
    - [ ] określenia liczby wszystkich potomków, po n-epokach,
    - [ ] określenia epoki, w której zmarło,
  - [ ] wskazać wszystkie zwierzęta z dominującym genomem.
- [ ] Program ma umożliwić wyświetlenie symulacji jednocześnie na dwóch mapach, z identycznymi parametrami początkowymi, lecz niezależnie losowanymi decyzjami.    
- [ ] Program powinien umożliwiać uzyskanie statystyki (jak w punkcie 4) po określonej liczbie epok w formie pliku tekstowego. Statystyki powinny stanowić uśrednienie wartości z poszczególnych epok.

#### Optymalizacja:
- [ ] w przypadku gy zapełnimy 60% mapy to patrzymy na "wolne pozycje" w placeGrass
- [ ] problem wielowątkowości albo rysujemy albo obliczamy - synchronizacja/ przekaywanie inforami pomiędzy wątkami
- [ ] zwierze posiada obliczone prawdopodobieństwo dla każdego z kierunków

#### Grafika:
- [ ] java fx
   - [ ] Program ma wyświetlać animację pokazującą pozycję zwierząt
   - [ ] Program ma wyświetlać energię zwierzą (np. w formie koloru)
   - [ ] Program ma wyświetlać rośliny
- [ ] (opcjonalnie swing)

Opcjonalinie: Gdy zwierze jest otoczone przez rośliny to uruchamiamy "rozglądanie"
 zwierze traci energie podczas rozglądania


# Źródła:
```https://github.com/apohllo/obiektowe-lab/tree/master/lab8```
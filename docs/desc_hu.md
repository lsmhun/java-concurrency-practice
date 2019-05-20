# Java párhuzamosság 

!!! UNDER CONSTURCTION!!!

Haladó interjúk során sokszor rákérdeznek a concurrency témakörére. 
Pár jellemző kérdéskört így összeszedtem magamnak, pontos és szabatos 
kifejtés nélkül. 

## Dead lock
Klasszikus kérdés, mi a deadlock, hogy lehet megtalálni és elkerülni. 
A deadlock egy olyan nem kívánt helyzet, amikor az egyik szál vár a másikra, 
miközben a másik szál is vár az egyikre. Így mindkettő blokkolva van. 
Feloldani legkönnyebben a sorrend helyes kialakításával lehet. 
[Deadlock](https://howtodoinjava.com/java/multi-threading/writing-a-deadlock-and-resolving-in-java/)
 
Megvizsgálásához a legegyszerűbb módszer, hogy egy full stack trace kérhetünk, 
majd betöltve a jVisualVM-be megvizsgálhatjuk az aktuális állapotot. 
Ezt a lenti példákkal is meg ki lehet próbálni. ( Ehhez néhány link: 
 [DZone](https://dzone.com/articles/how-analyze-java-thread-dumps) ,
 [JavaWorld](https://www.javaworld.com/article/2073601/ten-tips-for-using-java-stack-traces.html)
 ) 
- [DeadLock example](../src/main/java/hu/lsm/concurrency/practice/DeadLock.java)
- [DeadLock test](../src/test/java/hu/lsm/concurrency/practice/DeadLockTest.java)

## Atomic, volatile
Újabb örök kérdés, mi a volatile, mire jó, mire nem. A volatile kulcsszóval 
definiáljuk, hogy ezt az értéket több szálból is el szeretnénk érni, ezért ne legyen 
a stacken cache-elve a számítások során. A volatile biztosítja a láthatóságot, de az 
atomi működést nem. Azaz az biztos, hogy a szál ugyanazt az értéket látja, de 
módosításnál előfordulhat, hogy ugyanarra módosul két külön szál tevékenyége folytán. 
Ehhez jön még, hogy volatile nélkül a long értékeket két ciklus alatt módosítja a processzor, 
ami szintén potenciális probléma forrás lehet. 
  
A fenti problémakörre alakították ki az [AtomicInteger](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html), 
AtomicLong és társaikat, amik pont ezt a problémát hivatottak orvosolni, jó teljesítmény mellett.
  
A lenti tesztekben egy sima számlálón prezentálhatóak a fenti gondok. Előbb egy minden 
védelem nélküli példát mutatok, aztán egy volatile féle nem atomic művelet példát. 
Természetesen a syncronized megoldással ezt el lehet kerülni, de az nagyon lassú lesz, 
amint a futáskor ki is van írva. Végül egy AtomicInteger-t használó, jóval gyorsabb 
szálbiztos megoldás szerepel.
 
- [Volatile and atomic example](../src/main/java/hu/lsm/concurrency/practice/AtomicAndVolatileProblem.java)
- [Volatile and atomic test](../src/test/java/hu/lsm/concurrency/practice/AtomicAndVolatileProblemTest.java)


## Lock és ConcurrentHashMap
Javaban a két legismertebb Lock megoldás a  [ReentrantLock](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html)
és a  [ReentrantReadWriteLock](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html). 
Az első egy sima egyszerű lock, a második pedig két lockot tart karban,
egyet az írásra egyet az olvasásra. 

A példákban egy belső Map van kivezetve, hogy szálbiztosan 
működjön. Három féle képpen van megvalósítva, és a futási időkben,
illetve a futás alatti lekérdezések számából látható a teljesítmény
különbség.

A harmadik példa, bár nem szorosan, de ide kívánkozik. A [ConcurrentHashMap](https://www.baeldung.com/java-concurrent-map) ugyanis 
a lock striping technikát használja, melynek lényege, hogy belül nem az 
egész map-et, csak egyes bucketokat lockol le, és ezzel sokkal jobb 
teljesítményt biztosít.

- [Lock example](../src/main/java/hu/lsm/concurrency/practice/ReentrantExample.java)
- [Lock test](../src/test/java/hu/lsm/concurrency/practice/ReentrantExampleTest.java)

## Barrier
Ez kevésbé népszerű téma. Ha több szál különböző tevékenységeket végez,
akkor időnként össze kell várják egymást. Ekkor tesz jó szolgálatot a 
CyclicBarrier, ahol megadható, hogy hány szálat is kell összevárni. Ez 
újrafelhasználható, lehetséges .reset() hívás rajta.

A CountDownLatch hasonló funkcionalitást lát el, de ott nem lehetséges 
resetelni az állapotát. Például induláskor egyes kritikus service-eknek 
el kell indulnia, akkor lehet ezt alkalmazni.

Ezúttal egy közös számlálót adok át a szálaknak, amik véletlen hosszan 
alszanak, de azért összevárják egymást.

A példában látható egy problémás eset is, ha összevárás előtt reseteljük
a barriert, akkor a bent ragadt szálak dobnak BrokenBarrierException-t.

- [Cyclic barrier example](../src/main/java/hu/lsm/concurrency/practice/CyclicBarrierExample.java)
- [Cyclic barrier test](../src/test/java/hu/lsm/concurrency/practice/CyclicBarrierExampleTest.java)


## Future

A Future megoldás a Java 7-től szerepel, nagyon sok minden épít rá, legismertebb
példákat a Map-Reduce kapcsán szokták emlegetni. Lényegében egy hosszan futó folyamatnál
mondhatjuk azt, hogy ez majd a jövőben el fog készülni, de addig is folytatható 
a futás a fő szálon.

Ennek kapcsán érdemes ránézni az ExecutorService megvalósításokra (Executors.newSingleThreadExecutor() és
Executors.newFixedThreadPool(threadNumber)), valamint a ForkJoinPool példákra is.
Ez utóbbihoz leírás [itt](https://allprogrammingtutorials.com/tutorials/printing-file-names-recrusively-in-directory.php) 
és a leírás mintája: [ForkJoinPool example](../src/main/java/hu/lsm/concurrency/practice/PrintingFilesRecursivelyInDirectory.java)

Ebben a mintában négyzetet számolunk, és altatjuk a szálat egy rövidebb ideig. A
fő szálban az .isDone() lekérdezésével figyelhetjük a választ. Itt jól meg lehet 
figyelni, hogy mennyivel kevesebbet kell várni, mikor két független szál dolgozik.

A Java8-as Optional sok szempontból kényelmesebb és átgondoltabb, de a Future is 
számos feladatra megfelelő, és a Thread/Runnable pároshoz képest sokkal magasabb
szinten kezelhető.

- [Future example](../src/main/java/hu/lsm/concurrency/practice/FutureExample.java)
- [Future test](../src/test/java/hu/lsm/concurrency/practice/FutureExampleTest.java)
- [ForkJoinPool example](../src/main/java/hu/lsm/concurrency/practice/PrintingFilesRecursivelyInDirectory.java)

## Functional interface

A Functional interface megoldás a Java8 legfontosabb újdonsága, a lamda függvények enélkül 
nem is működhetnek. A functional interfaceben elméletileg egy metódus van csak, ami abstract.
Ebben a cikkben az alapokat jól mutatják: [Java8 functional interfaces](https://www.baeldung.com/java-8-functional-interfaces)

Legfontosabb a Function<T, R> , ahol az első a bemenő típus, a második a visszatérési típust 
adja meg. A [mintákban](../src/test/java/hu/lsm/concurrency/practice/FunctionalInterfaceExampleTest.java) pár
jellemző esetet jártam körbe, de ez a téma sokkal bővebb.

Néhány cikk a témában:
[geeks4geeks](https://www.geeksforgeeks.org/function-interface-in-java-with-examples/),
[Medium](https://medium.com/@gelopfalcon/best-practices-when-you-use-lambda-expressions-in-java-f51e96d44b25),
[dZone](https://dzone.com/articles/functional-programming-java-8)

A következő pár apró példa csak egy-egy esetet mutat be. Érdekes viszont ez a kis minta, 
ami két függvényből egy harmadikat alkot, ezzel megvalósítva a funkcionális paradigmát 
Java környezetben.

```java
// BinaryOperator<Function<Integer,Integer>> compose = (f, g) -> x -> g.apply(f.apply(x));
Function<Integer,Integer> composedFunction = functionEx.compose.apply(functionEx.add1, functionEx.add1);
```

- [Functional interface](../src/main/java/hu/lsm/concurrency/practice/FunctionalInterfaceExample.java)
- [Functional interface test](../src/test/java/hu/lsm/concurrency/practice/FunctionalInterfaceExampleTest.java)

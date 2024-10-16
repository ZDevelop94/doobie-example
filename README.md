Was confirming if `IO.parTraverseN(3)(staff)(member => repo.insertData(member._1, member._2, member._3).transact(xa)).void`
is faster than
```
val staffIOs = staff.map(repo.insertData).map(_.transact(xa))
staffIOs.parSequenceN(3)
```
### <h2>hypothesis:<h2>

The first code example is faster because the cats library has an optimised algorithm that analyses the Functor at the same time as the current position of the collection
where as `.map` `.map` has to loop through and munipulate the collection 3 times including the `parSequence`

### <h2>Result:<h2>
result is that the code using `parTraverseN` (first code example) was faster as I created a recursive function that repeated the side effect with x amount of elements (look at code). 
This then gave me the average time taken (with reliable results) without running the program multiple times; as this brings into the factor cpu thread alocation, spinup time and other
process running on my machine between intevals of program execution.

There is only several milisecond difference but this is crucial when intense systems dealing with millions of datapoints have to use the best solution to reduce latency. 

Run it yourself if you wish just don't forget to uncomment and comment out the different sections of code. 


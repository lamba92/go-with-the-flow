# Go with the Flow!

Hello! This is a repository intended for a talk of mine with the same title. Here you can find the code used during the live coding sessions, while the slides are available [here](https://docs.google.com/presentation/d/1FgH0GK5BzbwUwsVYLlxphhkWlkP6E_gr3fspa2rzOJ0/edit?usp=sharing)!

## Description

The Kotlin coroutines library only offers a few basic operators for Flow compared to Rx libraries — that’s by design.

When you get a bit in the weeds of a piece of reactive code and reach for some familiar Rx operator like `groupBy` or `throttle`, you’ll see there are no built-in equivalents in Flow. No need to worry, though: if you find yourself needing more than what comes out of the box, Flow operators are much easier to write than you would expect.

During this session, we’ll see how Flow operators are made, and how we can implement even the most complex behaviors with a handful of lines by leveraging the power of coroutines. :kotlin-intensifies:
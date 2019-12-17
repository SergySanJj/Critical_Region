# Critical Region

This repository is a collection of methods for working with critical regions or testing of new methods


## Created by

- <a href="https://github.com/SergySanJj">Sergei Yarema</a>
- <a href="https://github.com/nikitosoleil">Mykyta Oliinyk</a>

## Implemented parts

- Lock interfaces
- Fix number lock interfaces
- Race condition demonstration
- Dekker lock
- Framework for mutual exclusion testing

## Demonstration and testing

Demonstration is implemented in a form of unit tests which can be found <a href="/CriticalRegion/src/test/java/criticalregion/">here</a>

## <a href="/CriticalRegion/src/main/java/criticalregion">Sources</a>

## Lock interfaces and abstractions

Following interfaces and abstractions are implemented:
- FixNumLock (i);
- FixNumLockN (a);
- BinaryLock (a);

## Lock concrete implementations

- DekkerLock
- FakeLock (for false case demonstration of mutual exclusion testing framework)

## Mutual exclusion testing

To create a new unit test, you have to extend ```MutualExclusionTest<LockClass>``` class by implementing methods:
- ```prephase ```
- ```lockAction```
- ```unlockAction```

such methods will be called during testing with ```providesMutualExclusion(LockClass testableLock)``` method
```

package com.github.jldelarbre.javaExperiments.observer;

public class Linker {

	public static void main(String[] args) {
		IObserverTotoEvents1 observer1 = new ObserverTotoEvents1();
		IObserverTotoEvents1Events2 observer2 = new ObserverTotoEvents1Events2();
		IObserverTotoEvents3 observer3 = new ObserverTotoEvents3();

		IToto toto = Toto.build();
		toto.manageObserverTotoEvents1().addObserver(observer1);
		toto.addObserver(observer2);
		toto.manageObserverTotoEvents3().addObserver(observer3);

		toto.f();
	}
}

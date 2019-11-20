package com.github.jldelarbre.javaExperiments.observer;

public class Linker {

	public static void main(String[] args) {
		ITotoEvents1Observer observer1 = new TotoEvents1Observer();
		ITotoEvents1Events2Observer observer2 = new TotoEvents1Events2Observer();
		ITotoEvents3Observer observer3 = new TotoEvents3Observer();

		IToto toto = Toto.build();
		toto.manageObserverTotoEvents1().addObserver(observer1);
		toto.addObserver(observer2);
		toto.manageObserverTotoEvents3().addObserver(observer3);

		toto.f();
	}
}

package com.github.jldelarbre.javaExperiments.observer;

public class ObserverTotoEvents1Events2 implements IObserverTotoEvents1Events2 {

	public static class InnerObserverImpl implements IObservableTotosEvents1Events2 {
		@Override
		public void eventCommon() {
			System.out.println("ObserverTotoEvents1Events2.eventCommon()");
		}

		@Override
		public void eventYoupla1() {
			System.out.println("ObserverTotoEvents1Events2.eventYoupla1()");
		}

		@Override
		public void eventYouplaBoom1(String someParam) {
			System.out.println("ObserverTotoEvents1Events2.eventYouplaBoom1(" + someParam + ")");
		}

		@Override
		public void eventYoupla2() {
			System.out.println("ObserverTotoEvents1Events2.eventYoupla2()");
		}

		@Override
		public void eventYouplaBoom2(String someParam) {
			System.out.println("ObserverTotoEvents1Events2.eventYouplaBoom2(" + someParam + ")");
		}
	}

	@Override
	public IObservableTotosEvents1Events2 process() {
		return new InnerObserverImpl();
	}

	@Override
	public Class<IObservableTotosEvents1Events2> getObservablesEventsType() {
		return IObservableTotosEvents1Events2.class;
	}
}
